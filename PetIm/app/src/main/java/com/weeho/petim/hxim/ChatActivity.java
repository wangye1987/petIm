package com.weeho.petim.hxim;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;

import com.weeho.petim.controller.MainActivity;
import com.weeho.petim.R;
import com.weeho.petim.base.BaseActivity;

/**
 *
 */
public class ChatActivity extends BaseActivity {
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;
    private FragmentManager fragmentManager;
    private int head_img;

    @Override
    protected boolean hasTitle() {
        return false;
    }

    @Override
    protected void loadChildView() {

    }

    @Override
    protected void getNetData() {

    }

    @Override
    protected void reloadCallback() {

    }

    @Override
    protected void setChildViewListener() {

    }

    @Override
    protected String setTitleName() {
        return "聊天";
    }

    @Override
    protected String setRightText() {
        return null;
    }

    @Override
    protected int setLeftImageResource() {
        return 0;
    }

    @Override
    protected int setMiddleImageResource() {
        return 0;
    }

    @Override
    protected int setRightImageResource() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        activityInstance = this;
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString("userId");
        head_img = getIntent().getExtras().getInt("headImg",0);
        EaseConstant.role_img = head_img;
        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, chatFragment).commit();
        
    }

    @Override
    protected void onCreate() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	// make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    
    public String getToChatUsername(){
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                recordAudio();
            }else{
                //User denied Permission.
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Fragment mFragment = getSupportFragmentManager().findFragmentByTag(chatFragment.getTag());
        if(chatFragment!=null)
            chatFragment.onActivityResult( Activity.RESULT_OK, resultCode, data);
    }
}
