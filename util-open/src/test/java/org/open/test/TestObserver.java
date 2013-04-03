package org.open.test;

import java.util.Observable;
import java.util.Observer;

public class TestObserver implements Observer 
{
	private TestObservable testObservable;
	
	public TestObserver(TestObservable testObservable)
	{
		this.testObservable = testObservable;
		this.testObservable.addObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg) 
	{
		System.out.println(o.getClass());
		System.out.println(arg.getClass());
		System.out.println(this.testObservable.getName()+"update.");
	}

}
