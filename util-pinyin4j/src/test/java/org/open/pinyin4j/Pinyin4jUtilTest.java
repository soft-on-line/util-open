package org.open.pinyin4j;

import java.io.IOException;

import org.junit.Test;
import org.open.util.ReaderUtil;
import org.open.util.StringUtil;
import org.open.util.WriterUtil;

import junit.framework.TestCase;

public class Pinyin4jUtilTest extends TestCase {

    // @Test
    public void testGetFirstPinyinChar() {
        System.out.println(Pinyin4jUtil.getFirstPinyin(' '));
        System.out.println(Pinyin4jUtil.getFirstPinyin('中'));
        System.out.println(Pinyin4jUtil.getFirstPinyin((Character) null));
    }

    // @Test
    public void testGetFirstPinyinCharArray() {
        System.out.println(Pinyin4jUtil.getFirstPinyin("中国".toCharArray()));
        System.out.println(Pinyin4jUtil.getFirstPinyin("".toCharArray()));
        System.out.println("3->" + Pinyin4jUtil.getFirstPinyin("LAZYBATS".toCharArray()));
        System.out.println("4->" + Pinyin4jUtil.getFirstPinyin((Character) 'z'));
    }

    // @Test
    public void testGetPinyin() {
        System.out.println(Pinyin4jUtil.getPinyin(""));
        System.out.println(Pinyin4jUtil.getPinyin(null));
        System.out.println(Pinyin4jUtil.getPinyin("中国"));
    }

    @Test
    public void test() throws IOException {
        String data = ReaderUtil.read("d:/人名.txt", "gbk");
        String[] name = data.split("\\s");
        StringBuffer buf = new StringBuffer();
        for (String str : name) {
            StringBuilder line = new StringBuilder();
            if (StringUtil.isEmpty(str)) {
                continue;
            }
            // System.out.println(str);
            char[] dd = str.toCharArray();
            String c1 = "";
            for (int i = 0; i < dd.length; i++) {
                line.append(dd[i]);
                if (0 == i) {
//                    line.append(" ");
                    if (line.length() < 5) {
                        line.append(StringUtil.build(" ", 5 - line.length()));
                    }
                    c1 = Pinyin4jUtil.getPinyin(String.valueOf(dd[i]));
                } else {
                    c1 += Pinyin4jUtil.getFirstPinyin((dd[i])).toString();
                }
            }
//            System.out.println(line.length());
            if (line.length() < 15) {
                line.append(StringUtil.build(" ", 15 - line.length()));
            }
            line.append(Pinyin4jUtil.getPinyin(str));

            if (line.length() < 40) {
                line.append(StringUtil.build(" ", 40 - line.length()));
            }
            line.append(c1);

            buf.append(line).append("\r\n");
        }

        System.out.println(buf.toString());
        WriterUtil.writeFile("d:/out.txt", buf.toString());
    }
}
