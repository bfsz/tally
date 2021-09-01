package com.luna.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.luna.tally.adapter.AccountAdapter;
import com.luna.tally.db.AccountBean;
import com.luna.tally.db.DBManager;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayLv; // 展示今日收支情况
    ImageView searchIv;
    Button editBtn;
    ImageButton moreBtn;
    // 数据源
    List<AccountBean> mDatas;
    AccountAdapter adapter;
    int year, month, day;
    // 头布局
    View headerView;
    TextView topOutTv, topInTv, topbudgetTv, topConTv;
    ImageView topShowIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        //添加ListView头布局
        addLVHeaderView();
        mDatas = new ArrayList<>();
        //设置适配器
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }

    /*初始化自带View方法*/
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        searchIv = findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
    }

    /*添加ListView头布局方法*/
    private void addLVHeaderView() {
        //将布局转换成View对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        //查找头布局可用控件
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);
    }

    /*获取今日时间*/
    public void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    // 当 activity 获取焦点时，会调用方法
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }

    /*设置头布局文本内容显示*/
    private void setTopTvShow() {
        //获取今日支出和收入总金额，显示在view当中
        double incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        double outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "今日支出 ￥" + outcomeOneDay + "  收入 ￥" + incomeOneDay;
        topConTv.setText(infoOneDay);
        //获取本月收入和支出总金额
        double incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        double outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText("￥" + incomeOneMonth);
        topOutTv.setText("￥" + outcomeOneMonth);
    }

    // 加载数据
    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_iv_search:
                break;
            case R.id.main_btn_edit:
                Intent intent = new Intent(this, RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_more:
                break;
            case R.id.item_mainlv_top_tv_budget:
                break;
            case R.id.item_mainlv_top_iv_hide:
                break;
        }
        if (view == headerView) {
            // 头布局点击
        }
    }
}