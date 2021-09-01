package com.luna.tally;

import android.app.Application;

import com.luna.tally.db.DBManager;

/*全局應用類*/
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化数据库
        DBManager.initDB(getApplicationContext());
    }
}
