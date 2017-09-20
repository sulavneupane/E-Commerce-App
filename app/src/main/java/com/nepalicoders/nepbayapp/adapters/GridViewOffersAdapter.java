package com.nepalicoders.nepbayapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.SQLite_database.DatabaseHandler;
import com.nepalicoders.nepbayapp.StaticInfo.VariableNames;
import com.nepalicoders.nepbayapp.activities.LoginActivity;
import com.nepalicoders.nepbayapp.activities.MainActivity;
import com.nepalicoders.nepbayapp.activities.ProductDetail;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.objects.BaseCartItems;
import com.nepalicoders.nepbayapp.objects.Product;
import com.nepalicoders.nepbayapp.utils.Message;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sulav on 12/15/15.
 */
public class GridViewOffersAdapter extends BaseAdapter {

    private List<Product> products = new ArrayList<>();
    private final LayoutInflater inflater;
    private Context mContext;

    public GridViewOffersAdapter(Context c, List<Product> products) {
        mContext = c;
        inflater = LayoutInflater.from(mContext);
        this.products = products;
    }


    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(products.get(position).getId());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ImageView imgV;
        TextView name;
        TextView price;
        TextView original_price;
        TextView owner;
        Toolbar toolbar;
        CircularProgressView progressView;

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_templates_grid_view_single_product_offer, parent, false);
            v.setTag(R.id.product_image, v.findViewById(R.id.product_image));
            v.setTag(R.id.product_name, v.findViewById(R.id.product_name));
            v.setTag(R.id.product_price, v.findViewById(R.id.product_price));
            v.setTag(R.id.product_original_price, v.findViewById(R.id.product_original_price));
            v.setTag(R.id.product_owner, v.findViewById(R.id.product_owner));
            v.setTag(R.id.single_product_toolbar, v.findViewById(R.id.single_product_toolbar));
            v.setTag(R.id.loadingImage, v.findViewById(R.id.loadingImage));
        }

        imgV = (ImageView) v.getTag(R.id.product_image);
        name = (TextView) v.getTag(R.id.product_name);
        price = (TextView) v.getTag(R.id.product_price);
        original_price = (TextView) v.getTag(R.id.product_original_price);
        owner = (TextView) v.getTag(R.id.product_owner);
        toolbar = (Toolbar) v.getTag(R.id.single_product_toolbar);
        progressView = (CircularProgressView) v.getTag(R.id.loadingImage);

        Product product = getItem(position);
        /*imgV.setImageResource(product.imgId);*/
        name.setText(Html.fromHtml(product.getName()));
        owner.setText(Html.fromHtml("By: " + product.getCompany()));
        price.setText(Html.fromHtml("Rs. " + product.getPrice()));
        original_price.setText(Html.fromHtml("Rs. " + product.getOriginalPrice()));
        original_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        //Picasso.with(mContext).load(ApiUrl.NEPBAY_PRODUCT_IMAGES_URL_TEMPORARY + product.getImageId() + "&thumb=1").placeholder(R.drawable.progress_animation).into(imgV);

        progressView.setVisibility(View.VISIBLE);
        imgV.setVisibility(View.VISIBLE);
        final CircularProgressView progressV = progressView;
        Picasso.with(mContext)
                .load(ApiUrl.NEPBAY_PRODUCT_IMAGES_URL_TEMPORARY + product.getImageId() + "&thumb=1")
                .fit()
                .into(imgV, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressV.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub

                    }
                });

        imgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClicked(position);
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClicked(position);
            }
        });
        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClicked(position);
            }
        });

        //toolbar.setTitle("Rs. " + product.getPrice());
        //toolbar.setTitle("Rs. 999999.99");

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClicked(position);
            }
        });

        if (toolbar != null) {
            // inflate your menu
            if (toolbar.getMenu().size() == 0) {
                toolbar.inflateMenu(R.menu.single_product_menu);
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
                                    Product product = getItem(position);
                                    BaseCartItems.CartItems cartItem = new BaseCartItems.CartItems();
                                    cartItem.setId(Long.parseLong(product.getId()));
                                    cartItem.setQuantity(1);
                                    cartItem.setPrice(Float.parseFloat(product.getPrice()));
                                    cartItem.setName(product.getName());
                                    cartItem.setSellerName(product.getCompany() == null ? "" : product.getCompany());
                                    cartItem.setProductImage(product.getImageId());
                                    long itemid = handler.addItemToCart(cartItem);
                                    if (itemid > 0) {
                                        /*Message.snackBarShortPositive(((Activity) mContext).getWindow().getDecorView(), product.getName() + " has been added to your cart!");
                                        if (mContext instanceof MainActivity)
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
                                    Product product = getItem(position);
                                    BaseCartItems.CartItems cartItem = new BaseCartItems.CartItems();
                                    cartItem.setId(Long.parseLong(product.getId()));
                                    cartItem.setQuantity(1);
                                    cartItem.setPrice(Float.parseFloat(product.getPrice()));
                                    cartItem.setName(product.getName());
                                    cartItem.setSellerName(product.getCompany() == null ? "" : product.getCompany());
                                    cartItem.setProductImage(product.getImageId());
                                    long itemid = handler.addItemToCart(cartItem);
                                    if (itemid > 0) {
                                        Message.snackBarShortPositive(((Activity) mContext).getWindow().getDecorView(), product.getName() + " has been added to your cart!");
                                        if (mContext instanceof MainActivity)
                                            ((MainActivity) mContext).updateCartCounter(true);
                                    }
                                    handler.close();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            break;

                        case R.id.action_add_wishlist:
                            try {
                                DatabaseHandler handler = new DatabaseHandler(mContext).Open();
                                Product product = getItem(position);
                                long wishlistid = handler.addItemToWishlist(product);
                                if (wishlistid > 0) {
                                    Message.snackBarShortPositive(((Activity) mContext).getWindow().getDecorView(), product.getName() + " has been added to your wishlist!");
                                }
                                handler.close();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    return true;
                }
            });
        }

        return v;
    }

    public void updateProducts(List<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            this.products.add(products.get(i));
        }
        notifyDataSetChanged();
    }

    //Item click listener
    private void itemClicked(int position) {
        Intent intent = new Intent(mContext, ProductDetail.class);
        intent.putExtra("Product", products.get(position));
        mContext.startActivity(intent);
    }

}
