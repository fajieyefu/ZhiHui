package com.example.administrator.zhihuiyinshui.activity.text;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.BaseDialog;
import com.example.administrator.zhihuiyinshui.activity.Utils.CacheForUserInformation;
import com.example.administrator.zhihuiyinshui.activity.main.BindIdActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiancheng on 2016/9/21.
 */
public class DrinkViewPaper extends FragmentActivity {
	private List<Fragment> mFragmentList = new ArrayList<>();
	private FragmentAdapter mFragmentAdapter;
	private ViewPager mPageVp;
	private String card_code;
	private TextView todayText, historyText;
	private int first;
	private ImageView back;
	/*
	tab下面的那根引导线
	 */
	private ImageView mTabLineIv;
	/*
	Fragment
	 */
	private DrinkTodayFragment todayFg;
	private DrinkHistoryFragment historyFg;
	/*
	viewpaper当前选中页
	 */
	private int currentIndex;
	/*
	屏幕的宽度
	 */
	private int screenWidth;
	private BaseDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test_viewpaper);
		findById();
		card_code = new CacheForUserInformation().getCard_code();
		if (!card_code.equals("null")){
			init();
			initTabLineWidth();
		}else{
			 dialog = new BaseDialog.Builder(this).setTitle("提示").setMessage("请先绑定小朋友的水杯卡号")
					.setPositiveButton("去绑定卡号", new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							dialog.dismiss();
							Intent intent = new Intent(DrinkViewPaper.this, BindIdActivity.class);
							startActivity(intent);
							finish();
						}
					}).setNegativeButton("取消", new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							dialog.dismiss();
							finish();
						}
					}).setCancelable(false).setCanceledOnTouchOutside(false).create();
			dialog.show();
		}


	}

	private void findById() {

		mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);
		mPageVp = (ViewPager) this.findViewById(R.id.id_paper_vp);
		todayText = (TextView) findViewById(R.id.today_text);
		historyText = (TextView) findViewById(R.id.history_text);
		back= (ImageView) findViewById(R.id.back);
	}

	private void init() {
		todayText.setOnClickListener(new MyClickListener(0));
		historyText.setOnClickListener(new MyClickListener(1));
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		historyFg = new DrinkHistoryFragment();
		todayFg = new DrinkTodayFragment();
		mFragmentList.add(todayFg);
		mFragmentList.add(historyFg);
		mFragmentAdapter = new FragmentAdapter(
				this.getSupportFragmentManager(), mFragmentList);
		mPageVp.setAdapter(mFragmentAdapter);
		mPageVp.setCurrentItem(0);
		mPageVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			/**
			 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
			 * offsetPixels:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int position, float offset, int offsetPixels) {
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
						.getLayoutParams();
				Log.e("offset:", offset + "");
				/**
				 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
				 * 设置mTabLineIv的左边距 滑动场景：
				 * 记3个页面,
				 * 从左到右分别为0,1
				 * 0->1; 1->0
				 */

				if (currentIndex == 0 && position == 0) {// 0->1

					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 2) + currentIndex
							* (screenWidth / 2));

				} else if (currentIndex == 1 && position == 0) // 1->0
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 2) + currentIndex
							* (screenWidth / 2));

				}
				mTabLineIv.setLayoutParams(lp);

			}


			@Override
			public void onPageSelected(int position) {

				if (card_code.equals("notEdit")) {
					Toast.makeText(DrinkViewPaper.this, "请先绑定小朋友的水杯卡号", Toast.LENGTH_SHORT).show();
				}


				currentIndex = position;


			}

			/**
			 * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
			 */
			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

	}

	/*
	设置TAB下划线的宽度
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		lp.width = screenWidth / 2;
		mTabLineIv.setLayoutParams(lp);
		first = screenWidth / 2;

	}


	class MyClickListener implements View.OnClickListener {

		private int index = 0;

		public MyClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPageVp.setCurrentItem(index);

		}

	}
}
