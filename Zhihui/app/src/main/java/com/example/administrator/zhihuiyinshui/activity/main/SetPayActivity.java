package com.example.administrator.zhihuiyinshui.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.BaseDialog;
import com.example.administrator.zhihuiyinshui.activity.Utils.CacheForUserInformation;
import com.example.administrator.zhihuiyinshui.activity.bean.FeeModel;
import com.example.administrator.zhihuiyinshui.activity.adapter.FeeModelAdapter;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fajieyefu on 2016/9/5.
 */
public class SetPayActivity extends Activity implements View.OnClickListener {
    private List<FeeModel> list= new ArrayList<>();
    private FeeModelAdapter adapter;
    private ListView listView;
    private Handler handler= new Handler();
    private int selected_pos=0;
    private String card_code;
    private BaseDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay2);
        listView= (ListView) findViewById(R.id.listview_fee_model);
        adapter= new FeeModelAdapter(this,list);
        listView.setAdapter(adapter);
        initData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0;i<list.size();i++){
                    list.get(i).setModel_radio(false);
                }
                FeeModel feeModel = list.get(position);
                feeModel.setModel_radio(true);
                selected_pos=position;
                listView.setAdapter(adapter);
            }
        });
        Button button= (Button) findViewById(R.id.payButton);
        button.setOnClickListener(this);
        card_code = new CacheForUserInformation().getCard_code();
        if (card_code.equals("null")){
            dialog = new BaseDialog.Builder(this).setTitle("提示").setMessage("请先绑定小朋友的水杯卡号")
                    .setPositiveButton("去绑定卡号", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            Intent intent = new Intent(SetPayActivity.this, BindIdActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            finish();
                        }
                    }).setCancelable(false).setCanceledOnTouchOutside(false).create();
            dialog.show();
        }

    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String info=WebService.getFeeModel();
                try {
                    JSONArray jsonArray= new JSONArray(info);
                    JSONObject jsonObject;
                    FeeModel feeModel;
                    int len =jsonArray.length();
                    for (int i=0;i<len;i++){
                        jsonObject=jsonArray.getJSONObject(i);
                        String model_name=jsonObject.getString("model_name");
                        String model_price=jsonObject.getString("model_price");
                        String model_times=jsonObject.getString("model_times");
                        feeModel=new FeeModel(model_name,model_price,model_times);
                        list.add(feeModel);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            list.get(0).setModel_radio(true);
                            adapter.notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {         //如果得到的数据不是json格式
                    e.printStackTrace();
                }
            }
        }).start();


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.payButton:
                Intent intent = new Intent(SetPayActivity.this, ChoosePayActivity.class);
                FeeModel feeModel = list.get(selected_pos);
                intent.putExtra("model_name", feeModel.getModel_name());
                intent.putExtra("model_price", feeModel.getModel_price());
                intent.putExtra("model_times",feeModel.getModel_times());
                startActivity(intent);
        }
    }
}
