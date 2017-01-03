package com.example.administrator.zhihuiyinshui.activity.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.zhihuiyinshui.R;
import com.example.administrator.zhihuiyinshui.activity.Utils.CacheForUserInformation;
import com.example.administrator.zhihuiyinshui.activity.adapter.GonggaoAdapter;
import com.example.administrator.zhihuiyinshui.activity.bean.News;
import com.example.administrator.zhihuiyinshui.activity.layout.TitleLayout;
import com.example.administrator.zhihuiyinshui.activity.bean.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fajieyefu on 2016/6/17.
 */
public class GongGaoActivity extends Activity {
    private String kindergarten_code;
    private ListView listView;
    private List<News> list=new ArrayList<>();
    private GonggaoAdapter adapter;
    private int count=1;
    private Button leadMore;
    private TitleLayout titleLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gonggao);
        titleLayout= (TitleLayout) findViewById(R.id.title_layout);
        titleLayout.setTitleText("信息公告");
        View bottom=getLayoutInflater().inflate(R.layout.leadmore,null);
        leadMore= (Button) bottom.findViewById(R.id.lead);
        CacheForUserInformation cache = new CacheForUserInformation();
        kindergarten_code = cache.getKindergarten_code();
        listView = (ListView) findViewById(R.id.listview);
        listView.addFooterView(bottom);
        adapter = new GonggaoAdapter(GongGaoActivity.this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news= list.get(position);
                Intent intent= new Intent(GongGaoActivity.this,GongGaoDetail.class);
                intent.putExtra("news_title",news.getTitle());
                intent.putExtra("news_content",news.getContent());
                intent.putExtra("news_time",news.getTime());
                startActivity(intent);
            }
        });
        new ExecuteNews().execute();
        leadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ExecuteNews().execute();
            }
        });
    }



    class ExecuteNews extends AsyncTask<Void, Integer, Boolean> {

        private  ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(GongGaoActivity.this);
            dialog.setMessage("正在加载...");
            dialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String info = WebService.getKindergartenNews(kindergarten_code,count);
            JSONObject jsonObject ;
            JSONArray jsonArray ;
            News news;
            try {
                jsonArray = new JSONArray(info);
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    String news_title = jsonObject.getString("news_title");
                    String news_content = jsonObject.getString("news_content");
                    String news_time = jsonObject.getString("news_time");
                    news = new News(news_title, news_content, news_time, null);
                    list.add(news);
                }


            } catch (JSONException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (dialog!=null){
                dialog.dismiss();
            }

            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (dialog!=null){
                dialog.dismiss();
            }
            if (aBoolean){
                adapter.notifyDataSetChanged();
                count++;
            }else{
                Toast toast=Toast.makeText(GongGaoActivity.this, "消息已经到最后了！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                leadMore.setText("没有更多信息");
                leadMore.setEnabled(false);
            }

        }
    }
}