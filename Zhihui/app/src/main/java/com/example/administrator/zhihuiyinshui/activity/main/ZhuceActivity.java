package com.example.administrator.zhihuiyinshui.activity.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.zhihuiyinshui.R;

public class ZhuceActivity extends AppCompatActivity {

    EditText userName,passWord;
    Button sumbit;
    String user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuce);

        userName = (EditText) findViewById(R.id.account);
        passWord = (EditText) findViewById(R.id.password);
        sumbit = (Button) findViewById(R.id.sumbit);
    }
}
