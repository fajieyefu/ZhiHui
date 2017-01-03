package com.example.administrator.zhihuiyinshui.activity.bean;

/**
 * Created by LK on 2016/5/30.
 */
public class Login {
    private String name ;
    private String pwd;

    public void  setName(String name ){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setPwd(String pwd){
        this.pwd = pwd;
    }
    public String getPwd(){
        return pwd;
    }
}
