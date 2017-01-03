package com.example.administrator.zhihuiyinshui.activity.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;
import com.example.administrator.zhihuiyinshui.activity.receiver.AutoUpdateReceiver;
import com.example.administrator.zhihuiyinshui.activity.text.DrinkViewPaper;

import org.json.JSONObject;

/**
 * Created by Fajieyefu on 2016/8/29.
 */
public class UpdatePicService extends Service {
    private String card_code;
    private int drink_count;
    private SharedPreferences pref;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
         pref = UpdatePicService.this.getSharedPreferences("loginInfo", MODE_PRIVATE);
        card_code = pref.getString("card_code", "");
        if (TextUtils.isEmpty(card_code)) {
            Toast.makeText(UpdatePicService.this, "请绑定小朋友的水杯卡号", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updatePic();
            }
        }).start();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 2* 60 * 1000;//这是2分钟的时间
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新照片信息
     */
    private void updatePic() {
        drink_count=pref.getInt("drink_count",0);
        JSONObject jsonObject ;
                String info = WebService.executeHttpGetDrinkInfo(card_code, 0, 0);
                try {
                    jsonObject = new JSONObject(info);
                    int len = jsonObject.optInt("num_1");
                    int num_2=jsonObject.optInt("num_2");
                    Log.i("len:",len+"");
                    Log.i("drink_count:",drink_count+"");
                    if (len != drink_count) {
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        Intent intent = new Intent(UpdatePicService.this, DrinkViewPaper.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(UpdatePicService.this, 1, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                        builder.setTicker("快去看看，你家宝贝喝水啦");
                        builder.setContentTitle("智慧饮水");
                        builder.setContentText("宝贝喝水啦，真棒！0.0");
                        builder.setSmallIcon(R.drawable.logo_24);
                        builder.setContentIntent(contentIntent);
                        Notification notification=builder.build();
                        notification.flags=Notification.FLAG_AUTO_CANCEL;
                        notification.ledARGB= Color.BLUE;
                        notification.ledOnMS=1000;
                        notification.ledOffMS=1000;
                        notification.flags|=Notification.FLAG_SHOW_LIGHTS;
                        notification.defaults = Notification.DEFAULT_SOUND;
                        long[] vibrates = {0, 500, 500, 500};
                        notification.vibrate = vibrates;
                        notificationManager.notify(1, notification);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putInt("drink_count",len);
                        editor.apply();
                    }
                } catch (Exception e) {
                }


    }
}
