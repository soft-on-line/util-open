package org.open.crawler.httpclient;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;
import org.open.util.CodeUtil;
import org.open.util.ReaderUtil;
import org.open.util.RegexpUtil;
import org.open.util.WriterUtil;

public class BrowserTest extends TestCase {

    private static Browser browser = new Browser();

    private static void _testName(String name, StringBuffer result) throws Exception {
        String urlName = CodeUtil.URLEncoder(name);
        // String url =
        // "http://www.google.com.hk/search?hl=zh-CN&newwindow=1&safe=strict&client=aff-1616dh&affdom=1616dh.com&channel=footer&q="
        // + urlName + "&oq=" + urlName + "&aq=f&aqi=&aql=&gs_sm=e&gs_upl=26669l27054l0l3l3l0l0l0l0l0l0ll0";

        String url = "http://www.baidu.com/baidu?word=" + urlName + "&tn=myie2&ch=3";

        browser.getConnection(url);

        int count = RegexpUtil.matchGroups(browser.getHTML(), "(" + name + ".{0,10}公司)").length;
        System.out.println(count);

        result.append(name).append("=>").append(count).append("\r\n");

        Thread.sleep(5 * 1000);
    }

    private static String[] getNames() throws IOException {
        String content = ReaderUtil.read("c:/name.txt", "gbk");
        return RegexpUtil.matchGroups(content, "'(.*?)'");
    }

    // @Test
    public static void testTestName() throws Exception {
        _testName("杭州结构", new StringBuffer());
        // _testName("杭州恒生", new StringBuffer());
    }

    // @Test
    public static void testName() throws IOException {
        StringBuffer result = new StringBuffer();
        String[] name = getNames();
        for (int i = 0; i < name.length; i++) {
            try {
                System.out.println(name[i]);
                _testName("杭州" + name[i], result);
            } catch (Exception e) {
                e.printStackTrace();

                result.append(name[i]).append(" exception!");
            }
        }
        WriterUtil.writeFile("c:/result.txt", result.toString());
    }

    // @Test
    public void test() throws Exception {
        String url = "http://img.ifeng.com/tres/auto/9/2010/0627/109_4916108228_20100627111203.jpg";

        Browser browser = new Browser();
        browser.getConnection(url);

        WriterUtil.write("d:/img.jpg", browser.getStream());
    }

    @Test
    public void testPaipai() throws Exception {
        String url = "http://bbs.paipai.com/portal.php";

        browser.getConnection(url);
        String html = browser.getHTML();
//        browser.postConnection("http://bbs.paipai.com/forum.php?mod=post&action=reply&fid=13&tid=1656875&referer=http%3A//bbs.paipai.com/thread-1656875-1-1.html%3FPTAG%3D32272.2.13", httphead);


        System.out.println(html);
    }

    public static void main(String[] args) throws Exception {
        // testTestName();
        testName();
    }

}
