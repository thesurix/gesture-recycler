package com.thesurix.gesturerecycler;


import android.support.annotation.NonNull;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Default gesture listener that handles manual spawned drag gestures.
 * @author thesurix
 */
public class GestureListener implements GestureAdapter.OnGestureListener {

    @NonNull private final ItemTouchHelper mTouchHelper;

    public GestureListener(@NonNull ItemTouchHelper touchHelper) {
        mTouchHelper = touchHelper;
    }

    @Override
    public void onStartDrag(@NonNull GestureViewHolder viewHolder) {
        mTouchHelper.startDrag(viewHolder);
    }
}
