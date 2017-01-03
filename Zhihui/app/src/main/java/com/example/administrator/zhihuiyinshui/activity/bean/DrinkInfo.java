package com.example.administrator.zhihuiyinshui.activity.bean;

import java.io.Serializable;

/**
 * Created by Fajieyefu on 2016/6/24.
 */
public class DrinkInfo implements Serializable {
    private String drink_time;
    private String drink_day;
    private String water_temp;
    private String water_quality;
    private String drink_pic;

    public DrinkInfo() {
    }

    public DrinkInfo(String drink_time, String drink_day, String water_temp, String water_quality, String drink_pic) {
        this.drink_time = drink_time;
        this.drink_day=drink_day;
        this.water_temp = water_temp;
        this.water_quality = water_quality;
        this.drink_pic = drink_pic;
    }

    public void setDrink_day(String drink_day) {
        this.drink_day = drink_day;
    }

    public void setDrink_pic(String drink_pic) {
        this.drink_pic = drink_pic;
    }

    public void setDrink_time(String drink_time) {
        this.drink_time = drink_time;
    }

    public void setWater_quality(String water_quality) {
        this.water_quality = water_quality;
    }

    public void setWater_temp(String water_temp) {
        this.water_temp = water_temp;
    }

    public String getDrink_day() {

        return drink_day;
    }

    public String getDrink_pic() {
        return drink_pic;
    }

    public String getDrink_time() {
        return drink_time;
    }

    public String getWater_quality() {
        return water_quality;
    }

    public String getWater_temp() {
        return water_temp;
    }
}
