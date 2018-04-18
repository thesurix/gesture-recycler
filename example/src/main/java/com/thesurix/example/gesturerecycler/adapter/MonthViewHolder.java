package com.thesurix.example.gesturerecycler.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thesurix.example.gesturerecycler.R;
import com.thesurix.example.gesturerecycler.model.Month;
import com.thesurix.example.gesturerecycler.model.MonthItem;
import com.thesurix.gesturerecycler.GestureViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthViewHolder extends GestureViewHolder<MonthItem> {

    private static final int SELECT_DURATION_IN_MS = 250;

    @BindView(R.id.month_text)
    TextView mMonthText;

    @BindView(R.id.month_image)
    ImageView mMonthPicture;

    @BindView(R.id.mont_drag)
    ImageView mItemDrag;

    @Nullable
    @BindView(R.id.foreground)
    View mForegroundView;

    @Nullable
    @BindView(R.id.month_background_stub)
    ViewStub mBackgroundView;

    public MonthViewHolder(@NonNull View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public void bindHolder(MonthItem monthItem) {
        Month month = (Month) monthItem;
        mMonthText.setText(month.getName());

        Glide.with(getContext())
                .load(month.getDrawableId())
                .apply(RequestOptions.centerCropTransform())
                .into(mMonthPicture);
    }

    @Nullable
    @Override
    public View getDraggableView() {
        return mItemDrag;
    }

    @Override
    public View getForegroundView() {
        return mForegroundView == null ? super.getForegroundView() : mForegroundView;
    }

    @Nullable
    @Override
    public View getBackgroundView() {
        return mBackgroundView;
    }

    @Override
    public void onItemSelected() {
        int textColorFrom = ContextCompat.getColor(getContext(), android.R.color.white);
        int textColorTo = ContextCompat.getColor(getContext(), R.color.indigo_500);
        ValueAnimator textAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),
                textColorFrom, textColorTo);
        textAnimation.setDuration(SELECT_DURATION_IN_MS);
        textAnimation.addUpdateListener(getTextAnimatorListener(mMonthText, textAnimation));
        textAnimation.start();

        int backgroundColorFrom = ContextCompat.getColor(getContext(), R.color.indigo_500);
        int backgroundColorTo = ContextCompat.getColor(getContext(), android.R.color.white);
        ValueAnimator backgroundAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),
                backgroundColorFrom, backgroundColorTo);
        backgroundAnimation.setDuration(SELECT_DURATION_IN_MS);
        backgroundAnimation.addUpdateListener(getBackgroundAnimatorListener(mMonthText,
                backgroundAnimation));
        backgroundAnimation.start();
    }

    @Override
    public void onItemSelectionClear() {
        int textColorFrom = ContextCompat.getColor(getContext(), R.color.indigo_500);
        int textColorTo = ContextCompat.getColor(getContext(), android.R.color.white);
        ValueAnimator textAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),
                textColorFrom, textColorTo);
        textAnimation.setDuration(SELECT_DURATION_IN_MS);
        textAnimation.addUpdateListener(getTextAnimatorListener(mMonthText, textAnimation));
        textAnimation.start();

        int backgroundColorFrom = ContextCompat.getColor(getContext(), android.R.color.white);
        int backgroundColorTo = ContextCompat.getColor(getContext(), R.color.indigo_500);
        ValueAnimator backgroundAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),
                backgroundColorFrom, backgroundColorTo);
        backgroundAnimation.setDuration(SELECT_DURATION_IN_MS);
        backgroundAnimation.addUpdateListener(getBackgroundAnimatorListener(mMonthText,
                backgroundAnimation));
        backgroundAnimation.start();
    }

    @Override
    public void unbindHolder() {
        if (getForegroundView() != null) {
            getForegroundView().setTranslationX(0f);
        }
    }

    @Override
    public boolean canDrag() {
        return true;
    }

    @Override
    public boolean canSwipe() {
        return true;
    }

    private ValueAnimator.AnimatorUpdateListener getBackgroundAnimatorListener(
            final TextView view,
            final ValueAnimator animator
    ) {
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }
        };
    }

    private ValueAnimator.AnimatorUpdateListener getTextAnimatorListener(
            final TextView view,
            final ValueAnimator animator
    ) {
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setTextColor((int) animator.getAnimatedValue());
            }
        };
    }
}
