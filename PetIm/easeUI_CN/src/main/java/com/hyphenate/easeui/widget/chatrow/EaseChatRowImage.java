package com.hyphenate.easeui.widget.chatrow;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.util.Util;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.model.EaseImageCache;
import com.hyphenate.easeui.ui.EaseShowBigImageActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseImageUtils;
import com.hyphenate.easeui.widget.photoview.GifSupportView;
import com.hyphenate.util.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EaseChatRowImage extends EaseChatRowFile{

//    protected ImageView imageView;
    private EMImageMessageBody imgBody;
//    private GifSupportView imageView_gif;
    private ImageView imageView;
    private String remotePath;

    public EaseChatRowImage(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.ease_row_received_picture : R.layout.ease_row_sent_picture, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
          imageView = (ImageView) findViewById(R.id.image);
//        imageView_gif = (GifSupportView) findViewById(R.id.gif_view);
    }

    
    @Override
    protected void onSetUpView() {
        imgBody = (EMImageMessageBody) message.getBody();
        // received messages
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                imageView.setImageResource(R.drawable.ease_default_image);
                setMessageReceiveCallback();
            } else {
                progressBar.setVisibility(View.GONE);
                percentageView.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ease_default_image);
                //本地下载地址
                String thumbPath = imgBody.thumbnailLocalPath();
                String thumbnailUrl = imgBody.getThumbnailUrl();
                if (!new File(thumbPath).exists()) {
                	// to make it compatible with thumbnail received in previous version
                    thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
                }
                showImageView(thumbPath, imageView,imgBody.getLocalUrl(), message,thumbnailUrl);
            }
            return;
        }
        
        String filePath = imgBody.getLocalUrl();
        String thumbnailUrl = imgBody.getThumbnailUrl();
        String thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
//        String thumbPath = imgBody.getRemoteUrl();
        showImageView(thumbPath, imageView, filePath, message,thumbnailUrl);
        handleSendMessage();
    }
    
    @Override
    protected void onUpdateView() {
        super.onUpdateView();
    }
    
    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent(context, EaseShowBigImageActivity.class);
        File file = new File(imgBody.getLocalUrl());
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            intent.putExtra("uri", uri);
        } else {
            // The local full size pic does not exist yet.
            // ShowBigImage needs to download it from the server
            // first
            String msgId = message.getMsgId();
            intent.putExtra("messageId", msgId);
            intent.putExtra("localUrl", imgBody.getLocalUrl());
        }
        if (message != null && message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked()
                && message.getChatType() == ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        context.startActivity(intent);
    }
    boolean is_gif;
    /**
     * load image into image view
     * 
     * @param thumbernailPath
     * @param imageView
     * @paramposition
     * @return the image exists or not
     */
    private boolean showImageView(final String thumbernailPath, final ImageView imageView, final String localFullSizePath,final EMMessage message,final String thumbnailUrl) {
        // first check if the thumbnail image already loaded into cache
        Bitmap bitmap = EaseImageCache.getInstance().get(thumbernailPath);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            if(!localFullSizePath.contains(".gif"))
                imageView.setImageBitmap(bitmap);
            if(localFullSizePath.contains(".gif")){
                if( thumbnailUrl!=null && !thumbnailUrl.equals(""))
                Glide.with(getContext()).load(thumbnailUrl).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            }
            return true;
        } else {
            new AsyncTask<Object, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Object... args) {
                    File file = new File(thumbernailPath);
                    if (file.exists()) {
                        return EaseImageUtils.decodeScaleImage(thumbernailPath, 160, 160);
                    } else if (new File(imgBody.thumbnailLocalPath()).exists()) {
                        return EaseImageUtils.decodeScaleImage(imgBody.thumbnailLocalPath(), 160, 160);
                    }
                    else {
                        if (message.direct() == EMMessage.Direct.SEND) {
                            if (localFullSizePath != null && new File(localFullSizePath).exists()) {
                                return EaseImageUtils.decodeScaleImage(localFullSizePath, 160, 160);
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                }

                protected void onPostExecute(Bitmap image) {
                    if (image != null) {
                        if(!localFullSizePath .contains(".gif"))
                            imageView.setImageBitmap(image);
                        else{
                            Glide.with(getContext()).load(thumbnailUrl).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
//                           getContext().getApplicationContext().run
//                            progressBar.setVisibility(View.VISIBLE);
                    }
                        EaseImageCache.getInstance().put(thumbernailPath, image);
                    } else {
                        if (message.status() == EMMessage.Status.FAIL) {
                            if (EaseCommonUtils.isNetWorkConnected(activity)) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        EMClient.getInstance().chatManager().downloadThumbnail(message);
                                    }
                                }).start();
                            }
                        }
                    }
                }
            }.execute();
            return true;
        }
    }
    public File saveBitmapFile(Bitmap bitmap){
        File file;
             file = new File("/mnt/sdcard/pic/"+System.currentTimeMillis()+".gif");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @NonNull
    private InputStream getInputStream(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

}
