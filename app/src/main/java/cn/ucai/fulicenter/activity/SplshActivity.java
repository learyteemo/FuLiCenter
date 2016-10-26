package cn.ucai.fulicenter.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

public class SplshActivity extends AppCompatActivity {
    private static final String TAG = SplshActivity.class.getSimpleName();
    static final long sleepTime = 2000;
    SplshActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splsh);
        mContext = this;
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user =FuLiCenterApplication.getUser();
                L.e(TAG,"fulicenter,user=" +user);
                String username = SharePrefrenceUtils.getInstance(mContext).getUser();
                L.e(TAG,"fulicenter,username=" +username);
                if (user==null&&username!=null) {
                    UserDao dao = new UserDao(mContext);
                    user = dao.getUser(username);
                    L.e(TAG,"database user"+user);
                }
                if (user!=null){
                    FuLiCenterApplication.setUser(user);
                }
                MFGT.gotoMainActivity(SplshActivity.this);
                finish();
            }
        },sleepTime);
       /* new Thread(new Runnable() {
            @Override
            public void run() {
               long startTime =  System.currentTimeMillis();
                //creat db
                long lastTime = System.currentTimeMillis();
                long cost = lastTime - startTime;
                if (cost<sleepTime){

                    try{
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                *//*startActivity(new Intent(SplshActivity.this,MainActivity.class));
                finish();*//*
                MFGT.startActivity(SplshActivity.this,MainActivity.class);
                MFGT.finish(SplshActivity.this);
            }
        }).start();*/
    }
}
