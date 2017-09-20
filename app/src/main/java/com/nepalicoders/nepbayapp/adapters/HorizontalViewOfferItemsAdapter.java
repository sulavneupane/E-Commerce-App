package com.nepalicoders.nepbayapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

/**
 * Created by sulav on 12/14/15.
 */
public class HorizontalViewOfferItemsAdapter extends RecyclerView.Adapter<HorizontalViewOfferItemsAdapter.HorizontalScrollViewItemsViewHolder> {

    private LayoutInflater inflater;
    private List<Product> offerProducts;
    private Context context;

    public HorizontalViewOfferItemsAdapter(Context context, List<Product> offerProducts) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.offerProducts = offerProducts;
    }

    public void setOfferProducts(List<Product> offerProducts) {
        this.offerProducts = offerProducts;
    }

    @Override
    public HorizontalViewOfferItemsAdapter.HorizontalScrollViewItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_templates_horizontal_scroll_view_offer_single_product, parent, false);
        HorizontalScrollViewItemsViewHolder holder = new HorizontalScrollViewItemsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HorizontalViewOfferItemsAdapter.HorizontalScrollViewItemsViewHolder holder, final int position) {
        holder.productName.setText(Html.fromHtml(offerProducts.get(position).getName()));
        holder.productOwner.setText(Html.fromHtml(offerProducts.get(position).getCompany()));
        holder.productOriginalPrice.setText(Html.fromHtml("Rs. " + offerProducts.get(position).getOriginalPrice()));
        holder.productOriginalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.productPrice.setText(Html.fromHtml("Rs. " + offerProducts.get(position).getPrice()));
        //holder.singleProductToolbar.setTitle("Rs. " + offerProducts.get(position).getPrice());
        //Picasso.with(context).load(ApiUrl.NEPBAY_PRODUCT_IMAGES_URL_TEMPORARY + offerProducts.get(position).getImageId() + "&thumb=1").placeholder(R.drawable.progress_animation).into(holder.productImage);

        holder.progressView.setVisibility(View.VISIBLE);
        holder.productImage.setVisibility(View.VISIBLE);
        final CircularProgressView progressView = holder.progressView;
        Picasso.with(context)
                .load(ApiUrl.NEPBAY_PRODUCT_IMAGES_URL_TEMPORARY + offerProducts.get(position).getImageId() + "&thumb=1")
                .fit()
                .into(holder.productImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub

                    }
                });

        holder.productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
        holder.productOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
        if (holder.singleProductToolbar != null) {
            // inflate your menu
            if (holder.singleProductToolbar.getMenu().size() == 0) {
                holder.singleProductToolbar.inflateMenu(R.menu.single_product_menu);
            }
            holder.singleProductToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int id = menuItem.getItemId();
                    switch (id) {
                        case R.id.action_add_cart:
                            SharedPreferences sharedPreferences = context.getSharedPreferences(VariableNames.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
                            String sharedPreferences_sessionKey = sharedPreferences.getString(VariableNames.SESSION_KEY, "false");


                            if (sharedPreferences_sessionKey.equals("false")) {

                                try {
                                    DatabaseHandler handler = new DatabaseHandler(context).Open();
                                    Product product = offerProducts.get(position);
                                    BaseCartItems.CartItems cartItem = new BaseCartItems.CartItems();
                                    cartItem.setId(Long.parseLong(product.getId()));
                                    cartItem.setQuantity(1);
                                    cartItem.setPrice(Float.parseFloat(product.getPrice()));
                                    cartItem.setName(product.getName());
                                    cartItem.setSellerName(product.getCompany());
                                    cartItem.setProductImage(product.getImageId());
                                    long itemid = handler.addItemToCart(cartItem);
                                    if (itemid > 0) {
                                        Message.snackBarShortPositive(((Activity) context).getWindow().getDecorView(), product.getName() + " has been added to your cart!");
                                        if (context instanceof MainActivity)
                                            ((MainActivity) context).updateCartCounter(true);
                                    }
                                    handler.close();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                context.startActivity(new Intent(context, LoginActivity.class));

                            } else {
                                try {
                                    DatabaseHandler handler = new DatabaseHandler(context).Open();
                                    Product product = offerProducts.get(position);
                                    BaseCartItems.CartItems cartItem = new BaseCartItems.CartItems();
                                    cartItem.setId(Long.parseLong(product.getId()));
                                    cartItem.setQuantity(1);
                                    cartItem.setPrice(Float.parseFloat(product.getPrice()));
                                    cartItem.setName(product.getName());
                                    cartItem.setSellerName(product.getCompany());
                                    cartItem.setProductImage(product.getImageId());
                                    long itemid = handler.addItemToCart(cartItem);
                                    if (itemid > 0) {
                                        Message.snackBarShortPositive(((Activity) context).getWindow().getDecorView(), product.getName() + " has been added to your cart!");
                                        if (context instanceof MainActivity)
                                            ((MainActivity) context).updateCartCounter(true);
                                    }
                                    handler.close();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;

                        case R.id.action_add_wishlist:
                            try {
                                DatabaseHandler handler = new DatabaseHandler(context).Open();
                                Product product = offerProducts.get(position);
                                long wishlistid = handler.addItemToWishlist(product);
                                if (wishlistid > 0) {
                                    Message.snackBarShortPositive(((Activity) context).getWindow().getDecorView(), product.getName() + " has been added to your wishlist!");
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
    }

    @Override
    public int getItemCount() {
        if (offerProducts == null) {
            return 0;
        }
        return offerProducts.size();
    }

    class HorizontalScrollViewItemsViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        TextView productOwner;
        TextView productOriginalPrice;
        TextView productPrice;
        ImageView productImage;
        Toolbar singleProductToolbar;
        CircularProgressView progressView;

        public HorizontalScrollViewItemsViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productOwner = (TextView) itemView.findViewById(R.id.product_owner);
            productOriginalPrice = (TextView) itemView.findViewById(R.id.product_original_price);
            singleProductToolbar = (Toolbar) itemView.findViewById(R.id.single_product_toolbar);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            productImage = (ImageView) itemView.findViewById(R.id.product_image);
            progressView = (CircularProgressView) itemView.findViewById(R.id.loading);
        }
    }

    public void onItemClick(int position) {
        Intent intent = new Intent(context, ProductDetail.class);
        intent.putExtra("Product", offerProducts.get(position));
        context.startActivity(intent);
    }
}
