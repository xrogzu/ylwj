package com.administrator.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.administrator.bean.ActivityDetails;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 社区大舞台投票详情adapter
 * Created by My on 2016/4/12.
 */
public class ListAdapterVoteDetail extends BaseAdapter {
    private ActivityDetails.DataEntity bean;
    private Context context;
    private DisplayImageOptions optionhead;
    private ImageLoader imageLoader;
    private String[] options ;
    private String[] optionsSelected;
    private int optionNum;
    private Handler handler;

    public ListAdapterVoteDetail(ActivityDetails.DataEntity bean, Context context, ImageLoader imageLoader, BaseApplication appContext,Handler handler) {
        this.bean = bean;
        this.context = context;
        this.imageLoader = imageLoader;
        this.handler = handler;
        optionhead = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.touxiang)
                .showImageForEmptyUri(R.mipmap.touxiang)
                .showImageOnFail(R.mipmap.touxiang)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position == 0){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_big_stage_vote_details_one,null);
            ImageView userImageView = (ImageView) convertView.findViewById(R.id.bigstage_vote_details_user);
            TextView titleView = (TextView) convertView.findViewById(R.id.bigstage_vote_details_username);
            TextView numText = (TextView) convertView.findViewById(R.id.bigstage_vote_details_choicenum);
            imageLoader.displayImage(bean.getOrganizer_face(),userImageView,optionhead);
            titleView.setText("发起人："+bean.getOrganizer_name());
            options = bean.getOptions().split(";");
            RadioGroup optionRadioGroup = (RadioGroup) convertView.findViewById(R.id.bigstage_vote_details_option_rg);
            for (int i = 0;i<options.length;i++){
                RadioButton radioButton = new RadioButton(context);
                radioButton.setButtonDrawable(context.getResources().getDrawable(R.drawable.vote_type_choose_rb));
                radioButton.setText(options[i]);
                radioButton.setPadding(80,10,0,0);
                optionRadioGroup.addView(radioButton);
            }
            final View finalConvertView = convertView;
            optionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton tempButton = (RadioButton) finalConvertView.findViewById(checkedId);
                    String choice = tempButton.getText().toString();
                    Message message = handler.obtainMessage();
                    message.obj = choice;
                    message.what = 1000;//代表选择的选票项
                    handler.sendMessage(message);
                }
            });
        }
        if(position == 1){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_big_stage_vote_details_two,null);
            TextView contentView = (TextView) convertView.findViewById(R.id.bigstage_vote_details_content);
            contentView.setText(bean.getIntroduction());
        }
        return convertView;
    }

    private class VoteOptionsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(options != null){
                return options.length;
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
            return 0;
        }

        @Override
        public View getView(final int position, android.view.View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.vote_option_item,null);
                viewHolder = new ViewHolder();
                viewHolder.optionsName = (TextView) convertView.findViewById(R.id.vote_option_item_name);
                viewHolder.optionsCheckBox = (CheckBox) convertView.findViewById(R.id.vote_option_item_cb);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.optionsName.setText(options[position]);

            viewHolder.optionsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(optionsSelected == null){
                        optionsSelected = new String[options.length];
                    }
                    if(isChecked){
                        optionsSelected[position]=options[position];
                        optionNum++;
                    }else {
                        optionsSelected[position]="";
                        optionNum--;
                    }
                    Log.e("num",optionNum+"");
                    Log.e("num",optionsSelected.toString());
                }
            });
            return convertView;
        }
    }

    private class ViewHolder{
        TextView optionsName;
        CheckBox optionsCheckBox;
    }

}
