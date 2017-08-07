package com.weeho.petim.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.weeho.petim.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by wangkui on 2017/5/25.
 */

public class MyClock extends View {

    private int mWidth;
    private int mHeight;
    private int mDiameter;
    private float mHourR;
    private float mMinuteR;
    private float mSecondR;

    int mRingColor;
    int mLongScaleColor;
    int mCenterPointColor;
    int mShortScaleColor;
    int mTextColor;

    int mHourColor;
    int mMinuteColor;
    int mSecondColor;

    final int H =0;
    final int M =1;
    final int S =2;

    int mHour;
    int mMinute;
    int mSecond;
    public MyClock(Context context) {
        super(context);
        intData();
    }

    public MyClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        intData();
    }

    public MyClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intData();
    }
//    int mRingColor;
//    int mLongScaleColor;
//    int mCenterPointColor;
//    int mShortScaleColor;
//    int mTextColor;
//
//    int mHourColor;
//    int mMinuteColor;
//    int mSecondColor;
    private void intData() {
        mRingColor = R.color.white;
        mLongScaleColor = R.color.white;
        mCenterPointColor = R.color.white;
        mShortScaleColor = R.color.white;
        mTextColor = R.color.white;

        mTextColor = R.color.white;

        mHourColor = R.color.white;
        mMinuteColor = R.color.white;
        mSecondColor = R.color.white;
        startClock();
    }
//    int mHour;
//    int mMinute;
//    int mSecond;
    public void setHour(int mHour){
        this.mHour = mHour;
        invalidate();
    }
    public void setMinute(int mMinute){
        this.mMinute = mMinute;
        invalidate();
    }
    public void setSecond(int mSecond){
        this.mSecond = mSecond;
        invalidate();
    }
    private void startClock(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    mHour = calendar.get(Calendar.HOUR);
                    mMinute = calendar.get(Calendar.MINUTE);
                    mSecond = calendar.get(Calendar.SECOND);
                    postInvalidate();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = measuredWidth(widthMeasureSpec);
        mHeight = measuredHeight(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);

        // 钟表的外圆直径（除去 padding ）
        mDiameter = Math.min(mWidth - getPaddingLeft() - getPaddingRight(), mHeight - getPaddingTop() - getPaddingBottom());

        Bitmap bitmap_hour = BitmapFactory.decodeResource(getResources(),R.drawable.hour);
        Bitmap bitmap_minute = BitmapFactory.decodeResource(getResources(),R.drawable.minute);
        Bitmap bitmap_seconds = BitmapFactory.decodeResource(getResources(),R.drawable.seconds);
//        mHourR = bitmap_hour.getHeight();
//        mMinuteR = bitmap_minute.getHeight();
//        mSecondR = bitmap_seconds.getHeight();
        // 时针半径外环半径的1/3
        mHourR = mDiameter /2f/3;
//        // 分针半径为外环半径的1/2
        mMinuteR = mDiameter /2f/2;
//        // 秒针半径为外环半径的1/1.5
        mSecondR = mDiameter /2f/1.5f;
    }
    private int measuredWidth(int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        } else {
            result = 500;
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measuredHeight(int heightMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        } else {
            result = 500;
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawCircle(canvas);
        drawIndicator(canvas);
//        drawScale(canvas);
    }

    /**
     * 画指针：时针，分针，秒针
     * @param canvas
     */

    private void drawIndicator(Canvas canvas) {
        // 保存图层
        canvas.save();
        canvas.translate(mWidth/2f, mHeight/2f-30);
//        Paint paint = new Paint();
//        Bitmap bitmap_hour = BitmapFactory.decodeResource(getResources(),R.drawable.hour);
//        Bitmap bitmap_minute = BitmapFactory.decodeResource(getResources(),R.drawable.minute);
//        Bitmap bitmap_seconds = BitmapFactory.decodeResource(getResources(),R.drawable.seconds);
//        Matrix mMatrix1 = new Matrix();
//        Matrix mMatrix = new Matrix();
//        mMatrix1.postRotate(6,getLeftBy(H), getTopBy(H));
//        canvas.drawBitmap(bitmap_hour,mMatrix1,paint);
//
//        canvas.drawBitmap(bitmap_minute,mMatrix,paint);
//
//        mMatrix.postRotate(6,getLeftBy(S), getTopBy(S));
//        canvas.drawBitmap(bitmap_seconds,getLeftBy(S), getTopBy(S),paint);
//        canvas.drawBitmap(bitmap_seconds,mMatrix,paint);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(3);
        paint.setColor(mHourColor);
        canvas.drawLine(0, 0, getLeftBy(H), getTopBy(H), paint);

        paint.setStrokeWidth(2);
        paint.setColor(mMinuteColor);
        canvas.drawLine(0, 0, getLeftBy(M), getTopBy(M), paint);

        paint.setStrokeWidth(1);
        paint.setColor(mSecondColor);
        canvas.drawLine(0, 0, getLeftBy(S), getTopBy(S), paint);

        // 合并图层
        canvas.restore();
    }

    private float getLeftBy(int indicator){
        float r=0f;
        float digit = 0;

        switch(indicator){
            case H:
                r = mHourR;
                // 根据分钟进行补充,每5分钟进一小格
                digit = ((mHour%12/12f*60 + mMinute/60f*5)) % 60;
                break;
            case M:
                r = mMinuteR;
                digit = mMinute;
                break;
            case S:
                r = mSecondR;
                digit = mSecond+1;
                break;
        }

        float left = (float) Math.sin(digit/60f * Math.PI*2) * r;
        if(digit<=30){
            return Math.abs(left);
        } else {
            return -Math.abs(left);
        }
    }

    private float getTopBy(int indicator){
        float r=0f;
        float digit = 0;

        switch(indicator){
            case H:
                r = mHourR;
                // 根据分钟进行补充,每5分钟进一小格
                digit = ((mHour%12/12f*60 + mMinute/60f*5)) % 60;
                break;
            case M:
                r = mMinuteR;
                digit = mMinute;
                break;
            case S:
                r = mSecondR;
                digit = mSecond+1;
                break;
        }

        float left = (float) Math.cos(digit/60f * Math.PI*2) * r;
        if(15<=digit && digit<=45){
            return Math.abs(left);
        } else {
            return -Math.abs(left);
        }
    }
    /**
     * 画刻度
     * @param canvas
     */
    private void drawScale(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setTextSize(10);
        paint.setAntiAlias(true);
        for(int i=0; i<60; i++){

            if(i%5 == 0){
                paint.setStrokeWidth(2);
                paint.setColor(mLongScaleColor);
                paint.setTextSize(15);
                String timeText = "" + i/5;
                if(i==0){
                    timeText = "12";
                }
                canvas.drawLine(mWidth/2, mHeight/2 - mDiameter/2, mWidth/2, mHeight/2 - mDiameter/2 + 40, paint);
                paint.setColor(mTextColor);
                canvas.drawText(timeText, mWidth/2-paint.measureText(timeText)/2, mHeight/2-mDiameter/2+80, paint);
            } else {
                paint.setStrokeWidth(1);
                paint.setTextSize(15);
//                paint.setStrokeWidth(getDigit(1));
//                paint.setTextSize(getDigit(15));
                paint.setColor(mShortScaleColor);
                canvas.drawLine(mWidth/2, mHeight/2 - mDiameter/2, mWidth/2, mHeight/2 - mDiameter/2 + 20, paint);
            }
            // 旋转画布，每次旋转6度
            canvas.rotate(6,mWidth/2, mHeight/2);
        }
    }

    /**
     * 画外圆和中心实心圆
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mRingColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(2);
        canvas.drawCircle(mWidth/2, mHeight/2, mDiameter/2, paint);

        // 画中心的实心圆
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mCenterPointColor);
        canvas.drawCircle(mWidth/2, mHeight/2, 4, paint);
    }
}
