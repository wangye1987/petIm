package com.weeho.petim.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.HttpGet;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

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
import com.weeho.petim.controller.LoginActivity;
import com.weeho.petim.controller.MainActivity;
import com.weeho.petim.db.LastMsgGDUtil;
import com.weeho.petim.db.WeiXinInfoBean;
import com.weeho.petim.lib.activityManager.StartActivityUtil;
import com.weeho.petim.lib.utils.SharedPreferencesUtil;
import com.weeho.petim.lib.utils.StringUtil;
import com.weeho.petim.lib.utils.WeakHandler;
import com.weeho.petim.modle.BaseBean;
import com.weeho.petim.modle.LoginBeanSave;
import com.weeho.petim.modle.MsgHistoryBean;
import com.weeho.petim.modle.WxInfoBean;
import com.weeho.petim.modle.WxInfoDb;
import com.weeho.petim.modle.WxInfoDbDao;
import com.weeho.petim.network.ApiHttpCilent;
import com.weeho.petim.util.ConstantsUtil;
import com.weeho.petim.util.ToastUtil;

/**
 * Created by wangkui on 2017/5/18.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    private Button gotoBtn, regBtn, launchBtn, checkBtn, payBtn, favButton;
    // IWXAPI 是第三方app和微信通信的openapi接口
//    private IWXAPI api;
    private String code;
    private String accessToken;
    private String openID;
    private String refreshToken;
    private Long expires_in;
    private String mAccessToken;
    private BaseBean baseBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        MyApplication.mWxApi.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        Toast.makeText(this, "openid = " + req.openId, Toast.LENGTH_SHORT).show();

        switch (req.getType()) {

            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
                break;
            default:
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
//        Toast.makeText(this, "openid = " + resp.openId, Toast.LENGTH_SHORT).show();

        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
//            Toast.makeText(this, "code = " + ((SendAuth.Resp) resp).code, Toast.LENGTH_SHORT).show();
        }

        int result = 0;

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                result = R.string.errcode_success;
                //获取授权的code
                code = ((SendAuth.Resp) resp).code;
                Log.d("code",code+"授权");
                Dimess();
                LoginByWxCode(code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                result = R.string.errcode_cancel;
//                Log.d("code",result+"取消");
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝
                result = R.string.errcode_deny;
                Log.d("code",result+"拒绝");
                finish();
                break;
            default:
                result = R.string.errcode_unknown;
                finish();
                break;
        }

//        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }


    private void LoginByWxCode(String code) {
        Intent intent = new Intent();
        intent.setAction(ConstantsUtil.AUTHON);
        intent.putExtra("code",code);
        sendBroadcast(intent);
        finish();
        overridePendingTransition(com.weeho.petim.lib.R.anim.activity_in_from_right,
                com.weeho.petim.lib.R.anim.activity_out_to_left);
    }

    private void Dimess() {
        WXEntryActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if(ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing())
                    ApiHttpCilent.loading.dismiss();
            }
        });
    }

}
