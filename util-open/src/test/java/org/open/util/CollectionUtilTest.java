package org.open.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CollectionUtilTest {

    @Test
    public void testExchange() {
        // fail("Not yet implemented");
    }

    @Test
    public void testRanking() {
        // fail("Not yet implemented");
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);

        System.out.println(list);
        CollectionUtil.ranking(list, Integer.valueOf(3), -1);
        System.out.println(list);
    }

}
