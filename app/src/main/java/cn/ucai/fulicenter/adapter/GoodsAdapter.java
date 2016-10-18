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
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.utils.ImageLoader;
/**
 * Created by Administrator on 2016/10/17.
 */
public class GoodsAdapter extends RecyclerView.Adapter {
    List<NewGoodsBean> mList;
    Context context;
    RecyclerView parent;
    boolean isMore;
//    String Footer;
    public GoodsAdapter(List<NewGoodsBean> mList, Context context) {
        this.mList = mList;
        this.context = context;
        this.mList.addAll(mList);
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
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder;
        View layout=null;
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
        if (getItemViewType(position)==I.TYPE_FOOTER){
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(getFooter());
            return ;
        }
        GoodsViewHolder goodsViewHolder = (GoodsViewHolder) holder;
        NewGoodsBean newGoods = mList.get(position);
        goodsViewHolder.tvGoodsMoney.setText(newGoods.getCurrencyPrice());
        goodsViewHolder.tvGoodsName.setText(newGoods.getGoodsName());
        ImageLoader.downloadImg(context,goodsViewHolder.ivGoodsThumb,newGoods.getGoodsThumb());
    }

    private int getFooter() {
        return isMore?R.string.load_more:R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
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
    if (mList!=null){
        mList.clear();
    }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvFooter)
        TextView tvFooter;
        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    static class GoodsViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.ivGoods_Thumb)
        ImageView ivGoodsThumb;
        @Bind(R.id.tvGoods_Name)
        TextView tvGoodsName;
        @Bind(R.id.tvGoods_Money)
        TextView tvGoodsMoney;
        public GoodsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
