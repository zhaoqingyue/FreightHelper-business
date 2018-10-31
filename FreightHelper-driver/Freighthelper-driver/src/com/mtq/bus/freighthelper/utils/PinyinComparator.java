package com.mtq.bus.freighthelper.utils;

import java.util.Comparator;

import com.mtq.bus.freighthelper.bean.Driver;

public class PinyinComparator implements Comparator<Driver> {

	public int compare(Driver o1, Driver o2) {
		if (o1.letter.equals("@") || o2.letter.equals("#")) {
			return -1;
		} else if (o1.letter.equals("#") || o2.letter.equals("@")) {
			return 1;
		} else {
			return o1.letter.compareTo(o2.letter);
		}
	}
}
