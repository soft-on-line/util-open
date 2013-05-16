package org.open.thread;

import java.util.Random;

class Attempt {

	private static ThreadLocal<Record> history = new ThreadLocal<Record>();

	public static int guess(int maxValue) {
		Record record = getRecord();
		Random random = new Random();
		int value = 0;
		do {
			value = random.nextInt(maxValue) + 1;
		} while (record.contains(value));
		record.save(value);
		return value;
	}

	public static void review(String info) {
		System.out.println(info + getRecord());
	}

	private static Record getRecord() {
		Record record = history.get();
		if (record == null) {
			record = new Record();
			history.set(record);
		}
		return record;
	}

}