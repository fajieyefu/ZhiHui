package com.example.administrator.zhihuiyinshui.activity.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.ActivityCollector;
import com.example.administrator.zhihuiyinshui.activity.bean.BaseActivity;
import com.example.administrator.zhihuiyinshui.activity.bean.CircleTransform;
import com.example.administrator.zhihuiyinshui.activity.test.SettingActivity2;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Fajieyefu on 2016/6/17.
 */
public class MyInfoActivity extends BaseActivity implements View.OnClickListener {

    private Button exit;
    private LinearLayout bindId,setting,contract,about;
    private ImageView myPic;
    private SharedPreferences pref;
    private String account,nickname;
    private TextView nickname_textview;
    public static final String ACCOUNT_DIR = Environment.getExternalStorageDirectory().getPath()
            + "/account/";
    public static final String ACCOUNT_MAINTRANCE_ICON_CACHE = "icon_cache/";
    private static final String IMGPATH = ACCOUNT_DIR + ACCOUNT_MAINTRANCE_ICON_CACHE;
    private String TMP_IMAGE_FILE_NAME ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo);
        changeBottomPic();
        bindId= (LinearLayout) findViewById(R.id.bindCupId);
        setting= (LinearLayout) findViewById(R.id.person_info_setting);
        contract= (LinearLayout) findViewById(R.id.contract);
        about= (LinearLayout) findViewById(R.id.about);

        nickname_textview= (TextView) findViewById(R.id.nickname);
        exit=(Button)findViewById(R.id.exit);
        myPic= (ImageView) findViewById(R.id.pic);
        pref= MyInfoActivity.this.getSharedPreferences("loginInfo",MODE_PRIVATE);
        account=pref.getString("account","");
        Log.i("图片地址2",account);
        nickname=pref.getString("nickname","");
        nickname_textview.setText(nickname);
        exit.setOnClickListener(this);
        bindId.setOnClickListener(this);
        setting.setOnClickListener(this);
        contract.setOnClickListener(this);
        about.setOnClickListener(this);
        initPic();

    }

	/**
     * 改变下层状态栏图片
     */
    private void changeBottomPic() {
        Button bottom_select= (Button) findViewById(R.id.my);
        bottom_select.setTop(R.mipmap.mycenter_2);
        bottom_select.setTextColor(getResources().getColor(R.color.white));
    }

    private void initPic() {
        TMP_IMAGE_FILE_NAME=account+".jpg";
        File file = new File(IMGPATH, TMP_IMAGE_FILE_NAME);
        if (file.exists()){
            Bitmap bitmap= BitmapFactory.decodeFile(file.getPath());
            if (bitmap!=null){
                bitmap=new CircleTransform().transform(bitmap);
                myPic.setImageBitmap(bitmap);
            }


        }else{
            Picasso.with(MyInfoActivity.this).load("http://121.199.22.246:9891/ZhiHuiWeb/uploadImage/"+account+ ".jpg").transform(new CircleTransform()).into(myPic);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exit:
                Intent intent = new Intent(MyInfoActivity.this,LoginActivity.class);
                SharedPreferences.Editor editor= pref.edit();
                editor.clear();
                editor.commit();
                startActivity(intent);
                ActivityCollector.finishAll();
                break;
            case R.id.bindCupId:
                Intent intent_bind=new Intent(MyInfoActivity.this,BindIdActivity.class);
                startActivity(intent_bind);
                break;
            case R.id.person_info_setting:
                Intent intent_setting=new Intent(MyInfoActivity.this,SettingActivity2.class);
                startActivity(intent_setting);
                break;
            case R.id.contract:
                Intent intent_contract = new Intent(MyInfoActivity.this,ContractActivity.class);
                startActivity(intent_contract);
                break;
            case R.id.about:
                Intent intent_about=new Intent(MyInfoActivity.this,AboutActivity.class);
                startActivity(intent_about);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPic();
    }
}
