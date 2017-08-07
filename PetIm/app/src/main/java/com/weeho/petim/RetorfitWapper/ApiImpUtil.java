package com.weeho.petim.RetorfitWapper;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
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
import com.weeho.petim.network.UrlsUtil;

/**
 * Created by wangkui on 2017/5/17.
 * Api 接口工具
 */

public interface ApiImpUtil {
     // 登录鉴权验证码
     @GET(UrlsUtil.login)
     Call<BaseBean> Login(@QueryMap HashMap<String,String> map);

     // 获取版本号
     @GET(UrlsUtil.VersionData)
     Call<VersionInitdatabean> AppVersion(@Query("type")String type);

     // 上传宠物图像
     @Multipart
     @POST(UrlsUtil.UpdatePetImg)
     Call<UploadImgBean> UploadPetImage(@Part MultipartBody.Part file,@Query("userid") String userid);

     // 获取客服列表
     @GET(UrlsUtil.kfInfo)
     Call<HxBaseBean> GetHxServiceList(@Query("userid") String userid);

     // 获取客服列表
     @GET(UrlsUtil.kfimInfo)
     Call<HxImBaseBean> GetHxUserName(@Query("Type") String Type,@Query("userid") String userid);

     // 获取宠物成就信息
     @GET(UrlsUtil.AcheveInfo)
     Call<AcheveBaseBean> GetAchievement(@Query("userid") String userid);

     // 获取宠物信息
     @GET(UrlsUtil.petInfo)
     Call<PetBaseBean> GetPetInfo(@Query("userid") String userid);

     // 获取宠物种类数据信息
     @GET(UrlsUtil.VaritypetInfo)
     Call<PetVarietyBasebean> GetVarityInfo(@Query("userid") String userid);

     // 获取用户数据信息
     @GET(UrlsUtil.info)
     Call<UserInfoBean> GetUserInfo(@Query("userid") String userid);

     // 获取宠物数据字典
     @GET(UrlsUtil.BadhabitDicnfo)
     Call<BadHabitDicBean> GetPetHabitDic(@Query("userid") String userid);

     // 获取宠物恶习数据信息
     @GET(UrlsUtil.Badhabitnfo)
     Call<BadHabitBean> GetPetHabitInfo(@Query("userid") String userid);

     // 获取地区元数据
     @GET(UrlsUtil.addressInfo)
     Call<BaseAddressBean> GetAddressDic();

     // 修改个人信息
     @FormUrlEncoded
     @POST(UrlsUtil.updateInfo)
     Call<UploadImgBean> UptadeUserInfo(@FieldMap  Map<String, Object> param);

     // 修改宠物恶习信息
     @GET(UrlsUtil.BadhabitUpdatenfo)
     Call<BaseBean> UptadeHabitInfo(@Query("type") String type,@Query("viceid") String viceid,@Query("userid") String userid);

     // 修改宠物信息
     @FormUrlEncoded
     @POST(UrlsUtil.UpdatepetInfo)
     Call<UploadImgBean> UptadePetInfo(@FieldMap  Map<String, Object> param);


     // 修改作息信息
     @FormUrlEncoded
     @POST(UrlsUtil.UpdatepetTimeInfo)
     Call<BaseBean> UpdatePetAlarmInfo(@FieldMap  Map<String, Object> param);

     // 获取宠物作息数据信息
     @GET(UrlsUtil.alarm_time)
     Call<AlarmBaseBean> GetPetAlarmInfo(@Query("userid") String userid);

     // 获取宠物段位数据信息
     @GET(UrlsUtil.pet_dan)
     Call<PetDanBaseBean> GetPetDan(@Query("userid") String userid);
}
