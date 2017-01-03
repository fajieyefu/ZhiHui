package com.example.administrator.zhihuiyinshui.activity.layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.zhihuiyinshui.R;



/**
 * Created by qiancheng on 2016/9/29.
 */
public class TitleLayout extends LinearLayout {
	private TextView title;
	public TitleLayout(final Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.titlelayout, this);
		title = (TextView) findViewById(R.id.title);
		ImageView back_view = (ImageView) findViewById(R.id.back_view);
		back_view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				((Activity)getContext()).finish();
			}
		});

	}
	public void setTitleText(String  text){
		title.setText(text);
	}

}
