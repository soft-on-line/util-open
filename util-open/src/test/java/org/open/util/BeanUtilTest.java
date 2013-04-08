package org.open.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.open.util.debug.DebugUtil;

public class BeanUtilTest {

    @Test
    public void testGetAttribute() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<InnerObject> listInnerObject = new ArrayList<InnerObject>();
        for (int i = 0; i < 10; i++) {
            listInnerObject.add(new InnerObject());
        }

        DebugUtil.print(new InnerObject().getTestString());
        DebugUtil.print(BeanUtils.getNestedProperty(new InnerObject(), "testString"));
//        DebugUtil.print(BeanUtils.getIndexedProperty(new InnerObject(), "testString"));
        DebugUtil.print(BeanUtils.getSimpleProperty(new InnerObject(), "testString"));
//        DebugUtil.print(DataUtil.join(listInnerObject,"id",","));
//        DebugUtil.print(BeanUtils.getProperty(new InnerObject(), "id"));
        DebugUtil.print(BeanUtils.getProperty(new InnerObject(), "testString"));

//         DebugUtil.print("getArrayProperty=>"+BeanUtils.getArrayProperty(listInnerObject, "testString"));
//        DebugUtil.print(BeanUtil.getProperty(listInnerObject, "testString"));
    }

}
