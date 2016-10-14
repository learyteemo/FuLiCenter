package cn.ucai.fulicenter.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.utils.MFGT;

public class SplshActivity extends AppCompatActivity {
    static final long sleepTime = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splsh);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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
