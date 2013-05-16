package org.open.thread;

import java.util.Random;

class Judge {

	public static int  MAX_VALUE = 10;
	private static int targetValue;

	public static void prepare() {
		Random random = new Random();
		targetValue = random.nextInt(MAX_VALUE) + 1;
	}

	public static boolean judge(int value) {
		return value == targetValue;
	}

}