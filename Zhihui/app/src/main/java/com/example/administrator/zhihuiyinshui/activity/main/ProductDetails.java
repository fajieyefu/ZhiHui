package com.example.administrator.zhihuiyinshui.activity.main;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.BaseActivity;
import com.example.administrator.zhihuiyinshui.activity.bean.CityModel;
import com.example.administrator.zhihuiyinshui.activity.bean.DistrictModel;
import com.example.administrator.zhihuiyinshui.activity.bean.MinLayoutAsyncTask;
import com.example.administrator.zhihuiyinshui.activity.bean.ProductInfo;
import com.example.administrator.zhihuiyinshui.activity.bean.ProvinceModel;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;
import com.example.administrator.zhihuiyinshui.activity.service.XmlParserHandler;
import com.example.administrator.zhihuiyinshui.activity.widget.OnWheelChangedListener;
import com.example.administrator.zhihuiyinshui.activity.widget.WheelView;
import com.example.administrator.zhihuiyinshui.activity.widget.adapters.ArrayWheelAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by qiancheng on 2016/11/10.
 */
public class ProductDetails extends BaseActivity implements OnWheelChangedListener, View.OnTouchListener, View.OnClickListener {
	private ImageView proImg;//商品图片
	private TextView proName;//商品名称
	private TextView proExpress;//快递费
	private TextView proStock;//库存
	private TextView proPoints;//积分
	private TextView proQuantity;//数量
	private Button subtractionButton;//减号
	private Button additionButton;//加号
	private TextView receiptAddress;//收货地址
	private TextView proPrice;//商品价格
	private String pro_img, pro_name, pro_stock, pro_points, receipt_address, pro_price;
	private int pro_quantity = 1;
	private int pro_stock_int;
	private float pro_price_int;
	private ScrollView bottom_content;//底层要显示的内容布局
	private LinearLayout middlecontent_second;//蒙版布局
	private RelativeLayout receipt_address_layout;//控制滑动的布局
	private Button purchase_button;
	private FrameLayout middleLayout;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private Button mBtnAddArea;
	private ListView addressListView;
	private ArrayAdapter<String> adapter;
	private List<String> list;
	private EditText address_edit;
	private String address_string;
	private String username;
	private ImageButton addAddressButton;
	private Dialog dialog;
	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode = "";

	/**
	 * 解析省市区的XML数据
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setContentView(R.layout.product_detail);
		findById();
		initIntentData();
		initData();


	}

	private void initData() {
		SharedPreferences sp = ProductDetails.this.getSharedPreferences("loginInfo", MODE_PRIVATE);
		String receipt_address_string = sp.getString("receipt_address", "");
		username = sp.getString("account", "");
		Picasso.with(this).load("http://121.199.22.246:9891/ZhiHuiWeb/LoadFileServlet?fileName=pikaqiu.jpg").into(proImg);
		proName.setText(pro_name);
		proStock.setText(pro_stock);
		proPoints.setText(pro_points);
		proPrice.setText(pro_price);
		additionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pro_quantity++;
				judgeButtonOk();
				proQuantity.setText(pro_quantity + "");
			}
		});

		subtractionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pro_quantity--;
				judgeButtonOk();
				proQuantity.setText(pro_quantity + "");
			}
		});
		receiptAddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		list = new ArrayList<>();
		if (!receipt_address_string.equals("")) {
			try {
				JSONArray receipt_address_array = new JSONArray(receipt_address_string);
				int array_len = receipt_address_array.length();
				int num = 0;
				while (num < array_len) {
					JSONObject jsonObject = receipt_address_array.getJSONObject(num);
					String receipt_address = jsonObject.getString("receipt_address");
					list.add(receipt_address);
					num++;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
		addressListView.setAdapter(adapter);
		addressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String temp = list.get(position);
				receiptAddress.setText(temp);
				onTouch(middlecontent_second, null);//调用touch方法来关闭地址操作页面
			}
		});


	}

	/**
	 * 加载Intent传来的数据
	 */
	private void initIntentData() {
		Intent intent = getIntent();
		ProductInfo productInfo = (ProductInfo) intent.getSerializableExtra("productInfo");
		pro_img = productInfo.getPro_img();
		pro_name = productInfo.getPro_name();
		pro_points = productInfo.getPro_points();
		pro_stock = productInfo.getPro_stock();
		pro_stock_int = Integer.parseInt(pro_stock);
		pro_price = productInfo.getPrice();
		Log.i("pro_price", pro_price);
		pro_price_int = Float.parseFloat(pro_price);


	}

	/**
	 * 加载控件
	 */
	private void findById() {
		proImg = (ImageView) findViewById(R.id.pro_img);
		proName = (TextView) findViewById(R.id.pro_name);
		proExpress = (TextView) findViewById(R.id.express);
		proStock = (TextView) findViewById(R.id.stock);
		proPoints = (TextView) findViewById(R.id.pro_points);
		proQuantity = (TextView) findViewById(R.id.quantity);
		subtractionButton = (Button) findViewById(R.id.subtraction);
		additionButton = (Button) findViewById(R.id.addition);
		receiptAddress = (TextView) findViewById(R.id.receipt_address_purchase);
		proPrice = (TextView) findViewById(R.id.price);
		middleLayout = (FrameLayout) findViewById(R.id.middle_layout);
		bottom_content = (ScrollView) findViewById(R.id.bottom_content);
		middlecontent_second = (LinearLayout) findViewById(R.id.middlecontent_second);
		receipt_address_layout = (RelativeLayout) findViewById(R.id.receipt_address_layout);
		purchase_button = (Button) findViewById(R.id.purchase_button);
//		mViewProvince = (WheelView) findViewById(R.id.province_address);
//		mViewCity = (WheelView) findViewById(R.id.city_address);
//		mViewDistrict = (WheelView) findViewById(R.id.district_address);
		addressListView = (ListView) findViewById(R.id.receipt_address);
		addAddressButton = (ImageButton) findViewById(R.id.addAddressButton);
		addAddressButton.setOnClickListener(this);
		receiptAddress.setOnTouchListener(this);
		middlecontent_second.setOnTouchListener(this);
		purchase_button.setOnClickListener(this);
	}

	/**
	 * 判断是否减号按钮是否能够使用
	 */
	void judgeButtonOk() {
		switch (pro_quantity) {
			case 1:
				subtractionButton.setEnabled(false);
				break;
			case 2:

				subtractionButton.setEnabled(true);
				break;
			default:
				subtractionButton.setEnabled(true);
				break;
		}


	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		MinLayoutAsyncTask asyncTask = null;
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int layout_height = receipt_address_layout.getHeight();
		int scale = (int) metrics.density;
		int slip_width = layout_height;
		switch (v.getId()) {
			case R.id.receipt_address_purchase:
				//地址布局设置成显示状态
				receipt_address_layout.setVisibility(View.VISIBLE);
				//蒙版布局设置设置成显示状态
				middlecontent_second.setVisibility(View.VISIBLE);
				//设置蒙版的透明度
				middlecontent_second.getBackground().setAlpha(50);
				//实力滑动任务 参数1：要滑动的layout,参数2：最终的滑动距离 参数3：移动方向
				asyncTask = new MinLayoutAsyncTask(middleLayout, slip_width, "down");
				//开始任务 参数每次滑动的距离
				asyncTask.execute(200);
				break;
			case R.id.middlecontent_second:
				asyncTask = new MinLayoutAsyncTask(middlecontent_second, middleLayout, 0, "up");
				asyncTask.execute(-200);
				break;

		}
		return true;
	}

	private void setUpListener() {

		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.addAddressButton:
				initDialog();
				break;
			case R.id.addAreaBtn:
				addAreaMethod();
				break;
			case R.id.purchase_button:
				choosePayType();
				break;

		}

	}

	private void choosePayType() {

	}

	private void addAreaMethod() {
		String address_details = address_edit.getText().toString();
		if (TextUtils.isEmpty(address_details)) {
			Toast toast = Toast.makeText(ProductDetails.this, "请填写详细的收货地址", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return;
		}
		address_string = mCurrentProviceName + mCurrentCityName + mCurrentDistrictName + address_details;
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("username", username);
			jsonObject.put("address", address_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new AsyncTask<Void, Void, Integer>() {
			String msg = "";

			@Override
			protected Integer doInBackground(Void... params) {
				String info = WebService.addReceiptAddress(jsonObject.toString());

				if (!info.equals("-1")) {
					try {
						JSONObject jsonObject1 = new JSONObject(info);
						int code = jsonObject1.getInt("code");
						msg = jsonObject1.getString("msg");
						if (code == 0) {
							list.clear();
							JSONArray jsonArray = jsonObject1.getJSONArray("data");
							int len = jsonArray.length();
							int num = 0;
							while (num < len) {
								JSONObject jo_address = jsonArray.getJSONObject(num);
								String address = jo_address.getString("address");
								list.add(address);
								num++;
							}
						}
						return code;
					} catch (JSONException e) {
						return -1;
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Integer integer) {
				super.onPostExecute(integer);
				Toast toast = Toast.makeText(ProductDetails.this, "地址设置成功", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				switch (integer) {
					case -1:
						toast.setText("请检查网络是否可用");
						break;
					case 0:
						adapter.notifyDataSetChanged();
						if (dialog != null) {
							dialog.dismiss();
						}
						break;
					case 301:
						toast.setText("数据处理出现异常");
						break;

				}
			}
		}.execute();
	}

	private void initDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.choose_receipt_address, null);
		dialog = new Dialog(this);
		mViewProvince = (WheelView) view.findViewById(R.id.province_address);
		mViewCity = (WheelView) view.findViewById(R.id.city_address);
		mViewDistrict = (WheelView) view.findViewById(R.id.district_address);
		mBtnAddArea = (Button) view.findViewById(R.id.addAreaBtn);
		address_edit = (EditText) view.findViewById(R.id.address_details_2);
		mBtnAddArea.setOnClickListener(this);
		dialog.setTitle("添加新地址");
		dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		dialog.show();
		setUpListener();
		setUpData();


	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
//        AssetManager asset = getAssets();
		try {
//          InputStream input = asset.open("assets/province_data.xml");
			InputStream input = ProductDetails.this.getClass().getClassLoader().getResourceAsStream("assets/province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			//*/ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			//*/
			System.out.println("省份数量：" + provinceList.size());
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j).getDistrictList();
					String[] distrinctNameArray = new String[districtList.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[]{""};
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[]{""};
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}
}
