package com.example.administrator.zhihuiyinshui.activity.layout;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.main.MainActivity;
import com.example.administrator.zhihuiyinshui.activity.main.DrinkListActivity;
import com.example.administrator.zhihuiyinshui.activity.main.GongGaoActivity;
import com.example.administrator.zhihuiyinshui.activity.main.MallActivity;
import com.example.administrator.zhihuiyinshui.activity.main.MyInfoActivity;

/**
 * Created by Fajieyefu on 2016/6/17.
 */
public class BottomLayout extends LinearLayout implements View.OnClickListener {
	Button firstPageButton, drinkListButton, shopButton, myButton, gongGaoButton;

	public BottomLayout(Context context) {
		super(context);
	}

	public BottomLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.bottomlayout, this);
		firstPageButton = (Button) findViewById(R.id.firstPage);
		drinkListButton = (Button) findViewById(R.id.drinkList);
		gongGaoButton = (Button) findViewById(R.id.gongGao);
		shopButton = (Button) findViewById(R.id.shop);
		myButton = (Button) findViewById(R.id.my);
		firstPageButton.setOnClickListener(this);
		drinkListButton.setOnClickListener(this);
		gongGaoButton.setOnClickListener(this);
		shopButton.setOnClickListener(this);
		myButton.setOnClickListener(this);

	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent = new Intent();
		switch (id) {
			case R.id.firstPage:
				intent.setClass(getContext(), MainActivity.class);
				break;
			case R.id.drinkList:
				intent.setClass(getContext(), DrinkListActivity.class);
				break;
			case R.id.gongGao:
				intent.setClass(getContext(), GongGaoActivity.class);
				break;
			case R.id.shop:
                intent.setClass(getContext(), MallActivity.class);
//				Toast.makeText(getContext(), "该模块正在开发中...敬请期待！", Toast.LENGTH_SHORT).show();
				break;
			case R.id.my:
				intent.setClass(getContext(), MyInfoActivity.class);
				break;
		}

			getContext().startActivity(intent);



	}
}
