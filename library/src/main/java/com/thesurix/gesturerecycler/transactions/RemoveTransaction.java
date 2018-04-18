package com.thesurix.gesturerecycler.transactions;


import com.thesurix.gesturerecycler.GestureAdapter;

/**
 * @author thesurix
 */
public class RemoveTransaction<T> implements AdapterTransaction {

    private final GestureAdapter<T, ?> mAdapter;
    private T mItem;
    private final int mPosition;

    public RemoveTransaction(GestureAdapter<T, ?> adapter, int position) {
        mAdapter = adapter;
        mPosition = position;
    }

    @Override
    public boolean perform() {
        mItem = mAdapter.getData().remove(mPosition);
        boolean success = mItem != null;
        if (success) {
            mAdapter.notifyItemRemoved(mPosition);
        }
        return success;
    }

    @Override
    public boolean revert() {
        if (mItem != null) {
            mAdapter.getData().add(mPosition, mItem);
            mAdapter.notifyItemInserted(mPosition);
            return true;
        }

        return false;
    }
}
