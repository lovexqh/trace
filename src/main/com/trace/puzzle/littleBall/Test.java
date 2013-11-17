package com.trace.puzzle.littleBall;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.trace.puzzle.littleBall.enumbean.Direction;

public class Test implements Comparator<Object> {

	public int compare(Object o1, Object o2) {
		return toInt(o1) - toInt(o2);
	}

	private int toInt(Object o) {
		String str = (String) o;
		str = str.replaceAll("一", "1");
		str = str.replaceAll("二", "2");
		str = str.replaceAll("三", "3");
		// 
		return Integer.parseInt(str);
	}

	/**
	 * 测试方法
	 */
	public static void main(String[] args) {
		List<Direction> tmps = new ArrayList<Direction>();
		System.out.println(Direction.values().toString());
	}
}
