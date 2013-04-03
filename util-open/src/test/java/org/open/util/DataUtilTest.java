package org.open.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import junit.framework.TestCase;

public class DataUtilTest extends TestCase {

    public static class Data {

        private Integer id;
        private String  str;

        public Data(Integer id, String str) {
            this.id = id;
            this.str = str;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }
    }

    public void testJoinListOfObjectStringString() throws IllegalAccessException, InvocationTargetException,
                                                  NoSuchMethodException {
//        List<Object> listData = new ArrayList<Object>();
        List<Data> listData = new ArrayList<Data>();
        for (int i = 0; i < 10; i++) {
            listData.add(new Data(i, "str_" + i));
        }

        System.out.println(DataUtil.join(listData, "id", ","));
        System.out.println(DataUtil.join(listData, "str", "','"));
        System.out.println(BeanUtil.getProperty(listData,"id"));
        System.out.println("BeanUtil=>"+BeanUtil.getProperty(listData,"id"));
    }

    @Test
    public static void testGetAttribute() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<InnerObject> listInnerObject = new ArrayList<InnerObject>();
        for (int i = 0; i < 10; i++) {
            listInnerObject.add(new InnerObject());
        }

        System.out.println(new InnerObject().getTestString());
//        System.out.println(BeanUtils.getNestedProperty(new InnerObject(), "testString"));
//        System.out.println(BeanUtils.getIndexedProperty(new InnerObject(), "testString"));
//        System.out.println(BeanUtils.getSimpleProperty(new InnerObject(), "testString"));
        System.out.println("DataUtil=>"+DataUtil.join(listInnerObject,"id",","));
        System.out.println(BeanUtils.getProperty(new InnerObject(), "id"));
        System.out.println(BeanUtils.getProperty(new InnerObject(), "testString"));

        // System.out.println(BeanUtil.getAttribute(listInnerObject, "testInt"));
        System.out.println(BeanUtil.getProperty(listInnerObject, "testString"));
    }
}
