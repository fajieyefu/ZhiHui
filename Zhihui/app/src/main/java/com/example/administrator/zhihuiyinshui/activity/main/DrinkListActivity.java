package com.example.administrator.zhihuiyinshui.activity.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.BaseActivity;
import com.example.administrator.zhihuiyinshui.activity.Utils.CacheForUserInformation;
import com.example.administrator.zhihuiyinshui.activity.adapter.DrinkListAdapter;
import com.example.administrator.zhihuiyinshui.activity.bean.Rotate3dAnimation;
import com.example.administrator.zhihuiyinshui.activity.bean.Student;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fajieyefu on 2016/6/17.
 */
public class DrinkListActivity extends BaseActivity {
	private Student student;
	public String card_code;
	/**
	 * 以下为测试3D动画旋转的效果
	 */
	private TextView test;
	private LinearLayout contentLayout;
	private ListView lv_today, lv_history;
	private int centerX, centerY;
	private Rotate3dAnimation openAnimation, closeAnimation;
	private int duration = 600;
	private int depthZ = 400;
	private boolean isOpen = false;
	private List<Student> list_today, list_history;
	private DrinkListAdapter adapter_today, adapter_history;
	private int empty_mark_t, empty_mark_h;
	private LinearLayout emptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.drinklist);
		findById();
		init();
		changeBottomPic();
		test.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickView();
			}
		});


		CacheForUserInformation cache = new CacheForUserInformation();
		card_code = cache.getCard_code();
		if (!TextUtils.isEmpty(card_code)) {
			new DrinkListTask(0, list_today, adapter_today).execute();
			new DrinkListTask(1, list_history, adapter_history).execute();

		} else {
			Toast.makeText(DrinkListActivity.this, "请绑定小朋友的水杯卡号", Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * 初始化控件
	 */
	private void findById() {
		lv_today = (ListView) findViewById(R.id.lv_today);
		lv_history = (ListView) findViewById(R.id.lv_history);
		test = (TextView) findViewById(R.id.test);
		contentLayout = (LinearLayout) findViewById(R.id.list_layout);
		emptyView = (LinearLayout) findViewById(R.id.empty_view);

	}

	private void init() {
		list_today = new ArrayList<>();
		list_history = new ArrayList<>();
		adapter_today = new DrinkListAdapter(DrinkListActivity.this, R.layout.drinklist_item_1, list_today);
		adapter_history = new DrinkListAdapter(DrinkListActivity.this, R.layout.drinklist_item_1, list_history);
		lv_today.setAdapter(adapter_today);
		lv_history.setAdapter(adapter_history);

	}

	/**
	 * 3D旋转打开效果，注意旋转角度
	 */
	private void initOpenAnim() {
		//从0到90度，顺时针旋转视图，此时reverse参数为true，达到90度时动画结束时视图变得不可见，
		openAnimation = new Rotate3dAnimation(0, 90, centerX, centerY, depthZ, true);
		openAnimation.setDuration(duration);
		openAnimation.setFillAfter(true);
		openAnimation.setInterpolator(new AccelerateInterpolator());
		openAnimation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (empty_mark_h == 1) {
					emptyView.setVisibility(View.VISIBLE);
					lv_history.setVisibility(View.GONE);
				} else {
					emptyView.setVisibility(View.GONE);
					lv_history.setVisibility(View.VISIBLE);
				}
				lv_today.setVisibility(View.GONE);


				//从270到360度，顺时针旋转视图，此时reverse参数为false，达到360度动画结束时视图变得可见
				Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(270, 360, centerX, centerY, depthZ, false);
				rotateAnimation.setDuration(duration);
				rotateAnimation.setFillAfter(true);
				rotateAnimation.setInterpolator(new DecelerateInterpolator());
				contentLayout.startAnimation(rotateAnimation);
			}
		});
	}

	/**
	 * 3D旋转关闭效果，注意旋转角度
	 */
	private void initCloseAnim() {
		closeAnimation = new Rotate3dAnimation(360, 270, centerX, centerY, depthZ, true);
		closeAnimation.setDuration(duration);
		closeAnimation.setFillAfter(true);
		closeAnimation.setInterpolator(new AccelerateInterpolator());
		closeAnimation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (empty_mark_t == 1) {
					emptyView.setVisibility(View.VISIBLE);
					lv_today.setVisibility(View.GONE);
				} else {
					emptyView.setVisibility(View.GONE);
					lv_today.setVisibility(View.VISIBLE);
				}

				lv_history.setVisibility(View.GONE);

				Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(90, 0, centerX, centerY, depthZ, false);
				rotateAnimation.setDuration(duration);
				rotateAnimation.setFillAfter(true);
				rotateAnimation.setInterpolator(new DecelerateInterpolator());
				contentLayout.startAnimation(rotateAnimation);
			}
		});
	}

	public void onClickView() {
		//以旋转对象的中心点为旋转中心点，这里主要不要再onCreate方法中获取，因为视图初始绘制时，获取的宽高为0
		centerX = contentLayout.getWidth() / 2;
		centerY = contentLayout.getHeight() / 2;
		if (openAnimation == null) {
			initOpenAnim();
			initCloseAnim();
		}

		//用作判断当前点击事件发生时动画是否正在执行
		if (openAnimation.hasStarted() && !openAnimation.hasEnded()) {
			return;
		}
		if (closeAnimation.hasStarted() && !closeAnimation.hasEnded()) {
			return;
		}

		//判断动画执行
		if (isOpen) {
			contentLayout.startAnimation(closeAnimation);
			test.setText("今日喝水排名");

		} else {
			test.setText("本月喝水排名");
			contentLayout.startAnimation(openAnimation);
		}

		isOpen = !isOpen;


	}

	/**
	 * 改变最下面菜单点击后的效果
	 */
	private void changeBottomPic() {
		Button bottom_select = (Button) findViewById(R.id.drinkList);
		bottom_select.setTop(R.mipmap.paihang_2);
		bottom_select.setTextColor(getResources().getColor(R.color.white));
	}


	/**
	 * 自定义AsyncTask,获取喝水信息
	 */
	class DrinkListTask extends AsyncTask<Void, Void, Boolean> {
		private int type;
		private List<Student> list;
		private DrinkListAdapter adapter;
		private String info;


		DrinkListTask(int type, List<Student> list, DrinkListAdapter adapter) {
			this.type = type;
			this.list = list;
			this.adapter = adapter;
		}

		@Override
		protected Boolean doInBackground(Void... voids) {
			info = WebService.executeHttpGetDrinkListInfo(card_code, type);
			int drinkCount;
			String name;
			JSONArray jsonArray;
			JSONObject jsonObject;
			if (info.equals("null")) {//判断是否存在喝水信息，
				return false;
			} else {
				try {
					jsonArray = new JSONArray(info);
					int len = jsonArray.length();
					for (int i = 0; i < len; i++) {
						jsonObject = jsonArray.getJSONObject(i);
						drinkCount = Integer.parseInt(jsonObject.getString("drink_count"));
						name = jsonObject.getString("cardholder_name");
						student = new Student(drinkCount, name);
						list.add(student);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return true;
			}

		}

		@Override
		protected void onProgressUpdate(Void... values) {
			adapter.notifyDataSetChanged();
		}

		@Override
		protected void onPostExecute(Boolean aBoolean) {
			if (aBoolean) {
				adapter.notifyDataSetChanged();
			} else {
				if (type == 0) {
					empty_mark_t = 1;
					lv_today.setVisibility(View.GONE);
					emptyView.setVisibility(View.VISIBLE);
				} else if (type == 1) {
					empty_mark_h = 1;
					emptyView.setVisibility(View.VISIBLE);
				}
				Toast.makeText(DrinkListActivity.this, "没有获取到喝水信息ooO", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
