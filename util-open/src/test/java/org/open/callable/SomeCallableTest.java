package org.open.callable;


import java.awt.EventQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.junit.Test;

public class SomeCallableTest {

	@Test
	public void testCall() throws InterruptedException, ExecutionException {
		ExecutorService pool = Executors.newCachedThreadPool();

		SomeCallable sc = new SomeCallable();
		FutureTask<Integer> oneTask = new FutureTask<Integer>(sc);

//		pool.execute(oneTask);

		//		pool.submit(sc);
		
		EventQueue.invokeLater(oneTask);

		System.out.println(oneTask.isDone());
		System.out.println(oneTask.get());

		//		pool.invokeAll(oneTask);

		pool.shutdown();
	}

}
