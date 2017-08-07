package com.weeho.petim.controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.NoCopySpan;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weeho.petim.lib.activityManager.StartActivityUtil;
import com.weeho.petim.lib.utils.BitmapUtil;
import com.weeho.petim.lib.utils.SharedPreferencesUtil;
import com.weeho.petim.lib.utils.StringUtil;
import com.weeho.petim.lib.utils.WeakHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.weeho.petim.R;
import com.weeho.petim.RetorfitWapper.ApiCallUtil;
import com.weeho.petim.base.BaseActivity;
import com.weeho.petim.modle.BaseBean;
import com.weeho.petim.modle.LoginBeanSave;
import com.weeho.petim.modle.PetBaseBean;
import com.weeho.petim.modle.PetVarietyBasebean;
import com.weeho.petim.modle.UploadImgBean;
import com.weeho.petim.modle.VersionInitdatabean;
import com.weeho.petim.network.ApiHttpCilent;
import com.weeho.petim.util.ConstantsUtil;
import com.weeho.petim.util.ImageUploadUtil;
import com.weeho.petim.util.LogUtil;
import com.weeho.petim.util.ToastUtil;
import com.weeho.petim.view.ChangeAddressDialog;
import com.weeho.petim.view.ChangeDateDialog;
import com.weeho.petim.view.TakePhotosDialog;

/**
 * Created by wangkui on 2017/4/25.
 * 宠物信息页
 */

public class PetInfoActivity extends BaseActivity {

    private TextView tv_title_left;
    private ImageView iv_image;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_pz;
    private TextView tv_ym;
    private TextView tv_ly;
    private RelativeLayout image_relative;
    private RelativeLayout name_relative;
    private RelativeLayout sex_relative;
    private RelativeLayout pz_relative;
    private RelativeLayout ym_relative;
    private RelativeLayout ly_relative;
    private String name;
    private String titleName;
    private int index;
    private int type = 2;
    private ChangeDateDialog mChangeBirthDialog;
    private TextView tv_data;
    private String userid;
    private MyHandler handler = new MyHandler(this);
    String dataTime;
    private Activity mActivity;
    private PetVarietyBasebean petVarietyBasebean;
     String[] vartyName ;
    String[] vartyid ;

     String[] lyName  = new String[]{"是","否"};
    String[] lyid = new String[]{"0","1"} ;
    private List<PetVarietyBasebean.ResultBean.ListBean> list;
    private String varty;
    private String breed;
    private String cageCulture;
    private String vartyText = "";
    private String lyText = "";
    private String baseurl;
    Bitmap image;
    private int sexname = -1;
    private File uploadFile;
    private UploadImgBean uploadImgBean;
    private ApiCallUtil mApiCallUtil;
    private TakePhotosDialog dialog;

    @Override
    protected void onCreate() {
        setBaseContentView(R.layout.petinfo);
        initView();
        initData();
    }

    @Override
    protected boolean hasTitle() {
        return true;
    }

    @Override
    protected void loadChildView() {

    }

    @Override
    protected void getNetData() {

    }

    @Override
    protected void reloadCallback() {

    }

    @Override
    protected void setChildViewListener() {

    }

    @Override
    protected String setTitleName() {
        return null;
    }

    @Override
    protected String setRightText() {
        return null;
    }

    @Override
    protected int setLeftImageResource() {
        return 0;
    }

    @Override
    protected int setMiddleImageResource() {
        return 0;
    }

    @Override
    protected int setRightImageResource() {
        return 0;
    }

    private String GetUserId() {
        LoginBeanSave loginBeanSave  = (LoginBeanSave) SharedPreferencesUtil.get(this,"login","login");
        return loginBeanSave.getUserId();
    }
    private void initData() {
        mApiCallUtil = ApiCallUtil.getInstant(mActivity);
        if(StringUtil.isEmpty(userid))
            userid = GetUserId();
        //获取宠物信息
        ApiHttpCilent.CreateLoading(mActivity);
        GetPetInfo();
        //宠物品类数据子典
        mApiCallUtil.GetVarityInfo(userid).enqueue(new Callback<PetVarietyBasebean>() {
            @Override
            public void onResponse(Call<PetVarietyBasebean> call, Response<PetVarietyBasebean> response) {
                Dimess();
                petVarietyBasebean = response.body();
                if(petVarietyBasebean==null)
                    return;
                Message message = Message.obtain();
                if("1".equals(petVarietyBasebean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS_THREE;
                }else{
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = petVarietyBasebean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<PetVarietyBasebean> call, Throwable t) {
                Dimess();
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
            }
        });
    }

    private void GetPetInfo() {
        mApiCallUtil.GetPetInfo(userid).enqueue(new Callback<PetBaseBean>() {
            @Override
            public void onResponse(Call<PetBaseBean> call, Response<PetBaseBean> response) {
                Dimess();
                petbaseBean = response.body();
                if(petbaseBean==null)
                    return;
                Message message = Message.obtain();
                if("1".equals(petbaseBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS_TWO;
                }else{
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = petbaseBean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<PetBaseBean> call, Throwable t) {
                Dimess();
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据获取失败,请稍后重试";
                handler.sendMessage(message);
            }
        });
    }

    public  class MyHandler extends WeakHandler<PetInfoActivity> {
        public MyHandler(PetInfoActivity reference) {
            super(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantsUtil.CONTENT_SUCCESS:
                    // 成功
                    ToastUtil.showToast(getReference(),"修改成功");
                    if(!StringUtil.isEmpty(dataTime))
                    tv_data.setText(dataTime);

                    if(!StringUtil.isEmpty(vartyText))
                    tv_pz.setText(vartyText);

                    if(!StringUtil.isEmpty(lyText))
                        tv_ly.setText(lyText);

                    //上传图片成功 删除文件
                    if(uploadFile != null && iv_image != null){
                        //存储宠物图像 聊天需要用到
                        if(uploadImgBean!=null && uploadImgBean.getResult()!=null)
                        SharedPreferencesUtil.writeSharedPreferencesString(getReference(),"avatar","avatar",uploadImgBean.getResult().getHeadSculpturePath());
                        Glide.with(getReference()).load(uploadImgBean.getResult().getHeadSculpturePath()).into(iv_image);
                        if(uploadFile!= null && uploadFile.exists())
                        uploadFile.delete();
                        if(image!=null) {
                            image.recycle();
                            image = null;
                        }
                    }
                    ClearData();
                    break;
                case ConstantsUtil.CONTENT_SUCCESS_TWO:
                    //获取宠物信息成功
                    UpdateView(petbaseBean);
                    break;
                case ConstantsUtil.CONTENT_SUCCESS_THREE:
                    //获取宠物字典成功
                    SetValityData();
                    break;
                case ConstantsUtil.CONTENT_FAIL:
                    //
                   String error = (String) msg.obj;
                    if(!StringUtil.isEmpty(error))
                    ToastUtil.showToast(getReference(),error);
                    else
                    ToastUtil.showToast(getReference(),"操作失败,请重试");
                    break;
            }
        }
    }

    //获取品类数据
    private void  SetValityData(){
        PetVarietyBasebean.ResultBean resultBean = petVarietyBasebean.getResult();
        if(resultBean!=null){
            list = resultBean.getList();
//            for(int i=0;i<30;i++){
//                PetVarietyBasebean.ResultBean.ListBean   bean = new PetVarietyBasebean.ResultBean.ListBean();
//                bean.setId(""+(i+90));
//                bean.setVarietyName("藏獒"+i);
//                list.add(bean);
//            }
            if(list!=null){
                int size = list.size();
                vartyName = new String[size];
                vartyid = new String[size];
                for (int i=0;i< size;i++){
                    vartyName[i] = list.get(i).getVarietyName();
                    vartyid[i] = list.get(i).getId();
                }
            }
        }
    }
    //更新界面
    private void UpdateView(PetBaseBean petbaseBean) {
        if(petbaseBean != null ) {
            PetBaseBean.ResultBean  result = petbaseBean.getResult();
            if(result != null) {
               PetBaseBean.ResultBean.DataBean data =  result.getData();
                if(data != null) {
                    if(!StringUtil.isEmpty(data.getPetName()))
                    tv_name.setText(data.getPetName());
                    else
                        tv_name.setHint("请输入");

                    sexname = data.getPetSex();
                    if(data.getPetSex() == 0 || data.getPetSex() == 1)
                        tv_sex.setText(data.getPetSex()== 0?"雄性":"雌性");
                    else
                        tv_sex.setHint("请选择");

                    if(!StringUtil.isEmpty(data.getBreedName())) {
                        tv_pz.setText(data.getBreedName());
                    }else{
                        tv_pz.setHint("请选择");
                    }

                    if(!StringUtil.isEmpty(data.getTime())){
                        tv_data.setText(data.getTime());
                    }else{
                        tv_data.setHint("请选择");
                    }

                    tv_ly.setText(data.getCageCulture()== 0?"是":"否");
                    if(!StringUtil.isEmpty(data.getHeadSculpturePath())){
                        Glide.with(mActivity)
                                .load(data.getHeadSculpturePath())
                                .centerCrop()
                                .placeholder(R.drawable.em_actionbar_camera_icon)
                                .crossFade()
                                .into(iv_image);
                    }
                }
            }
        }
    }

    private void initView() {
        mActivity = PetInfoActivity.this;
        TextView base_activity_title_titlename = (TextView) findViewById(R.id.base_activity_title_titlename);
        tv_title_left = (TextView) findViewById(R.id.base_activity_title_left);
        tv_data = (TextView) findViewById(R.id.tv_data);
        tv_title_left.setVisibility(View.VISIBLE);
        tv_title_left.setText("宠物资料");
        base_activity_title_titlename.setText("");
        LinearLayout linear_back = (LinearLayout) findViewById(R.id.linear_back);
        linear_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        image_relative = (RelativeLayout) findViewById(R.id.image_relative);
        name_relative = (RelativeLayout) findViewById(R.id.name_relative);
        sex_relative = (RelativeLayout) findViewById(R.id.sex_relative);
        pz_relative = (RelativeLayout) findViewById(R.id.pz_relative);
        ym_relative = (RelativeLayout) findViewById(R.id.ym_relative);
        ly_relative = (RelativeLayout) findViewById(R.id.ly_relative);

        image_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivityForResult(, 1);
//                StartActivityUtil.startActivityForResult(PetInfoActivity.this,new Intent(PetInfoActivity.this,
//                        SelectPicPopupWindow.class),1);
                dialog();
            }
        });
        name_relative.setOnClickListener(this);
        sex_relative.setOnClickListener(this);
        pz_relative.setOnClickListener(this);
        ym_relative.setOnClickListener(this);
        ly_relative.setOnClickListener(this);

        iv_image = (ImageView) findViewById(R.id.iv_image);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_pz = (TextView) findViewById(R.id.tv_pz);
        tv_ym = (TextView) findViewById(R.id.tv_ym);
        tv_ly = (TextView) findViewById(R.id.tv_ly);
        userid = GetUserId();
    }
    public void dialog() {

        //设置dialog的样式
        dialog = new TakePhotosDialog(PetInfoActivity.this, R.style.dialog_setting);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animation); // 添加动画
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(params);
        Button choose_picture= (Button) dialog.findViewById(R.id.bt_choose_picture);
        Button take_picture= (Button) dialog.findViewById(R.id.bt_take_picture);
        Button cancel= (Button) dialog.findViewById(R.id.bt_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhone();
            }
        });
        choose_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhone();
            }
        });
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
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private void choosePhoto() {
        try {
            //选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
            //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 2);
        } catch (ActivityNotFoundException e) {
             e.printStackTrace();
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
        @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.name_relative:
                name = tv_name.getText().toString().trim();
                titleName = "名称";
                intent.putExtra("name", name);
                index = 10;
                GoToUpdate(intent);
                break;
            case R.id.sex_relative:
                name = tv_sex.getText().toString().trim();
                titleName = "性别";
                index = 11;
                intent.putExtra("name", name);
                GoToUpdate(intent);
                break;
            case R.id.pz_relative:
                ShowChoise("选择宠物品种",1,vartyName,vartyid);
                break;
            case R.id.ym_relative:
                if (mChangeBirthDialog == null) {
                    mChangeBirthDialog = new ChangeDateDialog(PetInfoActivity.this);
                }
                mChangeBirthDialog.setDate(2017, 04, 27);
                mChangeBirthDialog.show();
                mChangeBirthDialog.setDateListener(new ChangeDateDialog.OnDateListener() {
                    @Override
                    public void onClick(String year, String month, String day) {
                         dataTime = year+"年"+month+"月"+day+"日";
                        if(StringUtil.isEmpty(userid))
                            userid = GetUserId();
                        RefitUpdatePetInfo();
                    }
                });
                break;
            case R.id.ly_relative:
                ShowChoise("是否笼养",2,lyName,lyid);
                break;
        }
    }
    private void GoToUpdate(Intent intent) {
        intent.putExtra("title", titleName);
        intent.putExtra("type", type);
        intent.putExtra("index", index);
        intent.setClass(this,UpdateUserInfoActivity.class);
        StartActivityUtil.startActivityForResult(this,intent, ConstantsUtil.CONTENT_SUCCESS_TWO);
    }

    //清除数据
    private void ClearData(){
        dataTime = "";
        breed = "";
        cageCulture = "";
    }
    //修改宠物信息
    private void RefitUpdatePetInfo() {
        ApiHttpCilent.CreateLoading(this);
        Map<String,Object> map = new HashMap<>();
        map.clear();
        if(!StringUtil.isEmpty(dataTime))
        map.put("time",dataTime);
        if(!StringUtil.isEmpty(breed))
        map.put("breed",breed);
        if(!StringUtil.isEmpty(cageCulture))
        map.put("cageCulture",Integer.parseInt(cageCulture));
        if(!StringUtil.isEmpty(userid))
        map.put("userid",userid);
        mApiCallUtil.UptadePetInfo(map).enqueue(new Callback<UploadImgBean>() {
            @Override
            public void onResponse(Call<UploadImgBean> call, Response<UploadImgBean> response) {
                Dimess();
                UploadImgBean baseBean = response.body();
                if(baseBean==null)
                    return;
                Message message = Message.obtain();
                if("1".equals(baseBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS;
                }else{
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = baseBean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<UploadImgBean> call, Throwable t) {
                ClearData();
                Dimess();
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
            }
        });
    }

    private void ShowChoise(String notice,final int type,final String[] stName,final String[] stId)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(notice);
        //    设置一个下拉的列表选择项
        builder.setItems(stName, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                varty = stId[which];

                if(StringUtil.isEmpty(userid))
                    userid = GetUserId();
                if(1 == type) {
                    vartyText = stName[which];
                    breed = varty;
                }else{
                    lyText = stName[which];
                    cageCulture = varty;
                    }
                RefitUpdatePetInfo();
            }
        });
        builder.show();

    }
    private void Dimess() {
        PetInfoActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if(ApiHttpCilent.loading != null && ApiHttpCilent.loading.isShowing())
                    ApiHttpCilent.loading.dismiss();
            }
        });
    }
    private PetBaseBean petbaseBean;

    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }
    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            //拍照
            case 1:
                if (data != null) {
                    Bundle extras = data.getExtras(); //拍照没有uri
                    if (extras != null) {
                        //这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                        image = extras.getParcelable("data");
                        if (image != null) {
                            uploadFile = ImageUploadUtil.saveBitmapFile(image);
//                            iv_image.setImageBitmap(image);
                        }
                    }
                }

                if(dialog!= null && dialog.isShowing())
                   dialog.dismiss();
                UploadImage();
                break;
            //选择图片
            case 2:
                if (data != null) {
                    //取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
                    Uri mImageCaptureUri = data.getData();  //选择图片有uri
                    //返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
                    if (mImageCaptureUri != null) {
                        try {
                            // 防止OOM
                            image =  getBitmapFormUri(PetInfoActivity.this, mImageCaptureUri);
                            if(image!= null) {
                                uploadFile = ImageUploadUtil.saveBitmapFile(image);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(dialog!= null && dialog.isShowing())
                    dialog.dismiss();
                    UploadImage();
                    break;
        }
        switch (resultCode) {
//            case 1:
//                if (data != null) {
//                    //取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
//                    Uri mImageCaptureUri = data.getData();  //选择图片有uri
//                    //返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
//                    if (mImageCaptureUri != null) {
//                        try {
//                            // 防止OOM
//                            image =  getBitmapFormUri(PetInfoActivity.this, mImageCaptureUri);
//                            if(image!= null)
//                            uploadFile = ImageUploadUtil.saveBitmapFile(image);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    else {
//                        Bundle extras = data.getExtras(); //拍照没有uri
//                        if (extras != null) {
//                            //这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
//                             image = extras.getParcelable("data");
//                            if (image != null) {
//                                uploadFile = ImageUploadUtil.saveBitmapFile(image);
//                            }
//                        }
//                    }
//
//                    UploadImage();
//                }
//                break;
            case RESULT_OK:
                if(StringUtil.isEmpty(userid))
                    userid = GetUserId();
                GetPetInfo();
                break;
            default:
                break;
        }
    }

    private void UploadImage() {
        if(uploadFile == null)
            return;
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),uploadFile);
        MultipartBody.Part part =  MultipartBody.Part.createFormData("file",uploadFile.getName(),requestBody);
        ApiHttpCilent.CreateLoading(this);
        if(mApiCallUtil!= null){
        mApiCallUtil.UploadPetImage(part,userid).enqueue(new Callback<UploadImgBean>() {
            @Override
            public void onResponse(Call<UploadImgBean> call, Response<UploadImgBean> response) {
                Dimess();
                uploadImgBean = response.body();
                if(uploadImgBean== null)
                    return;
                Message message = Message.obtain();
                if("1".equals(uploadImgBean.getStatus())) {
                    message.what = ConstantsUtil.CONTENT_SUCCESS;
                }else{
                    message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                    message.obj = uploadImgBean.getError().getInfo();
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<UploadImgBean> call, Throwable t) {
                Dimess();
                Message message = Message.obtain();
                message.what = ConstantsUtil.CONTENT_FAIL;// 错误
                message.obj = "数据错误,请稍后重试";
                handler.sendMessage(message);
            }
        });}
    }

    public String Bitmap2StrByBase64(Bitmap bit){
    ByteArrayOutputStream bos=new ByteArrayOutputStream();
    bit.compress(Bitmap.CompressFormat.PNG,40,bos);//参数100表示不压缩  
    byte[] bytes=bos.toByteArray();
    return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
}


