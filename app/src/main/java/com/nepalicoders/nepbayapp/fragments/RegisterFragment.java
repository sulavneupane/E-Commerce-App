package com.nepalicoders.nepbayapp.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.StaticInfo.VariableNames;
import com.nepalicoders.nepbayapp.activities.LoginActivity;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.nepalicoders.nepbayapp.utils.CountryToPhonePrefix;
import com.nepalicoders.nepbayapp.utils.Message;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    TextView register_button;
    String firstName;
    String lastName;
    String mobileNumber;
    String email;
    //String username;
    String password;
    String confirmPassword;
    String countryCode;
    Fragment fragment;
    Toolbar toolbar;
    EditText country_code_edit;
    String gender;
    RadioGroup radioGroup;
    String date="0000-00-00";



    String CountryID;
    String CountryZipCode;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public void showDateDialog(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        //dpd.setMinDate(now);
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_register, container, false);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Register");

        TelephonyManager manager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        CountryZipCode = CountryToPhonePrefix.prefixFor(CountryID);

        /*String[] rl=this.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }*/
        if (CountryZipCode != null) {
            ((EditText) view.findViewById(R.id.register_country_code)).setText(CountryZipCode);
        }

       /* Log.d("Result",CountryToPhonePrefix.prefixFor(CountryID));*/

        //Extract text form Fields for register form
        register_button = (TextView) view.findViewById(R.id.register_button);
        radioGroup = (RadioGroup) view.findViewById(R.id.register_radio_group);

        ImageView dobPicker = (ImageView) view.findViewById(R.id.register_dob);

        dobPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //view.findViewById(R.id.register_first_name).setBackground(getActivity().getDrawable(R.drawable.border_accent));
                //hide softkeys on button click
                closeKeyboard(getActivity(), register_button.getWindowToken());
                YoYo.with(Techniques.Pulse).duration(400).playOn(getActivity().findViewById(R.id.register_button));
                firstName = ((EditText) view.findViewById(R.id.register_first_name)).getText().toString();
                lastName = ((EditText) view.findViewById(R.id.register_last_name)).getText().toString();
                countryCode = ((EditText) view.findViewById(R.id.register_country_code)).getText().toString();
                mobileNumber = ((EditText) view.findViewById(R.id.register_mobile)).getText().toString();
                email = ((EditText) view.findViewById(R.id.register_email)).getText().toString();
               /* username = ((EditText) view.findViewById(R.id.register_username)).getText().toString();*/
                password = ((EditText) view.findViewById(R.id.register_password)).getText().toString();
                confirmPassword = ((EditText) view.findViewById(R.id.register_confirm_password)).getText().toString();

                int idGender = radioGroup.getCheckedRadioButtonId();

                if (firstName.equals("")) {
                    //animation
                    YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_first_name));
                    //view.findViewById(R.id.register_first_name).setBackground(getActivity().getDrawable(R.drawable.border_accent));
                    //snackbar message popup
                    Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.fill_all_fields));
                }else if(lastName.equals("")){
                    //animation
                    YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_last_name));
                   // view.findViewById(R.id.register_last_name).setBackground(getActivity().getDrawable(R.drawable.border_accent));
                    //snackbar message popup
                    Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.fill_all_fields));

                }else if(countryCode.equals("")){
                    //animation
                    YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_country_code));
                    //view.findViewById(R.id.register_country_code).setBackground(getActivity().getDrawable(R.drawable.border_accent));
                    //snackbar message popup
                    Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.fill_all_fields));

                }else if(mobileNumber.equals("")){
                    //animation
                    YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_mobile));
                    //view.findViewById(R.id.register_mobile).setBackground(getActivity().getDrawable(R.drawable.border_accent));
                    //snackbar message popup
                    Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.fill_all_fields));
                }/*else if(email.equals("")){
                    //animation
                    YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_email));
                    //view.findViewById(R.id.register_email).setBackground(getActivity().getDrawable(R.drawable.border_accent));
                    //snackbar message popup
                    Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.fill_all_fields));

                }*/else if(idGender == -1){
                    YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_form_linear_layout));
                    Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.fill_all_fields));
                }else if(password.equals("")){
                    //animation
                    YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_password));
                    //view.findViewById(R.id.register_mobile).setBackground(getActivity().getDrawable(R.drawable.border_accent));
                    //snackbar message popup
                    Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.fill_all_fields));

                }else if(confirmPassword.equals("")){
                    //animation
                    YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_confirm_password));
                    //view.findViewById(R.id.register_confirm_password).setBackground(getActivity().getDrawable(R.drawable.border_accent));
                    //snackbar message popup
                    Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.fill_all_fields));

                }else if (!password.equals(confirmPassword)) {
                    //animation
                    YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_password));
                    YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_confirm_password));
                    //view.findViewById(R.id.register_password).setBackground(getActivity().getDrawable(R.drawable.border_accent));
                    //view.findViewById(R.id.register_confirm_password).setBackground(getActivity().getDrawable(R.drawable.border_accent));
                    //snackbar message popup
                    Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.passwords_not_same));
                } else if (!((CheckBox) view.findViewById(R.id.register_privacy_checkBox)).isChecked()) {
                    //animation
                    YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_form_linear_layout));
                    //snackbar message popup
                    Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.privacy_user));
                } else {

                    //generate a 4 digit integer 1000 <10000
                    int randomNUM = (int) (Math.random() * 9000) + 1000;
                    final String randomPIN = String.valueOf(randomNUM);

                    if (idGender == R.id.register_radio_male){
                        //Do something with the button
                        gender = "male";
                    }else if(idGender == R.id.register_radio_female){
                        gender = "female";
                    }

                    // Below exmaple is for sending Plain text
                    final String message = "Welcome to NepBay, your account activation pin is " + randomPIN;
                    final String destination = countryCode + mobileNumber;

                    Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
                   /* map.put("username", username);*/ //map.put can be use as many time as required
                    map.put("password", password);
                    map.put("first_name", firstName);
                    map.put("last_name", lastName);
                    map.put("mobile_number", mobileNumber);
                    map.put("email", email);
                    map.put("gender", gender);
                    map.put("dob", date);
                    map.put("activation_code", randomPIN);

                    BackgroundThread thread = new BackgroundThread(ApiUrl.USER_REGISTER_URL, map, Connection.GET);
                    thread.setBackgroundListener(getActivity(), new Callback() {

                        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", getActivity().getString(R.string.registering), true);

                        @Override
                        public void onFailure(Request request, IOException e) {
                            Log.d("RegistrationResponse", request.toString());
                            //dismiss login popup
                            dialog.dismiss();
                            //display error snackbar
                            Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getString(R.string.no_internet));
                        }

                        @Override
                        public void onResponse(Response response, String result) throws IOException {
                            //dismiss login popup
                            dialog.dismiss();

                            Log.d("RegistrationResponse", result);

                            if (response.isSuccessful()) {
                                Log.d("Test",result);

                                try {
                                    JSONObject res = new JSONObject(result);

                                    if (res.getString("status").equals("1")) {

                                        final String username = res.getString("username");

                                        String smsUrl = URLDecoder.decode(res.getString("url"), "UTF-8");

                                        Map<String, String> smsMap = new HashMap<>();
                                        smsMap.put("username", URLDecoder.decode(res.getString("user"), "UTF-8"));
                                        smsMap.put("password", URLDecoder.decode(res.getString("pass"), "UTF-8"));
                                        smsMap.put("type", VariableNames.SMS_TYPE);
                                        smsMap.put("dlr", VariableNames.SMS_DLR);
                                        smsMap.put("destination", destination);
                                        smsMap.put("source", URLDecoder.decode(res.getString("sender"), "UTF-8"));
                                        smsMap.put("message", URLDecoder.decode(res.getString("message"), "UTF-8"));

                                        Log.d("smsMap", smsMap.toString());
                                        Log.d("smsMap", "URL: " + smsUrl);

                                        BackgroundThread smsThread = new BackgroundThread(smsUrl, smsMap, Connection.POST);

                                        smsThread.setBackgroundListener(getActivity(), new Callback() {

                                            @Override
                                            public void onFailure(Request request, IOException e) {

                                                //dismiss login popup
                                                dialog.dismiss();
                                                //display error snackbar
                                                Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getString(R.string.no_internet));

                                            }

                                            @Override
                                            public void onResponse(Response response, String result) throws IOException {

                                                Log.d("smsMap", result);

                                                if (response.isSuccessful()) {

                                                    Message.snackBarShortPositive(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.register_successful));

                                                    Bundle pass = new Bundle();
                                                    pass.putString("username", username);//passing the username value to the next fragment
                                                    pass.putString("password", password);
                                                    fragment = new ActivationFragment();
                                                    fragment.setArguments(pass);

                                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_login_fragment, fragment).commit();

                                                }

                                            }
                                        });
                                        smsThread.execute();
                                    } else {
                                        YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_form_linear_layout));
                                        Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), res.getString("message"));
                                    }
                                    /* else if (result.equals("username already exists!")) {
                                        //animation
                                        YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_form_linear_layout));
                                        Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), "Username already exists");
                                    } else if (result.equals("email already exists!")) {
                                        YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_form_linear_layout));
                                        Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), "Email already exists !");
                                    } else if (result.equals("phone number already exists!")) {
                                        YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_form_linear_layout));
                                        Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), "Phone Number already exists !");
                                    } else {
                                        YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_form_linear_layout));
                                        Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.register_failed));
                                    }*/
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    thread.execute();

                    /*Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
                    map.put("username", username); //map.put can be use as many time as required
                    map.put("password", password);
                    map.put("first_name", firstName);
                    map.put("last_name", lastName);
                    map.put("mobile_number", mobileNumber);
                    map.put("email", email);
                    map.put("activation_code", randomPIN);

                    BackgroundThread thread = new BackgroundThread(ApiUrl.USER_REGISTER_URL, map, Connection.GET);
                    thread.setBackgroundListener(getActivity(), new Callback() {
                        //loading popup
                        ProgressDialog dialog = ProgressDialog.show(getActivity(), "", getActivity().getString(R.string.registering), true);

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
                                try {
                                    JSONObject object = new JSONObject(result);
                                    if (object.getString("status").equals("1")) {
                                        Message.snackBarShortPositive(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.register_successful));

                                        fragment = new ActivationFragment();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_login_fragment, fragment).commit();
                                    } else {
                                        //animation
                                        YoYo.with(Techniques.Shake).duration(400).playOn(view.findViewById(R.id.register_form_linear_layout));

                                        Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.register_failed));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    thread.execute();*/
                }
            }
        });


        return view;
    }

    //method to hide softkeys on button click
    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = new StringBuilder()
                // Month is 0 based, just add 1
                .append(year).append("-").append(monthOfYear + 1).append("-")
                .append(dayOfMonth).toString();

       // Toast.makeText(getActivity(),date,Toast.LENGTH_LONG).show();
    }
}
