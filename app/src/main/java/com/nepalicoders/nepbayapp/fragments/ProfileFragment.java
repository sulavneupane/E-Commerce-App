package com.nepalicoders.nepbayapp.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.StaticInfo.VariableNames;
import com.nepalicoders.nepbayapp.activities.MainActivity;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.json.JsonParser;
import com.nepalicoders.nepbayapp.objects.UserInfo;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.nepalicoders.nepbayapp.utils.Message;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    Fragment fragment;

    SharedPreferences sharedPreferences;
    String sharedPreferencesSessionKey;
    String sharedPreferencesUsername;

    String username;
    String firstName;
    String lastName;
    String email;
    String addressCountry;
    String addressState;
    String addressCity;
    String addressZip;
    String address1;
    String address2;
    String mobile;
    String phone;

    String totalOrder;
    String approvedOrder;
    String pendingOrder;
    String rejectedOrder;
    LinearLayout profileView;
    TextView noInternetMessage;

    CircularProgressView pd;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_drawer_profile, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Profile");
        //animation for profile fragment
        YoYo.with(Techniques.FadeInLeft).duration(700).playOn(view);

        pd = (CircularProgressView) view.findViewById(R.id.loading);

        TextView edit = (TextView) view.findViewById(R.id.user_edit_button);

        profileView = (LinearLayout) view.findViewById(R.id.profile_layout);
        noInternetMessage = (TextView) view.findViewById(R.id.noInternetMessage);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Pulse).duration(400).playOn(getActivity().findViewById(R.id.user_edit_button));
                Fragment frag;
                frag = new EditProfileFragment();

                //create bundle of userinfo
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("first_name", firstName);
                bundle.putString("last_name", lastName);
                bundle.putString("email", email);
                bundle.putString("mobile", mobile);
                bundle.putString("phone", phone);
                bundle.putString("billing_address_country", addressCountry);
                bundle.putString("billing_address_state", addressState);
                bundle.putString("billing_address_city", addressCity);
                bundle.putString("billing_address_zip", addressZip);
                bundle.putString("billing_address_1", address1);
                bundle.putString("billing_address_2", address2);
                frag.setArguments(bundle);

                //inflate editProfile fragment
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.drawer_fragment, frag).addToBackStack(null).commit();
            }
        });

        //open my orders fragment on clicking total orders in profile fragment
        view.findViewById(R.id.profile_orders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new OrdersFragment();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.drawer_fragment, fragment).addToBackStack(null).commit();

                //Highlight My Orders Item of Drawer
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                navigationView.getMenu().getItem(3).setChecked(true);
            }
        });

        sharedPreferences = getActivity().getSharedPreferences(VariableNames.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sharedPreferencesSessionKey = sharedPreferences.getString(VariableNames.SESSION_KEY, "false");
        sharedPreferencesUsername = sharedPreferences.getString(VariableNames.USERNAME, "false");

        Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
        map.put("sessionkey", sharedPreferencesSessionKey); //map.put can be use as many time as required

        BackgroundThread thread = new BackgroundThread(ApiUrl.USER_PROFILE_URL, map, Connection.GET);
        thread.setBackgroundListener(getActivity(), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //dismiss loader and set no internet message
                pd.setVisibility(View.GONE);
                noInternetMessage.setVisibility(View.VISIBLE);

            }

            @Override
            public void onResponse(Response response, String result) throws IOException {
                //dismiss loader and display profile info
                pd.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    if (result.equals("Not Logged In")) {
                        Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getString(R.string.not_logged_in));
                    } else {
                        UserInfo userInfo = JsonParser.parseUserInfo(result);

                        username = userInfo.getUsername();
                        firstName = userInfo.getFirstName();
                        lastName = userInfo.getLastName();
                        email = userInfo.getEmail();
                        addressCountry = userInfo.getCountry();
                        addressState = userInfo.getState();
                        addressCity = userInfo.getCity();
                        addressZip = userInfo.getZip();
                        address1 = userInfo.getAddress1();
                        address2 = userInfo.getAddress2();
                        mobile = userInfo.getMobileNo();
                        phone = userInfo.getPhone();

                        totalOrder = userInfo.getTotalOrders();
                        approvedOrder = userInfo.getApprovedOrders();
                        pendingOrder = userInfo.getPendingOrders();
                        rejectedOrder = userInfo.getRejectedOrders();

                        ((TextView) view.findViewById(R.id.username)).setText(username);
                        ((TextView) view.findViewById(R.id.first_name)).setText(firstName);
                        ((TextView) view.findViewById(R.id.last_name)).setText(lastName);
                        ((TextView) view.findViewById(R.id.email)).setText(email);

                        ((TextView) view.findViewById(R.id.billing_address_country)).setText(addressCountry);
                        ((TextView) view.findViewById(R.id.billing_address_state)).setText(addressState);
                        ((TextView) view.findViewById(R.id.billing_address_city)).setText(addressCity);
                        ((TextView) view.findViewById(R.id.billing_address_zip)).setText(addressZip);
                        ((TextView) view.findViewById(R.id.billing_address_1)).setText(address1);
                        ((TextView) view.findViewById(R.id.billing_address_2)).setText(address2);
                        ((TextView) view.findViewById(R.id.mobile)).setText(mobile);
                        ((TextView) view.findViewById(R.id.phone)).setText(phone);

                        ((TextView) view.findViewById(R.id.total_order)).setText(totalOrder);
                        ((TextView) view.findViewById(R.id.approved_order)).setText(approvedOrder);
                        ((TextView) view.findViewById(R.id.pending_order)).setText(pendingOrder);
                        ((TextView) view.findViewById(R.id.rejected_order)).setText(rejectedOrder);

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(VariableNames.SHARED_PREFERENCE_NAME_USER_ADDRESS, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(VariableNames.USERNAME, username);
                        editor.putString(VariableNames.FULL_NAME, firstName + " " + lastName);
                        editor.putString(VariableNames.FIRST_NAME, firstName);
                        editor.putString(VariableNames.LAST_NAME, lastName);
                        editor.putString(VariableNames.USER_ADDRESS_COUNTRY, addressCountry);
                        editor.putString(VariableNames.USER_ADDRESS_STATE, addressState);
                        editor.putString(VariableNames.USER_ADDRESS_CITY, addressCity);
                        editor.putString(VariableNames.USER_ADDRESS_ZIP, addressZip);
                        editor.putString(VariableNames.USER_ADDRESS_1, address1);
                        editor.putString(VariableNames.USER_ADDRESS_2, address2);
                        editor.putString(VariableNames.USER_MOBILE, mobile);
                        editor.putString(VariableNames.USER_PHONE, phone);
                        editor.apply();

                        profileView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        thread.execute();

        return view;
    }

}
