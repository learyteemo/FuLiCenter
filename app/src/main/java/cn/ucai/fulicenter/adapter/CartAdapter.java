package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
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
            final CartBean cartBean = mList.get(position);
            GoodsDetailsBean goods = cartBean.getGoods();
            if (goods!=null) {
                ImageLoader.downloadImg(mcontext,((CartViewHolder) holder).ivCartThumb,goods.getGoodsThumb());
                ((CartViewHolder) holder).tvCartGoodName.setText(goods.getGoodsName());
                ((CartViewHolder) holder).tvCartPrice.setText(goods.getCurrencyPrice());
            }
            ((CartViewHolder) holder).tvCartAddNum.setText("("+cartBean.getCount()+")");
            ((CartViewHolder) holder).cbCartSelected.setChecked(false);
            ((CartViewHolder) holder).cbCartSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    cartBean.setChecked(b);
                    mcontext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                }
            });
            ((CartViewHolder) holder).ivCartAdd.setTag(position);
            ((CartViewHolder) holder).ivCartDel.setTag(position);
        }
    }
    private int getFooter() {
        return isMore ? R.string.load_more : R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 0;
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
        mList = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<CartBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }


     class CartViewHolder extends RecyclerView.ViewHolder {
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
      /*  @Bind(R.id.layout_cart)
        RelativeLayout layoutCart;*/

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
       @OnClick(R.id.iv_cart_add)
        public void addCart(){
            final int posotion = (int) ivCartAdd.getTag();
           CartBean cart=mList.get(posotion);
           NetDao.updateCart(mcontext, cart.getId(), cart.getCount()+1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
               @Override
               public void onSuccess(MessageBean result) {
                   if (result!=null&&result.isSuccess()){
                       mList.get(posotion).setCount(mList.get(posotion).getCount()+1);
                       mcontext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                       tvCartAddNum.setText("("+(mList.get(posotion).getCount())+")");
                   }
               }
               @Override
               public void onError(String error) {
               }
           });
       }
         @OnClick({R.id.iv_cart_thumb,R.id.tv_cart_good_name})
         public void gotoDeail(){
             final int posotion = (int) ivCartAdd.getTag();
             CartBean cart=mList.get(posotion);
             MFGT.gotoGoodsDetailsActivity(mcontext,cart.getGoodsId());
         }
         @OnClick(R.id.iv_cart_del)
         public void delCart(){
             final int posotion = (int) ivCartDel.getTag();
             CartBean cart=mList.get(posotion);
             if (cart.getCount() > 1) {
                 NetDao.updateCart(mcontext, cart.getId(), cart.getCount() - 1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                     @Override
                     public void onSuccess(MessageBean result) {
                         if (result != null && result.isSuccess()) {
                             mList.get(posotion).setCount(mList.get(posotion).getCount() - 1);
                             mcontext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                             tvCartAddNum.setText("(" + (mList.get(posotion).getCount()) + ")");
                         }
                     }
                     @Override
                     public void onError(String error) {

                     }
                 });
             } else {
                 NetDao.deleteCart(mcontext, cart.getId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                     @Override
                     public void onSuccess(MessageBean result) {
                         if (result != null && result.isSuccess()) {
                             mList.remove(posotion);
                             mcontext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                             notifyDataSetChanged();
                         }
                     }
                     @Override
                     public void onError(String error) {

                     }
                 });
             }
         }
    }
}
