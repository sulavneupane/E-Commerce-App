package com.nepalicoders.nepbayapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.StaticInfo.VariableNames;
import com.nepalicoders.nepbayapp.application.App;
import com.nineoldandroids.animation.Animator;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.pushbots.push.Pushbots;


public class SplashActivity extends AppCompatActivity {

    ImageView logo;
    TextView description;
    CircularProgressView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences(VariableNames.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        final boolean firstTime = sharedPreferences.getBoolean(VariableNames.FIRST_TIME, true);

        ///Pushbots.sharedInstance().init(this);
        Tracker mTracker = ((App)getApplication()).getDefaultTracker();
        mTracker.setScreenName("SplashScreen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        logo = (ImageView)findViewById(R.id.logo);
        description = (TextView)findViewById(R.id.desc);
        loading = (CircularProgressView)findViewById(R.id.loading);

            YoYo.with(Techniques.FadeIn).duration(800).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

            @Override
            public void onAnimationEnd(Animator animation) {
                YoYo.with(Techniques.FadeIn).duration(800).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (firstTime) {
                                    startActivity(new Intent(getBaseContext(), AppIntro.class));
                                    finish();;
                                } else {
                                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                                    finish();
                                }
                            }
                        }, 400);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(description);

                description.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(logo);

    }
}
