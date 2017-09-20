package com.nepalicoders.nepbayapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.StaticInfo.VariableNames;
import com.nepalicoders.nepbayapp.activities.LoginActivity;
import com.nepalicoders.nepbayapp.activities.MainActivity;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.nepalicoders.nepbayapp.utils.Message;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/28/15.
 */
public class ActivationFragment extends Fragment {
    Toolbar toolbar;
    String activationCodeUser;
    String activationCodeServer = "0000";
    String username;
    String password;
    Fragment fragment;


    public ActivationFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_activation, container, false);

        username = getArguments().getString("username");
        password = getArguments().getString("password");

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Activation");

        TextView activateButton = (TextView) view.findViewById(R.id.activation_button);
        //TextView requestCode = (TextView) view.findViewById(R.id.activation_request_code);

        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activationCodeUser = ((EditText) view.findViewById(R.id.activation_edit_text)).getText().toString();

                if (activationCodeUser.equals(activationCodeServer)) {
                    Fragment fragment = new LoginFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_login_fragment, fragment).commit();
                } else {

                    Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
                    map.put("username", username); //map.put can be use as many time as required
                    map.put("activation_code", activationCodeUser);

                    BackgroundThread thread = new BackgroundThread(ApiUrl.USER_ACTIVATION_URL, map, Connection.GET);

                    thread.setBackgroundListener(getActivity(), new Callback() {

                        ProgressDialog dialog = ProgressDialog.show(getActivity(), "", getActivity().getString(R.string.activating), true);

                        @Override
                        public void onFailure(Request request, IOException e) {

                            //dismiss login popup
                            dialog.dismiss();
                            //display error snackbar
                            Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getString(R.string.no_internet));

                        }

                        @Override
                        public void onResponse(Response response, String result) throws IOException {

                            //dismiss login popup
                            dialog.dismiss();

                            if (response.isSuccessful()) {

                                if (result.equals("success")) {
                                    Message.snackBarShortPositive(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.activation_successful));

                                    Bundle pass = new Bundle();
                                    pass.putString("username",username);//passing the username value to the next fragment
                                    pass.putString("password", password);
                                    Fragment fragment = new LoginFragment();
                                    fragment.setArguments(pass);

                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_login_fragment,fragment).commit();

                                } else {

                                    Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.activation_failed));

                                }

                            }

                        }
                    });

                    thread.execute();

                }
            }
        });

        /*requestCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


        return view;

    }

}