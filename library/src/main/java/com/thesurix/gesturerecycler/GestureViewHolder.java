package com.thesurix.gesturerecycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Base view holder class for gesture compatible items.
 * @author thesurix
 */
public abstract class GestureViewHolder<T> extends RecyclerView.ViewHolder {

    public GestureViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * This method delegates release logic from
     * {@link RecyclerView.Adapter#onViewRecycled(RecyclerView.ViewHolder)} into your
     * {@code ViewHolder}. It's good place for release some listeners or something what have high
     * in-memory weight (high resolution bitmap etc.) from a views
     * */
    public void unbindHolder() {
    }

    /**
     * Simply getter for context from {@link #itemView}
     * @return context from
     * */
    public Context getContext() {
        return itemView.getContext();
    }

    /**
     * Returns view that can spawn drag gesture. If there is no view simply return null.
     * @return view that can spawn drag gesture
     */
    @Nullable
    public View getDraggableView() {
        return null;
    }

    /**
     * Returns top visible view (originally root view of the item),
     * override this method to use background view feature in case of swipe gestures.
     * @return top view
     */
    public View getForegroundView() {
        return itemView;
    }

    /**
     * Returns background view which is visible when foreground view is partially or fully swiped.
     * @return background view
     */
    @Nullable
    public View getBackgroundView() {
        return null;
    }

    /**
     * Method that shows view for manual drag gestures.
     * Called only when getDraggableView() returns valid view.
     */
    public void showDraggableView() {
        if (getDraggableView() != null) getDraggableView().setVisibility(View.VISIBLE);
    }

    /**
     * Method that hides view for manual drag gestures.
     * Called only when getDraggableView() returns valid view.
     */
    public void hideDraggableView() {
        if (getDraggableView() != null) getDraggableView().setVisibility(View.GONE);
    }

    /**
     * Indicates that view is selected.
     */
    public void onItemSelect() {
    }

    /**
     * Indicates that view has no selection.
     */
    public void onItemClear() {
    }

    /**
     * Returns information if we can drag this view.
     * @return true if draggable, false otherwise
     */
    public abstract boolean canDrag();

    /**
     * Returns information if we can swipe this view.
     * @return true if swipeable, false otherwise
     */
    public abstract boolean canSwipe();

    /**
     * This method delegates bind logic from
     * {@link RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)} into your
     * {@code ViewHolder}
     *
     * @param t model taken by position of holder from adapter's data collection
     * */
    public abstract void bindHolder(T t);
}
