package com.example.administrator.zhihuiyinshui.activity.bean;

/**
 * Created by Fajieyefu on 2016/7/4.
 */
public class Student {
    private String name;
    private int drinkCount;

    public Student(int drinkCount, String name) {
        this.drinkCount = drinkCount;
        this.name = name;
    }

    public int getDrinkCount() {
        return drinkCount;
    }

    public String getName() {
        return name;
    }
}
