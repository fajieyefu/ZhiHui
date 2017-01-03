package com.example.administrator.zhihuiyinshui.activity.text;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.DrinkInfo;
import com.example.administrator.zhihuiyinshui.activity.Utils.GetDrinkInfoUtil;
import com.example.administrator.zhihuiyinshui.activity.adapter.PictureListAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiancheng on 2016/9/21.
 */
public class DrinkHistoryFragment extends Fragment {
	private final int TYPE_HISTORY = 1;
	private int count = 1;
	private List<DrinkInfo> list;
	private PictureListAdapter adapter;
	private ListView listView;
	private GetDrinkInfoUtil util;
	private Button leadMore;
	private ImageView emptyView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.history_pic_fragment, container, false);
		emptyView = (ImageView) view.findViewById(R.id.empty_view);
		listView = (ListView) view.findViewById(R.id.history_list);
		listView.setEmptyView(emptyView);
		list = new ArrayList<>();
		adapter = new PictureListAdapter(getActivity(), R.layout.picture_browse_item_pic_right, list);
		View bottom_View = inflater.inflate(R.layout.leadmore, null);
		leadMore = (Button) bottom_View.findViewById(R.id.lead);
		listView.addFooterView(bottom_View);
		SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapter);
		animationAdapter.setAbsListView(listView);
		listView.setAdapter(animationAdapter);
		leadMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				new LoadDrinkInfoTask().execute();
			}
		});
		new LoadDrinkInfoTask().execute();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	class LoadDrinkInfoTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... voids) {
			util = new GetDrinkInfoUtil();
			List<DrinkInfo> temp = util.getDrinkInfo(TYPE_HISTORY, count);
			if (temp == null) {
				return false;
			} else {
				list.addAll(temp);
				count++;
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
				leadMore.setText("没有更多信息");
				leadMore.setEnabled(false);

			}


		}
	}
}
