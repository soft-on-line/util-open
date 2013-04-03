package org.open.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.util.Version;

import junit.framework.TestCase;

public class HighlighterUtilTest extends TestCase {

    public void testWrapStringString() {
        // String keyword = "Lux";
        String keyword = "公司 成立";
        String content = "1901年 Lux有限公司在斯德哥尔摩成立，生产Lux牌灯，这是一种用于户外的煤油灯。此灯后来使用于世界各地的灯塔上。"
                         + "1910年 Elektromekaniska有限公司在斯德哥尔摩成立。" + "1912年 生产出第一台吸尘器，开始在瑞典销售。 "
                         + "1919年 Elektromekaniska有限公司与Lux有限公司合并后，成立伊莱克斯(Electrolux)公司。 "
                         + "1925年 伊莱克斯购买了Arctic冰箱公司，生产第一台吸收式冰箱，当时称为“D冰箱”。许多人发现“这个奇妙的箱子”解决了他们食物保鲜的难题。"
                         + "1926年 国外第一家生产真空吸尘器的工厂在柏林成立。 ";

        int fragmentSize = 20;
        int maxNumFragmentsRequired = 3;
        String fragmentSeparator = "...";
        // Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
        Analyzer analyzer = new ChineseAnalyzer();
        Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");

        System.out.println(HighlighterUtil.wrap(keyword, content));
        System.out.println(HighlighterUtil.wrap(keyword, content, null, maxNumFragmentsRequired));
        System.out.println(HighlighterUtil.wrap(keyword, content, fragmentSize, maxNumFragmentsRequired,
                                                fragmentSeparator, analyzer, formatter));
    }
}
