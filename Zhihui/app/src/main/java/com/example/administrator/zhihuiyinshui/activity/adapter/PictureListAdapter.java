package com.example.administrator.zhihuiyinshui.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.Utils.CacheForUserInformation;
import com.example.administrator.zhihuiyinshui.activity.bean.DrinkInfo;
import com.example.administrator.zhihuiyinshui.activity.main.ImageBrowseActivity;
import com.example.administrator.zhihuiyinshui.activity.picbrowse.ImageBDInfo;
import com.example.administrator.zhihuiyinshui.activity.picbrowse.ImageLoaders;
import com.example.administrator.zhihuiyinshui.activity.text.DrinkTodayFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Fajieyefu on 2016/6/24.
 */
public class PictureListAdapter extends BaseAdapter {
	private List<DrinkInfo> mData;
	private LayoutInflater myInflater;
	private String cardholder_name, class_name;
	private int age;
	private Context mContext;
	private int mResource;
	private ImageLoader imageLoader;
	private DrinkInfo drinkInfo;
	private ImageBDInfo bdInfo;


	public PictureListAdapter(Context context, int resource, List<DrinkInfo> data) {
		this.mData = data;
		this.mContext = context;
		mResource = resource;
		myInflater = LayoutInflater.from(context);
		CacheForUserInformation cache = new CacheForUserInformation();
		cardholder_name = cache.getCardholder_name();
		class_name = cache.getClass_name();
		age = cache.getAge();
	}


	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int i) {
		return mData.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		drinkInfo = (DrinkInfo) getItem(position);
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = myInflater.inflate(mResource, null);
			viewHolder.drinkPic = (ImageView) convertView.findViewById(R.id.drink_pic);
			viewHolder.drinkTime = (TextView) convertView.findViewById(R.id.drink_time);
			viewHolder.drinkDay = (TextView) convertView.findViewById(R.id.drink_day);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.age = (TextView) convertView.findViewById(R.id.age);
			viewHolder.class_name = (TextView) convertView.findViewById(R.id.class_name);
			viewHolder.drinkPic.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					ImageBrowseActivity.startActivity(mContext,mData,position);
				}
			});
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();

		}

		viewHolder.drinkTime.setText(drinkInfo.getDrink_time());
		viewHolder.drinkDay.setText(drinkInfo.getDrink_day());
		viewHolder.name.setText(cardholder_name);
		viewHolder.age.setText(age + "");
		viewHolder.class_name.setText(class_name);
		System.out.println(drinkInfo.getDrink_pic());
		if (!TextUtils.isEmpty(drinkInfo.getDrink_pic())) {
			Picasso.with(mContext).load(drinkInfo.getDrink_pic()).into(viewHolder.drinkPic);
//			ImageLoaders.setsendimg(drinkInfo.getDrink_pic(),viewHolder.drinkPic);

		}
//		viewHolder.drinkPic.setOnClickListener(new ImageOnclick(position,viewHolder.drinkPic));
		return convertView;


	}

	class ViewHolder {
		TextView drinkTime;
		TextView drinkDay;
		TextView name;
		TextView age;
		TextView class_name;
		ImageView drinkPic;

	}
//	private class ImageOnclick implements View.OnClickListener {
//		private int index;
//		private ImageView imageView;
//		public ImageOnclick(int position, ImageView drinkPic) {
//			index=position;
//			imageView=drinkPic;
//		}
//
//
//		@Override
//		public void onClick(View v) {
//			View c;
//			switch(mResource){
//				case R.layout.picture_browse_item_pic_left:
//					c=
//			}
//			Activity activity = (Activity)mContext;
//			c = activity.listView.getChildAt(0);//返回的是这个listView当前显示出来的item中的第pos项，也就是说索引是从显示出来的第一项开始计算的;
//			int top = c.getTop();
//			int firstVisiblePosition = activity.listView.getFirstVisiblePosition();//获取第一个可见的位置
//			bdInfo.x = imageView.getLeft();
//			bdInfo.y = dip2px(7) + (index - firstVisiblePosition)  * dip2px(70) + top + activity.listView.getTop();
//			bdInfo.width = imageView.getLayoutParams().width;
//			bdInfo.height = imageView.getLayoutParams().height;
//			Intent intent = new Intent(context, PreviewImage.class);
//			intent.putExtra("data", (Serializable) data);
//			intent.putExtra("bdinfo",bdInfo);
//			intent.putExtra("index",index);
//			intent.putExtra("type",1);
//			context.startActivity(intent);
//		}
//	}

}
