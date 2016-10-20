package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

public class CategoryChildActivity extends BaseActivity {

    @Bind(R.id.tvGoodsDetails)
    TextView mtvGoodsDetails;
    @Bind(R.id.tvRefresh)
    TextView mtvRefresh;
    @Bind(R.id.RecyclerView)
    android.support.v7.widget.RecyclerView mRecyclerView;
    @Bind(R.id.SwipeRefreshLayout)
    android.support.v4.widget.SwipeRefreshLayout mSwipeRefreshLayout;
    CategoryChildActivity mContext;
    GoodsAdapter mAdapter;
    GridLayoutManager mLayoutManager ;
    ArrayList<NewGoodsBean> mList;
    int mPageId = 1;
    int catId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        catId =  getIntent().getIntExtra(I.CategoryChild.CAT_ID,0);
        if (catId == 0) {
            finish();
        }
        mContext = this;
        mList = new ArrayList<>();
        mAdapter = new GoodsAdapter(mList,mContext);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        mLayoutManager = new GridLayoutManager(mContext, I.COLUM_NUM);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(12));
    }
    @Override
    protected void initData() {
        downloadCategoryChild(I.ACTION_DOWNLOAD);
    }

    private void downloadCategoryChild(final int action) {
        NetDao.downloadCategoryChild(mContext,catId, mPageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                mSwipeRefreshLayout.setRefreshing(false);
                mtvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                L.e("result"+result);
                if (result!=null&&result.length>0){
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    if (action==I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN){
                        mAdapter.initData(list);
                    }else {
                        mAdapter.addData(list);
                    }

                    if (list.size()<I.PAGE_SIZE_DEFAULT){
                        mAdapter.setMore(false);
                    }
                }else {
                    mAdapter.setMore(false);
                }
            }
            @Override
            public void onError(String error) {
                mSwipeRefreshLayout.setRefreshing(false);
                mtvRefresh.setVisibility(View.GONE);
                CommonUtils.showLongToast(error);
                L.e("error"+error);
            }
        });
    }
    @Override
    protected void setListener() {
        setPullUpListener();
        setPullDownListener();
    }

    private void setPullDownListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mtvRefresh.setVisibility(View.VISIBLE);
                mPageId = 1;
                downloadCategoryChild(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void setPullUpListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = mLayoutManager.findLastVisibleItemPosition();
                if (newState==RecyclerView.SCROLL_STATE_IDLE
                        &&lastPosition==mAdapter.getItemCount()-1&&mAdapter.isMore()){
                    mPageId++;
                    downloadCategoryChild(I.ACTION_PULL_UP);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = mLayoutManager.findFirstVisibleItemPosition();
                mSwipeRefreshLayout.setEnabled(firstPosition==0);
            }
        });
    }


    @OnClick(R.id.ivBack)
    public void onClick() {
    }
}
