package com.weeho.petim.controller;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.weeho.petim.R;

/**
 * Created by wangkui on 2017/4/26.
 */

public class SelectPicPopupWindow extends Activity implements View.OnClickListener {

    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private LinearLayout layout;
    private Intent intent;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);
        intent = getIntent();
        btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo); //拍照
        btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);  //从相册选择
        btn_cancel = (Button) this.findViewById(R.id.btn_cancel);   //取消

        layout = (LinearLayout) findViewById(R.id.pop_layout);

        // 添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        layout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // 添加按钮监听
        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);
    }

    // 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {  //点击外面退出这activity
        finish();
        return true;
    }

    @Override  //startActivityResult()后调用的这下面方法
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {   // 选择完, 拍照,或者选择图片后调用的方法
        if (resultCode != RESULT_OK) {
            return;
        }
        //选择完或者拍完照后会在这里处理，然后我们继续使用setResult返回Intent以便可以传递数据和调用
        if (data.getExtras() != null)
            intent.putExtras(data.getExtras());   //拍照得到的图片
        if (data.getData()!= null)
            intent.setData(data.getData());   //选择图片得到的数据, 里面有uri
        setResult(1, intent);     // 返回到下面的, MainActivity
        finish();

    }


    public void takePhone(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE2);

        }else {
            takePhoto();
        }
    }

    private void takePhoto() {
        try {
            //拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
            //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //6.0以上版本适配获取权限
    public void choosePhone(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE2);

        }else {
            choosePhoto();
        }
    }

    private void choosePhoto() {
        try {
            //选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
            //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 2);
        } catch (ActivityNotFoundException e) {

        }
    }


    //权限分配
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                takePhoto();
            } else
            {
                // Permission Denied
//                Toast.makeText(SelectPicPopupWindow.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }


        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                choosePhoto();
            } else
            {
                // Permission Denied
//                Toast.makeText(SelectPicPopupWindow.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:     //拍照
                takePhone();
                break;
            case R.id.btn_pick_photo:    // 选择图片
                choosePhone();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            default:
                break;
        }
    }
}
