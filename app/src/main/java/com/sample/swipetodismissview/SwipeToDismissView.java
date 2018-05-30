package com.sample.swipetodismissview;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * This class is used to show swipe view to dismiss from left to right and vice-versa
 * They also handle alpha value of that view from 1 to 0 and 0 to 1
 * This is only for horizontal swipe not for vertical
 */
public class SwipeToDismissView implements View.OnTouchListener {

    private int _ViewWidth;
    // Store state for swiping is left or right
    private boolean _IsLeftSwipe = true;
    private float _ViewX;
    private View _root;
    private SwipeCallback _Callback;

    public SwipeToDismissView(SwipeCallback callback) {
        _Callback = callback;
    }

    /**
     * You have to create your own view with parent relative layout and child as any view and
     * Provide id for those view for swipable layout
     * To initialize view with parent(Any layout) and child(View which you want to swipe to dismiss)
     * @param root  Parent container for child view which you want to swipe to dismiss
     * @param child Child view which you want to dismiss
     */
    public void init(View root, final View child) {
        this._root = root;
        child.setOnTouchListener(this);
        child.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                _ViewX = child.getX();
                _ViewWidth = child.getWidth();
                child.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                float swipedPortion;
                // Calculate swiped portion according to left or right swipe gesture occur
                if (_IsLeftSwipe) {
                    swipedPortion = Math.abs(view.getX() - getScreenWidth() / 2);
                } else {
                    swipedPortion = Math.abs(view.getX() + view.getWidth() - getScreenWidth() / 2);
                }
                // If swiped portion is more from view width then set visibility GONE
                // Otherwise set alpha to that view
                if (swipedPortion > _ViewWidth) {
                    view.setVisibility(View.GONE);
                    if (_Callback != null) {
                        _Callback.onDismiss(view);
                    }
                } else {
                    view.setX(_ViewX);
                    view.setAlpha(1);
                    if (_Callback != null) {
                        _Callback.onCancel(view);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (X < getScreenWidth() / 2) {
                    _IsLeftSwipe = true;
                    // Calculating alpha value for left swipe
                    float decreasingAlpha = X / (getScreenWidth() / 2f);
                    if (decreasingAlpha != 0) {
                        view.setAlpha(decreasingAlpha);
                    }
                } else {
                    _IsLeftSwipe = false;
                    // Calculating alpha value for right swipe
                    float increasingAlpha = (getScreenWidth() - X) / (getScreenWidth() / 2f);
                    if (increasingAlpha != 0) {
                        view.setAlpha(increasingAlpha);
                    }
                }
                view.setX(X - _ViewWidth / 2);
                break;
        }
        _root.invalidate();
        return true;
    }

    // Get the width of parent layout and using this we calculate alpha value
    private int getScreenWidth() {
        return _root.getWidth();
    }

    public interface SwipeCallback {

        // Callback if view is dismissed
        void onDismiss(View view);

        // Callback if dismiss callback fail
        void onCancel(View view);
    }
}
