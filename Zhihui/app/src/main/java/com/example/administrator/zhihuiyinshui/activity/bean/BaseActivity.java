package com.example.administrator.zhihuiyinshui.activity.bean;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by Fajieyefu on 2016/8/31.
 */
public class BaseActivity extends Activity {
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            ActivityCollector.finishAll();
            System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(BaseActivity.this, "再点击一次退出程序", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    public boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getActiveNetworkInfo() != null && connManager.getActiveNetworkInfo().isAvailable();
    }

}
