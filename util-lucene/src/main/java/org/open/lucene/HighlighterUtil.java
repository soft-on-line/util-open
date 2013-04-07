package org.open.lucene;

import java.io.StringReader;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.NullFragmenter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.Version;
import org.open.util.DateUtil;

/**
 * @author ibm
 * @version $Id:$
 */
public class HighlighterUtil {

    private static final Log    log        = LogFactory.getLog(HighlighterUtil.class);
    private static final String FIELD_NAME = "CONTENT";

    // public static String wrap(String keyword, String content, int fragmentSize, int maxNumFragmentsRequired,
    // Analyzer analyzer, Formatter formatter) {
    // return wrap(keyword, content, fragmentSize, maxNumFragmentsRequired, "...", analyzer, formatter);
    // }

    // public static String wrap(String keyword, String content, int maxNumFragmentsRequired, Analyzer analyzer,
    // Formatter formatter) {
    // return wrap(keyword, content, null, maxNumFragmentsRequired, "...", analyzer, formatter);
    // }

    public static String wrap(String keyword, String content) {
        return wrap(keyword, content, null, 3, "...", new ChineseAnalyzer(),
                    new SimpleHTMLFormatter("<font color='red'>", "</font>"));
    }

    public static String wrap(String keyword, String content, Integer fragmentSize, int maxNumFragmentsRequired) {
        return wrap(keyword, content, fragmentSize, maxNumFragmentsRequired, "...", new ChineseAnalyzer(),
                    new SimpleHTMLFormatter("<font color='red'>", "</font>"));
    }

    @SuppressWarnings("resource")
    public static String wrap(String keyword, String content, Integer fragmentSize, int maxNumFragmentsRequired,
                              String fragmentSeparator, Analyzer analyzer, Formatter formatter) {
        long st = new Date().getTime();
        try {
            // init start
            analyzer = (null == analyzer) ? new StandardAnalyzer(Version.LUCENE_30) : analyzer;
            // init end

            MemoryIndex index = new MemoryIndex();
            index.addField(FIELD_NAME, content, analyzer);

            QueryParser parser = new QueryParser(Version.LUCENE_30, FIELD_NAME, analyzer);
            Query query = parser.parse(keyword);

            IndexSearcher searcher = index.createSearcher();
            IndexReader reader = searcher.getIndexReader();

            query = query.rewrite(reader);

            Highlighter highlighter = (null == formatter) ? new Highlighter(new QueryScorer(query)) : new Highlighter(
                                                                                                                      formatter,
                                                                                                                      new QueryScorer(
                                                                                                                                      query));
            if (fragmentSize == null) {
                highlighter.setTextFragmenter(new NullFragmenter());
            } else {
                highlighter.setTextFragmenter(new SimpleFragmenter(fragmentSize));
            }

            TokenStream tokenStream = analyzer.tokenStream(FIELD_NAME, new StringReader(content));

            return highlighter.getBestFragments(tokenStream, content, maxNumFragmentsRequired, fragmentSeparator);

        } catch (Exception e) {
            log.error("wrap(...) error!", e);
            return content;
        } finally {
            log.debug("Used Time:" + DateUtil.convert(new Date().getTime() - st));
        }
    }
}
