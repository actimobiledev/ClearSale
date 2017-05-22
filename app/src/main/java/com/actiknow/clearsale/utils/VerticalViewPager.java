package com.actiknow.clearsale.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by deming_huang on 2016/3/25.
 */
public class VerticalViewPager extends ViewPager {
    public VerticalViewPager(Context context) {
        super(context);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();
        float swapX = (event.getY() / height) * width;
        float swapY = (event.getX() / width) * height;
        event.setLocation(swapX, swapY);
        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = super.onInterceptTouchEvent(swapTouchEvent(event));
        swapTouchEvent(event);
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        swapTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
