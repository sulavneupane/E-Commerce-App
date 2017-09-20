package com.nepalicoders.nepbayapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.fragments.ActivationFragment;
import com.nepalicoders.nepbayapp.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.drawer_login_fragment, new LoginFragment()).commit();

    }

    //for opening forgot password page
    public void openForgotPassword(View v) {

        String url = "http://www.nepbay.com/forget.html";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    //for opening user agreement and privacy policy page
    public void openUserAgreement(View v) {
        String url = "http://www.nepbay.com/User+Agreement.html";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void openPrivacyPolicy(View v) {
        String url = "http://www.nepbay.com/Privacy+Policy.html";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
