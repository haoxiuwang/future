package dev.simplesolution.chrome;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;


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
            public void onPageFinished(WebView view, String url) {
                view.evaluateJavascript("(function() { " +

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
                    "  btn.style.display = 'none';" +
                    "  document.body.appendChild(btn);" +
                    "}" +

                    // 监听选中文字
                    "document.addEventListener('selectionchange', function() {" +
                    "  var selected = window.getSelection().toString().trim();" +
                    "  var btn = document.getElementById('dict-btn');" +
                    "  if (selected.length > 0 && selected.split(/\\s+/).length === 1) {" +
                    "    btn.style.display = 'block';" +
                    "  } else {" +
                    "    btn.style.display = 'none';" +
                    "  }" +
                    "});" +

                    // 点击按钮跳转 Bing
                    "document.getElementById('dict-btn').onclick = function() {" +
                    "  var word = window.getSelection().toString().trim();" +
                    "  if (word.length > 0) {" +
                    "    window.location.href = 'https://cn.bing.com/dict/search?q=' + encodeURIComponent(word);" +
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
}
