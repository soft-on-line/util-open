package org.open.crawler.htmlentities;

import com.tecnick.htmlutils.htmlentities.HTMLEntities;

public class HTMLEntitiesUtil {

	
	/**
	 * 将特殊字符转换成网页字符
	 * @param html
	 * @return
	 */
	public static String htmlentities(String html)
	{
		return HTMLEntities.htmlentities(html);
	}

	/**
	 * 将网页字符（如：&bnsp;）转换回特殊字符(" ")
	 * @param html
	 * @return
	 */
	public static String unhtmlentities(String html)
	{
		return HTMLEntities.unhtmlentities(html);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
