package com.example.administrator.zhihuiyinshui.activity.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Fajieyefu on 2016/7/4.
 */
public class KindergartenActivity extends Activity {
    private ImageView kindergartenPic;
    private TextView kindergartenName, getKindergartenInfo;
    private String card_code;
    private SharedPreferences pref;
    private String info;
    private Handler handler=new Handler();
    String kindergarten_pic;//注意此处后期需要修改 因为加入园区没有上穿图片时，可能会把报错，需要在服务器数据中默认一个照片，表示没有加载到图片
    String kindergarten_name;
    String kindergarten_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kindergarten);
        pref = KindergartenActivity.this.getSharedPreferences("loginInfo", MODE_PRIVATE);
        card_code = pref.getString("card_code", "");
        kindergartenPic = (ImageView) findViewById(R.id.kindergartenPicture);
        kindergartenName = (TextView) findViewById(R.id.kindergarten_name);
        getKindergartenInfo = (TextView) findViewById(R.id.kindergarten_info);
        if (!TextUtils.isEmpty(card_code)) {
            init();
        } else {
            Toast.makeText(KindergartenActivity.this, "**请先绑定小朋友的水杯卡号**", Toast.LENGTH_SHORT).show();
        }


    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                info = WebService.executeHttpGetKindergartenInfo(card_code);

                JSONArray jsonArray;
                JSONObject jsonObject;
                try {
                    jsonArray = new JSONArray(info);
                    jsonObject = jsonArray.getJSONObject(0);
                    kindergarten_info = jsonObject.getString("kindergarten_info");
                    kindergarten_name = jsonObject.getString("kindergarten_name");
                    kindergarten_pic = jsonObject.getString("kindergarten_pic");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(KindergartenActivity.this).load("http://123.132.252.2:9891/" + kindergarten_pic).into(kindergartenPic);
                        kindergartenName.setText(kindergarten_name);
                        getKindergartenInfo.setText(kindergarten_info);


                    }
                });


            }
        }).start();
    }
}
