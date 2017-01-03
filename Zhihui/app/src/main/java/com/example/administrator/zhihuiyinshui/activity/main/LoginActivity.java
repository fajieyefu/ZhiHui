package com.example.administrator.zhihuiyinshui.activity.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends Activity implements View.OnClickListener {

	private EditText userName, passWord;
	private Button sumbitButton;
	private Button register;
	private CheckBox checkBox;
	private long exitTime;
	private TextView forgotPass;

	private ProgressDialog dialog;
	private String info;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private SharedPreferences pref_loginInfo;
	private SharedPreferences.Editor editor_loginInfo;

	private static Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = pref.edit();
		pref_loginInfo = LoginActivity.this.getSharedPreferences("loginInfo", MODE_PRIVATE);
		userName = (EditText) findViewById(R.id.account);
		passWord = (EditText) findViewById(R.id.password);
		sumbitButton = (Button) findViewById(R.id.sumbit);
		register = (Button) findViewById(R.id.register);
		checkBox = (CheckBox) findViewById(R.id.checkbox);
		forgotPass = (TextView) findViewById(R.id.forgot_pass);
		sumbitButton.setOnClickListener(this);//设置登录按钮监听
		register.setOnClickListener(this);//设置注册按钮监听
		forgotPass.setOnClickListener(this);//设置忘记密码监听
		boolean isRemember = pref.getBoolean("remember_password", false);
		if (isRemember) {
			String account = pref.getString("account", "");
			String pass = pref.getString("pass", "");
			userName.setText(account);
			passWord.setText(pass);
			checkBox.setChecked(true);

		}

	}

	/*
	后续需要修改
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 判断间隔时间 大于2秒就退出应用
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				// 应用名
				String applicationName = getResources().getString(
						R.string.app_name);
				String msg = "再按一次返回键退出" + applicationName;
				//String msg1 = "再按一次返回键回到桌面";
				Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
				// 计算两次返回键按下的时间差
				exitTime = System.currentTimeMillis();
			} else {
				// 关闭应用程序
				//finish();
				// 返回桌面操作
				Intent home = new Intent(Intent.ACTION_MAIN);
				home.addCategory(Intent.CATEGORY_HOME);
				startActivity(home);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.sumbit:

				if (userName.getText().toString().equals("") || passWord.getText().toString().equals("")) {
					Toast toast = Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;
				}
				//检测网络连接
				if (!checkNetwork()) {

					Toast toast = Toast.makeText(LoginActivity.this, "网络未连接", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;
				}
				//提示框
				dialog = new ProgressDialog(this);
				dialog.setTitle("提示");
				dialog.setMessage("正在登陆请稍后...");
				dialog.setCancelable(false);
				dialog.show();
				//创建子线程，进行网络数据的传输
				new Thread(new myThread()).start();
				break;
			case R.id.register:
				Intent i_register = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(i_register);

				break;
			case R.id.forgot_pass:
				Intent i_forgotPass = new Intent(LoginActivity.this, ForgotPass.class);
				startActivity(i_forgotPass);
				break;

		}
	}

	//检测网络
	private boolean checkNetwork() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		return connManager.getActiveNetworkInfo() != null && connManager.getActiveNetworkInfo().isAvailable();
	}

	//子线程接收数据，主线程修改数据
	private class myThread implements Runnable {
		Boolean isSuccess = false;
		String card_code, register_time, pay_starttime, pay_endtime, nickname,
				address, introduce;
		String sex, birthday, relationship, account, cardholder_name,
				kindergarten_code, kindergarten_name, class_name, class_code,
				cardholder_birthday;

		public void run() {
			info = WebService.executeHttpGet(userName.getText().toString(), passWord.getText().toString());
			System.out.print(info);
			try {
				JSONObject jsonObject = new JSONObject(info);
				JSONArray receipt_address_array;
				final int code = jsonObject.getInt("code");
				if (code == 0) {
					receipt_address_array = jsonObject.optJSONArray("receipt_address");
					jsonObject = jsonObject.getJSONObject("data");
					account = jsonObject.optString("account");
					card_code = jsonObject.optString("card_code");
					register_time = jsonObject.optString("register_time");
					pay_starttime = jsonObject.optString("pay_starttime");
					pay_endtime = jsonObject.optString("pay_endtime");
					nickname = jsonObject.optString("nickname");
					address = jsonObject.optString("address");
					introduce = jsonObject.optString("introduce");
					sex = jsonObject.optString("sex");
					birthday = jsonObject.optString("birthday");
					relationship = jsonObject.optString("relationship");
					isSuccess = jsonObject.optBoolean("success");
					cardholder_name = jsonObject.optString("cardholder_name");
					kindergarten_code = jsonObject.optString("kindergarten_code");
					kindergarten_name = jsonObject.optString("kindergarten_name");
					class_code = jsonObject.optString("class_code");
					class_name = jsonObject.optString("class_name");
					cardholder_birthday = jsonObject.optString("cardholder_birthday");
					editor_loginInfo = pref_loginInfo.edit();
					editor_loginInfo.putString("account", account);
					editor_loginInfo.putString("card_code", card_code);
					editor_loginInfo.putString("register_time", register_time);
					editor_loginInfo.putString("pay_starttime", pay_starttime);
					editor_loginInfo.putString("pay_endtime", pay_endtime);
					editor_loginInfo.putString("nickname", nickname);
					editor_loginInfo.putString("address", address);
					editor_loginInfo.putString("introduce", introduce);
					editor_loginInfo.putString("sex", sex);
					editor_loginInfo.putString("birthday", birthday);
					editor_loginInfo.putString("relationship", relationship);
					editor_loginInfo.putString("cardholder_name", cardholder_name);
					editor_loginInfo.putString("kindergarten_code", kindergarten_code);
					editor_loginInfo.putString("kindergarten_name", kindergarten_name);
					editor_loginInfo.putString("class_name", class_name);
					editor_loginInfo.putString("class_code", class_code);
					editor_loginInfo.putString("cardholder_birthday", cardholder_birthday);
					if (receipt_address_array.length() != 0) {
						editor_loginInfo.putString("receipt_address", receipt_address_array.toString());
					}
					editor_loginInfo.apply();
				}
				handler.post(new Runnable() {
					@Override
					public void run() {

						if (code == 0) {
							String account = userName.getText().toString();
							String pass = passWord.getText().toString();

							if (checkBox.isChecked()) {//檢查复选框是否选中
								editor.putBoolean("remember_password", true);
								editor.putString("account", account);
								editor.putString("pass", pass);
							} else {
								editor.clear();
							}
							editor.apply();

							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							startActivity(intent);
							finish();

						} else {
							Toast toast = Toast.makeText(LoginActivity.this, "账号或密码不正确", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}

						if (dialog != null)
							dialog.dismiss();
					}
				});


			} catch (JSONException e) {
				e.printStackTrace();
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (dialog != null) {
							dialog.dismiss();
							Toast.makeText(LoginActivity.this, "服务器出现错误", Toast.LENGTH_SHORT).show();
						}
					}
				});


			}


		}
	}
}
