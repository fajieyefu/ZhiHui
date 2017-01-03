package com.example.administrator.zhihuiyinshui.activity.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.CustomDialog;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fajieyefu on 2016/6/22.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {
    private TextView userLicense;
    private Button register,sendMessageButton;
    private EditText phoneEdit, identifyCode,passWordEdit,useNameEdit;
    private String phone,getPasswordString,phoneCodeString,password,useName;
    private Handler handler = new Handler();
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        userLicense = (TextView) findViewById(R.id.userLicense);//用户协议
        userLicense.setOnClickListener(this);
        phoneEdit = (EditText) findViewById(R.id.phone);
        useNameEdit= (EditText) findViewById(R.id.userName);
        passWordEdit = (EditText) findViewById(R.id.passwordEdit);
//        identifyCode = (EditText) findViewById(R.id.identifyCode);//输入手机验证码 后期再实现
//        sendMessageButton = (Button) findViewById(R.id.getNumButton);//获取手机验证码的按钮
//        sendMessageButton.setOnClickListener(this);

        register = (Button) findViewById(R.id.registerButton);
        register.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerButton:
                phone = phoneEdit.getText().toString();
                password =passWordEdit.getText().toString();
                useName=useNameEdit.getText().toString();
                Boolean isMoblie = isMobileNO(phone);
                Boolean isPassOk= isPassOk(password);
                Boolean isUserNameOk=isUserNameOk(useName);
                if (!isMoblie) {
                    Toast.makeText(RegisterActivity.this, "您输入的手机号不合法", Toast.LENGTH_SHORT).show();
                    break;
                }else if(!isUserNameOk){
                    Toast.makeText(RegisterActivity.this, "用户名由字母数字下划线组成且开头必须是字母，不能超过16位", Toast.LENGTH_SHORT).show();
                    break;
                }else if(!isPassOk){
                    Toast.makeText(RegisterActivity.this, "密码必须同时有字母和数字，长度为 6-10", Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            info =WebService.makeAccount(phone,password,useName);
                                    if (info.equals("0")){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(RegisterActivity.this, "抱歉，该用户名已经被注册", Toast.LENGTH_SHORT).show();
                                                useNameEdit.setText("");
                                                useNameEdit.setFocusable(true);
                                            }
                                        });
                                    }else if (info.equals("1")){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                CustomDialog.Builder builder= new CustomDialog.Builder(RegisterActivity.this);
                                                builder.setTitle("友情提示");
                                                builder.setMessage("恭喜，注册成功！");
                                                builder.setPositiveButton("立即登录", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                        //设置你的操作事项
                                                    }
                                                });
                                                CustomDialog dialog= builder.create();
                                                dialog.setCanceledOnTouchOutside(false);
                                                dialog.show();
                                            }
                                        });

                                    }else{
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
//                                                myDialog =new AlertDialog.Builder(RegisterActivity.this).create();
//                                                TextView textView= (TextView) myDialog.getWindow().findViewById(R.id.dialog_text);
//                                                textView.setText("未知错误："+info);
//                                                myDialog.getWindow().findViewById(R.id.dialog_button).setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        myDialog.dismiss();
//                                                    }
//                                                });
//                                                myDialog.show();
                                            }
                                        });


                                    }
                                }



                    }).start();
                    break;
                }


        }

    }

    private Boolean isUserNameOk(String useName) {
        Pattern p= Pattern.compile("[a-zA-Z]{1}[a-zA-Z0-9_]{1,15}");
        Matcher m =p.matcher(useName);
        return  m.matches();
    }

    private Boolean isPassOk(String password) {
        Pattern p = Pattern.compile("^(\\d+[A-Za-z]+[A-Za-z0-9]*)|([A-Za-z]+\\d+[A-Za-z0-9]*)$");
         Matcher m=p.matcher(password);
        return m.matches();
    }

    public  boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);

        return m.matches();

    }
}
