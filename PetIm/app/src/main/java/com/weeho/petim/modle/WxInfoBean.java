package com.weeho.petim.modle;

import java.util.List;

/**
 * Created by wangkui on 2017/5/24.
 */

public class WxInfoBean {

    /**
     * openid : oF24Cw7ktbNg-oMA2ljRNENkGaFw
     * nickname : 王小二
     * sex : 1
     * language : zh_CN
     * city : Chaoyang
     * province : Beijing
     * country : CN
     * headimgurl : http://wx.qlogo.cn/mmopen/PiajxSqBRaEJLx7ib1AUydGflh2ib9Ds9kKDznYU5jQmM9P6xic5k05geob0ygSzjwic8VBgSqCHPHhqkzg4ZSibXWlw/0
     * privilege : []
     * unionid : oMXux0TR1B4iFkV5QEhUsbYTpDb4
     */

    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<?> privilege;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<?> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<?> privilege) {
        this.privilege = privilege;
    }
}
