package com.luna.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.luna.tally.R;

public class BeiZhuDialog extends Dialog implements View.OnClickListener {
    EditText et;
    Button cancelBtn, ensureBtn;
    OnEnsureListener onEnsureListener;

    // 设置回调接口方法
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public BeiZhuDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_beizhu); //设置对话框显示布局
        et = findViewById(R.id.dialog_beizhu_et);
        cancelBtn = findViewById(R.id.dialog_beizhu_btn_cancle);
        ensureBtn = findViewById(R.id.dialog_beizhu_btn_ensure);
        cancelBtn.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    /*接口*/
    public interface OnEnsureListener {
        public void onEnsure();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_beizhu_btn_cancle:
                cancel();
                break;
            case R.id.dialog_beizhu_btn_ensure:
                if (onEnsureListener != null) {
                    onEnsureListener.onEnsure();
                }
                break;
        }
    }

    /*獲取輸入數據方法*/
    public String getEditText() {
        return et.getText().toString().trim();
    }

    /*設置Dialog尺寸與屏幕一致*/
    public void setDialogSize() {
        // 获取当前窗口对象
        Window window = getWindow();
        // 获取窗口对象参数
        WindowManager.LayoutParams attributes = window.getAttributes();
        // 获取屏幕宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        attributes.width = (int) (d.getWidth()); // 对话窗口为屏幕窗口
        attributes.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(attributes);
        handler.sendEmptyMessageDelayed(1, 100);
    }

    /* 弹出软键盘 */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
