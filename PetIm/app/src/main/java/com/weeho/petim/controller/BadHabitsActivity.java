package com.weeho.petim.controller;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.weeho.petim.lib.activityManager.StartActivityUtil;
import com.weeho.petim.lib.utils.SharedPreferencesUtil;
import com.weeho.petim.lib.utils.StringUtil;
import com.weeho.petim.lib.utils.WeakHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.weeho.petim.R;
import com.weeho.petim.RetorfitWapper.ApiCallUtil;
import com.weeho.petim.adapter.AchievementAdapter;
import com.weeho.petim.adapter.BadBabitAdapter;
import com.weeho.petim.adapter.BadhabitAdapter;
import com.weeho.petim.base.BaseActivity;
import com.weeho.petim.modle.AchievementBean;
import com.weeho.petim.modle.BadHabitBean;
import com.weeho.petim.modle.BadHabitBean.ResultBean;
import com.weeho.petim.modle.BadHabitBean.ResultBean.DataBean;
import com.weeho.petim.modle.BadHabitBean.ResultBean.DataBean.IndoorBean;
import com.weeho.petim.modle.BadHabitBean.ResultBean.DataBean.OutdoorBean;
import com.weeho.petim.modle.HxBaseBean;
import com.weeho.petim.modle.LoginBeanSave;
import com.weeho.petim.network.ApiHttpCilent;
import com.weeho.petim.util.ConstantsUtil;
import com.weeho.petim.util.ToastUtil;

/**
 * Created by wangkui on 2017/4/25.
 * 宠物恶习界面
 */

public class BadHabitsActivity extends BaseActivity {

    private RecyclerView indoor_recycleView;
    private RecyclerView id_outdoor_recyclerview;
    private FragmentActivity fragmentActivity;
    private String userid;
    private MyHandler handler = new MyHandler(this);
    private BadHabitBean badHabitBean;
    private ArrayList<IndoorBean> list_indoor = new ArrayList<>();
    private ArrayList<OutdoorBean> list_outdoor  = new ArrayList<>();
    private TextView tv_achieve_update;
    private TextView tv_achieve_xl;
    private TextView tv_indoor_empty;
    private TextView tv_outdoor_empty;
    private BadBabitAdapter badBabitAdapter;
    private BadhabitAdapter outdoorAdapter;
    private ApiCallUtil mApiCallUtil;

    public  class MyHandler extends WeakHandler<BadHabitsActivity> {

        private String userId;
        private String hxUserName;
        private String hxpassword;

        public MyHandler(BadHabitsActivity reference) {
            super(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantsUtil.CONTENT_SUCCESS:
                    // 成功
                    if(badHabitBean != null && badHabitBean.getResult()!= null && badHabitBean.getResult().getData()!=null){
                        BadHabitBean.ResultBean.DataBean datebean  = badHabitBean.getResult().getData();
                        if(datebean != null) {
                            list_indoor = (ArrayList<IndoorBean>) datebean.getIndoor();
                            list_outdoor = (ArrayList<OutdoorBean>) datebean.getOutdoor();
                        }
                    }
                    UpdateView(list_indoor,list_outdoor);
                    break;
                case ConstantsUtil.CONTENT_FAIL:
                    // 失败
                    ToastUtil.showToast(getReference(),"操作失败，请稍后重试");
                    break;
            }
        }
    }

    //更新试图
    private void UpdateView(ArrayList<IndoorBean> list_indoor,ArrayList<OutdoorBean> list_outdoor ) {
        int size_indoor = list_indoor.size();
        int size_outdoor = list_outdoor.size();
        if(size_indoor>0) {
            badBabitAdapter.SetList(list_indoor);
            tv_achieve_update.setText("(已有" + size_indoor + "项恶习)");
            tv_indoor_empty.setVisibility(View.GONE);
            indoor_recycleView.setVisibility(View.VISIBLE);
        }else{
            tv_indoor_empty.setVisibility(View.VISIBLE);
            indoor_recycleView.setVisibility(View.GONE);
        }
        if(size_outdoor>0) {
            outdoorAdapter.SetList(list_outdoor);
            tv_achieve_xl.setText("(已有" + size_outdoor + "项恶习)");
            tv_outdoor_empty.setVisibility(View.GONE);
            id_outdoor_recyclerview.setVisibility(View.VISIBLE);
        }else{
            tv_outdoor_empty.setVisibility(View.VISIBLE);
            id_outdoor_recyclerview.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate() {
        setBaseContentView(R.layout.bad_habit);
        initView();
        initData();
    }

    @Override
    protected boolean hasTitle() {
        return true;
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
        return "添加";
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


    private void initView() {
        fragmentActivity = BadHabitsActivity.this;
        tv_achieve_update = (TextView) findViewById(R.id.tv_achieve_update);
        tv_achieve_xl = (TextView) findViewById(R.id.tv_achieve_xl);

        tv_indoor_empty = (TextView) findViewById(R.id.tv_indoor_empty);
        tv_outdoor_empty = (TextView) findViewById(R.id.tv_outdoor_empty);

        TextView base_activity_title_left = (TextView) findViewById(R.id.base_activity_title_left);
        base_activity_title_left.setVisibility(View.VISIBLE);
        base_activity_title_left.setText("宠物恶习");
        TextView tv_title = (TextView) findViewById(R.id.base_activity_title_titlename);
        tv_title.setText("");
        tvRight.setOnClickListener(this);
        base_activity_title_left.setOnClickListener(this);
        indoor_recycleView = (RecyclerView) findViewById(R.id.id_indoor_recyclerview);
        StaggeredGridLayoutManager stagger = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        indoor_recycleView.setLayoutManager(stagger);

        id_outdoor_recyclerview = (RecyclerView) findViewById(R.id.id_outdoor_recyclerview);
        StaggeredGridLayoutManager stagger1 = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        id_outdoor_recyclerview.setLayoutManager(stagger1);

    }
    private void Dimess() {
        BadHabitsActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if(ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing())
                    ApiHttpCilent.loading.dismiss();
            }
        });
    }
//    class MyCallBack extends BaseJsonHttpResponseHandler<BadHabitBean> {
//        @Override
//        public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BadHabitBean response) {
//            Dimess();
//        }
//
//        @Override
//        public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, BadHabitBean errorResponse) {
//            Dimess();
//            Message message = Message.obtain();
//            message.what = ConstantsUtil.CONTENT_FAIL;// 错误
//            message.obj = "数据错误,请稍后重试";
//            handler.sendMessage(message);
//        }
//
//        @Override
//        protected BadHabitBean parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
//            Dimess();
//            Gson gson = new Gson();
//            badHabitBean = gson.fromJson(rawJsonData, BadHabitBean.class);
//            Message message = Message.obtain();
//            if("1".equals(badHabitBean.getStatus())) {
//                message.what = ConstantsUtil.CONTENT_SUCCESS;
//                message.obj = badHabitBean.getResult();
//            }else{
//                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
//                message.obj = badHabitBean.getError().getInfo();
//            }
//            handler.sendMessage(message);
//            return badHabitBean;
//        }
//    }
    private void initData() {
        badBabitAdapter = new BadBabitAdapter(list_indoor,this);
        indoor_recycleView.setAdapter(badBabitAdapter);


        outdoorAdapter = new BadhabitAdapter(list_outdoor,this);
        id_outdoor_recyclerview.setAdapter(outdoorAdapter);

        userid = GetUserId();
        //获取宠物恶习
//        ApiHttpCilent.getInstance(fragmentActivity).PetHabitInfo(fragmentActivity,userid,new MyCallBack());
        mApiCallUtil = ApiCallUtil.getInstant(fragmentActivity);
        //获取宠物恶习信息
        GetPetInfo();
    }
    private String GetUserId() {
        LoginBeanSave loginBeanSave  = (LoginBeanSave) SharedPreferencesUtil.get(this,"login","login");
        return loginBeanSave.getUserId();
    }
    private void GetPetInfo() {
        ApiHttpCilent.CreateLoading(fragmentActivity);
        mApiCallUtil.GetPetHabitInfo(userid).enqueue(new Callback<BadHabitBean>() {
            @Override
            public void onResponse(Call<BadHabitBean> call, Response<BadHabitBean> response) {
                Dimess();
                badHabitBean = response.body();
                Message message = Message.obtain();
                if("1".equals(badHabitBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS;
                    message.obj = badHabitBean.getResult();
                }else{
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = badHabitBean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<BadHabitBean> call, Throwable t) {
                Dimess();
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
      switch (v.getId()){
          case R.id.base_activity_title_right_righttv:
              //跳转到添加宠物恶习界面
              StartActivityUtil.startActivityForResult(BadHabitsActivity.this,AddBadHabitActivity.class,ConstantsUtil.RESULT_REQUEST);
              break;
      }
    }
    //添加成功回调在查询
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(StringUtil.isEmpty(userid))
                userid = GetUserId();
//            ApiHttpCilent.getInstance(fragmentActivity).PetHabitInfo(fragmentActivity,userid,new MyCallBack());
            //获取宠物恶习信息
              GetPetInfo();
        }
    }
}
