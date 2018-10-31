package com.mtq.bus.freighthelper.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtils {

	private static String TAG = "DensityUtils";
	private static DisplayMetrics mDm = null;
	
	/**
	 * @Title: getDensityWidth
	 * @Description: 获取DensityWidth
	 * @param context
	 */
	public static int getWidth(Context context) {
		if (mDm == null) 
			mDm = context.getResources().getDisplayMetrics();
		
		int width = mDm.widthPixels;
		//CldLog.i(TAG, "sWidth: " + width);
		return width;
	}
	
	/**
	 * @Title: getDensityHeight
	 * @Description: 获取DensityHeight
	 * @param context
	 */
	public static int getHeight(Context context) {
		if (mDm == null) 
			mDm = context.getResources().getDisplayMetrics();
		
		int height = mDm.heightPixels;
		//CldLog.i(TAG, "sHeight: " + height);
		return height;
	}
	
	/**
	 * @Title: getDensityDpi
	 * @Description: 获取DensityDpi
	 * @param context
	 */
	public static int getDpi(Context context) {
		if (mDm == null) 
			mDm = context.getResources().getDisplayMetrics();
		
		int desityDpi = mDm.densityDpi;
		//CldLog.i(TAG, "sDesityDpi: " + desityDpi);
		return desityDpi;
	}
	
	/** 
	 * @Title: getDensity
	 * @Description: 获取Density
	 * @param context
	 */
	public static float getDensity(Context context) {
		if (mDm == null) 
			mDm = context.getResources().getDisplayMetrics();
		
		float desity = mDm.density;
		//CldLog.i(TAG, "sDesity: " + desity);
		return desity;
	}
	
	/**
	 * @Title: dip2px
	 * @Description: 将dip转化成px
	 * @param context
	 * @param dpValue
	 */
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
   
    /**
     * @Title: px2dip
     * @Description: 将px转化成dip
     * @param context
     * @param pxValue
     */
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    
    public static final int DEFAULT_SIZE = 10;
	
	public static int getDedaultSize(Context context) {
		return DensityUtils.dip2px(context, DEFAULT_SIZE);
	}
}
