package com.thesurix.gesturerecycler;

import android.support.annotation.NonNull;

/**
 * Default implementation of the {@link RecyclerItemTouchListener.ItemClickListener}.
 * @author thesurix
 */
public class DefaultItemClickListener<T> implements RecyclerItemTouchListener.ItemClickListener<T> {

    @Override
    public boolean onItemClick(@NonNull T item, int position) {
        return false;
    }

    @Override
    public void onItemLongPress(@NonNull T item, int position) {
    }

    @Override
    public boolean onDoubleTap(@NonNull T item, int position) {
        return false;
    }
}
