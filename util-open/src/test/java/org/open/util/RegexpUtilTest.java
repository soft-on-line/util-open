package org.open.util;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.open.util.debug.DebugUtil;

public class RegexpUtilTest extends TestCase {

    private static final Log log = LogFactory.getLog(RegexpUtilTest.class);
    DebugUtil                du  = new DebugUtil(DebugUtil.InstanceModel.ConsoleModel);

    public void testMatchGroup() {
        du.print(RegexpUtil.matchGroup("abc123cde456", "(\\d+)"));
    }

    public void testMatchGroups() {
        du.print(RegexpUtil.matchGroups("abc123cde456", "(\\d*)"));
    }

    public void testMatchMultiGroup() {
        du.print(RegexpUtil.matchMultiGroup("abc123cde456", "(\\w*)(\\d*)"));
    }

    public void testMatchMultiGroups() {
        du.print(RegexpUtil.matchMultiGroups("abc123cde456", "(\\w*)(\\d*)"));
    }

    public void testIsMatch() {
        System.out.println(RegexpUtil.isMatch("9999", "^-?\\d+\\.?\\d*$"));
        System.out.println(RegexpUtil.isMatch("999999999999999999999999", "^-?\\d+\\.?\\d*$"));
        System.out.println(RegexpUtil.isMatch("9999.9", "^-?\\d+\\.?\\d*$"));
        System.out.println(RegexpUtil.isMatch("9999.99999999999999", "^-?\\d+\\.?\\d*$"));
        System.out.println(RegexpUtil.isMatch("9999.", "^-?\\d+\\.?\\d*$"));
        System.out.println(RegexpUtil.isMatch("999999999999.", "^-?\\d+\\.?\\d*$"));
        System.out.println(RegexpUtil.isMatch("-999999999999.", "^-?\\d+\\.?\\d*$"));
        System.out.println(RegexpUtil.isMatch("", "^-?\\d+\\.?\\d*$"));
        System.out.println(RegexpUtil.isMatch("9999.g", "^-?\\d+\\.?\\d*$"));
        System.out.println(RegexpUtil.isMatch("g9999.g", "^-?\\d+\\.?\\d*$"));

        System.out.println("1=>" + RegexpUtil.isMatch("A1AZ", "^([0-9]|[a-z]|[A-Z])+$"));
    }

    @Test
    public void testPaipai() {
        String regexp1 = "http://bbs\\.paipai\\.com/thread-(\\d*?)-1-1\\.html";

        String url = "http://bbs.paipai.com/thread-1687026-1-1.html?PTAG=32272.2.13";
        // http://bbs.paipai.com/forum.php?mod=post&action=reply&fid=8&tid=1763635
        System.out.println(RegexpUtil.matchGroup(url, regexp1));

        url = "http://bbs.paipai.com/thread-1763635-1-1.html?PTAG=32272.2.13";

        String regexp2 = "http://bbs\\.paipai\\.com/forum\\.php\\?mod=viewthread&tid=(\\d*?)\\?";
        url = "http://bbs.paipai.com/forum.php?mod=viewthread&tid=1734019?PTAG=32272.2.5";
        // http://bbs.paipai.com/forum.php?mod=post&action=reply&fid=65&tid=1734019
        System.out.println(RegexpUtil.matchGroup(url, regexp2));

        url = "http://bbs.paipai.com/forum.php?mod=viewthread&tid=1626314?PTAG=32272.2.4";

        System.out.println(url);
    }

    public static void main(String[] args) {
        String str = ReaderUtil.read("c:/11.txt", "gbk");
        String[] dd = RegexpUtil.matchGroups(str, "<a.*?>(.+?)</a>");
        int count = 0;
        for (String d : dd) {
            // System.out.println((++count));
            // System.out.println(d);
            System.out.println("map.put(\"" + (++count) + "\", \"" + d + "\");");
        }
    }

}
