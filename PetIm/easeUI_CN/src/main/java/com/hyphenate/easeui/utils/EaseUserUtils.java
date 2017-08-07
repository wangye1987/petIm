package com.hyphenate.easeui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }
    
    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView,EMMessage message){
    	EaseUser user = getUserInfo(username);
        if(message!=null){
        if(message.direct() == EMMessage.Direct.SEND){
        //获取图像地址
        SharedPreferences userSd = context.getSharedPreferences("avatar",
                Context.MODE_PRIVATE);
        String headUrl =  userSd.getString("avatar", "");
        if(headUrl != null && !"".equals(headUrl)){
            Glide.with(context).load(headUrl).into(imageView);
        }else{
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
         }
        }else{
            try {
                int avatarResId = EaseConstant.getRole_img() ;
                Glide.with(context).load(avatarResId).placeholder(R.drawable.ease_default_avatar).error(R.drawable.ease_default_avatar).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load("").placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        }}
//        if(user != null && user.getAvatar() != null){
//            try {
//                int avatarResId = Integer.parseInt(user.getAvatar());
//                Glide.with(context).load(avatarResId).into(imageView);
//            } catch (Exception e) {
//                //use default avatar
//                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
//            }
//        }else{
//            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
//        }
    }
    
    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
        	if(user != null && user.getNick() != null){
        		textView.setText(user.getNick());
        	}else{
        		textView.setText(username);
        	}
        }
    }
    
}