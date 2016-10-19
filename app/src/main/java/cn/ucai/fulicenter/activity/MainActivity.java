package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.utils.L;

public class MainActivity extends BaseActivity {
    @Bind(R.id.newGood)
    RadioButton newGood;
    @Bind(R.id.boutique)
    RadioButton boutique;
    @Bind(R.id.category)
    RadioButton category;
    @Bind(R.id.cart)
    RadioButton cart;
    @Bind(R.id.personal)
    RadioButton personal;
    NewGoodsFragment mNewGoodsFragment;
    BoutiqueFragment mboutiqueFragment;
    Fragment [] mfragment;
    int index;
    int currentIndex;
    RadioButton [] rbs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("MainActivity create");
       /* initView();
        initFragment();*/
        super.onCreate(savedInstanceState);
    }
    private void initFragment() {
        mfragment = new Fragment[5];
        mNewGoodsFragment = new NewGoodsFragment();
        mboutiqueFragment = new BoutiqueFragment();
        mfragment[0] =mNewGoodsFragment;
        mfragment[1] = mboutiqueFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,mNewGoodsFragment)
                .add(R.id.fragment_container,mboutiqueFragment)
                .hide(mboutiqueFragment)
                .show(mNewGoodsFragment)
                .commit();

    }
    @Override
    protected void initView() {
        rbs = new RadioButton[5];
        rbs[0] = newGood;
        rbs[1] = boutique;
        rbs[2] = category;
        rbs[3] = cart;
        rbs[4] = personal;
    }

    @Override
    protected void initData() {
        initFragment();
    }

    @Override
    protected void setListener() {

    }

    public void onCheckedChange(View view) {
        switch (view.getId()){
            case R.id.newGood:
                index = 0;
                break;
            case R.id.boutique:
                index = 1;
                break;
            case R.id.category:
                index = 2;
                break;
            case R.id.cart:
                index = 3;
                break;
            case R.id.personal:
                index = 4;
                break;
        }
        setFragment();

    }

    private void setFragment() {
    if (index!=currentIndex){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(mfragment[currentIndex]);
        if (!mfragment[index].isAdded()){
            ft.add(R.id.fragment_container,mfragment[index]);
        }
        ft.show(mfragment[index]).commit();
    }
        setRadioButtonStatus();
        currentIndex=index;
    }

    private void setRadioButtonStatus() {
        L.e("index="+index);
        for (int i = 0 ;i<rbs.length;i++){
            if (i==index){
                rbs[i].setChecked(true);
            }else {
                rbs[i].setChecked(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
