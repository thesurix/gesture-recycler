package com.thesurix.gesturerecycler.transactions;

import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureViewHolder;

/**
 * @author thesurix
 */
public class RevertReorderTransaction<T> implements AdapterTransaction {

    private final GestureAdapter<T, ? extends GestureViewHolder> mAdapter;
    private final int mFrom;
    private final int mTo;

    public RevertReorderTransaction(
            GestureAdapter<T, ? extends GestureViewHolder> adapter,
            int from,
            int to
    ) {
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
