package com.weeho.petim.controller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.HttpGet;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.weeho.petim.db.WeiXinInfoBean;
import com.weeho.petim.fragment.AchievementFragment;
import com.weeho.petim.lib.activityManager.StartActivityUtil;
import com.weeho.petim.lib.utils.SharedPreferencesUtil;
import com.weeho.petim.lib.utils.StringUtil;
import com.weeho.petim.lib.utils.WeakHandler;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.weeho.petim.R;
import com.weeho.petim.RetorfitWapper.ApiCallUtil;
import com.weeho.petim.application.MyApplication;
import com.weeho.petim.base.BaseActivity;
import com.weeho.petim.modle.BaseBean;
import com.weeho.petim.modle.LoginBeanSave;
import com.weeho.petim.modle.WxInfoBean;
import com.weeho.petim.modle.WxInfoDb;
import com.weeho.petim.network.ApiHttpCilent;
import com.weeho.petim.network.Utils;
import com.weeho.petim.util.ConstantsUtil;
import com.weeho.petim.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangkui on 2017/4/25.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button bt_login;
    private EditText et_code;
    private BaseBean baseBean;
    //是否退出登录
    private boolean loginout;
    private ApiCallUtil mLoginFamuse;
    private WxReceiver mWxReceiver;
    private MyHandler handler = new MyHandler(this);
    private Long expires_in;
    private String accessToken;
    private String openID;
    private String refreshToken;
    private String code_input;
    private boolean isCheck;

    public  class MyHandler extends WeakHandler<LoginActivity> {

        private String userId;
        private String hxUserName;
        private String hxpassword;

        public MyHandler(LoginActivity reference) {
            super(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantsUtil.CONTENT_SUCCESS:
                    // 成功
                    if(baseBean != null && baseBean.getResult()!=null){
                        BaseBean.ResultBean resultBean = baseBean.getResult();
                        userId =  resultBean.getUserId();
                        hxUserName = resultBean.getHxusername();
                        hxpassword = resultBean.getHxpassword();
                        //存储宠物图像 聊天需要用到
                        SharedPreferencesUtil.writeSharedPreferencesString(getReference(),"avatar","avatar",resultBean.getHeadSculpturePath());
                        //是否验证过
                        SharedPreferencesUtil.writeSharedPreferencesBoolean(getReference(),"isCheck","isCheck",true);
                        //userid
                        SharedPreferencesUtil.writeSharedPreferencesString(getReference(),"key","userid",userId);
                        LoginBeanSave loginBeanSave = new LoginBeanSave();
                        loginBeanSave.setHxpassword(hxpassword);
                        loginBeanSave.setHxUserName(hxUserName);
                        loginBeanSave.setUserId(userId);
                        //存储登录数据
                        SharedPreferencesUtil.save(getReference(),"login","login",loginBeanSave);
                        getReference().LoginMainActivity(userId,hxUserName,hxpassword);
                    }
                    break;
                case ConstantsUtil.CONTENT_FAIL:
                    // 成功
                    String msgError = (String) msg.obj;
                    if( baseBean != null  && baseBean.getError()!= null){
                        String code  = baseBean.getError().getCode();
                        if("102".equals(code)){
                            et_code.setVisibility(View.VISIBLE);
                            SharedPreferencesUtil.writeSharedPreferencesBoolean(getReference(),"isCheck","isCheck",false);
                        }
                    }
                    if(!StringUtil.isEmpty(msgError))
                        ToastUtil.showToast(getReference(),msgError);
                    else
                        ToastUtil.showToast(getReference(),"登录失败，请您稍后重试");
                    break;
            }
        }
    }
    //登录成功
    private void LoginMainActivity(String userid,String hxUserName,String hxPassword) {
        Intent intent = new Intent();
        intent.putExtra("userid",userid);
        intent.putExtra("hxUserName",hxUserName);
        intent.putExtra("hxPassword",hxPassword);
        intent.setClass(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        StartActivityUtil.startActivity(this,intent);
        finish();
    }
    @Override
    protected void onCreate() {
        setBaseContentView(R.layout.login);
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
        return "宠伴";
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
        Intent intent = getIntent();
        if(intent != null){
            loginout = intent.getBooleanExtra("loginout",false);
        }
        mWxReceiver = new WxReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantsUtil.AUTHON);
        registerReceiver(mWxReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemKeyBoard(this,et_code);
    }
    /**
     * 隐藏键盘
     * */
    public static void hideSystemKeyBoard(Context mContext,View v) {
        InputMethodManager imm = (InputMethodManager) (mContext)
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 初始化试图控件
     * */
    private void initView() {
        et_code = (EditText) findViewById(R.id.et_code);
        bt_login = (Button) findViewById(R.id.bt_login);
        TextView left = (TextView) findViewById(R.id.base_activity_title_left);
        TextView  tv_title = (TextView) findViewById(R.id.base_activity_title_titlename);
        LinearLayout linear_back = (LinearLayout) findViewById(R.id.linear_back);
        linear_back.setVisibility(View.INVISIBLE);
        left.setVisibility(View.INVISIBLE);
        tv_title.setText("宠伴");
        bt_login.setOnClickListener(this);
        mLoginFamuse = ApiCallUtil.getInstant(this);
        //是否验证过
        isCheck = SharedPreferencesUtil.getSharedPreferencesBoolean(this,"isCheck","isCheck",false);
        if(!isCheck)
            et_code.setVisibility(View.VISIBLE);
    }

    /**
     * 点击事件
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
//                if(!Utils.isNetworkConnected(this))
//                    return;
//                if(!isCheck){
//                code_input = et_code.getText().toString().trim();
//                if(StringUtil.isEmpty(code_input)) {
//                   ToastUtil.showToast(this,"请输入授权码");
//                    return;
//                 }
//                }
//                //发送验证码验证
//                SharedPreferencesUtil.writeSharedPreferencesString(this,"code","code",code_input);
//                ApiHttpCilent.CreateLoading(this);
//                wxLogin();
                //我的微信
                  LoginByWxCode(et_code.getText().toString().trim());
//                  LoginByWxCode("oMXux0W-ZwXf5szg71LtIU6xBDUI");
            break;
        }
    }

    public class WxReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!= null ){
                ApiHttpCilent.CreateLoading((Activity) context);
                final String code = intent.getStringExtra("code");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getAccessToken(code);
                    }
                }).start();

            }
        }
    }

    private void LoginByWxCode(String mUnicode){
        if(mLoginFamuse == null)
            mLoginFamuse = ApiCallUtil.getInstant(this);
        HashMap<String,String> map = new HashMap<>();
        if(!StringUtil.isEmpty(code_input))
            map.put("validationInfo",code_input);
        if(!StringUtil.isEmpty(mUnicode))
            map.put("weiXinNumber",mUnicode);

        mLoginFamuse.LoginPet(map).enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                baseBean = response.body();
                if(baseBean ==null)
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
    private void StartServer(){
        StartActivityUtil.startActivity(this,H5ServerActivity.class);
    }
    public void wxLogin() {
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            ToastUtil.showToast(this,"您还未安装微信客户端");
            Dimess();
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "petim_wx_login";
        MyApplication.mWxApi.sendReq(req);

    }
    private boolean isAccessTokenIsInvalid() {
        String url = "https://api.weixin.qq.com/sns/auth?access_token=" + accessToken + "&openid=" + openID;
        URI uri = URI.create(url);
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(uri);
        HttpResponse response;
        try {
            response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();

                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                StringBuilder sb = new StringBuilder();

                for (String temp = reader.readLine(); temp != null; temp = reader.readLine()) {
                    sb.append(temp);
                }
                JSONObject object = new JSONObject(sb.toString().trim());
                int errorCode = object.getInt("errcode");
                if (errorCode == 0) {
                    return true;
                }
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    private String getAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ConstantsUtil.APP_ID+"&secret="+ConstantsUtil.SECRET+"&code="+code+"&grant_type=authorization_code";
        URI uri = URI.create(url);
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(uri);
        HttpResponse response;
        try {
            response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();

                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                StringBuilder sb = new StringBuilder();

                for (String temp = reader.readLine(); temp != null; temp = reader.readLine()) {
                    sb.append(temp);
                }

                JSONObject object = new JSONObject(sb.toString().trim());
                accessToken = object.getString("access_token");
                openID = object.getString("openid");
                refreshToken = object.getString("refresh_token");
                expires_in = object.getLong("expires_in");
                getUserInfo();
                return accessToken;
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private void SetData(WxInfoBean bean, WxInfoDb mWxInfoDb) {
        mWxInfoDb.setCity(bean.getCity());
        mWxInfoDb.setCountry(bean.getCountry());
        mWxInfoDb.setHeadimgurl(bean.getHeadimgurl());
        mWxInfoDb.setNickname(bean.getNickname());
        mWxInfoDb.setSex(bean.getSex());
        mWxInfoDb.setProvince(bean.getProvince());
    }
    //刷新token
    private void refreshAccessToken() {
        String uri = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + ConstantsUtil.APP_ID + "&grant_type=refresh_token&refresh_token="
                + refreshToken;
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(URI.create(uri));
        try {
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for (String temp = reader.readLine(); temp != null; temp = reader.readLine()) {
                    builder.append(temp);
                }
                JSONObject object = new JSONObject(builder.toString().trim());
                accessToken = object.getString("access_token");
                refreshToken = object.getString("refresh_token");
                openID = object.getString("openid");
                expires_in = object.getLong("expires_in");
                expires_in = expires_in+System.currentTimeMillis();
                if(!StringUtil.isEmpty(accessToken) && !StringUtil.isEmpty(openID))
                    getUserInfo();
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void getUserInfo() {
        if (isAccessTokenIsInvalid() && System.currentTimeMillis() < expires_in) {
            String uri = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openID;
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(URI.create(uri));
            try {
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    for (String temp = reader.readLine(); temp != null; temp = reader.readLine()) {
                        builder.append(temp);
                    }
                    JSONObject object = new JSONObject(builder.toString().trim());
//                    String nikeName = object.getString("nickname");
                    Gson gson = new Gson();
                    WxInfoBean bean = gson.fromJson(object.toString(), WxInfoBean.class);
                    List<WxInfoDb> listHas = WeiXinInfoBean.QuerySingle(bean.getUnionid());
                    WxInfoDb mWxInfoDb = new WxInfoDb();
                    if(listHas.size() == 0){
                        // 么有记录
                        SetData(bean, mWxInfoDb);
                        mWxInfoDb.setUnionid(bean.getUnionid());
                        WeiXinInfoBean.InsertMsg(mWxInfoDb);
                    }else{
                        mWxInfoDb = listHas.get(0);
                        SetData(bean, mWxInfoDb);
                        WeiXinInfoBean.UpdateMsg(mWxInfoDb);
                    }
                    //调取登陆
                    LoginByWxCode(bean.getUnionid());
                }
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            //超时 重新刷新token
            refreshAccessToken();
        }
    }
    private void Dimess() {
        LoginActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if(ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing())
                    ApiHttpCilent.loading.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mWxReceiver);
    }
}
