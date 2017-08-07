package com.weeho.petim.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.weeho.petim.controller.AlarmActivity;
import com.weeho.petim.controller.AlarmNoticeActivity;

/**
 * Created by wangkui on 2017/5/11.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i=new Intent(context, AlarmNoticeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
