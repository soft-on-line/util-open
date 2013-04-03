package org.open.lang;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumberOperatorTest {

    @Test
    public void testAddNumber() {
        Number a = 1;

        System.out.println(a.getClass());

        Number b = 0.5;

        System.out.println(NumberOperator.add(a, b));
    }

}
