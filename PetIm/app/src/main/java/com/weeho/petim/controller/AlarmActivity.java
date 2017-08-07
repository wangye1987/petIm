package com.weeho.petim.controller;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.weeho.petim.R;
import com.weeho.petim.base.BaseActivity;
import com.weeho.petim.lib.utils.SharedPreferencesUtil;
import com.weeho.petim.receiver.AlarmReceiver;
import com.weeho.petim.util.ToastUtil;

import java.util.Calendar;

//import android.support.annotation.RequiresApi;

/**
 * Created by wangkui on 2017/5/11.
 *
 * 设置闹钟界面
 */

//@RequiresApi(api = Build.VERSION_CODES.N)
public class AlarmActivity extends BaseActivity {

    private TimePicker timePicker;
    private Button alarm_button;
    private Activity mActivity;
    private AlarmManager alarmManager;
    private Calendar calendar = Calendar.getInstance();
    private TextView bt_time;

    @Override
    protected void onCreate() {
        setBaseContentView(R.layout.weeho_alarm);
        initView();
    }

    private void initView() {
        mActivity = AlarmActivity.this;
        alarm_button = (Button) findViewById(R.id.alarm_button);
        //闹钟管理器
        alarmManager = (AlarmManager) mActivity.getSystemService(mActivity.ALARM_SERVICE);
    }
    public AlertDialog.Builder ShowDialog(Context context) {
        // TODO Auto-generated constructor stub
        this.mActivity= (Activity) context;
        AlertDialog.Builder ad=new AlertDialog.Builder(context);
        //关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
//        Window window = ad.getWindow();
//        window.setContentView(R.layout.alertdialog);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.alertdialog,null);
        ad.setView(view);
        ad.show();
        bt_time = (TextView) view.findViewById(R.id.bt_time);
        bt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ToastUtil.showToast(mActivity,hourOfDay+"时"+minute+"分");
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        SharedPreferencesUtil.writeSharedPreferencesLong(mActivity,"alarm","alarm",calendar.getTimeInMillis());
                        Intent intent = new Intent(mActivity, AlarmReceiver.class);
                        PendingIntent pendingIntent  = PendingIntent.getBroadcast(mActivity,0,intent,0);
    //                 alarmManager.setWindow(RTC_WAKEUP,calendar.getTimeInMillis(),0,pendingIntent);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
                dialog.show();
            }
        });
        return ad;
    }
    public void Alarm(View view){
        ShowDialog(mActivity);

    }


    @Override
    protected boolean hasTitle() {
        return false;
    }

    @Override
    protected void loadChildView() {

    }

    @Override
    protected void getNetData() {

    }

    @Override
    protected void reloadCallback() {

    }

    @Override
    protected void setChildViewListener() {

    }

    @Override
    protected String setTitleName() {
        return null;
    }

    @Override
    protected String setRightText() {
        return null;
    }

    @Override
    protected int setLeftImageResource() {
        return 0;
    }

    @Override
    protected int setMiddleImageResource() {
        return 0;
    }

    @Override
    protected int setRightImageResource() {
        return 0;
    }
}
