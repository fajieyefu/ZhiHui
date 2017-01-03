package com.example.administrator.zhihuiyinshui.activity.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.adapter.ProductAdapter;
import com.example.administrator.zhihuiyinshui.activity.bean.BaseActivity;
import com.example.administrator.zhihuiyinshui.activity.bean.ProductInfo;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;
import com.example.administrator.zhihuiyinshui.activity.layout.TitleLayout;

import net.sf.json.util.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiancheng on 2016/11/9.
 */
public class MallActivity extends BaseActivity {
	private GridView mall_gv;
	private List<ProductInfo> list;
	private ProductAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mall_layout);
		TitleLayout mall_title = (TitleLayout) findViewById(R.id.mall_title);
		list = new ArrayList<>();
		mall_title.setTitleText("商城");
		mall_gv = (GridView) findViewById(R.id.mall_gv);
		adapter = new ProductAdapter(this, list);
		mall_gv.setAdapter(adapter);
		initData();
		mall_gv.setOnItemClickListener(new GVItemOnClickListener());
	}
	class GVItemOnClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ProductInfo productInfo = list.get(position);
			Intent intent = new Intent(MallActivity.this,ProductDetails.class);
			intent.putExtra("productInfo", (Serializable) productInfo);
			startActivity(intent);
		}
	}

	private void initData() {
		new AsyncTask<Void, Void, Integer>() {
			String msg;
			int code;

			@Override
			protected Integer doInBackground(Void... params) {
				String info = WebService.getProductList();
				Log.i("info", info);
				try {
					JSONObject jsonObject = new JSONObject(info);
					code = jsonObject.getInt("code");
					msg = jsonObject.getString("msg");
					if (code != 0) {
						return 301;
					}
					JSONArray data_jsonArray = jsonObject.getJSONArray("data");
					int len = data_jsonArray.length();
					int i = 0;
					while (i < len) {

						JSONObject data_jsonObject = data_jsonArray.getJSONObject(i);
						String pro_id = data_jsonObject.optString("pro_id");
						String pro_spec = data_jsonObject.optString("pro_spec");
						String pro_name = data_jsonObject.optString("pro_name");
						String pro_stock = data_jsonObject.optString("pro_stock");
						String pro_spec_code = data_jsonObject.optString("pro_spec_code");
						String pro_price = data_jsonObject.optString("pro_price");
						String pro_img = data_jsonObject.optString("pro_img");
						String pro_points = data_jsonObject.optString("pro_points");
						ProductInfo productInfo = new ProductInfo(null, pro_spec_code, pro_spec, pro_id, pro_name, pro_price, pro_points, pro_stock, pro_img);
						list.add(productInfo);
						i++;
					}
				} catch (JSONException e) {
					return -1;
				}
				return 0;
			}

			@Override
			protected void onPostExecute(Integer integer) {
				super.onPostExecute(integer);
				switch (integer) {
					case 0:
						adapter.notifyDataSetChanged();
						break;
					case 301:
						Toast.makeText(MallActivity.this, msg, Toast.LENGTH_SHORT).show();
						break;
					case -1:
						Toast.makeText(MallActivity.this, "服务器连接错误,请检查网络", Toast.LENGTH_SHORT).show();
						break;

				}
			}
		}.execute();

	}
}
