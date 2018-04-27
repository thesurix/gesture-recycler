package com.thesurix.gesturerecycler.transactions;


import com.thesurix.gesturerecycler.GestureAdapter;

public class MoveTransaction <T> implements AdapterTransaction {

    private final GestureAdapter<T, ?> mAdapter;
    private final int mFromPosition;
    private final int mToPosition;
    private T mItem;

    public MoveTransaction(GestureAdapter<T, ?> adapter, int fromPosition, int toPosition) {
        mAdapter = adapter;
        mFromPosition = fromPosition;
        mToPosition = toPosition;
    }

    @Override
    public boolean perform() {
        mItem = mAdapter.getData().remove(mFromPosition);
        boolean success = mItem != null;
        if (success) {
            mAdapter.getData().add(mToPosition, mItem);
            mAdapter.notifyItemMoved(mFromPosition, mToPosition);
        }
        return success;
    }

    @Override
    public boolean revert() {
        mItem = mAdapter.getData().remove(mToPosition);
        boolean success = mItem != null;
        if (success) {
            mAdapter.getData().add(mFromPosition, mItem);
            mAdapter.notifyItemMoved(mToPosition, mFromPosition);
        }
        return success;
    }
}