package org.open.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class RandomUtilTest {

    @Test
    public void testNextIntIntInt() {
        int i = 20;
        while ((i--) > 0) {
            System.out.print(RandomUtil.nextInt(2395202, 1) + ",");
        }
    }

}
