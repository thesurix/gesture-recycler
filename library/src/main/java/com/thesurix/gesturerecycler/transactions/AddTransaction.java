package com.thesurix.gesturerecycler.transactions;


import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureViewHolder;

/**
 * @author thesurix
 */
public class AddTransaction<T> implements AdapterTransaction {

    private final GestureAdapter<T, ? extends GestureViewHolder> mAdapter;
    private final T mItem;

    public AddTransaction(GestureAdapter<T, ? extends GestureViewHolder> adapter, T item) {
        mAdapter = adapter;
        mItem = item;
    }

    @Override
    public boolean perform() {
        final boolean success = mAdapter.getData().add(mItem);
        if (success) {
            mAdapter.notifyItemInserted(mAdapter.getItemCount());
        }
        return success;
    }

    @Override
    public boolean revert() {
        int dataSize = mAdapter.getItemCount();
        T item = mAdapter.getData().remove(dataSize - 1);
        boolean success = item != null;
        if (success) {
            mAdapter.notifyItemRemoved(dataSize);
        }
        return success;
    }
}
