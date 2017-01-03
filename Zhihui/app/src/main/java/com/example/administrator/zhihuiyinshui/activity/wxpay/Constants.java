package com.example.administrator.zhihuiyinshui.activity.wxpay;

public class Constants {
	//服务器地址
	public static final String  GANHOST = "http://123.132.252.2:9891";
	//微信支付配置文件
    public static final String WX_APP_ID = "wx534d85833b579f33";//APP_ID
	public static final String WX_API_KEY = "EczaKO5yLMnmLgUchOtP9tDY3hYfCKz9";// 支付密钥 在商品平台设置，是商品平台和开放平台的约定的API密钥
	public static final String WX_MCH_ID = "1379705102";//商户ID
	//支付宝配置文件
	public static final String PARTNER = "";//商户PID
	public static final String SELLER = "";// 商户收款账号
	public static final String RSA_PRIVATE = "";// 商户私钥，pkcs8格式
	public static final String aliPay_notifyURL = GANHOST+"/service/alipay/orderComplete";//支付宝--支付成功服务器的回调

}
