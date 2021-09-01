package com.luna.tally.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/*
 * 数据库管理类 增删改查
 * */
public class DBManager {
    private static SQLiteDatabase db;

    /*初始化数据库*/
    public static void initDB(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context); // 帮助类对象
        db = helper.getWritableDatabase(); //数据库对象
    }

    /*
     * 读取数据库中的数据 写入内存集合
     * */
    public static List<TypeBean> getTypeList(int kind) {
        List<TypeBean> list = new ArrayList<>();
        // 读取数据
        String sql = "select * from typetb where kind = " + kind;
        Cursor cursor = db.rawQuery(sql, null);
        // 循环读取，存入对象中
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String typeName = cursor.getString(cursor.getColumnIndex("typeName"));
            @SuppressLint("Range") int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            @SuppressLint("Range") int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            @SuppressLint("Range") int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean = new TypeBean(id, typeName, imageId, sImageId, kind);
            list.add(typeBean);
        }
        cursor.close();
        return list;
    }


    /*
     * 记账表插入数据
     * */
    public static void insertItemToAccounttb(AccountBean accountBean) {
        ContentValues values = new ContentValues();
        values.put("typeName", accountBean.getTypeName());
        values.put("sImageId", accountBean.getsImageId());
        values.put("beizhu", accountBean.getBeizhu());
        values.put("money", accountBean.getMoney());
        values.put("time", accountBean.getTime());
        values.put("year", accountBean.getYear());
        values.put("month", accountBean.getMonth());
        values.put("day", accountBean.getDay());
        values.put("kind", accountBean.getKind());
        db.insert("accounttb", null, values);
    }

    /*
     * 获取记录表某一天所有支出、收入情况
     * */
    public static List<AccountBean> getAccountListOneDayFromAccounttb(int year, int month, int day) {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String typeName = cursor.getString(cursor.getColumnIndex("typeName"));
            @SuppressLint("Range") String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
            @SuppressLint("Range") int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            @SuppressLint("Range") int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            @SuppressLint("Range") double money = cursor.getDouble(cursor.getColumnIndex("money"));
            AccountBean accountBean = new AccountBean(id, typeName, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        cursor.close();
        return list;
    }

    /*
     * 获取某一天的支出 收入总金额 kind 支出-0 收入-1
     * */
    public static double getSumMoneyOneDay(int year, int month, int day, int kind) {
        double total = 0;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        while (cursor.moveToNext()) {
            @SuppressLint("Range") double money = cursor.getDouble(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    /*
     * 获取某一月的支出 收入总金额 kind 支出-0 收入-1
     * */
    public static double getSumMoneyOneMonth(int year, int month, int kind) {
        double total = 0;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            @SuppressLint("Range") double money = cursor.getDouble(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    /*
     * 获取某一年的支出 收入总金额 kind 支出-0 收入-1
     * */
    public static double getSumMoneyOneYear(int year, int kind) {
        double total = 0;
        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        while (cursor.moveToNext()) {
            @SuppressLint("Range") double money = cursor.getDouble(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    /*
     * 根据 id 删除 accounttb 一条数据
     * */
    public static int deleteItemFromAccounttbById(int id) {
        int i = db.delete("accounttb", "id=?", new String[]{id + ""});
        return i;
    }

}
