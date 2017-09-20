package com.nepalicoders.nepbayapp.receivers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.nepalicoders.nepbayapp.activities.MainActivity;
import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sulav on 12/28/15.
 */
public class PushNotificationReceiver extends ParsePushBroadcastReceiver {

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        //super.onPushOpen(context, intent);

        //To track "App Opens"
        ParseAnalytics.trackAppOpenedInBackground(intent);

        //Here is data you sent
        //Log.i("NotificationLog", intent.getExtras().getString( "com.parse.Data" ));

        String notificationData = intent.getExtras().getString("com.parse.Data");

        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("notificationData", notificationData);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        //Log.d("NotificationLog", intent.getExtras().getString("com.parse.Data"));
    }

}
