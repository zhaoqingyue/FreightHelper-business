package com.mtq.bus.freighthelper.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ScrollViewForDrag extends ScrollView {

	public ScrollViewForDrag(Context context) {
		super(context);
	}

	public ScrollViewForDrag(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollViewForDrag(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean b = super.onTouchEvent(ev);
		return b;
	}
}
