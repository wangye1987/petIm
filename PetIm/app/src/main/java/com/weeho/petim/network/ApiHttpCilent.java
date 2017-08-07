package com.weeho.petim.network;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weeho.petim.lib.utils.NetWorkState;
import com.weeho.petim.lib.utils.StringUtil;

import java.io.File;

import com.weeho.petim.R;
import com.weeho.petim.modle.AcheveBaseBean;
import com.weeho.petim.modle.BadHabitBean;
import com.weeho.petim.modle.BadHabitDicBean;
import com.weeho.petim.modle.BaseAddressBean;
import com.weeho.petim.modle.BaseBean;
import com.weeho.petim.modle.HxBaseBean;
import com.weeho.petim.modle.HxImBaseBean;
import com.weeho.petim.modle.PetBaseBean;
import com.weeho.petim.modle.PetVarietyBasebean;
import com.weeho.petim.modle.UploadImgBean;
import com.weeho.petim.modle.UserInfoBean;
import com.weeho.petim.modle.VersionInitdatabean;
import com.weeho.petim.util.LogUtil;
import com.weeho.petim.util.ToastUtil;
import com.weeho.petim.view.LoadingView;


/**
 * @author 作者 E-mail:wk
 * @version 创建时间：2015-9-29 上午10:00:22 类说明
 * @param网络连接工具类
 */
public class ApiHttpCilent {
	private static ApiHttpCilent mApi;
	private static Context context;

	private static String TAG = "PetIm";
	public static LoadingView loading;

	public static ApiHttpCilent getInstance(Context _context) {
		context = _context;
		if (mApi == null) {
			mApi = new ApiHttpCilent();
		}
		return mApi;
	}

//
//	/**
//	 * 调取元数据接口获取版本数据
//	 *
//	 * @return
//	 */
//	public void initVersiondata(Context baseContext,String android,
//								BaseJsonHttpResponseHandler<VersionInitdatabean> callback) {
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new RequestParams();
//		paramsIn.put("type", android);
//		BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//				UrlsUtil.VersionData, callback);
//	}
//
//	/**
//	 * 验证邀请码
//	 *
//	 * @return
//	 */
//	public void VerificationCode(Context baseContext, String code,
//								BaseJsonHttpResponseHandler<BaseBean> callback) {
//		if(!Utils.isNetworkConnected(baseContext))
//			return;
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new RequestParams();
//		paramsIn.put("validationInfo", code);
////		paramsIn.put("weiXinNumber", "");
////		saveCookie(BasicHttpClient.getInstance(baseContext).asyncHttpClient, baseContext);
//		BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//				UrlsUtil.login, callback);
//	}
//
//	/**
//	 *
//	 *
//	 * @return
//	 */
//	public void Userinfo(Context baseContext, String userid,
//								 BaseJsonHttpResponseHandler<UserInfoBean> callback) {
//		if(!Utils.isNetworkConnected(baseContext))
//			return;
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new RequestParams();
//		paramsIn.put("userid", userid);
//		BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//				UrlsUtil.info, callback);
//	}
//	/**
//	 *
//	 * @return
//	 */
//	public void UpdateUserinfo(Context baseContext, String nickname,int sex,String mobile,String userid,String regionId,String weixinNumber,
//						 BaseJsonHttpResponseHandler<UploadImgBean> callback) {
//		if(!Utils.isNetworkConnected(baseContext))
//			return;
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new RequestParams();
//		if(sex!=-1)
//		paramsIn.put("sex", sex);
//
//		if(!StringUtil.isEmpty(nickname))
//			paramsIn.put("nickName", nickname);
//
//		if(!StringUtil.isEmpty(weixinNumber))
//		paramsIn.put("weixinNumber", weixinNumber);
//
//		if(!StringUtil.isEmpty(mobile))
//		paramsIn.put("mobile", mobile);
//
//		paramsIn.put("userid", userid);
//
//		if(!StringUtil.isEmpty(regionId)) {
//			paramsIn.put("regionId", regionId);
//		}
//		BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//				UrlsUtil.updateInfo, callback);
//	}
//	//获取地址元数据
//	public void GetBaseAddresss(Context baseContext,
//							   BaseJsonHttpResponseHandler<BaseAddressBean> callback) {
//		if(!Utils.isNetworkConnected(baseContext))
//			return;
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new RequestParams();
//		BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//				UrlsUtil.addressInfo, callback);
//	}
//
//	/**
//	 * 修改宠物信息
//	 * */
//	public void UpdatePetinfo(Context baseContext, String petname,int petSex,String breed,String time,String cageCulture,String userid,
//							   BaseJsonHttpResponseHandler<UploadImgBean> callback) {
//		if(!Utils.isNetworkConnected(baseContext))
//			return;
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new RequestParams();
//		if(!StringUtil.isEmpty(petname)) {
//			paramsIn.put("petName", petname);
//		}
//		if(petSex!=-1)
//			paramsIn.put("petSex", petSex);
//
//		if(!StringUtil.isEmpty(breed))
//			paramsIn.put("breed", breed);
//
//		if(!StringUtil.isEmpty(time))
//			paramsIn.put("time", time);
//		if(!StringUtil.isEmpty(cageCulture)) {
//			paramsIn.put("cageCulture", cageCulture);
//		}
//		paramsIn.put("userid", userid);
//
//		BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//				UrlsUtil.UpdatepetInfo, callback);
//	}
//	//修改宠物图像
//	public void UpdatePetImg(Context baseContext, File file, String userid,
//							 BaseJsonHttpResponseHandler<UploadImgBean> callback) {
//		if(!Utils.isNetworkConnected(baseContext))
//			return;
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new RequestParams();
//		try {
//			if (file != null)
//				paramsIn.put("file", file);
//			paramsIn.put("userid", userid);
//			BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//					UrlsUtil.UpdatePetImg, callback);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//	}
//	/*
//	* 获取宠物有信息
//	* */
//	public void GetPetInfo(Context baseContext, String userid,
//							BaseJsonHttpResponseHandler<PetBaseBean> callback){
//		if(!Utils.isNetworkConnected(baseContext))
//			return;
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new RequestParams();
//		paramsIn.put("userid", userid);
//
//		BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//				UrlsUtil.petInfo, callback);
//	}
//
//
//	/*
//	* 获取宠物成就
//	* */
//	public void GetPetAchieve(Context baseContext, String userid,
//						   BaseJsonHttpResponseHandler<AcheveBaseBean> callback){
//		if(!Utils.isNetworkConnected(baseContext))
//			return;
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new RequestParams();
//		paramsIn.put("userid", userid);
//
//		BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//				UrlsUtil.AcheveInfo, callback);
//	}
//
//
//	public void GetVarityInfo(Context baseContext, String userid,
//							BaseJsonHttpResponseHandler<PetVarietyBasebean> callback){
//		if(!Utils.isNetworkConnected(baseContext))
//			return;
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new RequestParams();
//		paramsIn.put("userid", userid);
//
//		BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//				UrlsUtil.VaritypetInfo, callback);
//	}
//
////所有客服账号
//	public void GetkeInfo(Context baseContext, String userid,
//							  BaseJsonHttpResponseHandler<HxBaseBean> callback){
//		if(!Utils.isNetworkConnected(baseContext))
//			return;
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new RequestParams();
//		paramsIn.put("userid", userid);
//
//		BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//				UrlsUtil.kfInfo, callback);
//	}
//
//
//	//当前点击客服账号
//	public void GetimInfo(Context baseContext, String userid,String type,
//						  BaseJsonHttpResponseHandler<HxImBaseBean> callback){
//		if(!Utils.isNetworkConnected(baseContext))
//			return;
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new RequestParams();
//		paramsIn.put("userid", userid);
//		paramsIn.put("Type", type);
//
//		BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//				UrlsUtil.kfimInfo, callback);
//	}
//
//	//
//	public void PetHabitInfo(Context baseContext, String userid,
//						  BaseJsonHttpResponseHandler<BadHabitBean> callback){
//		if(!Utils.isNetworkConnected(baseContext))
//			return;
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new RequestParams();
//		paramsIn.put("userid", userid);
//
//		BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//				UrlsUtil.Badhabitnfo, callback);
//	}
//	public void PetHabitDicInfo(Context baseContext, String userid,
//						  BaseJsonHttpResponseHandler<BadHabitDicBean> callback){
//		if(!Utils.isNetworkConnected(baseContext))
//			return;
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new RequestParams();
//		paramsIn.put("userid", userid);
//
//		BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//				UrlsUtil.BadhabitDicnfo, callback);
//	}
//	public void PetHabitUpdateInfo(Context baseContext, String type,String userid,String viceid,
//						  BaseJsonHttpResponseHandler<BaseBean> callback){
//		if(!Utils.isNetworkConnected(baseContext))
//			return;
//		CreateLoading((Activity) baseContext);
//		RequestParams paramsIn = new ReqestParams();
//		paramsIn.put("type", type);
//		paramsIn.put("userid", userid);
//		paramsIn.put("viceid", viceid);
//
//		BasicHttpClient.getInstance(baseContext).post(baseContext, paramsIn,
//				UrlsUtil.BadhabitUpdatenfo, callback);
//	}
	/*
	 * 等待提示框
	 */
	public static void CreateLoading(Activity baseContext) {
		if (loading == null) {
			loading = new LoadingView(baseContext);
		} else {
			loading.dismiss();
			loading = null;
			loading = new LoadingView(baseContext);
		}
		if(baseContext != null){
			loading.initLoadingDialog(baseContext.getResources().getString(R.string.loading));
			loading.show();
		}
	}
}
