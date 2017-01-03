package com.example.administrator.zhihuiyinshui.activity.bean;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fajieyefu on 2016/6/18.
 */
public class WebService {
  private static String IP = "121.199.22.246:9891";//阿里云服务器地址
//    private static String IP = "123.132.252.2:9891";//测试服务器的地址
    private static String constant_path = "http://" + IP + "/ZhiHuiWeb/";
    private static String WXPay_path="http://"+IP+"/WXPayWeb/";
    private static String ALPay_path="http://"+IP+"/ALiPayWeb/";
    private static String drink_path="http://"+IP+"/DrinkPicWeb/";

    //通过Get方式获取Http服务器数据
    public static String executeHttpGet(String username, String password) {
        String path = constant_path + "LogLet";//URl路径
        path = path + "?username=" + username + "&password=" + password;
        String info = getHttp(path);
        return info;
    }

    /*
    获取小朋友今日的喝水详情,type=0,代表今日喝水照片，type=1代表获取本月喝水照片
     */
    public static String executeHttpGetDrinkInfo(String cardId, int type, int count) {
        String path = drink_path + "WaterInfo";//URl路径
        path = path + "?cardId=" + cardId + "&type=" + type + "&count=" + count;
        String info = getHttp(path);
        return info;
    }

    /*
    获取小朋友所在班级所有小朋友的喝水量,type=0,表示获取当天喝水排名，type=1获取当月排名
     */
    public static String executeHttpGetDrinkListInfo(String cardId, int type) {
        String path = constant_path + "DrinkListInfo";//URl路径
        path = path + "?cardId=" + cardId + "&type=" + type;
        String info = getHttp(path);
        return info;
    }

    /*
    获取小朋友所在园区的信息
     */
    public static String executeHttpGetKindergartenInfo(String cardId) {
        String path = constant_path + "KindergartenInfo";//URl路径
        path = path + "?cardId=" + cardId;
        String info = getHttp(path);
        return info;
    }

    /**
     * 注册时获取输入的手机号，传给服务端，由服务端调用短信接口，将信息传到手机上，
     * 并需要在服务端端上有限时间内存储该验证码，以便于点击注册按钮时判断验证码是否有误。
     */
    public static String executeHttpSendPhoneNum(String phoneString) {
        String path = constant_path + "SendMessage";//URl路径
        path = path + "?phoneString=" + phoneString;
        String info = getHttp(path);
        return info;
    }

    public static String executeHttpBindCup(String cupId, String account, String studentName) {
        String path = constant_path + "BindCupServlet";//URl路径
        path = path + "?cupId=" + cupId + "&account=" + account + "&studentName=" + studentName;
        String info = getHttp(path);
        return info;
    }

    public static String executeHttpProduct(String card_code, int month) {
        String path = constant_path + "PayInfoServlet";//URl路径
        path = path + "?card_code=" + card_code + "&month=" + month;
        String info = getHttp(path);
        return info;

    }

    public static String makeAccount(String phone, String password, String useName) {
        Map<String, String> params = new HashMap<>();
        String path = constant_path+"RegLet";//URl路径
        params.put("phone", phone);
        params.put("password", password);
        params.put("useName", useName);
        String info = postHttp(params, path);
        return info;
    }

    public static String save(String account, String nickname_string, String sex_string, String birthday_string, String address_string, String introduce_string) {
        String path = constant_path+"SaveDataServlet";//URl路径
        Map<String, String> params = new HashMap<>();
        params.put("account", account);
        params.put("nickname", nickname_string);
        params.put("sex", sex_string);
        params.put("birthday", birthday_string);
        params.put("address", address_string);
        params.put("introduce", introduce_string);
        String info = postHttp(params, path);
        return info;

    }

    public static String executeHttpGetUserInfomation(String account) {
        String path = constant_path + "UserInfoServlet";//URl路径
        path = path + "?account=" + account;
        String info = getHttp(path);
        return info;
    }

    /**
     * 获取幼儿园发布的消息公告
     *
     * @param kindergarten_code
     * @return
     */
    public static String getKindergartenNews(String kindergarten_code,int count) {
        String path = constant_path + "KGNewsServlet";
        path = path + "?kindergarten_code=" + kindergarten_code+"&count="+count;
        String info = getHttp(path);
        return info;
    }

    /**
     *
     * @return
     */
    public static String getFeeModel() {
        String path=constant_path+"FeeModelServlet";
        String info =getHttp(path);
        return  info;
    }

    /**
     * 获取微信支付的prepayId
     * @param params
     * @return
     */

    public static String getPrepayId(Map<String, String> params) {
        String path =WXPay_path+"PayServlet";
        String info = postHttp(params, path);
        return info;
    }

    /**
     * 向服务器确认是否付款成功
     * @param card_code
     * @return
     */
    public static String confrimPayResult(String card_code) {
        String path=WXPay_path+"ConfrimServlet";
        String info=getHttp(path);
        return  info;

    }
    /**
     * 获取支付宝支付的信息
     * @param params
     * @return
     */
    public static String getPayInfo(Map<String, String> params) {
        String path=ALPay_path+"ALiPayServlet";
        String info=postHttp(params,path);
        return info;
    }

	/**
     * 获取APK信息
     * @return
     */
    public static String updateApk() {
        String path =constant_path+"ApkInfoServlet";
        String info=getHttp(path);
        return info;

    }

	/**
     * 获取商品里列表
     * @return
     */
    public static String getProductList() {
        String path =constant_path+"ProductList";
        String info =getHttp(path);
        return info ;
    }

	/**
     * 增加收货地址
     * @param jsonString
     * @return
     */
    public static String addReceiptAddress(String  jsonString) {
        String path =constant_path+"ReceiptAddressServlet";
        String info =postHttpByJson(jsonString,path);
        return info ;




    }
    //获得Get方式请求数据
    private static String getHttp(String path) {
        HttpURLConnection conn = null;
        InputStream in = null;
        try {
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(6000);//设置连接超时时间
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");//设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8");//设置接收数据编码格式

            if (conn.getResponseCode() == 200) {
                in = conn.getInputStream();
                return parseInfo(in);
            }
        } catch (Exception e) {
            return "-1";
        } finally {
            //意外退出时关闭连接
            if (conn != null) {
                conn.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    //获得post方式请求数据
    private static String postHttp(Map<String, String> params, String path) {

        byte[] data = getRequestData(params, "UTF-8").toString().getBytes();//获得请求体
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(6000);//设置连接超时时间
            conn.setReadTimeout(6000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");//设置获取信息方式
            conn.setUseCaches(false);//使用post方式不能使用缓存
            //设置请求体的类型是文本类型
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            conn.setRequestProperty("Charset", "UTF-8");//设置接收数据编码格式
            //获得输出流，向服务器写入数据
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(data);
            int response = conn.getResponseCode();            //获得服务器的响应码
            if (response == HttpURLConnection.HTTP_OK) {        //HTTP_OK=200
                InputStream inputStream = conn.getInputStream();
                return parseInfo(inputStream);                  //处理服务器的响应结果
            }
        } catch (Exception e) {
            return "-1";
        }
        return "-1";


    }

    //封装post请求
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    //将输入流转化为String类型
    private static String parseInfo(InputStream in) throws IOException {
        byte[] data = read(in);

        return new String(data, "UTF-8");

    }

    //将输入流转化为byte数组
    private static byte[] read(InputStream in) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int len = 0;

        while ((len = in.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        in.close();
        return outputStream.toByteArray();

    }
    //获得post方式请求数据(以json格式提交数据)
    private static String postHttpByJson(String jsonString,String path){
        Log.v("zd", "send json");
        try {
            // 转成指定格式
            byte[] requestData = jsonString.getBytes("UTF-8");
            HttpURLConnection conn = null;
            DataOutputStream outStream = null;
            String MULTIPART_FORM_DATA = "multipart/form-data";

            // 构造一个post请求的http头
            URL url = new URL(path); // 服务器地址
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // 允许输入
            conn.setDoOutput(true); // 允许输出
            conn.setUseCaches(false); // 不使用caches
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA);
            conn.setRequestProperty("Content-Length", Long.toString(requestData.length));
            conn.setRequestProperty("data", jsonString);
            // 请求参数内容, 获取输出到网络的连接流对象
            outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(requestData, 0, requestData.length);
            outStream.flush();
            outStream.close();
            ByteArrayOutputStream outStream2 = new ByteArrayOutputStream();

            int cah = conn.getResponseCode();
            if (cah != 200) {
                Log.v("data", "服务器响应错误代码:" + cah);
                return "-1";
            } else if (cah == 200) {
                Log.v("data", "服务器响应成功:" + cah);
            }
            InputStream inputStream = conn.getInputStream();
            int len = 0;
            byte[] data = new byte[1024];

            while ((len = inputStream.read(data)) != -1) {
                outStream2.write(data, 0, len);
            }
            outStream2.close();
            inputStream.close();
            String responseStr = new String(outStream2.toByteArray());
            Log.v("data", "data = " + responseStr);

            return responseStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }


}
