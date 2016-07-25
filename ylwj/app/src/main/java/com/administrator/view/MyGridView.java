package com.administrator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class MyGridView extends GridView {

    private OnTouchBlankListener mTouchBlankListener;
    private float mTouchX, mTouchY;
    private float mFinalTouchX, mFinalTouchY;
    private static final float BOUND = 12;

    public MyGridView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public interface OnTouchInvalidPositionListener {
        /**
         * motionEvent 可使用 MotionEvent.ACTION_DOWN 或者 MotionEvent.ACTION_UP等来按需要进行判断
         *
         * @return 是否要终止事件的路由
         */
        boolean onTouchInvalidPosition(int motionEvent);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getActionMasked();
        float x = e.getX();
        float y = e.getY();
        final int position = pointToPosition((int) x, (int) y);
        if (mTouchBlankListener != null && position == INVALID_POSITION) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mTouchX = x;
                    mTouchY = y;
//                    mTouchBlankListener.onTouchBlank(e);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mFinalTouchX = x;
                    mFinalTouchY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(mTouchX - mFinalTouchX) < BOUND && Math.abs(mTouchY - mFinalTouchY) < BOUND) {
                        mTouchBlankListener.onTouchBlank(e);
                    }
                    break;
            }
            if (action == MotionEvent.ACTION_UP) {
                return true;
            }
        }
        return super.onTouchEvent(e);
    }

    public interface OnTouchBlankListener {
        void onTouchBlank(MotionEvent e);
    }

    public void setOnTouchBlankListener(OnTouchBlankListener l) {
        mTouchBlankListener = l;
    }

}