package com.example.administrator.zhihuiyinshui.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.ProductInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by qiancheng on 2016/11/9.
 */
public class ProductAdapter extends BaseAdapter {
	private Context mContext;
	private List<ProductInfo> mData;

	public ProductAdapter(Context context, List<ProductInfo> data) {
		mContext = context;
		mData = data;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProductInfo productInfo = mData.get(position);
		ViewHolder_product viewHolder;
		if (convertView==null){
			viewHolder=new ViewHolder_product();
			convertView= LayoutInflater.from(mContext).inflate(R.layout.mall_item,null);
			viewHolder.product_img= (ImageView) convertView.findViewById(R.id.pro_img);
			viewHolder.product_name= (TextView) convertView.findViewById(R.id.pro_name);
			viewHolder.product_price= (TextView) convertView.findViewById(R.id.pro_price);
			convertView.setTag(viewHolder);
		}else{
			viewHolder= (ViewHolder_product) convertView.getTag();
		}
		Picasso.with(mContext).load("http://121.199.22.246:9891/ZhiHuiWeb/LoadFileServlet?fileName=pikaqiu.jpg").resize(100,100).into(viewHolder.product_img);
		viewHolder.product_name.setText(productInfo.getPro_name());
		viewHolder.product_price.setText("¥  "+productInfo.getPrice());
		return convertView;
	}
	private class ViewHolder_product{
		private ImageView product_img;
		private TextView product_name;
		private TextView product_price;
	}
}
