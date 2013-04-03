package org.open.util;

import java.util.Date;

import org.junit.Test;

public class DateUtilTest {

//    @Test
    public void testGetDateIntIntInt() {

        Date d1 = DateUtil.getDate("2011-01-14 01:00:00");
        Date d2 = DateUtil.getDate("2011-01-14 02:00:00");

        System.out.println(d2.getTime() - d1.getTime());
        System.out.println(1 * 60 * 60 * 1000);
    }

//    @Test
    public void testGetDateString() {
        System.out.println(DateUtil.getDefault(DateUtil.subtract(DateUtil.getDate("2011-01-14 01:00:00"),
                                                                 DateUtil.LONG_HOUR * 2)));

        System.out.println(DateUtil.getDefault(DateUtil.subtract(new Date(), DateUtil.LONG_DAY * 2)));
    }

    @Test
    public void testDayOfMonth(){
        System.out.println(DateUtil.getFirstDayOfWeek());
//        System.out.println(DateUtil.getFirstDayOfMonth());
    }

}
