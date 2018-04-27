package com.thesurix.gesturerecycler;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Observer class for managing visibility of the adapter's empty view.
 */
class EmptyViewDataObserver extends RecyclerView.AdapterDataObserver {

    @Nullable private RecyclerView mRecyclerView;
    @Nullable private View mEmptyView;
    @Nullable private GestureAdapter.OnEmptyViewVisibilityDelegate mVisibilityDelegate;

    @Override
    public void onChanged() {
        updateEmptyViewState();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        updateEmptyViewState();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        updateEmptyViewState();
    }

    public void setEmptyView(
            @Nullable View emptyView,
            @Nullable GestureAdapter.OnEmptyViewVisibilityDelegate delegate
    ) {
        mEmptyView = emptyView;
        mVisibilityDelegate = delegate;
        updateEmptyViewState();
    }

    public void setRecyclerView(@Nullable RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        updateEmptyViewState();
    }

    private void updateEmptyViewState() {
        if (mEmptyView != null && mRecyclerView != null) {
            if (mRecyclerView.getAdapter().getItemCount() == 0) {
                if (mVisibilityDelegate == null) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mVisibilityDelegate.onShow(mEmptyView, mRecyclerView);
                }
            } else {
                if (mVisibilityDelegate == null) {
                    mEmptyView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    mVisibilityDelegate.onHide(mEmptyView, mRecyclerView);
                }
            }
        }
    }
}
