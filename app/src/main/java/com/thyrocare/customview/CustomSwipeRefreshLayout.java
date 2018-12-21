package com.thyrocare.customview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by Orion on 7/10/15.
 */
public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {

	private int mTouchSlop;
	private float mPrevX;
	// Indicate if we've already declined the move event
	private boolean mDeclined;

	public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {

		switch (event.getAction()){
		case MotionEvent.ACTION_DOWN:
			mPrevX = MotionEvent.obtain(event).getX();
			mDeclined = false; // New action
			break;

		case MotionEvent.ACTION_MOVE:
			final float eventX = event.getX();
			float xDiff = Math.abs(eventX - mPrevX);

			if (mDeclined || xDiff > mTouchSlop){
				mDeclined = true; // Memorize
				return false;
			}
		}

		return super.onInterceptTouchEvent(event);
	}

}