package com.thesurix.gesturerecycler;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.thesurix.gesturerecycler.transactions.AdapterTransaction;
import com.thesurix.gesturerecycler.transactions.AddTransaction;
import com.thesurix.gesturerecycler.transactions.InsertTransaction;
import com.thesurix.gesturerecycler.transactions.MoveTransaction;
import com.thesurix.gesturerecycler.transactions.RemoveTransaction;
import com.thesurix.gesturerecycler.transactions.RevertReorderTransaction;
import com.thesurix.gesturerecycler.util.FixedSizeArrayDequeue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Base adapter for gesture recognition, extends this to provide own implementation. T is the data
 * type, K is the ViewHolder type.
 * @author thesurix
 */
public abstract class GestureAdapter<T, K extends GestureViewHolder<T>>
        extends RecyclerView.Adapter<K> {

    /** Delegate for manage visibility for empty view */
    public interface OnEmptyViewVisibilityDelegate {

        /**
         * Called when adapter have no data and empty view needs to be shown
         * @param emptyView view to show
         * @param recyclerView root view for this adapter
         * */
        void onShow(@NonNull View emptyView, @NonNull RecyclerView recyclerView);

        /**
         * Called when adapter have data and empty view needs to be hidden
         * @param emptyView view to show
         * @param recyclerView root view for this adapter
         * */
        void onHide(@NonNull View emptyView, @NonNull RecyclerView recyclerView);
    }

    /** Listener for data changes inside adapter */
    public interface OnDataChangeListener<T> {

        /**
         * Called when item has been removed by swipe gesture.
         * @param item removed item
         * @param position removed position
         */
        void onItemRemoved(@NonNull T item, int position);

        /**
         * Called when item has been reordered by drag gesture.
         * @param item reordered item
         * @param fromPos reorder start position
         * @param toPos reorder end position
         */
        void onItemReorder(@NonNull T item, int fromPos, int toPos);
    }

    /** Listener for gestures */
    interface OnGestureListener {

        /**
         * Called when view holder item has pending drag gesture.
         * @param viewHolder dragged view holder item
         */
        void onStartDrag(@NonNull GestureViewHolder viewHolder);
    }

    /**
     * The maximum size of undo stack to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     * */
    public final static int MAX_UNDO_STACK_SIZE = Integer.MAX_VALUE - 8;

    private static final int INVALID_POSITION = -1;

    /** Temp item for swap action */
    @Nullable private T mSwappedItem;
    /** Start position of the drag action */
    private int mStartDragPos;
    /** Stop position of the drag action */
    private int mStopDragPos = INVALID_POSITION;
    /** Flag that defines if adapter allows manual dragging */
    private boolean mIsManualDragAllowed;
    /** This variable holds stack of data transactions for undo purposes */
    @NonNull private Deque<AdapterTransaction> mTransactions = new FixedSizeArrayDequeue<>(1);

    private OnGestureListener mGestureListener;
    private OnDataChangeListener<T> mDataChangeListener;
    private final EmptyViewDataObserver mEmptyViewDataObserver = new EmptyViewDataObserver();
    private final View.OnAttachStateChangeListener mAttachListener =
            new View.OnAttachStateChangeListener() {

                private boolean isRegistered;

                @Override
                public void onViewAttachedToWindow(View v) {
                    if (!isRegistered) {
                        isRegistered = true;
                        registerAdapterDataObserver(mEmptyViewDataObserver);
                    }
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    if (isRegistered) {
                        isRegistered = false;
                        unregisterAdapterDataObserver(mEmptyViewDataObserver);
                    }
                    resetTransactions();
                }
            };

    /** Collection for adapter's data */
    @NonNull private final List<T> mData = new ArrayList<>();

    @Override
    public void onBindViewHolder(@NonNull final K holder, int position) {
        if (holder.getDraggableView() != null) {
            if (mIsManualDragAllowed && holder.canDrag()) {
                holder.showDraggableView();
                holder.getDraggableView().setOnTouchListener(new View.OnTouchListener() {

                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                            if (mGestureListener != null) {
                                mGestureListener.onStartDrag(holder);
                            }
                        }

                        return false;
                    }
                });
            } else {
                holder.hideDraggableView();
            }
        }
        holder.bindHolder(getItem(position));
    }

    @Override
    public void onViewRecycled(@NonNull K holder) {
        holder.unbindHolder();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mEmptyViewDataObserver.setRecyclerView(recyclerView);
        recyclerView.addOnAttachStateChangeListener(mAttachListener);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        mEmptyViewDataObserver.setRecyclerView(null);
        recyclerView.removeOnAttachStateChangeListener(mAttachListener);
        resetTransactions();
    }

    /**
     * Sets adapter data. This method will interrupt pending animations.
     * Use {@link #add(T)}, {@link #remove(int)} or {@link #insert(T, int)}
     * or {@link #setData(List, DiffUtil.Callback)} to achieve smooth animations.
     * @param data data to show
     */
    public void setData(@NonNull List<T> data) {
        setData(data, null);
    }

    /**
     * Sets adapter data with {@link DiffUtil.Callback} to achieve smooth animations.
     * @param data data to show
     * @param diffCallback diff callback to manage internal data changes
     */
    public void setData(@NonNull List<T> data, @Nullable DiffUtil.Callback diffCallback) {
        if (diffCallback == null) {
            setNewData(data);
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
            setNewData(data);
            diffResult.dispatchUpdatesTo(this);
        }

        resetTransactions();
    }

    /**
     * Clears data.
     */
    public void clearData() {
        mData.clear();
        notifyDataSetChanged();

        resetTransactions();
    }

    /**
     * @return copy of adapter's data
     */
    @NonNull public List<T> getData() {
        return mData;
    }

    /**
     * Returns item for the given position
     * @param position item's position
     * @return item
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>position &lt; 0 || position &gt;= size()</tt>)
     */
    @NonNull public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * Adds item to the adapter.
     * @param item item to add
     * @return true if added, false otherwise
     */
    public boolean add(@NonNull T item) {
        AdapterTransaction addTransaction = new AddTransaction<>(this, item);
        boolean success = addTransaction.perform();

        mTransactions.offer(addTransaction);
        return success;
    }

    /**
     * Removes item from the given position.
     * @param position item's position
     * @return true if removed, false otherwise
     */
    public boolean remove(int position) {
        AdapterTransaction removeTransaction = new RemoveTransaction<>(this, position);
        boolean success = removeTransaction.perform();

        mTransactions.offer(removeTransaction);
        return success;
    }

    /**
     * Removes item if it contains in data set.
     * @param item item to remove
     * @return true if removed, false otherwise
     */
    public boolean remove(@NonNull T item) {
        int position = mData.indexOf(item);
        if (position != INVALID_POSITION) {
            AdapterTransaction removeTransaction = new RemoveTransaction<>(this, position);
            boolean success = removeTransaction.perform();

            mTransactions.offer(removeTransaction);
            return success;
        }
        return false;
    }

    /**
     * Inserts item in the given position.
     * @param item item to insert
     * @param position position for the item
     */
    public void insert(@NonNull T item, int position) {
        AdapterTransaction insertTransaction = new InsertTransaction<>(this, item, position);
        insertTransaction.perform();

        mTransactions.offer(insertTransaction);
    }

    /**
     * Moves item from one position to another.
     * @param fromPosition item's old position
     * @param toPosition item's new position
     * @return true if moved, false otherwise
     */
    public boolean move(int fromPosition, int toPosition) {
        AdapterTransaction moveTransaction = new MoveTransaction<>(this, fromPosition,
                toPosition);
        boolean success = moveTransaction.perform();

        mTransactions.offer(moveTransaction);
        return success;
    }

    /**
     * Sets empty view. Empty view is used when adapter has no data.
     * Pass null to disable empty view feature.
     * @param emptyView view to show
     */
    public void setEmptyView(@Nullable View emptyView) {
        setEmptyView(emptyView, null);
    }

    /**
     * Sets empty view. Empty view is used when adapter has no data.
     * Pass null to disable empty view feature.
     * @param emptyView view to show
     * @param delegate delegate for managing empty view visibility
     */
    public void setEmptyView(
            @Nullable View emptyView,
            @Nullable OnEmptyViewVisibilityDelegate delegate
    ) {
        mEmptyViewDataObserver.setEmptyView(emptyView, delegate);
    }

    /**
     * Sets undo stack size. If undo stack is full, the oldest action will be removed
     * (default size is 1).
     * @param size undo actions size
     * @see #MAX_UNDO_STACK_SIZE
     */
    public void setUndoSize(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Stack can not have negative size.");
        }
        mTransactions = new FixedSizeArrayDequeue<>(size);
    }

    /**
     * Reverts last data transaction like {@link #add(T)}, {@link #remove(int)},
     * {@link #insert(T, int)}. It supports also reverting swipe and drag & drop actions.
     *
     * @return true for successful undo action, false otherwise
     */
    public boolean undoLast() {
        return !mTransactions.isEmpty() && mTransactions.pollLast().revert();
    }

    /**
     * Sets adapter data change listener.
     * @param listener data change listener
     */
    public void setDataChangeListener(@Nullable OnDataChangeListener<T> listener) {
        mDataChangeListener = listener;
    }

    /**
     * Sets adapter gesture listener.
     * @param listener gesture listener
     */
    void setGestureListener(@Nullable OnGestureListener listener) {
        mGestureListener = listener;
    }

    /**
     * Dismisses item from the given position.
     * @param position item's position
     */
    void onItemDismissed(int position) {
        T removed = mData.get(position);
        boolean wasRemoved = remove(position);
        if (wasRemoved && mDataChangeListener != null) {
            mDataChangeListener.onItemRemoved(removed, position);
        }
    }

    /**
     * Moves item from one position to another.
     * @param fromPosition start position
     * @param toPosition end position
     * @return returns true if transition is successful
     */
    boolean onItemMove(int fromPosition, int toPosition) {
        if (mSwappedItem == null) {
            mStartDragPos = fromPosition;
            mSwappedItem = mData.get(fromPosition);
        }
        mStopDragPos = toPosition;

        // Steps bigger than one we have to swap manually in right order
        int jumpSize = Math.abs(toPosition - fromPosition);
        if (jumpSize > 1) {
            int sign = Integer.signum(toPosition - fromPosition);
            int startPos = fromPosition;
            for (int i = 0; i < jumpSize; i++) {
                int endPos = startPos + sign;
                Collections.swap(mData, startPos, endPos);
                startPos += sign;
            }
        } else {
            Collections.swap(mData, fromPosition, toPosition);
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * Called when item has been moved.
     */
    void onItemMoved() {
        if (mSwappedItem != null && mStopDragPos != INVALID_POSITION) {
            if (mDataChangeListener != null) {
                mDataChangeListener.onItemReorder(mSwappedItem, mStartDragPos, mStopDragPos);
            }

            AdapterTransaction revertReorderTransaction = new RevertReorderTransaction<>(this,
                    mStartDragPos, mStopDragPos);
            mTransactions.offer(revertReorderTransaction);
            mSwappedItem = null;
            mStopDragPos = INVALID_POSITION;
        }
    }

    /**
     * Enables or disables manual drag actions on items. Manual dragging is disabled by default.
     * To allow manual drags provide draggable view, see {@link GestureViewHolder}.
     * @param allowState true to enable, false to disable
     */
    void allowManualDrag(boolean allowState) {
        mIsManualDragAllowed = allowState;
        notifyDataSetChanged();
    }

    private void setNewData(@NonNull List<T> data) {
        mData.clear();
        mData.addAll(data);
    }

    private void resetTransactions() {
        mTransactions.clear();
    }
}
