package cn.ucai.fulicenter.activity;

import android.content.Intent;
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
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.fragment.CategoryFragment;
import cn.ucai.fulicenter.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.fragment.PersonalFragment;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

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
    CategoryFragment mcategoryFragment;
    PersonalFragment mpersonalFragment;
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
        mcategoryFragment = new CategoryFragment();
        mpersonalFragment = new PersonalFragment();
        mfragment[0] =mNewGoodsFragment;
        mfragment[1] = mboutiqueFragment;
        mfragment[2] = mcategoryFragment;
        mfragment[4] = mpersonalFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,mNewGoodsFragment)
                .add(R.id.fragment_container,mboutiqueFragment)
                .add(R.id.fragment_container,mcategoryFragment)
                .hide(mboutiqueFragment)
                .hide(mcategoryFragment)
                .hide(mpersonalFragment)
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
                if (FuLiCenterApplication.getUser() == null) {
                    MFGT.gotoLoginActivity(this);
                } else {
                    index = 4;
                }
                break;
        }
        setFragment();

    }

    private void setFragment() {
    if (index!=currentIndex){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(mfragment[currentIndex]);
        if (!mfragment[index].isAdded()) {
            ft.add(R.id.fragment_container, mfragment[index]);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (index == 4 && FuLiCenterApplication.getUser() == null) {
            index = 0;
        }
        setFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("onActivityResult,requestCode"+requestCode);
        if (requestCode == I.REQUEST_CODE_LOGIN && FuLiCenterApplication.getUser() != null) {
            index = 4;
        }
    }
}
