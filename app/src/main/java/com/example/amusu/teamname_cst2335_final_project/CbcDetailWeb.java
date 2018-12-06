package com.example.amusu.teamname_cst2335_final_project;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CbcDetailWeb extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_web);
        webView = findViewById(R.id.webView);
        String url = getIntent().getStringExtra("url");
        String type = getIntent().getStringExtra("type");
        loadWeb(webView, url, type);
    }


    public static void loadWeb(WebView web, String url, String type) {
        WebSettings webSettings = web.getSettings();
        // set WebView property which can run the Javascript
        webSettings.setJavaScriptEnabled(true);
        // allow to access the file
        webSettings.setAllowFileAccess(true);
        // enable zoom in and out
        webSettings.setBuiltInZoomControls(true);
        // enable cache mode
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // enable DOM storage API
        webSettings.setDomStorageEnabled(true);

        //make it no zoom
        webSettings.setSupportZoom(true);


        // set screen overview
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDefaultTextEncodingName("UTF-8");
        if (type.equals("1")) {
            web.loadUrl(url);//
        } else {
            web.loadData(url, "text/html; charset=UTF-8", null);
        }

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
