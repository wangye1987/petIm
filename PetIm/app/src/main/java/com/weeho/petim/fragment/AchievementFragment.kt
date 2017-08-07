/*
package com.weeho.petim.fragment

import android.animation.AnimatorSet
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Message
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.DisplayMetrics
import android.view.Display
import android.view.GestureDetector
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.google.gson.Gson
import com.weeho.petim.R
import com.weeho.petim.RetorfitWapper.ApiCallUtil
import com.weeho.petim.adapter.AchievementAdapter
import com.weeho.petim.adapter.SegmentAdapter
import com.weeho.petim.base.BaseFragment
import com.weeho.petim.controller.MainActivity
import com.weeho.petim.hxim.Constant
import com.weeho.petim.lib.utils.SharedPreferencesUtil
import com.weeho.petim.lib.utils.StringUtil
import com.weeho.petim.lib.utils.WeakHandler
import com.weeho.petim.modle.AcheveBaseBean
import com.weeho.petim.modle.AcheveBaseBean.ResultBean
import com.weeho.petim.modle.LoginBeanSave
import com.weeho.petim.modle.PetDanBaseBean
import com.weeho.petim.modle.PetDanBaseBean.ResultBean.*
import com.weeho.petim.network.ApiHttpCilent
import com.weeho.petim.network.Utils
import com.weeho.petim.util.ConstantsUtil
import com.weeho.petim.util.ConvertDpAndPx
import com.weeho.petim.util.ToastUtil
import com.weeho.petim.view.AlertAutoDialog
import com.weeho.petim.view.AlertDanDialog
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList

import butterknife.Bind
import butterknife.ButterKnife
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


*/
/**
 * Created by wangkui on 2017/4/18.
 * 成就界面
 *//*


class AchievementFragment : BaseFragment() {

    private var recycleView: RecyclerView? = null
    private var dan_recyclerview: RecyclerView? = null
    private var mActivity: FragmentActivity? = null
    private var userid: String? = null
    private var acheveBaseBean: AcheveBaseBean? = null
    private val handler = MyHandler(this)
    private var ex_notice: TextView? = null
    private var tv_achieve_update: TextView? = null
    private var achievementAdapter: AchievementAdapter? = null
    internal var list = ArrayList<ResultBean.DataBean>()
    private var mApiCallUtil: ApiCallUtil? = null
    private var mPetDanBaseBean: PetDanBaseBean? = null
    private var mSegmentAdapter: SegmentAdapter? = null
    //段位集合
    private var mListResultBean: List<ListBean>? = ArrayList()
    private var dan_name: TextView? = null
    // 自动弹出框内容标题
    private var auto_name: Int = 0
    private val auto_title_name: String? = null
    private val mAuto_complete = ArrayList<String>()

    private val wordList_one = intArrayOf(R.string.achieve_one_one, R.string.achieve_one_two, R.string.achieve_one_three)
    private val wordList_two = intArrayOf(R.string.achieve_two_one, R.string.achieve_two_two, R.string.achieve_two_three)
    private val wordList_three = intArrayOf(R.string.achieve_three_one, R.string.achieve_three_two, R.string.achieve_three_three)
    private val wordList_four = intArrayOf(R.string.achieve_four_one, R.string.achieve_four_two, R.string.achieve_four_three)
    private val wordList_five = intArrayOf(R.string.achieve_five_one, R.string.achieve_five_two, R.string.achieve_five_three)
    private val wordList_six = intArrayOf(R.string.achieve_six_one, R.string.achieve_six_two, R.string.achieve_six_three)

    private val head_image = intArrayOf(R.drawable.qt, R.drawable.by, R.drawable.hj, R.drawable.bj, R.drawable.zs, R.drawable.wz)
    private var textId: Int = 0
    //图片下面标题
    private val headText = intArrayOf(R.string.achieve_qt, R.string.achieve_by, R.string.achieve_hj, R.string.achieve_bj, R.string.achieve_zs, R.string.achieve_wz)


    //存贮标志位 如果已经弹出过 就不需要在弹出
    private var isShowed: MutableList<Int> = ArrayList()
    private var head_image_id: Int = 0
    private var head_text_id: Int = 0
    private var dialog_auto: AlertAutoDialog? = null

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

    inner class MyHandler(reference: AchievementFragment) : WeakHandler<AchievementFragment>(reference) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                ConstantsUtil.CONTENT_SUCCESS -> if (acheveBaseBean != null && acheveBaseBean!!.result != null && acheveBaseBean!!.result.data != null) {
                    list.clear()
                    list = acheveBaseBean!!.result.data as ArrayList<ResultBean.DataBean>
                    val size = list.size
                    if (size > 0) {
                        tv_achieve_update!!.text = "(已改正" + size + "项恶习)"
                        achievementAdapter!!.setData(list)
                        achievementAdapter!!.notifyDataSetChanged()
                        ex_notice!!.visibility = View.GONE
                        recycleView!!.visibility = View.VISIBLE
                    } else {
                        ex_notice!!.visibility = View.VISIBLE
                        recycleView!!.visibility = View.GONE
                    }
                }
                ConstantsUtil.CONTENT_SUCCESS_TWO ->
                    //获取宠物段位成功
                    GetDanSuccess()
                ConstantsUtil.CONTENT_FAIL -> {
                    //
                    val error = msg.obj as String
                    if (StringUtil.isEmpty(error))
                        ToastUtil.showToast(reference.mActivity, "获取失败,请稍后重试")
                    else
                        ToastUtil.showToast(reference.mActivity, error)
                }
            }
        }
    }

    */
/**
     * 获取等级名称
     *//*

    private var mSegmentName = ""
    private val reach = ArrayList<String>()
    private fun getDanName(mListResultBean: List<ListBean>) {
        reach.clear()
        val size = mListResultBean.size
        for (i in 0..size - 1) {
            if (mListResultBean[i].isIsReach) {
                reach.add(mListResultBean[i].name)
            }
            val mActivityList = mListResultBean[i].activity
            for (mActivity in mActivityList) {
                if (mActivity.isIsReach) {
                    auto_name = i + 1
                }
            }
        }
    }

    private fun GetTitleName() {
        if (reach.contains("王者")) {
            mSegmentName = "王者"
            return
        } else if (reach.contains("钻石")) {
            mSegmentName = "钻石"
            return
        } else if (reach.contains("铂金")) {
            mSegmentName = "铂金"
            return
        } else if (reach.contains("黄金")) {
            mSegmentName = "黄金"
            return
        } else if (reach.contains("白银")) {
            mSegmentName = "白银"
            return
        } else if (reach.contains("青铜")) {
            mSegmentName = "青铜"
            return
        }
    }

    private fun GetDanSuccess() {
        if (mPetDanBaseBean != null && mPetDanBaseBean!!.result != null && mPetDanBaseBean!!.result.list != null) {
            mListResultBean = mPetDanBaseBean!!.result.list
            if (mListResultBean != null) {
                getDanName(mListResultBean)
                GetTitleName()
                //自动弹出提示框
                GetAutoNotice(mListResultBean)
                if (!StringUtil.isEmpty(mSegmentName))
                    dan_name!!.text = "(已达成" + mSegmentName + "等级)"
                mSegmentAdapter!!.setData(mListResultBean as ArrayList<ListBean>?)
                mSegmentAdapter!!.notifyDataSetChanged()
            }
        }
    }


    */
/**
     * 获取提示的文本信息
     *//*

    private fun GetAutoNotice(mListResultBean: List<ListBean>) {
        val size = mListResultBean.size
        mAuto_complete.clear()
        for (i in 0..size - 1) {
            val mActvity = mListResultBean[i].activity
            //其他阶段
            if (!StringUtil.isEmpty(mSegmentName)) {
                //如果在第几阶段 直接取当前阶段完成的数据 忽略其他阶段的完成项目
                if (i + 1 == auto_name) {
                    if (mActvity != null) {
                        for (mlist in mActvity) {
                            if (mlist.isIsReach) {
                                mAuto_complete.add(mlist.activityName + "_" + mActvity.size)
                            }
                        }
                    }
                }
            } else {
                //青铜阶段
                if (mActvity != null) {
                    for (mlist in mActvity) {
                        if (mlist.isIsReach) {
                            mAuto_complete.add(mlist.activityName + "_" + mActvity.size)
                        }
                    }
                }
            }
        }
        //显示当前的文本
        textId = ReturnAutoNotice(auto_name)
        if (textId != -1) {
            val mObject = SharedPreferencesUtil.get(activity, "autoshow", "autoshow")
            if (mObject != null) {
                isShowed = mObject as List<Int>
            }
            if (!isShowed.contains(textId)) {
                ShowAchievementDialog(textId)
            }
        }
    }

    private fun ReturnAutoNotice(index: Int): Int {
        if (mAuto_complete.size > 0) {
            if (mAuto_complete.size == 1) {
                //完成一项
                return ReturnText(index, 1)
            } else if (mAuto_complete.size == Integer.parseInt(mAuto_complete[0].split("_".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]) - 1) {
                //差一项完成
                return ReturnText(index, 2)
            } else if (mAuto_complete.size == Integer.parseInt(mAuto_complete[0].split("_".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1])) {
                //全部完成
                return ReturnText(index, 3)
            }
        }
        return -1
    }

    private fun ReturnText(index: Int, size: Int): Int {
        head_image_id = head_image[index - 1]
        head_text_id = headText[index - 1]
        when (index) {
            6 -> {
                if (size == 1) {
                    return wordList_six[0]
                } else if (size == 2) {
                    return wordList_six[1]
                } else if (size == 3) {
                    return wordList_six[2]
                }
                if (size == 1) {
                    return wordList_five[0]
                } else if (size == 2) {
                    return wordList_five[1]
                } else if (size == 3) {
                    return wordList_five[2]
                }
                if (size == 1) {
                    return wordList_four[0]
                } else if (size == 2) {
                    return wordList_four[1]
                } else if (size == 3) {
                    return wordList_four[2]
                }
                if (size == 1) {
                    return wordList_three[0]
                } else if (size == 2) {
                    return wordList_three[1]
                } else if (size == 3) {
                    return wordList_three[2]
                }
                if (size == 1) {
                    return wordList_two[0]
                } else if (size == 2) {
                    return wordList_two[1]
                } else if (size == 3) {
                    return wordList_two[2]
                }
                if (size == 1) {
                    return wordList_one[0]
                } else if (size == 2) {
                    return wordList_one[1]
                } else if (size == 3) {
                    return wordList_one[2]
                }
                return -1
            }
            5 -> {
                if (size == 1) {
                    return wordList_five[0]
                } else if (size == 2) {
                    return wordList_five[1]
                } else if (size == 3) {
                    return wordList_five[2]
                }
                if (size == 1) {
                    return wordList_four[0]
                } else if (size == 2) {
                    return wordList_four[1]
                } else if (size == 3) {
                    return wordList_four[2]
                }
                if (size == 1) {
                    return wordList_three[0]
                } else if (size == 2) {
                    return wordList_three[1]
                } else if (size == 3) {
                    return wordList_three[2]
                }
                if (size == 1) {
                    return wordList_two[0]
                } else if (size == 2) {
                    return wordList_two[1]
                } else if (size == 3) {
                    return wordList_two[2]
                }
                if (size == 1) {
                    return wordList_one[0]
                } else if (size == 2) {
                    return wordList_one[1]
                } else if (size == 3) {
                    return wordList_one[2]
                }
                return -1
            }
            4 -> {
                if (size == 1) {
                    return wordList_four[0]
                } else if (size == 2) {
                    return wordList_four[1]
                } else if (size == 3) {
                    return wordList_four[2]
                }
                if (size == 1) {
                    return wordList_three[0]
                } else if (size == 2) {
                    return wordList_three[1]
                } else if (size == 3) {
                    return wordList_three[2]
                }
                if (size == 1) {
                    return wordList_two[0]
                } else if (size == 2) {
                    return wordList_two[1]
                } else if (size == 3) {
                    return wordList_two[2]
                }
                if (size == 1) {
                    return wordList_one[0]
                } else if (size == 2) {
                    return wordList_one[1]
                } else if (size == 3) {
                    return wordList_one[2]
                }
                return -1
            }
            3 -> {
                if (size == 1) {
                    return wordList_three[0]
                } else if (size == 2) {
                    return wordList_three[1]
                } else if (size == 3) {
                    return wordList_three[2]
                }
                if (size == 1) {
                    return wordList_two[0]
                } else if (size == 2) {
                    return wordList_two[1]
                } else if (size == 3) {
                    return wordList_two[2]
                }
                if (size == 1) {
                    return wordList_one[0]
                } else if (size == 2) {
                    return wordList_one[1]
                } else if (size == 3) {
                    return wordList_one[2]
                }
                return -1
            }
            2 -> {
                if (size == 1) {
                    return wordList_two[0]
                } else if (size == 2) {
                    return wordList_two[1]
                } else if (size == 3) {
                    return wordList_two[2]
                }
                if (size == 1) {
                    return wordList_one[0]
                } else if (size == 2) {
                    return wordList_one[1]
                } else if (size == 3) {
                    return wordList_one[2]
                }
                return -1
            }
            1 -> {
                if (size == 1) {
                    return wordList_one[0]
                } else if (size == 2) {
                    return wordList_one[1]
                } else if (size == 3) {
                    return wordList_one[2]
                }
                return -1
            }
            else -> return -1
        }
    }

    fun createLocalMp3(): MediaPlayer {
        */
/**
         * 创建音频文件的方法：
         * 1、播放资源目录的文件：MediaPlayer.create(MainActivity.this,R.raw.beatit);//播放res/raw 资源目录下的MP3文件
         * 2:播放sdcard卡的文件：mediaPlayer=new MediaPlayer();
         * mediaPlayer.setDataSource("/sdcard/beatit.mp3");//前提是sdcard卡要先导入音频文件
         *//*

        val mp = MediaPlayer.create(activity, R.raw.success)
        mp.stop()
        return mp
    }

    */
/**
     * 自动显示成就信息
     * 提示内容 分为3段
     *//*

    private fun ShowAchievementDialog(textId: Int) {
        val mediaPlayer = createLocalMp3()
        mediaPlayer.setOnCompletionListener { mp ->
            mp.release()//释放音频资源
        }
        try {
            mediaPlayer.prepare()
            //开始播放音频
            mediaPlayer.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        dialog_auto = AlertAutoDialog(mActivity, R.style.CustomDialog)
        val window = dialog_auto!!.window
        window!!.setGravity(Gravity.CENTER)
        window.setWindowAnimations(R.style.dialog_top_animation) // 添加动画
        val windowManager = mActivity!!.windowManager
        val display = windowManager.defaultDisplay
        val params = dialog_auto!!.window!!.attributes
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val height = metrics.heightPixels
        val width = metrics.widthPixels
        params.width = width
        params.height = height
        dialog_auto!!.window!!.attributes = params
        val tv_notice = dialog_auto!!.findViewById(R.id.tv_notice) as TextView
        val iv_bg = dialog_auto!!.findViewById(R.id.iv_bg) as ImageView
        isShowed.clear()
        if (textId != -1) {

            tv_notice.setText(textId)
            dialog_auto!!.setCanceledOnTouchOutside(true)
            if (dialog_auto != null) {

                dialog_auto!!.show()
                isShowed.add(textId)
                SharedPreferencesUtil.save(activity, "autoshow", "autoshow", isShowed)

                //设置弹出框背景
                dialog_auto!!.setheadTv(true, head_text_id)
                dialog_auto!!.setheadImage(head_image_id)

                //旋转动画
                //                Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.auto_rotate);
                //                rotate.setFillAfter(true);
                //                iv_bg.setAnimation(rotate);
            }
        }
    }

    */
/**
     * 更新宠物段位试图
     *//*

    private fun UpdateView(isReach: Boolean, iv: ImageView) {
        if (isReach)
            iv.setImageResource(R.drawable.achievemented)
        else
            iv.setImageResource(R.drawable.achievement)
    }

    override fun isShowLeftIcon(): Boolean {
        return false
    }

    override fun setContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View {
        val rootView = inflater.inflate(R.layout.achieve_view, container,
                true)
        initView(rootView)
        initData()
        return rootView
    }

    private fun initView(rootView: View) {
        mActivity = activity
        dan_recyclerview = rootView.findViewById(R.id.dan_recyclerview) as RecyclerView
        recycleView = rootView.findViewById(R.id.id_recyclerview) as RecyclerView
        tv_achieve_update = rootView.findViewById(R.id.tv_achieve_update) as TextView
        dan_name = rootView.findViewById(R.id.dan_name) as TextView
        ex_notice = rootView.findViewById(R.id.ex_notice) as TextView
        val stagger = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        val stagger_dan = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recycleView!!.layoutManager = stagger
        dan_recyclerview!!.layoutManager = stagger_dan
    }

    private fun Dimess() {
        activity.runOnUiThread {
            if (ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing)
                ApiHttpCilent.loading.dismiss()
        }
    }

    private fun GetUserId(): String {
        val loginBeanSave = SharedPreferencesUtil.get(activity, "login", "login") as LoginBeanSave
        return loginBeanSave.userId
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            if (!Utils.isNetworkConnected(activity))
                return
            GetAchieveData()
            GetPetDan()
            //            Gson gson = new Gson();
            //            mPetDanBaseBean = gson.fromJson(getJson("json.txt"),PetDanBaseBean.class);
            //            GetDanSuccess();
        }
    }

    private fun initData() {

        val bundle = arguments
        userid = bundle.get("userid") as String
        //获取宠物信息
        if (StringUtil.isEmpty(userid))
            userid = GetUserId()
        mApiCallUtil = ApiCallUtil.getInstant(activity)
        achievementAdapter = AchievementAdapter(list, mActivity)
        recycleView!!.adapter = achievementAdapter

        mSegmentAdapter = SegmentAdapter(mListResultBean, mActivity)
        dan_recyclerview!!.adapter = mSegmentAdapter
        //宠物成就
        GetAchieveData()
        //宠物段位
        GetPetDan()


    }

    fun getJson(fileName: String): String {

        val stringBuilder = StringBuilder()
        try {
            val assetManager = mActivity!!.assets
            val bf = BufferedReader(InputStreamReader(
                    assetManager.open(fileName), "utf-8"))
            var line: String
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stringBuilder.toString()
    }

    */
/**
     * 获取宠物段位信息
     *//*

    private fun GetPetDan() {
        ApiHttpCilent.CreateLoading(mActivity)

        mApiCallUtil!!.GetPetDan(userid).enqueue(object : Callback<PetDanBaseBean> {
            override fun onResponse(call: Call<PetDanBaseBean>, response: Response<PetDanBaseBean>) {
                Dimess()
                mPetDanBaseBean = response.body()
                if (mPetDanBaseBean == null)
                    return
                val message = Message.obtain()
                if ("1" == mPetDanBaseBean!!.status) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS_TWO
                } else {
                    message.what = ConstantsUtil.CONTENT_FAIL// 错误
                    message.obj = mPetDanBaseBean!!.error.info
                }
                handler.sendMessage(message)
            }

            override fun onFailure(call: Call<PetDanBaseBean>, t: Throwable) {
                Dimess()
                val message = Message.obtain()
                message.what = ConstantsUtil.CONTENT_FAIL// 错误
                message.obj = "数据错误,请稍后重试"
                handler.sendMessage(message)
            }
        })
    }

    private fun GetAchieveData() {
        ApiHttpCilent.CreateLoading(mActivity)

        mApiCallUtil!!.GetAchievement(userid).enqueue(object : Callback<AcheveBaseBean> {
            override fun onResponse(call: Call<AcheveBaseBean>, response: Response<AcheveBaseBean>) {
                Dimess()
                val message = Message.obtain()
                acheveBaseBean = response.body()
                if (acheveBaseBean == null) {
                    message.what = ConstantsUtil.CONTENT_FAIL// 错误
                    message.obj = "数据错误,请稍后重试"
                    handler.sendMessage(message)
                    return
                }
                if ("1" == acheveBaseBean!!.status) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS
                } else {
                    message.what = ConstantsUtil.CONTENT_FAIL// 错误
                    message.obj = acheveBaseBean!!.error.info
                }
                handler.sendMessage(message)
            }

            override fun onFailure(call: Call<AcheveBaseBean>, t: Throwable) {
                Dimess()
                val message = Message.obtain()
                message.what = ConstantsUtil.CONTENT_FAIL// 错误
                message.obj = "数据错误,请稍后重试"
                handler.sendMessage(message)
            }
        })
    }


    override fun onResume() {
        super.onResume()
    }

    override fun getNetData() {

    }

    override fun setViewListener() {}


    override fun hasTitle(): Boolean {
        return true
    }

    override fun hasTitleIcon(): Boolean {
        return false
    }

    override fun hasDownIcon(): Boolean {
        return false
    }

    override fun reloadCallback() {

    }

    override fun setTitleName(): String {
        return "成就"
    }

    override fun setRightText(): String? {
        return null
    }

    override fun setLeftImageResource(): Int {
        return 0
    }

    override fun setMiddleImageResource(): Int {
        return 0
    }

    override fun setRightImageResource(): Int {
        return 0
    }

    companion object {

        fun setViewMargin(mContext: Context, view: View?, isDp: Boolean, left: Int, right: Int, top: Int, bottom: Int): ViewGroup.LayoutParams? {
            if (view == null) {
                return null
            }

            var leftPx = left
            var rightPx = right
            var topPx = top
            var bottomPx = bottom
            val params = view.layoutParams
            var marginParams: ViewGroup.MarginLayoutParams? = null
            //获取view的margin设置参数
            if (params is ViewGroup.MarginLayoutParams) {
                marginParams = params
            } else {
                //不存在时创建一个新的参数
                marginParams = ViewGroup.MarginLayoutParams(params)
            }

            //根据DP与PX转换计算值
            if (isDp) {
                leftPx = ConvertDpAndPx.Dp2Px(mContext, left.toFloat())
                rightPx = ConvertDpAndPx.Dp2Px(mContext, right.toFloat())
                topPx = ConvertDpAndPx.Dp2Px(mContext, top.toFloat())
                bottomPx = ConvertDpAndPx.Dp2Px(mContext, bottom.toFloat())
            }
            //设置margin
            marginParams.setMargins(leftPx, topPx, rightPx, bottomPx)
            view.layoutParams = marginParams
            return marginParams
        }
    }
}

*/
