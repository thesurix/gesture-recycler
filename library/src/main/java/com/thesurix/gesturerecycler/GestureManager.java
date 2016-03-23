package com.thesurix.gesturerecycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Class that is responsible for gesture management for {@link RecyclerView} widget.
 * @author thesurix
 */
public class GestureManager {

    private GestureTouchHelperCallback mTouchHelperCallback;

    private GestureManager(final Builder builder) {
        final GestureAdapter adapter = (GestureAdapter) builder.recyclerView.getAdapter();
        mTouchHelperCallback = new GestureTouchHelperCallback(adapter);
        mTouchHelperCallback.setSwipeEnabled(builder.isSwipeEnabled);
        mTouchHelperCallback.setLongPressDragEnabled(builder.isDragEnabled);
        mTouchHelperCallback.setManualDragEnabled(builder.isManualDragEnabled);

        final ItemTouchHelper touchHelper = new ItemTouchHelper(mTouchHelperCallback);
        touchHelper.attachToRecyclerView(builder.recyclerView);
        adapter.setGestureListener(new GestureListener(touchHelper));

        if(builder.swipeFlags == Builder.INVALID_FLAG || builder.dragFlags == Builder.INVALID_FLAG) {
            mTouchHelperCallback.setGestureFlagsForLayout(builder.recyclerView.getLayoutManager());
        } else {
            mTouchHelperCallback.setSwipeFlags(builder.swipeFlags);
            mTouchHelperCallback.setDragFlags(builder.dragFlags);
        }
    }

    /**
     * Sets swipe gesture enabled or disabled.
     * @param enabled true to enable, false to disable
     */
    public void setSwipeEnabled(final boolean enabled) {
        mTouchHelperCallback.setSwipeEnabled(enabled);
    }

    /**
     * Sets long press drag gesture enabled or disabled.
     * @param enabled true to enable, false to disable
     */
    public void setLongPressDragEnabled(final boolean enabled) {
        mTouchHelperCallback.setLongPressDragEnabled(enabled);
    }

    /**
     * Sets manual drag gesture enabled or disabled.
     * @param enabled true to enable, false to disable
     */
    public void setManualDragEnabled(final boolean enabled) {
        mTouchHelperCallback.setManualDragEnabled(enabled);
    }

    /**
     * Returns true if swipe is enabled, false if swipe is disabled.
     * @return swipe state
     */
    public boolean isSwipeEnabled() {
        return mTouchHelperCallback.isItemViewSwipeEnabled();
    }

    /**
     * Returns true if long press drag is enabled, false if long press drag is disabled.
     * @return long press drag state
     */
    public boolean isLongPressDragEnabled() {
        return mTouchHelperCallback.isLongPressDragEnabled();
    }

    /**
     * Returns true if manual drag is enabled, false if manual drag is disabled.
     * @return manual drag state
     */
    public boolean isManualDragEnabled() {
        return mTouchHelperCallback.isManualDragEnabled();
    }

    /**
     * Class that builds {@link GestureManager} instance.
     */
    public static class Builder{

        private static final int INVALID_FLAG = -1;

        private RecyclerView recyclerView;

        private int swipeFlags = INVALID_FLAG;
        private int dragFlags = INVALID_FLAG;
        private boolean isSwipeEnabled;
        private boolean isDragEnabled;
        private boolean isManualDragEnabled;

        /**
         * Constructs {@link GestureManager} for the given RecyclerView.
         * @param recyclerView RecyclerView instance
         */
        public Builder(@NonNull final RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        /**
         * Sets swipe gesture enabled or disabled.
         * Swipe is disabled by default.
         * @param enabled true to enable, false to disable
         * @return returns builder instance
         */
        public Builder setSwipeEnabled(final boolean enabled) {
            isSwipeEnabled = enabled;
            return this;
        }

        /**
         * Sets long press drag gesture enabled or disabled.
         * Long press drag is disabled by default.
         * @param enabled true to enable, false to disable
         * @return returns builder instance
         */
        public Builder setLongPressDragEnabled(final boolean enabled) {
            isDragEnabled = enabled;
            return this;
        }

        /**
         * Sets manual drag gesture enabled or disabled.
         * Manual drag is disabled by default.
         * @param enabled true to enable, false to disable
         * @return returns builder instance
         */
        public Builder setManualDragEnabled(final boolean enabled) {
            isManualDragEnabled = enabled;
            return this;
        }

        /**
         * Sets flags for swipe and drag gesture. Do not set this flags if you want predefined flags for RecyclerView layout manager.
         * See {@link ItemTouchHelper} flags.
         * @param swipeFlags flags for swipe gesture
         * @param dragFlags flags for drag gesture
         * @return returns builder instance
         */
        public Builder setGestureFlags(final int swipeFlags, final int dragFlags) {
            this.swipeFlags = swipeFlags;
            this.dragFlags = dragFlags;
            return this;
        }

        /**
         * Builds {@link GestureManager} instance.
         * @return returns GestureManager instance
         */
        public GestureManager build() {
            validateBuilder();
            return new GestureManager(this);
        }

        private void validateBuilder() {
            final boolean hasAdapter = recyclerView.getAdapter() instanceof GestureAdapter;
            if (!hasAdapter) {
                throw new IllegalArgumentException("RecyclerView does not have adapter that extends " + GestureAdapter.class.getName());
            }

            if (swipeFlags == INVALID_FLAG || dragFlags == INVALID_FLAG) {
                if (recyclerView.getLayoutManager() == null) {
                    throw new IllegalArgumentException("No layout manager for RecyclerView. Provide custom flags or attach layout manager to RecyclerView.");
                }
            }
        }
    }

}
