package com.luna.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.luna.tally.R;

/*
 * 记录页面弹出时间对话框
 * */
public class SelectTimeDialog extends Dialog implements View.OnClickListener {
    EditText hourEt, minuteEt;
    DatePicker datePicker;
    Button ensureBtn, cancelBtn;

    public interface OnEnsureListener {
        public void onEnsure(String time, int year, int month, int day);
    }

    OnEnsureListener onEnsureListener;

    public OnEnsureListener getOnEnsureListener() {
        return onEnsureListener;
    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public SelectTimeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time);
        hourEt = findViewById(R.id.dialog_time_et_hour);
        minuteEt = findViewById(R.id.dialog_time_et_minute);
        datePicker = findViewById(R.id.dialog_time_dp);
        ensureBtn = findViewById(R.id.dialog_time_btn_ensure);
        cancelBtn = findViewById(R.id.dialog_time_btn_cancel);
        ensureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_time_btn_ensure:
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();
                String monthStr = String.valueOf(month);
                String dayStr = String.valueOf(day);
                if (month < 10) {
                    monthStr = "0" + month;
                }
                if (day < 10) {
                    dayStr = "0" + day;
                }
                String hourStr = hourEt.getText().toString();
                String minuteStr = minuteEt.getText().toString();
                int hour = 0;
                if (!TextUtils.isEmpty(hourStr)) {
                    hour = Integer.parseInt(hourStr);
                    hour = hour % 24;
                }
                int minute = 0;
                if (!TextUtils.isEmpty(minuteStr)) {
                    minute = Integer.parseInt(minuteStr);
                    minute = minute % 60;
                }

                hourStr = String.valueOf(hour);
                minuteStr = String.valueOf(minute);
                if (hour < 10) {
                    hourStr = "0" + hour;
                }
                if (minute < 10) {
                    minuteStr = "0" + minute;
                }

                String timeFormat = year + "年" + monthStr + "月" + dayStr + "日" + " " + hourStr + ":" + minuteStr;

                if (onEnsureListener != null) {
                    onEnsureListener.onEnsure(timeFormat, year, month, day);
                }
                cancel();
                break;
            case R.id.dialog_time_btn_cancel:
                cancel();
                break;
        }
    }
}
