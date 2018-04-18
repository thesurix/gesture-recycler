package com.thesurix.gesturerecycler.transactions;

import com.thesurix.gesturerecycler.GestureAdapter;

/**
 * @author thesurix
 */
public class RevertReorderTransaction<T> implements AdapterTransaction {

    private final GestureAdapter<T, ?> mAdapter;
    private final int mFrom;
    private final int mTo;

    public RevertReorderTransaction(GestureAdapter<T, ?> adapter, int from, int to) {
        mAdapter = adapter;
        mFrom = from;
        mTo = to;
    }

    @Override
    public boolean perform() {
        return false;
    }

    @Override
    public boolean revert() {
        T item = mAdapter.getData().remove(mTo);
        if (item != null) {
            mAdapter.notifyItemRemoved(mTo);
            mAdapter.getData().add(mFrom, item);
            mAdapter.notifyItemInserted(mFrom);
            return true;
        }

        return false;
    }
}
