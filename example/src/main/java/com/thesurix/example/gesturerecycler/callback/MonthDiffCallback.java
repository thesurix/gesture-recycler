package com.thesurix.example.gesturerecycler.callback;


import android.support.v7.util.DiffUtil;

import com.thesurix.example.gesturerecycler.model.MonthItem;

import java.util.List;

public class MonthDiffCallback extends DiffUtil.Callback {

    private final List<MonthItem> mOldList;
    private final List<MonthItem> mNewList;

    public MonthDiffCallback(List<MonthItem> oldList, List<MonthItem> newList) {
        mOldList = oldList;
        mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList == null ? 0 : mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList == null ? 0 : mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return areItemsEqual(oldItemPosition, newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return areItemsEqual(oldItemPosition, newItemPosition);
    }

    private boolean areItemsEqual(int oldItemPosition,  int newItemPosition) {
        MonthItem oldItem = mOldList.get(oldItemPosition);
        MonthItem newItem = mNewList.get(newItemPosition);

        return oldItem.getName().equals(newItem.getName());
    }
}
