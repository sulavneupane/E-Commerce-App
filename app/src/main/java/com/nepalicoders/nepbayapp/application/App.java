package com.nepalicoders.nepbayapp.application;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.nepalicoders.nepbayapp.R;
import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by Sabin on 12/21/2015.
 */
public class App extends Application {

    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "i2ePVwVdGDZzlm2dBFCYkz18DV7ckgTl7tt8BMec", "OeQPX96Pop02cjq7k0udPDc9Ac7lSV4eGD9JGJGX");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }


}
