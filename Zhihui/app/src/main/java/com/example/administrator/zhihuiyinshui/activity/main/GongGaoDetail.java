package com.example.administrator.zhihuiyinshui.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.zhihuiyinshui.R;

/**
 * Created by Fajieyefu on 2016/9/3.
 */
public class GongGaoDetail extends Activity {
    private TextView title_txt,content_txt,time_txt;
    private String title,content,time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        Intent intent=getIntent();
        title= intent.getStringExtra("news_title");
        content = intent.getStringExtra("news_content");
        time=intent.getStringExtra("news_time");
        initViews();


    }

    private void initViews() {
        title_txt= (TextView) findViewById(R.id.title);
        content_txt= (TextView) findViewById(R.id.content);
        time_txt= (TextView) findViewById(R.id.time);
        title_txt.setText(title);
        content_txt.setText(content);
        time_txt.setText(time);

    }
}
