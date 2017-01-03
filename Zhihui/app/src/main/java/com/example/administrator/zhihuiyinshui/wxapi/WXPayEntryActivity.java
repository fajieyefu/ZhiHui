package com.example.administrator.zhihuiyinshui.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.Utils.CacheForUserInformation;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;
import com.example.administrator.zhihuiyinshui.activity.wxpay.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Fajieyefu on 2016/7/6.
 */

//支付状态返回信息
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private String card_code;

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.第一个修改的地方 我删掉了,它自带的布局, 当然如果你想保留,完全OK.因为布局太难看所以我干掉他了
        CacheForUserInformation cache= new CacheForUserInformation();
        card_code= cache.getCard_code();
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {}

    @Override
    public void onResp(BaseResp resp) {
        //2.这是我修改的第二个地方, 原版的是弹出一个对话框, 我觉得太丑,并且原版也没有给出详细问题提示,只给出了 0, -1 ,-2 这样会让客户不明所以,所以我替换成如下3个文本. 此外   //原版支付后,不会直接跳转到 你的应用,需要按一次 返回键,才行, 所以我加入了finish()便于在提示后,直接返回我的应用
        if(resp.errCode==0){
            String info= WebService.confrimPayResult(card_code);
            if(info.equals("success")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.app_tips);
                builder.setMessage(getString(R.string.pay_result_callback_msg)+ resp.errStr +";code=" + String.valueOf(resp.errCode));
                builder.setCancelable(false);
                builder.setPositiveButton("返回智慧成长", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.setNegativeButton("留在微信", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
            Toast.makeText(this,"支付成功!",Toast.LENGTH_SHORT).show();

        }else if(resp.errCode==-1){
            Toast.makeText(this,"支付失败!",Toast.LENGTH_SHORT).show();
            finish();

        }else if(resp.errCode==-2){
            Toast.makeText(this,"取消支付!",Toast.LENGTH_SHORT).show();
            finish();

        }
    }
}