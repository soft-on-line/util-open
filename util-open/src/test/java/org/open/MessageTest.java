package org.open;

import static org.junit.Assert.*;

import org.junit.Test;
import org.open.util.debug.DebugUtil;

public class MessageTest {

    DebugUtil du = new DebugUtil(DebugUtil.InstanceModel.ConsoleModel);

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
        du.print(Message.instance.out(10));
        du.print(Message.instance.out(10));
    }

    @Test
    public void testOutStringInt() {
        du.print(Message.instance.out("a", 10));
        du.print(Message.instance.out("a", 10));

        du.print(Message.instance.out("b", 10));
        du.print(Message.instance.out("b", 10));
    }

}
