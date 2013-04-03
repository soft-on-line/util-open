package org.open.util;

/**
 * Integer工具包类
 * @author moon
 * @version $Id: IntegerUtil.java,v 1.1 2009/03/13 03:18:24 moon Exp $
 */
public class IntegerUtil 
{
	/**
	 * @param a 原始整型
	 * @param length 产生的字符串长度
	 * @return length长度的整型字符串，不够长度前端补0
	 */
	public static String toString(int a,int length)
	{
		String str = String.valueOf(a);
		
		return StringUtil.build("0",length-str.length())+str;
	}
}
