package com.example.administrator.zhihuiyinshui.activity.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.administrator.zhihuiyinshui.activity.bean.DrinkInfo;
import com.example.administrator.zhihuiyinshui.activity.bean.MyApplication;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiancheng on 2016/9/21.
 */
public class GetDrinkInfoUtil {
	private String card_code;
	private String drink_time;
	private String drink_day;
	private String water_temp;
	private String water_quality;
	private String drink_pic;
	private DrinkInfo drinkInfo;
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	private List<DrinkInfo> list = new ArrayList<>();
	private SharedPreferences pref;

	public GetDrinkInfoUtil() {
		card_code=new CacheForUserInformation().getCard_code();
		pref= MyApplication.getContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
	}

	public List<DrinkInfo> getDrinkInfo(int type, int time) {
		String info = WebService.executeHttpGetDrinkInfo(card_code, type, time);
		try {
			jsonObject=new JSONObject(info);
			int num_1=jsonObject.optInt("num_1");
			int num_2=jsonObject.optInt("num_2");
			JSONArray jsonArray= jsonObject.getJSONArray("data");
			if (type==0){
				int len = num_1;
				SharedPreferences.Editor editor=pref.edit();
				editor.putInt("drink_count",len);
				editor.apply();//定义变量记住本次喝水数，对应下次是否有新的记录
			}
//			StaticVariable.lastUpdateCount = len;
			for (int i = 0; i < num_1 ; i++) {
				jsonObject=jsonArray.getJSONObject(i);
				drink_time = jsonObject.getString("drink_time");
				drink_pic = jsonObject.getString("drink_pic");
				String[] temp = drink_time.split(" ");
				drink_day = temp[0];
				drink_time = temp[1];
				drinkInfo = new DrinkInfo(drink_time, drink_day, water_temp, water_quality, drink_pic);
				list.add(drinkInfo);
			}
		} catch (JSONException e) {
			return  null;
		}
		return list;
	}


}
