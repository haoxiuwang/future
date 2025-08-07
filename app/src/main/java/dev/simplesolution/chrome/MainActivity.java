package dev.simplesolution.chrome;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.JsResult;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.webkit.WebChromeClient;
import android.os.Handler;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import dev.simplesolution.chrome.LocalMemory;
import dev.simplesolution.chrome.ButtonAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import java.util.List; 
import java.util.ArrayList; 
import android.widget.Button;
import android.view.View;
import java.util.Arrays; 
import android.widget.Toast;

public class MainActivity extends AppCompatActivity 
    implements ButtonAdapter.OnButtonClickListener{

    private WebView webView;
    private EditText addressBar;
    private ListView historyList;
    private ListView favoriteList;
    private Button btnCloseFavorite;
    private Button btnCloseHistory;
    private Button btnHistory;
    private Button btnFavorite;
    private LinearLayout favoritePopup;
    private LinearLayout historyPopup;
    private LocalMemory history;
    private LocalMemory favorite;
    private String[] urls = new String[15];
    private int position = 0;
    @Override
    public void onButtonClick(int position, String item, int memo){
        webView.loadUrl(item);
        // if(0==memo){
        //     historyPopup.setVisibility(View.GONE);
        //     return;
        // }        
        // favoritePopup.setVisibility(View.GONE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        history = new LocalMemory();
        favorite = new LocalMemory();        
        setContentView(R.layout.activity_main);
        addressBar = findViewById(R.id.addressBar);
        webView = findViewById(R.id.webView);
        btnHistory = findViewById(R.id.btnHistory);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnCloseHistory = findViewById(R.id.btnCloseHistory);
        btnCloseFavorite = findViewById(R.id.btnCloseFavorite);
        historyList = findViewById(R.id.historyList);
        favoriteList = findViewById(R.id.favoriteList);        
       
        btnCloseHistory.setOnClickListener(v ->{
            btnCloseHistory.setVisibility(View.GONE);
        });
        btnCloseFavorite.setOnClickListener(v ->{
            btnCloseFavorite.setVisibility(View.GONE);
        });
        BaseAdapter adapter_f = new ButtonAdapter(
            this,
            favorite,
            this,
            0              // 数据源
        );
        favoriteList.setAdapter(adapter_f);
        
            
        BaseAdapter adapter_h = new ButtonAdapter(
            this,
            history,
            this,
            0              // 数据源
        );
        historyList.setAdapter(adapter_h);
        
        btnHistory.setOnLongClickListener(v -> {            
            historyList.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "显示历史", Toast.LENGTH_SHORT).show();
            return true; // 返回true表示已消费事件
        });
        btnFavorite.setOnClickListener(v ->{
            String str = addressBar.getText().toString().trim();
            favorite.addRecord(str);
            adapter_f.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "收藏："+str, Toast.LENGTH_SHORT).show();
            
        });
        btnFavorite.setOnLongClickListener(v -> {
            
            favoriteList.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "显示收藏", Toast.LENGTH_SHORT).show();
            return true; // 返回true表示已消费事件
        });
        // 在 WebView 中打开链接
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                // 判断是否要外部打开（例如所有 http/https 链接）
                if (url.contains("bing")&&url.contains("dict")) {
                    // 使用 Intent 在外部浏览器打开
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true; // 拦截，不交给 WebView 处理
                }
                addressBar.setText(url);                
                return false; // 其他情况由 WebView 处理
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                history.addRecord(url);
                adapter_h.notifyDataSetChanged();
                String jsCode = """
                        (function() {
                          
                            // 添加查词按钮
                            if (!document.getElementById('dict-btn')) {
                                var btn = document.createElement('button');
                                btn.id = 'dict-btn';
                                btn.innerText = 'Bing Dict';
                                btn.style.position = 'fixed';
                                btn.style.bottom = '20px';
                                btn.style.right = '20px';
                                btn.style.left = '20px';
                                btn.style.zIndex = '9999';
                                btn.style.padding = '10px 15px';
                                btn.style.backgroundColor = '#0078D7';
                                btn.style.color = 'white';
                                btn.style.border = 'none';
                                btn.style.borderRadius = '8px';
                                btn.style.fontSize = '16px';
                                btn.style.display = 'flex';
                                btn.style.placeContent = 'center';
                                btn.style.placeItems = 'center';
                                document.body.appendChild(btn);
                            }
                            document.getElementById('dict-btn').onclick = function() {
                                var word = window.getSelection().toString().trim();
                                if (word.length > 0) {
                                    window.open('https://cn.bing.com/dict/search?q=' + encodeURIComponent(word), 'bingdict');
                                }
                            };
                          
                        })();
                        """;

                new Handler().postDelayed(() -> {
                    view.evaluateJavascript(jsCode, null);
                }, 500); // 延迟 500ms 再注入
                
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示")
                    .setMessage(message)
                    .setPositiveButton("确定", (dialog, which) -> result.confirm())
                    .setCancelable(false)
                    .create()
                    .show();
                return true;
            }
        });


        // 回车触发加载
        addressBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO || 
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                String url = addressBar.getText().toString().trim();

                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://" + url;
                }

                webView.loadUrl(url);
                return true;
            }
            return false;
        });

        // 默认加载主页
        webView.loadUrl("https://www.bing.com");
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack(); // WebView 内部后退
        } else {
            super.onBackPressed(); // 应用关闭或返回上一个 Activity
        }
    }
}
