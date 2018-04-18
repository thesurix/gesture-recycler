package com.thesurix.gesturerecycler.transactions;


import android.support.annotation.NonNull;

import com.thesurix.gesturerecycler.GestureAdapter;

/**
 * @author thesurix
 */
public class InsertTransaction<T> implements AdapterTransaction {

    private final GestureAdapter<T, ?> mAdapter;
    private final T mItem;
    private final int mPosition;

    public InsertTransaction(GestureAdapter<T, ?> adapter, @NonNull T item, int position) {
        mAdapter = adapter;
        mItem = item;
        mPosition = position;
    }

    @Override
    public boolean perform() {
        mAdapter.getData().add(mPosition, mItem);
        mAdapter.notifyItemInserted(mPosition);
        return true;
    }

    @Override
    public boolean revert() {
        T item = mAdapter.getData().remove(mPosition);
        boolean success = item != null;
        if (success) {
            mAdapter.notifyItemRemoved(mPosition);
        }
        return success;
    }
}
