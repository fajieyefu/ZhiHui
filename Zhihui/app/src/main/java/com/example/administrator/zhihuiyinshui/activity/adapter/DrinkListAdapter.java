package com.example.administrator.zhihuiyinshui.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.Student;

import java.util.List;

/**
 * Created by Fajieyefu on 2016/7/4.
 */
public class DrinkListAdapter extends ArrayAdapter {
    private int resourceId;
    public DrinkListAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        resourceId= resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Student student= (Student) getItem(position);
        View view;
        DrinkListViewHolder drinkListViewHolder;
        if (convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            drinkListViewHolder= new DrinkListViewHolder();
            drinkListViewHolder.orderText= (TextView) view.findViewById(R.id.order_num);
            drinkListViewHolder.studentPic= (ImageView) view.findViewById(R.id.student_pic);
            drinkListViewHolder.medalPic= (ImageView) view.findViewById(R.id.medal);
            drinkListViewHolder.drinkCount=(TextView)view.findViewById(R.id.drink_count);
            drinkListViewHolder.studentName=(TextView)view.findViewById(R.id.student_name);

            view.setTag(drinkListViewHolder);

        }else{
            view=convertView;
            drinkListViewHolder= (DrinkListViewHolder) view.getTag();
        }
        drinkListViewHolder.orderText.setText(position+1+"");
        drinkListViewHolder.drinkCount.setText(student.getDrinkCount()+"");
        drinkListViewHolder.studentName.setText(student.getName());
        int pic = 0;
        if((position>=0)&&(position<=2)) {
            switch (position) {
                case 0:
                    pic = R.drawable.medal_1;
                    break;
                case 1:
                    pic = R.drawable.medal_2;
                    break;
                case 2:
                    pic = R.drawable.medal_3;
                    break;
            }
        }else{
            pic=R.drawable.flower;
        }
        drinkListViewHolder.medalPic.setImageResource(pic);


        return view;


    }
    class  DrinkListViewHolder{
        TextView drinkCount;
        TextView studentName;
        TextView orderText;
        ImageView studentPic;
        ImageView medalPic;


    }
}
