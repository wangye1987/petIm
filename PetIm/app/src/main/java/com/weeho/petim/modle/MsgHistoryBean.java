package com.weeho.petim.modle;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangkui on 2017/5/22.
 * 数据库实体类 映射为数据库表名 属性为数据库字段
 */
@Entity
public class MsgHistoryBean {
    @Id(autoincrement = true)
    private Long id;
    //聊天客服类型 0-4
    @Unique
    private String type;
    //最后一条消息
    private String lastMsg;

    //最后一条消息时间
    private String lastMsgTime;

    @Generated(hash = 98073620)
    public MsgHistoryBean(Long id, String type, String lastMsg,
            String lastMsgTime) {
        this.id = id;
        this.type = type;
        this.lastMsg = lastMsg;
        this.lastMsgTime = lastMsgTime;
    }

    @Generated(hash = 624882983)
    public MsgHistoryBean() {
    }

    public Long getId() {
        return this.id;
    }


    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastMsg() {
        return this.lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getLastMsgTime() {
        return this.lastMsgTime;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
