package org.open.crawler.nekohtml;

import static org.junit.Assert.*;

import org.junit.Test;
import org.open.crawler.httpclient.Browser;
import org.open.util.ReaderUtil;

public class NekoHtmlUtilTest {

    @Test
    public void testGetDOMParser() throws Exception {
        // String url = "http://iask.sina.com.cn/b/6513062.html";
        //
        // Browser browser = new Browser();
        // browser.getConnection(url);
        // String html = browser.getHTML();
        String html = ReaderUtil.read("d:/input.html");

//        NekoHtmlUtil.getDOMParser(html);
    }

}
