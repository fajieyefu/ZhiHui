package com.example.administrator.zhihuiyinshui.activity.bean;

/**
 * Created by Fajieyefu on 2016/9/3.
 */
public class News {
    public String news_pic;
    public String title;
    public String content;
    public String time;

    public News(String title, String content, String time, String news_pic) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.news_pic = news_pic;
    }

    public String getContent() {
        return content;
    }

    public String getNews_pic() {
        return news_pic;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }
}

