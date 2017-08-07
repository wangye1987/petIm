package com.weeho.petim.modle;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangkui on 2017/5/22.
 */
@Entity
public class LoginUserBean {
    @Id(autoincrement = true)
    private long id;
    @Unique
    private String userName;

    private String password;

    @Generated(hash = 1805317737)
    public LoginUserBean(long id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    @Generated(hash = 1232875992)
    public LoginUserBean() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
