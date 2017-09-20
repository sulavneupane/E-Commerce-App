package com.nepalicoders.nepbayapp.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/10/15.
 */
public class EditProfileFragment extends Fragment {

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
    MaterialDialog passwordChangeDialog;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        //animation for edit profile fragment
        YoYo.with(Techniques.FadeIn).duration(400).playOn(view);

        //get string form bundle
        username = getArguments().getString("username");
        firstName = getArguments().getString("first_name");
        lastName = getArguments().getString("last_name");
        email = getArguments().getString("email");
        mobile = getArguments().getString("mobile");
        phone = getArguments().getString("phone");
        addressCountry = getArguments().getString("billing_address_country");
        addressState = getArguments().getString("billing_address_state");
        addressCity = getArguments().getString("billing_address_city");
        addressZip = getArguments().getString("billing_address_zip");
        address1 = getArguments().getString("billing_address_1");
        address2 = getArguments().getString("billing_address_2");

        //set text to textview and edit text
        ((TextView) view.findViewById(R.id.edit_username)).setText(username);
        ((EditText) view.findViewById(R.id.edit_first_name)).setText(firstName, TextView.BufferType.EDITABLE);
        ((EditText) view.findViewById(R.id.edit_last_name)).setText(lastName, TextView.BufferType.EDITABLE);
        ((TextView) view.findViewById(R.id.edit_email)).setText(email);
        ((TextView) view.findViewById(R.id.edit_mobile)).setText(mobile);
        ((TextView) view.findViewById(R.id.edit_phone)).setText(phone);

        ((EditText) view.findViewById(R.id.edit_billing_address_country)).setText(addressCountry, TextView.BufferType.EDITABLE);
        ((EditText) view.findViewById(R.id.edit_billing_address_state)).setText(addressState, TextView.BufferType.EDITABLE);
        ((EditText) view.findViewById(R.id.edit_billing_address_city)).setText(addressCity, TextView.BufferType.EDITABLE);
        ((EditText) view.findViewById(R.id.edit_billing_address_zip)).setText(addressZip, TextView.BufferType.EDITABLE);
        ((EditText) view.findViewById(R.id.edit_billing_address_1)).setText(address1, TextView.BufferType.EDITABLE);
        ((EditText) view.findViewById(R.id.edit_billing_address_2)).setText(address2, TextView.BufferType.EDITABLE);

        //on save button click
        TextView save = (TextView) view.findViewById(R.id.user_save_text);
        TextView cancel = (TextView) view.findViewById(R.id.user_cancel_text);
        ImageView editPassword = (ImageView) view.findViewById(R.id.edit_password_button);

        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrapInScrollView = true;
                passwordChangeDialog = new MaterialDialog.Builder(getActivity())
                        .title("Change Password")
                        .customView(R.layout.fragment_edit_password_pop_up, wrapInScrollView)
                        .positiveText("ok")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                changePassword();
                            }
                        })
                        .show();

            }
        });

        //on edit click
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Pulse).duration(400).playOn(view.findViewById(R.id.user_save_text));
                //get text from fields
                firstName = ((EditText) view.findViewById(R.id.edit_first_name)).getText().toString();
                lastName = ((EditText) view.findViewById(R.id.edit_last_name)).getText().toString();
                phone = ((EditText) view.findViewById(R.id.edit_phone)).getText().toString();

                addressCountry = ((EditText) view.findViewById(R.id.edit_billing_address_country)).getText().toString();
                addressState = ((EditText) view.findViewById(R.id.edit_billing_address_state)).getText().toString();
                addressCity = ((EditText) view.findViewById(R.id.edit_billing_address_city)).getText().toString();
                addressZip = ((EditText) view.findViewById(R.id.edit_billing_address_zip)).getText().toString();
                address1 = ((EditText) view.findViewById(R.id.edit_billing_address_1)).getText().toString();
                address2 = ((EditText) view.findViewById(R.id.edit_billing_address_2)).getText().toString();

                //hasmap to store text form above fields for API
                Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
                //map.put can be use as many time as required
                map.put("username", username);
                map.put("first_name", firstName);
                map.put("last_name", lastName);
                map.put("phone", phone);
                map.put("address_country", addressCountry);
                map.put("address_state", addressState);
                map.put("address_city", addressCity);
                map.put("address_zip", addressZip);
                map.put("address_1", address1);
                map.put("address_2", address2);

                BackgroundThread thread = new BackgroundThread(ApiUrl.USER_PROFILE_UPDATE_URL, map, Connection.GET);
                thread.setBackgroundListener(getActivity(), new Callback() {
                    //loading popup
                    ProgressDialog dialog = ProgressDialog.show(getActivity(), "", getString(R.string.updating), true);

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

                        if(response.isSuccessful()){
                            if (result.equals("user_info_update_success")) {
                           /* ProgressDialog.show(getActivity(), "Loading", "Loading...");*/
                                Message.snackBarShortPositive(getActivity().getWindow().getDecorView(), getActivity().getString(R.string.profile_update_success));
                            } else {
                                Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getString(R.string.profile_update_not_success));
                            }
                            //inflate Profile fragment
                            Fragment fragment = new ProfileFragment();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_fragment, fragment).commit();
                        }
                    }
                });
                thread.execute();
            }
        });

        //on cancel click
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Pulse).duration(400).playOn(view.findViewById(R.id.user_cancel_text));
                //inflate editProfile fragment
                Fragment frag = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_fragment, frag).commit();
            }
        });

        return view;
    }

    public void changePassword() {
        View view = passwordChangeDialog.getCustomView();
        final String oldPassword = ((EditText) view.findViewById(R.id.edit_password_old_password)).getText().toString();
        final String newPassword = ((EditText) view.findViewById(R.id.edit_password_new_password)).getText().toString();
        final String confirmPassword = ((EditText) view.findViewById(R.id.edit_password_confirm_new_password)).getText().toString();

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(VariableNames.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String sharedPreferences_username = sharedPreferences.getString(VariableNames.USERNAME, "false");

        Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
        map.put("username", sharedPreferences_username); //map.put can be use as many time as required
        map.put("old_password", oldPassword);
        map.put("new_password", newPassword);
        map.put("confirm_password", confirmPassword);

        BackgroundThread thread = new BackgroundThread(ApiUrl.USER_PROFILE_CHANGE_PASSWORD_URL, map, Connection.GET);
        thread.setBackgroundListener(getActivity(), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response, String result) throws IOException {
                if(response.isSuccessful()){
                    if (result.equals("password_changed")) {

                        Message.alertDialogSimple("Success", "Your Password has been Changed!", getActivity());

                    } else if(result.equals("different_password")) {
                        Message.alertDialogSimple("Error", "Passwords Does not Match!", getActivity());
                    } else if(result.equals("incorrect_password")){
                        Message.alertDialogSimple("Error", "Incorrect Password!", getActivity());
                    }
                }
            }
        });

        thread.execute();

    }

}
