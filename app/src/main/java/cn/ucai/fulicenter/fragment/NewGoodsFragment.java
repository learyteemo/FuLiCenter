package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView mRefresh;
    RecyclerView mRecyclerView;
    MainActivity mContext;
    GoodsAdapter mAdapter;
    GridLayoutManager mLayoutManager ;
    ArrayList<NewGoodsBean> mList;
    int mPageId = 1;
    public NewGoodsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this,view);
        mContext = (MainActivity) getContext();
        mList = new ArrayList<>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout);
        mRefresh = (TextView) view.findViewById(R.id.tvRefresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        mAdapter = new GoodsAdapter(mList,mContext);
        initView();
        initData();
        setListener();
        return view;
    }

    private void setListener() {
        setPullUpListener();
        setPullDownListener();
    }

    private void setPullDownListener() {
    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mSwipeRefreshLayout.setRefreshing(true);
            mRefresh.setVisibility(View.VISIBLE);
            mPageId = 1;
            downloadNewGoods(I.ACTION_PULL_DOWN);
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
                downloadNewGoods(I.ACTION_PULL_UP);
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

    private void downloadNewGoods(final int action) {
        NetDao.downloadNewGoods(mContext, mPageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                mSwipeRefreshLayout.setRefreshing(false);
                mRefresh.setVisibility(View.GONE);
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
                mRefresh.setVisibility(View.GONE);
                CommonUtils.showLongToast(error);
                L.e("error"+error);
            }
        });
    }


    private void initData() {
        downloadNewGoods(I.ACTION_DOWNLOAD);
    }
    private void initView() {
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
}
