package com.administrator.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2015/12/23.
 */
public class MainViewPager extends ViewPager {
    private boolean scrollble=false;

   public MainViewPager(Context context){
        super(context);
    }
    public MainViewPager(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {

            return false;
        }
        return super.onTouchEvent(ev);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!scrollble) {

            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }




    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }


}
