package com.weeho.petim.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.weeho.petim.controller.PetRestTimeActivity;
import com.weeho.petim.lib.activityManager.StartActivityUtil;
import com.weeho.petim.lib.utils.SharedPreferencesUtil;

import com.weeho.petim.R;
import com.weeho.petim.base.BaseFragment;
import com.weeho.petim.controller.AlarmActivity;
import com.weeho.petim.controller.BadHabitsActivity;
import com.weeho.petim.controller.LoginActivity;
import com.weeho.petim.controller.MainActivity;
import com.weeho.petim.controller.PetInfoActivity;
import com.weeho.petim.controller.UserInfoActivity;
import com.weeho.petim.network.ApiHttpCilent;
import com.weeho.petim.network.Utils;
import com.weeho.petim.util.ToastUtil;
import com.weeho.petim.view.CommonDialog;
import com.weeho.petim.view.LoadingView;

import java.util.zip.Inflater;

/**
 * Created by wangkui on 2017/4/20.
 */

public class MyFragment extends BaseFragment {

    private LinearLayout userinfo;
    private LinearLayout petinfo;
    private LinearLayout petex;
    private LinearLayout petresttime;
    private LinearLayout aboutus;
    private LinearLayout login_out;
    private Context mContext;
    private String userid;

    @Override
    protected boolean isShowLeftIcon() {
        return false;
    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my, container,
                true);
        initView(rootView);
        initData();
        return rootView;
    }

    private void initData() {
       Bundle  bundle = getArguments();//从activity传过来的Bundle  
        userid = (String) bundle.get("userid");
    }

    private void initView(View rootView) {
        mContext = getActivity();
        userinfo = (LinearLayout) rootView.findViewById(R.id.userinfo);
        petinfo = (LinearLayout) rootView.findViewById(R.id.petinfo);
        petex = (LinearLayout) rootView.findViewById(R.id.petex);
        petresttime = (LinearLayout) rootView.findViewById(R.id.petresttime);
        aboutus = (LinearLayout) rootView.findViewById(R.id.aboutus);
        login_out = (LinearLayout) rootView.findViewById(R.id.login_out);
    }

    @Override
    protected void getNetData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        if(!Utils.isNetworkConnected(getActivity()))
            return;
        switch (v.getId()){
            case R.id.userinfo:
                intent.putExtra("userid",userid);
                intent.setClass((Activity) mContext, UserInfoActivity.class);
                StartActivityUtil.startActivity((Activity) mContext, intent);
                break;
            case R.id.petinfo:
                StartActivityUtil.startActivity((Activity) mContext, PetInfoActivity.class);
                break;
            case R.id.petex:
                //恶习
                StartActivityUtil.startActivity((Activity) mContext, BadHabitsActivity.class);
                break;
            case R.id.aboutus:
//                ToastUtil.showToast(mContext,"功能暂未开放");
//                StartActivityUtil.startActivity(getActivity(), AlarmActivity.class);
                break;
            case R.id.login_out:
                ShowDialog(getActivity());
                break;
            case R.id.petresttime:
                StartActivityUtil.startActivity(getActivity(),PetRestTimeActivity.class);
                break;
        }
    }

    private void LoginHxCount() {
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        ToastUtil.showToast(mContext,"退出成功");
                                if(ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing())
                                    ApiHttpCilent.loading.dismiss();
                    }

                });
                SharedPreferencesUtil.writeSharedPreferencesBoolean(mContext,"islogin","login",false);
                //清除登录数据
                SharedPreferencesUtil.ClearSharedPreferences(mContext,"login");
                Intent intent = new Intent();
                intent.putExtra("loginout",true);
                intent.setClass(mContext,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                StartActivityUtil.startActivity((Activity) mContext,intent);
                ((Activity) mContext).finish();
                if(ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing())
                    ApiHttpCilent.loading.dismiss();
            }
            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                ToastUtil.showToast(mContext,"退出失败,请重试");
                if(ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing())
                    ApiHttpCilent.loading.dismiss();
            }
        });
    }

    public void ShowDialog(Activity mActivity) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mActivity);
        View mView = LinearLayout.inflate(mActivity,R.layout.login_out,null);
        mBuilder.setView(mView);
        mBuilder.create();

        final Dialog mDialog =  mBuilder.show();
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button loginOut_ok = (Button) mView.findViewById(R.id.loginOut_ok);
        Button loginOut_Cancle = (Button) mView.findViewById(R.id.loginOut_cancle);
        loginOut_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击“确认”后的操作
                        ApiHttpCilent.CreateLoading(getActivity());
                        LoginHxCount();
            }
        });
        loginOut_Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDialog!=null && mDialog.isShowing())
                    mDialog.dismiss();
            }
        });
    }
    @Override
    protected void setViewListener() {
        userinfo.setOnClickListener(this);
        petinfo.setOnClickListener(this);
        petex.setOnClickListener(this);
        petresttime.setOnClickListener(this);
        aboutus.setOnClickListener(this);
        login_out.setOnClickListener(this);
    }

    @Override
    protected boolean hasTitle() {
        return true;
    }

    @Override
    protected boolean hasTitleIcon() {
        return false;
    }

    @Override
    protected boolean hasDownIcon() {
        return false;
    }

    @Override
    protected void reloadCallback() {

    }

    @Override
    protected String setTitleName() {
        return "我";
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
