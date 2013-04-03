package org.open.crawler.httpclient;

import org.junit.Test;



public class BrowserProxyTest {

    @Test
    public void test() throws Exception{

        BrowserProxy browser = new BrowserProxy();
        browser.setProxy("127.0.0.1", 80);

        browser.getConnection("http://www.baidu.com");

        System.out.println(browser.getHTML());
    }
}
