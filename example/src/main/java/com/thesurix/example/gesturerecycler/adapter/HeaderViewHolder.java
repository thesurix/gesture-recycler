package com.thesurix.example.gesturerecycler.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.thesurix.example.gesturerecycler.R;
import com.thesurix.example.gesturerecycler.model.MonthHeader;
import com.thesurix.example.gesturerecycler.model.MonthItem;
import com.thesurix.gesturerecycler.GestureViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeaderViewHolder extends GestureViewHolder<MonthItem> {

    @BindView(R.id.header_text)
    TextView mHeaderText;

    public HeaderViewHolder(@NonNull View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public void bindHolder(@NonNull MonthItem monthItem) {
        MonthHeader monthHeader = (MonthHeader) monthItem;
        mHeaderText.setText(monthHeader.getName());
    }

    @Override
    public boolean canDrag() {
        return false;
    }

    @Override
    public boolean canSwipe() {
        return false;
    }
}
