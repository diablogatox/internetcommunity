package com.mofang.util;

import java.util.Comparator;

import com.mofang.pb.Contacts;

@SuppressWarnings("rawtypes")
public class PinyinComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		String str1 = PinyinUtils.getPingYin(((Contacts) o1).getPy());
		String str2 = PinyinUtils.getPingYin(((Contacts) o2).getPy());
		return str1.compareTo(str2);
	}

}
