package com.weeho.petim.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.weeho.petim.lib.view.ViewUtil;

import com.weeho.petim.R;

/**
 * Created by wangkui on 2017/4/28.
 */

public class CommonDialog extends Dialog{

    private static CommonDialog dialog;
    private Context mContext = null;
    private String title = null;
    private String content = null;
    private OnDialogListener mOnDialogListener = null;
    public CommonDialog(@NonNull Context context) {
        super(context);
    }

    public CommonDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected CommonDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    // dismess当前对话框
    public static void Dissmess() {
        if (dialog != null) {
            dialog.dismiss();
            dialog.cancel();
        }
    }
    public static CommonDialog makeText(Context context, String title,
                                        String content, OnDialogListener onDialogListener) {
        dialog = new CommonDialog(context, R.style.dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.mContext = context;
        dialog.title = title;
        dialog.content = content;
        dialog.setContentView(R.layout.confirm_dialog);
        View view = LinearLayout.inflate(context,R.layout.confirm_dialog,null);
        int width = ViewUtil.getDisplayWidth((Activity) context);
        int height = ViewUtil.getDisplayHeight((Activity) context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width*3/5,height/4);
        view.setLayoutParams(lp);
//		dialog.getWindow().setLayout(width*3/5,height/4);
        dialog.mOnDialogListener = onDialogListener;
        return dialog;
    }
    public void showDialog() {
//        loadViews();
//        if (isHideRightLayout) {
//            if (null != llRightText) {
//                llRightText.setVisibility(View.GONE);
//            }
//        }
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.WindowAnimation);
        WindowManager.LayoutParams wm = window.getAttributes();
        wm.gravity = Gravity.CENTER;
        wm.width = ViewUtil.getScreenWith(mContext) * 11 / 12;
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    public interface OnDialogListener {
        public static final int LEFT = 1;
        public static final int RIGHT = 0;

        public void onResult(int result, CommonDialog commonDialog, String tel);
    }
}
