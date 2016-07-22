package com.administrator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.library.listview.XListView;

/**
 * Created by Administrator on 2016/3/15.
 */
public class MyXListView extends XListView {


    public MyXListView(Context context) {
        super(context);
    }

    public MyXListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyXListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(ev);
    }
}
