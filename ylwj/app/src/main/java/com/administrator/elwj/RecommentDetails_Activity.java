package com.administrator.elwj;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.administrator.myviews.ScrollSwipeRefreshLayout;

//前人做的activity，目前没有用到，可以删掉
public class RecommentDetails_Activity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private ScrollSwipeRefreshLayout swipeRefreshLayout;
    private String url;
    private View.OnKeyListener backlistener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {

            if (i == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                webView.goBack();// 返回前一个页面
                return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomment_details_);
        webView = (WebView) findViewById(R.id.webview_recomment);
        webView.setFocusable(true);//这个和下面的这个命令必须要设置了，才能监听back事件。
        webView.setFocusableInTouchMode(true);
        webView.setOnKeyListener(backlistener);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_recomment);
        swipeRefreshLayout = (ScrollSwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(url);
            }
        });

        url = getIntent().getStringExtra("URL");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
    }
}
