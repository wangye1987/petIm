package com.weeho.petim.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weeho.petim.lib.view.GifSupportView;

import com.weeho.petim.R;


public class LoadingView {

	private Activity mContext;

	private LayoutInflater mInflater;// 填充对话框布局

	private View mView;// 将xml转成view

	private LinearLayout mRollLayout;

	private ImageView mRollImageView;

	private Animation mRollAnimation;// 加载的动画
	private Animation mRollAnimation_loading;// 加载的动画

	public Dialog mLoadingDialog;
	

	private ImageView loading_dialog_view_image;

	public LoadingView(Context mContext) {
		this(mContext, null);
		initLoadingDialog("");
	}

	public LoadingView(Context mContext, CharSequence loadingText) {
		super();
		this.mContext = (Activity) mContext;
		initLoadingDialog(loadingText);
	}

	public LoadingView(Context mContext, int resId) {
		super();
		this.mContext = (Activity) mContext;
		initLoadingDialog(mContext.getString(resId));
	}

	public void initLoadingDialog(CharSequence loaddingText) {
		mInflater = LayoutInflater.from(mContext);
		mView = mInflater.inflate(R.layout.loading_dialog, null);
		mRollLayout = (LinearLayout) mView
				.findViewById(R.id.loading_dialog_root);// 加载布局
		mRollImageView = (ImageView) mView
				.findViewById(R.id.loading_dialog_img);
		if (TextUtils.isEmpty(loaddingText))
			loaddingText = "努力加载中...";
		((TextView) mView.findViewById(R.id.loading_dialog_text))
				.setText(loaddingText);
		GifSupportView gifView = (GifSupportView) mView
				.findViewById(R.id.loading_dialog_view);
		loading_dialog_view_image = (ImageView) mView
				.findViewById(R.id.loading_dialog_view_image);
		gifView.setMovieResource(R.raw.loading);
		mRollAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.loading_dialog_anim);
		if(mContext.getParent() != null){
			mLoadingDialog = new Dialog(mContext.getParent(), R.style.loading_dialog);// 创建自定义样式dialog
		}else{
			mLoadingDialog = new Dialog(mContext, R.style.loading_dialog);// 创建自定义样式dialog
		}

		mLoadingDialog.setCancelable(true);// 不可点击返回键取消
		mLoadingDialog.setContentView(mRollLayout,
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

	};

	public void show() {
		// 使用ImageView显示动画
		mRollImageView.startAnimation(mRollAnimation);
		if(!( mContext).isFinishing())
		{
			mLoadingDialog.show();
		}
	}

	public void dismiss() {
		if (mLoadingDialog != null ) {
			if(mLoadingDialog.isShowing() && mContext !=null){
				mLoadingDialog.dismiss();
			}
			mLoadingDialog = null;
		}
	}

	public boolean isShowing() {
		return mLoadingDialog != null ? mLoadingDialog.isShowing() : false;
	}
}
