package com.thesurix.example.gesturerecycler.model;


import android.support.annotation.DrawableRes;

public class Month implements MonthItem {

    private String mName;
    private int mDrawableId;

    public Month(String name, @DrawableRes int drawableId) {
        mName = name;
        mDrawableId = drawableId;
    }

    @Override
    public MonthItemType getType() {
        return MonthItemType.MONTH;
    }

    @Override
    public String getName() {
        return mName;
    }

    public int getDrawableId() {
        return mDrawableId;
    }
}
