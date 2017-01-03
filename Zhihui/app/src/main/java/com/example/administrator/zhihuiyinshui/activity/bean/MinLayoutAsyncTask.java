package com.example.administrator.zhihuiyinshui.activity.bean;

import android.os.AsyncTask;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by qiancheng on 2016/11/12.
 */
public class MinLayoutAsyncTask extends AsyncTask<Integer,Integer,Integer> {
	private static final String UP="up";
	private static final String DOWN = "down";
	/**要显示的底部的rightLayout*/
	View underLayout;
	/**要控制移动的layout*/
	FrameLayout midLayout;
	/**layout的参数对象*/
	FrameLayout.LayoutParams midLayoutParams;
	/**最终距离*/
	int max;
	/**移动方向*/
	String direction;

	/**
	 * 构造器
	 * @param underLayout
	 * @param midLayout 要控制的布局
	 * @param max 最终要到达的
	 * @param direction 移动方向
	 */
	public MinLayoutAsyncTask(View underLayout, View midLayout, int max, String direction) {
		this.underLayout = underLayout;
		this.midLayout = (FrameLayout) midLayout;
		this.max = max;
		this.direction = direction;
	}
	public MinLayoutAsyncTask( View midLayout, int max, String direction){
		this(null,midLayout,max,direction);
	}

	/**
	 * 任务开始前的准备工作
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		midLayoutParams= (FrameLayout.LayoutParams) midLayout.getLayoutParams();

	}

	/**
	 * 该类实例.execute(int i)传进来的参数 每次移动的距离
	 * @param speed
	 * @return
	 */
	@Override
	protected Integer doInBackground(Integer... speed) {
		/**每次进来都先获取layout的现时的topMargin*/
		int topMargin = midLayoutParams.topMargin;
		//根据传进来的速度来滚动界面，当滚动到达最大距离时，跳出循环
		while(true){
			//当前topMargin要移动的距离
			topMargin=topMargin+speed[0];
			//移动方向
			if(DOWN.equals(direction)){
				if (topMargin>max){
					topMargin=max;
					break;
				}
			}
			if (UP.equals(direction)){
				if (topMargin<max){
					topMargin=max;
					break;
				}


			}
			sleep(10);
			publishProgress(topMargin);

		}
		return topMargin;
	}

	@Override
	protected void onProgressUpdate(Integer...topMargin ) {
		midLayoutParams.topMargin=topMargin[0];
		midLayout.setLayoutParams(midLayoutParams);


	}
	/**
	 * 完成任务后在UI线程执行的方法 参数是doInBackground返回的结果
	 *
	 */
	@Override
	protected void onPostExecute(Integer topMargin) {
		if (topMargin==0){
			underLayout.setVisibility(View.GONE);
		}
		midLayoutParams.topMargin=topMargin;
		midLayout.setLayoutParams(midLayoutParams);

	}
	private void sleep(int min){
		try {
			Thread.sleep(min);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
