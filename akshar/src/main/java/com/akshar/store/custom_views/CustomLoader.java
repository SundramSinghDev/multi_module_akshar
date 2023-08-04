package com.akshar.store.custom_views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.Wave;
import com.akshar.store.R;

import java.util.Objects;

public class CustomLoader extends Dialog {
    private Context context;

    public CustomLoader(@NonNull Context context) {
        super(context, R.style.TransparentProgressDialog);
        this.context = context;

        WindowManager.LayoutParams wlmp = Objects.requireNonNull(getWindow()).getAttributes();
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);

        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);

        View view = View.inflate(this.context, R.layout.loader, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addContentView(view, params);
    }

    @Override
    public void show() {
        try{
            if (!((Activity) context).isDestroyed() && !((Activity) context).isFinishing())
            {
                super.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try{
            if (!((Activity) context).isDestroyed()) {
                super.dismiss();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
