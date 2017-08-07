package com.weeho.petim.modle;

import android.graphics.drawable.Drawable;

/**
 * Created by wangkui on 2017/5/16.
 */

public class MyAppInfo {
    private Drawable image;
    private String appName;

    public MyAppInfo(Drawable image, String appName) {
        this.image = image;
        this.appName = appName;
    }
    public MyAppInfo() {

    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
