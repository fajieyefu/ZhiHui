package com.example.administrator.zhihuiyinshui.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.News;

import java.util.List;

/**
 * Created by Fajieyefu on 2016/9/3.
 */
public class GonggaoAdapter extends BaseAdapter {
    private List<News> mData;
    private LayoutInflater mInflater;
    public GonggaoAdapter(Context context, List<News> data) {
        this.mData=data;
        mInflater=LayoutInflater.from(context);
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
        News news=mData.get(position);
        ViewHolder viewHolder=null;
        //判断是否缓存
        if(convertView==null){
            viewHolder=new ViewHolder();
            //通过LayoutInflater实例化布局
            convertView=mInflater.inflate(R.layout.gonggao_item,null);
            viewHolder.img= (ImageView) convertView.findViewById(R.id.gonggao_pic);
            viewHolder.title_txt= (TextView) convertView.findViewById(R.id.title);
            viewHolder.content_txt= (TextView) convertView.findViewById(R.id.content);
            viewHolder.time_txt= (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        }else{
            //通过tag找到缓存的布局
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //设置布局中控件要显示的视图
        viewHolder.img.setImageResource(R.mipmap.ic_launcher);
        viewHolder.title_txt.setText(news.getTitle());
        viewHolder.content_txt.setText(news.getContent());
        viewHolder.time_txt.setText(news.getTime());

        return convertView;
    }
    public final class ViewHolder{
        public ImageView img;
        public TextView title_txt;
        public TextView time_txt;
        public TextView content_txt;
    }
}
