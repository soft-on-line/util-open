package org.open.test;


class TestRunnableMini implements Runnable {
	
	static ThreadLocal<Integer> tlSum = new ThreadLocal<Integer>();

	int                  sum   = 100;

	public TestRunnableMini() {
		tlSum.set(100);
		System.out.println("I:"+Thread.currentThread());
		System.out.println("I:" + tlSum.get());
	}

	public void print() {
		System.out.println("P:"+Thread.currentThread());
		System.out.println("P:" + tlSum.get());
	}

	public void run() {
		//		//实现 
		//		while (sum > 0) {
		//			System.out.println(sum);
		//			sum--;
		//		}
		sum -= 5;
		System.out.println(sum);

		print();
		System.out.println("B:"+Thread.currentThread());
		System.out.println("B:" + tlSum.get());
		//		tlSum.set(tlSum.get() - 5);
		System.out.println(tlSum.get());
	}
}

class TestRunnable {

	public static void main(String[] arg) {
		TestRunnableMini tr = new TestRunnableMini(); //构造一个Runnable实例 

		//同时用 tr构造了两个线程，他们将共用一个变量 sum
		//如果用普通的Thread方法，则各自用自己的Sum变量。相当于两个独立的类了
		Thread t1 = new Thread(tr);
		t1.start();

		Thread t2 = new Thread(tr);
		t2.start();
	}
}