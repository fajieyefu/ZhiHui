package com.example.administrator.zhihuiyinshui.activity.bean;

/**
 * Created by Fajieyefu on 2016/9/5.
 */
public class FeeModel {
    private String model_name;
    private String model_price;
    private String model_times;
    private Boolean model_radio=false;

    public FeeModel(String model_name, String model_price, String model_times) {
        this.model_name = model_name;
        this.model_price = model_price;
        this.model_times = model_times;
    }

    public String getModel_name() {
        return model_name;
    }

    public String getModel_price() {
        return model_price;
    }

    public String getModel_times() {
        return model_times;
    }

    public Boolean getModel_radio() {
        return model_radio;
    }

    public void setModel_radio(Boolean model_radio) {
        this.model_radio = model_radio;
    }
}
