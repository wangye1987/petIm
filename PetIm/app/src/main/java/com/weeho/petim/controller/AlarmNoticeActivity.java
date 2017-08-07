package com.weeho.petim.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.io.IOException;

import com.weeho.petim.util.ToastUtil;

/**
 * Created by wangkui on 2017/5/11.
 */

public class AlarmNoticeActivity extends Activity {

    private Activity mActivity;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = AlarmNoticeActivity.this;
        //显示对话框
        ShowAlarm();
        startAlarm();
    }
    private void startAlarm() {
        mMediaPlayer = MediaPlayer.create(mActivity, getSystemDefultRingtoneUri());
        mMediaPlayer.setLooping(true);
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }

    private void Stop(){
        if(mMediaPlayer!=null && mMediaPlayer.isPlaying())
            mMediaPlayer.stop();
    }
    //获取系统默认铃声的Uri
    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
    }
    private void ShowAlarm(){
        final AlertDialog mDialog = new AlertDialog.Builder(this).create();
        mDialog.setMessage("设置闹钟");
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showToast(mActivity,"确定");
                        Stop();
                    }
                });
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                        Stop();
                    }
                });
        mDialog.show();
    }
}
