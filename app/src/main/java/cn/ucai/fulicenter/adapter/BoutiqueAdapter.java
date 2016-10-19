package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.ImageLoader;

/**
 * Created by Administrator on 2016/10/19.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mcontext;
    ArrayList<BoutiqueBean> mList;
    boolean isMore;

    public BoutiqueAdapter(Context mcontext, ArrayList<BoutiqueBean> mList) {
        this.mcontext = mcontext;
        this.mList = mList;
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
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new GoodsAdapter.FooterViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.item_footer, parent, false));
        } else {
            holder = new BoutiqueViewHolder(LayoutInflater.from(mcontext)
                    .inflate(R.layout.item_boutique, parent, false));
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodsAdapter.FooterViewHolder) {
            GoodsAdapter.FooterViewHolder footerViewHolder = (GoodsAdapter.FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(getFooter());
        }
        if (holder instanceof BoutiqueViewHolder) {
            BoutiqueBean boutiqueBean = mList.get(position);
            BoutiqueViewHolder boutiqueViewHolder = (BoutiqueViewHolder) holder;
            boutiqueViewHolder.tvBoutiqueTitle.setText(boutiqueBean.getTitle());
            boutiqueViewHolder.tvBoutiqueName.setText(boutiqueBean.getName());
            boutiqueViewHolder.ivBoutiqueDescription.setText(boutiqueBean.getDescription());
            ImageLoader.downloadImg(mcontext,boutiqueViewHolder.ivBoutiqueThumb,boutiqueBean.getImageurl());
        }
    }

    private int getFooter() {
        return isMore ? R.string.load_more : R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==getItemCount()-1){
            return I.TYPE_FOOTER;
        }else {
            return I.TYPE_ITEM;
        }
    }
    static class BoutiqueViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.ivBoutiqueThumb)
        ImageView ivBoutiqueThumb;
        @Bind(R.id.tvBoutiqueTitle)
        TextView tvBoutiqueTitle;
        @Bind(R.id.tvBoutiqueName)
        TextView tvBoutiqueName;
        @Bind(R.id.ivBoutiqueDescription)
        TextView ivBoutiqueDescription;
        @Bind(R.id.layout_boutique)
        RelativeLayout layoutBoutique;

        BoutiqueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}