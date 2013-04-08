package org.open;


import org.junit.Test;
import org.open.util.debug.DebugUtil;

public class MessageTest {

	@Test
	public void testInString() {
		Message.instance.in("test");
		Message.instance.in("test1");
	}

	@Test
	public void testInStringString() {
		Message.instance.in("a", "a1");
		Message.instance.in("a", "a2");

		Message.instance.in("b", "b1");
		Message.instance.in("b", "b2");
	}

	@Test
	public void testOutInt() {
		DebugUtil.print(Message.instance.out(10));
		DebugUtil.print(Message.instance.out(10));
	}

	@Test
	public void testOutStringInt() {
		DebugUtil.print(Message.instance.out("a", 10));
		DebugUtil.print(Message.instance.out("a", 10));

		DebugUtil.print(Message.instance.out("b", 10));
		DebugUtil.print(Message.instance.out("b", 10));
	}

}
