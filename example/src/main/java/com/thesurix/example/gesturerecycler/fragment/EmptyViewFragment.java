package com.thesurix.example.gesturerecycler.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.thesurix.example.gesturerecycler.R;
import com.thesurix.example.gesturerecycler.adapter.MonthsAdapter;
import com.thesurix.example.gesturerecycler.callback.MonthDiffCallback;
import com.thesurix.example.gesturerecycler.model.Month;
import com.thesurix.example.gesturerecycler.model.MonthItem;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmptyViewFragment extends BaseFragment {

    private MonthsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);

        mAdapter = new MonthsAdapter(R.layout.linear_item_with_background);
        mAdapter.setData(getMonths());
        mAdapter.setUndoSize(2);
        mAdapter.setDataChangeListener(new GestureAdapter.OnDataChangeListener<MonthItem>() {
            @Override
            public void onItemRemoved(final MonthItem item, final int position) {
                final Snackbar undoSnack = Snackbar.make(view, "Month removed from position " + position, Snackbar.LENGTH_SHORT);
                undoSnack.setAction(R.string.undo_text, new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        mAdapter.undoLast();
                    }
                });
                undoSnack.show();
            }

            @Override
            public void onItemReorder(final MonthItem item, final int fromPos, final int toPos) {
                final Snackbar undoSnack = Snackbar.make(view, "Month moved from position " + fromPos + " to " + toPos, Snackbar.LENGTH_SHORT);
                undoSnack.setAction(R.string.undo_text, new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        mAdapter.undoLast();
                    }
                });
                undoSnack.show();
            }
        });

        final View emptyView = view.findViewById(R.id.empty_root);
        mAdapter.setEmptyView(emptyView);

        mRecyclerView.setAdapter(mAdapter);

        mGestureManager = new GestureManager.Builder(mRecyclerView)
                .setSwipeEnabled(true)
                .setSwipeFlags(ItemTouchHelper.LEFT)
                .setLongPressDragEnabled(true)
                .build();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.recycler_empty_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recycler_undo_menu:
                mAdapter.undoLast();
                break;
            case R.id.recycler_clear_menu:
                mAdapter.clearData();
                break;
            case R.id.recycler_generate_menu:
                final List<MonthItem> months = getMonths();
                final int month = (int)(Math.random() * months.size());
                mAdapter.insert(months.get(month), 0);
                mRecyclerView.scrollToPosition(0);
                break;
            case R.id.recycler_diff_menu:
                final List<MonthItem> diffMonths = getMonths();
                Collections.shuffle(diffMonths);
                mAdapter.setData(diffMonths, new MonthDiffCallback(mAdapter.getData(), diffMonths));
                mRecyclerView.scrollToPosition(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected List<MonthItem> getMonths() {
        final List<MonthItem> monthList = new ArrayList<>();
        monthList.add(new Month("JAN", R.drawable.january));
        monthList.add(new Month("FEB", R.drawable.february));
        monthList.add(new Month("MAR", R.drawable.march));
        monthList.add(new Month("APR", R.drawable.april));
        monthList.add(new Month("MAY", R.drawable.may));
        monthList.add(new Month("JUN", R.drawable.june));

        return monthList;
    }
}
