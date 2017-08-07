package com.weeho.petim.RetorfitWapper;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import com.weeho.petim.application.MyApplication;
import com.weeho.petim.modle.AcheveBaseBean;
import com.weeho.petim.modle.AlarmBaseBean;
import com.weeho.petim.modle.BadHabitBean;
import com.weeho.petim.modle.BadHabitDicBean;
import com.weeho.petim.modle.BaseAddressBean;
import com.weeho.petim.modle.BaseBean;
import com.weeho.petim.modle.HxBaseBean;
import com.weeho.petim.modle.HxImBaseBean;
import com.weeho.petim.modle.PetBaseBean;
import com.weeho.petim.modle.PetDanBaseBean;
import com.weeho.petim.modle.PetVarietyBasebean;
import com.weeho.petim.modle.UploadImgBean;
import com.weeho.petim.modle.UserInfoBean;
import com.weeho.petim.modle.VersionInitdatabean;
import com.weeho.petim.network.RequestConfiguration;

/**
 * Created by wangkui on 2017/5/17.
 * 联网工具类
 */

public class ApiCallUtil {
    static ApiCallUtil mLoginFamuse;
    private final ApiImpUtil mApiImpUtil;

    ApiCallUtil(Context mContext){
        if (MyApplication.debugToggle) {
            /**
             *
             * 正式状态
             */
            mApiImpUtil = RetorfitWapper.getInstance(RequestConfiguration.BASE_URL_TEST).create(ApiImpUtil.class);
        } else {
            /**
             * 调试状态
             */
            mApiImpUtil = RetorfitWapper.getInstance(RequestConfiguration.BASE_URL_TEST_LACALHOST).create(ApiImpUtil.class);
        }
    }
    public static ApiCallUtil getInstant(Context mContext){
        if(mLoginFamuse == null){
            synchronized (ApiCallUtil.class){
                mLoginFamuse = new ApiCallUtil(mContext);
            }
        }
        return mLoginFamuse;
    }

    //调取登录接口
    public Call<BaseBean> LoginPet( HashMap<String,String> map){
        Call<BaseBean> callback =  mApiImpUtil.Login(map);
        return  callback;
    }

    //调取登录接口
    public Call<VersionInitdatabean> AppVersion(String type){
        Call<VersionInitdatabean> callback =  mApiImpUtil.AppVersion(type);
        return  callback;
    }

    //上传宠物图像接口
    public Call<UploadImgBean> UploadPetImage(MultipartBody.Part file,String userId){
        Call<UploadImgBean> callback =  mApiImpUtil.UploadPetImage(file,userId);
        return  callback;
    }

    //获取客服列表接口
    public Call<HxBaseBean> GetHxServiceList(String userId){
        Call<HxBaseBean> callback =  mApiImpUtil.GetHxServiceList(userId);
        return  callback;
    }

    //获取点击的环信聊天账号
    public Call<HxImBaseBean> GetHxUserName(String type,String userId){
        Call<HxImBaseBean> callback =  mApiImpUtil.GetHxUserName(type,userId);
        return  callback;
    }

    //获取宠物成就接口
    public Call<AcheveBaseBean> GetAchievement(String userId){
        Call<AcheveBaseBean> callback =  mApiImpUtil.GetAchievement(userId);
        return  callback;
    }

    //获取宠物信息接口
    public Call<PetBaseBean> GetPetInfo(String userId){
        Call<PetBaseBean> callback =  mApiImpUtil.GetPetInfo(userId);
        return  callback;
    }

    //获取宠物数据字典接口
    public Call<PetVarietyBasebean> GetVarityInfo(String userId){
        Call<PetVarietyBasebean> callback =  mApiImpUtil.GetVarityInfo(userId);
        return  callback;
    }

    //获取用户信息
    public Call<UserInfoBean> GetUserInfo(String userId){
        Call<UserInfoBean> callback =  mApiImpUtil.GetUserInfo(userId);
        return  callback;
    }

    //获取宠物恶习信息
    public Call<BadHabitBean> GetPetHabitInfo(String userId){
        Call<BadHabitBean> callback =  mApiImpUtil.GetPetHabitInfo(userId);
        return  callback;
    }

    //获取宠物恶习数据字典
    public Call<BadHabitDicBean> GetPetHabitDic(String userId){
        Call<BadHabitDicBean> callback =  mApiImpUtil.GetPetHabitDic(userId);
        return  callback;
    }

    //更新恶习信息
    public Call<BaseBean> UptadeHabitInfo(String type,String id,String userId){
        Call<BaseBean> callback =  mApiImpUtil.UptadeHabitInfo(type,id,userId);
        return  callback;
    }

    //更新用户信息
    public Call<UploadImgBean> UptadeUserInfo(Map<String,Object> map){
        Call<UploadImgBean> callback =  mApiImpUtil.UptadeUserInfo(map);
        return  callback;
    }

    //更新宠物信息
    public Call<UploadImgBean> UptadePetInfo(Map<String,Object> map){
        Call<UploadImgBean> callback =  mApiImpUtil.UptadePetInfo(map);
        return  callback;
    }

    //获取地区元数据
    public Call<BaseAddressBean> GetAddressDic(){
        Call<BaseAddressBean> callback =  mApiImpUtil.GetAddressDic();
        return  callback;
    }

    //获取宠物作息时间数据
    public Call<AlarmBaseBean> GetPetAlarmInfo(String userId){
        Call<AlarmBaseBean> callback =  mApiImpUtil.GetPetAlarmInfo(userId);
        return  callback;
    }

    //修改宠物作息时间数据
    public Call<BaseBean> UpdatePetAlarmInfo(Map<String,Object> map){
        Call<BaseBean> callback =  mApiImpUtil.UpdatePetAlarmInfo(map);
        return  callback;
    }

    //获取宠物成就段位接口
    public Call<PetDanBaseBean> GetPetDan(String userId){
        Call<PetDanBaseBean> callback =  mApiImpUtil.GetPetDan(userId);
        return  callback;
    }
}
