package com.example.administrator.zhihuiyinshui.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.BaseDialog;
import com.example.administrator.zhihuiyinshui.activity.Utils.CacheForUserInformation;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;
import com.squareup.picasso.Picasso;

import android.view.ViewGroup.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qiancheng on 2016/9/24.
 */
public class KindergartenViewPaper extends Activity implements GestureDetector.OnGestureListener {
	private ViewFlipper viewFlipper;
	private TextView title, content;
	private GestureDetector gestureDetector;
	private ImageView[] tips;
	private String[] imgString = new String[5];
	private int bottom_viewFlipper;
	private FrameLayout view_layout;
	private LinearLayout tipsBox;
	private String title_string,content_string;
	private String card_code;
	private int currentPage=0;//当前展示的页码
	private LinearLayout titleLayout;
	private BaseDialog dialog;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.kindergarten_viewpaper);
		findById();
		card_code = new CacheForUserInformation().getCard_code();
		if (!card_code.equals("null")){
			init();
		}else{
			dialog = new BaseDialog.Builder(this).setTitle("提示").setMessage("请先绑定小朋友的水杯卡号")
					.setPositiveButton("去绑定卡号", new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							dialog.dismiss();
							Intent intent = new Intent(KindergartenViewPaper.this, BindIdActivity.class);
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
		title = (TextView) findViewById(R.id.kindergarten_name);
		content = (TextView) findViewById(R.id.kindergarten_info);
		viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
		gestureDetector = new GestureDetector(this, this);
		view_layout = (FrameLayout) findViewById(R.id.view_layout);
		tipsBox= (LinearLayout) findViewById(R.id.tipsBox);
		titleLayout= (LinearLayout) findViewById(R.id.title_layout);
		TextView title= (TextView) titleLayout.findViewById(R.id.title);
		title.setText("园所介绍");
		viewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
					updateDotTips();

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});


	}

	private void updateDotTips() {
		int i =viewFlipper.getDisplayedChild();
		tips[i].setBackgroundResource(R.drawable.page_now);
		tips[currentPage].setBackgroundResource(R.drawable.page);
		currentPage=i;
	}

	private void init() {//添加图片资源
		CacheForUserInformation cache = new CacheForUserInformation();
		card_code=cache.getCard_code();
		viewFlipper.setAutoStart(true);//设置自动播放功能（点击时间前自动播放）
		viewFlipper.setFlipInterval(3000);//设置自动播放时间间隔
		if (viewFlipper.isAutoStart() && !viewFlipper.isFlipping()) {//判断状态是否为自动播放且是否正在滑动
			viewFlipper.startFlipping();
		}
		new LoadPicAsyncTask().execute();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int y = (int) event.getRawY();
		System.out.println("RawY:" + y);
		System.out.println(bottom_viewFlipper);
		if (y < bottom_viewFlipper) {
			viewFlipper.stopFlipping(); // 点击事件后，停止自动播放
			viewFlipper.setAutoStart(false);
			return gestureDetector.onTouchEvent(event); // 注册手势事件
		}
		return true;
	}

	@Override
	public boolean onDown(MotionEvent motionEvent) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent motionEvent) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent motionEvent) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent motionEvent) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2, float arg3) {
		if (e2.getX() - e1.getX() > 120) { // 从左向右滑动（左进右出）
			Animation rInAnim = AnimationUtils.loadAnimation(this,
					R.anim.push_right_in); // 向右滑动左侧进入的渐变效果（alpha 0.1 -> 1.0）
			Animation rOutAnim = AnimationUtils.loadAnimation(this,
					R.anim.push_right_out); // 向右滑动右侧滑出的渐变效果（alpha 1.0 -> 0.1）

			viewFlipper.setInAnimation(rInAnim);
			viewFlipper.setOutAnimation(rOutAnim);
			viewFlipper.showPrevious();
			updateDotTips();
			return true;
		} else if (e2.getX() - e1.getX() < -120) { // 从右向左滑动（右进左出）
			Animation lInAnim = AnimationUtils.loadAnimation(this,
					R.anim.push_left_in); // 向左滑动左侧进入的渐变效果（alpha 0.1 -> 1.0）
			Animation lOutAnim = AnimationUtils.loadAnimation(this,
					R.anim.push_left_out); // 向左滑动右侧滑出的渐变效果（alpha 1.0 -> 0.1）

			viewFlipper.setInAnimation(lInAnim);
			viewFlipper.setOutAnimation(lOutAnim);
			viewFlipper.showNext();
			updateDotTips();
			return true;
		}
		return true;
	}
	class LoadPicAsyncTask extends AsyncTask<Void,Void,Boolean>{


		@Override
		protected Boolean doInBackground(Void... voids) {
			String info = WebService.executeHttpGetKindergartenInfo(card_code);
			try {
				JSONObject jsonObject= new JSONObject(info);
				title_string=jsonObject.optString("kindergarten_name");
				content_string=jsonObject.optString("kindergarten_info");
				JSONArray jsonArray = jsonObject.getJSONArray("kindergarten_pic_all");
				int len =jsonArray.length();
				int i=0;
				while (i<len)
				{
					jsonObject=jsonArray.getJSONObject(i);
					String url_pic= jsonObject.optString("kindergarten_pic");

					imgString[i]=url_pic;
					i++;

				}
			} catch (JSONException e) {
				return false;
			}
			return true;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			title.setText(title_string);
			content.setText(content_string);
		}

		@Override
		protected void onPostExecute(Boolean aBoolean) {
			if(aBoolean){
				title.setText(title_string);
				content.setText(content_string);

				for (int i = 0; i < imgString.length; i++) {
					ImageView iv = new ImageView(KindergartenViewPaper.this);
//					iv.setImageResource(imgs[i]);
					Picasso.with(KindergartenViewPaper.this).load("http://121.199.22.246:9891/" + imgString[i]).into(iv);
					iv.setScaleType(ImageView.ScaleType.FIT_XY);
					viewFlipper.addView(iv, new LayoutParams(LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
				}
				setDotTips();
			}else {
				Toast.makeText(KindergartenViewPaper.this, "图片加载失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void setDotTips() {
		tips = new ImageView[imgString.length];
		for (int i = 0; i < tips.length; i++) {

			ImageView img = new ImageView(this);

			img.setLayoutParams(new LinearLayout.LayoutParams(10, 10));

			tips[i] = img;

			if (i == 0)

			{

				img.setBackgroundResource(R.drawable.page_now);

			} else {

				img.setBackgroundResource(R.drawable.page);

			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

			params.leftMargin = 10;

			params.rightMargin = 10;

			tipsBox.addView(img, params); //把点点添加到容器中
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		bottom_viewFlipper=viewFlipper.getBottom();
		System.out.println("getBottom:"+bottom_viewFlipper);
	}
}
