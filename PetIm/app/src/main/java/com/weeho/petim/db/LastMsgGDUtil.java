package com.weeho.petim.db;

import java.util.List;

import com.weeho.petim.application.MyApplication;
import com.weeho.petim.modle.MsgHistoryBean;
import com.weeho.petim.modle.MsgHistoryBeanDao;

/**
 * Created by wangkui on 2017/5/22.
 *   操作数据库数据库数据工具
 */

public class LastMsgGDUtil {
    //插入消息
    public static void InsertLastMsg(MsgHistoryBean msgDao){
        MyApplication.getDaoInstant().getMsgHistoryBeanDao().insert(msgDao);
    }

    //更新消息
    public static void UpdateLastMsg(MsgHistoryBean msgDao){
        MyApplication.getDaoInstant().getMsgHistoryBeanDao().update(msgDao);
    }

    //查询全部消息
    public static List<MsgHistoryBean> QueryLastMsgAll(){
      return  MyApplication.getDaoInstant().getMsgHistoryBeanDao().loadAll();
    }

    //查询单个消息
    public static List<MsgHistoryBean> QueryLastMsgSingle(String type){
       return  MyApplication.getDaoInstant().getMsgHistoryBeanDao().queryBuilder().where(MsgHistoryBeanDao.Properties.Type.eq(type)).list();
    }
}
