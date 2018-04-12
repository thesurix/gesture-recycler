package com.thesurix.gesturerecycler;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import static com.thesurix.gesturerecycler.LayoutFlags.GRID;
import static com.thesurix.gesturerecycler.LayoutFlags.LINEAR;
import static com.thesurix.gesturerecycler.LayoutFlags.STAGGERED;

/**
 * Touch helper callback that handles different RecycleView gestures.
 * @author thesurix
 */
public class GestureTouchHelperCallback extends ItemTouchHelper.Callback {

    /** GestureAdapter instance for touch callbacks */
    private final GestureAdapter mGestureAdapter;

    /** Flag that enables or disables swipe gesture */
    private boolean mIsSwipeEnabled;
    /** Flag that enables or disables manual drag gesture */
    private boolean mIsManualDragEnabled;
    /** Flag that enables long press drag gesture */
    private boolean mIsLongPressDragEnabled;

    /** Flags for drag gesture */
    private int mDragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    /** Flags for swipe gesture */
    private int mSwipeFlags = ItemTouchHelper.RIGHT;

    /**
     * Constructs callback object based on passed adapter.
     * @param adapter adapter
     */
    public GestureTouchHelperCallback(GestureAdapter adapter) {
        mGestureAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        GestureViewHolder holder = (GestureViewHolder) viewHolder;
        return makeMovementFlags(holder.canDrag() ? mDragFlags : 0,
                holder.canSwipe() ? mSwipeFlags : 0);
    }

    @Override
    public boolean onMove(
            RecyclerView recyclerView,
            RecyclerView.ViewHolder source,
            RecyclerView.ViewHolder target
    ) {
        return mGestureAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mGestureAdapter.onItemDismissed(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE
                && viewHolder instanceof GestureViewHolder) {
            GestureViewHolder itemViewHolder = (GestureViewHolder) viewHolder;
            View backgroundView = itemViewHolder.getBackgroundView();
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && backgroundView != null) {
                backgroundView.setVisibility(View.VISIBLE);
            }

            itemViewHolder.onItemSelect();
        }
    }

    @Override
    public void onChildDraw(
            Canvas c, RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder,
            float dX,
            float dY,
            int actionState,
            boolean isCurrentlyActive
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View foregroundView = ((GestureViewHolder) viewHolder).getForegroundView();
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState,
                    isCurrentlyActive);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        mGestureAdapter.onItemMoved();
        if (viewHolder instanceof GestureViewHolder) {
            GestureViewHolder itemViewHolder = (GestureViewHolder) viewHolder;
            itemViewHolder.onItemClear();

            View backgroundView = itemViewHolder.getBackgroundView();
            if (backgroundView != null) {
                backgroundView.setVisibility(View.GONE);
            }

            View foregroundView = itemViewHolder.getForegroundView();
            getDefaultUIUtil().clearView(foregroundView);
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return mIsLongPressDragEnabled;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return mIsSwipeEnabled;
    }

    /**
     * Returns true if manual drag is enabled, false if manual drag is disabled.
     * @return manual drag state
     */
    public boolean isManualDragEnabled() {
        return mIsManualDragEnabled;
    }

    /**
     * Sets manual drag gesture enabled or disabled.
     * Manual drag is disabled by default.
     * @param enabled true to enable, false to disable
     */
    public void setManualDragEnabled(boolean enabled) {
        mIsManualDragEnabled = enabled;
        //update adapter to enable/disable manual drag spawns
        mGestureAdapter.allowManualDrag(mIsManualDragEnabled);
    }

    /**
     * Sets swipe gesture enabled or disabled.
     * Swipe is disabled by default.
     * @param enabled true to enable, false to disable
     */
    public void setSwipeEnabled(boolean enabled) {
        mIsSwipeEnabled = enabled;
    }

    /**
     * Sets long press drag gesture enabled or disabled.
     * Long press drag is disabled by default.
     * @param enabled true to enable, false to disable
     */
    public void setLongPressDragEnabled(boolean enabled) {
        mIsLongPressDragEnabled = enabled;
    }

    /**
     * Sets predefined drag flags for RecyclerView layout.
     * @param layout type of the RecyclerView layout
     */
    public void setDragFlagsForLayout(RecyclerView.LayoutManager layout) {
        if (layout instanceof GridLayoutManager) {
            mDragFlags = GRID.getDragFlags(layout);
        } else if (layout instanceof LinearLayoutManager) {
            mDragFlags = LINEAR.getDragFlags(layout);
        } else if (layout instanceof StaggeredGridLayoutManager) {
            mDragFlags = STAGGERED.getDragFlags(layout);
        } else {
            throw new IllegalArgumentException("Unsupported layout type.");
        }
    }

    /**
     * Sets predefined swipe flags for RecyclerView layout.
     * @param layout type of the RecyclerView layout
     */
    public void setSwipeFlagsForLayout(RecyclerView.LayoutManager layout) {
        if (layout instanceof GridLayoutManager) {
            mSwipeFlags = GRID.getSwipeFlags(layout);
        } else if (layout instanceof LinearLayoutManager) {
            mSwipeFlags = LINEAR.getSwipeFlags(layout);
        } else if (layout instanceof StaggeredGridLayoutManager) {
            mSwipeFlags = STAGGERED.getSwipeFlags(layout);
        } else {
            throw new IllegalArgumentException("Unsupported layout type.");
        }
    }

    /**
     * Sets flags for drag gesture.
     * See {@link ItemTouchHelper} flags.
     * @param flags flags for drag gesture
     */
    public void setDragFlags(int flags) {
        mDragFlags = flags;
    }

    /**
     * Sets flags for swipe gesture.
     * See {@link ItemTouchHelper} flags.
     * @param flags flags for swipe gesture
     */
    public void setSwipeFlags(int flags) {
        mSwipeFlags = flags;
    }
}
