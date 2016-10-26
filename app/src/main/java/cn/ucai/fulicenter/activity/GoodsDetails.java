package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.FlowIndicator;
import cn.ucai.fulicenter.views.SlideAutoLoopView;

public class GoodsDetails extends BaseActivity {
    GoodsDetails mContext;
    @Bind(R.id.tvGoodsEnglishName)
    TextView mtvGoodsEnglishName;
    @Bind(R.id.tvGoodsName)
    TextView mtvGoodsName;
    @Bind(R.id.tvPrice)
    TextView mtvPrice;
    @Bind(R.id.slideAutoLoopView)
    SlideAutoLoopView mslideAutoLoopView;
    @Bind(R.id.flowIndicator)
    FlowIndicator mflowIndicator;
    @Bind(R.id.webView)
    WebView mwebView;
    int goodsId;
    @Bind(R.id.ivGoodsDetailCollect)
    ImageView mivGoodsDetailCollect;
    boolean isCollected = false;
    @Bind(R.id.ivGoodsDetailShare)
    ImageView ivGoodsDetailShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_details);

        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("edtails", "goodsid" + goodsId);
        if (goodsId == 0) {
            finish();
        }
        mContext = this;
        /*initView();
        initData();
        setListener();*/
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.ivGoodsDetailCollect)
    public void onCollectClick() {
        final User user = FuLiCenterApplication.getUser();
        if (user == null) {
            MFGT.gotoLoginActivity(mContext);
        } else {
            if (isCollected) {
                NetDao.deleteCollect(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollected = !isCollected;
                            updateGoodsCollectStatus();
                            CommonUtils.showLongToast(user.getMuserNick() + "成功取消了收藏商品");
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            } else {
                NetDao.addCollect(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollected = !isCollected;
                            updateGoodsCollectStatus();
                            CommonUtils.showLongToast(user.getMuserNick() + "成功收藏了商品");

                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }
    }

    @Override
    protected void initData() {
        NetDao.downloadGoodsDetail(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.i("details=" + result);
                if (result != null) {
                    showGoodDetails(result);
                } else {
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                finish();
                L.e("details,error" + error);
                CommonUtils.showLongToast(error);
            }
        });
    }

    private void showGoodDetails(GoodsDetailsBean details) {
        mtvGoodsEnglishName.setText(details.getGoodsEnglishName());
        mtvGoodsName.setText(details.getGoodsName());
        mtvPrice.setText(details.getShopPrice());
        mslideAutoLoopView.startPlayLoop(mflowIndicator, getAlbumImgUrl(details), getAlumbImgCount(details));
        mwebView.loadDataWithBaseURL(null, details.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAlumbImgCount(GoodsDetailsBean details) {
        if (details.getProperties() != null && details.getProperties().length > 0) {
            return details.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean details) {
        String[] urls = new String[]{};
        if (details.getPromotePrice() != null && details.getProperties().length > 0) {
            AlbumsBean[] albums = details.getProperties()[0].getAlbums();
            urls = new String[albums.length];
            for (int i = 0; i < albums.length; i++) {
                urls[i] = albums[i].getImgUrl();
            }
        }
        return urls;
    }

    @OnClick(R.id.ivBack)
    public void onBackClick() {
        MFGT.finish(this);
    }

    private void isCollected() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.isColected(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        isCollected = true;
                        updateGoodsCollectStatus();
                    } else {
                        isCollected = false;
                        updateGoodsCollectStatus();
                    }
                }

                @Override
                public void onError(String error) {
                    isCollected = false;
                    updateGoodsCollectStatus();
                }
            });
        }
    }

    private void updateGoodsCollectStatus() {
        if (isCollected) {
            mivGoodsDetailCollect.setImageResource(R.mipmap.bg_collect_out);
        } else {
            mivGoodsDetailCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCollected();
    }
}
