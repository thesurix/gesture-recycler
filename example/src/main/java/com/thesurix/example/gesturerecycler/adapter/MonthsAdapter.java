package com.thesurix.example.gesturerecycler.adapter;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thesurix.example.gesturerecycler.R;
import com.thesurix.example.gesturerecycler.model.MonthItem;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureViewHolder;

public class MonthsAdapter extends GestureAdapter<MonthItem, GestureViewHolder<MonthItem>> {

    private final int mItemResId;

    public MonthsAdapter(@LayoutRes final int itemResId) {
        mItemResId = itemResId;
    }

    @Override
    public GestureViewHolder<MonthItem> onCreateViewHolder(final ViewGroup parent, final int viewType) {
        if (viewType == MonthItem.MonthItemType.MONTH.ordinal()) {
            final View itemView = LayoutInflater.from(parent.getContext()).inflate(mItemResId, parent, false);
            return new MonthViewHolder(itemView);
        } else {
            final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item, parent, false);
            return new HeaderViewHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(final int position) {
        return getItem(position).getType().ordinal();
    }
}
