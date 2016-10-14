package cn.ucai.filicenter.activity;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.filicenter.R;

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
        new Thread(new Runnable() {
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
                startActivity(new Intent(SplshActivity.this,MainActivity.class));

            }
        }).start();
    }
}
