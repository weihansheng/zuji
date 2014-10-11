package com.project.zuji.tools;

public class TextTool {
	public static int intLatitude(int intStr) {
		if (intStr < 1e1) {
			intStr = intStr * 10000000;
		} else if (intStr < 1e2) {
			intStr = intStr * 1000000;
		} else if (intStr < 1e3) {
			intStr = intStr * 100000;
		} else if (intStr < 1e4) {
			intStr = intStr * 10000;
		} else if (intStr < 1e5) {
			intStr = intStr * 1000;
		} else if (intStr < 1e6) {
			intStr = intStr * 100;
		} else if (intStr < 1e7) {
			intStr = intStr * 10;
		} else if (intStr < 1e8) {
			intStr = intStr * 1;
		}
		return intStr;

	}

	public static int intLongitude(int intStr) {
		if (intStr < 1e1) {
			intStr = intStr * 100000000;
		} else if (intStr < 1e2) {
			intStr = intStr * 10000000;
		} else if (intStr < 1e3) {
			intStr = intStr * 1000000;
		} else if (intStr < 1e4) {
			intStr = intStr * 100000;
		} else if (intStr < 1e5) {
			intStr = intStr * 10000;
		} else if (intStr < 1e6) {
			intStr = intStr * 1000;
		} else if (intStr < 1e7) {
			intStr = intStr * 100;
		} else if (intStr < 1e8) {
			intStr = intStr * 10;
		}else if (intStr < 1e9) {
			intStr = intStr * 1;
		}
		return intStr;

	}
}
