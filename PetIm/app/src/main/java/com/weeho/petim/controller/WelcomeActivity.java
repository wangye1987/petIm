package com.weeho.petim.controller;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hyphenate.chat.EMClient;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.weeho.petim.lib.activityManager.StartActivityUtil;
import com.weeho.petim.lib.utils.SharedPreferencesUtil;
import com.weeho.petim.lib.utils.StringUtil;

import com.weeho.petim.R;
import com.weeho.petim.base.BaseActivity;
import com.weeho.petim.modle.LoginBeanSave;
import com.weeho.petim.util.ConstantsUtil;

/**
 * Created by wangkui on 2017/5/10.
 */

public class WelcomeActivity extends BaseActivity {
    private ImageView splase;

    private IWXAPI api;
    @Override
    protected void onCreate() {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setBaseContentView(R.layout.welcome);
        initView();
    }

    //注册到微信
    private void RegisterWx(){
        //获取IWXAPI实例
        api =  WXAPIFactory.createWXAPI(this, ConstantsUtil.APP_ID,true);
        //注册到微信
        api.registerApp(ConstantsUtil.APP_ID);
    }
    private void initView() {
        splase = (ImageView) findViewById(R.id.splase);
        //如果没有登录过 跳转到登录页面 登录过就直接跳转到首页
                    splase.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(SharedPreferencesUtil.get(WelcomeActivity.this,"login","login") != null){
                                final LoginBeanSave loginBeanSave = (LoginBeanSave) SharedPreferencesUtil.get(WelcomeActivity.this,"login","login");
                                if(loginBeanSave!=null) {
                                    if(!StringUtil.isEmpty(loginBeanSave.getUserId()) && !StringUtil.isEmpty(loginBeanSave.getHxUserName()) && !StringUtil.isEmpty(loginBeanSave.getHxpassword())){
                                        LoginMainActivity(loginBeanSave.getUserId(),loginBeanSave.getHxUserName(),loginBeanSave.getHxpassword());
                                        }else{
                                        StartActivityUtil.startActivity(WelcomeActivity.this,LoginActivity.class);
                                        finish();
                                    }
                                }
                                   } else{
                                           StartActivityUtil.startActivity(WelcomeActivity.this,LoginActivity.class);
                                           finish();
                                   }
                        } }, 2000);
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
        }

    //登录成功
    private void LoginMainActivity(String userid,String hxUserName,String hxPassword) {
        Intent intent = new Intent();
        intent.putExtra("userid",userid);
        intent.putExtra("hxUserName",hxUserName);
        intent.putExtra("hxPassword",hxPassword);
        intent.setClass(this,MainActivity.class);
        StartActivityUtil.startActivity(this,intent);
        finish();
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
