package cn.ucai.fulicenter.dao;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/10/24.
 */
public class SharePrefrenceUtils {
    private static final String SHARE_NAME ="saveUserInfo";
    private static SharePrefrenceUtils instance;
    private SharedPreferences msharedPreferences;
    private SharedPreferences.Editor meditor;
    public static final String SHARE_KEY_USER_NAEM = "share_key_user_name";

    public SharePrefrenceUtils(Context context){
        msharedPreferences = context.getSharedPreferences(SHARE_NAME,Context.MODE_APPEND);
        meditor = msharedPreferences.edit();
    }
    public static SharePrefrenceUtils getInstance(Context context){
        if (instance==null){
            instance = new SharePrefrenceUtils(context);
        }
        return instance;
    }
    public void saveUser(String username){
        meditor.putString(SHARE_KEY_USER_NAEM,username);
        meditor.commit();
    }
    public String getUser(){
        return msharedPreferences.getString(SHARE_KEY_USER_NAEM,null);
    }
    public void removeUser(){
        meditor.remove(SHARE_KEY_USER_NAEM);
        meditor.commit();
    }
}
