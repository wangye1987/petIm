package com.weeho.petim.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weeho.petim.lib.utils.StringUtil;
import com.weeho.petim.lib.view.ViewUtil;

import com.weeho.petim.R;


/**
 * Created by wangkui on 2017/5/10.
 */

public class AlertDialogCustom {
    Activity mcontext;
    private UpdateOrNot updateOrNot;
    private View layout;
    private LinearLayout linear_close;
    private TextView hehe_updatelater;
    private TextView tv_ts;
    private TextView hehe_updatenow;
    public ProgressBar pb;
    //回调接口
    public interface UpdateOrNot{
        void setResult(int modle);
    }
    /**
     * 选择更新app
     * */
    public MyDialog Updategrade(Activity mcontext, String updateNotice, final UpdateOrNot updateOrNot){
        this.mcontext = mcontext;
        this.updateOrNot = updateOrNot;
        LayoutInflater inflater = (LayoutInflater)
                mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.upgrade_app,null);
        //关闭按钮
        linear_close = (LinearLayout) layout.findViewById(R.id.linear_close);
        //取消更新
        hehe_updatelater = (TextView) layout.findViewById(R.id.hehe_updatelater);
        //更新提示文本
        tv_ts = (TextView) layout.findViewById(R.id.tv_ts);
        pb = (ProgressBar) layout.findViewById(R.id.pb);
        //更新下载app
        hehe_updatenow = (TextView) layout.findViewById(R.id.hehe_updatenow);
        tv_ts.setText(StringUtil.isEmpty(updateNotice)?"":updateNotice);
        builders = new MyDialog(mcontext,0,0, layout,R.style.dialog);
        builders.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
                {
                    return true;
                }
                return false;
            }
        });
        //不能点击外面消除
        builders.setCanceledOnTouchOutside(false);
        hehe_updatelater.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateOrNot.setResult(1);
            }
        });
        hehe_updatenow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateOrNot.setResult(2);
            }
        });
        Show();
        return builders;
    }
    /*
	 * 显示当前对话框
	 * */
    public void Show(){
        if(builders!=null && !builders.isShowing()){
            builders.show();
        }
    }
    public class MyDialog extends Dialog {

        private static final int default_width = 160; //默认宽度
        private static final int default_height = 120;//默认高度
        public MyDialog(Context context, int width, int height, View layout, int style) {
            super(context, style);
            setContentView(layout);
            Window window = getWindow();
//	        window.setWindowAnimations(R.anim.pop_buttom);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            float widthdp = window.getWindowManager().getDefaultDisplay().getWidth();
            params.width  = (int) (widthdp- ViewUtil.dip2px(context, 80));
            window.setWindowAnimations(R.style.mystyle);  //添加动画
            window.setAttributes(params);
        }
    }
    public  MyDialog builders;
    public MyDialog getInstance(Context mContext){
        builders  = null;
        if(builders == null){
            builders = new MyDialog(mContext,0,0,layout, R.style.dialog);
        }
        return builders;
    }
    /*
         * 显示当前对话框
         * */
    public void Demiss(){
        if(builders!=null){
            builders.dismiss();
        }
    }
}
