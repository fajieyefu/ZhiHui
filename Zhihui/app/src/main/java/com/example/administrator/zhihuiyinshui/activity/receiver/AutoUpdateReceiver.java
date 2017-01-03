package com.example.administrator.zhihuiyinshui.activity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.zhihuiyinshui.activity.service.UpdatePicService;

/**
 * Created by Fajieyefu on 2016/8/29.
 */
public class AutoUpdateReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, UpdatePicService.class);
        context.startService(i);
    }
}
