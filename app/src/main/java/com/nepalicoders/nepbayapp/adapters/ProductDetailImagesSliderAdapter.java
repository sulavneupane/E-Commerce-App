package com.nepalicoders.nepbayapp.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.MotionEvent;

import com.nepalicoders.nepbayapp.fragments.HomeImageSlider;
import com.nepalicoders.nepbayapp.fragments.ProductDetailImageSlider;
import com.nepalicoders.nepbayapp.fragments.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nepalicoders on 12/4/15.
 */
public class ProductDetailImagesSliderAdapter extends FragmentPagerAdapter{
    private final List<Fragment> mFragments = new ArrayList<>();

    public ProductDetailImagesSliderAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(String imageId) {
        Fragment fragment = new ProductDetailImageSlider();
        Bundle sendImgSlider = new Bundle();
        sendImgSlider.putString("image", imageId);
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
