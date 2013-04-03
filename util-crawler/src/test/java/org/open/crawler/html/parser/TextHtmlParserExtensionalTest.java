package org.open.crawler.html.parser;

import static org.junit.Assert.*;

import org.junit.Test;
import org.open.util.ReaderUtil;

public class TextHtmlParserExtensionalTest {

    @Test
    public void testExtendFirstTextNodesStyleForOdd_Even() {
        String html = ReaderUtil.read("d:/in.html","gbk");

        TextHtmlParserExtensional test = new TextHtmlParserExtensional(html);

        String oddStyle = "background: #abc2e0;";
        String evenStyle = "background: #CCCCCC;";

//        System.out.println(test.extendFirstTextNodesStyleForOdd_Even(oddStyle, evenStyle));
        System.out.println(test.extendFirstTextNodesStyleClassNameForOdd_Even(oddStyle, evenStyle));
    }

}
