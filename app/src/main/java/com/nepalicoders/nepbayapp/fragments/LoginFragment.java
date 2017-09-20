package com.nepalicoders.nepbayapp.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.StaticInfo.VariableNames;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.nepalicoders.nepbayapp.utils.Message;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    String username;
    String password;
    String sessionKey;
    String userid;
    String fullName;
    String userProfileUrl;
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

    private String active;
    private View view;
    private TextView loginButton;
    private TextView registerLink;

    JSONObject loginInfo;
    JSONObject userInfo;

    Fragment fragment;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        try {
            username = getArguments().getString("username");
            password = getArguments().getString("password");
        } catch (Exception ex) {
            ex.getMessage();
        }

        if (username != null && password != null) {
            login(username, password);

        } else {

            loginButton = (TextView) view.findViewById(R.id.login_button);
            registerLink = (TextView) view.findViewById(R.id.not_yet_user);


            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.Pulse).duration(200).playOn(getActivity().findViewById(R.id.login_button));

                    username = ((EditText) view.findViewById(R.id.login_username)).getText().toString();
                    password = ((EditText) view.findViewById(R.id.login_password)).getText().toString();

                    //hide softkeys on button click
                    closeKeyboard1(getActivity(), loginButton.getWindowToken());

                    if (username.equals("") || password.equals("")) {

                        YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.login_form_linear_layout));
                        Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.fill_all_fields));
                    } else {
                        login(username, password);
                    }

                }
            });

            registerLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment = new RegisterFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_login_fragment, fragment).commit();
                }
            });

        }

        return view;
    }

    //method to hide softkeys on button click
    public static void closeKeyboard1(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    public void login(final String username_inside, final String password_inside) {
        Log.d("RESULT", "inside login");

        Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
        map.put("username", username_inside); //map.put can be use as many time as required
        map.put("password", password_inside); //map.put can be use as many time as required

        BackgroundThread thread = new BackgroundThread(ApiUrl.USER_LOGIN_URL, map, Connection.GET);
        thread.setBackgroundListener(getActivity(), new Callback() {

            //loading popup
            ProgressDialog dialog = ProgressDialog.show(getActivity(), "", getActivity().getString(R.string.logging_in), true);

            @Override
            public void onFailure(Request request, IOException e) {
                //dismiss login popup
                dialog.dismiss();
                //display error snackbar
                Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getString(R.string.no_internet));
            }

            @Override
            public void onResponse(Response response, String result) throws IOException {
                if (response.isSuccessful()) {

                    try {
                        loginInfo = new JSONObject(result);
                        userInfo = loginInfo.getJSONObject("user_info");
                        active = userInfo.getString("Active");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //dismiss login popup after it gets response
                    dialog.dismiss();

                    Log.d("RESULT", result);
                    if (result.equals("Incorrect username or password")) {
                        //animation
                        YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.login_form_linear_layout));

                        Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.username_password_incorrect));

                    } else if (!active.equals("1")) {
                        //redirect to activation fragment
                        Bundle pass = new Bundle();
                        pass.putString("username", username_inside);//passing the username value to the next fragment
                        pass.putString("password", password_inside);

                        fragment = new ActivationFragment();
                        fragment.setArguments(pass);

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_login_fragment, fragment).commit();

                    } else {
                        Log.d("RESULT", "login saved");

                        try {

                            //Json parser for login
                            fullName = userInfo.getString("FirstName") + " " + userInfo.getString("LastName");
                            email = userInfo.getString("Email");

                            //adding username and session key to SharedPreferences
                            username = userInfo.getString("UserName");
                            sessionKey = loginInfo.getString("session_key");
                            userid = userInfo.getString("UID");

                            userProfileUrl = URLDecoder.decode(userInfo.getString("profile"), "UTF-8");
                            /*UserInfo userInfo = JsonParser.parseUserInfo(result);*/

                            firstName = userInfo.getString("FirstName");
                            lastName = userInfo.getString("LastName");
                            email = userInfo.getString("Email");
                            addressCountry = userInfo.getString("Country");
                            addressState = userInfo.getString("State");
                            addressCity = userInfo.getString("City");
                            addressZip = userInfo.getString("ZIP");
                            address1 = userInfo.getString("Address1");
                            address2 = userInfo.getString("Address2");
                            mobile = userInfo.getString("Mobile_no");
                            phone = userInfo.getString("Phone");

                        } catch (Exception ex) {
                            ex.getMessage();
                        }

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(VariableNames.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(VariableNames.USERNAME, username);
                        editor.putString(VariableNames.SESSION_KEY, sessionKey);
                        editor.putString(VariableNames.FULL_NAME, fullName);
                        editor.putString(VariableNames.EMAIL, email);
                        editor.putString(VariableNames.USERID, userid);
                        editor.putString(VariableNames.USER_PROFILE_URL, userProfileUrl);
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

                        //animation
                        YoYo.with(Techniques.FadeOutLeft).duration(400).playOn(view);

                        getActivity().finish();

                    }
                }
            }
        });

        thread.execute();

    }

}