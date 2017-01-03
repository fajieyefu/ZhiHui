package com.example.administrator.zhihuiyinshui.activity.bean;

import java.util.Comparator;

/**
 * Created by Fajieyefu on 2016/7/4.
 */
public class ComparatorImpl implements Comparator<Student> {
    @Override
    public int compare(Student lhs, Student rhs) {
        int drinkCount_2= lhs.getDrinkCount();
        int drinkCount_1= rhs.getDrinkCount();
        if (drinkCount_1>drinkCount_2){
            return 1;
        }else if (drinkCount_1<drinkCount_2){
            return -1;
        }
        return 0;
    }
}
