package com.nepalicoders.nepbayapp.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.etsy.android.grid.StaggeredGridView;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.SQLite_database.DatabaseHandler;
import com.nepalicoders.nepbayapp.SQLite_database.DatabaseHelper;
import com.nepalicoders.nepbayapp.activities.MainActivity;
import com.nepalicoders.nepbayapp.adapters.CartAdapter;
import com.nepalicoders.nepbayapp.adapters.WishListAdapter;
import com.nepalicoders.nepbayapp.interfaces.WishlistListener;
import com.nepalicoders.nepbayapp.objects.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WishlistFragment extends Fragment implements WishlistListener {


    List<Product> itemList = new ArrayList<>();
    DatabaseHandler handler;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    public WishlistFragment() {
        // Required empty public constructor
    }

    TextView emptyMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drawer_wishlist, container, false);

        emptyMessage = (TextView)view.findViewById(R.id.emptyMessage);
        //animation for wishlist fragment
        YoYo.with(Techniques.FadeInLeft).duration(700).playOn(view);

        try {
            handler = new DatabaseHandler(getActivity()).Open();
            buildWishlist();

            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            mAdapter = new WishListAdapter(getActivity(), this, itemList);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new StaggeredGridLayoutManager(2,1);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

            if(itemList.size() == 0){
                showEmptyWishlistMessage();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



        return view;
    }

    private void buildWishlist(){

        Cursor cursor = handler.getAllItemsFromWishlist();
        if(cursor!=null && cursor.getCount()>0){
            for(int i=0; i<cursor.getCount(); i++ ){
                cursor.moveToPosition(i);
                Product items = new Product();
                items.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_WISHLIST_ITEM_NAME)));
                items.setPrice(String.valueOf(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.KEY_WISHLIST_ITEM_PRICE))));
                items.setCompany(String.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_WISHLIST_ITEM_OWNER))));
                items.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_WISHLIST_ITEM_ID))));
                items.setImageId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_WISHLIST_ITEM_IMAGE_URL)));
                itemList.add(items);
            }
        }
    }

    @Override
    public void onItemRemove(int position) {
        String id = ((WishListAdapter)mAdapter).getItem(position).getId();
        int result = handler.removeItemFromWishlist(Long.parseLong(id));
        if(result>0){
            ((WishListAdapter)mAdapter).removeProduct(position);
        }
    }

    public void showEmptyWishlistMessage(){
        emptyMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        itemList.clear();
        buildWishlist();
        ((WishListAdapter)mAdapter).setProducts(itemList).notifyDataSetChanged();

    }
}
