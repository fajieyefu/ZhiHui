package com.example.administrator.zhihuiyinshui.activity.bean;

import java.io.Serializable;

/**
 * Created by qiancheng on 2016/11/5.
 */
public class ProductInfo implements Serializable {
	private String id;//序号
	private String pro_name;//商品名称
	private String pro_code;//商品编码
	private String pro_spec;//商品规格
	private String pro_spec_code;//商品规格编号
	private String price;//商品单价
	private String pro_points;//商品积分
	private String pro_stock;//商品库存
	private String pro_img;
	public ProductInfo(String id, String pro_spec_code, String pro_spec, String pro_code, String pro_name, String price, String pro_points, String pro_stock,String pro_img) {
		this.id = id;
		this.pro_spec_code = pro_spec_code;
		this.pro_spec = pro_spec;
		this.pro_code = pro_code;
		this.pro_name = pro_name;
		this.price = price;
		this.pro_points = pro_points;
		this.pro_stock = pro_stock;
		this.pro_img= pro_img;
	}

	public ProductInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPro_code() {
		return pro_code;
	}

	public void setPro_code(String pro_code) {
		this.pro_code = pro_code;
	}

	public String getPro_name() {
		return pro_name;
	}

	public void setPro_name(String pro_name) {
		this.pro_name = pro_name;
	}

	public String getPro_points() {
		return pro_points;
	}

	public void setPro_points(String pro_points) {
		this.pro_points = pro_points;
	}

	public String getPro_spec() {
		return pro_spec;
	}

	public void setPro_spec(String pro_spec) {
		this.pro_spec = pro_spec;
	}

	public String getPro_spec_code() {
		return pro_spec_code;
	}

	public void setPro_spec_code(String pro_spec_code) {
		this.pro_spec_code = pro_spec_code;
	}

	public String getPro_stock() {
		return pro_stock;
	}

	public void setPro_stock(String pro_stock) {

		this.pro_stock = pro_stock;
	}

	public String getPro_img() {
		return pro_img;
	}

	public void setPro_img(String pro_img) {
		this.pro_img = pro_img;
	}
}
