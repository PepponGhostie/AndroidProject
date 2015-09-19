package com.example.mathias.helloworld;

/**
 * Created by Mathias on 18-09-2015.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.os.Handler;
import android.view.animation.Interpolator;

public class SlideOutContainer extends LinearLayout
{
    private View menu;
    private View content;

    protected static final int menuMargin = 150;

    public enum MenuState{
        CLOSED, OPEN, CLOSING, OPENING
    };

    //position information attributes
    protected int currentContentOffset = 0;
    protected MenuState menuCurrentState = MenuState.CLOSED;

    public SlideOutContainer (Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    //Animation Objects
    protected Scroller menuAnimationScroller = new Scroller(this.getContext(),new SmoothInterpolator());
    protected Runnable menuAnimationRunnable = new AnimationRunnable();
    protected Handler menuAnimationHandler = new Handler();

    //Animation contants
    private static final int menuAnimationDuration = 1000;
    private static final int menuAnimationPollingInterval = 16;

    protected void onAttachedToWindow(){
        super.onAttachedToWindow();
        this.menu = this.getChildAt(0);
        this.content = this.getChildAt(1);
        this.menu.setVisibility(View.GONE);
    }

    protected void onLayout (boolean changed, int left, int top, int right, int bottom){
        if(changed)
            this.calculateChildDimensions();

        this.menu.layout(left, top, right - menuMargin, bottom);
        this.content.layout(left + this.currentContentOffset, top, right + this.currentContentOffset, bottom);
    }

    public void toggleMenu(){
        switch (this.menuCurrentState) {
            case CLOSED:
                this.menuCurrentState = MenuState.OPEN;
                this.menu.setVisibility(View.VISIBLE);
                this.menuAnimationScroller.startScroll(0,0,this.getMenuWidth(),0,menuAnimationDuration);
                break;
            case OPEN:

                this.menuCurrentState = MenuState.CLOSED;
                this.menuAnimationScroller.startScroll(this.currentContentOffset,0,-this.currentContentOffset,0,menuAnimationDuration);
                break;
            default:
                return;
        }
        this.menuAnimationHandler.postDelayed(this.menuAnimationRunnable,menuAnimationPollingInterval);
    }

    protected class SmoothInterpolator implements Interpolator{
        public float getInterpolation (float t){
            return (float) Math.pow(t - 1, 5) + 1;
        }
    }

    protected class AnimationRunnable implements Runnable {
        public void run(){
            boolean isAnimationOnGoing = SlideOutContainer.this.menuAnimationScroller.computeScrollOffset();
            SlideOutContainer.this.adjustContentPosition(isAnimationOnGoing);
        }
    }

    private int getMenuWidth(){
        return this.menu.getLayoutParams().width;
    }

    private void calculateChildDimensions(){
        this.content.getLayoutParams().height = this.getHeight();
        this.content.getLayoutParams().width = this.getWidth();
        this.menu.getLayoutParams().height = this.getHeight() -menuMargin;
        this.menu.getLayoutParams().width = this.getWidth();
    }

    private void adjustContentPosition(boolean isAnimationOngoing) {
        int scrollerOffset = this.menuAnimationScroller.getCurrX();

        this.content.offsetLeftAndRight(scrollerOffset
                - this.currentContentOffset);

        this.currentContentOffset = scrollerOffset;

        this.invalidate();

        if (isAnimationOngoing)
            this.menuAnimationHandler.postDelayed(this.menuAnimationRunnable,
                    menuAnimationPollingInterval);
        else
            this.onMenuTransitionComplete();
    }

    private void onMenuTransitionComplete() {
        switch (this.menuCurrentState) {
            case OPENING:
                this.menuCurrentState = MenuState.OPEN;
                break;
            case CLOSING:
                this.menuCurrentState = MenuState.CLOSED;
                this.menu.setVisibility(View.GONE);
                break;
            default:
                return;
        }
    }

}
