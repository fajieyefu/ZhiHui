package com.example.administrator.zhihuiyinshui.activity.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.BaseActivity;
import com.example.administrator.zhihuiyinshui.activity.Utils.CacheForUserInformation;
import com.example.administrator.zhihuiyinshui.activity.bean.CircleTransform;
import com.example.administrator.zhihuiyinshui.activity.service.UpdatePicService;
import com.example.administrator.zhihuiyinshui.activity.text.DrinkViewPaper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements View.OnClickListener {

	private Button btn_jieshao, btn_liulan, btn_zixun, btn_shangcheng, btn_jiaofei, btn_gonggao;
	private ImageView use_pic;
	private String account;
	private SharedPreferences pref;
	private ImageView imageView;
	public static final String ACCOUNT_DIR = Environment.getExternalStorageDirectory().getPath()
			+ "/account/";
	public static final String ACCOUNT_MAINTRANCE_ICON_CACHE = "icon_cache/";
	private static final String IMGPATH = ACCOUNT_DIR + ACCOUNT_MAINTRANCE_ICON_CACHE;//照片存放地址
	private String TMP_IMAGE_FILE_NAME;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CacheForUserInformation cache = new CacheForUserInformation();
		account=cache.getAccount();
		changeBottomPic();
		imageView = (ImageView) findViewById(R.id.test);
		btn_jieshao = (Button) findViewById(R.id.btn_jiehao);
		btn_liulan = (Button) findViewById(R.id.btn_liulan);
		btn_zixun = (Button) findViewById(R.id.btn_zixun);
		btn_shangcheng = (Button) findViewById(R.id.btn_shangcheng);
		btn_jiaofei = (Button) findViewById(R.id.btn_jiaofei);
		btn_gonggao = (Button) findViewById(R.id.btn_gonggao);
		use_pic = (ImageView) findViewById(R.id.pic);
		btn_jieshao.setOnClickListener(this);

		btn_liulan.setOnClickListener(this);

		btn_zixun.setOnClickListener(this);

		btn_shangcheng.setOnClickListener(this);

		btn_jiaofei.setOnClickListener(this);

		btn_gonggao.setOnClickListener(this);
		initPic();
		//开启任务监听喝水动态
		TimerTask task = new TimerTask() {
			public void run() {
				Intent intent = new Intent(MainActivity.this, UpdatePicService.class);
				startService(intent);
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 5000);
	}

	//改变下面布局的图片显示
	private void changeBottomPic() {
		Button bottom_select = (Button) findViewById(R.id.firstPage);
		bottom_select.setTop(R.mipmap.shouye_2);
		bottom_select.setTextColor(getResources().getColor(R.color.white));
	}

	//加载头像
	private void initPic() {
		TMP_IMAGE_FILE_NAME = account + ".jpg";
		Log.i("图片地址",TMP_IMAGE_FILE_NAME);
		File file = new File(IMGPATH, TMP_IMAGE_FILE_NAME);
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
			if (bitmap != null) {
				bitmap = new CircleTransform().transform(bitmap);
				use_pic.setImageBitmap(bitmap);
			}


		} else {
			new AsyncTask<Void, Void, Boolean>() {
				Bitmap bitmap;

				@Override
				protected Boolean doInBackground(Void... voids) {
					TMP_IMAGE_FILE_NAME = account + ".jpg";
					ImageLoader imageLoader = ImageLoader.getInstance();
					if (!imageLoader.isInited()) {
						imageLoader.init(ImageLoaderConfiguration.createDefault(MainActivity.this));
					}
					try {
						bitmap = imageLoader.loadImageSync("http://121.199.22.246:9891/ZhiHuiWeb/uploadImage/" + account + ".jpg");
						FileOutputStream out;
						final File file = new File(IMGPATH, TMP_IMAGE_FILE_NAME);
						out = new FileOutputStream(file);
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
						out.flush();
						out.close();

					} catch (Exception e) {
						bitmap = null;
						return false;
					}
					return true;
				}

				@Override
				protected void onPostExecute(Boolean aBoolean) {
					if (aBoolean) {
						bitmap = new CircleTransform().transform(bitmap);
						use_pic.setImageBitmap(bitmap);
					} else {
						use_pic.setImageBitmap(null);
						Toast.makeText(MainActivity.this, ">请设置头像ooO<", Toast.LENGTH_SHORT).show();
					}

				}
			}.execute();
		}


	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.btn_liulan:
				Intent intent_liuLan = new Intent(MainActivity.this, DrinkViewPaper.class);
				startActivity(intent_liuLan);
				break;
			case R.id.btn_jiehao:
				Intent intent_jieshao = new Intent(MainActivity.this, KindergartenViewPaper.class);
				startActivity(intent_jieshao);
				break;
			case R.id.btn_jiaofei:
				Intent intent_jiaofei = new Intent(MainActivity.this, SetPayActivity.class);
				startActivity(intent_jiaofei);
				break;
			case R.id.btn_gonggao:
				Intent intent_gonggao = new Intent(MainActivity.this, GongGaoActivity.class);
				startActivity(intent_gonggao);
				break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		initPic();
	}

}
