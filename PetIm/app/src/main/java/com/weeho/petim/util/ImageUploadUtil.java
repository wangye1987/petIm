package com.weeho.petim.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;


import com.weeho.petim.lib.utils.BitmapUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

/** 
 * @author 作者 E-mail: wangkui
 * @version 创建时间：2015-12-1 下午3:04:17 
 * 类说明 
 * @param 
 */
public class ImageUploadUtil {
	public static Bitmap newBitmap;
	private static Bitmap bitmaps;
	private static String picNamethumbnail;
	static ImageUploadUtil imageUploadUtil;
	static Activity Mcontext;
	// 图片存储路径
		private static final String PATH = Environment
				.getExternalStorageDirectory() + "/DCIM/";
	public static ImageUploadUtil getInstance(Context Mcontext){
		if(imageUploadUtil==null)
			imageUploadUtil = new ImageUploadUtil();
		ImageUploadUtil.Mcontext = (Activity) Mcontext;
		return imageUploadUtil;
	}
	//获取图片path
		public static String getAbsoluteImagePath(Uri uri, Activity activity)
		   {  
			 String[] proj = { MediaStore.Images.Media.DATA };
			 Cursor actualimagecursor = activity.managedQuery(uri,proj,null,null,null);
			 int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			 actualimagecursor.moveToFirst(); 
			 String img_path = actualimagecursor.getString(actual_image_column_index);
		       
			 return img_path;  
		   } 
		
		public static File getPath(String imagepath){
			BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true; 
//	        bitmaps = BitmapFactory.decodeFile(imagepath, options);
	         //缩放比 
	        options.inSampleSize = CalculateBitmap(imagepath,200,200);
	        //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦 
	        options.inJustDecodeBounds = false; 
	        bitmaps= BitmapFactory.decodeFile(imagepath,options);

	        newBitmap = BitmapUtil.compressImage(bitmaps);
	        //创建保存图片的文件夹
	        File file = new File(PATH, picNamethumbnail);
	        FileOutputStream bao = null;
	     		try {
	     			bao = new FileOutputStream(file);
	     		} catch (FileNotFoundException e) {
	     			// TODO Auto-generated catch block
	     			e.printStackTrace();
	     		}
	     		//向文件写图片流
	     		newBitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);
	        return file;
		}
		
		//计算压缩图片的samplesize 循环压缩
		public static int CalculateBitmap(String pathName, int mwidth, int mheight){
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(pathName, option);
			//获取
			int bitmapwidth = option.outWidth;
			int bitmapheight = option.outHeight;
			if(mwidth == 0 || mheight ==0){
				return 1;
			}
			int isSampleSize = 1;
			if(bitmapwidth > mwidth || bitmapheight>mheight){
				int halfwidth = bitmapwidth / 2;
				int halfheight = bitmapheight / 2;
				while(halfwidth/isSampleSize > mwidth && halfheight/isSampleSize> mheight){
					isSampleSize *=2;
				}
			}
			return isSampleSize;
		}

	public static File saveBitmapFile(Bitmap bitmap){
		picNamethumbnail = DateFormat.format("yyyyMMdd_hhmmss",
				Calendar.getInstance(Locale.CHINA))+ ".jpg";
		File file = new File(PATH, picNamethumbnail);
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  file;
	}

}
 