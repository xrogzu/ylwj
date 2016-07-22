package com.administrator.elwj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.administrator.bean.Addresses;
import com.administrator.bean.Constant;
import com.administrator.fragment.AddressFragment;

//我的中的地址管理页面
public class MyAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTitle;
    //右上角按钮
    private TextView tvRightTop;
    //是否是从订单中跳过来的,
    private boolean isFromOrderEmpty = false;
    //类型
    private int mType;
    //管理收货地址fragment
    private AddressFragment mManageAddressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        initViews();
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            isFromOrderEmpty = intent.getBooleanExtra("isFromOrderEmpty",false);
        }
    }

    /**
     * 初始化
     */
    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.title);
        Intent intent = getIntent();
        mType = intent.getIntExtra("type",AddressFragment.TYPE_MANAGE_ADDRESS);
        ImageButton ibCancel = (ImageButton) findViewById(R.id.hot_ib_back);
        ibCancel.setOnClickListener(this);
        tvRightTop = (TextView)findViewById(R.id.tv_myaddress_righttop);
        tvRightTop.setVisibility(View.GONE);
        if(mType == AddressFragment.TYPE_MANAGE_ADDRESS) {
            setTitle("管理收货地址");
            //tvRightTop.setText("新增");
            tvRightTop.setBackgroundResource(R.mipmap.new_address);
        }
        else {
            setTitle("选择收货地址");
            tvRightTop.setText("管理");
            tvRightTop.setVisibility(View.INVISIBLE);
        }
        tvRightTop.setOnClickListener(this);
        Bundle bundle = new Bundle();
        bundle.putInt("type", mType);
        bundle.putBoolean("isFromOrderEmpty", isFromOrderEmpty);
        mManageAddressFragment = new AddressFragment();
        mManageAddressFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.my_address_fragment_container,mManageAddressFragment,"manage").commit();
    }

    /**
     * 设置Activity的标题
     * @param title 要设置的标题
     */
    public void setTitle(String title){
        if(title != null)
            tvTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hot_ib_back:
                Intent newAddress = new Intent();
                setResult(Constant.NEW_ADDRESS, newAddress);
                finish();
                break;
            case R.id.tv_myaddress_righttop:
                Intent intent = null;
                switch (mType){

                    case AddressFragment.TYPE_MANAGE_ADDRESS:
                        intent= new Intent(this,EditAddressActivity.class);
                        startActivityForResult(intent, Constant.ADD_ADDRESS_REQUEST_CODE);
                        break;
                    case AddressFragment.TYPE_SELECT_ADDRESS:
                        intent = new Intent(this,MyAddressActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constant.ADD_ADDRESS_REQUEST_CODE:
                if(resultCode == RESULT_OK && data != null && mType == AddressFragment.TYPE_MANAGE_ADDRESS){
                    Addresses.DataEntity.AddressListEntity addressListEntity= data.getParcelableExtra("address");
                    if(addressListEntity != null && mManageAddressFragment != null){
                        mManageAddressFragment.addAddress(addressListEntity);
                    }
                }else if(resultCode == RESULT_OK && data != null & mType == AddressFragment.TYPE_SELECT_ADDRESS){
                    Addresses.DataEntity.AddressListEntity addressListEntity= data.getParcelableExtra("address");
                    if(addressListEntity != null && mManageAddressFragment != null){
                        if(isFromOrderEmpty){
                            Intent intent = new Intent();
                            intent.putExtra("data",addressListEntity);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                }
                break;
            case Constant.EDIT_ADDRESS_REQUEST_CODE:

                if(resultCode == RESULT_OK && data != null && mType == AddressFragment.TYPE_MANAGE_ADDRESS){
                    Addresses.DataEntity.AddressListEntity addressListEntity= data.getParcelableExtra("address");
                    if(addressListEntity != null && mManageAddressFragment != null){
                        mManageAddressFragment.updateAddress(addressListEntity);
                    }
                }
            default:
                break;
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent newAddress = new Intent();
            setResult(Constant.NEW_ADDRESS, newAddress);
            finish();

        }

        return true;
    }

    /**
     * <p>设置标题栏上面的添加地址按钮的隐藏或者显示。主要是为了在没有地址条目的时候隐藏掉</p>
     * @param visible true 显示  false 隐藏
     */
    public void setRightTopVisible(boolean visible){
        if(tvRightTop != null){
            if(visible){
                tvRightTop.setVisibility(View.VISIBLE);
            }else {
                tvRightTop.setVisibility(View.GONE);
            }
        }
    }
}
