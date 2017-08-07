
package com.weeho.petim.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.hyphenate.util.DateUtils;
import com.weeho.petim.lib.utils.SharedPreferencesUtil;
import com.weeho.petim.lib.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.weeho.petim.R;
import com.weeho.petim.RetorfitWapper.ApiCallUtil;
import com.weeho.petim.application.MyApplication;
import com.weeho.petim.base.BaseFragment;
import com.weeho.petim.controller.MainActivity;
import com.weeho.petim.db.LastMsgGDUtil;
import com.weeho.petim.hxim.ChatActivity;
import com.weeho.petim.hxim.Constant;
import com.weeho.petim.lib.utils.TimeUtil;
import com.weeho.petim.modle.HxBaseBean;
import com.weeho.petim.modle.HxImBaseBean;
import com.weeho.petim.modle.LoginBeanSave;
import com.weeho.petim.modle.MsgHistoryBean;
import com.weeho.petim.modle.NotReadList;
import com.weeho.petim.modle.PetBaseBean;
import com.weeho.petim.network.ApiHttpCilent;
import com.weeho.petim.network.Utils;
import com.weeho.petim.util.ConstantsUtil;
import com.weeho.petim.util.ToastUtil;

/**
 * Created by wangkui on 2017/4/18.
 */


public class ImFragemnt extends BaseFragment {

    private LinearLayout rc_msg;
    private LinearLayout xw_msg;
    private LinearLayout yp_msg;
    private LinearLayout xl_msg;
    private LinearLayout ts_msg;
    private TextView tv_msg1;
    private TextView tv_time1;
    private TextView tv_msg2;
    private TextView tv_time2;
    private TextView tv_msg3;
    private TextView tv_time3;
    private TextView tv_msg4;
    private TextView tv_time4;
    private TextView tv_msg5;
    private TextView tv_time5;
    private final static int MSG_REFRESH = 2;
    private final static int MSG_NEW = 3;
    private String userid;
    private HxImBaseBean hxImBaseBean;
    private String hxUserName;
    private HxBaseBean baseBean;
    private HxBaseBean.ResultBean resultBean;
    private TextView num_1;
    private TextView num_2;
    private TextView num_3;
    private TextView num_4;
    private TextView num_5;
    private int unreadNum;
    private String typeUpdate = "";
    private MainActivity.MyHandler mActicityHandler;
    private PetBaseBean petbaseBean;
    private int img_id;
    private ApiCallUtil mApiCallUtil;
    private String userTitle;
    //所用未读消息数
    private  int mTotalNum;
    private  int mTotalNuminit;
    //本地存储未读条数
//    private ArrayList<NotReadList.NotReadBean> list_num = new ArrayList<>();
    private boolean isChange;
    //主动请求的客服环信账号
//    private String mReceive_hxUserName;
    private long currt_time;
    private String hxId = "";
    private String hxTo;
    private Activity mActivity;
    private EMConversation conversation_unread;

    public ImFragemnt(){
    }
    @SuppressLint("ValidFragment")
    public ImFragemnt(MainActivity.MyHandler mActicityHandler){
      this.mActicityHandler = mActicityHandler;
  }
    @Override
    protected boolean isShowLeftIcon() {
        return false;
    }
    private String userId;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.imfragment, container,
                true);
        initView(rootView);
        initData();
        return rootView;
    }
    /**
     * refresh ui
     */
    public void refresh() {
        if(!handler.hasMessages(MSG_REFRESH)){
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initData();

        }
    }


    private MainActivity.MyHandler myHandler;
    protected Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
//                    onConnectionDisconnected();conversationList.addAll(loadConversationList())
                    break;
                case 1:
//                    onConnectionConnected();
                    break;

                case MSG_REFRESH:
                {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    setView(conversationList);
//                    conversationListView.refresh();
                    break;

                }

                case MSG_NEW:
                        //更新界面

                    break;
                case ConstantsUtil.CONTENT_SUCCESS:
                    if(hxImBaseBean != null && hxImBaseBean.getResult()!= null) {
                        hxTo = hxImBaseBean.getResult().getHxusername();
                        UpdateNum(hxTo);
                        if(!StringUtil.isEmpty(hxTo)) {
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            intent.putExtra("userId", hxTo);
                            intent.putExtra("headImg", img_id);
                            intent.putExtra("userTitle", userTitle);
                            intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                            //设置聊天图像 聊天界面用
                            startActivity(intent);
                        }else{
                            ToastUtil.showToast(getActivity(),"数据异常,请您稍后重试");
                        }
                    }
                    break;
                case ConstantsUtil.CONTENT_SUCCESS_TWO:
                    //获取所有客服信息
                    if(baseBean != null && baseBean.getResult()!= null){
                        resultBean = baseBean.getResult();
                        //存储客服列表
                        ArrayList<List<String>> chatImService = new ArrayList();
                        InsertImService(chatImService);
                        SharedPreferencesUtil.save(getContext(),"chatservice","service",chatImService);
                        refresh();
                    }
                    break;
                case ConstantsUtil.CONTENT_FAIL:
                    String error = (String) msg.obj;
                    if(StringUtil.isEmpty(error))
                        ToastUtil.showToast(getActivity(),"操作失败,请您稍后重试");
                    else
                        ToastUtil.showToast(getActivity(),error);
                    break;
                default:
                    break;
            }
        }
    };

    private void InsertImService(ArrayList<List<String>> chatImService){
        for(HxBaseBean.ResultBean.List1Bean bean:resultBean.getList1()) {
            List<String> chatImServiceList = new ArrayList();
            chatImServiceList.add(bean.getHxusername());
            chatImService.add(chatImServiceList);
        }
        for(HxBaseBean.ResultBean.List2Bean bean:resultBean.getList2()) {
            List<String> chatImServiceList = new ArrayList();
            chatImServiceList.add(bean.getHxusername());
            chatImService.add(chatImServiceList);
        }
        for(HxBaseBean.ResultBean.List3Bean bean:resultBean.getList3()) {
            List<String> chatImServiceList = new ArrayList();
            chatImServiceList.add(bean.getHxusername());
            chatImService.add(chatImServiceList);
        }
        for(HxBaseBean.ResultBean.List4Bean bean:resultBean.getList4()) {
            List<String> chatImServiceList = new ArrayList();
            chatImServiceList.add(bean.getHxusername());
            chatImService.add(chatImServiceList);
        }
        for(HxBaseBean.ResultBean.List5Bean bean:resultBean.getList5()) {
            List<String> chatImServiceList = new ArrayList();
            chatImServiceList.add(bean.getHxusername());
            chatImService.add(chatImServiceList);
        }
    }
private String input_num = "0";
    //点击获取客服账号成功 更新被点击的聊天对应未读数为0
    private void UpdateNum(String hxTo){
        mTotalNum = 0;
        if(!StringUtil.isEmpty(typeUpdate)){
            int numType = Integer.parseInt(typeUpdate);
            switch (numType){
                case Constant.ZERRO:
                    if(!StringUtil.isEmpty(num_1.getText().toString().trim()))
                        input_num = num_1.getText().toString().trim();
                    else
                        input_num = "0";
                    num_1.setText("0");
                    num_1.setVisibility(View.INVISIBLE);
                    break;
                case Constant.ONE:
                    if(!StringUtil.isEmpty(num_2.getText().toString().trim()))
                        input_num = num_2.getText().toString().trim();
                    else
                        input_num = "0";
                    num_2.setText("0");
                    num_2.setVisibility(View.INVISIBLE);
                    break;
                case Constant.TWO:
                    if(!StringUtil.isEmpty(num_3.getText().toString().trim()))
                        input_num = num_3.getText().toString().trim();
                    else
                        input_num = "0";
                    num_3.setText("0");
                    num_3.setVisibility(View.INVISIBLE);
                    break;
                case Constant.THREE:
                    if(!StringUtil.isEmpty(num_4.getText().toString().trim()))
                        input_num = num_4.getText().toString().trim();
                    else
                        input_num = "0";
                    num_4.setText("0");
                    num_4.setVisibility(View.INVISIBLE);
                    break;
                case Constant.FOUR:
                    if(!StringUtil.isEmpty(num_5.getText().toString().trim()))
                        input_num = num_5.getText().toString().trim();
                    else
                        input_num = "0";
                    num_5.setText("0");
                    num_5.setVisibility(View.INVISIBLE);
                    break;
            }

            //点击后更新本地数据
//            if(list_num!=null ){
//            for(NotReadList.NotReadBean bean : list_num){
//                if(bean.getId().equals(String.valueOf((Integer.parseInt(typeUpdate)+1)))){
//                    bean.setNum(0);
//                    bean.setHxUserid(hxTo);
//                    break;
//                }
//               }
//                SharedPreferencesUtil.save(getActivity(),"listNum","listNum",list_num);
//            }
//            if(list_num != null ) {
//                if( list_num.size() > 0 ) {
//                    for (NotReadList.NotReadBean notBean : list_num) {
//                        mTotalNum += notBean.getNum();
//                    }
//                    Message msgNum = new Message();
//                    msgNum.what = ConstantsUtil.CONTENT_SUCCESS_THREE;
//                    msgNum.arg1 = mTotalNum;
//                    mActicityHandler.handleMessage(msgNum);
//                }
//            }
        }
    }

    private EaseConversationList.EaseConversationListHelper cvsListHelper;

    public void setCvsListHelper(EaseConversationList.EaseConversationListHelper cvsListHelper){
        this.cvsListHelper = cvsListHelper;
    }
    private int SmatchIm(String fromUserName){
        if(resultBean!=null){
        List<HxBaseBean.ResultBean.List1Bean> list1 = resultBean.getList1();
        int size = list1.size();
        for(int i = 0;i<size;i++){
            if(list1.get(i).getHxusername().equalsIgnoreCase(fromUserName)){
                return Constant.ONE;
            }
        }
        List<HxBaseBean.ResultBean.List2Bean> list2 = resultBean.getList2();
         size = list2.size();
        for(int i=0;i<size;i++){
            if(list2.get(i).getHxusername().equalsIgnoreCase(fromUserName)){
                return Constant.TWO;
            }
        }
        List<HxBaseBean.ResultBean.List3Bean> list3 = resultBean.getList3();
         size = list3.size();
        for(int i=0;i<size;i++){
            if(list3.get(i).getHxusername().equalsIgnoreCase(fromUserName)){
                return Constant.THREE;
            }
        }
        List<HxBaseBean.ResultBean.List4Bean> list4 = resultBean.getList4();
         size = list4.size();
        for(int i=0;i<size;i++){
            if(list4.get(i).getHxusername().equalsIgnoreCase(fromUserName)){
                return Constant.FOUR;
            }
        }
        List<HxBaseBean.ResultBean.List5Bean> list5 = resultBean.getList5();
         size = list5.size();
        for(int i=0;i<size;i++){
            if(list5.get(i).getHxusername().equalsIgnoreCase(fromUserName)){
                return Constant.FIRVE;
            }
        }}
        return 0;
    }



    /**
     * 推送更新试图
     * */
    private void UpdateMsg(TextView msg,TextView time,  EMMessage lastMessage,TextView unReadnum,int type){
        msg.setText(EaseSmileUtils.getSmiledText(getContext(), EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()))),TextView.BufferType.SPANNABLE);
        time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
        UpdateHistoryMsg(msg, time, type);

        if(unreadNum>0) {
            unReadnum.setVisibility(View.VISIBLE);
            if(unreadNum >= 99) {
                unReadnum.setText("99");
            }else {
                unReadnum.setText(unreadNum + "");
            }
            isChange = false;
            //存储各个未读消息个数
//            NotReadList.NotReadBean mNotReadBean = new NotReadList.NotReadBean();
//            if(list_num != null ){
//                   for(NotReadList.NotReadBean bean : list_num){
//                      if(bean.getId().equals(String.valueOf(type))){
//                          bean.setNum(unreadNum);
//                      }
//                   }
//            }
            //存取各个聊天对象未读消息到本地
//            if(list_num!= null && mActivity!=null)
//            SharedPreferencesUtil.save(mActivity,"listNum","listNum",list_num);
        }else{
            unReadnum.setVisibility(View.INVISIBLE);
        }
    }

    private void UpdateHistoryMsg(TextView msg, TextView time, int type) {
        //插入数据 先查询是否有记录 有就更新 没有就插入
        List<MsgHistoryBean> listHas = LastMsgGDUtil.QueryLastMsgSingle(String.valueOf(type));
        MsgHistoryBean msgHistoryBean = new MsgHistoryBean();
        SetMsgData(msg, time, type, msgHistoryBean);
        if(listHas.size() == 0){
            // 么有记录
            LastMsgGDUtil.InsertLastMsg(msgHistoryBean);
        }else{
            msgHistoryBean = listHas.get(0);
            SetMsgData(msg, time, type, msgHistoryBean);
            LastMsgGDUtil.UpdateLastMsg(msgHistoryBean);
        }
    }

    private void SetMsgData(TextView msg, TextView time, int type, MsgHistoryBean msgHistoryBean) {
        msgHistoryBean.setType(String.valueOf(type));
        msgHistoryBean.setLastMsg(msg.getText().toString().trim());
        msgHistoryBean.setLastMsgTime(time.getText().toString().trim());
    }

    //初始化更新聊天记录
    private void InitUpdateMsg(TextView msg,TextView time,MsgHistoryBean msgHistoryBean){
        if(msgHistoryBean!=null && !StringUtil.isEmpty(msgHistoryBean.getLastMsg()))
        msg.setText(msgHistoryBean.getLastMsg());
        if(msgHistoryBean!=null && !StringUtil.isEmpty(msgHistoryBean.getLastMsgTime()))
            time.setText(msgHistoryBean.getLastMsgTime());
    }

    //收到推送更新试图
    private void setView(List<EMConversation> conversationList) {
        MyApplication.totalNum = 0;
        mTotalNum = 0;
        mActivity = getActivity();
       for(EMConversation conversation:conversationList){
           if(conversation.getType() == EMConversation.EMConversationType.Chat){
               if (conversation.getAllMsgCount() != 0) {
                   if(mActivity !=null) {
//                       if (SharedPreferencesUtil.get(mActivity, "listNum", "listNum") != null)
//                           list_num = (ArrayList<NotReadList.NotReadBean>) SharedPreferencesUtil.get(getActivity(), "listNum", "listNum");
                   }
                   // show the content of latest message
                   EMMessage lastMessage = conversation.getLastMessage();
                   //未读数量
//                   unreadNum = conversation.getUnreadMsgCount();
//                   mTotalNum = mTotalNum+unreadNum;

                   String content = null;
                   if(cvsListHelper != null){
                       content = cvsListHelper.onSetItemSecondaryText(lastMessage);
                   }
                   hxId = conversation.conversationId();
                   switch (SmatchIm(conversation.conversationId())){
                       case Constant.ONE:
                           //主动接受的客服账号 规则都是发给第一个训犬师账号
                           UpdateMsg(tv_msg1,tv_time1,lastMessage,num_1,Constant.ONE);
                           UpdateReceiveNum(num_1,hxId);
                           break;
                       case Constant.TWO:
                           UpdateMsg(tv_msg2,tv_time2,lastMessage,num_2,Constant.TWO);
                           UpdateReceiveNum(num_2,hxId);
                           break;
                       case Constant.THREE:
                           UpdateMsg(tv_msg3,tv_time3,lastMessage,num_3,Constant.THREE);
                           UpdateReceiveNum(num_3,hxId);
                           break;
                       case Constant.FOUR:
                           UpdateMsg(tv_msg4,tv_time4,lastMessage,num_4,Constant.FOUR);
                           UpdateReceiveNum(num_4,hxId);
                           break;
                       case Constant.FIRVE:
                           UpdateMsg(tv_msg5,tv_time5,lastMessage,num_5,Constant.FIRVE);
                           UpdateReceiveNum(num_5,hxId);
                           break;
                   }

               }
           }
       }

        Message msgNum = new Message();
        msgNum.what = ConstantsUtil.CONTENT_SUCCESS_THREE;
        msgNum.arg1 = mTotalNum;
        if(mActicityHandler!=null)
        mActicityHandler.handleMessage(msgNum);

    }

    /*
    * 获取每个角色未读消息数量
    * @tv_unread_num 显示的地方
    *
    * @username聊天对象id
    * */
    private void UpdateReceiveNum(TextView tv_unread_num,String username){

        conversation_unread = EMClient.getInstance().chatManager().getConversation(username);
        if(conversation_unread.getUnreadMsgCount()!=0) {
            tv_unread_num.setVisibility(View.VISIBLE);
            tv_unread_num.setText(conversation_unread.getUnreadMsgCount() + "");
        }else{
                tv_unread_num.setVisibility(View.INVISIBLE);
            }
        mTotalNum+=conversation_unread.getUnreadMsgCount();
    }
//    private void UpdateReceiveNum(String type) {
//        if(list_num != null){
//            for(NotReadList.NotReadBean notReadBean:list_num){
//                if(type.equals(notReadBean.getId())){
//                    notReadBean.setNum(unreadNum);
////                    notReadBean.setHxUserid(hxId);
//                    break;
//                }
//            }
//        }
//    }


    /**
     * load conversation list
     *
     * @return
    +    */
    protected List<EMConversation> loadConversationList(){
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }
    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    private TextView getTv(int id){
        switch (id){
            case Constant.ONE:
            return tv_msg1;
            case Constant.TWO:
            return tv_msg2;
            case Constant.THREE:
            return tv_msg3;
            case Constant.FOUR:
            return tv_msg4;
            case Constant.FIRVE:
            return tv_msg5;
            default:
                return null;
        }
    }
    private String GetUseName() {
        LoginBeanSave loginBeanSave  = (LoginBeanSave) SharedPreferencesUtil.get(getActivity(),"login","login");
        return loginBeanSave.getHxUserName();
    }
    private TextView getTimeTv(int id){
        switch (id){
            case Constant.ONE:
            return tv_time1;
            case Constant.TWO:
            return tv_time2;
            case Constant.THREE:
            return tv_time3;
            case Constant.FOUR:
            return tv_time4;
            case Constant.FIRVE:
            return tv_time5;
            default:
                return null;
        }
    }

    //初始化数据
    private void initData() {

        Bundle bundle = getArguments();
        userid = (String) bundle.get("userid");
        hxUserName = (String) bundle.get("hxUserName");
        if (StringUtil.isEmpty(hxUserName)) {
            hxUserName = GetUseName();
        }
        mApiCallUtil = ApiCallUtil.getInstant(getActivity());
        if (StringUtil.isEmpty(userid)) {
            LoginBeanSave loginBeanSave = (LoginBeanSave) SharedPreferencesUtil.get(getActivity(), "login", "login");
            userid = loginBeanSave.getUserId();
        }
        //获取客服列表
        GetServerList();

        //初始化更新消息历史
        List<MsgHistoryBean> listMsg = LastMsgGDUtil.QueryLastMsgAll();
        if (listMsg != null && listMsg.size() > 0) {
            for (int i = 1; i < 6; i++) {
                for (MsgHistoryBean msgHistoryBean : listMsg) {
                    if (msgHistoryBean.getType().equals(String.valueOf(i))) {
                        InitUpdateMsg(getTv(i), getTimeTv(i), msgHistoryBean);
                        break;
                    }
                }
            }
        }

        //获取每个聊天未读消息
//        list_num = (ArrayList<NotReadList.NotReadBean>) SharedPreferencesUtil.get(getActivity(), "listNum", "listNum");


        //不是第一次登录
//        if(list_num != null ) {
//              if( list_num.size() > 0 ) {
//                  mTotalNum = 0;
//                  for (NotReadList.NotReadBean notBean : list_num) {
//                      mTotalNum += notBean.getNum();
//                  }
//                  Message msgNum = new Message();
//                  msgNum.what = ConstantsUtil.CONTENT_SUCCESS_THREE;
//                  msgNum.arg1 = mTotalNum;
//                  if(mActicityHandler!=null)
//                  mActicityHandler.handleMessage(msgNum);
//              }
//        }
        //初始化所用未读消息数
//        if (list_num != null){
//            InitNotReadNum();
//        }else {
//            list_num = new ArrayList<>();
//            //初始化所用数据都为0
//            for(int i = 1;i < 6;i++){
//                NotReadList.NotReadBean mNotReadBean = new NotReadList.NotReadBean();
//                mNotReadBean.setId(String.valueOf(i));
//                mNotReadBean.setNum(0);
//                list_num.add(mNotReadBean);
//            }
//            SharedPreferencesUtil.save(getActivity(),"listNum","listNum",list_num);
//        }

//        EMMessageListener msgListener = new EMMessageListener() {
//
//            @Override
//            public void onMessageReceived(List<EMMessage> messages) {
//                //收到消息
//                for (EMMessage message : messages) {
//                    String username = null;
//                    // group message
//                    if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
//                        username = message.getTo();
//                    } else {
//                        // single chat message
//                        username = message.getFrom();
//                    }
//                    userId = username;
//                    if(message.getType().equals(EMMessage.Type.TXT))
////                    tv_msg1.setText(message.getBody().toString());
//                    // if the message is for current conversation
////                    if (username.equals(toChatUsername) || message.getTo().equals(toChatUsername)) {
//////                        messageList.refreshSelectLast();
////                        EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
////                        conversation.markMessageAsRead(message.getMsgId());
////                    } else {
////                        EaseUI.getInstance().getNotifier().onNewMsg(message);
////                    }
//                }
//            }
//
//            @Override
//            public void onCmdMessageReceived(List<EMMessage> messages) {
//                //收到透传消息
//            }
//
//            @Override
//            public void onMessageRead(List<EMMessage> messages) {
//                //收到已读回执
//            }
//
//            @Override
//            public void onMessageDelivered(List<EMMessage> message) {
//                //收到已送达回执
//            }
//
//            @Override
//            public void onMessageChanged(EMMessage message, Object change) {
//                //消息状态变动
//            }
//        };
//        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    //未读消息初始化
//    private void InitNotReadNum(){
//       for(NotReadList.NotReadBean bean : list_num){
//           if(!StringUtil.isEmpty(bean.getId())){
//           switch (Integer.parseInt(bean.getId())){
//               case Constant.ONE:
//                   InitViewByKey(num_1,bean.getNum());
//                   break;
//               case Constant.TWO:
//                   InitViewByKey(num_2,bean.getNum());
//                   break;
//               case Constant.THREE:
//                   InitViewByKey(num_3,bean.getNum());
//                   break;
//               case Constant.FOUR:
//                   InitViewByKey(num_4,bean.getNum());
//                   break;
//               case Constant.FIRVE:
//                   InitViewByKey(num_5,bean.getNum());
//                   break;
//           }
//       }}
//    }
    private void InitViewByKey(TextView tvNum,int num){
        if(num<1){
            tvNum.setVisibility(View.INVISIBLE);
        }else if(num>99){
            tvNum.setVisibility(View.VISIBLE);
            tvNum.setText("99");
        }else{
            tvNum.setVisibility(View.VISIBLE);
            tvNum.setText(num+"");
        }
    }
    private void GetServerList() {
        mApiCallUtil.GetHxServiceList(userid).enqueue(new Callback<HxBaseBean>() {
            @Override
            public void onResponse(Call<HxBaseBean> call, Response<HxBaseBean> response) {
                baseBean = response.body();
                if(baseBean==null)
                    return;
                Message message = Message.obtain();
                if("1".equals(baseBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS_TWO;
                    message.obj = baseBean.getResult();
                }else{
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = baseBean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<HxBaseBean> call, Throwable t) {
                Dimess();
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
            }
        });
    }

    private void initView(View rootView){
        rc_msg = (LinearLayout) rootView.findViewById(R.id.rc_msg);
        xw_msg = (LinearLayout) rootView.findViewById(R.id.xw_msg);
        yp_msg = (LinearLayout) rootView.findViewById(R.id.yp_msg);
        xl_msg = (LinearLayout) rootView.findViewById(R.id.xl_msg);
        ts_msg = (LinearLayout) rootView.findViewById(R.id.ts_msg);

        tv_msg1 = (TextView) rootView.findViewById(R.id.tv_msg1);
        tv_time1 = (TextView) rootView.findViewById(R.id.tv_time1);
        tv_msg2 = (TextView) rootView.findViewById(R.id.tv_msg2);
        tv_time2 = (TextView) rootView.findViewById(R.id.tv_time2);

        num_1 = (TextView) rootView.findViewById(R.id.num_1);
        num_2 = (TextView) rootView.findViewById(R.id.num_2);
        num_3 = (TextView) rootView.findViewById(R.id.num_3);
        num_4 = (TextView) rootView.findViewById(R.id.num_4);
        num_5 = (TextView) rootView.findViewById(R.id.num_5);

        tv_msg3 = (TextView) rootView.findViewById(R.id.tv_msg3);
        tv_time3 = (TextView) rootView.findViewById(R.id.tv_time3);

        tv_msg4 = (TextView) rootView.findViewById(R.id.tv_msg4);
        tv_time4 = (TextView) rootView.findViewById(R.id.tv_time4);

        tv_msg5 = (TextView) rootView.findViewById(R.id.tv_msg5);
        tv_time5 = (TextView) rootView.findViewById(R.id.tv_time5);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(!Utils.isNetworkConnected(getActivity()))
            return;
        switch (v.getId()){
            case R.id.rc_msg:
                img_id = R.drawable.iv_rc;
                userTitle = "日常训练师";
                GetKfIm("0");
                break;
            case R.id.xw_msg:
                GetKfIm("1");
                img_id = R.drawable.iv_xw;
                userTitle = "行为分析师";
                break;
            case R.id.yp_msg:
                GetKfIm("2");
                img_id = R.drawable.iv_yp;
                userTitle = "用品专家";
                break;
            case R.id.xl_msg:
                GetKfIm("3");
                img_id = R.drawable.iv_fxs;
                userTitle = "心理分析师";
                break;
            case R.id.ts_msg:
                GetKfIm("4");
                img_id = R.drawable.iv_ts;
                userTitle = "投诉中心";
                break;
        }
    }
    private String GetUserId() {
        LoginBeanSave loginBeanSave  = (LoginBeanSave) SharedPreferencesUtil.get(getActivity(),"login","login");
        return loginBeanSave.getUserId();
    }
    //获取客服im账号
    private void GetKfIm(String type){
        if(!Utils.isNetworkConnected(getActivity()))
            return;
        typeUpdate = type;
        if(StringUtil.isEmpty(userid))
            userid = GetUserId();

        ApiHttpCilent.CreateLoading(getActivity());
        mApiCallUtil.GetHxUserName(type,userid).enqueue(new Callback<HxImBaseBean>() {
            @Override
            public void onResponse(Call<HxImBaseBean> call, Response<HxImBaseBean> response) {
                Dimess();
                hxImBaseBean = response.body();
                if(hxImBaseBean==null)
                    return;
                Message message = Message.obtain();
                if("1".equals(hxImBaseBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS;
                }else{
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = hxImBaseBean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<HxImBaseBean> call, Throwable t) {
                Dimess();
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
            }
        });
    }
    private void Dimess() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if(ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing())
                    ApiHttpCilent.loading.dismiss();
            }
        });
    }
    @Override
    protected void getNetData() {

    }

    @Override
    protected void setViewListener() {
        rc_msg.setOnClickListener(this);
        xw_msg.setOnClickListener(this);
        yp_msg.setOnClickListener(this);
        xl_msg.setOnClickListener(this);
        ts_msg.setOnClickListener(this);
    }

    @Override
    protected boolean hasTitle() {
        return true;
    }

    @Override
    protected boolean hasTitleIcon() {
        return false;
    }

    @Override
    protected boolean hasDownIcon() {
        return false;
    }

    @Override
    protected void reloadCallback() {
    }

    @Override
    protected String setTitleName() {
        return "交流";
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
}
