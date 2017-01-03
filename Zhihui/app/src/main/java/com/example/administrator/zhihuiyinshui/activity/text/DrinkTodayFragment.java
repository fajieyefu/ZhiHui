package com.example.administrator.zhihuiyinshui.activity.text;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.DrinkInfo;
import com.example.administrator.zhihuiyinshui.activity.Utils.GetDrinkInfoUtil;
import com.example.administrator.zhihuiyinshui.activity.adapter.PictureListAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiancheng on 2016/9/21.
 */
public class DrinkTodayFragment extends Fragment {
	private final int TYPE_TODAY = 0;
	private int count = 1;
	private PictureListAdapter adapter;
	private List<DrinkInfo> list;
	public ListView listView;
	private ImageView emptyView;
	private ImageView imageView;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.today_drink_fragment, container, false);
		emptyView = (ImageView) view.findViewById(R.id.empty_view);
		listView = (ListView) view.findViewById(R.id.today_list);
		listView.setEmptyView(emptyView);
		list = new ArrayList<>();
		adapter = new PictureListAdapter(getActivity(), R.layout.picture_browse_item_pic_left, list);
		SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapter);
		animationAdapter.setAbsListView(listView);
		listView.setAdapter(animationAdapter);
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... voids) {
				GetDrinkInfoUtil util = new GetDrinkInfoUtil();
				List<DrinkInfo> temp = util.getDrinkInfo(TYPE_TODAY, count);
				if (temp == null) {
					return false;
				} else {
					list.addAll(temp);
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
					Toast.makeText(getActivity(), "噢噢，好像没有消息了", Toast.LENGTH_SHORT).show();
				}


			}
		}.execute();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

}
