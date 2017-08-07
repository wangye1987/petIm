package com.weeho.petim.fragment;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.weeho.petim.R;
import com.weeho.petim.RetorfitWapper.ApiCallUtil;
import com.weeho.petim.adapter.AchievementAdapter;
import com.weeho.petim.adapter.SegmentAdapter;
import com.weeho.petim.base.BaseFragment;
import com.weeho.petim.lib.utils.SharedPreferencesUtil;
import com.weeho.petim.lib.utils.StringUtil;
import com.weeho.petim.lib.utils.WeakHandler;
import com.weeho.petim.modle.AcheveBaseBean;
import com.weeho.petim.modle.AcheveBaseBean.ResultBean;
import com.weeho.petim.modle.LoginBeanSave;
import com.weeho.petim.modle.PetDanBaseBean;
import com.weeho.petim.modle.PetDanBaseBean.ResultBean.ListBean;
import com.weeho.petim.network.ApiHttpCilent;
import com.weeho.petim.network.Utils;
import com.weeho.petim.util.ConstantsUtil;
import com.weeho.petim.util.ConvertDpAndPx;
import com.weeho.petim.util.ToastUtil;
import com.weeho.petim.view.AlertAutoDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by wangkui on 2017/4/18.
 * 成就界面
 */

public class AchievementFragment extends BaseFragment {

    private RecyclerView recycleView;
    private RecyclerView dan_recyclerview;
    private FragmentActivity mActivity;
    private String userid;
    private AcheveBaseBean acheveBaseBean;
    private MyHandler handler = new MyHandler(this);
    private TextView ex_notice;
    private TextView tv_achieve_update;
    private AchievementAdapter achievementAdapter;
    ArrayList<ResultBean.DataBean>  list = new ArrayList<>();
    private ApiCallUtil mApiCallUtil;
    private PetDanBaseBean mPetDanBaseBean;
    private SegmentAdapter mSegmentAdapter;
    //段位集合
    private List<ListBean> mListResultBean = new ArrayList<>();
    private TextView dan_name;
    // 自动弹出框内容标题
    private int auto_name  ;
    private String auto_title_name ;
    private List<String> mAuto_complete = new ArrayList<>();

    private int[] wordList_one = new int[]{R.string.achieve_one_one, R.string.achieve_one_two, R.string.achieve_one_three};
    private int[] wordList_two = new int[]{R.string.achieve_two_one, R.string.achieve_two_two, R.string.achieve_two_three};
    private int[] wordList_three = new int[]{R.string.achieve_three_one, R.string.achieve_three_two, R.string.achieve_three_three};
    private int[] wordList_four = new int[]{R.string.achieve_four_one, R.string.achieve_four_two, R.string.achieve_four_three};
    private int[] wordList_five = new int[]{R.string.achieve_five_one, R.string.achieve_five_two, R.string.achieve_five_three};
    private int[] wordList_six = new int[]{R.string.achieve_six_one, R.string.achieve_six_two, R.string.achieve_six_three};

    private int[] head_image = new int[]{R.drawable.qt, R.drawable.by, R.drawable.hj, R.drawable.bj, R.drawable.zs, R.drawable.wz};
    private int textId ;
    //图片下面标题
    private int headText[] = new int[]{R.string.achieve_qt, R.string.achieve_by, R.string.achieve_hj, R.string.achieve_bj, R.string.achieve_zs, R.string.achieve_wz};


    //存贮标志位 如果已经弹出过 就不需要在弹出
    private List<Integer> isShowed = new ArrayList<>();
    private int head_image_id;
    private int head_text_id;
    private AlertAutoDialog dialog_auto;

    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // TODO: inflate a fragment view
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        ButterKnife.bind(this, rootView);
//        initData();
//        return rootView;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.unbind(this);
//    }

    public class MyHandler extends WeakHandler<AchievementFragment> {
        public MyHandler(AchievementFragment reference) {
            super(reference);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantsUtil.CONTENT_SUCCESS:
                    if (acheveBaseBean != null && acheveBaseBean.getResult() != null && acheveBaseBean.getResult().getData() != null) {
                        list.clear();
                        list = (ArrayList<ResultBean.DataBean>) acheveBaseBean.getResult().getData();
                        int size = list.size();
                        if (size > 0) {
                            tv_achieve_update.setText("(已改正" + size + "项恶习)");
                            achievementAdapter.setData(list);
                            achievementAdapter.notifyDataSetChanged();
                            ex_notice.setVisibility(View.GONE);
                            recycleView.setVisibility(View.VISIBLE);
                        } else {
                            ex_notice.setVisibility(View.VISIBLE);
                            recycleView.setVisibility(View.GONE);
                        }
                    }
                    break;
                case ConstantsUtil.CONTENT_SUCCESS_TWO:
                    //获取宠物段位成功
                    GetDanSuccess();

                    break;
                case ConstantsUtil.CONTENT_FAIL:
                    //
                    String error = (String) msg.obj;
                    if(StringUtil.isEmpty(error))
                        ToastUtil.showToast(getReference().mActivity, "获取失败,请稍后重试");
                    else
                        ToastUtil.showToast(getReference().mActivity, error);
                    break;
            }
        }
    }

    /**
     * 获取等级名称
     */
    private String mSegmentName = "";
    private  List<String> reach = new ArrayList<>();
    private  void getDanName(List<ListBean> mListResultBean){
        reach.clear();
        int size = mListResultBean.size();
        for(int i = 0;i < size; i++){
            if(mListResultBean.get(i).isIsReach()){
                reach.add(mListResultBean.get(i).getName());
            }
            List<ListBean.ActivityBean> mActivityList = mListResultBean.get(i).getActivity();
            for(ListBean.ActivityBean mActivity:mActivityList) {
                if (mActivity.isIsReach()) {
                    auto_name = i + 1;
                }
            }
        }
    }
    private void GetTitleName(){
        if(reach.contains("王者")){
            mSegmentName = "王者";
            return;
        }else if(reach.contains("钻石")){
            mSegmentName = "钻石";
            return;
        }else if(reach.contains("铂金")){
            mSegmentName = "铂金";
            return;
        }else if(reach.contains("黄金")){
            mSegmentName = "黄金";
            return;
        }else if(reach.contains("白银")){
            mSegmentName = "白银";
            return;
        }else if(reach.contains("青铜")){
            mSegmentName = "青铜";
            return;
        }
    }
    private void GetDanSuccess() {
        if(mPetDanBaseBean != null && mPetDanBaseBean.getResult()!= null && mPetDanBaseBean.getResult().getList()!= null){
            mListResultBean = mPetDanBaseBean.getResult().getList();
            if(mListResultBean != null) {
               getDanName(mListResultBean);
                GetTitleName();
                //自动弹出提示框
                GetAutoNotice(mListResultBean);
                if (!StringUtil.isEmpty(mSegmentName))
                    dan_name.setText("(已达成" + mSegmentName + "等级)");
                mSegmentAdapter.setData((ArrayList<ListBean>) mListResultBean);
                mSegmentAdapter.notifyDataSetChanged();
            }
        }
    }


    /**
     * 获取提示的文本信息
     * */
    private void  GetAutoNotice(List<ListBean> mListResultBean){
        int size  = mListResultBean.size();
        mAuto_complete.clear();
        for(int i=0;i<size;i++){
            List<ListBean.ActivityBean> mActvity = mListResultBean.get(i).getActivity();
          //其他阶段
            if(!StringUtil.isEmpty(mSegmentName)){
                //如果在第几阶段 直接取当前阶段完成的数据 忽略其他阶段的完成项目
                if(i+1 == auto_name){
                  if(mActvity != null){
                       for(ListBean.ActivityBean mlist:mActvity){
                           if(mlist.isIsReach()){
                               mAuto_complete.add(mlist.getActivityName()+"_"+mActvity.size());
                           }
                       }
               }
                }
            }else{
                //青铜阶段
                if(mActvity != null){
                    for(ListBean.ActivityBean mlist:mActvity){
                        if(mlist.isIsReach()){
                            mAuto_complete.add(mlist.getActivityName()+"_"+mActvity.size());
                        }
                    }
                }
            }
        }
        //显示当前的文本
        textId = ReturnAutoNotice(auto_name);
        if(textId !=  -1) {
            Object mObject = SharedPreferencesUtil.get(getActivity(),"autoshow","autoshow");
            if(mObject!=null){
                isShowed = (List<Integer>) mObject;
            }
            if(!isShowed.contains(textId)) {
              ShowAchievementDialog(textId);
            }
        }
    }

    private int ReturnAutoNotice(final int index) {
        if(mAuto_complete.size() > 0) {
            if (mAuto_complete.size() == 1) {
                //完成一项
              return   ReturnText(index,1);
            } else if (mAuto_complete.size() == (Integer.parseInt(mAuto_complete.get(0).split("_")[1]))-1) {
                //差一项完成
                return  ReturnText(index,2);
            }else if (mAuto_complete.size() == (Integer.parseInt(mAuto_complete.get(0).split("_")[1]))) {
                //全部完成
                return ReturnText(index,3);
            }
        }
        return -1;
    }

    private int ReturnText(int index,int size) {
        head_image_id = head_image[index-1];
        head_text_id = headText[index-1];
        switch (index) {
            case 6:
                if (size == 1){
                    return wordList_six[0];
                  }else if(size == 2){
                    return wordList_six[1];
                }else if(size == 3){
                    return wordList_six[2];
                }
            case 5:
                if (size == 1){
                    return wordList_five[0];
                }else if(size == 2){
                    return wordList_five[1];
                }else if(size == 3){
                    return wordList_five[2];
                }
            case 4:
                if (size == 1){
                    return wordList_four[0];
                }else if(size == 2){
                    return wordList_four[1];
                }else if(size == 3){
                    return wordList_four[2];
                }
            case 3:
                if (size == 1){
                    return wordList_three[0];
                }else if(size == 2){
                    return wordList_three[1];
                }else if(size == 3){
                    return wordList_three[2];
                }
            case 2:
                if (size == 1){
                    return wordList_two[0];
                }else if(size == 2){
                    return wordList_two[1];
                }else if(size == 3){
                    return wordList_two[2];
                }
            case 1:
                if (size == 1){
                    return wordList_one[0];
                }else if(size == 2){
                    return wordList_one[1];
                }else if(size == 3){
                    return wordList_one[2];
                }
                default:
                    return -1;
        }
    }

    public static ViewGroup.LayoutParams setViewMargin(Context mContext, View view, boolean isDp, int left, int right, int top, int bottom) {
        if (view == null) {
            return null;
        }

        int leftPx = left;
        int rightPx = right;
        int topPx = top;
        int bottomPx = bottom;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = null;
        //获取view的margin设置参数
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            //不存在时创建一个新的参数
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }

        //根据DP与PX转换计算值
        if (isDp) {
            leftPx = ConvertDpAndPx.Dp2Px(mContext,left);
            rightPx = ConvertDpAndPx.Dp2Px(mContext,right);
            topPx = ConvertDpAndPx.Dp2Px(mContext,top);
            bottomPx = ConvertDpAndPx.Dp2Px(mContext,bottom);
        }
        //设置margin
        marginParams.setMargins(leftPx, topPx, rightPx, bottomPx);
        view.setLayoutParams(marginParams);
        return marginParams;
    }
    public MediaPlayer createLocalMp3(){
        /**
         * 创建音频文件的方法：
         * 1、播放资源目录的文件：MediaPlayer.create(MainActivity.this,R.raw.beatit);//播放res/raw 资源目录下的MP3文件
         * 2:播放sdcard卡的文件：mediaPlayer=new MediaPlayer();
         *   mediaPlayer.setDataSource("/sdcard/beatit.mp3");//前提是sdcard卡要先导入音频文件
         */
        MediaPlayer mp=MediaPlayer.create(getActivity(), R.raw.success);
        mp.stop();
        return mp;
    }
    /**
     * 自动显示成就信息
     * 提示内容 分为3段
     * */
    private void ShowAchievementDialog(int textId){
        MediaPlayer mediaPlayer = createLocalMp3();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();//释放音频资源
            }
        });
        try {
            mediaPlayer.prepare();
            //开始播放音频
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog_auto = new AlertAutoDialog(mActivity, R.style.CustomDialog);
        Window window = dialog_auto.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.dialog_top_animation); // 添加动画
        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialog_auto.getWindow().getAttributes();
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width  = metrics.widthPixels;
        params.width = width;
        params.height = height;
        dialog_auto.getWindow().setAttributes(params);
        TextView tv_notice = (TextView) dialog_auto.findViewById(R.id.tv_notice);
        ImageView iv_bg = (ImageView) dialog_auto.findViewById(R.id.iv_bg);
        isShowed.clear();
        if(textId!=-1) {

               tv_notice.setText(textId);
               dialog_auto.setCanceledOnTouchOutside(true);
            if(dialog_auto!=null ) {

                dialog_auto.show();
                isShowed.add(textId);
                SharedPreferencesUtil.save(getActivity(), "autoshow", "autoshow", isShowed);

                //设置弹出框背景
                dialog_auto.setheadTv(true,head_text_id);
                dialog_auto.setheadImage(head_image_id);

                //旋转动画
//                Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.auto_rotate);
//                rotate.setFillAfter(true);
//                iv_bg.setAnimation(rotate);
                }
            }
    }
    /**
     * 更新宠物段位试图
     * */
    private void UpdateView(boolean isReach,ImageView iv){
        if(isReach)
            iv.setImageResource(R.drawable.achievemented);
        else
            iv.setImageResource(R.drawable.achievement);
    }
    @Override
    protected boolean isShowLeftIcon() {
        return false;
    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.achieve_view, container,
                true);
        initView(rootView);
        initData();
        return rootView;
    }

    private void initView(View rootView) {
        mActivity = getActivity();
        dan_recyclerview = (RecyclerView) rootView.findViewById(R.id.dan_recyclerview);
        recycleView = (RecyclerView) rootView.findViewById(R.id.id_recyclerview);
        tv_achieve_update = (TextView) rootView.findViewById(R.id.tv_achieve_update);
        dan_name = (TextView) rootView.findViewById(R.id.dan_name);
        ex_notice = (TextView) rootView.findViewById(R.id.ex_notice);
        StaggeredGridLayoutManager stagger = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        StaggeredGridLayoutManager stagger_dan = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recycleView.setLayoutManager(stagger);
        dan_recyclerview.setLayoutManager(stagger_dan);
    }

    private void Dimess() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing())
                    ApiHttpCilent.loading.dismiss();
            }
        });
    }

    private String GetUserId() {
        LoginBeanSave loginBeanSave = (LoginBeanSave) SharedPreferencesUtil.get(getActivity(), "login", "login");
        return loginBeanSave.getUserId();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if(!Utils.isNetworkConnected(getActivity()))
                return;
            GetAchieveData();
            GetPetDan();
//            Gson gson = new Gson();
//            mPetDanBaseBean = gson.fromJson(getJson("json.txt"),PetDanBaseBean.class);
//            GetDanSuccess();
        }
    }
    private void initData() {

        Bundle bundle = getArguments();
        userid = (String) bundle.get("userid");
        //获取宠物信息
        if (StringUtil.isEmpty(userid))
            userid = GetUserId();
        mApiCallUtil = ApiCallUtil.getInstant(getActivity());
        achievementAdapter = new AchievementAdapter(list, mActivity);
        recycleView.setAdapter(achievementAdapter);

        mSegmentAdapter = new SegmentAdapter(mListResultBean, mActivity);
        dan_recyclerview.setAdapter(mSegmentAdapter);
        //宠物成就
        GetAchieveData();
        //宠物段位
        GetPetDan();


    }
    public String getJson(String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = mActivity.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName),"utf-8"));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    /**
     * 获取宠物段位信息
     * */
    private void GetPetDan(){
        ApiHttpCilent.CreateLoading(mActivity);

        mApiCallUtil.GetPetDan(userid).enqueue(new Callback<PetDanBaseBean>() {
            @Override
            public void onResponse(Call<PetDanBaseBean> call, Response<PetDanBaseBean> response) {
                Dimess();
                mPetDanBaseBean = response.body();
                if (mPetDanBaseBean == null)
                    return;
                Message message = Message.obtain();
                if ("1".equals(mPetDanBaseBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS_TWO;
                } else {
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = mPetDanBaseBean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<PetDanBaseBean> call, Throwable t) {
                Dimess();
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
            }
        });
    }
    private void GetAchieveData() {
        ApiHttpCilent.CreateLoading(mActivity);

        mApiCallUtil.GetAchievement(userid).enqueue(new Callback<AcheveBaseBean>() {
            @Override
            public void onResponse(Call<AcheveBaseBean> call, Response<AcheveBaseBean> response) {
                Dimess();
                Message message = Message.obtain();
                acheveBaseBean = response.body();
                if (acheveBaseBean == null) {
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = "数据错误,请稍后重试";
                    handler.sendMessage(message);
                    return;
                }
                if ("1".equals(acheveBaseBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS;
                } else {
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = acheveBaseBean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<AcheveBaseBean> call, Throwable t) {
                Dimess();
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
            }
        });
    }






    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void getNetData() {

    }

    @Override
    protected void setViewListener() {
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
        return "成就";
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

