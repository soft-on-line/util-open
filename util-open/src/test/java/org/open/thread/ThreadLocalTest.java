package org.open.thread;


public class ThreadLocalTest {

	public static void main(String[] args) throws InterruptedException {
		Judge.prepare();

		Player a = new Player(1);
		a.start();
		a.join();

		Player b = new Player(2);
		b.start();
		b.join();

		Player c = new Player(3);
		c.start();
		c.join();
	}

}