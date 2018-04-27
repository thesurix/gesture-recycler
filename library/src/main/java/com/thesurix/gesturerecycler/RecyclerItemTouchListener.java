package com.thesurix.gesturerecycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Class that is responsible for handling item touch events.
 * @author thesurix
 */
public class RecyclerItemTouchListener<T> extends RecyclerView.SimpleOnItemTouchListener {

    /**
     * The listener that is used to notify when a tap, long press or double tap occur.
     */
    public interface ItemClickListener<T> {

        /**
         * Called when a tap occurs on a specified item.
         * @param item pressed item
         * @param position item's position
         * @return true if the event is consumed, else false
         */
        boolean onItemClick(@NonNull T item, int position);

        /**
         * Called when a long press occurs on a specified item.
         * @param item pressed item
         * @param position item's position
         */
        void onItemLongPress(@NonNull T item, int position);

        /**
         * Called when a double tap occurs on a specified item.
         * @param item tapped item
         * @param position item's position
         * @return true if the event is consumed, else false
         */
        boolean onDoubleTap(@NonNull T item, int position);
    }

    private GestureDetector mGestureDetector;
    private final GestureClickListener<T> mGestureClickListener;

    /**
     * Constructs {@link RecyclerView} touch listener.
     * @param listener listener for item's click events
     */
    public RecyclerItemTouchListener(@NonNull ItemClickListener<T> listener) {
        mGestureClickListener  = new GestureClickListener<>(listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null) {
            int childPosition = view.getChildAdapterPosition(childView);
            RecyclerView.Adapter adapter = view.getAdapter();
            if (adapter instanceof GestureAdapter) {
                GestureAdapter<T, ?> gestureAdapter = (GestureAdapter) adapter;
                mGestureClickListener.setTouchedItem(gestureAdapter.getItem(childPosition),
                        childPosition);
            }

            return getGestureDetector(view.getContext()).onTouchEvent(e);
        }

        return false;
    }

    private GestureDetector getGestureDetector(Context context) {
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(context, mGestureClickListener);
        }

        return mGestureDetector;
    }

    private static class GestureClickListener<T> extends GestureDetector.SimpleOnGestureListener {

        private ItemClickListener<T> listener;

        private T item;
        private int viewPosition;

        GestureClickListener(ItemClickListener<T> listener) {
            this.listener = listener;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return listener.onItemClick(item, viewPosition);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            listener.onItemLongPress(item, viewPosition);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return listener.onDoubleTap(item, viewPosition);
        }

        void setTouchedItem(T item, int viewPosition) {
            this.item = item;
            this.viewPosition = viewPosition;
        }
    }
}
