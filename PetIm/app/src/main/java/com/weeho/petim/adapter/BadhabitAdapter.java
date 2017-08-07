package com.weeho.petim.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weeho.petim.lib.utils.StringUtil;

import java.util.ArrayList;

import com.weeho.petim.R;
import com.weeho.petim.modle.BadHabitBean;

/**
 * Created by wangkui on 2017/4/28.
 */

public class BadhabitAdapter  extends RecyclerView.Adapter<BadBabitAdapter.ViewHolder>{
    //定义成就相关信息
    ArrayList<BadHabitBean.ResultBean.DataBean.OutdoorBean> list_outdoor;
    private View view;
    private BadBabitAdapter.ViewHolder viewHolder;
    private Activity mActivity;


    public BadhabitAdapter(ArrayList<BadHabitBean.ResultBean.DataBean.OutdoorBean> list_outdoor, Activity mActivity) {
        this.list_outdoor = list_outdoor;
        this.mActivity = mActivity;
    }
    public void SetList(ArrayList<BadHabitBean.ResultBean.DataBean.OutdoorBean> list_outdoor){
        this.list_outdoor = list_outdoor;
        notifyDataSetChanged();
    }
    @Override
    public BadBabitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achieve_item,parent,false);
        viewHolder = new BadBabitAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BadBabitAdapter.ViewHolder holder, int position) {
        BadHabitBean.ResultBean.DataBean.OutdoorBean outdoorBean = list_outdoor.get(position);
        holder.tv.setText(StringUtil.isEmpty(outdoorBean.getName())?"":outdoorBean.getName());
//        int drawable = R.drawable.medal;
//        if(!StringUtil.isEmpty(coorec.getIscorrect())){
//        if("0".equals(coorec.getIscorrect())){
//            drawable = R.drawable.medaled;
//        }else if("1".equals(coorec.getIscorrect())){
//            drawable = R.drawable.medal;
//        }
//        }
//        Glide.with(mActivity)
//                .load(coorec.getCourl())
//                .centerCrop()
//                .placeholder(drawable)
//                .crossFade()
//                .into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return list_outdoor.size();
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

