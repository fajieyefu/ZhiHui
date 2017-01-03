package com.example.administrator.zhihuiyinshui.activity.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.administrator.zhihuiyinshui.activity.bean.MyApplication;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by Fajieyefu on 2016/8/26.
 */
public class CacheForUserInformation {
    private String account;
    private SharedPreferences pref_loginInfo;
    private String card_code, register_time, pay_starttime, pay_endtime, nickname;
    private String address, introduce, sex, birthday, relationship,
            cardholder_name, kindergarten_code, kindergarten_name,
            class_name, class_code, cardholder_birthday;
    private int age;

    public CacheForUserInformation() {
        pref_loginInfo = MyApplication.getContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        account = pref_loginInfo.getString("account", "");
    }

    public void cacheinfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String info = WebService.executeHttpGetUserInfomation(account);
                JSONArray jsonArray;
                Boolean isSuccess;
                SharedPreferences.Editor editor_loginInfo;
                try {
                    jsonArray = new JSONArray(info);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
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
                        kindergarten_name = jsonObject.getString("kindergarten_name");
                        class_code = jsonObject.getString("class_code");
                        class_name = jsonObject.getString("class_name");
                        cardholder_birthday=jsonObject.getString("cardholder_birthday");
                    }
                    editor_loginInfo = pref_loginInfo.edit();
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
                    editor_loginInfo.putString("kindergarten_name", kindergarten_name);
                    editor_loginInfo.putString("class_name", class_name);
                    editor_loginInfo.putString("class_code", class_code);
                    editor_loginInfo.putString("cardholder_birthday", cardholder_birthday);
                    editor_loginInfo.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String getCard_code() {
        return pref_loginInfo.getString("card_code", "");
    }

    public String getKindergarten_code() {
        return pref_loginInfo.getString("kindergarten_code", "");
    }

    public String getKindergarten_name() {
        return pref_loginInfo.getString("kindergarten_name", "");
    }

    public String getClass_name() {

        return pref_loginInfo.getString("kindergarten_name", "");
    }
    public String getAccount(){
        return pref_loginInfo.getString("account", "");
    }

    public String getCardholder_name() {
        return pref_loginInfo.getString("cardholder_name", "");
    }
    public int  getAge(){
        long times = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String year= dateFormat.format(times);
        String birthday_year=pref_loginInfo.getString("cardholder_birthday","").substring(0,4);

        int year_int=Integer.parseInt(year);
        int cardholder_birthday_int= Integer.parseInt(birthday_year);
        age=year_int-cardholder_birthday_int;
        return age;

    }
}




