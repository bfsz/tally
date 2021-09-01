package com.luna.tally;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.luna.tally.adapter.AccountAdapter;
import com.luna.tally.db.AccountBean;
import com.luna.tally.db.DBManager;
import com.luna.tally.utils.BudgeDialog;

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
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
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
        setLVLongClickListener();
    }

    /*设置ListView长按事件*/
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) { // 点击头布局
                    return false;
                }
                int pos = i - 1;
                AccountBean clickBean = mDatas.get(pos); // 获取正在被点击信息
                int click_id = clickBean.getId();
                // 弹出对话框
                showDeleteTiemDialog(clickBean);
                return false;
            }
        });
    }

    /*
     * 弹出是否删除一条记录对话框
     * */
    private void showDeleteTiemDialog(AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("您确定删除这条记录吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 执行删除操作
                        DBManager.deleteItemFromAccounttbById(clickBean.getId()); // 数据库删除
                        mDatas.remove(clickBean); // 删除对象 实时刷新
                        adapter.notifyDataSetChanged(); // 提示适配器更新数据
                        setTopTvShow(); // 改变头部局显示的内容
                    }
                });
        builder.create().show(); //显示对话框
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

        //设置预算剩余
        float bmoney = preferences.getFloat("bmoney", 0);
        if (bmoney == 0) {
            topbudgetTv.setText("￥ 0");
        } else {
            float syMoney = bmoney - (float) outcomeOneMonth;
            topbudgetTv.setText("￥ " + syMoney);
        }
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
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_iv_hide:
                // 切换 TestView明文密文
                toggleShow();
                break;
        }
        if (view == headerView) {
            // 头布局点击
        }
    }

    /*
     * 显示设置预算对话框
     * */
    private void showBudgetDialog() {
        BudgeDialog dialog = new BudgeDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(double money) {
                // 将运算金额写入到各项参数存储
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney", (float) money);
                editor.commit();

                // 计算剩余金额
                double outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float syMoney = (float) money - (float) outcomeOneMonth;
                topbudgetTv.setText("￥" + syMoney);
            }
        });
    }


    boolean isShow = true;

    /*
     * 点击头布局眼睛,明文 密文 切换
     * */
    private void toggleShow() {
        if (isShow) {//明——密
            PasswordTransformationMethod method = PasswordTransformationMethod.getInstance();
            topOutTv.setTransformationMethod(method);// 设置隐藏
            topInTv.setTransformationMethod(method);// 设置隐藏
            topbudgetTv.setTransformationMethod(method);// 设置隐藏
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow = false;
        } else {// 密--明
            HideReturnsTransformationMethod method = HideReturnsTransformationMethod.getInstance();
            topOutTv.setTransformationMethod(method);// 设置隐藏
            topInTv.setTransformationMethod(method);// 设置隐藏
            topbudgetTv.setTransformationMethod(method);// 设置隐藏
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow = true;
        }
    }
}