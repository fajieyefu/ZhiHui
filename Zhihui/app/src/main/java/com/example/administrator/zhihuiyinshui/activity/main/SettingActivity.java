package com.example.administrator.zhihuiyinshui.activity.main;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.ActionSheet;
import com.example.administrator.zhihuiyinshui.activity.bean.AreaActivity;
import com.example.administrator.zhihuiyinshui.activity.Utils.CacheForUserInformation;
import com.example.administrator.zhihuiyinshui.activity.bean.CircleTransform;
import com.example.administrator.zhihuiyinshui.activity.Utils.HttpAssist;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Fajieyefu on 2016/7/5.
 */
public class SettingActivity extends Activity implements View.OnClickListener, ActionSheet.MenuItemClickListener {
	private LinearLayout changeImage, nickname, sex, birthday, address;
	private TextView nickname_text, address_text;
	private TextView introduce;
	private TextView datePicker, sexPicker;
	private static final int CODE_CAMERA_REQUEST = 1;
	private static final int CODE_GALLERY_REQUEST = 2;
	private static final int CODE_RESULT_REQUEST = 3;
	private static final int CODE_AREA_REQUEST = 4;
	private ImageView picture;
	private String mCurrentPhotoPath;
	private Uri imageUri, outputImageUri;
	private String imagePath;
	private SharedPreferences pref;
	private String account;
	private File outputImageFile;
	private static int output_X = 480;
	private static int output_Y = 480;
	private static Handler handler = new Handler();
	private int mYear, mMonth, mDay;
	private ImageView save, cancel;
	private String nickname_string, sex_string, birthday_string, address_string, introduce_string;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);
		pref = SettingActivity.this.getSharedPreferences("loginInfo", MODE_PRIVATE);
		account = pref.getString("account", "");


		File file = new File(getFilesDir() + "/ASK", account + ".jpg");
		picture = (ImageView) findViewById(R.id.picture);
		changeImage = (LinearLayout) findViewById(R.id.changeImage);
		nickname = (LinearLayout) findViewById(R.id.nickname);
		sex = (LinearLayout) findViewById(R.id.sex);
		birthday = (LinearLayout) findViewById(R.id.birthday);
		address = (LinearLayout) findViewById(R.id.address);
		introduce = (TextView) findViewById(R.id.introduce);
		nickname_text = (TextView) findViewById(R.id.nickname_text);
		address_text = (TextView) findViewById(R.id.address_text);
		datePicker = (TextView) findViewById(R.id.date_picker);
		sexPicker = (TextView) findViewById(R.id.sex_picker);
		save = (ImageView) findViewById(R.id.save);
		cancel = (ImageView) findViewById(R.id.cancel);
		//获得当前时间
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		birthday.setOnClickListener(this);
		//从相册中选择相片
		changeImage.setOnClickListener(this);
		nickname.setOnClickListener(this);
		sex.setOnClickListener(this);
		birthday.setOnClickListener(this);
		address.setOnClickListener(this);
		introduce.setOnClickListener(this);
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
			picture.setImageBitmap(bitmap);
		} else {
			Picasso.with(SettingActivity.this).load("http://123.132.252.2:9891/HelloWeb/uploadImage/" + account + ".jpg").transform(new CircleTransform()).into(picture);
		}
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
		init();

	}


	private void init() {
		nickname_string = pref.getString("nickname", "");
		sex_string = pref.getString("sex", "");
		birthday_string = pref.getString("birthday", "");
		address_string = pref.getString("address", "");
		introduce_string = pref.getString("introduce", "");
		nickname_text.setText(nickname_string);
		sexPicker.setText(sex_string);
		datePicker.setText(birthday_string);
		address_text.setText(address_string);
		introduce.setText(introduce_string);
	}

//    private Dialog showDatePickerDialog() {
//        return new DatePickerDialog(this,
//                mDateSetListener,
//                mYear, mMonth, mDay);
//    }

	private void updateDisplay() {
		datePicker.setText(
				new StringBuilder().append(mYear).append("-")
						.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
						.append((mDay < 10) ? "0" + mDay : mDay).toString());
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {

				public void onDateSet(DatePicker view, int year,
									  int monthOfYear, int dayOfMonth) {
					mYear = year;
					mMonth = monthOfYear;
					mDay = dayOfMonth;
					updateDisplay();
				}
			};

	/**
	 * 点击设置头像按钮，弹出选择对话框：拍照或者从相册中选择
	 *
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.changeImage:
				final String[] items = {"拍照上传", "从相册中选择"};
				new AlertDialog.Builder(SettingActivity.this).setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
							case 0:
								chooseHeadImageFromCameraCapture();
								break;
							case 1:
								chooseHeadImageFromGallery();
								break;
							default:
								break;
						}
					}
				}).show();
				break;
			case R.id.nickname:
				saveEdit(nickname_text, "请输入昵称");
				break;
			case R.id.address:
				//saveEdit(address_text,"请输入所在地");
				Intent intent = new Intent(this, AreaActivity.class);
				startActivityForResult(intent, CODE_AREA_REQUEST);
				break;
			case R.id.introduce:
				saveEdit(introduce, "请自我介绍");
				break;
			case R.id.birthday:
				Dialog dialog = new DatePickerDialog(SettingActivity.this,
						mDateSetListener,
						mYear, mMonth, mDay);
				dialog.show();
				break;
			case R.id.sex:
				setTheme(R.style.ActionSheetStyleIOS7);
				showActionSheet();
				break;
			case R.id.save:
				getChangeContent();//获取改变的内容
				save();
				break;
			case R.id.cancel:
				finish();
				break;
			default:
				break;

		}
	}

	private void save() {
		dialog = new ProgressDialog(this);
		dialog.setTitle("正在保存资料");
		dialog.setMessage("请稍后...");
		dialog.setCancelable(false);
		dialog.show();
		new Thread(new MyThread()).start();
	}

	private void getChangeContent() {
		nickname_string = nickname_text.getText().toString();
		sex_string = sexPicker.getText().toString();
		birthday_string = datePicker.getText().toString();
		address_string = address_text.getText().toString();
		introduce_string = introduce.getText().toString();
	}

	//设置显示性别选择框
	public void showActionSheet() {
		ActionSheet menuView = new ActionSheet(this);
		menuView.setCancelButtonTitle("取消");// before add items
		menuView.addItems("男", "女");
		menuView.setItemClickListener(this);
		menuView.setCancelableOnTouchMenuOutside(true);
		menuView.showMenu();
	}

	@Override
	public void onItemClick(int itemPosition) {
		if (itemPosition == 0) {
			sexPicker.setText("男");
		} else if (itemPosition == 1) {
			sexPicker.setText("女");
		}
	}

	private void saveEdit(final TextView editText, String hint_String) {
		String current_text = editText.getText().toString();
		final Dialog dialog = new Dialog(this, R.style.Dialog);
		View contentView = LayoutInflater.from(this).inflate(R.layout.edit_dialog, null);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(contentView);
		dialog.show();
		setDialogWindowAttr(dialog, SettingActivity.this);
		Button save = (Button) contentView.findViewById(R.id.sumbit);
		Button cancel = (Button) contentView.findViewById(R.id.cancel);
		final EditText editText_dialog = (EditText) contentView.findViewById(R.id.edit);
		editText_dialog.setHint(hint_String);
		editText_dialog.setText(current_text);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = editText_dialog.getText().toString();
				if (text.equals("")) {
					text = "匿名";
				}
				editText.setText(text);
				dialog.dismiss();
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	public static void setDialogWindowAttr(Dialog dlg, Context ctx) {
		Window window = dlg.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		DisplayMetrics metric = ctx.getResources().getDisplayMetrics();//用于获取屏幕的尺寸
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels; // 屏幕高度（像素）
		float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
		int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
		lp.gravity = Gravity.CENTER;
		lp.height = (int) (height * 0.6); // 高度设置为屏幕的0.6
		lp.width = (int) (width * 0.95); // 宽度设置为屏幕的0.95
		dlg.getWindow().setAttributes(lp);
	}

	/**
	 * 从相册中选择照片
	 */
	private void chooseHeadImageFromGallery() {
		Intent intentFromGallery = new Intent();
		intentFromGallery.setType("image/*");//设置为选择图片
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);

	}


	/**
	 * 拍照选择照片
	 */
	private void chooseHeadImageFromCameraCapture() {
		Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));

//        if (intentFromCapture.resolveActivity(getPackageManager()) != null) {
//            File acheFile = null;
//            try {
//                acheFile = createImageFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (acheFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        acheFile);
//                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
		startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
//	}
//}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case CODE_GALLERY_REQUEST://如果来自本地,直接裁剪图片
					cropRawPhoto(intent.getData());
					break;
				case CODE_CAMERA_REQUEST://如果来自拍照，拿到拍照存放的地址，然后截取图片；
					File f = new File(Environment.getExternalStorageDirectory(), "head.jpg");
					cropRawPhoto(Uri.fromFile(f));
					break;
				case CODE_RESULT_REQUEST:
					if (intent != null) {
						setImageToHeadView(intent);//设置图片框
					}
					break;
				case CODE_AREA_REQUEST:
					String returnData = intent.getStringExtra("data_return");
					address_text.setText(returnData);
					break;


				default:
					break;
			}
		}

	}

	/**
	 * 提取保存裁剪后的图片数据，并设置头像部分View;
	 *
	 * @param intent
	 */
	private void setImageToHeadView(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			picture.setImageBitmap(photo);
			//新建文件夹 先选好路径 再调用 mkdir函数
			File nf = new File(getFilesDir() + "/ASK");
			nf.mkdir();
			final File f = new File(getFilesDir() + "/ASK", account + ".jpg");
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(f);
				photo.compress(Bitmap.CompressFormat.PNG, 90, out);

				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					HttpAssist.uploadFile(f);
				}
			}).start();

		}
	}

	/**
	 * 裁剪图片
	 *
	 * @param uri
	 */
	private void cropRawPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//设置裁剪
		intent.putExtra("crop", "true");
		//aspectX,aspectY:宽高比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		//设置图片宽高
		intent.putExtra("outputX", output_X);
		intent.putExtra("outputY", output_Y);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CODE_RESULT_REQUEST);

	}

	private File createImageFile() throws IOException {
		// Create an image file name

		String imageFileName = account;
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir       /* directory */
		);
		mCurrentPhotoPath = image.getAbsolutePath();
		// Save a file: path for use with ACTION_VIEW intents
		return image;
	}


	private class MyThread implements Runnable {
		@Override
		public void run() {
			String info = WebService.save(account, nickname_string, sex_string, birthday_string, address_string, introduce_string);

			switch (Integer.parseInt(info)) {
				case 1:
					CacheForUserInformation cacheInfo = new CacheForUserInformation();
					cacheInfo.cacheinfo();
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(SettingActivity.this, "信息保存成功！", Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}
					});
					break;
				case -1:
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(SettingActivity.this, "信息保存失败！请稍后重试", Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}
					});
					break;
				default:
					break;

			}

		}
	}


}
