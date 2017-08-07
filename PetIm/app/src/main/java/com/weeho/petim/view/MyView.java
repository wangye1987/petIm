package com.weeho.petim.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import okhttp3.MultipartBody;

import static com.weeho.petim.R.styleable.View;

/**
 * Created by wangkui on 2017/5/19.
 */

public class MyView extends View {

    private int width;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private int  getSize(int size,int MeasureSpecs){
        int mySize = size;
        int mode = MeasureSpec.getMode(MeasureSpecs);
        int modeSize = MeasureSpec.getSize(MeasureSpecs);
        switch (mode){
            case MeasureSpec.AT_MOST:
                mySize = modeSize;
                break;
            case MeasureSpec.EXACTLY:
                 //固定大小
                mySize = size;
                break;
            case MeasureSpec.UNSPECIFIED:
                mySize = size;
                break;
        }
             return mySize;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getSize(300,widthMeasureSpec);
        int height = getSize(300,heightMeasureSpec);

        if(width < height){
            width = height;
        }else{
            height = width;
        }
        setMeasuredDimension(width,height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //半径
        int r = width / 2;//也可以是getMeasuredHeight()/2,本例中我们已经将宽高设置相等了
        //圆心的横坐标为当前的View的左边起始位置+半径
        int centerX = getLeft() + r;
        //圆心的纵坐标为当前的View的顶部起始位置+半径
        int centerY = getTop() + r;

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        //开始绘制
        canvas.drawCircle(centerX, centerY, r, paint);
//        int r = getMeasuredHeight()/2;
//       int  x = getLeft()+r;
//       int y = getTop()+r;
//        Paint mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        mPaint.setColor(Color.BLUE);
//        canvas.drawCircle(x,y,r,mPaint);
    }
}
