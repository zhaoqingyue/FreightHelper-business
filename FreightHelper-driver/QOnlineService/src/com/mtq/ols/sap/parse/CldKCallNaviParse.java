/*
 * @Title CldKCallNavi.java
 * @Copyright Copyright 2010-2015 Careland Software Co,.Ltd All Rights Reserved.
 * @author Zhouls
 * @date 2015-3-21 ÏÂÎç5:19:20
 * @version 1.0
 */
package com.mtq.ols.sap.parse;

import java.util.ArrayList;
import java.util.List;

import com.mtq.ols.sap.parse.CldKBaseParse.ProtBase;


/**
 * Àà×¢ÊÍ
 * 
 * @author Zhouls
 * @date 2015-3-21 ÏÂÎç5:19:20
 */
public class CldKCallNaviParse {
	public static class ProtKCallMobile extends ProtBase {
		private List<String> data;

		public ProtKCallMobile() {
			data = new ArrayList<String>();
		}

		public List<String> getData() {
			return data;
		}

		public void setData(List<String> data) {
			this.data = data;
		}
	}
}
