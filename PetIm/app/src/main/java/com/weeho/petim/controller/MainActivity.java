package com.weeho.petim.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.weeho.petim.lib.utils.MD5Tools;
import com.weeho.petim.lib.utils.SharedPreferencesUtil;
import com.weeho.petim.lib.utils.StringUtil;
import com.weeho.petim.lib.utils.WeakHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.weeho.petim.R;
import com.weeho.petim.RetorfitWapper.ApiCallUtil;
import com.weeho.petim.application.MyApplication;
import com.weeho.petim.base.BaseActivity;
import com.weeho.petim.db.InviteMessgeDao;
import com.weeho.petim.fragment.AchievementFragment;
import com.weeho.petim.fragment.ImFragemnt;
import com.weeho.petim.fragment.MyFragment;
import com.weeho.petim.hxim.Constant;
import com.weeho.petim.hxim.HxHelper;
import com.weeho.petim.modle.HxBaseBean;
import com.weeho.petim.modle.MyAppInfo;
import com.weeho.petim.modle.NotReadList;
import com.weeho.petim.modle.VersionInitdatabean;
import com.weeho.petim.network.ApiHttpCilent;
import com.weeho.petim.network.Utils;
import com.weeho.petim.util.ActivityManagerUtil;
import com.weeho.petim.util.ConstantsUtil;
import com.weeho.petim.util.ToastUtil;
import com.weeho.petim.util.UtilsActivity;
import com.weeho.petim.view.AlertDialogCustom;

public class MainActivity extends BaseActivity implements View.OnClickListener ,EMMessageListener{
    private long exitTime;
    private FrameLayout fragment_content;
    private ImageView ivHome;
    private TextView main_activity_tab_home_tv;
    private RelativeLayout main_activity_tab_communicate;
    private ImageView ivAchieve;
    private TextView tvAchieve;
    private RelativeLayout main_activity_tab_achievement;
    private ImageView main_activity_tab_shopcart_iv;
    private TextView tvHome;
    private Button main_activity_product_num;
    private RelativeLayout main_activity_tab_shopping_cart;
    private ImageView ivMy;
    private TextView tvMy;
    private RelativeLayout main_activity_tab_my;
    private LinearLayout main_activity_tab_parent;
    private RelativeLayout relative_parent;

    private FragmentManager fragmentManager;
    private ImFragemnt imFragment;
    private AchievementFragment achieveFragment;
    private MyFragment myFragment;
    private FragmentType currentFragmentType;
    private String pw;
    private String TAG = "MainActivity";
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private TextView unreadLabel;
    public  TextView unreadAddressLable;
    private InviteMessgeDao inviteMessgeDao;
    private String userid;
    private String hxUserName;
    private String hxPassword;
    private FragmentActivity mActivity;
    public MyHandler handler = new MyHandler(this);
    private HxBaseBean baseBean;
    private VersionInitdatabean versionInitdatabean;
    private AlertDialogCustom alertDialog;
    private ApiCallUtil mLoginFamuse;
    private int mTotalNuminit;
//    private ArrayList<NotReadList.NotReadBean> list_num;
    private int mTotalNum;
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            HxHelper.getInstance().getNotifier().onNewMsg(message);
        }
        refreshUIWithMessage();
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        for (MyOnTouchListener listener : onTouchListeners) {
//            if(listener != null) {
//                listener.onTouch(event);
//            }
//        }
//        return super.onTouchEvent(event);
//    }


    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    public  class MyHandler extends WeakHandler<MainActivity> {

        private String userId;
        private String hxUserName;
        private String hxpassword;


        public MyHandler(MainActivity reference) {
            super(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantsUtil.CONTENT_SUCCESS:
                    // 成功
                    break;
                case ConstantsUtil.CONTENT_FAIL:
                    // 成功
                    String error = (String) msg.obj;
                    if(StringUtil.isEmpty(error)) {
                        ToastUtil.showToast(getReference(), "获取数据失败，请重试");
                    }else{
                        ToastUtil.showToast(getReference(), error);
                    }
                    break;
                case ConstantsUtil.CONTENT_SUCCESS_TWO:
                    // 获取版本成功
                    upgradeVersion();
                    break;
                case ConstantsUtil.CONTENT_SUCCESS_THREE:

                    int num = msg.arg1;
                    if(num > 0){
                        unreadAddressLable.setVisibility(View.VISIBLE);
                        unreadAddressLable.setText(num+"");
                    }else{
                        unreadAddressLable.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    }

    //升级版本
    private void upgradeVersion() {
        if(versionInitdatabean !=null && versionInitdatabean.getResult()!=null ){
            VersionInitdatabean.VersionInfo versionBean = versionInitdatabean.getResult();
            //获取当前app版本号
            int currtVersion = Utils.getCurrentAppVersionCode(this);
            //获取服务器版本号
            int serverVersion = versionBean.getVersion();
            //是否强制更新
            final boolean isfocuse =  versionBean.getIsmust();
            //升级地址
            final String appUrl = versionBean.getUrl();
            //升级提示
            final String updateNotice = versionBean.getUpdateinfo();
            if(currtVersion < serverVersion){
                if(alertDialog != null)
                    alertDialog.Updategrade(this, updateNotice,new AlertDialogCustom.UpdateOrNot() {
                        @Override
                        public void setResult(int modle) {
                            if(1 == modle){
                                if(alertDialog !=null )
                                {
                                    alertDialog.Demiss();
                                }
                                if(isfocuse){
                                    android.os.Process.killProcess(android.os. Process.myPid());
                                    System.exit(0);
                                }
                            }else{
                                //更新
                                if (Environment.getExternalStorageState().equals(
                                        Environment.MEDIA_MOUNTED)) {
                                    downFile(appUrl);     //在下面的代码段
                                } else {
                                    Toast.makeText(baseActivity, "SD卡不可用，请插入SD卡",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
            }
        }
    }

    //下载apk
    private void downFile(final String appUrl) {
        alertDialog.pb.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpclient =  new DefaultHttpClient();
                HttpGet httpget = new HttpGet(appUrl);
                HttpResponse response;
                HttpEntity httpentry;
                FileOutputStream fo = null;
                InputStream in = null;
                try {
                    response = httpclient.execute(httpget);
                    httpentry = response.getEntity();
                    int apksize = (int) httpentry.getContentLength();
                    alertDialog.pb.setMax(apksize);
                    in =  httpentry.getContent();
                    if(in != null){
                        File file = new File(Environment.getExternalStorageDirectory(),"petIm.apk");
                        fo = new FileOutputStream(file);
                        byte bytezie[] = new byte[1024];
                        int ch;
                        int lenthcurrt = 0;
                        while((ch=in.read(bytezie)) != -1){
                            //如果还有就写入到本地文件
                            fo.write(bytezie, 0, ch);
                            lenthcurrt += ch;
                            alertDialog.pb.setProgress(lenthcurrt);
                        }
                        fo.flush();//刷新缓存
                        if(fo !=null){
                            fo.close();
                        }
                        if(in !=null){
                            in.close();
                        }
                        InitApk();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void InitApk() {
        handler.post(new Runnable() {
            public void run() {
                alertDialog.Demiss();
                update();
            }
        });
    }
    //安装文件，固定写法
    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "petIm.apk")),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
    @Override
    protected void onCreate() {
        setBaseContentView(R.layout.activity_main);
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        initView();
        initData();
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
        mLoginFamuse = ApiCallUtil.getInstant(mActivity);

    }


    @Override
    protected boolean hasTitle() {
        return false;
    }

    @Override
    protected void loadChildView() {

    }

    @Override
    protected void getNetData() {
     //获取版本信息
        if(mLoginFamuse == null)
        mLoginFamuse = ApiCallUtil.getInstant(mActivity);
        mLoginFamuse.AppVersion("android").enqueue(new Callback<VersionInitdatabean>() {
            @Override
            public void onResponse(Call<VersionInitdatabean> call, Response<VersionInitdatabean> response) {
                versionInitdatabean = response.body();
                Message message = Message.obtain();
                if(versionInitdatabean != null)
                if("1".equals(versionInitdatabean.getStatus())){
                    message.what = ConstantsUtil.CONTENT_SUCCESS_TWO;
                }else {
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = versionInitdatabean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<VersionInitdatabean> call, Throwable t) {

            }
        });
    }


    @Override
    protected void reloadCallback() {

    }

    @Override
    protected void setChildViewListener() {

    }

    @Override
    protected String setTitleName() {
        return null;
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

    private void initData() {
        alertDialog = new AlertDialogCustom();
        Intent intent = getIntent();
        if(intent !=null){
            userid = intent.getStringExtra("userid");
            hxUserName = intent.getStringExtra("hxUserName");
            hxPassword = intent.getStringExtra("hxPassword");
            SharedPreferencesUtil.writeSharedPreferencesString(this,"hxUserName","hxUserName",hxUserName);
        }
        inviteMessgeDao = new InviteMessgeDao(this);
        fragmentManager = getSupportFragmentManager();
        showComunitionFragment();
        boolean islogin = SharedPreferencesUtil.getSharedPreferencesBoolean(mActivity,"islogin","login",false);
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        if(!islogin) {
            if(!StringUtil.isEmpty(hxUserName) && !StringUtil.isEmpty(hxPassword))
            LoadHxIm();
        }

    }
    //实现ConnectionListener接口
    private  class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                        SharedPreferencesUtil.writeSharedPreferencesBoolean(MainActivity.this,"islogin","login",false);
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        SharedPreferencesUtil.writeSharedPreferencesBoolean(MainActivity.this,"islogin","login",false);
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)){
                            //连接不到聊天服务器
                        }else{
                        //当前网络不可用，请检查网络设置
                            }
                    }
                }
            });
        }
    }
    private void Dimess() {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if(ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing())
                    ApiHttpCilent.loading.dismiss();
            }
        });
    }
    /**
     *
     * 测试用注册环信账户
     * */
    //加密注册密码
    private String Md5Pw(String pw){
       return MD5Tools.MD5(pw);

    }
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMessageCount();
        for(EMConversation conversation:EMClient.getInstance().chatManager().getAllConversations().values()){
            if(conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal-chatroomUnreadMsgCount;
    }
//    public void updateUnreadAddressLable() {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                int count = getUnreadAddressCountTotal();
//                if (count > 0) {
//                    unreadAddressLable.setVisibility(View.VISIBLE);
//                } else {
//                    unreadAddressLable.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//
//
//    }
    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }
    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
//        intentFilter.addAction(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        // refresh conversation list
//red packet code : 处理红包回执透传消息
//end of red packet code
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
//                updateUnreadLabel();
                //更新未读消息个数
//                updateUnreadAddressLable();
                if (currentFragmentType == FragmentType.IMfRAGMRNT) {
                    // refresh conversation list
                    if (imFragment != null) {
                        imFragment.refresh();
                    }
                }
                //end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }
    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
//                updateUnreadAddressLable();
//                if (currentFragmentType == FragmentType.IMfRAGMRNT) {
                    // refresh conversation list
                    if (imFragment != null) {
                        imFragment.refresh();
                    }
//                }
            }
        });
    }
    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
               HxHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
//            for (EMMessage message : messages) {
//                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
//                final String action = cmdMsgBody.action();//获取自定义action
//                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//                    RedPacketUtil.receiveRedPacketAckMessage(message);
//                }
//            }
            //end of red packet code
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {}
    };

    @Override
    protected void onResume() {
        super.onResume();
        HxHelper sdkHelper = HxHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
        refreshUIWithMessage();
       //获取每个聊天未读消息
//        list_num = (ArrayList<NotReadList.NotReadBean>) SharedPreferencesUtil.get(this, "listNum", "listNum");
//        mTotalNum = 0;
//        if(list_num != null && list_num.size()>0) {
//            for (NotReadList.NotReadBean notbean : list_num) {
//                mTotalNum += notbean.getNum();
//            }
//            if(mTotalNum > 0){
//                unreadAddressLable.setVisibility(View.VISIBLE);
//                unreadAddressLable.setText(mTotalNum+"");
//            }else{
//                unreadAddressLable.setVisibility(View.INVISIBLE);
//            }
//        }
    }

    /*
        * 初始化拉去环信聊天记录
        * */
    private void LoadHxIm(){
        EMClient.getInstance().login(hxUserName, hxPassword, new EMCallBack() {
            @Override
            public void onSuccess() {
//                Log.d(TAG, "login: onSuccess");
                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                SharedPreferencesUtil.writeSharedPreferencesBoolean(mActivity,"islogin","login",true);
                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        MyApplication.currentUserNick.trim());
                if (!updatenick) {
//                    Log.e("LoginActivity", "update current user nick fail");
                }

                // get user's info (this should be get from App's server or 3rd party service)
                HxHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();


            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d(TAG, "login: onError: " + code);
                runOnUiThread(new Runnable() {
                    public void run() {
//                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
//                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void initView() {
        mActivity = MainActivity.this;
        fragment_content = (FrameLayout) findViewById(R.id.fragment_content);
        ivHome = (ImageView) findViewById(R.id.main_activity_tab_home_iv);
        tvHome = (TextView) findViewById(R.id.main_activity_tab_home_tv);
        main_activity_tab_communicate = (RelativeLayout) findViewById(R.id.main_activity_tab_communicate);
        ivAchieve = (ImageView) findViewById(R.id.main_activity_tab_find_iv);
        tvAchieve = (TextView) findViewById(R.id.main_activity_tab_find_tv);
        main_activity_tab_achievement = (RelativeLayout) findViewById(R.id.main_activity_tab_achievement);
        main_activity_tab_shopcart_iv = (ImageView) findViewById(R.id.main_activity_tab_shopcart_iv);
        main_activity_product_num = (Button) findViewById(R.id.main_activity_product_num);
        main_activity_tab_shopping_cart = (RelativeLayout) findViewById(R.id.main_activity_tab_shopping_cart);
        ivMy = (ImageView) findViewById(R.id.main_activity_tab_my_iv);
        tvMy = (TextView) findViewById(R.id.main_activity_tab_my_tv);
        main_activity_tab_my = (RelativeLayout) findViewById(R.id.main_activity_tab_my);
        main_activity_tab_parent = (LinearLayout) findViewById(R.id.main_activity_tab_parent);
        relative_parent = (RelativeLayout) findViewById(R.id.relative_parent);
        unreadAddressLable = (TextView) findViewById(R.id.unreadAddressLable);

        main_activity_product_num.setOnClickListener(this);

        main_activity_tab_communicate.setOnClickListener(this);
        main_activity_tab_achievement.setOnClickListener(this);
        main_activity_tab_my.setOnClickListener(this);

        ivHome.setOnClickListener(this);
        ivAchieve.setOnClickListener(this);
        ivMy.setOnClickListener(this);


    }
    public enum FragmentType{
        IMfRAGMRNT,ACHIEVE,MYCENTER
    }
    private void showComunitionFragment() {
        // ViewUtil.hideKeyBoard(etInput, baseContext);
        currentFragmentType = FragmentType.IMfRAGMRNT;
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.out_alpha, 0);
        // 隐藏存在的fragment
		hideFragment(transaction);

        if (imFragment == null) {
            // 对象不存在
            imFragment = new ImFragemnt(handler);
            Bundle bundle = new Bundle();
            bundle.putString("userid",userid);
            bundle.putString("hxUserName",hxUserName);
            imFragment.setArguments(bundle);
        }
        if (!imFragment.isAdded()) {
            // 没有被添加过
            transaction.add(R.id.fragment_content, imFragment)
                    .commitAllowingStateLoss();
        } else {
            // 被添加过
            transaction.show(imFragment).commitAllowingStateLoss();
        }
    }

    private void hideFragment(FragmentTransaction transaction) {
		if (achieveFragment != null) {
			transaction.hide(achieveFragment);
		}
		if (imFragment != null) {
			transaction.hide(imFragment);
		}
		if (myFragment != null) {
			transaction.hide(myFragment);
		}
	}

    private void showAchieveFragment() {
        // ViewUtil.hideKeyBoard(etInput, baseContext);
        currentFragmentType = FragmentType.ACHIEVE;
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.out_alpha, 0);
        // 隐藏存在的fragment
		hideFragment(transaction);

        if (achieveFragment == null) {
            // 对象不存在
            achieveFragment = new AchievementFragment();
            Bundle bundle = new Bundle();
            bundle.putString("userid",userid);
            achieveFragment.setArguments(bundle);
        }
        if (!achieveFragment.isAdded()) {
            // 没有被添加过
            transaction.add(R.id.fragment_content, achieveFragment)
                    .commitAllowingStateLoss();
        } else {
            // 被添加过
            transaction.show(achieveFragment).commitAllowingStateLoss();
        }
    }

    private void showMyFragment() {
        // ViewUtil.hideKeyBoard(etInput, baseContext);
        currentFragmentType = FragmentType.MYCENTER;
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.out_alpha, 0);
        // 隐藏存在的fragment
		hideFragment(transaction);

        if (myFragment == null) {
            // 对象不存在
            myFragment = new MyFragment();
            Bundle bundle = new Bundle();
            bundle.putString("userid",userid);
            myFragment.setArguments(bundle);
        }

        if (!myFragment.isAdded()) {
            // 没有被添加过
            transaction.add(R.id.fragment_content, myFragment)
                    .commitAllowingStateLoss();
        } else {
            // 被添加过
            transaction.show(myFragment).commitAllowingStateLoss();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_activity_tab_communicate:
                /**
                 * 聊天主界面
                 */
                MainFragment();
                break;

            case R.id.main_activity_tab_achievement:
                /**
                 * 成就
                 */
                AchieveFragment();
                break;
            case R.id.main_activity_tab_home_iv:
                /**
                 * 聊天主界面
                 */
                MainFragment();
                break;

            case R.id.main_activity_tab_find_iv:
                /**
                 * 成就
                 */
                AchieveFragment();
                break;
            case R.id.main_activity_tab_my:
                /**
                 * 个人中心页面
                 */
                MyFragment();
                break;
            case R.id.main_activity_tab_my_iv:
                /**
                 * 个人中心页面
                 */
                MyFragment();
                break;

        }
    }

    private void MyFragment() {
        if (!(FragmentType.MYCENTER == currentFragmentType)) {
            currentFragmentType = FragmentType.MYCENTER;
            initTab();
            ivMy.setImageResource(R.drawable.myclick);
            tvMy.setTextColor(getResources().getColor(
                    R.color.color_1AAD19));
            showMyFragment();
        }
    }

    private void AchieveFragment() {
        if (!(FragmentType.ACHIEVE == currentFragmentType)) {
            currentFragmentType = FragmentType.ACHIEVE;
            initTab();
            ivAchieve.setImageResource(R.drawable.achievemented);
            tvAchieve.setTextColor(getResources().getColor(
                    R.color.color_1AAD19));
            showAchieveFragment();

        }
    }

    private void MainFragment() {
        if (!(FragmentType.IMfRAGMRNT == currentFragmentType)) {
            currentFragmentType = FragmentType.IMfRAGMRNT;
            initTab();
            ivHome.setImageResource(R.drawable.comniued);
            tvHome.setTextColor(getResources().getColor(
                    R.color.color_1AAD19));
             showComunitionFragment();
        }
    }

    private void initTab() {
        ivHome.setImageResource(R.drawable.comniu);
        ivAchieve.setImageResource(R.drawable.achievement);
        ivMy.setImageResource(R.drawable.my);

        tvHome.setTextColor(getResources().getColor(R.color.color_8f8f8f));
        tvAchieve.setTextColor(getResources().getColor( R.color.color_8f8f8f));
        tvMy.setTextColor(getResources().getColor(R.color.color_8f8f8f));

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // TODO Auto-generated method stub
            exitApplication(keyCode, event);

        return true;
    }

    protected void exitApplication(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                ToastUtil.showToast(this,
//                        getResources().getString(R.string.exit_message));
//                exitTime = System.currentTimeMillis();
//            } else {
                finish();
                ActivityManagerUtil.getActivityManager().exitApp(mActivity);
//            }
//        }
    }
}
