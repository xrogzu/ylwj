package com.administrator.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.administrator.bean.Constant;
import com.administrator.elwj.R;
import com.administrator.myviews.ScrollSwipeRefreshLayout;

/**
 * 服务地图fragment
 * Created by acer on 2016/1/7.
 */
public class ServiceMapFragment extends BaseFragment {
    private boolean isNetRequestDone = false;//是否网络请求完成
    private ProgressDialog progressDialog;
    private WebView webView;
    private ProgressBar progressBar;
    private ScrollSwipeRefreshLayout swipeLayout;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_servicemap, null);
        webView = (WebView) mView.findViewById(R.id.webview_servicemap);
        webView.setFocusable(true);//这个和下面的这个命令必须要设置了，才能监听back事件。
        webView.setFocusableInTouchMode(true);
        webView.setOnKeyListener(backlistener);
        progressBar = (ProgressBar) mView.findViewById(R.id.progressbar_servicemap);
        swipeLayout = (ScrollSwipeRefreshLayout) mView.findViewById(R.id.swipe_container);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constant.PERMISSION_LOCATION);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constant.PERMISSION_LOCATION);
            }
        } else {
            initData(true);
        }
        return mView;
    }


    public void initData(boolean granted) {
        if (granted) {
            swipeLayout.setViewGroup(webView);
            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    //重新刷新页面
                    webView.loadUrl(Constant.SERVICEMAP);

                }
            });

            //设置WebView属性，能够执行Javascript脚本
            webView.getSettings().setJavaScriptEnabled(true);
            //设置请求定位
            webView.getSettings().setDatabaseEnabled(true);
            String dir = getActivity().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
            webView.getSettings().setGeolocationEnabled(true);
            webView.getSettings().setGeolocationDatabasePath(dir);
            webView.getSettings().setDomStorageEnabled(true);
            //加载需要显示的网页
            webView.loadUrl(Constant.SERVICEMAP);
            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if (progress == 100) {
                        progressBar.setVisibility(View.GONE);
                        swipeLayout.setRefreshing(false);
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    super.onProgressChanged(view, progress);
                }

                @Override
                public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                    callback.invoke(origin, true, false);
                    super.onGeolocationPermissionsShowPrompt(origin, callback);
                }
            });
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if(progressDialog != null){
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);
                    return true;
                }
            });
        }
    }

    public void showProgressDialog(){
//        if(!isNetRequestDone && getActivity() != null){
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getActivity().getString(R.string.waitNetRequest));
//            progressDialog.show();
//        }
    }
}
