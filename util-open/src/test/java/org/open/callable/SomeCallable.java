package org.open.callable;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class SomeCallable implements Callable<Integer> {

	@Override
	public Integer call() throws Exception {
		return 10;
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		SomeCallable sc = new SomeCallable();
		FutureTask<Integer> oneTask = new FutureTask<Integer>(sc);
		//		oneTask.run();
		System.out.println(oneTask.isDone());
		Integer t = null;
		if (oneTask.isDone() && oneTask.isCancelled()) {
			//		if (true){
			t = oneTask.get();
			System.out.println(t);
		} else {
			System.out.println("running.");
		}

		Thread oneThread = new Thread(oneTask);
		oneThread.start();

		System.out.println(oneTask.isDone());
		t = oneTask.get();
		System.out.println(t);
	}

}
