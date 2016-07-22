package com.administrator.fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.administrator.adapter.MyGridAdapter;
import com.administrator.bean.Constant;
import com.administrator.elwj.R;
import com.administrator.minterface.GetServiceWhere;

/**
 * Created by acer on 2016/1/5.
 * 服务fragment
 */
public class ServiceFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_servicemain,null);
        TextView tv_head= (TextView) view.findViewById(R.id.title);
        tv_head.setText(R.string.service);
        GridView gridView= (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(new MyGridAdapter(getActivity()));
        ImageButton back = (ImageButton)view.findViewById(R.id.hot_ib_back);
        back.setVisibility(View.INVISIBLE);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        GetServiceWhere where0= (GetServiceWhere) getActivity();
                        where0.getWhere(Constant.IN_INTEGRAL_SHOP);
                        break;
                    case 1:
                        GetServiceWhere where1= (GetServiceWhere) getActivity();
                        where1.getWhere(Constant.IN_COMMUNITY_STAGE);
                        break;
                    case 2:
                        GetServiceWhere where= (GetServiceWhere) getActivity();
                        where.getWhere(Constant.IN_FINANCIAL_EXPERT);
                        break;
                    case 3:
                        GetServiceWhere where2 = (GetServiceWhere) getActivity();
                        where2.getWhere(Constant.IN_DELICATE_LIFE);
                        break;
                    case 4:
                        GetServiceWhere where3 = (GetServiceWhere) getActivity();
                        where3.getWhere(Constant.IN_REGIMEN);
                        break;
                }
            }
        });
        return  view;
    }
}
