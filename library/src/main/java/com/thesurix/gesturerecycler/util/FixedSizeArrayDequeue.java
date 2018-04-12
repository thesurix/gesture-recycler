package com.thesurix.gesturerecycler.util;

import java.util.ArrayDeque;

/**
 * @author thesurix
 */
public class FixedSizeArrayDequeue<E> extends ArrayDeque<E> {

    private int mMaxSize;

    public FixedSizeArrayDequeue(int maxSize) {
        super(maxSize);
        mMaxSize = maxSize;
    }

    @Override
    public boolean offer(E e) {
        if (size() == mMaxSize) {
            removeFirst();
        }

        return super.offer(e);
    }
}
