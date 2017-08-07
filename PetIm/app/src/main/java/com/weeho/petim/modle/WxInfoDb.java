package com.weeho.petim.modle;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangkui on 2017/5/24.
 */
@Entity
public class WxInfoDb {
    @Id(autoincrement = true)
    private Long id;
    //聊天客服类型 0-4
    @Unique
    private String unionid;

    private int sex;

    private String nickname;

    private String city;

    private String province;

    private String country;

    private String headimgurl;
//    openid : oF24Cw7ktbNg-oMA2ljRNENkGaFw
//     * nickname : 王小二
//     * sex : 1
//     * language : zh_CN
//     * city : Chaoyang
//     * province : Beijing
//     * country : CN
//     * headimgurl : http://wx.qlogo.cn/mmopen/PiajxSqBRaEJLx7ib1AUydGflh2ib9Ds9kKDznYU5jQmM9P6xic5k05geob0ygSzjwic8VBgSqCHPHhqkzg4ZSibXWlw/0
//            * privilege : []
//            * unionid : oMXux0TR1B4iFkV5QEhUsbYTpDb4

    @Generated(hash = 992304901)
    public WxInfoDb(Long id, String unionid, int sex, String nickname, String city, String province, String country, String headimgurl) {
        this.id = id;
        this.unionid = unionid;
        this.sex = sex;
        this.nickname = nickname;
        this.city = city;
        this.province = province;
        this.country = country;
        this.headimgurl = headimgurl;
    }

    @Generated(hash = 1527048412)
    public WxInfoDb() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnionid() {
        return this.unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return this.headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }
}
