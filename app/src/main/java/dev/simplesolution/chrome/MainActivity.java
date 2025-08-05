package dev.simplesolution.chrome;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.JsResult;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText addressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressBar = findViewById(R.id.addressBar);
        webView = findViewById(R.id.webView);

        // 在 WebView 中打开链接
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
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
    
            @Override
            public void onPageFinished(WebView view, String url) {
                view.evaluateJavascript("(function() { " +
                    "alert('启动完成')" +
                    // 添加查词按钮
                    "if (!document.getElementById('dict-btn')) {" +
                    "  var btn = document.createElement('button');" +
                    "  btn.id = 'dict-btn';" +
                    "  btn.innerText = 'Bing Dict';" +
                    "  btn.style.position = 'fixed';" +
                    "  btn.style.bottom = '20px';" +
                    "  btn.style.right = '20px';" +
                    "  btn.style.left = '20px';" +                    
                    "  btn.style.zIndex = '9999';" +
                    "  btn.style.padding = '10px 15px';" +
                    "  btn.style.backgroundColor = '#0078D7';" +
                    "  btn.style.color = 'white';" +
                    "  btn.style.border = 'none';" +
                    "  btn.style.borderRadius = '8px';" +
                    "  btn.style.fontSize = '16px';" +
                    "  btn.style.display = 'flex';" +
                    "  btn.style.placeContent = 'center';" +
                    "  btn.style.placeItems = 'center';" +
                    "  document.body.appendChild(btn);" +
                    "}" +
                    "document.getElementById('dict-btn').onclick = function() {" +
                    "  var word = window.getSelection().toString().trim();" +
                    "  if (word.length > 0) {" +
                    "    window.open('https://cn.bing.com/dict/search?q=' + encodeURIComponent(word),'bingdict')" +                    
                    "  }" +
                    "};" +

                "})()", null);
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
