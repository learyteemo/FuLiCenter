package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/19.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mcontext;
    ArrayList<CartBean> mList;
    boolean isMore;

    public CartAdapter(Context mcontext, ArrayList<CartBean> mList) {
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
            holder = new FooterViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.item_footer, parent, false));
        } else {
            holder = new CartViewHolder(LayoutInflater.from(mcontext)
                    .inflate(R.layout.item_cart, parent, false));
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(getFooter());
        }
        if (holder instanceof CartViewHolder) {
            CartBean cartBean = mList.get(position);
            CartViewHolder cartViewHolder = (CartViewHolder) holder;
           /* boutiqueViewHolder.tvBoutiqueTitle.setText(boutiqueBean.getTitle());
            boutiqueViewHolder.tvBoutiqueName.setText(boutiqueBean.getName());
            boutiqueViewHolder.ivBoutiqueDescription.setText(boutiqueBean.getDescription());
            boutiqueViewHolder.layoutBoutique.setTag(boutiqueBean);

            ImageLoader.downloadImg(mcontext, boutiqueViewHolder.ivBoutiqueThumb, boutiqueBean.getImageurl());*/
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
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    public void initData(ArrayList<CartBean> list) {
        if (mList != null) {
            this.mList.clear();
        }
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<CartBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }


    static class CartViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cb_cart_selected)
        CheckBox cbCartSelected;
        @Bind(R.id.iv_cart_thumb)
        ImageView ivCartThumb;
        @Bind(R.id.tv_cart_good_name)
        TextView tvCartGoodName;
        @Bind(R.id.iv_cart_add)
        ImageView ivCartAdd;
        @Bind(R.id.tv_cart_add_num)
        TextView tvCartAddNum;
        @Bind(R.id.iv_cart_del)
        ImageView ivCartDel;
        @Bind(R.id.tv_cart_price)
        TextView tvCartPrice;
        @Bind(R.id.layout_cart)
        RelativeLayout layoutCart;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.layout_cart)
        public void onCartClick() {
           /* BoutiqueBean bean = (BoutiqueBean) layoutBoutique.getTag();
            MFGT.gotoBoutiqueChildActivity(mcontext, bean);*/
        }
    }
}
