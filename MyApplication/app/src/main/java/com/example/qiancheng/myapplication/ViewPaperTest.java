package com.example.qiancheng.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiancheng on 2016/9/21.
 */
public class ViewPaperTest extends FragmentActivity {
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentAdapter mFragmentAdapter ;
    private ViewPager mPageVp;
    /*
    tab显示内容的TextView
     */
    private TextView mTabChatTv, mTabContactsTv, mTabFriendTv;
    /*
    tab下面的那根引导线
     */
    private ImageView mTabLineIv;
    /*
    Fragment
     */
    private ChatFragment mChatFg;
    private ContractsFragment mContactsFg;
    private FriendFragment mFriendFg;
    /*
    viewpaper当前选中页
     */
    private  int currentIndex;
    /*
    屏幕的宽度
     */
    private  int screenWidth;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.test_viewpaper);
        findById();
        init();
        initTabLineWidth();
    }
    /**
     * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / 3;
        mTabLineIv.setLayoutParams(lp);
    }

    private void findById() {

        mTabContactsTv = (TextView) this.findViewById(R.id.contract_context);
        mTabChatTv = (TextView) this.findViewById(R.id.chat_context);
        mTabFriendTv = (TextView) this.findViewById(R.id.friend_context);
        mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);
        mPageVp = (ViewPager) this.findViewById(R.id.id_paper_vp);
    }
    private void init() {
        mFriendFg = new FriendFragment();
        mContactsFg = new ContractsFragment();
        mChatFg = new ChatFragment();
        mFragmentList.add(mChatFg);
        mFragmentList.add(mFriendFg);
        mFragmentList.add(mContactsFg);
        mFragmentAdapter = new FragmentAdapter(
                this.getSupportFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setCurrentItem(0);
        mPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                        .getLayoutParams();
                Log.e("offset:", offset + "");
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记3个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */

                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                }
                mTabLineIv.setLayoutParams(lp);
            }


            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        mTabChatTv.setTextColor(Color.BLUE);
                        break;
                    case 1:
                        mTabFriendTv.setTextColor(Color.BLUE);
                        break;
                    case 2:
                        mTabContactsTv.setTextColor(Color.BLUE);
                        break;
                }
                currentIndex = position;
            }
            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void resetTextView() {
        /**
         * 重置颜色
         */
            mTabChatTv.setTextColor(Color.BLACK);
            mTabFriendTv.setTextColor(Color.BLACK);
            mTabContactsTv.setTextColor(Color.BLACK);
    }


}
