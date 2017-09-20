package com.nepalicoders.nepbayapp.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by sulav on 12/24/15.
 */
public class NepaliCodersViewPager extends ViewPager {

    public NepaliCodersViewPager(Context context) {
        super(context);
    }

    public NepaliCodersViewPager(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
