package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.views.DisplayUtils;

public class OrderActivity extends BaseActivity {

    @Bind(R.id.ed_username)
    EditText edUsername;
    @Bind(R.id.ed_number)
    EditText edNumber;
    @Bind(R.id.ed_address)
    EditText edAddress;
    @Bind(R.id.Spinner)
    android.widget.Spinner spinner;
    @Bind(R.id.ed_street)
    EditText edStreet;
    @Bind(R.id.tvCount)
    TextView tvCount;
    @Bind(R.id.tvCommit)
    TextView tvCommit;
    User user =null;
    OrderActivity mContext;
    ArrayList<CartBean> mList = null;
    String[] ids = new String[]{};
    String cartIds = null;
    int rankPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext,"确定订单");
    }

    @Override
    protected void initData() {
       cartIds =   getIntent().getStringExtra(I.Cart.ID);
        L.e("cartIds"+cartIds);
        user = FuLiCenterApplication.getUser();
        L.e("cartIds="+cartIds);
        if (cartIds==null||cartIds.equals("")||user==null) {
        finish();
        }
        ids = cartIds.split(",");
        geOrderList();
    }

    private void geOrderList() {
        NetDao.downloadCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                ArrayList<CartBean> list =ResultUtils.getCartFromJson(s);
                if (list == null || list.size() == 0) {
                    finish();
                } else {
                    mList.addAll(list);
                    sumPrice();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void sumPrice() {
        cartIds = "";
        rankPrice = 0;
        if (mList != null && mList.size() > 0) {
            for (CartBean c : mList) {
                for (String id : ids) {
                    if (id.equals(c.getId() + "")) {
                        rankPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                    }
                }
            }
        }
        tvCount.setText("合计：￥"+Double.valueOf(rankPrice));
    }
    private int getPrice(String price){
        price = price.substring(price.indexOf("￥")+1);
        return Integer.valueOf(price);
    }
    @Override
    protected void setListener() {

    }

    @OnClick(R.id.tvCommit)
    public void checkOrder() {
        String name = edUsername.getText().toString();
        if (TextUtils.isEmpty(name)){
            edUsername.setError("收货人姓名不能为空");
            edUsername.requestFocus();
            return;
        }
        String number = edNumber.getText().toString();
        if (!number.matches("[\\d]{11}]")){
            edNumber.setError("手机号码格式错误");
            edNumber.requestFocus();
            return;
        }
        String area = spinner.getSelectedItem().toString();
        if (TextUtils.isEmpty(area)){
            Toast.makeText(OrderActivity.this, "收货地址不为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String address = edStreet.getText().toString();
        if (TextUtils.isEmpty(address)){
            edStreet.setError("街道地址不为空");
            edStreet.requestFocus();
            return;
        }
        gotoStatements();
    }

    private void gotoStatements() {
    }
}
