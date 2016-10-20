package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.activity.GoodsDetails;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/17.
 */
public class GoodsAdapter extends RecyclerView.Adapter {
    List<NewGoodsBean> mList;
    Context mcontext;
    RecyclerView parent;
    boolean isMore;
    int sortBy = I.SORT_BY_ADDTIME_DESC;

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
        sortBy();
        notifyDataSetChanged();
    }

    //    String Footer;
    public GoodsAdapter(List<NewGoodsBean> List, Context context) {
        mcontext = context;
        mList = new ArrayList<>();
        mList.addAll(List);
    }
    private void sortBy(){
        Collections.sort(mList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean left, NewGoodsBean right) {
                int result = 0;
                switch (sortBy){
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (Long.valueOf(left.getAddTime())-Long.valueOf(right.getAddTime()));
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int) (Long.valueOf(right.getAddTime())-Long.valueOf(left.getAddTime()));
                        break;
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(left.getCurrencyPrice())-getPrice(right.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(right.getCurrencyPrice())-getPrice(left.getCurrencyPrice());

                }
                return result;
            }
            private int getPrice(String price){
                price = price.substring(price.indexOf("￥")+1);
                return  Integer.valueOf(price);
            }
        });
    }


    /* public void setFooter(String footer) {
         notifyDataSetChanged();
     }*/
    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = (RecyclerView) parent;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        RecyclerView.ViewHolder holder;
        View layout = null;
        if (viewType == I.TYPE_FOOTER) {
            layout = inflater.inflate(R.layout.item_footer, parent, false);
            holder = new FooterViewHolder(layout);
        } else {
            layout = inflater.inflate(R.layout.item_newgoods, parent, false);
            holder = new GoodsViewHolder(layout);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(getFooter());
            return;
        }
        GoodsViewHolder goodsViewHolder = (GoodsViewHolder) holder;
        NewGoodsBean newGoods = mList.get(position);
        goodsViewHolder.tvGoodsMoney.setText(newGoods.getCurrencyPrice());
        goodsViewHolder.tvGoodsName.setText(newGoods.getGoodsName());
        ImageLoader.downloadImg(mcontext, goodsViewHolder.ivGoodsThumb, newGoods.getGoodsThumb());
        goodsViewHolder.itemNewGoods.setTag(newGoods.getGoodsId());
    }

    private int getFooter() {
        return isMore ? R.string.load_more : R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    public void initData(ArrayList<NewGoodsBean> list) {
        if (mList != null) {
            this.mList.clear();
        }
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<NewGoodsBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }





     class GoodsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivGoods_Thumb)
        ImageView ivGoodsThumb;
        @Bind(R.id.tvGoods_Name)
        TextView tvGoodsName;
        @Bind(R.id.tvGoods_Money)
        TextView tvGoodsMoney;
        @Bind(R.id.item_newGoods)
        LinearLayout itemNewGoods;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.item_newGoods)
        public void onGoodsItemClick() {
            int goodsId = (int) itemNewGoods.getTag();
            MFGT.gotoGoodsDetailsActivity(mcontext,goodsId);
        }
    }
}
