package com.example.amusu.teamname_cst2335_final_project;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * this class is link to show the web interface
 * https://www.cnblogs.com/demongao/p/6229013.html
 * https://blog.csdn.net/qq_36990613/article/details/80774160
 * https://developer.android.com/guide/webapps/webview
 */

public class CbcDetailWeb extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_web);
        webView = findViewById(R.id.webView);
        String url = getIntent().getStringExtra("url");
        loadWeb(webView, url);
    }
    /**
     * Load URL method definition
     * @param web
     * @param url
     */

    public static void loadWeb(WebView web, String url) {

        web.loadUrl(url);
        web.setWebViewClient(new WebViewClient() {

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });

    }

}
