package cn.ucai.fulicenter.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends BaseFragment {
    MainActivity mContext;
    CartAdapter mAdapter;
    ArrayList<CartBean> mList;
    LinearLayoutManager mLayoutManager;
    int mPageId;
    @Bind(R.id.tvRefresh)
    TextView mtvRefresh;
    @Bind(R.id.RecyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.tv_cart_nothing)
    TextView tvCartNothing;

    @Bind(R.id.tv_cart_count_price)
    TextView tvCartCountPrice;

    @Bind(R.id.tv_cart_save_price)
    TextView tvCartSavePrice;
    @Bind(R.id.account)
    TextView account;
    @Bind(R.id.layoutCart)
    LinearLayout mlayoutCart;
    updateCartReceiver mReceiver;
    String cartIds = "";
    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        mContext = (MainActivity) getContext();
        mList = new ArrayList<>();
        mAdapter = new CartAdapter(mContext, mList);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void setListener() {
        setPullUpListener();
        setPullDownListener();
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATA_CART);
        mReceiver = new updateCartReceiver();
        mContext.registerReceiver(mReceiver, filter);

    }

    private void setPullDownListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mtvRefresh.setVisibility(View.VISIBLE);
                mPageId = 1;
                downloadCart(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void setPullUpListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = mLayoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == mAdapter.getItemCount() - 1 && mAdapter.isMore()) {
                    mPageId++;
                    downloadCart(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = mLayoutManager.findFirstVisibleItemPosition();
                mSwipeRefreshLayout.setEnabled(firstPosition == 0);
            }
        });
    }

    @Override
    protected void initData() {
        downloadCart(I.ACTION_DOWNLOAD);
    }

    private void downloadCart(final int action) {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.downloadCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
                @Override
                public void onSuccess(String str) {
                    ArrayList<CartBean> list = ResultUtils.getCartFromJson(str);
                    L.e("teemosb="+list);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mtvRefresh.setVisibility(View.GONE);
                    mAdapter.setMore(true);
                    L.e("result" + str);
                    if (list != null && list.size() > 0) {
                        L.e("list[0]" + list.get(0));
                        mList.clear();
                        mList.addAll(list);
                        if (action== I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN){
                            mAdapter.initData(mList);
                        }else {
                            mAdapter.addData(mList);
                        }
                        if (list.size() < I.PAGE_SIZE_DEFAULT) {
                            mAdapter.setMore(false);
                        }
                        setCartLayout(true);
                    } else {
                        mAdapter.setMore(false);
                        setCartLayout(false);
                    }
                }
                @Override
                public void onError(String error) {
                    setCartLayout(false);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mtvRefresh.setVisibility(View.GONE);
                    CommonUtils.showLongToast(error);
                    L.e("error" + error);
                }
            });
        }
    }
    @Override
    protected void initView() {
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_blue)
        );
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(12));
        setCartLayout(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    public void setCartLayout(boolean hasLayout) {
        /*if (mList!=null&&mList.size()>0){
            hasLayout = true;
        }*/
        mlayoutCart.setVisibility(hasLayout?View.VISIBLE:View.GONE);
        tvCartNothing.setVisibility(hasLayout?View.GONE:View.VISIBLE);
        mRecyclerView.setVisibility(hasLayout?View.VISIBLE:View.GONE);
        sumPrice();
    }
    @OnClick(R.id.account)
    public void buy() {
        if (cartIds != null&&!cartIds.equals("")&&cartIds.length()>0) {
            MFGT.gotobuy(mContext,cartIds);
        } else {
            CommonUtils.showLongToast(R.string.order_nothing);
        }
    }
    private void sumPrice(){
        cartIds = "";
        int rankPrice = 0;
        int sumPrice = 0;
        if (mList != null && mList.size() > 0) {
            for (CartBean c : mList) {
                if (c.isChecked()) {
                    cartIds +=c.getId()+",";
                    sumPrice +=getPrice(c.getGoods().getCurrencyPrice())*c.getCount();
                    rankPrice+=getPrice(c.getGoods().getRankPrice())*c.getCount();
                }
            }
            tvCartCountPrice.setText("合计：￥"+Double.valueOf(sumPrice));
            tvCartSavePrice.setText("节省：￥"+Double.valueOf(sumPrice-rankPrice));
        } else {
            cartIds = "";
            tvCartCountPrice.setText("合计：￥0");
            tvCartSavePrice.setText("节省：￥0");
        }
    }
    class updateCartReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            sumPrice();
            setCartLayout(mList != null && mList.size() > 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver!=null){
            mContext.unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private int getPrice(String price){
        price = price.substring(price.indexOf("￥")+1);
        return Integer.valueOf(price);
    }

}
