package com.weeho.petim.db;

import com.weeho.petim.application.MyApplication;
import com.weeho.petim.modle.MsgHistoryBean;
import com.weeho.petim.modle.MsgHistoryBeanDao;
import com.weeho.petim.modle.WxInfoDb;
import com.weeho.petim.modle.WxInfoDbDao;

import java.util.List;

/**
 * Created by wangkui on 2017/5/24.
 */

public class WeiXinInfoBean {
    //插入消息
    public static void InsertMsg(WxInfoDb msgDao){
        MyApplication.getDaoInstant().getWxInfoDbDao().insert(msgDao);
    }

    //更新消息
    public static void UpdateMsg(WxInfoDb msgDao){
        MyApplication.getDaoInstant().getWxInfoDbDao().update(msgDao);
    }

    //查询全部消息
    public static List<WxInfoDb> QueryAll(){
        return  MyApplication.getDaoInstant().getWxInfoDbDao().loadAll();
    }

    //查询单个消息
    public static List<WxInfoDb> QuerySingle(String uniqne){
        return  MyApplication.getDaoInstant().getWxInfoDbDao().queryBuilder().where(WxInfoDbDao.Properties.Unionid.eq(uniqne)).list();
    }
}
