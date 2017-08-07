package com.weeho.petim.adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.weeho.petim.R;
import com.weeho.petim.controller.PetRestTimeActivity;
import com.weeho.petim.hxim.Constant;
import com.weeho.petim.lib.utils.SharedPreferencesUtil;
import com.weeho.petim.lib.utils.StringUtil;
import com.weeho.petim.modle.AcheveBaseBean;
import com.weeho.petim.modle.AlarmBaseBean;
import com.weeho.petim.network.Utils;
import com.weeho.petim.receiver.AlarmReceiver;
import com.weeho.petim.util.ConstantsUtil;
import com.weeho.petim.util.ToastUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wangkui on 2017/5/25.
 */

public class AlarmAdapter  extends RecyclerView.Adapter<AlarmAdapter.ViewHolder>{
    //定义闹钟相关信息
    ArrayList<AlarmBaseBean.ResultBean.AlarmBean> list;
    private View view;
    private AlarmAdapter.ViewHolder viewHolder;
    private Activity mActivity;
    private Calendar calendar = Calendar.getInstance();
    private AlarmManager alarmManager;
    private PetRestTimeActivity.MyHandler handler;
    String hour_check;
    String minute_check;
    Calendar mCalendar = Calendar.getInstance();
    // 当前界面的是否关闭推送
    private boolean isPush_statue ;

    public AlarmAdapter(ArrayList<AlarmBaseBean.ResultBean.AlarmBean> list, Activity mActivity,AlarmManager alarmManager,PetRestTimeActivity.MyHandler handler) {
        this.list = list;
        this.mActivity = mActivity;
        this.alarmManager = alarmManager;
        this.handler = handler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weeho_alarm_item_time,parent,false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void SetList(ArrayList<AlarmBaseBean.ResultBean.AlarmBean> list){
        this.list = list;
    }
    public void onMessage(Message msg,AlarmBaseBean.ResultBean.AlarmBean pushBean) {
        if(handler.obtainMessage(msg.what,msg.obj)!= null){
            Message msg_s = new Message();
            msg_s.obj = pushBean;
            msg_s.what = ConstantsUtil.CONTENT_SUCCESS_FOUR;
            msg = msg_s;
        }
        handler.sendMessage(msg);
    }
    @Override
    public void onBindViewHolder(final AlarmAdapter.ViewHolder holder, final int position) {
        final AlarmBaseBean.ResultBean.AlarmBean alarmBean = list.get(position);
        holder.alarm_title.setText(StringUtil.isEmpty(alarmBean.getRestTimeTitle())?"":alarmBean.getRestTimeTitle().trim());
        if(!StringUtil.isEmpty(alarmBean.getAlarmTime())){
            holder.alarm_time.setText(alarmBean.getAlarmTime());
            isPush_statue = alarmBean.isPush();
            if(alarmBean.isPush()) {
                holder.iv_isPush.setImageResource(R.drawable.open);
            }else {
                holder.iv_isPush.setImageResource(R.drawable.close);
            }

            holder.iv_isPush.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Message msg = new Message();
                    AlarmBaseBean.ResultBean.AlarmBean pushBean  = new  AlarmBaseBean.ResultBean.AlarmBean();
                    pushBean.setAlarmId(list.get(position).getAlarmId());
                    if(isPush_statue){
                        isPush_statue = false;
                        holder.iv_isPush.setImageResource(R.drawable.close);
                    }else{
                        isPush_statue = true;
                        holder.iv_isPush.setImageResource(R.drawable.open);
                    }
                    msg.obj = pushBean;
                    msg.what = ConstantsUtil.CONTENT_SUCCESS_FOUR;
                    pushBean.setPush(isPush_statue);

                    onMessage(msg,pushBean);
                }
            });
            holder.time_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
                        private String alarm_time;
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            if(hourOfDay<10){
                                hour_check = "0"+String.valueOf(hourOfDay);
                            }else{
                                hour_check = String.valueOf(hourOfDay);
                            }
                            if(minute<10){
                                minute_check = "0"+String.valueOf(minute);
                            }else{
                                minute_check = String.valueOf(minute);
                            }

                            alarm_time = hour_check+":"+minute_check ;
                            Message msg = new Message();
                            msg.obj = alarm_time;
                            msg.arg1 = alarmBean.getAlarmId();
                            msg.what = ConstantsUtil.CONTENT_SUCCESS_TWO;
                            handler.sendMessage(msg);
//                            calendar.setTimeInMillis(System.currentTimeMillis());
//                            calendar.set(Calendar.HOUR,hourOfDay);
//                            calendar.set(Calendar.MINUTE,minute);
//                            SharedPreferencesUtil.writeSharedPreferencesLong(mActivity,"alarm","alarm",calendar.getTimeInMillis());
//                            Intent intent = new Intent(mActivity, AlarmReceiver.class);
//                            PendingIntent pendingIntent  = PendingIntent.getBroadcast(mActivity,0,intent,0);
//                            //  alarmManager.setWindow(RTC_WAKEUP,calendar.getTimeInMillis(),0,pendingIntent);
//                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
                        }
                    },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
                    dialog.show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView alarm_title;
        TextView alarm_time;
        ImageView iv_isPush;
        LinearLayout time_linear;
        ViewHolder(View view){
            super(view);
            alarm_title = (TextView) view.findViewById(R.id.alarm_title);
            alarm_time = (TextView) view.findViewById(R.id.alarm_time);
            iv_isPush = (ImageView) view.findViewById(R.id.iv_isPush);
            time_linear = (LinearLayout) view.findViewById(R.id.time_linear);
        }
    }
}
