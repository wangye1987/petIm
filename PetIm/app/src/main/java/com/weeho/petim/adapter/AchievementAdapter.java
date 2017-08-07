package com.weeho.petim.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weeho.petim.lib.base.BaseListAdapter;
import com.weeho.petim.lib.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import com.weeho.petim.R;
import com.weeho.petim.modle.AcheveBaseBean;
import com.weeho.petim.modle.AchievementBean;

/**
 * Created by wangkui on 2017/4/19.
 */

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder>{
    //定义成就相关信息
    ArrayList<AcheveBaseBean.ResultBean.DataBean>  list;
    private View view;
    private ViewHolder viewHolder;
    private Activity mActivity;
    public AchievementAdapter(ArrayList<AcheveBaseBean.ResultBean.DataBean> list, Activity mActivity) {
        this.list = list;
        this.mActivity = mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achieve_item,parent,false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AcheveBaseBean.ResultBean.DataBean coorec = list.get(position);
        holder.tv.setText(StringUtil.isEmpty(coorec.getName())?"":coorec.getName());
        holder.iv.setImageResource(R.drawable.medaled);
    }

    public void setData(ArrayList<AcheveBaseBean.ResultBean.DataBean>  list){
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
