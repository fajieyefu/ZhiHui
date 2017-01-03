package com.example.administrator.zhihuiyinshui.activity.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.Utils.CheckNetWorkUtil;
import com.example.administrator.zhihuiyinshui.activity.Utils.UpdateApk;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;
import com.example.administrator.zhihuiyinshui.activity.service.UpdatePicService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShanPingActivity extends Activity {
    private String info = null;
    private String account = null;
    private String password = null;
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shan_ping);


        SharedPreferences pref = ShanPingActivity.this.getSharedPreferences("com.example.administrator.zhihui_preferences",
                MODE_PRIVATE);
        account = pref.getString("account", "");
        password = pref.getString("pass", "");
        Boolean isNetOk = CheckNetWorkUtil.checkNetwork(this);//检查网络
        if (!isNetOk) {
            Toast.makeText(ShanPingActivity.this, "请检查网络连接！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ShanPingActivity.this,
                    LoginActivity.class); // 从启动动画ui跳转到主ui
            startActivity(intent);
        }
        //检查是否有新版本
        new AsyncTask<Void,Void,Boolean>(){
            Boolean vsIsNew;
            @Override
            protected Boolean doInBackground(Void... voids) {
                 vsIsNew = UpdateApk.isNewVersion(ShanPingActivity.this);
                return vsIsNew;
        }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean){
                    new Thread(new myThread()).start();
                }else{
                   Dialog dialog =new UpdateApk(ShanPingActivity.this).updateDialog(ShanPingActivity.this);
                    dialog.show();
                }
            }
        }.execute();



    }

    private class myThread implements Runnable {
        @Override
        public void run() {
            if (!TextUtils.isEmpty(account))
                info = WebService.executeHttpGet(account, password);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (info != null && !info.equals("fail")) {
                        String card_code, register_time, pay_starttime, pay_endtime, nickname;
                        String address, introduce, sex, birthday, relationship;
                        String cardholder_name = "", kindergarten_code = "", class_code = "";
                        Boolean isSuccess;
                        SharedPreferences pref_loginInfo = ShanPingActivity.this.getSharedPreferences("loginInfo", MODE_PRIVATE);
                        try {
                            JSONArray jsonArray = new JSONArray(info);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            account = jsonObject.getString("account");
                            card_code = jsonObject.getString("card_code");
                            register_time = jsonObject.getString("register_time");
                            pay_starttime = jsonObject.getString("pay_starttime");
                            pay_endtime = jsonObject.getString("pay_endtime");

                            nickname = jsonObject.getString("nickname");
                            address = jsonObject.getString("address");
                            introduce = jsonObject.getString("introduce");
                            sex = jsonObject.getString("sex");
                            birthday = jsonObject.getString("birthday");
                            relationship = jsonObject.getString("relationship");
                            isSuccess = jsonObject.getBoolean("success");
                            if (isSuccess) {
                                cardholder_name = jsonObject.getString("cardholder_name");
                                kindergarten_code = jsonObject.getString("kindergarten_code");
                                class_code = jsonObject.getString("class_code");
                            }
                            SharedPreferences.Editor editor_loginInfo = pref_loginInfo.edit();
                            editor_loginInfo.putString("account", account);
                            editor_loginInfo.putString("card_code", card_code);
                            editor_loginInfo.putString("register_time", register_time);
                            editor_loginInfo.putString("pay_starttime", pay_starttime);
                            editor_loginInfo.putString("pay_endtime", pay_endtime);
                            editor_loginInfo.putString("nickname", nickname);
                            editor_loginInfo.putString("address", address);
                            editor_loginInfo.putString("introduce", introduce);
                            editor_loginInfo.putString("sex", sex);
                            editor_loginInfo.putString("birthday", birthday);
                            editor_loginInfo.putString("relationship", relationship);
                            editor_loginInfo.putString("cardholder_name", cardholder_name);
                            editor_loginInfo.putString("kindergarten_code", kindergarten_code);
                            editor_loginInfo.putString("class_code", class_code);
                            editor_loginInfo.apply();
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                        Intent i = new Intent(ShanPingActivity.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        Intent intent = new Intent(ShanPingActivity.this,
                                LoginActivity.class); // 从启动动画ui跳转到主ui
                        startActivity(intent);
                        finish();
                    }


//                overridePendingTransition(R.anim.in_from_right,
//                        R.anim.out_to_left);
                    ShanPingActivity.this.finish(); // 结束启动动画界面

                }
            }, 3000); // 启动动画持续3秒钟
        }
    }
}
