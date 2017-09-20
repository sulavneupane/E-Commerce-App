package com.nepalicoders.nepbayapp.SQLite_database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nepalicoders.nepbayapp.application.App;
import com.nepalicoders.nepbayapp.objects.BaseCartItems;
import com.nepalicoders.nepbayapp.objects.Product;

import java.sql.SQLException;

/**
 * Created by Kayub on 7/26/2015.
 */
public class DatabaseHandler {

    private DatabaseHelper ourHelper;
    private Context context;
    private SQLiteDatabase ourDatabase;


    Tracker mTracker;

    int i = 0;

    public DatabaseHandler(Context c) {
        context = c;
        mTracker = ((App)(((Activity)c).getApplication())).getDefaultTracker();
        mTracker.setScreenName("Database Operations");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    public DatabaseHandler Open() throws SQLException {
        ourHelper = new DatabaseHelper(context);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public Long addItemToCart(BaseCartItems.CartItems item){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.KEY_ITEM_ID, item.getId());
        cv.put(DatabaseHelper.KEY_ITEM_NAME, item.getName());
        cv.put(DatabaseHelper.KEY_ITEM_PRICE, item.getPrice());
        cv.put(DatabaseHelper.KEY_ITEM_OWNER, item.getSellerName());
        cv.put(DatabaseHelper.KEY_ITEM_IMAGE_URL, item.getProductImage());
        cv.put(DatabaseHelper.KEY_ITEM_QUANTITY, item.getQuantity());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("AddToCart")
                .setAction("Product = " + item.getName() + " Price = " + item.getPrice())
                .build());

        return ourDatabase.insert(DatabaseHelper.DATABASE_TABLE_CART, null, cv);
    }

    public Cursor getAllItemsFromCart(){
       return ourDatabase.query(DatabaseHelper.DATABASE_TABLE_CART, null, null, null, null, null, null);
    }

    public int removeItemFromCart(long id){
        return ourDatabase.delete(DatabaseHelper.DATABASE_TABLE_CART, DatabaseHelper.KEY_ITEM_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int removeAllItemsFromCart(){
        return ourDatabase.delete(DatabaseHelper.DATABASE_TABLE_CART, null, null);
    }

    public Long addItemToWishlist(Product item){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.KEY_WISHLIST_ITEM_ID, item.getId());
        cv.put(DatabaseHelper.KEY_WISHLIST_ITEM_NAME, item.getName());
        cv.put(DatabaseHelper.KEY_WISHLIST_ITEM_PRICE, item.getPrice());
        cv.put(DatabaseHelper.KEY_WISHLIST_ITEM_OWNER, item.getCompany()==null?"":item.getCompany());
        cv.put(DatabaseHelper.KEY_WISHLIST_ITEM_IMAGE_URL, item.getImageId());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("AddToWishList")
                .setAction("Product = " + item.getName()+ "ItemCode = " + item.getProductCode() + " Price = " + item.getPrice())
                .build());

        return ourDatabase.insert(DatabaseHelper.DATABASE_TABLE_WISHLIST, null, cv);
    }

    public Cursor getAllItemsFromWishlist(){
        return ourDatabase.query(DatabaseHelper.DATABASE_TABLE_WISHLIST, null, null, null, null, null, null);
    }

    public int removeItemFromWishlist(long id){
        return ourDatabase.delete(DatabaseHelper.DATABASE_TABLE_WISHLIST, DatabaseHelper.KEY_WISHLIST_ITEM_ID + "=?", new String[]{String.valueOf(id)});
    }


    public long insertSuggestion(String text) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.FIELD_SUGGESTION, text);
        Log.d("Saved Search", text);
        return ourDatabase.insert(DatabaseHelper.DATABASE_TABLE_SUGGESTION , null, values);
    }

    public Cursor getSuggestions(String text) {
        return ourDatabase.query(DatabaseHelper.DATABASE_TABLE_SUGGESTION ,
                new String[]{DatabaseHelper.FIELD_ID, DatabaseHelper.FIELD_SUGGESTION}, DatabaseHelper.FIELD_SUGGESTION + " LIKE '" + text + "%'", null, null, null, null);
    }

}