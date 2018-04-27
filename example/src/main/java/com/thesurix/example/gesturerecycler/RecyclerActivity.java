package com.thesurix.example.gesturerecycler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.thesurix.example.gesturerecycler.fragment.EmptyViewFragment;
import com.thesurix.example.gesturerecycler.fragment.GridRecyclerFragment;
import com.thesurix.example.gesturerecycler.fragment.LinearRecyclerFragment;

public class RecyclerActivity extends AppCompatActivity {

    private static final String EXTRA_RECYCLER_OPTION = "recycler_option";

    public enum RecyclerOption {
        LINEAR, GRID, EMPTY
    }

    public static Intent getIntent(Context ctx, RecyclerOption option) {
        Intent intent = new Intent(ctx, RecyclerActivity.class);
        intent.putExtra(EXTRA_RECYCLER_OPTION, option.ordinal());

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        int optionOrdinal = getIntent().getIntExtra(EXTRA_RECYCLER_OPTION,
                RecyclerOption.LINEAR.ordinal());
        RecyclerOption option = RecyclerOption.values()[optionOrdinal];

        Fragment fragment;
        switch (option) {
            case GRID:
                fragment = new GridRecyclerFragment();
                break;
            case EMPTY:
                fragment = new EmptyViewFragment();
                break;
            default:
            case LINEAR:
                fragment = new LinearRecyclerFragment();
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_placeholder, fragment)
                .commit();
    }
}
