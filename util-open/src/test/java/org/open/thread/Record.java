package org.open.thread;

import java.util.ArrayList;
import java.util.List;


class Record {

	private List<Integer> attemptList = new ArrayList<Integer>(); ;

	public void save(int value) {
		attemptList.add(value);
	}

	public boolean contains(int value) {
		return attemptList.contains(value);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(attemptList.size() + " Times: ");
		int count = 1;
		for (Integer attempt : attemptList) {
			buffer.append(attempt);
			if (count < attemptList.size()) {
				buffer.append(", ");
				count++;
			}
		}
		return buffer.toString();
	}

}
