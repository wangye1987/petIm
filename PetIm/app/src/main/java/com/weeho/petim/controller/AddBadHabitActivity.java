package com.weeho.petim.controller;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
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
import com.weeho.petim.adapter.AddIndoorAdapter;
import com.weeho.petim.adapter.AddOutdoorAdapter;
import com.weeho.petim.base.BaseActivity;
import com.weeho.petim.modle.AcheveBaseBean;
import com.weeho.petim.modle.AchievementBean;
import com.weeho.petim.modle.BadHabitBean;
import com.weeho.petim.modle.BadHabitDicBean;
import com.weeho.petim.modle.BadHabitDicBean.ResultBean.DataBean.IndoorBean;
import com.weeho.petim.modle.BadHabitDicBean.ResultBean.DataBean.OutdoorBean;
import com.weeho.petim.modle.BaseBean;
import com.weeho.petim.modle.LoginBeanSave;
import com.weeho.petim.modle.UserInfoBean;
import com.weeho.petim.network.ApiHttpCilent;
import com.weeho.petim.util.ConstantsUtil;
import com.weeho.petim.util.ToastUtil;

/**
 * Created by wangkui on 2017/4/26.
 * 宠物数据字典界面
 */

public class AddBadHabitActivity extends BaseActivity {
    private RelativeLayout rv_outdoor;
    private RelativeLayout rv_indoor;
    private ViewPager vp_habit;
    private View viewindoor;
    private View viewoutdoor;
    private ArrayList<View> viewList;
    private MyPagerAdapter adapter;
    private TextView indoor;
    private TextView outdoor;
    private TextView line_indoor;
    private TextView line_outdoor;
    private TextView tv_title;
    private ImageView ivbaxk;
    private TextView base_activity_title_left;
    private LinearLayout linear_back;
    private FragmentActivity mactivty;
    private String userid;
    private MyHandler handler = new MyHandler(this);
    private BadHabitDicBean badHabitDicBean;
    private BadHabitDicBean.ResultBean.DataBean datedic;
    private ArrayList<IndoorBean> listindoor = new ArrayList<>();
    private ArrayList<OutdoorBean> listoutdoor = new ArrayList<>();
    private RecyclerView indoor_recyclerview;
    private RecyclerView outdoor_recyclerview;
    private Button bt_outdoor;
    private Button bt_indoor;
    private StringBuffer sb_submit;
    private String submitid ;
    private Activity mActivity;
    private ApiCallUtil mApiCallUtil;

    @Override
    protected void onCreate() {
        setBaseContentView(R.layout.add_badhabit);
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



    private void initData() {
        mActivity = this;
        mApiCallUtil = ApiCallUtil.getInstant(mActivity);
        sb_submit = new StringBuffer();
        userid =  GetUserId();
        //获取宠物恶习字典
        GetHabitDic();
    }
    private void GetHabitDic() {
        ApiHttpCilent.CreateLoading(mActivity);
        mApiCallUtil.GetPetHabitDic(userid).enqueue(new Callback<BadHabitDicBean>() {
            @Override
            public void onResponse(Call<BadHabitDicBean> call, Response<BadHabitDicBean> response) {
                Dimess();
                badHabitDicBean = response.body();
                Message message = Message.obtain();
                if("1".equals(badHabitDicBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS;
                    message.obj = badHabitDicBean.getResult();
                }else{
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = badHabitDicBean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<BadHabitDicBean> call, Throwable t) {
                Dimess();
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
            }
        });
    }
    private void Dimess() {
        AddBadHabitActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if(ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing())
                    ApiHttpCilent.loading.dismiss();
            }
        });
    }

    public  class MyHandler extends WeakHandler<AddBadHabitActivity> {

        private String userId;

        public MyHandler(AddBadHabitActivity reference) {
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
                case ConstantsUtil.CONTENT_SUCCESS_TWO:
                    // 成功
                    ToastUtil.showToast(getReference(),"添加成功");
                    setResult(RESULT_OK);
                    finish();
                    break;
                case ConstantsUtil.CONTENT_FAIL:
                    // 成功
                    ToastUtil.showToast(getReference(),"操作失败");
                    break;
            }
        }
    }

    private void UpdateView() {
        if(badHabitDicBean != null && badHabitDicBean.getResult()!= null &&  badHabitDicBean.getResult().getData()!= null){
            datedic = badHabitDicBean.getResult().getData();
            listindoor = (ArrayList<IndoorBean>) datedic.getIndoor();
            listoutdoor = (ArrayList<OutdoorBean>) datedic.getOutdoor();
            initIndoorData();
            initOutdoorData();
        }
    }

    void setDefault(){
        indoor.setTextColor(getResources().getColor(R.color.gray_drak));
        outdoor.setTextColor(getResources().getColor(R.color.gray_drak));
        line_indoor.setVisibility(View.INVISIBLE);
        line_outdoor.setVisibility(View.INVISIBLE);

        Drawable drawable = getResources().getDrawable(R.drawable.shinei);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        indoor.setCompoundDrawables(drawable,null,null,null);

        Drawable drawable1 = getResources().getDrawable(R.drawable.shiwai);
        // 这一步必须要做,否则不会显示.
        drawable1.setBounds(0,0,drawable1.getMinimumWidth(),drawable1.getMinimumHeight());
        outdoor.setCompoundDrawables(drawable1,null,null,null);

    }
    private void initView() {
        mactivty = AddBadHabitActivity.this;
        rv_outdoor = (RelativeLayout) findViewById(R.id.rv_outdoor);
        rv_indoor = (RelativeLayout) findViewById(R.id.rv_indoor);
        indoor = (TextView) findViewById(R.id.indoor);
        outdoor = (TextView) findViewById(R.id.outdoor);
        line_indoor = (TextView) findViewById(R.id.line_indoor);
        line_outdoor = (TextView) findViewById(R.id.line_outdoor);
        vp_habit = (ViewPager) findViewById(R.id.vp_habit);
        viewindoor = LinearLayout.inflate(this, R.layout.indoor_item,null);
        viewoutdoor = LinearLayout.inflate(this, R.layout.outdoor_item,null);
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中  


        indoor_recyclerview = (RecyclerView) viewindoor.findViewById(R.id.indoor_recyclerview);
        bt_indoor = (Button) viewindoor.findViewById(R.id.bt_indoor);
        outdoor_recyclerview = (RecyclerView) viewoutdoor.findViewById(R.id.outdoor_recyclerview);
        bt_outdoor = (Button) viewoutdoor.findViewById(R.id.bt_outdoor);

        StaggeredGridLayoutManager stagger = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        StaggeredGridLayoutManager staggeroutdoor = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        indoor_recyclerview.setLayoutManager(stagger);
        outdoor_recyclerview.setLayoutManager(staggeroutdoor);

        viewList.add(viewindoor);
        viewList.add(viewoutdoor);
        adapter = new MyPagerAdapter();
        vp_habit.setAdapter(adapter);
        vp_habit.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setDefault();
                if(0==position) {
                    indoor.setTextColor(getResources().getColor(R.color.FF2AA53A));
                    line_indoor.setBackgroundColor(getResources().getColor(R.color.FF2AA53A));
                    line_indoor.setVisibility(View.VISIBLE);
                    Drawable drawable = getResources().getDrawable(R.drawable.shineied);
                    // 这一步必须要做,否则不会显示.
                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                    indoor.setCompoundDrawables(drawable,null,null,null);
                }else{
                    outdoor.setTextColor(getResources().getColor(R.color.FF2AA53A));
                    line_outdoor.setBackgroundColor(getResources().getColor(R.color.FF2AA53A));
                    line_outdoor.setVisibility(View.VISIBLE);
                    Drawable drawable = getResources().getDrawable(R.drawable.shiwaied);
                    // 这一步必须要做,否则不会显示.
                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                    outdoor.setCompoundDrawables(drawable,null,null,null);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
//        setLeftText("宠物恶习");
        base_activity_title_left = (TextView) findViewById(R.id.base_activity_title_left);
        base_activity_title_left.setVisibility(View.VISIBLE);
        base_activity_title_left.setText("宠物恶习");
        tv_title = (TextView) findViewById(R.id.base_activity_title_titlename);
        tv_title.setText("");
        ivbaxk = (ImageView) findViewById(R.id.base_activity_title_backicon);
        linear_back = (LinearLayout) findViewById(R.id.linear_back);
        rv_indoor.setOnClickListener(this);
        rv_outdoor.setOnClickListener(this);
        ivbaxk.setOnClickListener(this);
        linear_back.setOnClickListener(this);
        bt_indoor.setOnClickListener(this);
        bt_outdoor.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_back:
                finish();
                break;
            case R.id.base_activity_title_backicon:
                finish();
                break;
            case R.id.rv_indoor:
                vp_habit.setCurrentItem(0);
                break;
            case R.id.rv_outdoor:
                vp_habit.setCurrentItem(1);
                break;
            case R.id.bt_indoor:
                SubmitDic(0);
                break;
            case R.id.bt_outdoor:
                SubmitDic(1);

                break;
        }
    }
    private String GetUserId() {
        LoginBeanSave loginBeanSave  = (LoginBeanSave) SharedPreferencesUtil.get(this,"login","login");
        return loginBeanSave.getUserId();
    }
    //提交的数据集合
    private void SubmitDic(int type){
        sb_submit.delete(0,sb_submit.length());
          if(type == 0){
              for(IndoorBean indoorBean:listindoor){
                     if(indoorBean.getCheck()==0){
                         sb_submit.append(indoorBean.getViceid()+",");
                     }
              }
          }else{
              for(OutdoorBean outdoorBean:listoutdoor){
                  if(outdoorBean.getCheck()==0){
                      sb_submit.append(outdoorBean.getViceid()+",");
                  }
              }
          }
       int lenght =  sb_submit.toString().length();
        submitid = sb_submit.toString();
          if(submitid.endsWith(",")){
              submitid = submitid.substring(0,lenght-1);
          }
        if(StringUtil.isEmpty(userid))
            userid = GetUserId();
          //修改
//        ApiHttpCilent.getInstance(mactivty).PetHabitUpdateInfo(mactivty,type+"", userid,submitid,new MyUpdateCallBack());
        ApiHttpCilent.CreateLoading(mActivity);
        mApiCallUtil.UptadeHabitInfo(type+"",submitid,userid).enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                Dimess();
                BaseBean  baseBean = response.body();
                Message message = Message.obtain();
                if("1".equals(baseBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS_TWO;
                    message.obj = baseBean.getResult();
                }else{
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = baseBean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<BaseBean> call, Throwable t) {
                Dimess();
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
            }
        });
    }
    private void initIndoorData() {
        AddIndoorAdapter indoorAdapter = new AddIndoorAdapter(listindoor,this);
        indoor_recyclerview.setAdapter(indoorAdapter);
    }
    private void initOutdoorData() {
        AddOutdoorAdapter outdoorAdapter = new AddOutdoorAdapter(listoutdoor,this);
        outdoor_recyclerview.setAdapter(outdoorAdapter);
    }


    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(viewList.get(position));
            return viewList.get(position);
        }
    }
}
