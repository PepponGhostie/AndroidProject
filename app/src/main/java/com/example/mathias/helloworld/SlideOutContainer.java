package com.example.mathias.helloworld;

/**
 * Created by Mathias on 18-09-2015.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.jar.Attributes;

public class SlideOutContainer extends LinearLayout
{
    private View menu;
    private View content;

    protected static final int menuMargin = 150;

    public enum MenuState{
        CLOSED, OPEN
    };

    //position information attributes
    protected int currentContentOffset = 0;
    protected MenuState menuCurrentState = MenuState.CLOSED;

    public SlideOutContainer (Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

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
                this.menu.setVisibility(View.VISIBLE);
                this.currentContentOffset = this.getMenuWidth();
                this.content.offsetLeftAndRight(currentContentOffset);
                this.menuCurrentState = MenuState.OPEN;
                break;
            case OPEN:
                this.content.offsetLeftAndRight(-currentContentOffset);
                this.currentContentOffset = 0;
                this.menuCurrentState = MenuState.CLOSED;
                this.menu.setVisibility(View.GONE);
                break;
        }
        this.invalidate();
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

}
