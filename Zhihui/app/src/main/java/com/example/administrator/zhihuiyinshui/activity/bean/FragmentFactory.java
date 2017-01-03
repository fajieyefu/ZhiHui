package com.example.administrator.zhihuiyinshui.activity.bean;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.zhihuiyinshui.R;
import com.squareup.picasso.Picasso;

/**
 * Created by qiancheng on 2016/9/24.
 */
public class FragmentFactory {

	public static Fragment createFragment(String url) {
		Fragment fragment = new MyFragment(url);

		return fragment;
	}

}

class MyFragment extends Fragment {
	private  String url;
	MyFragment(String url){
		this.url=url;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.kindergatern_fragment_view, container, false);
		ImageView imageView = (ImageView) view.findViewById(R.id.kg_pic);
		Picasso.with(getActivity()).load(url).into(imageView);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}