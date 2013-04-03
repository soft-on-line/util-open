package org.open.util;

import java.io.File;

import org.junit.Test;

public class FileUtilTest {

    @Test
    public void test_1() {
        String path = "D:/workspace/eclipse/portalshop/trunk/moon/src/main/resources/org/open/portalshop/moon/biz/dao/ibatis/sqlmap";

        File file = new File(path);
        File sqlMap[] = file.listFiles();
        for (File each : sqlMap) {
            System.out.println("<sqlMap resource=\"org\\open\\portalshop\\moon\\biz\\dao\\ibatis\\sqlmap\\"+FileUtil.getName(each)+"\" />");
        }

    }

}
