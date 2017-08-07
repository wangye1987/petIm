package com.weeho.petim.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.weeho.petim.R;

/**
 * Created by wangkui on 2017/6/6.
 */

public class TakePhotosDialog extends Dialog {

    public TakePhotosDialog(Context context) {
        super(context);
        this.show();
    }

    public TakePhotosDialog(Context context, int theme) {
        super(context, theme);
        this.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_takephotos);
    }


}
