package org.open.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.open.util.debug.DebugUtil;

public class BeanUtilTest {

    private static DebugUtil du = new DebugUtil(DebugUtil.InstanceModel.ConsoleModel);

    @Test
    public void testGetAttribute() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<InnerObject> listInnerObject = new ArrayList<InnerObject>();
        for (int i = 0; i < 10; i++) {
            listInnerObject.add(new InnerObject());
        }

        du.print(new InnerObject().getTestString());
        du.print(BeanUtils.getNestedProperty(new InnerObject(), "testString"));
//        du.print(BeanUtils.getIndexedProperty(new InnerObject(), "testString"));
        du.print(BeanUtils.getSimpleProperty(new InnerObject(), "testString"));
//        du.print(DataUtil.join(listInnerObject,"id",","));
//        du.print(BeanUtils.getProperty(new InnerObject(), "id"));
        du.print(BeanUtils.getProperty(new InnerObject(), "testString"));

//         du.print("getArrayProperty=>"+BeanUtils.getArrayProperty(listInnerObject, "testString"));
        du.print(BeanUtil.getProperty(listInnerObject, "testString"));
    }

}
