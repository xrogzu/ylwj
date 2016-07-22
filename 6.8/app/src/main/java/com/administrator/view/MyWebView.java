package com.administrator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/3/14.
 */

    public class MyWebView extends WebView {
        public MyWebView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        public MyWebView(Context context) {
            super(context);
        }
        @Override
        public boolean onTouchEvent(MotionEvent event){
            requestDisallowInterceptTouchEvent(true);
            return super.onTouchEvent(event);
        }
    }

