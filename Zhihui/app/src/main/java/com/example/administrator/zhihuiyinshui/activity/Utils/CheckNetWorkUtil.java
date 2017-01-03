package com.example.administrator.zhihuiyinshui.activity.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Fajieyefu on 2016/9/19.
 */
public class CheckNetWorkUtil {
    public static boolean checkNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getActiveNetworkInfo() != null && connManager.getActiveNetworkInfo().isAvailable();
    }

}
