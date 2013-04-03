package org.open.util;

import org.junit.Test;

public class StringUtilTest {

//    @Test
    public void test_substringBefore() {
        System.out.println(StringUtil.substringBefore("123456", -1));
        System.out.println(StringUtil.substringBefore("123456", 3));
        System.out.println(StringUtil.substringBefore("123", 3));
        System.out.println(StringUtil.substringBefore("", 3));
        System.out.println(StringUtil.substringBefore(null, 3));
    }

//    @Test
    public void test_substringAfter() {
        System.out.println(StringUtil.substringAfter("123456", -1));
        System.out.println(StringUtil.substringAfter("123456", 3));
        System.out.println(StringUtil.substringAfter("123", 3));
        System.out.println(StringUtil.substringAfter("", 3));
        System.out.println(StringUtil.substringAfter(null, 3));
    }

    @Test
    public void test_convert(){
        System.out.println(StringUtil.convert("1,33,32,5"));
        System.out.println(StringUtil.convert("1,33,32,5",",",String.class));
    }

}
