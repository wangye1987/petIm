package com.weeho.petim.controller;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weeho.petim.db.WeiXinInfoBean;
import com.weeho.petim.lib.activityManager.StartActivityUtil;
import com.weeho.petim.lib.utils.SharedPreferencesUtil;
import com.weeho.petim.lib.utils.StringUtil;
import com.weeho.petim.lib.utils.WeakHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.weeho.petim.R;
import com.weeho.petim.RetorfitWapper.ApiCallUtil;
import com.weeho.petim.base.BaseActivity;
import com.weeho.petim.modle.BaseAddressBean;
import com.weeho.petim.modle.LoginBeanSave;
import com.weeho.petim.modle.UploadImgBean;
import com.weeho.petim.modle.UserInfoBean;
import com.weeho.petim.modle.WxInfoDb;
import com.weeho.petim.network.ApiHttpCilent;
import com.weeho.petim.util.ConstantsUtil;
import com.weeho.petim.util.ToastUtil;
import com.weeho.petim.view.ChangeAddressDialog;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wangkui on 2017/4/25.
 */

public class UserInfoActivity extends BaseActivity {

    private RelativeLayout nick_relative;
    private LinearLayout sex_relative;
    private RelativeLayout wechat_relative;
    private RelativeLayout phone_relative;
    private RelativeLayout area_relative;
    private TextView tv_area;
    private TextView tv_phone;
    private TextView tv_wechat;
    private TextView tv_sex;
    private TextView tv_nick;
    private String titleName = "";
    private String nickName;
    private String sexName;
    private String phoneName;
    private String areaName;
    private String weichatName;
    private TextView tv_title_left;
    private LinearLayout linear_back;
    private String userid;
    private UserInfoBean.ResultBean resultBean;
    private MyHandler handler = new  MyHandler(this);
    private String areaId;
    private String nickNamese;
    private int sexNamese = -1;
    private String wechatNamese;
    private String phoneNamese;
    private String areaNamese;
    private String regionId_chioce;
    private int index;
    private BaseAddressBean baseAddressBean;
    //市ID
    private int regionId;
    private int type = 1;//1 修改个人信息 2 宠物信息
    private ApiCallUtil mApiCallUtil;
    List<WxInfoDb> listWx;
    private ChangeAddressDialog mChangeAddressDialog;
    private String setAdd;

    @Override
    protected void onCreate() {
        setBaseContentView(R.layout.userinfo);
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


    public  class MyHandler extends WeakHandler<UserInfoActivity> {

        private String userId;


        public MyHandler(UserInfoActivity reference) {
            super(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantsUtil.CONTENT_SUCCESS:
                    // 成功
                    resultBean = (UserInfoBean.ResultBean) msg.obj;
                   if(resultBean!=null && resultBean.getData()!=null)
                    getReference().UpdateView();
                    break;
                case ConstantsUtil.CONTENT_SUCCESS_TWO:
                    //获取地区成功
                    //获取基础信息地址信息正确
                    if (mChangeAddressDialog == null) {
                        mChangeAddressDialog = new ChangeAddressDialog(UserInfoActivity.this,baseAddressBean);
                    }
//                    mChangeAddressDialog.setAddress("北京市", "丰台区");
                    mChangeAddressDialog.show();
                    mChangeAddressDialog.setAddresskListener(new ChangeAddressDialog.OnAddressCListener() {
                        @Override
                        public void onClick(String province, String city) {
                            String text = "";
                            if(city.contains("-")){
                                text = city.split("-")[0];
                            }else{
                                text = city;
                            }
                            if(city.contains("-")) {
                                regionId_chioce = city.split("-")[1];
                            }else{
                                List<BaseAddressBean.ResultBean.DataBean> listAll =   baseAddressBean.getResult().getData();
                                if(!StringUtil.isEmpty(city)){
                                    for(BaseAddressBean.ResultBean.DataBean databean :listAll){
                                        if(databean.getName().equals(province)){
                                            List<BaseAddressBean.ResultBean.DataBean.CityBean> listCity = databean.getCity();
                                            for(BaseAddressBean.ResultBean.DataBean.CityBean cityBean:listCity){
                                                if(cityBean.getName().equals(city)){
                                                    regionId_chioce = cityBean.getId();
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }}
                            setAdd = province+text;
                            RefitUpdateUerInfo();
                        }
                    });
                    break;
                case ConstantsUtil.CONTENT_SUCCESS_THREE:
                    // 成功
                    ToastUtil.showToast(getReference(),"修改成功");
                    if(!StringUtil.isEmpty(setAdd))
                    tv_area.setText(setAdd);
                    break;

                case ConstantsUtil.CONTENT_FAIL:
                    // 失败
                    ToastUtil.showToast(getReference(),"操作失败，请稍后重试");
                    break;
            }
        }
    }
    //修改个人信息
    private void RefitUpdateUerInfo() {
        ApiHttpCilent.CreateLoading(this);
        HashMap<String,Object> map = new HashMap<>();

        if(!StringUtil.isEmpty(userid))
            map.put("userid",userid);
        if(!StringUtil.isEmpty(regionId_chioce))
            map.put("regionId",regionId_chioce);
        mApiCallUtil.UptadeUserInfo(map).enqueue(new Callback<UploadImgBean>() {
            @Override
            public void onResponse(Call<UploadImgBean> call, Response<UploadImgBean> response) {
                Dimess();
                UploadImgBean baseBean = response.body();
                if(baseBean==null)
                    return;
                Message message = Message.obtain();
                if("1".equals(baseBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS_THREE;
                }else{
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = baseBean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<UploadImgBean> call, Throwable t) {
                Dimess();
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
            }
        });
    }
    private void initData() {
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        if(StringUtil.isEmpty(userid))
            userid = GetUserId();
        mApiCallUtil = ApiCallUtil.getInstant(this);
        //获取个人基本信息
        GetUserInfo();
    }
    private String GetUserId() {
        LoginBeanSave loginBeanSave  = (LoginBeanSave) SharedPreferencesUtil.get(this,"login","login");
        return loginBeanSave.getUserId();
    }
    private void GetUserInfo() {
        ApiHttpCilent.CreateLoading(this);
        mApiCallUtil.GetUserInfo(userid).enqueue(new Callback<UserInfoBean>() {
            @Override
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                Dimess();
                UserInfoBean baseBean = response.body();
                if(baseBean==null)
                    return;
                Message message = Message.obtain();
                if("1".equals(baseBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS;
                    message.obj = baseBean.getResult();
                }else{
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = baseBean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<UserInfoBean> call, Throwable t) {
                Dimess();
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
            }
        });
    }

    private void Dimess() {
       UserInfoActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if(ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing())
                    ApiHttpCilent.loading.dismiss();
            }
        });
    }
    private void UpdateView() {
        areaId = resultBean.getData().getRegionId();
        nickNamese = resultBean.getData().getNickName();
        sexNamese = resultBean.getData().getSex();
        wechatNamese = resultBean.getData().getWeixinNumber();
        phoneNamese = resultBean.getData().getMobile();
        areaNamese = resultBean.getData().getAddress();
        listWx =  WeiXinInfoBean.QueryAll();
        String sexText = "" ;
            if(0 == sexNamese){
                sexText = "男";
            } else if (1 == sexNamese) {
                sexText = "女";
            }
//            else {
//
//                if(listWx != null && listWx.size()>0){
//                    int dbSex = listWx.get(0).getSex();
//                    if(dbSex == 1)
//                        sexText = "男";
//                        else
//                        sexText = "女";
//                }
//            }
            if(!StringUtil.isEmpty(nickNamese))
                tv_nick.setText(nickNamese);
            else
                tv_nick.setHint("请输入");

//        if(listWx != null && listWx.size()>0){
//            String  dbNickName = listWx.get(0).getNickname();
//            if(!StringUtil.isEmpty(dbNickName))
//                tv_nick.setText(dbNickName);
//        }

            if(!StringUtil.isEmpty(wechatNamese))
                tv_wechat.setText(wechatNamese.trim());
            else
                tv_wechat.setHint("请输入");

            if(!StringUtil.isEmpty(phoneNamese))
                tv_phone.setText(phoneNamese.trim());
            else
                tv_phone.setHint("请输入");

            if(!StringUtil.isEmpty(areaNamese))
                tv_area.setText(areaNamese.trim());
            else
                tv_area.setHint("请选择");

            if(sexNamese!=0 && sexNamese!=1) {
                tv_sex.setText("");
                tv_sex.setHint("请选择");
            }else{
                tv_sex.setText(sexText);
            }

    }
    private void initView() {

        tv_title_left = (TextView) findViewById(R.id.base_activity_title_left);
        nick_relative = (RelativeLayout) findViewById(R.id.nick_relative);
        sex_relative = (LinearLayout) findViewById(R.id.sex_relative);
        wechat_relative = (RelativeLayout) findViewById(R.id.wechat_relative);
        phone_relative = (RelativeLayout) findViewById(R.id.phone_relative);
        area_relative = (RelativeLayout) findViewById(R.id.area_relative);

        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_wechat = (TextView) findViewById(R.id.tv_wechat);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_nick = (TextView) findViewById(R.id.tv_nick);
        ResetTitle("");
        tv_title_left.setText("个人信息");
        tv_title_left.setVisibility(View.VISIBLE);
        linear_back = (LinearLayout) findViewById(R.id.linear_back);
        linear_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nick_relative.setOnClickListener(this);
        sex_relative.setOnClickListener(this);
        phone_relative.setOnClickListener(this);
        area_relative.setOnClickListener(this);
        wechat_relative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        index = -1;
        switch (v.getId()){
            case R.id.nick_relative:
                nickName = tv_nick.getText().toString().trim();
                titleName = "昵称";
                index = 0;
                intent.putExtra("name", nickName);
                GetToUpdate(intent);
                break;
            case R.id.sex_relative:
                sexName = tv_sex.getText().toString().trim();
                titleName = "性别";
                index = 1;
                intent.putExtra("name", sexName);
                GetToUpdate(intent);
                break;
            case R.id.phone_relative:
                phoneName = tv_phone.getText().toString().trim();
                titleName = "手机号";
                index = 2;
                intent.putExtra("name", phoneName);
                GetToUpdate(intent);
                break;
            case R.id.area_relative:
                GetBaseAddress();
                return;
            case R.id.wechat_relative:
                weichatName = tv_wechat.getText().toString().trim();
                titleName = "微信号";
                index = 4;
                intent.putExtra("name", weichatName);
                GetToUpdate(intent);
                break;
        }
    }

    private void GetToUpdate(Intent intent) {
        intent.putExtra("title", titleName);
        intent.putExtra("index", index);
        intent.putExtra("type", type);
        intent.putExtra("regionId", areaId);
        intent.setClass(this,UpdateUserInfoActivity.class);
        StartActivityUtil.startActivityForResult(this,intent, ConstantsUtil.RESULT_REQUEST);
    }

    //获取地址信息
    private void GetBaseAddress(){
        ApiHttpCilent.CreateLoading(this);
        mApiCallUtil.GetAddressDic().enqueue(new Callback<BaseAddressBean>() {
            @Override
            public void onResponse(Call<BaseAddressBean> call, Response<BaseAddressBean> response) {
                Dimess();
                baseAddressBean = response.body();
                Message message = Message.obtain();
                if("1".equals(baseAddressBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS_TWO;
                }else{
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = baseAddressBean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<BaseAddressBean> call, Throwable t) {
                Dimess();
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK == resultCode){
            if(StringUtil.isEmpty(userid))
                userid = GetUserId();
            GetUserInfo();
        }
    }
}
