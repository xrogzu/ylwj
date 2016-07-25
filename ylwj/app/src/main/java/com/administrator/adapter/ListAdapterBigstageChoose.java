package com.administrator.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.administrator.bean.Bean_Bigstage_List;
import com.administrator.elwj.BigStageNormalDetailsActivity;
import com.administrator.elwj.BigStageVoteDetailsActivity;
import com.administrator.elwj.R;

/**
 * 目前没有用到，可以删除
 * Created by acer on 2016/1/24.
 */
public class ListAdapterBigstageChoose extends BaseAdapter {

    public ListAdapterBigstageChoose(Context context){
    }

    public ListAdapterBigstageChoose(Context context,Bean_Bigstage_List bean) {
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder_BigStage {
        TextView tv_name;
        TextView bt_join;
        ImageView iv_head;
        TextView tv_location;
        TextView tv_tickets;
        TextView tv_NUM;
        TextView tv_personnum;
        TextView tv_starnum;
        TextView tv_imgnum;
        RatingBar ratingBar;
        ImageView iv_bigstage_listitem_time;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder_BigStage vh;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_bigstage, null);
//            vh = new ViewHolder_BigStage();
//            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_bigstage_listitem_name);
//            vh.bt_join = (TextView) convertView.findViewById(R.id.bt_bigstage_listitem_join);
//            vh.iv_head = (ImageView) convertView.findViewById(R.id.iv_bigstage_listitem_head);
//            vh.tv_location = (TextView) convertView.findViewById(R.id.tv_bigstage_listitem_location);
//            vh.tv_tickets = (TextView) convertView.findViewById(R.id.tv_bigstage_listitem_time);
//            vh.tv_NUM = (TextView) convertView.findViewById(R.id.tv_bigstage_listitem_going);
//            vh.tv_personnum = (TextView) convertView.findViewById(R.id.tv_bigstage_listitem_personnum);
//            vh.tv_starnum = (TextView) convertView.findViewById(R.id.tv_bigstage_listitem_starnum);
//            vh.tv_imgnum = (TextView) convertView.findViewById(R.id.tv_bigstage_listitem_imgnum);
//            vh.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingbar);
//            vh.iv_bigstage_listitem_time = (ImageView) convertView.findViewById(R.id.iv_bigstage_listitem_time);
//
//            convertView.setTag(vh);
//        } else {
//            vh = (ViewHolder_BigStage) convertView.getTag();
//        }
//        final int p = position;
//
//        vh.iv_bigstage_listitem_time.setImageResource(R.mipmap.icon_piao);
//        vh.bt_join.setText("立即投票");
//        vh.tv_tickets.setText("123票");
//        vh.tv_NUM.setText("NO."+(position+1));
//        vh.ratingBar.setRating(position);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (p == 1 || p == 0) {
//                    Intent intent = new Intent(context, BigStageNormalDetailsActivity.class);
//                    intent.putExtra("bean",bean);
//                    context.startActivity(intent);
//                } else if (p == 2) {
//                    Intent intent = new Intent(context, BigStageVoteDetailsActivity.class);
//                    context.startActivity(intent);
//                }
//            }
//        });

        return convertView;
    }
}
