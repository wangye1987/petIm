package com.weeho.petim.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.weeho.petim.lib.utils.SharedPreferencesUtil;

/**
 * Created by wangkui on 2017/5/11.
 * 监听重新开机注册闹钟广播
 */

public class BootCompletedReceiver extends BroadcastReceiver {

    private AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        //闹钟管理器
        if(alarmManager==null)
        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        long currtTime = (long) SharedPreferencesUtil.getSharedPreferencesLong(context,"alarm","alarm",0);
        Intent intents = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent  = PendingIntent.getBroadcast(context,0,intents,0);
//                 alarmManager.setWindow(RTC_WAKEUP,calendar.getTimeInMillis(),0,pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, currtTime, pendingIntent);
    }

}
