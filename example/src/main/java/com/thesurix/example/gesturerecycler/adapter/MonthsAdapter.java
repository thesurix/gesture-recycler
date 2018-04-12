package com.thesurix.example.gesturerecycler.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thesurix.example.gesturerecycler.R;
import com.thesurix.example.gesturerecycler.model.MonthItem;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureViewHolder;

public class MonthsAdapter extends GestureAdapter<MonthItem, GestureViewHolder<MonthItem>> {

    private final int mItemResId;

    public MonthsAdapter(@LayoutRes int itemResId) {
        mItemResId = itemResId;
    }

    @NonNull
    @Override
    public GestureViewHolder<MonthItem> onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == MonthItem.MonthItemType.MONTH.ordinal()) {
            View itemView = inflater.inflate(mItemResId, parent, false);
            return new MonthViewHolder(itemView);
        } else {
            View itemView = inflater.inflate(R.layout.header_item, parent, false);
            return new HeaderViewHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType().ordinal();
    }
}
