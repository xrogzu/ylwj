package com.administrator.myviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class NumImageView extends ImageView {

    private TextPaint textPaint = null;
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        invalidate();
    }

    private Paint paint = null;

    public NumImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        textPaint = new TextPaint();
        paint = new Paint();
        paint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
        paint.setColor(0xffff0000);
        textPaint.setColor(0xffffffff);
        textPaint.setTextSize(30);
    }

    public NumImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public NumImageView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if (num > 0) {
            float w = textPaint.measureText("" + num);
            float h = textPaint.getFontMetrics().bottom * 3;
            if (num < 100) {
                int radius = getPaddingTop() > getPaddingRight() ? getPaddingRight()
                        : getPaddingTop();
                canvas.drawCircle(getWidth() - getPaddingRight(),
                        getPaddingTop(), radius, paint);
                canvas.drawText("" + num, getWidth() - getPaddingRight() - w
                        / 2, getPaddingTop() + h / 2, textPaint);
            } else {
                w = textPaint.measureText("" + 99);
                int radius = getPaddingTop() > getPaddingRight() ? getPaddingRight()
                        : getPaddingTop();
                canvas.drawCircle(getWidth() - getPaddingRight(),
                        getPaddingTop(), radius, paint);
                canvas.drawText("" + 99,
                        getWidth() - getPaddingRight() - w / 2, getPaddingTop()
                                + h / 2, textPaint);
            }
        }

    }

}
