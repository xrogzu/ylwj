package com.administrator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.administrator.bean.VoteResult;
import com.administrator.elwj.R;

/**
 * 社区大舞台投票详情adapter
 * Created by My on 2016/4/14.
 */
public class ListAdapterVoteResult extends BaseAdapter {
    private Context context;
    private VoteResult voteResult;
    private int[] result;

    public ListAdapterVoteResult(Context context, VoteResult voteResult){
        this.context = context;
        this.voteResult = voteResult;
        if(voteResult != null){
            result = new int[voteResult.getOptionSize()];
            for (int i = 0;i<result.length;i++){
                if(i == 0){
                    result[i]=voteResult.getO1();
                }
                if(i == 1){
                    result[i]=voteResult.getO2();
                }
                if(i == 2){
                    result[i]=voteResult.getO3();
                }
                if(i == 3){
                    result[i]=voteResult.getO4();
                }
                if(i == 4){
                    result[i]=voteResult.getO5();
                }
                if(i == 5){
                    result[i]=voteResult.getO6();
                }
                if(i == 6){
                    result[i]=voteResult.getO7();
                }
                if(i == 7){
                    result[i]=voteResult.getO8();
                }
                if(i == 8){
                    result[i]=voteResult.getO9();
                }
            }
        }
    }
    @Override
    public int getCount() {
        if(voteResult != null){
            return voteResult.getOptionSize();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_bigstage_vote_result_item,null);
            viewHolder = new ViewHolder();
            viewHolder.optionText = (TextView) convertView.findViewById(R.id.vote_result_item_option);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            viewHolder.choiceNum = (TextView) convertView.findViewById(R.id.vote_result_item_num);
            viewHolder.choicePer = (TextView) convertView.findViewById(R.id.vote_result_item_per);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.optionText.setText(voteResult.getOptionsArray()[position]);
        viewHolder.progressBar.setMax(voteResult.getCount());
        if(result != null && result.length > 0){
            viewHolder.progressBar.setProgress(result[position]);
            viewHolder.choiceNum.setText(result[position]+"");
            viewHolder.choicePer.setText((result[position] / voteResult.getCount()) * 100 +"%");
        }
        return convertView;
    }

    private class ViewHolder{
        TextView optionText;
        ProgressBar progressBar;
        TextView choiceNum;
        TextView choicePer;
    }
}
