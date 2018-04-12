package com.thesurix.example.gesturerecycler.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.thesurix.example.gesturerecycler.R;
import com.thesurix.example.gesturerecycler.adapter.MonthsAdapter;
import com.thesurix.example.gesturerecycler.model.MonthItem;
import com.thesurix.gesturerecycler.DefaultItemClickListener;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureManager;
import com.thesurix.gesturerecycler.RecyclerItemTouchListener;

public class GridRecyclerFragment extends BaseFragment {

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);

        final MonthsAdapter adapter = new MonthsAdapter(R.layout.grid_item);
        adapter.setData(getMonths());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemTouchListener<>(
                new DefaultItemClickListener<MonthItem>() {

                    @Override
                    public boolean onItemClick(@NonNull MonthItem item, int position) {
                        Snackbar.make(view, "Click event on the " + position
                                + " position", Snackbar.LENGTH_SHORT).show();
                        return true;
                    }

                    @Override
                    public void onItemLongPress(@NonNull MonthItem item, int position) {
                        Snackbar.make(view, "Long press event on the " + position
                                + " position", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public boolean onDoubleTap(@NonNull MonthItem item, int position) {
                        Snackbar.make(view, "Double tap event on the " + position
                                + " position", Snackbar.LENGTH_SHORT).show();
                        return true;
                    }
                }));

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                MonthItem item = adapter.getItem(position);
                return item.getType() == MonthItem.MonthItemType.HEADER
                        ? manager.getSpanCount()
                        : 1;
            }
        });

        mGestureManager = new GestureManager.Builder(mRecyclerView)
                .setSwipeEnabled(true)
                .setLongPressDragEnabled(true)
                .build();

        adapter.setDataChangeListener(new GestureAdapter.OnDataChangeListener<MonthItem>() {
            @Override
            public void onItemRemoved(@NonNull MonthItem item, int position) {
                Snackbar.make(view, "Month removed from position "
                        + position, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onItemReorder(@NonNull MonthItem item, int fromPos, int toPos) {
                Snackbar.make(view, "Month moved from position " + fromPos + " to "
                        + toPos, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.recycler_drag_menu) {
            mGestureManager.setManualDragEnabled(!mGestureManager.isManualDragEnabled());
        }
        return super.onOptionsItemSelected(item);
    }
}
