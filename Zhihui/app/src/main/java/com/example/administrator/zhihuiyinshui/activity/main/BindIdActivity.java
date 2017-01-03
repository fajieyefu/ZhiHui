package com.example.administrator.zhihuiyinshui.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.Utils.CacheForUserInformation;
import com.example.administrator.zhihuiyinshui.activity.layout.TitleLayout;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;

/**
 * Created by Fajieyefu on 2016/7/5.
 */



public class BindIdActivity extends Activity implements View.OnClickListener{
    private EditText cupId;
    private EditText studentName;
    private Button bindButton;
    private SharedPreferences pref;
    private SharedPreferences pref_1;
    private SharedPreferences.Editor editor;
    private String account;
    private String card_code;
    private String info;
    private String cupIdText;
    private String student_name;
    private Handler handler =new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_cup);
        CacheForUserInformation cacheForUserInfomation= new CacheForUserInformation();
        account=cacheForUserInfomation.getAccount();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref_1=BindIdActivity.this.getSharedPreferences("loginInfo", MODE_PRIVATE);
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.title_layout);
        titleLayout.setTitleText("绑定水杯卡号");
        cupId= (EditText) findViewById(R.id.cupIdEditText);
        studentName= (EditText) findViewById(R.id.studentNameText);
        bindButton = (Button) findViewById(R.id.bind);
        bindButton.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bind:
                cupIdText = cupId.getText().toString();
                student_name=studentName.getText().toString();

                if (!TextUtils.isEmpty(cupIdText)&&!TextUtils.isEmpty(student_name)){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            info= WebService.executeHttpBindCup(cupIdText,account,student_name);
                            if(!TextUtils.isEmpty(info)&&info.equals("success")){
                                editor=pref_1.edit();
                                editor.putString("card_code",cupIdText);
                                Intent intent = new Intent(BindIdActivity.this,MyInfoActivity.class);
                                startActivity(intent);
                                finish();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(BindIdActivity.this, "恭喜，绑定成功！", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else{
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(BindIdActivity.this, "哎呀，绑定失败，请检查卡号和小朋友的名字是否正确！", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    }).start();

                }else{
                    Toast.makeText(BindIdActivity.this, "请输入卡号或者小朋友的名字", Toast.LENGTH_SHORT).show();
                }

        }
    }
}
