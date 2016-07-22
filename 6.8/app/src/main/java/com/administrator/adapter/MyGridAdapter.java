package com.administrator.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.elwj.R;
import com.administrator.minterface.GetServiceWhere;

/**
 * 服务页面apdater
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams
 */
public class MyGridAdapter extends BaseAdapter {
	private Context mContext;

	public String[] img_text = { "积分商城","社区大舞台","金融管家","精致生活","健康养生" };
	public int[] imgs = {R.mipmap.fw_01,R.mipmap.fw_02,R.mipmap.fw_03,R.mipmap.fw_04,R.mipmap.yangsheng};

	public MyGridAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return img_text.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder_Service vh;
		if (convertView == null) {
			vh=new ViewHolder_Service();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.gridview_item, parent, false);
			//vh.tv= (TextView) convertView.findViewById(R.id.tv_item);
		}
		((ImageView)convertView).setImageResource(imgs[position]);
		//vh.tv.setText(img_text[position]);
		return convertView;
	}
	public class ViewHolder_Service{
		ImageView iv;
		TextView tv;
	}


}
