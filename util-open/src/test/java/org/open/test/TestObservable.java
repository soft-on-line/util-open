package org.open.test;

import java.util.Observable;

public class TestObservable extends Observable 
{
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.setChanged();
//		this.notifyObservers();
		this.notifyObservers(name);
	}
	
}
