package com.wakawala.customclasses;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by bhavesh on 28/4/17.
 */

public class NonSwipeAbleViewPager extends ViewPager {

    public NonSwipeAbleViewPager(Context context) {
        super(context);
    }

    public NonSwipeAbleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Never allow swiping to switch between pages\
        return false;
    }
}
