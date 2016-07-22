package com.administrator.elwj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import java.util.Map;

/**
 * 点击身边新鲜事地址，进入的地图界面
 * Created by Administrator on 2016/3/12.
 */
public class MapActivity extends AppCompatActivity{

    private MapView mapView;
    private AMap aMap;
    private UiSettings uiSetting;

    private TextView tvTitle;

    //经度
    private double x;
    //纬度
    private double y;
    //地址
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        tvTitle = (TextView) findViewById(R.id.title);
        ImageView ivBack = (ImageView) findViewById(R.id.back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.this.finish();
            }
        });
        getIntentData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if(intent != null){
            x = intent.getDoubleExtra("y",36.058942);
            y = intent.getDoubleExtra("x", 120.393318);
            address = intent.getStringExtra("address");
        }
        tvTitle.setText(address);
        initMap();
    }

    //初始化地图
    private void initMap() {
        if(aMap == null){
            aMap = mapView.getMap();
            uiSetting = aMap.getUiSettings();
        }
        uiSetting.setMyLocationButtonEnabled(false);
        aMap.setMyLocationEnabled(false);
        uiSetting.setCompassEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(x, y),16));
        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(x, y));
        options.title(address);
        options.draggable(false);
        aMap.addMarker(options);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
