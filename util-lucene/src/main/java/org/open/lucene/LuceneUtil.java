package org.open.lucene;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;

/**
 * 自定义Lucene全文索引工具包
 * @author 覃芝鹏
 * @version $Id: LuceneUtil.java,v 1.1 2008/04/08 06:00:34 moon Exp $
 */
public class LuceneUtil 
{
	//记录日志
	private static final Log log = LogFactory.getLog(LuceneUtil.class);
	
	/**
	 * lucene分析器 默认中文分析器
	 * @see org.apache.lucene.analysis.Analyzer
	 */
	private static Analyzer analyzer = new ChineseAnalyzer();
	
	/**
	 * 从指定的字符串(content)中查找关键字(keyword);
	 * @param keyword 需要查找关键字
	 * @param content 给定查找内容
	 * @return 返回查询匹配度值 {@link org.apache.lucene.index.memory.MemoryIndex#search(org.apache.lucene.search.Query)}
	 * @throws ParseException
	 * @see org.apache.lucene.index.memory.MemoryIndex
	 */
	public static float match(String content,String keyword)
	{
		try {
			MemoryIndex index = new MemoryIndex();
			
			index.addField("content", content, analyzer);
			
			QueryParser parser = new QueryParser(Version.LUCENE_30,"content", analyzer);
			
			return index.search(parser.parse(keyword));
		} catch(Exception e) {
			log.error("LuceneUtil match(keyword,content) error!", e);
			return -1;
		}
	}
	
	/**
	 * 从指定的字符串(content)中查找关键字(keyword);
	 * @param keyword 需要查找关键字
	 * @param content 给定查找内容
	 * @return 查找到返回true 否则返回false
	 * @throws ParseException
	 * @see org.apache.lucene.index.memory.MemoryIndex
	 */
	public static boolean isMatch(String content,String keyword)
	{
		return (match(content,keyword) > 0);
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception 
	{
		String content = "中华人民共和国";
		String keyword = "中和国";
		
		System.out.println(LuceneUtil.isMatch(keyword, content));
	}

}
