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
import java.util.List;

import com.weeho.petim.R;
import com.weeho.petim.modle.AcheveBaseBean;
import com.weeho.petim.modle.BadHabitDicBean;

/**
 * Created by wangkui on 2017/4/28.
 */

public class AddIndoorAdapter extends RecyclerView.Adapter<AddIndoorAdapter.ViewHolder>{
    //定义成就相关信息
    ArrayList<BadHabitDicBean.ResultBean.DataBean.IndoorBean> listindoor;
    private View view;
    private AddIndoorAdapter.ViewHolder viewHolder;
    private Activity mActivity;
    public AddIndoorAdapter(ArrayList<BadHabitDicBean.ResultBean.DataBean.IndoorBean> listindoor, Activity mActivity) {
        this.listindoor = listindoor;
        this.mActivity = mActivity;
    }

    @Override
    public AddIndoorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achieve_item,parent,false);
        viewHolder = new AddIndoorAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AddIndoorAdapter.ViewHolder holder, final int position) {
         BadHabitDicBean.ResultBean.DataBean.IndoorBean coorec = listindoor.get(position);
        holder.tv.setText(StringUtil.isEmpty(coorec.getName())?"":coorec.getName());
        if(coorec.getCheck()==0){
            holder.iv_check.setVisibility(View.VISIBLE);
        }else{
            holder.iv_check.setVisibility(View.GONE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listindoor.get(position).getCheck()==0){
                    listindoor.get(position).setCheck(1);
                    holder.iv_check.setVisibility(View.GONE);
                }else{
                    listindoor.get(position).setCheck(0);
                    holder.iv_check.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listindoor.size();
    }

    static class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView iv;
        ImageView iv_check;
        TextView tv;
        ViewHolder(View view){
            super(view);
            iv = (ImageView) view.findViewById(R.id.iv_image);
            iv_check = (ImageView) view.findViewById(R.id.iv_check);
            tv = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
