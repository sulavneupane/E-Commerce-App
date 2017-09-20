package com.nepalicoders.nepbayapp.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.SQLite_database.DatabaseHandler;
import com.nepalicoders.nepbayapp.StaticInfo.VariableNames;
import com.nepalicoders.nepbayapp.adapters.ProductDetailImagesSliderAdapter;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.application.App;
import com.nepalicoders.nepbayapp.fragments.WishlistFragment;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.objects.BaseCartItems;
import com.nepalicoders.nepbayapp.objects.Product;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.nepalicoders.nepbayapp.utils.Message;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.IOException;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetail extends AppCompatActivity {
    String productUrl;
    String allResult;
    Tracker mTracker;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set status bar color
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("Product");

        try{
            String message = intent.getStringExtra("notification");
            if(!message.equals(""))
                Message.alertDialogSimple("Notification", message, this);
        } catch (Exception ex){

        }

        getSupportActionBar().setTitle(Html.fromHtml(product.getName()));

        mTracker = ((App) getApplication()).getDefaultTracker();
        mTracker.setScreenName("ProductDetail");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Products")
                .setAction(product.getName() + " : " + product.getProductCode())
                .build());

        ((TextView) (findViewById(R.id.product_detail_name))).setText(Html.fromHtml(product.getName()));
        ((TextView) (findViewById(R.id.product_detail_product_code))).setText(Html.fromHtml("Product Code : " + product.getProductCode()));
        ((TextView) (findViewById(R.id.product_detail_price))).setText(Html.fromHtml("Rs. " + product.getPrice()));
        ((TextView) (findViewById(R.id.product_detail_seller))).setText(Html.fromHtml("Seller:  " + product.getCompany()));

        try{
            String originalPrice = product.getOriginalPrice();
            if(!originalPrice.equals("")){
                TextView originalPriceTextView = (TextView) findViewById(R.id.product_detail_offer_price);
                originalPriceTextView.setVisibility(View.VISIBLE);
                originalPriceTextView.setText(Html.fromHtml("(Rs. " + product.getOriginalPrice() + ")"));
                originalPriceTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            }
        } catch (Exception ex){

        }

        ((TextView) (findViewById(R.id.product_detail_seller))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> map = new HashMap<String, String>();
                map.put("shop_username", product.getUsername());

                BackgroundThread thread = new BackgroundThread(ApiUrl.GENERATE_SHOP_URL, map, Connection.GET);
                thread.setBackgroundListener(ProductDetail.this, new Callback() {

                    //loading popup
                    final ProgressDialog dialog = ProgressDialog.show(ProductDetail.this, "", getString(R.string.loading), true);

                    @Override
                    public void onFailure(Request request, IOException e) {

                        //dismiss loading dialog
                        dialog.dismiss();

                    }

                    @Override
                    public void onResponse(Response response, String result) throws IOException {

                        //dismiss loading dialog
                        dialog.dismiss();

                        if (response.isSuccessful()) {

                            try {
                                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                                startActivity(myIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(ProductDetail.this, "No application can handle this request."
                                        + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }

                        }

                    }
                });

                thread.execute();

            }
        });

        if (product.getDescriptionShort().equals("")) {
            ((TextView) (findViewById(R.id.product_detail_description))).setText("No Description...");
        } else {
            ((TextView) (findViewById(R.id.product_detail_description))).setText(Html.fromHtml(product.getDescriptionShort()));
        }

        final List<String> productImages = new ArrayList<>();
        productImages.add(product.getImageId());

        String str = product.getMoreImages();
        String[] myStrings = str.split(",");
        for (int i = 0; i < myStrings.length; i++) {
            String marco = myStrings[i];
            if (!marco.equals("")) {
                productImages.add(marco);
            }
        }

        //Generate Web URL for the product
        //productUrl = generateUrl(product.getUsername(), product.getId(), product.getName());
        //Log.d("PRODUCTURL", productUrl);
        findViewById(R.id.product_detail_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productUrl));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ProductDetail.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }*/

                Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
                map.put("product_id", product.getId()); //map.put can be use as many time as required
                map.put("username", product.getUsername());
                map.put("product_name", product.getName());

                BackgroundThread thread = new BackgroundThread(ApiUrl.GENERATE_RRODUCT_URL, map, Connection.GET);
                thread.setBackgroundListener(ProductDetail.this, new Callback() {

                    //loading popup
                    final ProgressDialog dialog = ProgressDialog.show(ProductDetail.this, "", getString(R.string.loading), true);

                    @Override
                    public void onFailure(Request request, IOException e) {

                        //dismiss loading dialog
                        dialog.dismiss();

                    }

                    @Override
                    public void onResponse(Response response, String result) throws IOException {

                        //dismiss loading dialog
                        dialog.dismiss();

                        if(response.isSuccessful()){

                            try {
                                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                                startActivity(myIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(ProductDetail.this, "No application can handle this request."
                                        + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }

                        }

                    }
                });

                thread.execute();

            }
        });

        ViewPager imageSlider = (ViewPager) findViewById(R.id.imageSlider);
        if (imageSlider != null) {
            setupViewPager(imageSlider, 0, productImages);
        }

        findViewById(R.id.button_add_to_wishlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*findViewById(R.id.button_add_to_cart_icon).setBackgroundColor(getResources().getColor(R.color.colorAccent));*/



                YoYo.with(Techniques.Pulse).duration(400).playOn(findViewById(R.id.button_add_to_wishlist));
                try {
                    DatabaseHandler handler = new DatabaseHandler(ProductDetail.this).Open();
                    long wishlistid = handler.addItemToWishlist(product);
                    if (wishlistid > 0) {
                        //Message.snackBarShortPositive(ProductDetail.this.getWindow().getDecorView(), product.getName() + " has been added to your wishlist!");
                        //Toast.makeText(ProductDetail.this, product.getName() + " has been added to your wishlist!", Toast.LENGTH_SHORT).show();
                        Message.alertDialogSimple("Added To Wishlist!", product.getName() + " has been added to your wishlist!", ProductDetail.this);
                    }
                    handler.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                /*Bundle bundle = new Bundle();
                bundle.putBoolean("toWishList",true);
                startActivity(new Intent(ProductDetail.this, MainActivity.class).putExtras(bundle));*/
            }
        });
        findViewById(R.id.button_order_by_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*findViewById(R.id.button_add_to_cart_icon).setBackgroundColor(getResources().getColor(R.color.colorAccent));*/
                YoYo.with(Techniques.Pulse).duration(400).playOn(findViewById(R.id.button_order_by_phone));

                //loading popup
                final ProgressDialog dialog = ProgressDialog.show(ProductDetail.this, "", getString(R.string.loading), true);

                SharedPreferences sharedPreferences = getSharedPreferences(VariableNames.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
                String sessionKey = sharedPreferences.getString(VariableNames.SESSION_KEY, "");
                String username = sharedPreferences.getString(VariableNames.USERNAME, "");
                String userid = sharedPreferences.getString(VariableNames.USERID, "");

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("OrderByPhone")
                        .setAction("Username = " + username + " : SessionKey = " + sessionKey + " : userid =" + userid + " : Product = " + product.getName() + " : " + product.getProductCode())
                        .build());

                Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
                map.put("product_code", product.getProductCode()); //map.put can be use as many time as required

                BackgroundThread thread = new BackgroundThread(ApiUrl.SHOP_PHONE_URL, map, Connection.GET);
                thread.setBackgroundListener(ProductDetail.this, new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        //dismiss loading dialog
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(Response response, String result) throws IOException {
                        if (response.isSuccessful()) {
                            List<String> resultList = Arrays.asList(result.split(","));

                            final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(ProductDetail.this);

                            for (int i = 0; i < resultList.size(); i++) {
                                adapter.add(new MaterialSimpleListItem.Builder(ProductDetail.this)
                                        .content(resultList.get(i))
                                        .icon(R.drawable.ic_phone_white)
                                        .iconPadding(10)
                                        .backgroundColor(getResources().getColor(R.color.colorPrimary))
                                        .build());
                            }

                            //dismiss loading dialog
                            dialog.dismiss();

                            new MaterialDialog.Builder(ProductDetail.this)
                                    .title(R.string.order_by_phone_dialog_title_label)//R.string.order_by_phone_dialog_title_label product.getProductCode()
                                    .positiveText(R.string.order_pop_up_positive_string)
                                    .adapter(adapter, new MaterialDialog.ListCallback() {

                                        @Override
                                        public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {

                                            MaterialSimpleListItem item = adapter.getItem(i);
                                            String phone = (String) item.getContent();

                                            TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                            int simState = telMgr.getSimState();
                                            switch (simState) {
                                                case TelephonyManager.SIM_STATE_ABSENT:
                                                    Message.snackBarShortNegative(ProductDetail.this.getWindow().getDecorView(), "No SIM Available !");
                                                    break;
                                                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                                                    Message.snackBarShortNegative(ProductDetail.this.getWindow().getDecorView(), "Network locked in this device !");
                                                    break;
                                                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                                                    Message.snackBarShortNegative(ProductDetail.this.getWindow().getDecorView(), "PIN required to make a call from this device !");
                                                    break;
                                                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                                                    Message.snackBarShortNegative(ProductDetail.this.getWindow().getDecorView(), "PUK required to make a call from this device !");
                                                    break;
                                                case TelephonyManager.SIM_STATE_READY:
                                                    startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + phone)));
                                                    break;
                                                case TelephonyManager.SIM_STATE_UNKNOWN:
                                                    Message.snackBarShortNegative(ProductDetail.this.getWindow().getDecorView(), "No SIM Available !");
                                                    break;
                                            }
                                        }
                                    })
                                    .show();

                        }
                    }
                });
                thread.execute();

            }
        });
        findViewById(R.id.button_add_to_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*findViewById(R.id.button_add_to_cart_icon).setBackgroundColor(getResources().getColor(R.color.colorAccent));*/
                YoYo.with(Techniques.Pulse).duration(400).playOn(findViewById(R.id.button_add_to_cart));

                startActivity(new Intent(ProductDetail.this, CartActivity.class));

                SharedPreferences sharedPreferences = getSharedPreferences(VariableNames.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
                String sharedPreferences_sessionKey = sharedPreferences.getString(VariableNames.SESSION_KEY, "false");


                if (sharedPreferences_sessionKey.equals("false")) {

                    try {
                        DatabaseHandler handler = new DatabaseHandler(ProductDetail.this).Open();
                        BaseCartItems.CartItems cartItem = new BaseCartItems.CartItems();
                        cartItem.setId(Long.parseLong(product.getId()));
                        cartItem.setQuantity(1);
                        cartItem.setPrice(Float.parseFloat(product.getPrice()));
                        cartItem.setName(product.getName());
                        cartItem.setSellerName(product.getCompany() == null ? "" : product.getCompany());
                        cartItem.setProductImage(product.getImageId());
                        long itemid = handler.addItemToCart(cartItem);
                        if (itemid > 0) {
                            //Message.snackBarShortPositive(ProductDetail.this.getWindow().getDecorView(), product.getName() + " has been added to your cart!");
                            //Toast.makeText(ProductDetail.this, product.getName() + " has been added to your cart!", Toast.LENGTH_SHORT).show();
                        }
                        handler.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    startActivity(new Intent(ProductDetail.this, LoginActivity.class));
                } else {
                    try {
                        DatabaseHandler handler = new DatabaseHandler(ProductDetail.this).Open();
                        BaseCartItems.CartItems cartItem = new BaseCartItems.CartItems();
                        cartItem.setId(Long.parseLong(product.getId()));
                        cartItem.setQuantity(1);
                        cartItem.setPrice(Float.parseFloat(product.getPrice()));
                        cartItem.setName(product.getName());
                        cartItem.setSellerName(product.getCompany() == null ? "" : product.getCompany());
                        cartItem.setProductImage(product.getImageId());
                        long itemid = handler.addItemToCart(cartItem);
                        if (itemid > 0) {
                            //Message.snackBarShortPositive(ProductDetail.this.getWindow().getDecorView(), product.getName() + " has been added to your cart!");
                            Toast.makeText(ProductDetail.this, product.getName() + " has been added to your cart!", Toast.LENGTH_SHORT).show();
                        }
                        handler.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager, int position, List<String> productImages) {
        ProductDetailImagesSliderAdapter adapter = new ProductDetailImagesSliderAdapter(getSupportFragmentManager());

        for (int i = 0; i < productImages.size(); i++) {
            adapter.addFragment(productImages.get(i));
        }

        viewPager.setAdapter(adapter);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }


}
