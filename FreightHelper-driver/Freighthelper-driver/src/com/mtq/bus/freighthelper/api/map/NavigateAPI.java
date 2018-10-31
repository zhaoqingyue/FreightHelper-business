/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: NavigateAPI.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.api.map
 * @Description: 导航相关API
 * @author: zhaoqy
 * @date: 2017年6月13日 下午8:30:43
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.api.map;

import java.util.List;

import android.app.Activity;
import android.content.Context;

import com.cld.mapapi.map.CldMap.NaviInitListener;
import com.cld.mapapi.map.MapView;
import com.cld.navisdk.CldNaviManager;
import com.cld.navisdk.guide.CldNavigatorView;
import com.cld.navisdk.guide.CldSimulationView;
import com.cld.navisdk.hy.routeplan.CldHYRoutePlaner;
import com.cld.navisdk.hy.routeplan.HYRoutePlanParm;
import com.cld.navisdk.hy.routeplan.HYRoutePlanParm.HYTruckType;
import com.cld.navisdk.routeguide.CldNavigator;
import com.cld.navisdk.routeplan.CldRoutePlaner;
import com.cld.navisdk.routeplan.CldRoutePlaner.RoutePlanListener;
import com.cld.navisdk.routeplan.RoutePlanNode;
import com.cld.navisdk.util.view.CldProgress;
import com.cld.navisdk.util.view.CldProgress.CldProgressListener;
import com.cld.nv.hy.company.CldWayBillRoute;
import com.cld.nv.route.CldRoute;

public class NavigateAPI {

	private String TAG = "NavigateAPI";
	private static NavigateAPI mNavigateAPI;

	public static NavigateAPI getInstance() {
		synchronized (NavigateAPI.class) {
			if (mNavigateAPI == null) {
				synchronized (NavigateAPI.class) {
					mNavigateAPI = new NavigateAPI();
				}
			}
		}
		return mNavigateAPI;
	}

	// 初始化 sdk
	public void init(Context ctx, NaviInitListener listener) {
		CldNaviManager.getInstance().init(ctx, listener);
	}

	//
	public MapView createMapView(Context ctx) {
		return CldNaviManager.getInstance().createNMapView(ctx);
	}

	public CldNavigatorView init(Activity activity, MapView mapView) {
		return CldNavigator.getInstance().init(activity, mapView);
	}

	public CldSimulationView initSimulation(Activity activity, MapView mapView) {
		return CldNavigator.getInstance().initSimulation(activity, mapView);
	}

	// 开始导航
	public void startNavi() {
		CldNavigator.getInstance().startNavi();
	}

	// 调用导航模式相应回退方法
	public void onBackPressed() {
		CldNavigator.getInstance().onBackPressed();
	}

	public void onResume() {
		CldNavigator.getInstance().onResume();
	}

	// 计算路径
	/**
	 * @Title: routePlan
	 * @Description: 普通路线，这个接口，货运宝不调用
	 * @param ctx
	 * @param startNode
	 *            ：起点
	 * @param passListNode
	 *            ：经由地列表
	 * @param destinationNode
	 *            ： 终点
	 * @param planPreference
	 *            ：算路方式
	 * @param routePlanListener
	 *            ：回调
	 * @return: void
	 */
	public void routePlan(Context ctx, RoutePlanNode startNode,
			List<RoutePlanNode> passListNode, RoutePlanNode destinationNode,
			int planPreference, RoutePlanListener routePlanListener) {

		// 显示等待进度条
		CldProgress.showProgress(ctx, "正在规划路线...", new CldProgressListener() {

			@Override
			public void onCancel() {

			}
		});

		CldRoutePlaner.getInstance().routePlan(ctx, startNode, passListNode,
				destinationNode, planPreference, routePlanListener);
	}

	/**
	 * @Title: hyRoutePlan
	 * @Description: 计算货车路线，如果没有货车参数的话就调这个接口
	 * @param ctx
	 * @param startNode
	 *            ：起点
	 * @param passListNode
	 *            ：经由地列表
	 * @param destinationNode
	 *            ： 终点
	 * @param planPreference
	 *            ：算路方式
	 * @param routePlanListener
	 *            ：回调
	 * @return: void
	 */
	public void hyRoutePlan(Context ctx, RoutePlanNode startNode,
			List<RoutePlanNode> passListNode, RoutePlanNode destinationNode,
			int planPreference, RoutePlanListener routePlanListener) {
		HYRoutePlanParm hyRpParm = new HYRoutePlanParm();
		hyRpParm.truckType = HYTruckType.LightTruck;
		// hyRpParm.weightFlag = 1;
		// hyRpParm.weight = 40;
		// hyRpParm.height =1;
		// hyRpParm.width = 1;
		// hyRpParm.axleCount = 4;
		hyRoutePlan(ctx, startNode, passListNode, destinationNode,
				planPreference, hyRpParm, routePlanListener);
	}

	/**
	 * @Title: hyRoutePlan
	 * @Description: 计算货车路线，如果有货车参数的话就调这个接口
	 * @param ctx
	 * @param startNode
	 *            ：起点
	 * @param passListNode
	 *            ：经由地列表
	 * @param destinationNode
	 *            ： 终点
	 * @param planPreference
	 *            ：算路方式
	 * @param hyRpParm
	 *            ：货车参数
	 * @param routePlanListener
	 *            ：回调
	 * @return: void
	 */
	public void hyRoutePlan(final Context ctx, RoutePlanNode startNode,
			List<RoutePlanNode> passListNode, RoutePlanNode destinationNode,
			int planPreference, HYRoutePlanParm hyRpParm,
			RoutePlanListener routePlanListener) {

		// 显示等待进度条
		if (!CldProgress.isShowProgress())
			CldProgress.showProgress(ctx, "正在规划路线...",
					new CldProgressListener() {

						@Override
						public void onCancel() {
							CldRoute.cancleRoutePlan();
							if (ctx != null) {
								((Activity) ctx).runOnUiThread(new Runnable() {

									@Override
									public void run() {
										if (CldProgress.isShowProgress()) {
											CldProgress.cancelProgress();
										}
									}
								});
							}
						}
					});

		CldWayBillRoute.isEnterpriseRouteActive = false;
		CldHYRoutePlaner.getInstance().hyRoutePlan(ctx, startNode,
				passListNode, destinationNode, planPreference, hyRpParm,
				routePlanListener);
	}

}
