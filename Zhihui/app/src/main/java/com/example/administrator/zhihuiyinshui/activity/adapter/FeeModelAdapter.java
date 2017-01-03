package com.example.administrator.zhihuiyinshui.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.FeeModel;

import java.util.List;

/**
 * Created by Fajieyefu on 2016/9/5.
 */
public class FeeModelAdapter extends BaseAdapter{
    private  List<FeeModel> mData;
    private LayoutInflater myInflater;
    public FeeModelAdapter(Context context, List<FeeModel> data){
        mData=data;
        myInflater=LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        FeeModel feeModel= mData.get(position);
        convertView=myInflater.inflate(R.layout.pay_item,null);
        TextView model_name_view = (TextView) convertView.findViewById(R.id.model_name);
        TextView model_price_view= (TextView) convertView.findViewById(R.id.model_price);
        RadioButton radioButton= (RadioButton) convertView.findViewById(R.id.model_radio);
        model_name_view.setText(feeModel.getModel_name()+":");
        model_price_view.setText(feeModel.getModel_price()+"元");
        radioButton.clearFocus();
        radioButton.setChecked(feeModel.getModel_radio());

        return convertView;
    }
}
