package com.weeho.petim.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weeho.petim.R;

/**
 * Created by wangkui on 2017/6/6.
 */

public class AlertDanDialog extends Dialog {

    private LinearLayout image_background;
    private ImageView image_head;
    private TextView tv_head;

    public AlertDanDialog(Context context) {
        super(context);
        this.show();
    }

    public AlertDanDialog(Context context, int theme) {
        super(context, theme);
        this.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weeho_dan_alert);
        image_background = (LinearLayout) findViewById(R.id.image_background);
        image_head = (ImageView) findViewById(R.id.image_head);
        tv_head = (TextView) findViewById(R.id.tv_head);
    }

    public void setbg(boolean isReach){
        if(isReach)
        image_background.setBackgroundResource(R.drawable.fsg);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            image_background.setBackground(null);
        }
    }
    public void setheadbg(int drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            image_head.setBackground(null);
        }
        image_head.setBackgroundResource(drawable);
    }

    public void setheadTv(boolean isReach,String tvName){
        if(isReach)
            tv_head.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        else
            tv_head.setTextColor(ContextCompat.getColor(getContext(), R.color.FFDCDCDE));
        tv_head.setText(tvName);
    }

    public void setDiagleBg(int bg){
            image_background.setBackgroundResource(bg);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                if(AlertDanDialog.this!=null)
                    AlertDanDialog.this.dismiss();
        }
        return super.onTouchEvent(event);
    }
}