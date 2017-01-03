package com.example.administrator.zhihuiyinshui.activity.test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
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
public class SettingActivity2 extends Activity implements View.OnClickListener, ActionSheet.MenuItemClickListener {
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
	private Uri imgUri, outputImageUri;
	private String imagePath = "/sdcard/myHead/";//sd路径
	private SharedPreferences pref;
	private String account;
	private File outputImageFile;
	private static Handler handler = new Handler();
	private int mYear, mMonth, mDay;
	private ImageView save, cancel;
	private String nickname_string, sex_string, birthday_string, address_string, introduce_string;
	private ProgressDialog dialog;
	private Bitmap head;
	private String hp_name;
	//版本比较：是否是4.4版本以上的系统
	final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	//保存图片本地路径
	public static final String ACCOUNT_DIR = Environment.getExternalStorageDirectory().getPath()
			+ "/account/";
	public static final String ACCOUNT_MAINTRANCE_ICON_CACHE = "icon_cache/";
	private static final String IMGPATH = ACCOUNT_DIR + ACCOUNT_MAINTRANCE_ICON_CACHE;

	private static final String IMAGE_FILE_NAME = "faceImage.jpeg";
	private String TMP_IMAGE_FILE_NAME;
	//常量定义
	public static final int TAKE_A_PICTURE = 10;
	public static final int SELECT_A_PICTURE = 20;
	public static final int SET_PICTURE = 30;
	public static final int SET_ALBUM_PICTURE_KITKAT = 40;
	public static final int SELECET_A_PICTURE_AFTER_KIKAT = 50;
	private String mAlbumPicturePath = null;

	File fileone = null;
	File filetwo = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);
		pref = SettingActivity2.this.getSharedPreferences("loginInfo", MODE_PRIVATE);
		account = pref.getString("account", "");
		TMP_IMAGE_FILE_NAME = account + ".jpg";
		File directory = new File(ACCOUNT_DIR);
		File imagepath = new File(IMGPATH);
		if (!directory.exists()) {
			directory.mkdir();
		}
		if (!imagepath.exists()) {
			imagepath.mkdir();
		}
		fileone = new File(IMGPATH, IMAGE_FILE_NAME);
		filetwo = new File(IMGPATH, TMP_IMAGE_FILE_NAME);
		try {
			if (!fileone.exists() && !filetwo.exists()) {
				fileone.createNewFile();
				filetwo.createNewFile();
			}
		} catch (Exception e) {
		}


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
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
		if (filetwo.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(filetwo.getPath());
			picture.setImageBitmap(bitmap);
		} else {
			Picasso.with(SettingActivity2.this).load("http://121.199.22.246:9891/ZhiHuiWeb/uploadImage/" + account + ".jpg").transform(new CircleTransform()).into(picture);
		}
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
				new AlertDialog.Builder(SettingActivity2.this).setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
							case 0:
								chooseHeadImageFromCameraCapture();//拍照上传
								break;
							case 1: //从相册中选择
//								chooseHeadImageFromGallery();
								if (mIsKitKat) {
									selectImageUriAfterKikat();
								} else {
									cropImageUri();
								}
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
				Dialog dialog = new DatePickerDialog(SettingActivity2.this,
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
		setDialogWindowAttr(dialog, SettingActivity2.this);
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
		Intent intent1 = new Intent(Intent.ACTION_PICK, null);
		intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent1, CODE_GALLERY_REQUEST);

	}


	/**
	 * 拍照选择照片
	 */
	private void chooseHeadImageFromCameraCapture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
		startActivityForResult(intent, TAKE_A_PICTURE);
	}

	/**
	 * 活动回调
	 *
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode == RESULT_OK) {
//			switch (requestCode) {
//				case CODE_GALLERY_REQUEST://如果来自本地,直接裁剪图片
//					cropPhoto(data.getData());
//					break;
//				case CODE_CAMERA_REQUEST://如果来自拍照，拿到拍照存放的地址，然后截取图片；
//					File temp = new File(Environment.getExternalStorageDirectory() +"/"+hp_name);
//					cropPhoto(Uri.fromFile(temp));// 裁剪图片
//					break;
//				case CODE_RESULT_REQUEST:
//					if (data != null) {
//						Bundle extras = data.getExtras();
//						head = extras.getParcelable("data");
//						if (head != null) {
//							/**
//							 * 上传服务器代码
//							 */
//							setPicToView(head);// 保存在SD卡中
//							picture.setImageBitmap(head);// 用ImageView显示出来
//						}
//					}
//					break;
//				case CODE_AREA_REQUEST:
//					String returnData = data.getStringExtra("data_return");
//					address_text.setText(returnData);
//					break;
//
//				default:
//					break;
//			}
//		}
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case SELECET_A_PICTURE_AFTER_KIKAT://从相册中选取照片
					if (null != data) {
						mAlbumPicturePath = getPath(getApplicationContext(), data.getData());
						cropImageUriAfterKikat(Uri.fromFile(new File(mAlbumPicturePath)));
//						cropImageUriAfterKikat(Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
					}
					break;
				case TAKE_A_PICTURE://拍照选取照片
					cameraCropImageUri(Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
					break;
				case SELECT_A_PICTURE://4.4以下截取圖片返回處理
					if (null != data) {
						Log.i("zou", "4.4以下的");
						Bitmap bitmap = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
						picture.setImageBitmap(bitmap);
					}
					break;
				case SET_ALBUM_PICTURE_KITKAT://4.4以上截取图片返回处理
					Bitmap bitmap = decodeUriAsBitmap(Uri.fromFile(new File(mAlbumPicturePath)));
					Log.i("zou", "4.4以上上的 RESULT_OK");
					picture.setImageBitmap(bitmap);
					uploadPic(bitmap);
					break;
				case SET_PICTURE://拍照的设置头像  不考虑版本
					if (data != null) {
						Bitmap bitmap2 = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
						picture.setImageBitmap(bitmap2);
						uploadPic(bitmap2);

					}
					break;
				case CODE_AREA_REQUEST://设置地址，此处设计不合理，后期需要修改
					String returnData = data.getStringExtra("data_return");
					address_text.setText(returnData);
					break;
			}
		}

	}

	private void uploadPic(Bitmap bitmap) {
		FileOutputStream out = null;
		final File file = new File(IMGPATH, TMP_IMAGE_FILE_NAME);
		try {
			out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 10, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpAssist.uploadFile(file);
			}
		}).start();
	}


//	private void setPicToView(Bitmap mBitmap) {
//		String sdStatus = Environment.getExternalStorageState();
//		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
//			return;
//		}
//		FileOutputStream b = null;
//		File file = new File(imagePath);
//		file.mkdirs();// 创建文件夹
//		final String fileName = imagePath + hp_name;// 图片名字
//		try {
//			b = new FileOutputStream(fileName);
//			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				// 关闭流
//				b.flush();
//				b.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				File f = new File(fileName);
//				HttpAssist.uploadFile(f);
//			}
//		}).start();
//
//
//	}

//	/**
//	 * 裁剪图片
//	 *
//	 * @param uri
//	 */
//	public void cropPhoto(Uri uri) {
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
//		intent.putExtra("crop", "true");
//		// aspectX aspectY 是宽高的比例
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//		// outputX outputY 是裁剪图片宽高
//		intent.putExtra("outputX", 150);
//		intent.putExtra("outputY", 150);
//		intent.putExtra("return-data", true);
//		startActivityForResult(intent, CODE_RESULT_REQUEST);
//	}


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
							Toast.makeText(SettingActivity2.this, "信息保存成功！", Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}
					});

					break;
				case -1:
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(SettingActivity2.this, "信息保存失败！请稍后重试", Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}
					});
					break;
				default:
					break;

			}

		}
	}

	/**
	 * <br>功能简述:裁剪图片方法实现---------------------- 相册
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	private void cropImageUri() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 10);
		intent.putExtra("aspectY", 10);
		intent.putExtra("outputX", 380);
		intent.putExtra("outputY", 380);
		intent.putExtra("scaleUpIfNeeded",true);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, SELECT_A_PICTURE);
	}


	/**
	 * <br>功能简述:4.4以上裁剪图片方法实现---------------------- 相册
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void selectImageUriAfterKikat() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
		startActivityForResult(intent, SELECET_A_PICTURE_AFTER_KIKAT);
	}

	/**
	 * <br>功能简述:裁剪图片方法实现----------------------相机
	 * <br>功能详细描述:
	 * <br>注意:
	 *
	 * @param uri
	 */
	private void cameraCropImageUri(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 10);
		intent.putExtra("aspectY", 10);
		intent.putExtra("outputX", 380);
		intent.putExtra("outputY", 380);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded",true);
		//		if (mIsKitKat) {
		//			intent.putExtra("return-data", true);
		//			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		//		} else {
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		//		}
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, SET_PICTURE);
	}

	/**
	 * <br>功能简述: 4.4及以上改动版裁剪图片方法实现 --------------------相机
	 * <br>功能详细描述:
	 * <br>注意:
	 *
	 * @param uri
	 */
	private void cropImageUriAfterKikat(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 10);
		intent.putExtra("aspectY", 10);
		intent.putExtra("outputX", 380);
		intent.putExtra("outputY", 380);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded",true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", true);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			intent.putExtra("return-data", false);//设置为不返回数据
		} else {
			intent.putExtra("return-data", true);//设置为不返回数据
		}
		startActivityForResult(intent, SET_ALBUM_PICTURE_KITKAT);
	}

	/**
	 * <br>功能简述:
	 * <br>功能详细描述:
	 * <br>注意:
	 *
	 * @param uri
	 * @return
	 */
	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * <br>功能简述:4.4及以上获取图片的方法
	 * <br>功能详细描述:
	 * <br>注意:
	 *
	 * @param context
	 * @param uri
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[]{split[1]};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {column};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

}





