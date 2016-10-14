package cn.ucai.fulicenter.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.utils.L;

public class MainActivity extends AppCompatActivity {
        RadioButton mLayoutNewGood,mLayokutBoutique,mLayoutCategroy,
    mLayoutCart,mLayoutPersonalCenter;
        TextView mtvCartHint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.i("MainActivity create");
        initView();
    }

    private void initView() {
        mLayoutNewGood = (RadioButton) findViewById(R.id.newGood);
        mLayokutBoutique = (RadioButton) findViewById(R.id.boutique);
        mLayoutCategroy = (RadioButton) findViewById(R.id.category);
        mLayoutCart = (RadioButton) findViewById(R.id.cart);
        mLayoutPersonalCenter = (RadioButton) findViewById(R.id.personal);
        mtvCartHint = (TextView) findViewById(R.id.tvCartHint);
    }

    public  void onCheckedChange(View view){

    }
}
