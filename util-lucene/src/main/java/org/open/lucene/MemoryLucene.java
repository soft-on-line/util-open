package org.open.lucene;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;

/**
 * Lucene内存式 全文索引类
 * 
 * @author 覃芝鹏
 * @version $Id: MemoryLucene.java,v 1.1 2008/04/08 06:00:34 moon Exp $
 */
public class MemoryLucene {

    // 记录日志
    private static final Log log                 = LogFactory.getLog(MemoryLucene.class);

    /**
     * 定义Lucene全文索引路径
     */
    private static String    DEFAULT_FIELD_LABEL = "content";

    /**
     * lucene分析器 默认中文分析器
     * 
     * @see org.apache.lucene.analysis.Analyzer
     */
    private static Analyzer  analyzer            = new ChineseAnalyzer();

    /**
     * @see org.apache.lucene.index.memory.MemoryIndex
     */
    private MemoryIndex      index               = new MemoryIndex();

    /**
     * @see org.apache.lucene.queryParser.QueryParser
     */
    private QueryParser      parser              = new QueryParser(Version.LUCENE_30, DEFAULT_FIELD_LABEL, analyzer);

    /**
     * 构造函数 初始化一次全文索引文本内容 然后函数可以多次索引
     * 
     * @param content 需要索引的文本内容
     */
    public MemoryLucene(String content) {
        index.addField(DEFAULT_FIELD_LABEL, content, analyzer);
    }

    /**
     * 从指定的构造函数初始化字符串中查找关键字(keyword);
     * 
     * @param keyword 索引的关键字
     * @return 返回查询匹配度值 {@link org.apache.lucene.index.memory.MemoryIndex#search(org.apache.lucene.search.Query)}
     */
    public float search(String keyword) {
        try {
            return index.search(parser.parse(keyword));
        } catch (Exception e) {
            log.error("LuceneUtil match(keyword,content) error!", e);
            return -1;
        }
    }

    /**
     * 从指定的构造函数初始化字符串中查找关键字(keyword);
     * 
     * @param keyword 索引的关键字
     * @return 查找到返回true 否则返回false
     */
    public boolean isMatch(String keyword) {
        return (search(keyword) > 0);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        //
    }

}
