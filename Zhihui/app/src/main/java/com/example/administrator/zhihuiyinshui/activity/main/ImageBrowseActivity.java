package com.example.administrator.zhihuiyinshui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.adapter.ViewPageAdapter;
import com.example.administrator.zhihuiyinshui.activity.bean.DrinkInfo;
import com.example.administrator.zhihuiyinshui.activity.bean.ImageBrowsePresenter;
import com.example.administrator.zhihuiyinshui.activity.bean.ImageBrowseView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qiancheng on 2016/10/19.
 */
public class ImageBrowseActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener ,ImageBrowseView {
	private ViewPager vp;
	private TextView hint;
	private TextView save;
	private ViewPageAdapter adapter;
	private ImageBrowsePresenter presenter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.pic_browse_layout);
		vp = (ViewPager) this.findViewById(R.id.viewPager);
		hint = (TextView) this.findViewById(R.id.hint);
		save = (TextView) this.findViewById(R.id.save);
		save.setOnClickListener(this);
		initPresenter();
		presenter.loadImage();
	}

	public void initPresenter(){
		presenter = new ImageBrowsePresenter(this);
	}

	@Override
	public Intent getDataIntent() {
		return getIntent();
	}



	@Override
	public Context getMyContext() {
		return this;
	}

	@Override
	public void setImageBrowse(List<DrinkInfo> images, int position) {
		if(adapter == null && images != null && images.size() != 0){
			adapter = new ViewPageAdapter(this,images);
			vp.setAdapter(adapter);
			vp.setCurrentItem(position);
			vp.addOnPageChangeListener(this);
			hint.setText(position + 1 + "/" + images.size());
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		presenter.setPosition(position);
		hint.setText(position + 1 + "/" + presenter.getImages().size());
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onClick(View v) {
		presenter.saveImage();
	}

	public static void startActivity(Context context, List<DrinkInfo> images , int position){
		Intent intent = new Intent(context,ImageBrowseActivity.class);
		intent.putExtra("images", (Serializable) images);
		intent.putExtra("position",position);
		context.startActivity(intent);
	}
}
