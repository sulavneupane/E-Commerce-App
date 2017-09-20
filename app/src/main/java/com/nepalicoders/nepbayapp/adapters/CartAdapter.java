package com.nepalicoders.nepbayapp.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.SQLite_database.DatabaseHandler;
import com.nepalicoders.nepbayapp.StaticInfo.VariableNames;
import com.nepalicoders.nepbayapp.activities.CartActivity;
import com.nepalicoders.nepbayapp.activities.MainActivity;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.interfaces.CartListener;
import com.nepalicoders.nepbayapp.objects.BaseCartItems;
import com.nepalicoders.nepbayapp.objects.Product;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.nepalicoders.nepbayapp.utils.Message;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sabin on 12/3/15.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    public List<BaseCartItems.CartItems> items;
    public BaseCartItems baseCartItems;
    DatabaseHandler handler;

    private static final int VIEW_TYPE_ITEM_LIST = 0;
    private static final int VIEW_TYPE_TOTAL = 1;
    private static final int VIEW_TYPE_EMPTY = 2;

    private CartListener onQuantityChangeListener;

    private TotalViewHolder mTotalviewHolder;

    public void setOnQuantiyChangeListener(CartListener onQuantityChangeListener) {
        this.onQuantityChangeListener = onQuantityChangeListener;
    }

    public CartAdapter(Context mContext, BaseCartItems baseCartItems) {
        this.mContext = mContext;
        this.baseCartItems = baseCartItems;
        items = baseCartItems.getItems();

        //initializing handler(to remove all items from cart)
        try {
            handler = new DatabaseHandler(mContext).Open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() > 0) {
            if (position != items.size()) {
                return VIEW_TYPE_ITEM_LIST;
            } else {
                return VIEW_TYPE_TOTAL;
            }
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case VIEW_TYPE_ITEM_LIST:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_cart_item_design, parent, false);
                holder = new ListViewHolder(v);
                break;
            case VIEW_TYPE_TOTAL:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_cart_total_design, parent, false);
                holder = new TotalViewHolder(v);
                break;
            case VIEW_TYPE_EMPTY:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_cart_empty, parent, false);
                holder = new EmptyViewHolder(v);
                break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_ITEM_LIST:
                final BaseCartItems.CartItems items = getItem(position);
                ListViewHolder listViewHolder = (ListViewHolder) holder;
                listViewHolder.sellerName.setText(Html.fromHtml("By: " + items.getSellerName()));
                listViewHolder.productPrice.setText("Rs. " + String.format("%.2f", items.getPrice()));
                listViewHolder.productName.setText(Html.fromHtml(items.getName()));
                listViewHolder.etQuantity.setText(items.getQuantity() + "");
                Picasso.with(mContext).load(ApiUrl.NEPBAY_PRODUCT_IMAGES_URL_TEMPORARY + items.getProductImage() + "&thumb=1").into(listViewHolder.icon);
                if (position == this.items.size() - 1) {
                    listViewHolder.border.setVisibility(View.GONE);
                }
                break;

            case VIEW_TYPE_TOTAL:
                final TotalViewHolder totalViewHolder = (TotalViewHolder) holder;
                // totalViewHolder.totalQuantity.setText("" + baseCartItems.getTotalQuantity());
                totalViewHolder.totalPrice.setText("Rs. " + String.format("%.2f", baseCartItems.getTotalPrice()));
                // totalViewHolder.totalProduct.setText("" + baseCartItems.getTotalProducts());

                final SharedPreferences sharedPreferences;
                sharedPreferences = mContext.getSharedPreferences(VariableNames.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
                /*if(sharedPreferences.getString(VariableNames.SESSION_KEY,"flase").equals("flase")){

                }*/
                String shippingCountry = sharedPreferences.getString(VariableNames.USER_ADDRESS_COUNTRY, "");
                String shippingState = sharedPreferences.getString(VariableNames.USER_ADDRESS_STATE, "");
                String shippingCity = sharedPreferences.getString(VariableNames.USER_ADDRESS_CITY, "");
                String shippingZip = sharedPreferences.getString(VariableNames.USER_ADDRESS_ZIP, "");
                String shippingAddress1 = sharedPreferences.getString(VariableNames.USER_ADDRESS_1, "");
                String shippingAddress2 = sharedPreferences.getString(VariableNames.USER_ADDRESS_2, "");
                String shippingMobile = sharedPreferences.getString(VariableNames.USER_MOBILE, "");
                String shippingPhone = sharedPreferences.getString(VariableNames.USER_PHONE, "");

                totalViewHolder.shippingCountry.setText(shippingCountry);
                totalViewHolder.shippingState.setText(shippingState);
                totalViewHolder.shippingCity.setText(shippingCity);
                totalViewHolder.shippingZip.setText(shippingZip);
                totalViewHolder.shippingAddress1.setText(shippingAddress1);
                totalViewHolder.shippingAddress2.setText(shippingAddress2);
                totalViewHolder.shippingMobile.setText(shippingMobile);
                totalViewHolder.shippingPhone.setText(shippingPhone);


                totalViewHolder.placeOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YoYo.with(Techniques.Pulse).duration(400).playOn(totalViewHolder.placeOrder);

                        String shippingAddressCountry = totalViewHolder.shippingCountry.getText().toString();
                        String shippingAddressState = totalViewHolder.shippingState.getText().toString();
                        String shippingAddressCity = totalViewHolder.shippingCity.getText().toString();
                        String shippingAddressZip = totalViewHolder.shippingZip.getText().toString();
                        String shippingAddressAddress1 = totalViewHolder.shippingAddress1.getText().toString();
                        String shippingAddressAddress2 = totalViewHolder.shippingAddress2.getText().toString();
                        String shippingMobile = totalViewHolder.shippingMobile.getText().toString();
                        String shippingPhone = totalViewHolder.shippingPhone.getText().toString();

                        String username = sharedPreferences.getString(VariableNames.USERNAME, "");
                        String userid = sharedPreferences.getString(VariableNames.USERID, "");
                        String firstName = sharedPreferences.getString(VariableNames.FIRST_NAME, "");
                        String lastName = sharedPreferences.getString(VariableNames.LAST_NAME, "");

                        //int totalProducts = baseCartItems.getTotalProducts();
                        //int totalQuantity = baseCartItems.getTotalQuantity();
                        //double totalPrice = baseCartItems.getTotalPrice();

                        JSONArray array = new JSONArray();
                        List<BaseCartItems.CartItems> products = baseCartItems.getItems();
                        for (int i = 0; i < products.size(); i++) {
                            try {
                                JSONObject product = new JSONObject();
                                BaseCartItems.CartItems item = products.get(i);
                                product.put("name", item.getName());
                                product.put("price", item.getPrice());
                                product.put("quantity", item.getQuantity());
                                product.put("id", item.getId());
                                array.put(product);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //loading popup
                        final ProgressDialog dialog = ProgressDialog.show(mContext, "", mContext.getString(R.string.placing_order), true);


                        Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
                        map.put("userid", userid); //map.put can be use as many time as required
                        map.put("username", username);
                        map.put("first_name", firstName);
                        map.put("last_name", lastName);
                        map.put("country", shippingAddressCountry);
                        map.put("state", shippingAddressState);
                        map.put("zip", shippingAddressZip);
                        map.put("city", shippingAddressCity);
                        map.put("address1", shippingAddressAddress1);
                        map.put("address2", shippingAddressAddress2);
                        map.put("mobile", shippingMobile);
                        map.put("phone", shippingPhone);
                        map.put("products_array", array.toString());

                        BackgroundThread thread = new BackgroundThread(ApiUrl.PLACE_ORDER_URL, map, Connection.GET);
                        thread.setBackgroundListener(mContext, new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                dialog.dismiss();
                            }

                            @Override
                            public void onResponse(Response response, String result) throws IOException {
                                if (response.isSuccessful()) {
                                    dialog.dismiss();
                                    if (result.equals("order_success")) {

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                                        // set title
                                        alertDialogBuilder.setTitle("Success");
                                        // set dialog message
                                        alertDialogBuilder
                                                .setMessage("Your order has been placed!")
                                                .setCancelable(false)


                                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // if this button is clicked, just close
                                                        // the dialog box and do nothing
                                                        //remove cart items form cart when order is placed successfully and redirect to home activity
                                                        handler.removeAllItemsFromCart();

                                                        dialog.cancel();

                                                        Intent intent = new Intent(mContext, MainActivity.class);
                                                        mContext.startActivity(intent);
                                                    }
                                                });
                                        // create alert dialog
                                        AlertDialog alertDialog = alertDialogBuilder.create();

                                        // show it
                                        alertDialog.show();
                                    } else {
                                        Message.alertDialogSimple("Failed", "Your order has not been placed!", mContext);
                                    }
                                }
                            }
                        });
                        thread.execute();
                        try {
                            Log.d("CartActivity", String.valueOf(new JSONArray(baseCartItems)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                mTotalviewHolder = totalViewHolder;
                break;

            case VIEW_TYPE_EMPTY:
                ((CartListener) mContext).showEmptyCart();
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (items != null && items.size() > 0) {
            return items.size() + 1; // Extra 1 count to display the Total View when there is some items in the cart
        } else {
            return 1;
        }
    }

    public BaseCartItems.CartItems getItem(int position) {
        return items.get(position);

    }

    public BaseCartItems getBaseCartItems() {
        return baseCartItems;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView productName, productPrice, sellerName;
        EditText etQuantity;
        TableRow border;
        Toolbar toolbar;

        public ListViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.cart_product_icon);
            productName = (TextView) itemView.findViewById(R.id.cart_item_name);
            productPrice = (TextView) itemView.findViewById(R.id.cart_item_price);
            sellerName = (TextView) itemView.findViewById(R.id.cart_item_seller);
            etQuantity = (EditText) itemView.findViewById(R.id.cart_et_qty);
            toolbar = (Toolbar) itemView.findViewById(R.id.cart_toolbar);
            border = (TableRow) itemView.findViewById(R.id.border);

            etQuantity.setImeActionLabel("Confirm", KeyEvent.KEYCODE_ENTER);
            /*etQuantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == KeyEvent.KEYCODE_ENTER) {
                        if (onQuantityChangeListener != null && !etQuantity.getText().toString().isEmpty())
                            onQuantityChangeListener.onQuantitychange(Integer.parseInt(v.getText().toString()), getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });*/

          /*  etQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // code to execute when EditText loses focus
                        if (onQuantityChangeListener != null && !etQuantity.getText().toString().isEmpty())
                            Log.e("rockit", etQuantity.getText().toString() + ", " + getAdapterPosition());
                            onQuantityChangeListener.onQuantitychange(Integer.parseInt(etQuantity.getText().toString()), getAdapterPosition());
                    }
                }
            });*/

            //This is used to calculate as soon as the text changes but keyboard is reset in this process
            etQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (onQuantityChangeListener != null) {
                        String value = s.toString().isEmpty() ? "0" : s.toString();
                        onQuantityChangeListener.onQuantitychange(Integer.parseInt(value), getAdapterPosition(), mTotalviewHolder);
                    }
                }
            });

            if (toolbar != null) {
                // inflate your menu
                if (toolbar.getMenu().size() == 0) {
                    toolbar.inflateMenu(R.menu.cart_item_menu);
                }
                toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_remove:
                                ((CartListener) mContext).onItemRemove(getItem(getAdapterPosition()).getId(), getAdapterPosition(), mTotalviewHolder);
                                break;

                            case R.id.action_add_wishlist:
                                try {
                                    DatabaseHandler handler = new DatabaseHandler(mContext).Open();
                                    BaseCartItems.CartItems cartItems = getItem(getAdapterPosition());

                                    Product product = new Product();
                                    product.setName(cartItems.getName());
                                    product.setImageId(cartItems.getProductImage());
                                    product.setCompany(cartItems.getSellerName());
                                    product.setId(String.valueOf(cartItems.getId()));
                                    product.setPrice(String.valueOf(cartItems.getPrice()));

                                    long wishlistid = handler.addItemToWishlist(product);
                                    if (wishlistid > 0) {
                                        Message.snackBarShortPositive(((CartActivity) mContext).getWindow().getDecorView(), product.getName() + " has been added to your wishlist!");
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
    }

    public class TotalViewHolder extends RecyclerView.ViewHolder {

        public TextView totalProduct, totalQuantity, totalPrice;
        public EditText shippingCountry, shippingState, shippingCity, shippingZip, shippingAddress1, shippingAddress2, shippingMobile, shippingPhone;
        public View placeOrder;

        public TotalViewHolder(View v) {
            super(v);
            //totalProduct = (TextView) v.findViewById(R.id.total_products);
            //totalQuantity = (TextView) v.findViewById(R.id.total_quantity);
            totalPrice = (TextView) v.findViewById(R.id.total_price);

            shippingCountry = (EditText) itemView.findViewById(R.id.order_process_shipping_address_country);
            shippingState = (EditText) itemView.findViewById(R.id.order_process_shipping_address_state);
            shippingCity = (EditText) itemView.findViewById(R.id.order_process_shipping_address_city);
            shippingZip = (EditText) itemView.findViewById(R.id.order_process_shipping_address_zip);
            shippingAddress1 = (EditText) itemView.findViewById(R.id.order_process_shipping_address_1);
            shippingAddress2 = (EditText) itemView.findViewById(R.id.order_process_shipping_address_2);
            shippingMobile = (EditText) itemView.findViewById(R.id.order_process_shipping_mobile);
            shippingPhone = (EditText) itemView.findViewById(R.id.order_process_shipping_phone);

            placeOrder = itemView.findViewById(R.id.order_process_place_order);
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {

        public TextView totalProduct, totalQuantity, totalPrice;

        public EmptyViewHolder(View v) {
            super(v);
        }
    }
}
