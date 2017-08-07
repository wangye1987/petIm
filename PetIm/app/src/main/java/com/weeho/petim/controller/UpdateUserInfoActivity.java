package com.weeho.petim.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.weeho.petim.lib.utils.SharedPreferencesUtil;
import com.weeho.petim.lib.utils.StringUtil;
import com.weeho.petim.lib.utils.WeakHandler;
import com.weeho.petim.lib.view.DeleteEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.weeho.petim.R;
import com.weeho.petim.RetorfitWapper.ApiCallUtil;
import com.weeho.petim.base.BaseActivity;
import com.weeho.petim.hxim.Constant;
import com.weeho.petim.modle.BaseAddressBean;
import com.weeho.petim.modle.BaseBean;
import com.weeho.petim.modle.LoginBeanSave;
import com.weeho.petim.modle.UploadImgBean;
import com.weeho.petim.modle.UserInfoBean;
import com.weeho.petim.network.ApiHttpCilent;
import com.weeho.petim.network.Utils;
import com.weeho.petim.util.ConstantsUtil;
import com.weeho.petim.util.ToastUtil;
import com.weeho.petim.view.ChangeAddressDialog;

/**
 * Created by wangkui on 2017/4/25.
 */

public class UpdateUserInfoActivity extends BaseActivity {

    private String name;
    private String title;
    private DeleteEditText editText;
    private LinearLayout linear_back;
    private int index ;
    String nickName;
    String ymtime;
    String sex;
    int sexId = -1;
    String mobile;
    String wechat;
    String isVallc;
    String provinceid;
    String regionId;
    private RadioGroup radio;
    private RadioButton man;
    private RadioButton female;
    private String userid;
    private MyHandler handler = new MyHandler(this);
    private Activity mActivity;
    private TextView tv_area;
    private ChangeAddressDialog mChangeAddressDialog;
    private BaseAddressBean baseAddressBean;
    private RelativeLayout re_area;
    private int type;
    private ApiCallUtil mApiCallUtil;

    @Override
    protected void onCreate() {
        setBaseContentView(R.layout.udapte_info);
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
        return "保存";
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

    private String userId;
    public  class MyHandler extends WeakHandler<UpdateUserInfoActivity> {



        public MyHandler(UpdateUserInfoActivity reference) {
            super(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantsUtil.CONTENT_SUCCESS:
                    // 成功
                    ToastUtil.showToast(getReference(),"修改成功");
                    setResult(RESULT_OK);
                    finish();
                    break;
//                case ConstantsUtil.CONTENT_SUCCESS_TWO:
//                    //获取基础信息地址信息正确
//                    if (mChangeAddressDialog == null) {
//                        mChangeAddressDialog = new ChangeAddressDialog(UpdateUserInfoActivity.this,baseAddressBean);
//                    }
////                    mChangeAddressDialog.setAddress("北京市", "丰台区");
//                    mChangeAddressDialog.show();
//                    mChangeAddressDialog.setAddresskListener(new ChangeAddressDialog.OnAddressCListener() {
//                        @Override
//                        public void onClick(String province, String city) {
//                            String text = "";
//                            if(city.contains("-")){
//                                text = city.split("-")[0];
//                            }else{
//                                text = city;
//                            }
//                            if(city.contains("-")) {
//                                regionId = city.split("-")[1];
//                            }else{
//                                List<BaseAddressBean.ResultBean.DataBean> listAll =   baseAddressBean.getResult().getData();
//                                if(!StringUtil.isEmpty(city)){
//                                for(BaseAddressBean.ResultBean.DataBean databean :listAll){
//                                    if(databean.getName().equals(province)){
//                                       List<BaseAddressBean.ResultBean.DataBean.CityBean> listCity = databean.getCity();
//                                        for(BaseAddressBean.ResultBean.DataBean.CityBean cityBean:listCity){
//                                            if(cityBean.getName().equals(city)){
//                                                regionId = cityBean.getId();
//                                                break;
//                                            }
//                                        }
//                                    }
//                                }
//                            }}
//                            tv_area.setText(province+text);
//                        }
//                    });
//                    break;
                case ConstantsUtil.CONTENT_FAIL:
                    // 成功
                    ToastUtil.showToast(getReference(),"操作失败,请重试");
                    break;
            }
        }
    }
    private void initData() {
        mApiCallUtil = ApiCallUtil.getInstant(this);
        mActivity = UpdateUserInfoActivity.this;

        userid = GetUserId();
       Intent intent =  getIntent();
        //获取传递过来的宠物信息或者个人信息
        if(intent!=null){
            name = intent.getStringExtra("name");
            title = intent.getStringExtra("title");
            regionId = intent.getStringExtra("regionId");
            index = intent.getIntExtra("index",-1);
            type = intent.getIntExtra("type",-1);
            ResetTitle(StringUtil.isEmpty(title)?"":title);
            editText.setText(StringUtil.isEmpty(name)?"":name);
            if(Constant.ONE == type){
            if(Constant.ONE == index){
                radio.setVisibility(View.VISIBLE);
                editText.setVisibility(View.GONE);
                if(!StringUtil.isEmpty(name)){
                if("女".equals(name)){
                    female.setChecked(true);
                 }else {
                    man.setChecked(true);
                } }
            }
            if(Constant.TWO == index){
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});//最大输入长度 
            }
            if(Constant.THREE == index){
                editText.setVisibility(View.GONE);
                re_area.setVisibility(View.VISIBLE);
                if(!StringUtil.isEmpty(name)) {
                    tv_area.setText(name);
                }else{
                    tv_area.setHint("请选择");
                }
             }
            if(Constant.FOUR == index){
                editText.setText(StringUtil.isEmpty(name)?"":name);
             }
            }else{
                //修改宠物信息
                if(Constant.ELEVEN == index){
                    radio.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.GONE);
                    female.setText("雌性");
                    man.setText("雄性");
                    if(!StringUtil.isEmpty(name)){
                        if("雌性".equals(name)){
                            female.setChecked(true);
                        }else {
                            man.setChecked(true);
                        }
                    }
                }
            }
        }
    }

    private String GetUserId() {
        LoginBeanSave loginBeanSave  = (LoginBeanSave) SharedPreferencesUtil.get(this,"login","login");
        return loginBeanSave.getUserId();
    }

    private void initView() {
        tv_area = (TextView) findViewById(R.id.tv_area);
        editText = (DeleteEditText) findViewById(R.id.editText);
        linear_back = (LinearLayout) findViewById(R.id.linear_back);
        re_area = (RelativeLayout) findViewById(R.id.re_area);
        radio = (RadioGroup) findViewById(R.id.radio);
        man = (RadioButton) findViewById(R.id.man);
        female = (RadioButton) findViewById(R.id.female);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.man){
                    sexId = Constant.ZERRO;
                }else{
                    sexId = Constant.ONE;
                }
            }
        });
        tv_area.setOnClickListener(this);
        tv_left_title.setVisibility(View.VISIBLE);
        tv_left_title.setText("返回");
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.base_activity_title_right_righttv:
                if(Constant.ONE == type) {
                    UpdateUserInfo();
                }else{
                    UpdatePetInfo();
                }
                break;
        }
    }

    //修改宠物信息
    private void UpdatePetInfo() {
        switch (index){
            case 10:
                nickName  = editText.getText().toString().trim();
                if(StringUtil.isEmpty(nickName)){
                    ToastUtil.showToast(mActivity,"请您填写宠物名称");
                    return;
                }
                break;
            case Constant.THREITH :
                ymtime  = editText.getText().toString().trim();
                if(StringUtil.isEmpty(ymtime)){
                    ToastUtil.showToast(mActivity,"请您填写初次疫苗时间");
                    return;
                }
                break;
        }
        if(StringUtil.isEmpty(userid))
            userid = GetUserId();
        RefitUpdatePetInfo();
    }
    //修改宠物信息
    private void RefitUpdatePetInfo() {
        ApiHttpCilent.CreateLoading(this);
        Map<String,Object> map = new HashMap<>();
        map.clear();
        if(!StringUtil.isEmpty(nickName))
        map.put("petName",nickName);
        if(sexId!=-1)
         map.put("petSex",sexId);
        if(!StringUtil.isEmpty(userid))
            map.put("userid",userid);
        if(mApiCallUtil== null)
            mApiCallUtil = ApiCallUtil.getInstant(mActivity);
        mApiCallUtil.UptadePetInfo(map).enqueue(new Callback<UploadImgBean>() {
            @Override
            public void onResponse(Call<UploadImgBean> call, Response<UploadImgBean> response) {
                Dimess();
                UploadImgBean baseBean = response.body();
                if(baseBean==null)
                    return;
                Message message = Message.obtain();
                if("1".equals(baseBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS;
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
    private void UpdateUserInfo() {
        switch (index){
            case Constant.ZERRO:
                nickName  = editText.getText().toString().trim();
                if(StringUtil.isEmpty(nickName)){
                    ToastUtil.showToast(mActivity,"请您填写昵称");
                    return;
                }
                break;
            case Constant.TWO:
                mobile = editText.getText().toString().trim();
                if(StringUtil.isEmpty(mobile)){
                    ToastUtil.showToast(mActivity,"请您填写手机号");
                    return;
                }
            if(!Utils.isMobile(mobile)){
                ToastUtil.showToast(mActivity,"请您填写正确的手机号");
                return;
                }
                break;
            case Constant.FOUR:
                wechat = editText.getText().toString().trim();
                if(StringUtil.isEmpty(wechat)){
                    ToastUtil.showToast(mActivity,"请您填写微信号");
                    return;
                }
                break;
        }
        if(StringUtil.isEmpty(userid))
            userid = GetUserId();
        RefitUpdateUerInfo();
    }


    //修改个人信息
    private void RefitUpdateUerInfo() {
        ApiHttpCilent.CreateLoading(this);
        HashMap<String,Object> map = new HashMap<>();
        if(!StringUtil.isEmpty(nickName))
            map.put("nickName",nickName);
        if(sexId != -1)
            map.put("sex",sexId);
        if(!StringUtil.isEmpty(mobile))
            map.put("mobile",mobile);
        if(!StringUtil.isEmpty(wechat))
            map.put("weixinNumber",wechat);
        if(!StringUtil.isEmpty(userid))
            map.put("userid",userid);
        if(!StringUtil.isEmpty(regionId))
            map.put("regionId",regionId);
        mApiCallUtil.UptadeUserInfo(map).enqueue(new Callback<UploadImgBean>() {
            @Override
            public void onResponse(Call<UploadImgBean> call, Response<UploadImgBean> response) {
                Dimess();
                UploadImgBean baseBean = response.body();
                if(baseBean==null)
                    return;
                Message message = Message.obtain();
                if("1".equals(baseBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS;
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

    //获取地址信息
//    private void GetBaseAddress(){
//         mApiCallUtil.GetAddressDic().enqueue(new Callback<BaseAddressBean>() {
//             @Override
//             public void onResponse(Call<BaseAddressBean> call, Response<BaseAddressBean> response) {
//                 Dimess();
//                 baseAddressBean = response.body();
//                 Message message = Message.obtain();
//                 if("1".equals(baseAddressBean.getStatus())) {
//                     message.what = ConstantsUtil.CONTENT_SUCCESS_TWO;
//                 }else{
//                     message.what = ConstantsUtil.CONTENT_FAIL;// 错误
//                     message.obj = baseAddressBean.getError().getInfo();
//                 }
//                 handler.sendMessage(message);
//             }
//
//             @Override
//             public void onFailure(Call<BaseAddressBean> call, Throwable t) {
//                 Dimess();
//                 Message message = Message.obtain();
//                 message.what = ConstantsUtil.CONTENT_FAIL;// 错误
//                 message.obj = "数据错误,请稍后重试";
//                 handler.sendMessage(message);
//             }
//         });
//    }
    private void Dimess() {
        UpdateUserInfoActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if(ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing())
                    ApiHttpCilent.loading.dismiss();
            }
        });
    }

}
