package com.nepalicoders.nepbayapp.utils;

import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.TextView;

import com.nepalicoders.nepbayapp.R;

/**
 * Created by kshitij on 12/9/15.
 */

public class NavItemsHideShow {
    View v;

    public NavItemsHideShow() {

    }

    public static void navItemsHide(View v) {

        NavigationView navigationView = (NavigationView) v.findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_profile).setTitle("Login");
        /*navigationView.getMenu().findItem(R.id.nav_wishlist).setVisible(false);*/
        navigationView.getMenu().findItem(R.id.nav_orders).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
    }

    public static void navItemsShow(View v) {
        NavigationView navigationView = (NavigationView) v.findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_profile).setTitle("Profile");
        /*navigationView.getMenu().findItem(R.id.nav_wishlist).setVisible(true);*/
        navigationView.getMenu().findItem(R.id.nav_orders).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
    }
}
