package com.nepalicoders.nepbayapp.activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.SQLite_database.DatabaseHandler;
import com.nepalicoders.nepbayapp.SQLite_database.DatabaseHelper;
import com.nepalicoders.nepbayapp.adapters.CartAdapter;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.application.App;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.interfaces.CartListener;
import com.nepalicoders.nepbayapp.objects.BaseCartItems;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements CartListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    List<BaseCartItems.CartItems> itemList = new ArrayList<>();
    BaseCartItems baseCartItems;
    Toolbar toolbar;
    DatabaseHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        try {
            handler = new DatabaseHandler(this).Open();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        baseCartItems = new BaseCartItems();
        Cursor cursor = handler.getAllItemsFromCart();
        if (cursor != null && cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                BaseCartItems.CartItems items = new BaseCartItems.CartItems();

                items.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ITEM_NAME)));
                items.setPrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.KEY_ITEM_PRICE)));
                items.setQuantity(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_ITEM_QUANTITY)));
                items.setSellerName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ITEM_OWNER)));
                items.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_ITEM_ID)));
                items.setProductImage(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ITEM_IMAGE_URL)));
                itemList.add(items);
                baseCartItems.setTotalQuantity(items.getQuantity());
                baseCartItems.setTotalPrice(items.getPrice() * items.getQuantity());
            }
        }
        baseCartItems.setTotalProducts(itemList.size());
        baseCartItems.setItems(itemList);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new CartAdapter(this, baseCartItems);
        ((CartAdapter) mAdapter).setOnQuantiyChangeListener(this);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void addItemtoCart() {
        for (int i = 0; i < 3; i++) {
            BaseCartItems.CartItems items = new BaseCartItems.CartItems();
            items.setId(i);
            items.setName("Wedge Heels " + i);
            items.setPrice(500 * (i + 1));
            items.setQuantity(i + 1);
            items.setSellerName("Rebook");
            handler.addItemToCart(items);

        }
    }

    public void deletItemFromCart() {
        handler.removeAllItemsFromCart();
    }

    @Override
    public void onQuantitychange(int qty, int position, CartAdapter.TotalViewHolder totalViewHolder) {

            BaseCartItems baseCartItems = ((CartAdapter) mAdapter).baseCartItems;
            List<BaseCartItems.CartItems> items = ((CartAdapter) mAdapter).items;

            items.get(position).setQuantity(qty);

            baseCartItems.reset();
            baseCartItems.setTotalProducts(items.size());

            for (int i = 0, len = items.size(); i < len; i++) {
                baseCartItems.setTotalQuantity(items.get(i).getQuantity());
                baseCartItems.setTotalPrice(items.get(i).getPrice() * items.get(i).getQuantity());
            }
        /*try {
            mAdapter.notifyDataSetChanged();
            *//*mAdapter.notifyItemChanged(items.size()+1);*//*
        } catch (Exception e) {
            Log.d("hello", e.toString());
        }*/
        if (totalViewHolder != null) {
            //totalViewHolder.totalQuantity.setText("" + baseCartItems.getTotalQuantity());
            totalViewHolder.totalPrice.setText(Html.fromHtml("Rs." + baseCartItems.getTotalPrice()));
            //totalViewHolder.totalProduct.setText("" + baseCartItems.getTotalProducts());
        }
    }

    @Override
    public void onItemRemove(long id, int position, CartAdapter.TotalViewHolder totalViewHolder) {
        handler.removeItemFromCart(id);

        BaseCartItems baseCartItems = ((CartAdapter) mAdapter).baseCartItems;
        BaseCartItems.CartItems items = ((CartAdapter) mAdapter).items.get(position);
        double price = items.getPrice();
        int quantity = items.getQuantity();
        double totalItemPrice = price * quantity;
        ((CartAdapter) mAdapter).items.remove(position);
        Log.e("Price", price + "");
        Log.e("quantity", quantity + "");
        Log.e("totalItemPrice", totalItemPrice + "");
        Log.e("TotalPrice", baseCartItems.getTotalPrice() - totalItemPrice + "");
        Log.e("totalTotalQuantity", baseCartItems.getTotalQuantity() - quantity + "");

        baseCartItems.setTotalNewPrice(baseCartItems.getTotalPrice() - totalItemPrice);
        baseCartItems.setTotalNewQuantity(baseCartItems.getTotalQuantity() - quantity);
        baseCartItems.setTotalProducts(baseCartItems.getTotalProducts() - 1);

        mAdapter.notifyItemRemoved(position);

        if(totalViewHolder!=null) {
          //  totalViewHolder.totalQuantity.setText("" + baseCartItems.getTotalQuantity());
            totalViewHolder.totalPrice.setText(Html.fromHtml("Rs." + baseCartItems.getTotalPrice()));
          //  totalViewHolder.totalProduct.setText("" + baseCartItems.getTotalProducts());
        }
    }

    @Override
    public void showEmptyCart() {
        findViewById(R.id.emptycart).setVisibility(View.VISIBLE);
    }


}
