package com.example.administrator.zhihuiyinshui.activity.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.Utils.CacheForUserInformation;
import com.example.administrator.zhihuiyinshui.activity.bean.Result;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;
import com.example.administrator.zhihuiyinshui.activity.wxpay.Constants;
import com.example.administrator.zhihuiyinshui.activity.wxpay.MD5;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Fajieyefu on 2016/7/6.
 */
public class ChoosePayActivity extends Activity implements View.OnClickListener {
    private LinearLayout weixinPay;
    private LinearLayout zhifubaoPay;
    private String model_name, model_price, model_times;
    private Map<String, String> params;
    private String card_code;
    private Map<String, String> resultunifiedorder;
    private PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    Result payResult = new Result((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(ChoosePayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ChoosePayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ChoosePayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choosepay);
        CacheForUserInformation cacheForUserInfomation = new CacheForUserInformation();
        card_code = cacheForUserInfomation.getCard_code();
        msgApi.registerApp(Constants.WX_APP_ID);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        model_name = intent.getStringExtra("model_name");
        model_price = intent.getStringExtra("model_price");
        model_times = intent.getStringExtra("model_times");
        TextView model_name_view = (TextView) findViewById(R.id.model_name);
        TextView model_price_view = (TextView) findViewById(R.id.model_price);
        TextView model_times_view = (TextView) findViewById(R.id.model_times);
        model_name_view.setText(model_name);
        model_price_view.setText(model_price+"元");
        model_times_view.setText(model_times);
        weixinPay = (LinearLayout) findViewById(R.id.weixinPay);
        zhifubaoPay = (LinearLayout) findViewById(R.id.zhifubaoPay);
        weixinPay.setOnClickListener(this);
        zhifubaoPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        params = new HashMap<>();
        params.put("model_name", model_name);
        params.put("model_price", model_price);
        params.put("model_times", model_times);
        params.put("card_code", card_code);
        switch (v.getId()) {
            case R.id.weixinPay:
                WXAsyncTask task = new WXAsyncTask();
                task.execute();
                break;
            case R.id.zhifubaoPay:
                ZFBAsyncTask task1 = new ZFBAsyncTask();
                task1.execute();
                break;



        }
    }
    class ZFBAsyncTask extends AsyncTask<Void,Void,Boolean>{
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog=ProgressDialog.show(ChoosePayActivity.this,getString(R.string.app_tips),getString(R.string.getting_prepayid));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (dialog!=null){
                dialog.dismiss();
            }
        }

        @Override
        protected Boolean doInBackground(Void... p) {
           String info = WebService.getPayInfo(params);
            String sign = null,orderInfo = null;
            try {
                JSONObject jsonObject= new JSONObject(info);
                sign=jsonObject.getString("sign");
                orderInfo=jsonObject.getString("orderInfo");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                sign= URLEncoder.encode(sign,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(ChoosePayActivity.this);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(payInfo, true);

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            Thread payThread = new Thread(payRunnable);
            payThread.start();
            return true;

        }
    }
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
    class WXAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ChoosePayActivity.this, getString(R.string.app_tips), getString(R.string.getting_prepayid));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

        }

        @Override
        protected Boolean doInBackground(Void... p) {
            String info = WebService.getPrepayId(params);
            System.out.println(info);
            req = new PayReq();
            resultunifiedorder = decodeXml(info);
            genPayReq();
            sendPayReq();
            return  true;
        }
    }

    private void sendPayReq() {
        msgApi.registerApp(Constants.WX_APP_ID);
        msgApi.sendReq(req);
    }

    private void genPayReq() {

        req.appId = Constants.WX_APP_ID;
        req.partnerId = Constants.WX_MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "prepay_id=" + resultunifiedorder.get("prepay_id");
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        System.out.println("signParams:" + signParams.toString());

    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.WX_API_KEY);

        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        System.out.println("appSign:" + appSign);
        return appSign;
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if (!("xml".equals(nodeName)) ) {
                            // 实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            System.out.println("orion:" + e.toString());
        }
        return null;

    }


}
