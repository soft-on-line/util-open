package org.open.crawler.html;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.open.crawler.httpclient.Browser;
import org.open.util.CodeUtil;
import org.open.util.WriterUtil;
import org.open.util.debug.DebugUtil;

public class NewsParserTest extends TestCase {

    private static final Log log = LogFactory.getLog(NewsParserTest.class);
    private DebugUtil        du  = new DebugUtil(DebugUtil.InstanceModel.ConsoleModel);

    // public void testParseImg() {
    // Browser browser = new Browser();
    // try {
    // browser.getConnection("http://www.cenn.cn/News/2008-7/82424_2008711154656.shtml");
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // String html = browser.getHTML();
    // NewsParser parser = new NewsParser();
    // parser.parseHtml(html);
    // log.info("ok");
    // }

    public void _testParseNews() {
        List<String> list = new ArrayList();
        // list.add("http://finance.eastmoney.com/100127,1292947.html");
        // list.add("http://www.shaoxing.com.cn/news/content/2010-04/01/content_479485.htm");
        list.add("http://gongyi.qq.com/a/20100430/000037.htm");
        list.add("http://hi.cenn.cn/info/shownews.asp?newsid=56980");
        list.add("http://news.szchehang.cn/html/qcwh/2010042241527572505.html");
        list.add("http://www.cenn.cn/News/2008-7/82424_2008711154656.shtml");
        for (String url : list) {
            Browser browser = new Browser();
            try {
                browser.getConnection(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // browser.getHTML();
            String html = browser.getHTML();
            // log.info(browser.getCharSet());
            log.info(HtmlNewsUtil.getNews(browser.getHTML(), url));
        }
        // log.info(HtmlNewsUtil.getNews(html));
    }

    public void test() throws Exception {
        // String url = "http://iask.sina.com.cn/b/6513062.html";
        String url = "http://gongyi.qq.com/a/20100430/000037.htm";

        Browser browser = new Browser();
        browser.getConnection(url);
        String html = browser.getHTML();
        
        String[] data = HtmlNewsUtil.getNews(html, url);
        StringBuffer buf = new StringBuffer();
        for (String str : data) {
            buf.append(str).append("<br/>");
        }
        du.print(buf.toString());
        // WriterUtil.writeFile("d:/html_content_parse.html", buf.toString());
    }

}
