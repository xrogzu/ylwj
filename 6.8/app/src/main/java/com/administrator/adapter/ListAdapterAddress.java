package com.administrator.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.bean.Addresses;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.EditAddressActivity;
import com.administrator.elwj.MyAddressActivity;
import com.administrator.elwj.R;
import com.administrator.fragment.AddressFragment;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 地址管理adapter
 * Created by xu on 2016/3/2.
 */
public class ListAdapterAddress extends BaseAdapter {

    //地址列表
    private List<Addresses.DataEntity.AddressListEntity> mData;
    private Activity mActivity;
    private BaseApplication application;
    private MyAddressActivity addressActivity;

    private static final int DEL = 0;
    private static final int SET_DEFAULT = 1;
    //当前操作的address id
    private int mCurID = -1;
    //界面类型
    private int mType;

    public static class MyHandler extends Handler {

        private WeakReference<ListAdapterAddress> mListAdapterAddress;

        public MyHandler(ListAdapterAddress listAdapterAddress) {
            mListAdapterAddress = new WeakReference<ListAdapterAddress>(listAdapterAddress);
        }

        @Override
        public void handleMessage(Message msg) {
            ListAdapterAddress listAdapterAddress = mListAdapterAddress.get();
            if (listAdapterAddress != null) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int result = jsonObject.getInt("result");
                    if (result == 1) {
                        switch (msg.what) {
                            case DEL:
                                for (int i = 0; i < listAdapterAddress.mData.size(); ++i) {
                                    if (listAdapterAddress.mData.get(i).getAddr_id() == listAdapterAddress.mCurID) {
                                        listAdapterAddress.mData.remove(i);
                                        listAdapterAddress.notifyDataSetChanged();
                                        ToastUtil.showToast(listAdapterAddress.mActivity, "删除成功");
                                        if (listAdapterAddress.mData.size() == 0) {
                                            listAdapterAddress.setAddBtnVisible(false);
                                        }
                                        break;
                                    }
                                }
                                break;
                            case SET_DEFAULT:
                                for (int i = 0; i < listAdapterAddress.mData.size(); ++i) {
                                    if (listAdapterAddress.mData.get(i).getAddr_id() == listAdapterAddress.mCurID) {
                                        listAdapterAddress.mData.get(i).setDef_addr(1);
                                    } else {
                                        listAdapterAddress.mData.get(i).setDef_addr(0);
                                    }
                                    ToastUtil.showToast(listAdapterAddress.mActivity, "设置默认地址成功");
//                                    Toast.makeText(listAdapterAddress.mActivity, "设置默认地址成功！", Toast.LENGTH_SHORT).show();
                                    listAdapterAddress.notifyDataSetChanged();
                                }
                                break;
                            default:
                                break;
                        }
                    } else {
                        if (msg.what == DEL)
                            ToastUtil.showToast(listAdapterAddress.mActivity, "删除失败：" + jsonObject.get("message"));
                        else if (msg.what == SET_DEFAULT)
                            ToastUtil.showToast(listAdapterAddress.mActivity, "设置失败：" + jsonObject.get("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Handler handler = new MyHandler(this);

    public ListAdapterAddress(BaseApplication application, Activity activity, int type) {
        mActivity = activity;
        this.application = application;
        mType = type;
        mData = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return mData.size();

    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (mType == AddressFragment.TYPE_SELECT_ADDRESS) {
            SelectViewHolder viewHolder = new SelectViewHolder();
            if (convertView == null) {
                viewHolder = new SelectViewHolder();
                convertView = RelativeLayout.inflate(mActivity, R.layout.select_address_item, null);
                viewHolder.tvAddr = (TextView) convertView.findViewById(R.id.tv_select_address_item_addr);
                viewHolder.tvRecPerson = (TextView) convertView.findViewById(R.id.tv_select_address_item_rec_person);
                viewHolder.tvDefault = (TextView) convertView.findViewById(R.id.tv_select_address_item_cur_default);
                viewHolder.ivDefault = (ImageView) convertView.findViewById(R.id.iv_select_address_item);
                viewHolder.tvMobile = (TextView) convertView.findViewById(R.id.tv_select_address_item_mobile);
                viewHolder.rlItem = (RelativeLayout) convertView.findViewById(R.id.rl_select_address_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (SelectViewHolder) convertView.getTag();
            }
            viewHolder.ivDefault.setVisibility(View.GONE);
            if (mData.get(position).getDef_addr() == 1) {
                viewHolder.tvDefault.setVisibility(View.VISIBLE);
                viewHolder.ivDefault.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvDefault.setVisibility(View.GONE);
                viewHolder.tvDefault.setVisibility(View.GONE);
            }

            viewHolder.tvMobile.setText(mData.get(position).getMobile());
            viewHolder.tvRecPerson.setText(mData.get(position).getName());
            viewHolder.tvAddr.setText(mData.get(position).getAddr());
            viewHolder.rlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("address", mData.get(position));
                    mActivity.setResult(Activity.RESULT_OK, intent);
                    mActivity.finish();
                }
            });
            convertView.setTag(viewHolder);
        } else {
            ManageViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ManageViewHolder();
                convertView = LinearLayout.inflate(mActivity, R.layout.manage_address_item, null);
                viewHolder.tvRecPerson = (TextView) convertView.findViewById(R.id.tv_manage_address_item_rec_person);
                viewHolder.tvAddr = (TextView) convertView.findViewById(R.id.tv_manage_address_item_rec_address);
                viewHolder.tvDefaultAddr = (TextView) convertView.findViewById(R.id.tv_manage_address_item_default_add);
                viewHolder.ibDel = (ImageButton) convertView.findViewById(R.id.ib_manage_address_item_del);
                viewHolder.ibEdit = (ImageButton) convertView.findViewById(R.id.ib_manage_address_item_edit);
                viewHolder.rbSetDefaultAddr = (RadioButton) convertView.findViewById(R.id.rb_manage_address_item_set_default_addr);
                viewHolder.phone = (TextView) convertView.findViewById(R.id.phone);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ManageViewHolder) convertView.getTag();
            }
            viewHolder.tvAddr.setText(mData.get(position).getAddr());//收货地址
            viewHolder.tvRecPerson.setText(mData.get(position).getName());//收货人
            viewHolder.phone.setText(mData.get(position).getMobile());
            viewHolder.rbSetDefaultAddr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && mData.get(position).getDef_addr() == 0) {
                        mCurID = mData.get(position).getAddr_id();
                        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.setDefaultAddress, new String[]{"addr_id"}, new String[]{String.format("%d", mData.get(position).getAddr_id())}, handler, SET_DEFAULT);
                    }
                }
            });
            if (mData.get(position).getDef_addr() == 1) {
                viewHolder.tvDefaultAddr.setVisibility(View.VISIBLE);
                viewHolder.rbSetDefaultAddr.setChecked(true);
            } else {
                viewHolder.tvDefaultAddr.setVisibility(View.GONE);
                viewHolder.rbSetDefaultAddr.setChecked(false);
            }
            viewHolder.ibEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, EditAddressActivity.class);
                    intent.putExtra("address", mData.get(position));
                    intent.putExtra("def_addr", mData.get(position).getDef_addr());
                    mActivity.startActivityForResult(intent, Constant.EDIT_ADDRESS_REQUEST_CODE);
                }
            });
            viewHolder.ibDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mActivity).setMessage("确定要删除该收货地址吗？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCurID = mData.get(position).getAddr_id();
                            VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.delRecAddress, new String[]{"addr_id"}, new String[]{String.format("%d", mData.get(position).getAddr_id())}, handler, DEL);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });

            convertView.setTag(viewHolder);
        }

        return convertView;
    }

    /**
     * 添加地址数据
     *
     * @param data
     */
    public void addData(List<Addresses.DataEntity.AddressListEntity> data) {
        if (data != null && data.size() > 0) {
            this.mData = data;
            notifyDataSetChanged();
            setAddBtnVisible(true);
        } else {
            setAddBtnVisible(false);
        }
    }

    /**
     * 添加地址数据
     */
    public void addData(Addresses.DataEntity.AddressListEntity data) {
        if (data != null) {
            mData.add(data);
            notifyDataSetChanged();
            setAddBtnVisible(true);
        } else {
            setAddBtnVisible(false);
        }

    }

    /**
     * 更新地址数据
     *
     * @param data
     */
    public void updateData(Addresses.DataEntity.AddressListEntity data) {
        if (data != null) {
            for (int i = 0; i < mData.size(); ++i) {
                if (mData.get(i).getAddr_id() == data.getAddr_id()) {
                    mData.remove(i);
                    mData.add(i, data);
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    /**
     * <p>为了设置Activity标题栏按钮的隐藏或出现</p>
     */
    private void setAddBtnVisible(boolean visible) {
        if (addressActivity == null) {
            addressActivity = (MyAddressActivity) mActivity;
        }
        addressActivity.setRightTopVisible(visible);
    }

    /**
     * 管理收货地址viewholder
     */
    public class ManageViewHolder {
        //收货人
        public TextView tvRecPerson;
        //收货地址
        public TextView tvAddr;
        //默认收货地址
        public TextView tvDefaultAddr;
        //设为默认收货地址
        public RadioButton rbSetDefaultAddr;
        //编辑地址
        public ImageButton ibEdit;
        //删除地址
        public ImageButton ibDel;
        public TextView phone;

    }

    /**
     * 选择收货地址viewHolder
     */
    public class SelectViewHolder {
        //收货人
        public TextView tvRecPerson;
        //收获电话
        public TextView tvMobile;
        //默认收货
        public TextView tvDefault;
        //收货地址
        public TextView tvAddr;
        //默认收货图标
        public ImageView ivDefault;
        //整个选项
        public RelativeLayout rlItem;
    }
}
