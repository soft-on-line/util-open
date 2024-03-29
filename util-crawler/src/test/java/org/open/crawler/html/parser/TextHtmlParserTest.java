package org.open.crawler.html.parser;

import org.junit.Test;
import org.open.crawler.httpclient.Browser;
import org.open.util.debug.DebugUtil;

public class TextHtmlParserTest {

    @Test
    public void testGetDenseTextNodes() throws Exception {
        // String url = "http://iask.sina.com.cn/b/6513062.html";
        // String url = "http://wenda.tianya.cn/wenda/thread?tid=4bc89e3da219571c";
        // String url = "http://gongyi.qq.com/a/20100430/000037.htm";
        // String url = "http://wenda.tianya.cn/wenda/thread?tid=28a4cc01a697a0d8";
        // String url = "http://zhidao.baiDebugUtil.com/question/32262114.html";
        // String url = "http://zhidao.baiDebugUtil.com/question/92134423.html";
//         String url = "http://zhidao.baiDebugUtil.com/question/118668001.html";
//         String url = "http://zhidao.baiDebugUtil.com/question/73686809.html";
//         String url = "http://zhidao.baiDebugUtil.com/question/91270468.html";
//        String url = "http://zhidao.baiDebugUtil.com/question/59729209.html";
//        String url = "http://zhidao.baiDebugUtil.com/question/6237994.html";//???
        String url = "http://aocrealtek2649-xml.wasu.tv/xml228/nodeXml/228.xml";//???

        Browser browser = new Browser();
        browser.getConnection(url);
        String html = browser.getHTML();

        // String html = ReaderUtil.read("d:/input.html","gb18030");
        // String html = ReaderUtil.read("d:/wenda_sina.html","gb18030");
        // String html = ReaderUtil.read("d:/ttt.txt","gbk");

        // DebugUtil.print(html);

        TextHtmlParser generalHtmlParser = new TextHtmlParser(null, html);
        // Node[] nodes = htmlParser.getDenseTextNodes();
        // for (Node node : nodes) {
        // // DebugUtil.print("NodeName:" + node.getNodeName());
        // // DebugUtil.print("NodeValue:" + node.getNodeValue());
        // // DebugUtil.print("NodeContent:" + node.getTextContent().trim());
        // // DebugUtil.print(node.getOwnerDocument().toString());
        // }

        String coreHtml = generalHtmlParser.getDensityTextNodesHtml();

        DebugUtil.print("CoreTextNode:\r\n" + coreHtml);

        DebugUtil.print("HighLight:\r\n"
                 + new TextHtmlParserExtensional(coreHtml).extendFirstTextNodesStyleForOdd_Even("background: #abc2e0;",
                                                                                                "background: #eeeeee;"));
    }

}
