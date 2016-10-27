package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.activity.CategoryChildActivity;
import cn.ucai.fulicenter.activity.CollectActivity;
import cn.ucai.fulicenter.activity.GoodsDetails;
import cn.ucai.fulicenter.activity.LoginActivity;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.activity.PersonalSettingActivity;
import cn.ucai.fulicenter.activity.RegisterActivity;
import cn.ucai.fulicenter.activity.UpdateNickActivity;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;


public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        startActivity(context,intent);

    }
    public static void gotoGoodsDetailsActivity(Context context, int goodsId){
        Intent intent = new Intent();
        intent.setClass(context, GoodsDetails.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsId);
        context.startActivity(intent);
    }
    public static void startActivity(Context context,Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void gotoBoutiqueChildActivity(Context context, BoutiqueBean bean){
        Intent intent = new Intent();
        intent.setClass(context, BoutiqueChildActivity.class);
        intent.putExtra(I.Boutique.CAT_ID,bean);
        context.startActivity(intent);
    }
    public static void gotoCategoryChildActivity(Context context, int catId, String groupName
            , ArrayList<CategoryChildBean> list){
        Intent intent = new Intent();
        intent.setClass(context, CategoryChildActivity.class);
        intent.putExtra(I.CategoryChild.CAT_ID,catId);
        intent.putExtra(I.CategoryGroup.NAME,groupName);
        intent.putExtra(I.CategoryChild.ID,list);
        context.startActivity(intent);
    }

    public static void gotoLoginActivity(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context,LoginActivity.class);
        startActivityForResult(context, intent,I.REQUEST_CODE_LOGIN);
    }
    public static void gotoLoginFromCart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context,LoginActivity.class);
        startActivityForResult(context, intent,I.REQUEST_CODE_LOGIN_FROM_CART);
    }
    public static void gotoRegisterActivity(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context,RegisterActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_REGISTER);

    }
    public static void startActivityForResult(Activity context,Intent intent,int requestCode){
        context.startActivityForResult(intent,requestCode);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void gotoPersonalSettingActivity(Activity context){
        startActivity(context,PersonalSettingActivity.class);
    }
    public static void gotoUpdateNick(Activity context){
        startActivityForResult(context,new Intent(context, UpdateNickActivity.class),I.REQUEST_CODE_NICK);
    }
    public static void gotoCollectsActivity(Activity context){
        startActivity(context, CollectActivity.class);
    }

}
