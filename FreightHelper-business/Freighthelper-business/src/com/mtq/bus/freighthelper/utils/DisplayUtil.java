package com.mtq.bus.freighthelper.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.mtq.bus.freighthelper.application.BFApplication;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

/**
 * 屏幕显示工具类
 * 
 */
public class DisplayUtil {

	private static final String TAG = "DisplayUtil";
	private static int mScreenHeight;//屏幕可绘制的 高度
	private static int mScreenWidth;//屏幕绘制部分的宽度
	private static float autoWidth;//UI出图对应的宽
	private static float autoHeight;//UI出图对应的高
	static {
		int mWidth = BFApplication.mContext.getResources().getDisplayMetrics().widthPixels;
		int mHeght = BFApplication.mContext.getResources().getDisplayMetrics().heightPixels;
		
		mScreenHeight = mHeght < mWidth?mWidth:mHeght;
		mScreenWidth = mHeght < mWidth?mHeght:mWidth;
		Log.e(TAG, "mScreenHeight:"+mScreenHeight+"...mScreenWidth:"+mScreenWidth);
		int tempWidth = getConfigInt( BFApplication.mContext,"design_width");
		if (tempWidth > 0){
			autoWidth = tempWidth;
			autoHeight = getConfigInt( BFApplication.mContext,"design_height");
			Log.e(TAG, "autoWidth"+autoWidth+"...autoHeight"+autoHeight);
		}
	}
	/**
	 * @param key
	 *            键名
	 * @return 返回字符串
	 */
	public static int getConfigInt(Context context, String key)
	{
		int val = 0;
		try
		{
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Object temp = appInfo.metaData.get(key);
			if (temp == null)
			{
				Log.e("UICOMMON", "please set config value for " + key + " in manifest.xml first");
			}else{
				val = (Integer)temp;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return val;
	}

	/**
	 * 获取通知栏告诉.
	 *
	 *        上下文环境.
	 * @return 通知栏高度.
	 * 
	 */
	public static int getStatusBarHeight() {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = BFApplication.mContext.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * 
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 * 
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}


	/**
	 * 获取屏幕宽度
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 *
	 * @return
	 */
	public static int getScreenWidth() {
		return mScreenWidth;
	}

	/**
	 * 整个屏幕的高度包括 虚拟按键 和状态栏
	 * @return
	 */
	public static int getAllScreenHeight( ){
		int dpi = 0;
		WindowManager windowManager = (WindowManager) BFApplication.mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
			method.invoke(display, displayMetrics);
			dpi=displayMetrics.heightPixels;
		}catch(Exception e){
			e.printStackTrace();
		}
		return dpi;
	}
	/**
	 * 得到屏幕的高 没包括虚拟键盘
	 * 
	 * @return
	 */
	public static int getScreenHeight() {
		return mScreenHeight;
	}
	
	/**
	 * 得到屏幕除去静态状态栏的显示高度(px)
	 * 
	 * @return
	 */
	public static int getShowHeight() {
		int showHeight = mScreenHeight - getStatusBarHeight();
		return showHeight;
	}

	public static int getMesureSize(int with){
		float scleX = getScreenWidth()/autoWidth;
		float scleY = getShowHeight()/autoHeight;
		float scle = scleY>scleX?scleX:scleY;
		return (int) (with*scle);
	}
	public static int getByWidthPosition(int posX){
		float scleX = getScreenWidth()/autoWidth;
		return (int) (posX*scleX);
	}
	public static int getByHeightPosition(int posY){
		float scleY = getShowHeight()/autoHeight;
		return (int) (posY*scleY);
	}
	public static float getMeSureHeight(int posY){
		float scleY = getShowHeight()/autoHeight;
		return (posY*scleY);
	}
	/**
	 * 根据比例设置控件的宽高
	 * 
	 * @param context
	 *        上下文
	 * @param view
	 *        需要设置宽高的控件
	 * @param l
	 *        给定的比例
	 */
	public static void setViewWH(Context context, View view, float l) {
		int screenWidth = DisplayUtil.getScreenWidth();
		int height = (int) (screenWidth / l);
		view.setLayoutParams(new LayoutParams(screenWidth, height));
	}

	/**
	 * 根据比例设置控件的宽高(迪讯好友资料页面专用)
	 * 
	 * @param context
	 *        上下文
	 * @param view
	 *        需要设置宽高的控件
	 * @param l
	 *        给定的比例
	 */
	public static void setViewWH_Dixun(Context context, View view, int v, float l) {
		int screenWidth = DisplayUtil.getScreenWidth();
		int height = (int) (screenWidth / l);
		android.widget.RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth, height);
		layoutParams.addRule(RelativeLayout.ABOVE, v);
		view.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth, height));

	}



	/**
	 * 显示/隐藏 软键盘
	 * 
	 * 
	 * @param show
	 *        true显示软键盘，false关闭软键盘
	 * @param context
	 */
	public static void showInput(boolean show, Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		View currentFocus = ((Activity) context).getCurrentFocus();
		if (currentFocus != null) {
			IBinder applicationWindowToken = currentFocus.getApplicationWindowToken();
			if (show) {
				if (applicationWindowToken != null) {
					imm.showSoftInputFromInputMethod(applicationWindowToken, 0);
				}
			} else {
				if (applicationWindowToken != null) {
					imm.hideSoftInputFromWindow(applicationWindowToken, 0);
				}
			}
		}
	}

}
