package com.nepalicoders.nepbayapp.SQLite_database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Kayub on 7/26/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "NepBay";

    public static final String DATABASE_TABLE_CART = "tbl_cart";
    public static final String KEY_ITEM_ID = "_id";
    public static final String KEY_ITEM_NAME = "_name";
    public static final String KEY_ITEM_PRICE = "_price";
    public static final String KEY_ITEM_OWNER = "_owner";
    public static final String KEY_ITEM_IMAGE_URL = "_imageurl";
    public static final String KEY_ITEM_QUANTITY = "_quantity";

    public static String CREATE_TABLE_CART = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_CART +
                        "("+
                            KEY_ITEM_ID + " INT PRIMARY KEY NOT NULL, " +
                            KEY_ITEM_NAME+" TEXT NOT NULL, " +
                            KEY_ITEM_PRICE+" FLOAT NOT NULL, " +
                            KEY_ITEM_OWNER+" TEXT NOT NULL, " +
                            KEY_ITEM_QUANTITY+" INT NOT NULL, " +
                            KEY_ITEM_IMAGE_URL+ " TEXT)";


    public static final String DATABASE_TABLE_WISHLIST = "tbl_wishlist";
    public static final String KEY_WISHLIST_ITEM_ID = "_wishlist_id";
    public static final String KEY_WISHLIST_ITEM_NAME = "_wishlist_name";
    public static final String KEY_WISHLIST_ITEM_PRICE = "_wishlist_price";
    public static final String KEY_WISHLIST_ITEM_OWNER = "_wishlist_owner";
    public static final String KEY_WISHLIST_ITEM_IMAGE_URL = "_wishlist_imageurl";
    public static String CREATE_TABLE_WISHLIST = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_WISHLIST +
                        "("+
                            KEY_WISHLIST_ITEM_ID + " INT PRIMARY KEY NOT NULL, "+
                            KEY_WISHLIST_ITEM_NAME+" TEXT NOT NULL, "+
                            KEY_WISHLIST_ITEM_PRICE+" FLOAT NOT NULL, "+
                            KEY_WISHLIST_ITEM_OWNER+" TEXT NOT NULL, "+
                            KEY_WISHLIST_ITEM_IMAGE_URL+ " TEXT)";

    public final static String DATABASE_TABLE_SUGGESTION = "tbl_suggestion";
    public final static String FIELD_SUGGESTION = "suggestion";
    public final static String FIELD_ID = "_id";
    public static String CREATE_TABLE_SEARCH_SUGGESTION ="CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_SUGGESTION  +
                        " (" +  FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                FIELD_SUGGESTION + " TEXT UNIQUE) ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CART);
        db.execSQL(CREATE_TABLE_WISHLIST);
        db.execSQL(CREATE_TABLE_SEARCH_SUGGESTION);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + DATABASE_TABLE_CART);
        db.execSQL("DROP TABLE IF EXIST " + DATABASE_TABLE_WISHLIST);
        db.execSQL("DROP TABLE IF EXIST " + DATABASE_TABLE_SUGGESTION );
        onCreate(db);
    }
}