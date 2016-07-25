package com.administrator.elwj;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.Addresses;
import com.administrator.bean.Constant;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.Utils;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * 编辑或者新增地址
 * 当传入intent有地址数据时为编辑地址模式
 * 否则为新增地址模式
 * Created by xu on 2016/3/2.
 */
public class EditAddressActivity extends AppCompatActivity implements View.OnClickListener {
    //姓名
    private EditText etName;
    //收货地址
    private EditText etAddr;
    //手机
    private EditText etPhone;
    //是否是新增地址，true为表示进入新增地址页面，false表示进入边界地址页面
    private boolean isAdd = true;
    //地址信息
    private Addresses.DataEntity.AddressListEntity mAddressEntity;
    //姓名
    private String mName;
    //地址
    private String mAddr;
    //电话
    private String mPhone;
    private int def_addr;

    //新增地址
    private static final int ADD = 1;
    //修改地址
    private static final int EDIT = 2;
    //获取区域
    private static final int GET_AREA = 3;

    public static class MyHandler extends Handler{

        private WeakReference<EditAddressActivity> mActivity;

        public MyHandler(EditAddressActivity addressActivity){
            mActivity = new WeakReference<EditAddressActivity>(addressActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            EditAddressActivity activity = mActivity.get();
            if (activity != null) {
                String json = (String) msg.obj;
                LogUtils.d("xu_test_address", json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int result = (int) jsonObject.get("result");
                    if (result == 1) {
                        if (msg.what == ADD)
                            ToastUtil.showToast(activity, "地址添加成功");
//                            Toast.makeText(activity, "地址添加成功!", Toast.LENGTH_SHORT).show();
                        else if (msg.what == EDIT)
                            ToastUtil.showToast(activity,"地址修改成功");
//                            Toast.makeText(activity, "地址修改成功!", Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String address = jsonObject1.getJSONObject("address").toString();
                        Gson gson = new Gson();
                        Addresses.DataEntity.AddressListEntity addressListEntity1 = gson.fromJson(address, Addresses.DataEntity.AddressListEntity.class);
                        addressListEntity1.setDef_addr(activity.def_addr);
                        Intent intent = new Intent();
                        intent.putExtra("address", addressListEntity1);
                        if (msg.what == ADD)
                            activity.setResult(RESULT_OK, intent);
                        else
                            activity.setResult(RESULT_OK, intent);
                        activity.finish();
                    } else {
                        if (msg.what == ADD)
                            ToastUtil.showToast(activity, "地址添加失败");
//                            Toast.makeText(activity, "地址添加失败!", Toast.LENGTH_SHORT).show();
                        else if (msg.what == EDIT)
                            ToastUtil.showToast(activity,"地址修改失败");
//                            Toast.makeText(activity, "地址修改失败!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Handler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        initViews();
    }

    private void initViews() {
        //标题
        TextView tvTitle = (TextView) findViewById(R.id.title);
        //取消按钮
        ImageButton ibCancel = (ImageButton) findViewById(R.id.ib_edit_address_cancel);
        TextView tvCommit = (TextView) findViewById(R.id.tv_edit_address_commit);
        etAddr = (EditText) findViewById(R.id.et_edit_address_addr);
        etName = (EditText) findViewById(R.id.et_edit_address_name);
        etPhone = (EditText) findViewById(R.id.et_edit_address_phone);
        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    checkMobile();
                }
            }
        });
        Spinner spArea = (Spinner) findViewById(R.id.sp_edit_address_area);
        ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_address_region);
        mSpinnerAdapter.add("山东省青岛市市南区");
        spArea.setAdapter(mSpinnerAdapter);

        Intent intent = getIntent();
        //如果有地址信息，表示进入的是编辑地址，否则是新增地址
        if (intent != null) {
            mAddressEntity = intent.getParcelableExtra("address");
            def_addr=intent.getIntExtra("def_addr",0);
            if (mAddressEntity != null) {
                isAdd = false;
                etAddr.setText(mAddressEntity.getAddr());
                etPhone.setText(mAddressEntity.getMobile());
                etName.setText(mAddressEntity.getName());
            }
        }

        if (isAdd) {
            tvTitle.setText("新增收货地址");
        } else {
            tvTitle.setText("编辑收货地址");
        }

        ibCancel.setOnClickListener(this);
        tvCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //提交
            case R.id.tv_edit_address_commit:
                if (checkInput()) {
                    String keys[] = null;
                    String values[] = null;
                    if (isAdd) {
                        keys = new String[]{"name", "province_id", "city_id", "region_id", "addr", "mobile","def_addr"};
                        values = new String[]{mName, "491", "492", "493", mAddr, mPhone,String.valueOf(def_addr)};
                        VolleyUtils.NetUtils(((BaseApplication) getApplication()).getRequestQueue(), Constant.baseUrl + Constant.addRecAddress, keys, values, handler, ADD);
                    } else if (mAddressEntity != null) {
                        keys = new String[]{"addr_id", "name", "province_id", "city_id", "region_id", "addr", "mobile","def_addr"};
                        values = new String[]{String.format("%d", mAddressEntity.getAddr_id()), mName, "491", "492", "493", mAddr, mPhone,String.valueOf(def_addr)};
                        VolleyUtils.NetUtils(((BaseApplication) getApplication()).getRequestQueue(), Constant.baseUrl + Constant.editAddress, keys, values, handler, EDIT);
                    }
                }
                break;
            case R.id.ib_edit_address_cancel:
                finish();
                break;
        }

    }

    /**
     * 检测输入是否合法
     *
     * @return 输入合法为true，否则为false
     */
    private boolean checkInput() {
        boolean result = true;
        mPhone = etPhone.getText().toString();
        mName = etName.getText().toString();
        mAddr = etAddr.getText().toString();
        if(mPhone.equals("")||mName.equals("")||mAddr.equals("")
                ||mPhone==null||mName==null||mAddr==null){
            ToastUtil.showToast(EditAddressActivity.this, "请输入完整资料");
//            Toast.makeText(EditAddressActivity.this,"请输入完整资料",Toast.LENGTH_LONG).show();
            return  false;
        }else {
            if (!Utils.isMobileFormat(mPhone)) {
//                etPhone.setText("");
//                etPhone.setHint("非法字符，请输入正确的手机号码");
//                etPhone.setHintTextColor(getResources().getColor(R.color.red));
                ToastUtil.showToast(this, "电话号码非法，请填写正确的电话号码");
//                Toast.makeText(this,"电话号码非法，请填写正确的电话号码",Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!Utils.isValidWord(mName)) {
//                etName.setText("");
//                etName.setHint("非法字符，请输入正确的姓名");
//                etName.setHintTextColor(getResources().getColor(R.color.red));
                ToastUtil.showToast(this, "姓名只允许汉字、英文和数字，请填写正确的姓名");
//                Toast.makeText(this,"姓名只允许汉字、英文和数字，请填写正确的姓名",Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!Utils.isValidWord(mAddr)) {
//                etAddr.setText("");
                ToastUtil.showToast(this, "地址只允许汉字、英文和数字，请填写正确的地址");
//                Toast.makeText(this,"地址只允许汉字、英文和数字，请填写正确的地址",Toast.LENGTH_SHORT).show();
//                etAddr.setHint("非法字符，请输入正确的地址");
//                etAddr.setHintTextColor(getResources().getColor(R.color.red));
                return false;
            }
        }
        return result;
    }
    /**
     * <p>验证手机号码正确性</p>
     */
    private boolean checkMobile(){
        String mobile = etPhone.getText().toString();
        if(!Utils.isMobileFormat(mobile)){
//            etPhone.setText("");
//            etPhone.setHint("请输入正确的手机号码");
//            etPhone.setHintTextColor(getResources().getColor(R.color.red));
            ToastUtil.showToast(EditAddressActivity.this, "请输入正确的手机号码");
//            Toast.makeText(EditAddressActivity.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }
}
