package com.hyphenate.easeui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * 配置工具类
 */
public class SharedPreferencesUtilUi {
    public static final String APP_EXCEPTION = "AppException";
    public static final String APP_EXCEPTION_KEY = "app_exception_key";
    public static final String APP_EXCEPTION_EXIST = "AppExceptionExist";
    public static final String APP_EXCEPTION_EXIST_KEY = "app_exception_exist_key";

    public static void writeSharedPreferencesBoolean(Context context,
                                                     String spName, String key, boolean value) {
        if (null == context || null == spName || null == key) {
            return;
        }
        SharedPreferences user = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        Editor editor = user.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getSharedPreferencesBoolean(Context context,
                                                      String spName, String key, boolean defValue) {
        if (null == context || null == spName || null == key) {
            return defValue;
        }
        SharedPreferences user = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        return user.getBoolean(key, defValue);
    }

    public static void writeSharedPreferencesString(Context context,
                                                    String spName, String key, String value) {
        if (null == context || null == spName || null == key) {
            return;
        }
        SharedPreferences user = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        Editor editor = user.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSharedPreferencesString(Context context,
                                                    String spName, String key, String defValue) {
        if (null == context || null == spName || null == key) {
            return defValue;
        }
        SharedPreferences user = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        return user.getString(key, defValue);
    }

    public static void writeSharedPreferencesInt(Context context,
                                                 String spName, String key, int value) {
        if (null == context || null == spName || null == key) {
            return;
        }
        SharedPreferences user = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        Editor editor = user.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void writeSharedPreferencesDouble(Context context,
                                                    String spName, String key, double value) {
        if (null == context || null == spName || null == key) {
            return;
        }
        SharedPreferences user = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        Editor editor = user.edit();
        editor.putFloat(key, (float) value);
        editor.commit();
    }
    public static void writeSharedPreferencesLong(Context context,
                                                    String spName, String key, Long value) {
        if (null == context || null == spName || null == key) {
            return;
        }
        SharedPreferences user = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        Editor editor = user.edit();
        editor.putFloat(key, (float) value);
        editor.commit();
    }

    public static double getSharedPreferencesDouble(Context context,
                                                    String spName, String key, double value) {
        if (null == context || null == spName || null == key) {
            return (float) value;
        }
        SharedPreferences user = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        return user.getFloat(key, (float) value);
    }
    public static double getSharedPreferencesLong(Context context,
                                                    String spName, String key, long value) {
        if (null == context || null == spName || null == key) {
            return (float) value;
        }
        SharedPreferences user = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        return user.getFloat(key, (float) value);
    }

    public static int getSharedPreferencesInt(Context context, String spName,
                                              String key, int value) {
        if (null == context || null == spName || null == key) {
            return value;
        }
        SharedPreferences user = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        return user.getInt(key, value);
    }

    public static void ClearSharedPreferences(Context context, String spName) {
        if (null == context || null == spName) {
            return;
        }
        SharedPreferences user = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        Editor editor = user.edit();
        editor.clear();
        editor.commit();
    }

    public static void writeSharedPreferencesMap(Context context,
                                                 String spName, Map<String, String> map) {
        if (null == context || null == spName || null == map) {
            return;
        }
        SharedPreferences user = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        Editor editor = user.edit();

        for (String key : map.keySet()) {
            editor.putString(key, map.get(key));
        }
        editor.commit();
    }
    /**
     * 使用SharedPreference保存对象
     *
     * @param fileKey    储存文件的key
     * @param key        储存对象的key
     * @param saveObject 储存的对象
     */
    public static void save(Context context,String fileKey, String key, Object saveObject) {
        SharedPreferences user = context.getSharedPreferences(fileKey,
                Context.MODE_PRIVATE);
        Editor editor = user.edit();
        String string = Object2String(saveObject);
        editor.putString(key, string);
        editor.commit();
    }

    /**
     * 获取SharedPreference保存的对象
     *
     * @param fileKey 储存文件的key
     * @param key     储存对象的key
     * @return object 返回根据key得到的对象
     */
    public static Object get(Context context,String fileKey, String key) {
        SharedPreferences user = context.getSharedPreferences(fileKey,
                Context.MODE_PRIVATE);
        String string = user.getString(key, null);
        if (string != null) {
            Object object = String2Object(string);
            return object;
        } else {
            return null;
        }
    }

    /**
     * writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
     * 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
     *
     * @param object 待加密的转换为String的对象
     * @return String   加密后的String
     */
    private static String Object2String(Object object) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            objectOutputStream.close();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用Base64解密String，返回Object对象
     *
     * @param objectString 待解密的String
     * @return object      解密后的object
     */
    private static Object String2Object(String objectString) {
        byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
