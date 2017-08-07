package com.weeho.petim.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weeho.petim.R;
import com.weeho.petim.lib.utils.StringUtil;
import com.weeho.petim.modle.PetDanBaseBean;
import com.weeho.petim.view.AlertDanDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkui on 2017/6/9.
 */

public class SegmentAdapter extends RecyclerView.Adapter<SegmentAdapter.ViewHolder>{
    //定义段位相关信息
    List<PetDanBaseBean.ResultBean.ListBean>  list;
    private View view;
    private SegmentAdapter.ViewHolder viewHolder;
    private Activity mActivity;
    private int image[] = new int[]{R.drawable.qt,R.drawable.by,R.drawable.hj,R.drawable.bj,R.drawable.zs,R.drawable.wz};
    private int image_not[] = new int[]{R.drawable.qted,R.drawable.byed,R.drawable.hjed,R.drawable.bjed,R.drawable.zsed,R.drawable.wzed};
    private int index;
    private int drawble;
    public SegmentAdapter(List<PetDanBaseBean.ResultBean.ListBean>  list, final Activity mActivity) {
        this.list = list;
        this.mActivity = mActivity;
    }

    @Override
    public SegmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achieve_item,parent,false);
        viewHolder = new SegmentAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SegmentAdapter.ViewHolder holder, final int position) {
       final PetDanBaseBean.ResultBean.ListBean mSegment = list.get(position);
        holder.tv.setText(StringUtil.isEmpty(mSegment.getName())?"":mSegment.getName());

        if(mSegment.isIsReach()) {
            holder.iv.setImageResource(image[position]);
            drawble = image[position];
        }else{
             holder.iv.setImageResource(image_not[position]);
            drawble = image_not[position];
            }
        holder.iv.setOnClickListener(new MyClick(mActivity,list.get(position),position,drawble));
    }

    class MyClick implements View.OnClickListener{
        Activity mActivity;
        PetDanBaseBean.ResultBean.ListBean mSegment;
        int index;
        int drawble;
        MyClick(Activity mActivity,PetDanBaseBean.ResultBean.ListBean mSegment,int index,int drawble){
            this.mActivity = mActivity;
            this.mSegment = mSegment;
            this.index = index;
            this.drawble = drawble;
        }
        @Override
        public void onClick(View view) {
            ShowDialog(mActivity,mSegment,index,drawble);
        }
    }

    public void ShowDialog(Activity mActivity,PetDanBaseBean.ResultBean.ListBean mSegment,int index,int drawble) {
        //设置dialog的样式
        AlertDanDialog dialog = new AlertDanDialog(mActivity, R.style.dialog_setting);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.dialog_top_animation); // 添加动画
        dialog.show();
        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width  = metrics.widthPixels;
        params.width = width*2/3;
//        params.height = height *2/3;
        dialog.getWindow().setAttributes(params);
        LinearLayout linear_parent = (LinearLayout) dialog.findViewById(R.id.buttom_layout);
        List<PetDanBaseBean.ResultBean.ListBean.ActivityBean> mActivityList =  mSegment.getActivity();
        //设置弹出框背景
           dialog.setbg(mSegment.isIsReach());
           if(mSegment.isIsReach())
           dialog.setheadbg(image[index]);
        else
               dialog.setheadbg(image_not[index]);
        dialog.setheadTv(mSegment.isIsReach(),mSegment.getName());

        for(PetDanBaseBean.ResultBean.ListBean.ActivityBean mActivityBean:mActivityList) {
            View mView = LinearLayout.inflate(mActivity,R.layout.weeho_dan_item,null);
            TextView tv_name = (TextView) mView.findViewById(R.id.tv_name);
            ImageView tv_statue = (ImageView) mView.findViewById(R.id.tv_statue);
            tv_name.setText(StringUtil.isEmpty(mActivityBean.getActivityName())?"":mActivityBean.getActivityName());
            if(mActivityBean.isIsReach()) {
                tv_statue.setImageResource(R.drawable.zhuang);
                tv_name.setTextColor(mActivity.getResources().getColor(R.color.white));
            }else {
                tv_statue.setImageResource(R.drawable.zhuanged);
                tv_name.setTextColor(mActivity.getResources().getColor(R.color.color_7d7686));
            }
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
            lp.setMargins(0, 3, 0, 3);
            mView.setLayoutParams(lp);
            linear_parent.addView(mView);
        }
    }


    public void setData(ArrayList<PetDanBaseBean.ResultBean.ListBean>  list){
        this.list = list;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;
        ViewHolder(View view){
            super(view);
            iv = (ImageView) view.findViewById(R.id.iv_image);
            tv = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}

