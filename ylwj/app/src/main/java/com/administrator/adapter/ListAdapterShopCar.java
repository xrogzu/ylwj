package com.administrator.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.bean.Bean_Shopcarlist;
import com.administrator.bean.Constant;
import com.administrator.elwj.R;
import com.administrator.elwj.ShopCarActivity;
import com.administrator.minterface.changeAllprice;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 购物车adapter
 * Created by acer on 2016/1/30.
 */
public class ListAdapterShopCar extends BaseAdapter {

    private HashMap<Integer, Double> totalPrice = new HashMap<>();
    private String numStr = "1";
    private HashMap<Integer, Boolean> isSelected;
    private List<Bean_Shopcarlist.DataEntity.GoodslistEntity> lists = new ArrayList<>();
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private RequestQueue requestQueue;
    private Context context;
    private SharedPreferences sharedPreferences;
    private int pos;
    private ShopCarActivity mShopCarActivity;


    public static class MyHandler extends Handler {

        private WeakReference<ListAdapterShopCar> mListAdapterShopCar;

        public MyHandler(ListAdapterShopCar listAdapterShopCar) {
            mListAdapterShopCar = new WeakReference<ListAdapterShopCar>(listAdapterShopCar);
        }

        @Override
        public void handleMessage(Message msg) {
            ListAdapterShopCar listAdapterShopCar = mListAdapterShopCar.get();
            if (listAdapterShopCar != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.DELETE_ONE) {
                    try {
                        JSONObject object = new JSONObject(json);
                        int result = object.optInt("result");
                        if (result == 1) {
                            changeAllprice allprice = (changeAllprice) listAdapterShopCar.context;
                            allprice.change();
                            ToastUtil.showToast(listAdapterShopCar.context, "删除成功");
                            listAdapterShopCar.sharedPreferences.edit().remove(String.valueOf(listAdapterShopCar.lists.get(listAdapterShopCar.pos).getGoods_id())).commit();
                            VolleyUtils.NetUtils(listAdapterShopCar.requestQueue, Constant.baseUrl + Constant.getAllShopcar, null, null, listAdapterShopCar.handler, Constant.GET_ALLSHOPCAR);
                        } else {
                            ToastUtil.showToast(listAdapterShopCar.context, "删除失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (which == Constant.GET_ALLSHOPCAR) {
                    JSONObject obj;
                    try {
                        obj = new JSONObject(json);
                        JSONObject data = obj.getJSONObject("data");
                        int count = data.optInt("count");
                        if (count == 0) {
                            listAdapterShopCar.mShopCarActivity.setBt_payClickable(false);
                            listAdapterShopCar.mShopCarActivity.setPrice("￥0");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Gson gson = new Gson();
                    Bean_Shopcarlist bean = gson.fromJson(json, Bean_Shopcarlist.class);
                    listAdapterShopCar.addData(bean, listAdapterShopCar.context, listAdapterShopCar.requestQueue);
                }
            }
        }
    }

    private Handler handler = new MyHandler(this);

    public ListAdapterShopCar(ShopCarActivity activity, Context context, SharedPreferences carnum) {
        this.context = context;
        isSelected = new HashMap<Integer, Boolean>();
        sharedPreferences = carnum;
        mShopCarActivity = activity;
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        // 初始化数据
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < lists.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    /**
     * 显示总的商品价格
     */
    public void getTotalPrice() {
        double allPrice = 0;
        for (int i = 0; i < lists.size(); i++) {
            if (totalPrice.containsKey(i)) {
                double everyPrice = totalPrice.get(i);
                allPrice = allPrice + everyPrice;
            }
        }

        mShopCarActivity.setPrice("￥" + allPrice + "元");
    }

    /**
     * 会返回一个选择结果
     */
    public List<Bean_Shopcarlist.DataEntity.GoodslistEntity> getSelectedResult() {
        return lists;
    }

    public void addData(Bean_Shopcarlist bean_shopcarlist, Context context, RequestQueue requestQueue) {
        this.context = context;
        if (bean_shopcarlist != null) {
            lists = bean_shopcarlist.getData().getGoodslist();
            initDate();
        } else {
            lists.clear();
        }
        this.requestQueue = requestQueue;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (lists != null) {
            return lists.size();
        } else {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ShopCarViewHolder vh = null;
        if (convertView == null) {
            vh = new ShopCarViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopcar_listitem, null);
            vh.iv = (ImageView) convertView.findViewById(R.id.iv_shopcar_list);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_shopcar_list_name);
            vh.tv_num = (TextView) convertView.findViewById(R.id.tv_shopcar_list_num);
            vh.tv_price = (TextView) convertView.findViewById(R.id.tv_shopcar_list_price);
            vh.tv_delete = (TextView) convertView.findViewById(R.id.bt_shopcar_delete);
            vh.num = (TextView) convertView.findViewById(R.id.tv_num);
            vh.min = (Button) convertView.findViewById(R.id.ib_less);
            vh.add = (Button) convertView.findViewById(R.id.ib_add);
            vh.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
            convertView.setTag(vh);
        } else {
            vh = (ShopCarViewHolder) convertView.getTag();
        }
        imageLoader.displayImage(lists.get(position).getImage_default(), vh.iv, options);
        vh.tv_name.setText(lists.get(position).getName());
        vh.tv_price.setText("单价：￥" + lists.get(position).getPrice());
        vh.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cartid = Integer.parseInt(lists.get(position).getId());
                pos = position;
                VolleyUtils.NetUtils(requestQueue, Constant.baseUrl + Constant.deleOneGood, new String[]{"cartid"}, new String[]{cartid + ""}, handler, Constant.DELETE_ONE);
            }
        });
        int numShopCar = sharedPreferences.getInt(String.valueOf(lists.get(position).getGoods_id()), 0);
        if (numShopCar == 0) {
            vh.number = 1;
            vh.num.setText("1");
            vh.tv_num.setText("1");
            numShopCar = 1;
        } else {
            vh.number = numShopCar;
            vh.num.setText(String.valueOf(numShopCar));
            vh.tv_num.setText("数量：" + String.valueOf(numShopCar));
        }
        lists.get(position).setNum(String.format("%d",numShopCar));
        final ShopCarViewHolder finalVh = vh;
        totalPrice.put(position, Double.parseDouble(lists.get(position).getPrice()) * numShopCar);
        if (lists.size() == position + 1) {
            getTotalPrice();
        }
        vh.everyPrice = Double.parseDouble(lists.get(position).getPrice());
        vh.min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalVh.number = --finalVh.number;
                if (finalVh.number <= 1) {
                    finalVh.number = 1;
                    finalVh.num.setText(String.valueOf(finalVh.number));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(String.valueOf(lists.get(position).getGoods_id()), finalVh.number).commit();
                    finalVh.tv_num.setText("数量：" + String.valueOf(finalVh.number));
                    lists.get(position).setNum(String.format("%d", finalVh.number));
                    finalVh.everyPrice = Integer.valueOf(lists.get(position).getPrice()) * finalVh.number;
                    totalPrice.put(position, finalVh.everyPrice);
                    getTotalPrice();
                } else {
                    finalVh.num.setText(String.valueOf(finalVh.number));
                    finalVh.tv_num.setText("数量：" + String.valueOf(finalVh.number));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(String.valueOf(lists.get(position).getGoods_id()), finalVh.number).commit();
                    lists.get(position).setNum(String.format("%d", finalVh.number));
                    finalVh.everyPrice = Integer.valueOf(lists.get(position).getPrice()) * finalVh.number;
                    totalPrice.put(position, finalVh.everyPrice);
                    getTotalPrice();
                }
            }
        });
        vh.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalVh.number = ++finalVh.number;
                finalVh.num.setText(String.valueOf(finalVh.number));
                finalVh.tv_num.setText("数量：" + String.valueOf(finalVh.number));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(String.valueOf(lists.get(position).getGoods_id()), finalVh.number).commit();
                lists.get(position).setNum(String.format("%d", finalVh.number));
                finalVh.everyPrice = Integer.valueOf(lists.get(position).getPrice()) * finalVh.number;
                totalPrice.put(position, finalVh.everyPrice);
                getTotalPrice();
            }
        });

        // 根据isSelected来设置checkbox的选中状况
        vh.cb.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        isSelected = isSelected;
    }

    public class ShopCarViewHolder {
        ImageView iv;
        TextView tv_name;
        TextView tv_num;
        TextView tv_price;
        TextView tv_delete;
        TextView num;
        Button min;
        Button add;
        public CheckBox cb;
        int number;
        double everyPrice;
    }

    //获取数据
    public List<Bean_Shopcarlist.DataEntity.GoodslistEntity> getData(){
        return lists;
    }
}
