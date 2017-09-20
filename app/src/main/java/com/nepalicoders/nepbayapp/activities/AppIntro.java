package com.nepalicoders.nepbayapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nepalicoders.nepbayapp.R;
import com.vlonjatg.android.apptourlibrary.MaterialSlide;

public class AppIntro extends com.vlonjatg.android.apptourlibrary.AppTour {

    @Override
    public void init(@Nullable Bundle savedInstanceState) {

        int firstColor = Color.parseColor("#673AB7");
        int secondColor = Color.parseColor("#8BC34A");
        int thirdColor = Color.parseColor("#9C27B0");

        //Create pre-created fragments
        Fragment firstSlide = MaterialSlide.newInstance(R.drawable.first, "Welcome to NepBay!",
                "Experience online shopping like never before with our world class app! 100s of Brands & 1000s of Products.", Color.WHITE, Color.WHITE);

        Fragment secondSlide = MaterialSlide.newInstance(R.drawable.second, "Easy Manage Account Services",
                "Ease up your Account Services just by sliding on the left drawer.", Color.WHITE, Color.WHITE);

        Fragment thirdSlide = MaterialSlide.newInstance(R.drawable.third, "Shop by Department",
                "Enhance your shopping experience with the neatly arranged product menu on the right drawer.", Color.WHITE, Color.WHITE);

        //Add slides
        addSlide(firstSlide, firstColor);
        addSlide(secondSlide, secondColor);
        addSlide(thirdSlide, thirdColor);

        //Custom slide
        //addSlide(new CustomSlide(), customSlideColor);

        //Customize tour
        setSkipButtonTextColor(Color.WHITE);
        setNextButtonColorToWhite();
        setDoneButtonTextColor(Color.WHITE);

        hideSkip();
    }

    @Override
    public void onSkipPressed() {

    }

    @Override
    public void onDonePressed() {
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }
}
