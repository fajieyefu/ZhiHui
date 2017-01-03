package com.example.administrator.zhihuiyinshui.activity.Utils;


import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;
import com.example.administrator.zhihuiyinshui.activity.main.LoginActivity;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by qiancheng on 2016/10/18.
 */
public class UpdateApk extends Service {
	private static String updateMessage;//更新的信息
	private static String apkUrl;//下载的地址
	private static int versionCode;//更新的版本号
	private static int apkCode;//运行的程序的版本号
	private static Context mContext;
	private File apkFile;//下载存放位置
	private int screenHeight;
	private int screenWidth;

	public UpdateApk(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm=context.getResources().getDisplayMetrics();
		screenHeight=dm.heightPixels;
		screenWidth=dm.widthPixels;
	}

	public static boolean isNewVersion(Context context) {
		mContext = context;
		String info = WebService.updateApk();
		try {
			Log.i("info", info);
			JSONObject jsonObject = new JSONObject(info);
			updateMessage = jsonObject.optString("updateMessage");
			apkUrl = jsonObject.optString("url");
			versionCode = jsonObject.optInt("version_code");
			apkCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
			if (apkCode >= versionCode) {
				return true;
			} else {
				return false;
			}

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
	}

	public Dialog updateDialog(final Context context) {
		final Dialog dialog = new Dialog(context);
		View view = LayoutInflater.from(context).inflate(R.layout.update_apk_dialog, null);
		TextView content = (TextView) view.findViewById(R.id.content);
		Button update = (Button) view.findViewById(R.id.update);
		Button cancel = (Button) view.findViewById(R.id.cancel);
		content.setText(updateMessage);
		update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//下载apk
				new UpdateApkAsyncTask(context).execute();
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (dialog != null) {
					dialog.dismiss();
					Intent intent = new Intent(context, LoginActivity.class);
					context.startActivity(intent);

				}
			}
		});
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view, new ViewGroup.LayoutParams(screenWidth/3*2, screenHeight/3));
		dialog.setCanceledOnTouchOutside(false);

		return dialog;

	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	class UpdateApkAsyncTask extends AsyncTask<Void, Integer, Boolean> {
		private Context mContext;
		private int file_size;//文件大小
		private int progress;
		private Dialog dialog;
		private ProgressBar progressBar;
		private Button setup;

		UpdateApkAsyncTask(Context context) {
			mContext = context;
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new Dialog(mContext);
			View view = LayoutInflater.from(mContext).inflate(R.layout.update_progress_dialog, null);
			progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
			setup = (Button) view.findViewById(R.id.setup);
			setup.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					installApk();//安装Apk
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(view, new ViewGroup.LayoutParams(screenWidth/3*2
					, screenHeight/3));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Boolean doInBackground(Void... voids) {
			InputStream in = null;
			FileOutputStream out = null;
			URL url = null;
			try {
				url = new URL(apkUrl);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setDoOutput(false);
				urlConnection.setConnectTimeout(10 * 1000);
				urlConnection.setReadTimeout(10 * 1000);
				urlConnection.setRequestProperty("Connection", "Keep-Alive");
				urlConnection.setRequestProperty("Charset", "UTF-8");
				urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
				urlConnection.connect();
				file_size = urlConnection.getContentLength();
				long bytesum = 0;
				int byteread = 0;
				in = urlConnection.getInputStream();
				File dir = StorageUtils.getCacheDirectory(mContext);
				String apkName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1, apkUrl.length());
				apkFile = new File(dir, apkName);
				out = new FileOutputStream(apkFile);
				byte[] buffer = new byte[1024];
//				int oldProgress = 0;
				while ((byteread = in.read(buffer)) != -1) {
					bytesum += byteread;
					//计算进度条位置
					progress = (int) (((float) bytesum / file_size) * 100);
					publishProgress(progress);
					out.write(buffer, 0, byteread);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			progressBar.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Boolean aBoolean) {
			super.onPostExecute(aBoolean);
			setup.setVisibility(View.VISIBLE);
		}
	}


	private void installApk() {
		if (!apkFile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Log.i("APK", "apk文件存在");
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(i);
	}

}
