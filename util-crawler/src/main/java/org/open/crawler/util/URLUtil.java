package org.open.crawler.util;

import java.net.URLDecoder;

import org.archive.net.UURI;
import org.archive.net.UURIFactory;
import org.open.util.RegexpUtil;


/**
 * 爬虫组建url地址的工具包类
 * 引用开源项目 heritrix,在此感谢Heritrix开发组成员及其组织
 * @see Heritrix
 * @author 覃芝鹏
 * @version $Id: URLUtil.java,v 1.1 2009/06/09 12:44:29 ibm Exp $
 */
public class URLUtil
{
	/**
	 * @see #canonicalURL(String, String)
	 */
	public static String[] canonicalURL(String original,String[] absent) 
		throws Exception
	{
		for(int i=0;i<absent.length;i++)
		{
			if(!isTrue(absent[i]))
			{
				absent[i] = canonicalURL(original,absent[i]);
			}
		}
		
		return absent;
	}
	
	/**
	 * 整理不完整的url地址,包括以"../../"或"./"或"/"等开头的url地址.
	 * @param original 原始的,完整的url地址.
	 * @param absent 需要整理的的url地址.
	 * @return absent规范后的地址
	 * @throws URIException 
	 * @throws UnsupportedEncodingException 
	 */
	public static String canonicalURL(String original,String absent) 
		throws Exception
	{
		//避免apache heritrix组装url地址中出现%40之类关键字会出错的bug。
		original = original.replaceAll("\\%", "\\$");
		absent = absent.replaceAll("\\%", "\\$");
		
		UURI base = UURIFactory.getInstance(original);
		UURI uuri = UURIFactory.getInstance(base, URLDecoder.decode(absent,"gbk"));
		
		//返回地址时候还原带有%之类的关键字
		return uuri.getURI().replaceAll("\\$", "\\%");
	}
	
	/**
	 * 替换原始url地址中的'&amp;'等等.
	 * @param original 原始url地址
	 * @return 处理后的url地址
	 */
	public static String filterHtmlTag(String original)
	{
		if(original == null){
			return original;
		}
		
		return original.replaceAll("&amp;","&");
	}
	
	private static final String URL_REGEXP = "^((https|http|ftp|rtsp|mms)?://)"
		+ "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"//ftp的user@ 
		+ "(([0-9]{1,3}\\.){3}[0-9]{1,3}"// IP形式的URL- 221.2.162.15
		+ "|"// 允许IP和DOMAIN（域名）
		+ "([0-9a-z_!~*'()-]+\\.)*"// 域名- www. 
		+ "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\."// 二级域名 
		+ "[a-z]{2,6})"// first level domain- .com or .museum 
		+ "(:[0-9]{1,4})?"// 端口- :80 
		+ "((/?)|"// a slash isn't required if there is no file name 
		+ "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
	
	/**
	 * 验证是否争取的url地址
	 * @param url url地址串
	 * @return true正确false错误
	 */
	public static boolean isTrue(String url)
	{
		return RegexpUtil.isMatch(url, URL_REGEXP);
	}
	
}
