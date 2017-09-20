package com.nepalicoders.nepbayapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.SQLite_database.DatabaseHandler;
import com.nepalicoders.nepbayapp.StaticInfo.VariableNames;
import com.nepalicoders.nepbayapp.activities.LoginActivity;
import com.nepalicoders.nepbayapp.activities.MainActivity;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.fragments.WishlistFragment;
import com.nepalicoders.nepbayapp.interfaces.WishlistListener;
import com.nepalicoders.nepbayapp.objects.BaseCartItems;
import com.nepalicoders.nepbayapp.objects.Product;
import com.nepalicoders.nepbayapp.utils.Message;
import com.nepalicoders.nepbayapp.utils.SquareImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by sabin on 12/12/15.
 */
public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.Holder> {

    private List<Product> products;
    private WishlistFragment fm;
    private Context mContext;

    public WishListAdapter(Context c, WishlistFragment fragment, List<Product> products) {
        mContext = c;
        fm = fragment;
        this.products = products;
    }

    public WishListAdapter setProducts(List<Product> products) {
        this.products = products;
        return this;
    }

    @Override
    public WishListAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_templates_grid_view_single_product, parent, false);
        Holder holder = new Holder(v);
        return holder;

    }

    public void removeProduct(int position) {
        products.remove(position);
        notifyItemRemoved(position);
        if (products.size() == 0) {
            fm.showEmptyWishlistMessage();
        }
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Product product = getItem(position);

        //Picasso.with(mContext).load(ApiUrl.NEPBAY_PRODUCT_IMAGES_URL_TEMPORARY + product.getImageId() + "&thumb=1").placeholder(R.drawable.progress_animation).into(holder.productImage);

        holder.progressView.setVisibility(View.VISIBLE);
        holder.productImage.setVisibility(View.VISIBLE);
        final CircularProgressView progressV = holder.progressView;
        Picasso.with(mContext)
                .load(ApiUrl.NEPBAY_PRODUCT_IMAGES_URL_TEMPORARY + product.getImageId() + "&thumb=1")
                .fit()
                .into(holder.productImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressV.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub

                    }
                });

        holder.name.setText(Html.fromHtml(product.getName()));
        holder.seller.setText(Html.fromHtml(product.getCompany()));
        holder.productPrice.setText(Html.fromHtml("Rs. " + product.getPrice()));

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public Product getItem(int position) {
        return products.get(position);
    }

    public class Holder extends RecyclerView.ViewHolder {

        SquareImageView productImage;
        TextView name, seller, productPrice;
        Toolbar toolbar;
        CircularProgressView progressView;

        public Holder(View itemView) {
            super(itemView);
            productImage = (SquareImageView) itemView.findViewById(R.id.product_image);
            name = (TextView) itemView.findViewById(R.id.product_name);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            seller = (TextView) itemView.findViewById(R.id.product_owner);
            toolbar = (Toolbar) itemView.findViewById(R.id.single_product_toolbar);
            progressView = (CircularProgressView) itemView.findViewById(R.id.loadingImage);
            if (toolbar != null) {
                // inflate your menu
                if (toolbar.getMenu().size() == 0) {
                    toolbar.inflateMenu(R.menu.wishlist_menu);
                }
                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.action_add_cart:
                                SharedPreferences sharedPreferences = mContext.getSharedPreferences(VariableNames.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
                                String sharedPreferences_sessionKey = sharedPreferences.getString(VariableNames.SESSION_KEY, "false");


                                if (sharedPreferences_sessionKey.equals("false")) {

                                    try {
                                        DatabaseHandler handler = new DatabaseHandler(mContext).Open();
                                        Product product = getItem(getAdapterPosition());
                                        BaseCartItems.CartItems cartItem = new BaseCartItems.CartItems();
                                        cartItem.setId(Long.parseLong(product.getId()));
                                        cartItem.setQuantity(1);
                                        cartItem.setPrice(Float.parseFloat(product.getPrice()));
                                        cartItem.setName(product.getName());
                                        cartItem.setSellerName(product.getCompany());
                                        cartItem.setProductImage(product.getImageId());
                                        long itemid = handler.addItemToCart(cartItem);
                                        if (itemid > 0) {
                                            /*Message.snackBarShortPositive(((MainActivity) mContext).getWindow().getDecorView(), product.getName() + " has been added to your cart!");
                                            ((MainActivity) mContext).updateCartCounter(true);*/
                                        }
                                        handler.close();

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                    mContext.startActivity(new Intent(mContext, LoginActivity.class));

                                } else {
                                    try {
                                        DatabaseHandler handler = new DatabaseHandler(mContext).Open();
                                        Product product = getItem(getAdapterPosition());
                                        BaseCartItems.CartItems cartItem = new BaseCartItems.CartItems();
                                        cartItem.setId(Long.parseLong(product.getId()));
                                        cartItem.setQuantity(1);
                                        cartItem.setPrice(Float.parseFloat(product.getPrice()));
                                        cartItem.setName(product.getName());
                                        cartItem.setSellerName(product.getCompany());
                                        cartItem.setProductImage(product.getImageId());
                                        long itemid = handler.addItemToCart(cartItem);
                                        if (itemid > 0) {
                                            Message.snackBarShortPositive(((MainActivity) mContext).getWindow().getDecorView(), product.getName() + " has been added to your cart!");
                                            ((MainActivity) mContext).updateCartCounter(true);
                                        }
                                        handler.close();

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;

                            case R.id.action_remove:
                                ((WishlistListener) fm).onItemRemove(getAdapterPosition());
                                break;
                        }
                        return true;
                    }
                });
            }
        }
    }
}
