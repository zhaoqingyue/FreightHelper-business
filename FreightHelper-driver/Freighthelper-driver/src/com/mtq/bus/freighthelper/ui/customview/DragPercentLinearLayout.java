package com.mtq.bus.freighthelper.ui.customview;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhy.android.percent.support.PercentLinearLayout;

public class DragPercentLinearLayout extends PercentLinearLayout {

	private ViewDragHelper mDragger;

	private ViewDragHelper.Callback callback;

	private View mSupportDragView;

	private boolean mOnlyVerticalDrag = true;

	private int mTopLimit;
	private int mBottomLimit;

	private final int OFFSET = 50;

	private boolean firstCome = true;

	public DragPercentLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		mDragger = ViewDragHelper.create(this, 1.0f,
				new ViewDragHelper.Callback() {
					@Override
					public boolean tryCaptureView(View child, int pointerId) {
						if (mSupportDragView == null) {
							return true;
						}

						if (mSupportDragView.equals(child)) {
							return true;
						}

						return false;
					}

					@Override
					public int clampViewPositionHorizontal(View child,
							int left, int dx) {
						if (mOnlyVerticalDrag) {
							// 只支持上下拖
							return 0;
						}

						return left;
					}

					@Override
					public int clampViewPositionVertical(View child, int top,
							int dy) {
						if (top < mTopLimit) {
							return mTopLimit;
						}

						if (top > mBottomLimit) {
							return mBottomLimit;
						}

						return top;
					}

					@Override
					public void onEdgeDragStarted(int edgeFlags, int pointerId) {
						mDragger.captureChildView(mSupportDragView, pointerId);
					}

					@Override
					public void onViewReleased(View releasedChild, float xvel,
							float yvel) {
						if (mSupportDragView == null) {
							return;
						}

						if (releasedChild.equals(mSupportDragView)) {
							int top = mSupportDragView.getTop();
							if (top <= mBottomLimit / 4) {
								mDragger.settleCapturedViewAt(0, 0);
							} else if (top >= mBottomLimit * 0.75) {
								mDragger.settleCapturedViewAt(0, mBottomLimit);
							} else {
								mDragger.settleCapturedViewAt(0,
										mBottomLimit >> 1);
							}

							invalidate();
						}
					}

				});

		mDragger.setEdgeTrackingEnabled(ViewDragHelper.DIRECTION_ALL);

	}

	@Override
	public void computeScroll() {
		if (mDragger.continueSettling(true)) {
			invalidate();
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (mSupportDragView != null
				&& (event.getRawY() < mSupportDragView.getTop() + getTop()
						+ OFFSET)) {
			return false;
		}

		final int action = MotionEventCompat.getActionMasked(event);
		if (action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_UP) {
			mDragger.cancel();
			return false;
		}
		return mDragger.shouldInterceptTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mSupportDragView != null
				&& (event.getRawY() < mSupportDragView.getTop() + getTop()
						+ OFFSET)) {
			return false;
		}
		mDragger.processTouchEvent(event);
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (firstCome) {
			super.onLayout(changed, l, t, r, b);
		}
		firstCome = false;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// mSupportDragView = getChildAt(0);
	}

	public View getmSupportDragView() {
		return mSupportDragView;
	}

	public void setmSupportDragView(View mSupportDragView) {
		this.mSupportDragView = mSupportDragView;
	}

	public boolean ismOnlyVerticalDrag() {
		return mOnlyVerticalDrag;
	}

	public void setmOnlyVerticalDrag(boolean mOnlyVerticalDrag) {
		this.mOnlyVerticalDrag = mOnlyVerticalDrag;
	}

	public void setmTopLimit(int mTopLimit) {
		this.mTopLimit = mTopLimit;
	}

	public void setmBottomLimit(int mBottomLimit) {
		this.mBottomLimit = mBottomLimit;
	}

	public void setFirstCome(boolean firstCome) {
		this.firstCome = firstCome;
	}

	public void onLayout(int l, int t, int r, int b) {
		mSupportDragView.layout(l, t, r, b);
	}
}
