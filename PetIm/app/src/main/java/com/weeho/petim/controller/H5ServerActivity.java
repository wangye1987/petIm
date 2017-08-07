package com.weeho.petim.controller;

import android.content.Context;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.weeho.petim.BuildConfig;
import com.weeho.petim.R;
import com.weeho.petim.base.BaseActivity;

import java.lang.reflect.Field;

//import android.support.annotation.RequiresApi;

/**
 * Created by wangkui on 2017/5/9.
 *
 *服务端H5
 */

public class H5ServerActivity extends BaseActivity{

    private WebSettings ws;
    private WebView webView;

    @Override
    protected void onCreate() {
       setBaseContentView(R.layout.h5server);
        initView();
//        webView.loadUrl("http://www.w3school.com.cn/jquery/index.asp");
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.server_h5);
        ws = webView.getSettings();
        ws.setLoadsImagesAutomatically(true);
        ws.setDefaultTextEncodingName("UTF-8");
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);// 解决缓存问题
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        ws.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        ws.setTextZoom(100);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        ws.setDisplayZoomControls(false);
        ws.setJavaScriptEnabled(true); // 设置支持javascript脚本
        ws.setAllowFileAccess(true); // 允许访问文件
        ws.setBuiltInZoomControls(true); // 设置显示缩放按钮
        ws.setSupportZoom(true); // 支持缩放
        ws.setSavePassword(false);
        webView.requestFocusFromTouch();


        //启用数据库
        ws.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();

        //启用地理定位
        ws.setGeolocationEnabled(true);
        //设置定位的数据库路径
        ws.setGeolocationDatabasePath(dir);

        //设置localStorage
        ws.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        ws.setAppCachePath(appCachePath);
        ws.setAllowFileAccess(true);
        ws.setAppCacheEnabled(true);
        ws.setDomStorageEnabled(true);

        webView.setWebChromeClient(new WebChromeClient(){
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
////                view.loadUrl(request.getUrl().getPath());
//                return true;
//            }
        });

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
        return "客服端";
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
    @Override
    protected void onStop() {
        super.onStop();
        webView.getSettings().setJavaScriptEnabled(false);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解决H5界面加载过多 内存溢出
        releaseAllWebViewCallback();
    }

    public void releaseAllWebViewCallback() {
        if (android.os.Build.VERSION.SDK_INT < 16) {
            try {
                Field field = WebView.class.getDeclaredField("mWebViewCore");
                field = field.getType().getDeclaredField("mBrowserFrame");
                field = field.getType().getDeclaredField("sConfigCallback");
                field.setAccessible(true);
                field.set(null, null);
            } catch (NoSuchFieldException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                Field sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
                if (sConfigCallback != null) {
                    sConfigCallback.setAccessible(true);
                    sConfigCallback.set(null, null);
                }
            } catch (NoSuchFieldException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
    }
}
