package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

public class OrderActivity extends BaseActivity implements PaymentHandler{

    @Bind(R.id.ed_username)
    EditText edUsername;
    @Bind(R.id.ed_number)
    EditText edNumber;
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
    private static final String TAG = OrderActivity.class.getSimpleName();
    private static String URL = "http://218.244.151.190/demo/charge";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order);
         ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        // 设置要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "cnp", "bfb"});
        //设置是否支持外卡支付， true：支持， false：不支持， 默认不支持外卡
        PingppOne.CONTENT_TYPE = "application/json";
        //是否开启日志
        PingppLog.DEBUG = true;
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
        L.e(TAG,ids+"");
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
                L.e("c,id="+c.getId());
                for (String id : ids) {
                    L.e("order.id="+id);
                    if (id.equals(String.valueOf(c.getId()))) {
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
        if (!number.matches("[\\d]{11}")){
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
        L.e("rankPrice"+rankPrice);
        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());

        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", rankPrice*100);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);
    }

    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {

            // result：支付结果信息
            // code：支付结果码
            //-2:用户自定义错误
            //-1：失败
            // 0：取消
            // 1：成功
            // 2:应用内快捷支付支付结果
            L.e(TAG,"code="+data.getExtras().getInt("code"));
            if (data.getExtras().getInt("code") != 2) {
                PingppLog.d(data.getExtras().getString("result") + "  " + data.getExtras().getInt("code"));
            } else {
                String result = data.getStringExtra("result");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (resultJson.has("error")) {
                        result = resultJson.optJSONObject("error").toString();
                    } else if (resultJson.has("success")) {
                        result = resultJson.optJSONObject("success").toString();
                    }
                    L.e(TAG, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
