package org.open.thread;


import java.util.ArrayList;
import java.util.List;

class Player extends Thread {

	private int           playerId;

	private List<Integer> myGuess = new ArrayList<Integer>(); ;

	public Player(int playerId) {
		this.playerId = playerId;
	}

	@Override
	public void run() {
		boolean success = false;
		while (!success) {
			int value = Attempt.guess(Judge.MAX_VALUE);
			myGuess.add(value);
			success = Judge.judge(value);
			System.out.println(String.format("Plyaer %s Attempts %s and %s", playerId, value, success ? " Success" : "Failed"));
		}
		System.out.println("MY->" + this.playerId + "->" + myGuess);
		Attempt.review(String.format("[IFNO] Plyaer %s Completed by ", playerId));
	}

}