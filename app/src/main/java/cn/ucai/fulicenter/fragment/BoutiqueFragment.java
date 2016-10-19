package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {


    @Bind(R.id.tvRefresh)
    TextView mtvRefresh;
    @Bind(R.id.RecyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    MainActivity mContext;
    BoutiqueAdapter mAdapter;
    ArrayList<BoutiqueBean> mList;
    LinearLayoutManager mLayoutManager;
    int mPageId;

    public BoutiqueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_boutique, container, false);
        ButterKnife.bind(this, view);
        mContext = (MainActivity) getContext();
        mList = new ArrayList<>();
            mAdapter = new BoutiqueAdapter(mContext,mList);
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
                mtvRefresh.setVisibility(View.VISIBLE);
                mPageId = 1;
                downloadBoutique(I.ACTION_PULL_DOWN);
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
                    downloadBoutique(I.ACTION_PULL_UP);
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

    private void initData() {
    downloadBoutique(I.ACTION_DOWNLOAD);
    }

    private void downloadBoutique(final int action) {
        NetDao.downloadBoutique(mContext, new OkHttpUtils.OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                mSwipeRefreshLayout.setRefreshing(false);
                mtvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                L.e("result"+result);
                if (result!=null&&result.length>0){
                    ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);
                    if (action== I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN){
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

    private void initView() {
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
