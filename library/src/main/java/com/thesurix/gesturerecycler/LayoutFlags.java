package com.thesurix.gesturerecycler;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Enum with predefined gesture flags for various layout managers
 * @see RecyclerView.LayoutManager
 * @author thesurix
 */
enum LayoutFlags {

    LINEAR {
        @Override
        int getDragFlags(RecyclerView.LayoutManager layout) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

            LinearLayoutManager linearLayout = (LinearLayoutManager) layout;
            if (linearLayout.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
            return dragFlags;
        }

        @Override
        int getSwipeFlags(RecyclerView.LayoutManager layout) {
            int swipeFlags = ItemTouchHelper.RIGHT;

            LinearLayoutManager linearLayout = (LinearLayoutManager) layout;
            if (linearLayout.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                swipeFlags = ItemTouchHelper.UP;
            }
            return swipeFlags;
        }
    },
    GRID {
        @Override
        int getDragFlags(RecyclerView.LayoutManager layout) {
            return ItemTouchHelper.UP
                    | ItemTouchHelper.DOWN
                    | ItemTouchHelper.LEFT
                    | ItemTouchHelper.RIGHT;
        }

        @Override
        int getSwipeFlags(RecyclerView.LayoutManager layout) {
            int swipeFlags = ItemTouchHelper.RIGHT;

            GridLayoutManager gridLayout = (GridLayoutManager) layout;
            if (gridLayout.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
            return swipeFlags;
        }
    },
    STAGGERED {
        @Override
        int getDragFlags(RecyclerView.LayoutManager layout) {
            return ItemTouchHelper.UP
                    | ItemTouchHelper.DOWN
                    | ItemTouchHelper.LEFT
                    | ItemTouchHelper.RIGHT;
        }

        @Override
        int getSwipeFlags(RecyclerView.LayoutManager layout) {
            int swipeFlags = ItemTouchHelper.RIGHT;

            StaggeredGridLayoutManager staggeredGridLayout = (StaggeredGridLayoutManager) layout;
            if (staggeredGridLayout.getOrientation() == StaggeredGridLayoutManager.HORIZONTAL) {
                swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
            return swipeFlags;
        }
    };

    /**
     * Returns drag flags for the given layout manager.
     * @param layout layout manager instance
     * @return drag flags
     */
    abstract int getDragFlags(RecyclerView.LayoutManager layout);

    /**
     * Returns swipe flags for the given layout manager.
     * @param layout layout manager instance
     * @return swipe flags
     */
    abstract int getSwipeFlags(RecyclerView.LayoutManager layout);
}
