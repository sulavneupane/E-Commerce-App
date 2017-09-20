package com.nepalicoders.nepbayapp.interfaces;

import com.nepalicoders.nepbayapp.adapters.CartAdapter;

/**
 * Created by sabin on 12/4/15.
 */
public interface CartListener {

    void onQuantitychange(int qty, int position, CartAdapter.TotalViewHolder mTotalViewHolder);
    void onItemRemove(long id, int position, CartAdapter.TotalViewHolder mTotalViewHolder);
    void showEmptyCart();

}
