package org.open.test;

public class TestWraperObject extends TestObject {
	
	private TestObject testObject;
	
	public TestWraperObject(TestObject testObject){
		this.testObject = testObject;
	}
	
	public String toString(){
		
		testObject.toString();
		
		System.out.println(TestWraperObject.class.getName());
		return TestWraperObject.class.getName();
	}
	
	public static void main(String[] args)
	{
		TestObject testObject = new TestObject();
		
		TestWraperObject testWraperObject = new TestWraperObject(testObject);
		testWraperObject.toString();
	}
}
