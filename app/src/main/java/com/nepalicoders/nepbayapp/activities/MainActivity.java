package com.nepalicoders.nepbayapp.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.SQLite_database.DatabaseHandler;
import com.nepalicoders.nepbayapp.SQLite_database.DatabaseHelper;
import com.nepalicoders.nepbayapp.StaticInfo.VariableNames;
import com.nepalicoders.nepbayapp.adapters.SuggestionSimpleCursorAdapter;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.application.App;
import com.nepalicoders.nepbayapp.fragments.CategoryProductsListingFragment;
import com.nepalicoders.nepbayapp.fragments.HomeFragment;
import com.nepalicoders.nepbayapp.fragments.OrdersFragment;
import com.nepalicoders.nepbayapp.fragments.ProfileFragment;
import com.nepalicoders.nepbayapp.fragments.WishlistFragment;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.objects.Product;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.nepalicoders.nepbayapp.utils.Message;
import com.nepalicoders.nepbayapp.utils.NavItemsHideShow;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static int hot_number = 0;
    public static long back_pressed;
    private TextView ui_hot = null;

    private boolean isNavigationItemSelected = false;
    private boolean isRightDrawerNavigationItemSelected = false;
    private boolean isRightDrawerOpen = false;
    private Fragment nextFrag;

    private int navigationItemId;

    DrawerLayout drawer;
    View rightSliderBar;

    SharedPreferences sharedPreferences;
    String sharedPreferences_sessionKey;
    String sharedPreferences_username;
    String sharedPreferences_fullName;
    String sharedPreferences_email;
    String sharedPreferences_mobile;
    String sharedPreferences_userid;
    String sharedPreferences_userProfileUrl;


    NavigationView navigationView;
    Tracker mTracker;

    Toolbar toolbar;
    View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLogin();
        navigationView.getMenu().getItem(0).setChecked(true);

        sharedPreferences = getSharedPreferences(VariableNames.SHARED_PREFERENCE_NAME, MODE_PRIVATE);

        sharedPreferences.edit().putBoolean(VariableNames.FIRST_TIME, false).apply();

        /*//This code is not working currently
        Bundle notification = getIntent().getExtras();
        if (notification != null) {
            Log.d("SeeNotification", notification.getString("notification"));
            new MaterialDialog.Builder(this).title("News").positiveText(notification.getString("notification")).build().show();
        }*/


        String versionNumber = "";
        int versionCode = -1;

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionNumber = pInfo.versionName;
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mTracker = ((App) getApplication()).getDefaultTracker();
        mTracker.setScreenName("MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Device")
                .setAction("(" + versionCode + ") (" + versionNumber + ") " + " " + Build.MANUFACTURER + " " + Build.MODEL)
                .build());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        rightSliderBar = findViewById(R.id.right_sidebar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (drawerView.getId() == R.id.right_sidebar) {
                    isRightDrawerOpen = false;
                    invalidateOptionsMenu();

                    if (isRightDrawerNavigationItemSelected) { //check if right drawer list was selected
                        getSupportFragmentManager().beginTransaction().add(R.id.drawer_fragment, nextFrag).addToBackStack(null).commit();
                        isRightDrawerNavigationItemSelected = false; //reset the flag
                    }
                }

                if (isNavigationItemSelected) { // check if left drawer list was selected
                    drawerClosed(); // replace the fragment with the selected item
                    isNavigationItemSelected = false; //reset the flag
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (drawerView.getId() == R.id.right_sidebar) {
                    isRightDrawerOpen = true;

                    invalidateOptionsMenu();
                }
                checkLogin();
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        //sharedPreferences.edit().putBoolean(VariableNames.FIRST_TIME, false).apply();


        Bundle wishlistBundle = getIntent().getExtras();
        if (wishlistBundle != null) {
            if (wishlistBundle.getBoolean("toWishList")) {
                Log.d("CheckWIshList", wishlistBundle.getBoolean("toWishList") + " ok");
                //Going to wishlist from product details
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_fragment, new WishlistFragment()).commit();
                getSupportActionBar().setTitle("My Wishlist");
            } else {
                //Open the home fragment by default
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_fragment, new HomeFragment()).commit();
            }
        } else {
            //Open the home fragment by default
            getSupportFragmentManager().beginTransaction().replace(R.id.drawer_fragment, new HomeFragment()).commit();
        }


        String notification = getIntent().getStringExtra("notificationData");
        if (notification != null) {
            JSONObject notificationData = null;
            try {
                notificationData = new JSONObject(notification);
                Log.d("NotificationData", notificationData.toString());

                if (!notificationData.isNull("type")) {

                    String type = notificationData.getString("type");
                    if (type.equals("category")) {

                        Bundle pass = new Bundle();
                        pass.putString("sub_cat_ID", notificationData.getString("category_id"));//passing the clicked value to the next fragment
                        pass.putString("sub_cat_title", notificationData.getString("category_title"));//passing the clicked value to the next fragment
                        pass.putString("notification", notificationData.getString("alert"));
                        Fragment nextFrag = new CategoryProductsListingFragment();
                        nextFrag.setArguments(pass);

                        getSupportFragmentManager().beginTransaction().add(R.id.drawer_fragment, nextFrag).addToBackStack(null).commit();

                    } else if (type.equals("product")) {

                        String id = notificationData.getString("product_id");
                        final String message = notificationData.getString("alert");

                        Map<String, String> map = new HashMap<>();
                        map.put("product_id", id); //map.put can be use as many time as required

                        BackgroundThread thread = new BackgroundThread(ApiUrl.GET_RRODUCT_URL, map, Connection.GET);
                        thread.setBackgroundListener(MainActivity.this, new Callback() {

                            //loading popup
                            ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", MainActivity.this.getString(R.string.please_wait), true);

                            @Override
                            public void onFailure(Request request, IOException e) {
                                //dismiss login popup
                                dialog.dismiss();
                                //display error snackbar
                                Message.snackBarShortNegative(MainActivity.this.getWindow().getDecorView(), MainActivity.this.getString(R.string.no_internet));
                            }

                            @Override
                            public void onResponse(Response response, String result) throws IOException {

                                //dismiss login popup after it gets response
                                dialog.dismiss();

                                if (response.isSuccessful()) {

                                    Product product = new Product();

                                    try {
                                        JSONObject pro = new JSONObject(result);

                                        product.setId(pro.getString("id"));
                                        product.setProductCode(pro.getString("product_code"));
                                        product.setName(pro.getString("name"));
                                        product.setUsername(pro.getString("username"));
                                        product.setCompany(pro.getString("company"));
                                        product.setPrice(pro.getString("price"));
                                        product.setImageId(pro.getString("image_id"));
                                        product.setMoreImages(pro.getString("more_images"));
                                        product.setDescriptionShort(pro.getString("description_short"));

                                        Intent intent = new Intent(MainActivity.this, ProductDetail.class);
                                        intent.putExtra("Product", product);
                                        intent.putExtra("notification", message);
                                        startActivity(intent);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        });

                        thread.execute();

                    } else {

                        Message.alertDialogSimple("Notification", notificationData.getString("alert"), this);

                    }

                } else {

                    Message.alertDialogSimple("Notification", notificationData.getString("alert"), this);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                       /* if(!(((MainActivity)getActivity()).getSupportActionBar().getTitle().toString().equals("NepBay"))){
                            ((MainActivity)getActivity()).getSupportActionBar().setTitle("NepBay");
                        }*/
                        //getSupportActionBar().setTitle("NepBay");
                        Fragment fragment = getFragment();
                        setTitleFromFragment(fragment);
                    }
                });
    }

    private void setTitleFromFragment(Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            getSupportActionBar().setTitle("NepBay");
        } else if (fragment instanceof WishlistFragment) {
            getSupportActionBar().setTitle("My Wishlist");
        } else if (fragment instanceof ProfileFragment) {
            getSupportActionBar().setTitle("Profile");
        } else if (fragment instanceof OrdersFragment) {
            getSupportActionBar().setTitle("My Orders");
        }
    }

    private Fragment getFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.drawer_fragment);
        return fragment;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //close drawer if open
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else if (getFragmentManager().getBackStackEntryCount() == 0) {
                //close app on double back press
                if (back_pressed + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                } else {
                    Message.snackBarShortNegative(this.getWindow().getDecorView(), getString(R.string.press_again_exit));
                    back_pressed = System.currentTimeMillis();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!isRightDrawerOpen) {
            getMenuInflater().inflate(R.menu.main, menu);
            final View menu_hotlist = menu.findItem(R.id.action_cart).getActionView();
            ui_hot = (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);
            updateHotCount(hot_number);
            new MyMenuItemStuffListener(menu_hotlist, "Show hot message") {
                @Override
                public void onClick(View v) {
                    onHotlistSelected();
                }
            };

            Drawable drawable = menu.findItem(R.id.action_right_drawer).getIcon();
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, getResources().getColor(R.color.colorText));
            menu.findItem(R.id.action_right_drawer).setIcon(drawable);

            /*final View rightDrawerMenu = menu.findItem(R.id.action_right_drawer).getActionView();
            rightDrawerMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(rightSliderBar);
                }
            });*/
            // Associate searchable configuration with the SearchView
            final DatabaseHandler searchDB = new DatabaseHandler(this);
            try {
                searchDB.Open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                //Handles the search query when submitted through the actionbar
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.onActionViewCollapsed();
                    Intent intent = new Intent(MainActivity.this, SearchResult.class);
                    intent.putExtra("searchQuery", query);
                    startActivity(intent);

                    sendScreenName("Search");

                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Search - " + query)
                            .build());

                    long result = searchDB.insertSuggestion(query);
                    return result != -1;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //Log.d("SearchText", newText);
                    Boolean value = false;

                    Cursor cursor = searchDB.getSuggestions(newText);
                    Log.d("Search", cursor.getCount() + "");
                    if (cursor.getCount() > 0) {
                        String[] columns = new String[]{DatabaseHelper.FIELD_SUGGESTION};
                        int[] columnTextId = new int[]{android.R.id.text1};
                        SuggestionSimpleCursorAdapter simple = new SuggestionSimpleCursorAdapter(getBaseContext(),
                                android.R.layout.simple_list_item_1, cursor,
                                columns, columnTextId
                                , 0);
                        searchView.setSuggestionsAdapter(simple);
                        value = true;
                    } else {
                        value = false;
                    }
                    return value;
                }

            });
            searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionSelect(int position) {
                    return true;
                }

                @Override
                public boolean onSuggestionClick(int position) {
                    SQLiteCursor cursor = (SQLiteCursor) searchView.getSuggestionsAdapter().getItem(position);
                    int indexColumnSuggestion = cursor.getColumnIndex(DatabaseHelper.FIELD_SUGGESTION);

                    searchView.setQuery(cursor.getString(indexColumnSuggestion), false);
                    Intent intent = new Intent(MainActivity.this, SearchResult.class);
                    intent.putExtra("searchQuery", cursor.getString(indexColumnSuggestion));
                    startActivity(intent);

                    sendScreenName("Search");

                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Search - " + cursor.getString(indexColumnSuggestion))
                            .build());

                    long result = searchDB.insertSuggestion(cursor.getString(indexColumnSuggestion));
                    return result != -1;
                }
            });


        }
        return super.onCreateOptionsMenu(menu);
    }


    // call the updating code on the main thread,
    // so we can call this asynchronously
    public void updateHotCount(final int new_hot_number) {
        hot_number = new_hot_number;
        if (ui_hot == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_hot_number == 0 || !checkLogin()) {
                    ui_hot.setVisibility(View.INVISIBLE);
                } else {
                    ui_hot.setVisibility(View.VISIBLE);
                    ui_hot.setText(Integer.toString(new_hot_number));
                }
            }
        });
    }

    static abstract class MyMenuItemStuffListener implements View.OnClickListener, View.OnLongClickListener {
        private String hint;
        private View view;

        MyMenuItemStuffListener(View view, String hint) {
            this.view = view;
            this.hint = hint;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        abstract public void onClick(View v);

        @Override
        public boolean onLongClick(View v) {
            final int[] screenPos = new int[2];
            final Rect displayFrame = new Rect();
            view.getLocationOnScreen(screenPos);
            view.getWindowVisibleDisplayFrame(displayFrame);
            final Context context = view.getContext();
            final int width = view.getWidth();
            final int height = view.getHeight();
            final int midy = screenPos[1] + height / 2;
            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            Toast cheatSheet = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
            if (midy < displayFrame.height()) {
                cheatSheet.setGravity(Gravity.TOP | Gravity.RIGHT,
                        screenWidth - screenPos[0] - width / 2, height);
            } else {
                cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
            }
            cheatSheet.show();
            return true;
        }
    }

    //when clicked on cart icon
    public void onHotlistSelected() {

        if (checkLogin()) {
            startActivity(new Intent(this, CartActivity.class));
            sendScreenName("Cart");
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            Message.snackBarLongNegative(getWindow().getDecorView(), "Please Login First !");
        }
    }

    public void shakeCart() {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .playOn(findViewById(R.id.action_cart));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_right_drawer:
                Log.e("IsRightDrawer", isRightDrawerOpen + "");
                drawer.openDrawer(rightSliderBar);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        isNavigationItemSelected = true;
        navigationItemId = id;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        item.setChecked(true); //dont remove this

        return true;
    }

    private void drawerClosed() {
        //Replace Fragment over here!! So that the fragment is opened only when the drawer is closed so as to remove the lag during transaction

        Fragment fragment;

        switch (navigationItemId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.drawer_fragment, fragment).commit();
                getSupportActionBar().setTitle("NepBay");
                sendScreenName("Home");
                break;

            case R.id.nav_profile:

                if (checkLogin()) {
                    fragment = new ProfileFragment();
                    getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.drawer_fragment, fragment).commit();
                    getSupportActionBar().setTitle("Profile");
                    sendScreenName("Profile");

                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    sendScreenName("Login");
                }

                break;

               /* case R.id.nav_categories:
                    fragment = new CategoriesFragment();
                    getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.drawer_fragment, fragment).commit();
                    break;*/

            case R.id.nav_wishlist:
                fragment = new WishlistFragment();
                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.drawer_fragment, fragment).commit();
                getSupportActionBar().setTitle("My Wishlist");
                sendScreenName("Wishlist");
                break;

            case R.id.nav_orders:
                fragment = new OrdersFragment();
                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.drawer_fragment, fragment).commit();
                getSupportActionBar().setTitle("My Orders");
                sendScreenName("Orders");
                break;
            case R.id.nav_sell_my_products:
                if(isNavigationItemSelected){

                }
                try {
                    Intent intent_sell = new Intent(Intent.ACTION_VIEW, Uri.parse("https://nepbay.com/index.php?mod=marketplaces_addproduct"));
                    startActivity(intent_sell);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                break;
            case R.id.nav_advertising_solutions:
                try {
                    Intent intent_sell = new Intent(Intent.ACTION_VIEW, Uri.parse("https://nepbay.com/advertising/"));
                    startActivity(intent_sell);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            case R.id.nav_web_services:
                try {
                    Intent intent_sell = new Intent(Intent.ACTION_VIEW, Uri.parse("http://nepbay.com/Web+Services.html"));
                    startActivity(intent_sell);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            case R.id.nav_nepbay_bazar:
                try {
                    Intent intent_sell = new Intent(Intent.ACTION_VIEW, Uri.parse("http://nepbay.com/NepBay+Bazar.html"));
                    startActivity(intent_sell);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            case R.id.nav_community:
                try {
                    Intent intent_sell = new Intent(Intent.ACTION_VIEW, Uri.parse("https://nepbay.com/community/"));
                    startActivity(intent_sell);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            case R.id.nav_ecommerce_solutions:
                try {
                    Intent intent_sell = new Intent(Intent.ACTION_VIEW, Uri.parse("http://nepbay.com/Shop+Manager.html"));
                    startActivity(intent_sell);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            case R.id.nav_about_nepbay:
                try {
                    Intent intent_sell = new Intent(Intent.ACTION_VIEW, Uri.parse("http://nepbay.com/About+Us.html"));
                    startActivity(intent_sell);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            case R.id.nav_shipping:
                try {
                    Intent intent_sell = new Intent(Intent.ACTION_VIEW, Uri.parse("http://nepbay.com/Express+Delivery.html"));
                    startActivity(intent_sell);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            case R.id.nav_customer_support:
                try {
                    Intent intent_sell = new Intent(Intent.ACTION_VIEW, Uri.parse("http://nepbay.com/Customer+Support.html"));
                    startActivity(intent_sell);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;

            case R.id.nav_logout:


                //delete session key and password
                sharedPreferences.edit().remove(VariableNames.SESSION_KEY).apply();
                sharedPreferences.edit().remove(VariableNames.USERNAME).apply();
                sharedPreferences.edit().remove(VariableNames.EMAIL).apply();
                sharedPreferences.edit().remove(VariableNames.USERID).apply();
                sharedPreferences.edit().remove(VariableNames.FULL_NAME).apply();
                sharedPreferences.edit().remove(VariableNames.USER_ADDRESS_COUNTRY).apply();
                sharedPreferences.edit().remove(VariableNames.USER_ADDRESS_STATE).apply();
                sharedPreferences.edit().remove(VariableNames.USER_ADDRESS_CITY).apply();
                sharedPreferences.edit().remove(VariableNames.USER_ADDRESS_ZIP).apply();
                sharedPreferences.edit().remove(VariableNames.USER_ADDRESS_1).apply();
                sharedPreferences.edit().remove(VariableNames.USER_ADDRESS_2).apply();
                sharedPreferences.edit().remove(VariableNames.USER_MOBILE).apply();
                sharedPreferences.edit().remove(VariableNames.USER_PHONE).apply();
                Message.snackBarShortPositive(getWindow().getDecorView(), getResources().getString(R.string.logged_out));

                //hide items form nav drawer
                NavItemsHideShow.navItemsHide(navigationView);

                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateCartCounter(false);
        checkLogin();
    }

    public void updateCartCounter(boolean shakeCart) {
        try {
            DatabaseHandler handler = new DatabaseHandler(this).Open();
            Cursor cursor = handler.getAllItemsFromCart();
            if (cursor != null && cursor.getCount() > 0) {
                hot_number = cursor.getCount();
            } else {
                hot_number = 0;
            }
            if (shakeCart) {
                shakeCart();
            }
            updateHotCount(hot_number);
            handler.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Called when item on right drawer is selected
    public void onRightDrawerNavigationItemSelected(Fragment nextFrag, String categoryName) {
        isRightDrawerNavigationItemSelected = true;
        this.nextFrag = nextFrag;
        getSupportActionBar().setTitle(categoryName);
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isChecked()) {
                menu.getItem(i).setChecked(false);//removes selection from navigation view
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.END);
    }

    public void sendScreenName(String name) {
        mTracker = ((App) getApplication()).getDefaultTracker();
        mTracker.setScreenName(name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public boolean checkLogin() {

        sharedPreferences = this.getSharedPreferences(VariableNames.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sharedPreferences_sessionKey = sharedPreferences.getString(VariableNames.SESSION_KEY, "false");
        sharedPreferences_username = sharedPreferences.getString(VariableNames.USERNAME, "");
        sharedPreferences_fullName = sharedPreferences.getString(VariableNames.FULL_NAME, "NepBay");
        sharedPreferences_email = sharedPreferences.getString(VariableNames.EMAIL, "www.nepabay.com");
        sharedPreferences_mobile = sharedPreferences.getString(VariableNames.USER_MOBILE, "");
        sharedPreferences_userid = sharedPreferences.getString(VariableNames.USERID, "");
        sharedPreferences_userProfileUrl = sharedPreferences.getString(VariableNames.USER_PROFILE_URL, "");

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        header = navigationView.getHeaderView(0);

        if (sharedPreferences_sessionKey.equals("false")) {
            ((ImageView) header.findViewById(R.id.drawer_nav_header_image)).setImageResource(R.drawable.ic_nepbay_user);
            ((TextView) header.findViewById(R.id.drawer_nav_header_name)).setText("NepBay");
            ((TextView) header.findViewById(R.id.drawer_nav_header_mobile)).setText("www.nepbay.com");

            //hide nav drawer items
            NavItemsHideShow.navItemsHide(navigationView);

            return false;

        } else {
            //Setting full name and email in nav header of left drawer after user logs in

            Picasso.with(MainActivity.this)
                    .load(sharedPreferences_userProfileUrl)
                    .error(R.drawable.ic_nepbay_user)
                    .placeholder(R.drawable.ic_nepbay_user)
                    .into((CircleImageView) header.findViewById(R.id.drawer_nav_header_image));

            ((TextView) header.findViewById(R.id.drawer_nav_header_name)).setText(sharedPreferences_fullName);
           /* ((TextView) header.findViewById(R.id.drawer_nav_header_name)).setText(sharedPreferences_fullName);*/
            ((TextView) header.findViewById(R.id.drawer_nav_header_mobile)).setText(sharedPreferences_mobile);

            //show nav drawer items
            NavItemsHideShow.navItemsShow(navigationView);

            return true;

        }
    }

}
