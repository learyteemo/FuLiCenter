package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.views.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/17.
 */
public class CollectAdapter extends RecyclerView.Adapter {
    List<CollectBean> mList;
    Context mcontext;
    RecyclerView parent;
    boolean isMore;


    public CollectAdapter(List<CollectBean> List, Context context) {
        mcontext = context;
        mList = new ArrayList<>();
        mList.addAll(List);
    }


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
            layout = inflater.inflate(R.layout.item_collect, parent, false);
            holder = new CollectViewHolder(layout);
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
        CollectViewHolder goodsViewHolder = (CollectViewHolder) holder;
        CollectBean newGoods = mList.get(position);
        goodsViewHolder.tvGoodsName.setText(newGoods.getGoodsName());
        goodsViewHolder.ivDelete.setImageResource(R.mipmap.delete);
        ImageLoader.downloadImg(mcontext, goodsViewHolder.ivGoodsThumb, newGoods.getGoodsThumb());
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

    public void initData(ArrayList<CollectBean> list) {
        if (mList != null) {
            this.mList.clear();
        }
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<CollectBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }


    static class CollectViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivDelete)
        ImageView ivDelete;
        @Bind(R.id.ivGoods_Thumb)
        ImageView ivGoodsThumb;
        @Bind(R.id.tvGoods_Name)
        TextView tvGoodsName;


        public CollectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
