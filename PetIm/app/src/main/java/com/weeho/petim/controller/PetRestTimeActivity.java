package com.weeho.petim.controller;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.weeho.petim.R;
import com.weeho.petim.RetorfitWapper.ApiCallUtil;
import com.weeho.petim.adapter.AlarmAdapter;
import com.weeho.petim.base.BaseActivity;
import com.weeho.petim.lib.utils.SharedPreferencesUtil;
import com.weeho.petim.lib.utils.StringUtil;
import com.weeho.petim.lib.utils.WeakHandler;
import com.weeho.petim.modle.AlarmBaseBean;
import com.weeho.petim.modle.BaseBean;
import com.weeho.petim.modle.LoginBeanSave;
import com.weeho.petim.network.ApiHttpCilent;
import com.weeho.petim.util.ConstantsUtil;
import com.weeho.petim.util.ToastUtil;
import com.weeho.petim.view.MyClock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wangkui on 2017/5/25.
 * 作息时间
 */

public class PetRestTimeActivity extends BaseActivity {
    private MyHandler handler = new MyHandler(this);
    private RecyclerView recycle_alarm;
    private ArrayList<AlarmBaseBean.ResultBean.AlarmBean> list;
    private ApiCallUtil mLoginFamuse;
    private String userid;
    private AlarmBaseBean mAlarmBaseBean;
    private AlarmAdapter alarmAdapter;
    private AlarmManager alarmManager;
    private BaseBean mBaseBean;
    private MyClock myclock;
    //是否局部更新
    private boolean isItemUpdate ;
    private int alarm_id;
    private String alarm_time;

    public  class MyHandler extends WeakHandler<PetRestTimeActivity> {

        private String userId;
        private String hxUserName;
        private String hxpassword;
        private AlarmBaseBean.ResultBean.AlarmBean mPushBean;

        public MyHandler(PetRestTimeActivity reference) {
            super(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantsUtil.CONTENT_SUCCESS:
                    // 成功
                    UpdateView();
                    break;
                case ConstantsUtil.CONTENT_SUCCESS_FOUR:
                    //更新是否开启推送
                    if(mLoginFamuse == null){
                        mLoginFamuse = ApiCallUtil.getInstant(getReference());
                    }
                    isItemUpdate = true;
                    mPushBean = (AlarmBaseBean.ResultBean.AlarmBean) msg.obj;
                    alarm_id = mPushBean.getAlarmId();
                    UpdateAlarm(null, mPushBean.getAlarmId(), mPushBean.isPush());
                    break;
                case ConstantsUtil.CONTENT_SUCCESS_THREE:
                    // 成功
                    ToastUtil.showToast(getReference(),"修改成功");
                    //需要局部更新
                    if(list != null  ){
                        int zise = list.size();
                        int index_position = 0;
                        for(int i = 0;i < zise;i++){
                            if(list.get(i).getAlarmId() == alarm_id){
                                if(!StringUtil.isEmpty(alarm_time)){
                                    list.get(i).setAlarmTime(alarm_time);
                                }else{
                                    if(mPushBean!=null)
                                    list.get(i).setPush(mPushBean.isPush());
                                }
                                index_position = i;
                                break;
                            }
                        }
                        //局部刷新
                        alarmAdapter.SetList(list);
                        alarmAdapter.notifyItemChanged(index_position);
                        alarm_time = null;
                    }
//                    getNetData();
                    break;
                case ConstantsUtil.CONTENT_SUCCESS_TWO:
                    // 修改成功 时间
                    alarm_time = (String) msg.obj;
                    alarm_id = msg.arg1;
                    UpdateAlarm(alarm_time, alarm_id,true);
                    break;
                case ConstantsUtil.CONTENT_FAIL:
                    // 成功
                    String msgError = (String) msg.obj;
                    if(!StringUtil.isEmpty(msgError))
                        ToastUtil.showToast(getReference(),msgError);
                    else
                        ToastUtil.showToast(getReference(),"数据异常，请您稍后重试");
                    break;
            }
        }
    }


    private void UpdateAlarm(String alarmTime,int alarmid, boolean isPush) {
        Map<String,Object> map = new HashMap<>();
        map.put("userid",userid);
        if(!StringUtil.isEmpty(alarmTime))
        map.put("alarmtime",alarmTime);
        if(StringUtil.isEmpty(alarmTime))
        map.put("isPush",isPush);
        map.put("alarmid",String.valueOf(alarmid));
        mLoginFamuse.UpdatePetAlarmInfo(map).enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                mBaseBean = response.body();
                if(mBaseBean ==null)
                    return;
                Message message = Message.obtain();
                if("1".equals(mBaseBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS_THREE;
                    message.obj = mBaseBean.getResult();
                }else{
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = mBaseBean.getError().getInfo();
                }
                handler.sendMessage(message);
                Dimess();
            }

            @Override
            public void onFailure(Call<BaseBean> call, Throwable t) {
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
                Dimess();
            }
        });
    }

    //更新试图
    private void UpdateView() {
        if(mAlarmBaseBean != null && mAlarmBaseBean.getResult()!=null && mAlarmBaseBean.getResult().getAlarm()!=null){
            list = (ArrayList<AlarmBaseBean.ResultBean.AlarmBean>) mAlarmBaseBean.getResult().getAlarm();
            alarmAdapter.SetList(list);
            alarmAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate() {
        setBaseContentView(R.layout.weeho_alam_time);
        initView();
    }

    private void initView() {
        //闹钟管理器
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        recycle_alarm = (RecyclerView) findViewById(R.id.recycle_alarm);
        myclock = (MyClock) findViewById(R.id.myclock);
        list = new  ArrayList();
        alarmAdapter = new AlarmAdapter(list,this,alarmManager,handler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycle_alarm.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycle_alarm.setAdapter(alarmAdapter);
    }


    @Override
    protected boolean hasTitle() {
        return true;
    }

    @Override
    protected void loadChildView() {

    }
    private String GetUserId() {
        LoginBeanSave loginBeanSave  = (LoginBeanSave) SharedPreferencesUtil.get(this,"login","login");
        return loginBeanSave.getUserId();
    }
    @Override
    protected void getNetData() {
        userid = GetUserId() ;
        if(mLoginFamuse==null)
            mLoginFamuse = ApiCallUtil.getInstant(this);
        mLoginFamuse.GetPetAlarmInfo(userid).enqueue(new Callback<AlarmBaseBean>() {
            @Override
            public void onResponse(Call<AlarmBaseBean> call, Response<AlarmBaseBean> response) {
                mAlarmBaseBean = response.body();
                if(mAlarmBaseBean ==null)
                    return;
                Message message = Message.obtain();
                if("1".equals(mAlarmBaseBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS;
                    message.obj = mAlarmBaseBean.getResult();
                }else{
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = mAlarmBaseBean.getError().getInfo();
                }
                handler.sendMessage(message);
                Dimess();
            }

            @Override
            public void onFailure(Call<AlarmBaseBean> call, Throwable t) {
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
                Dimess();
            }
        });
    }
    private void Dimess() {
       runOnUiThread(new Runnable() {
            public void run() {
                if(ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing())
                    ApiHttpCilent.loading.dismiss();
            }
        });
    }
    @Override
    protected void reloadCallback() {

    }

    @Override
    protected void setChildViewListener() {

    }

    @Override
    protected String setTitleName() {
        return "作息时间";
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
