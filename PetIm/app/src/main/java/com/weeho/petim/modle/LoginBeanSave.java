package com.weeho.petim.modle;

import java.io.Serializable;

/**
 * Created by wangkui on 2017/5/9.
 * 存储登录信息
 */

public class LoginBeanSave implements Serializable {
    private String userId;
    private String hxUserName;
    private String hxpassword;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHxUserName() {
        return hxUserName;
    }

    public void setHxUserName(String hxUserName) {
        this.hxUserName = hxUserName;
    }

    public String getHxpassword() {
        return hxpassword;
    }

    public void setHxpassword(String hxpassword) {
        this.hxpassword = hxpassword;
    }
}
