package com.administrator.myviews;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * @author:大龙君
 * @创建时间:2015-11-16下午7:31:50 说明：
 */
public class MyViewGroup extends ViewGroup {
	private Context context;
	private Paint paint;
	private int w;
	private int h;

	public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public MyViewGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public MyViewGroup(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	// 排列子view的方法
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// getChildCount()是获取子控件的方法
		int sumH = 0;
		int sumW = 0;
		
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			view.measure(0, 0);
			int height = view.getMeasuredHeight();
			int width = view.getMeasuredWidth();
			if ((w - sumW) < width) {
//				Log.i("test", "第"+i+"个"+w+"宽度"+sumW+"--"+width);
				sumW = 0;
				sumH += height+15;
				view.layout(sumW, sumH, sumW + width, sumH + height);
				sumW += width+25;
			} else {
				view.layout(sumW, sumH, sumW + width, sumH + height);
				sumW += width+25;
			}
		}
	}

	public void init() {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		w = getResources().getDisplayMetrics().widthPixels;
		h = wm.getDefaultDisplay().getHeight();
		paint = new Paint();

	}
	/**
	 * 
	 android获取屏幕的高度和宽度用到WindowManager这个类，两种方法：
	 * 
	 * 1、WindowManager wm = (WindowManager) getContext()
	 * .getSystemService(Context.WINDOW_SERVICE);
	 * 
	 * int width = wm.getDefaultDisplay().getWidth(); int height =
	 * wm.getDefaultDisplay().getHeight();
	 * 
	 * 2、WindowManager wm = this.getWindowManager();
	 * 
	 * int width = wm.getDefaultDisplay().getWidth(); int height =
	 * wm.getDefaultDisplay().getHeight();
	 * 
	 * 3.DisplayMetrics metric = new DisplayMetrics();
	 * getWindowManager().getDefaultDisplay().getMetrics(metric); int width =
	 * metric.widthPixels; // 屏幕宽度（像素） int height = metric.heightPixels; //
	 * 屏幕高度（像素） float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5） int
	 * densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
	 */

}
