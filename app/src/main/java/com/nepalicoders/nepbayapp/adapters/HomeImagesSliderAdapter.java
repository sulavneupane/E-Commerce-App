package com.nepalicoders.nepbayapp.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nepalicoders.nepbayapp.fragments.HomeImageSlider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kshitij on 12/4/15.
 */

public class HomeImagesSliderAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();

    public HomeImagesSliderAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(String link, String image) {
        Fragment fragment = new HomeImageSlider();
        Bundle sendImgSlider = new Bundle();
        sendImgSlider.putString("link", link);
        sendImgSlider.putString("image", image);
        fragment.setArguments(sendImgSlider);
        mFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}