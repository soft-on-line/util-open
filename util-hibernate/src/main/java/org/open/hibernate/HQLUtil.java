package org.open.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 程序调试 辅助类(调试Hibernate HQL)
 * @author 覃芝鹏
 * @version $Id: HQLUtil.java,v 1.2 2008/08/08 02:33:58 moon Exp $
 */
public class HQLUtil 
{
	private static final Log log = LogFactory.getLog(HQLUtil.class);
	
	/**
	 * 调试hibernate hql
	 * @param hql hibernate HQL
	 * @param values 传入的对象数组
	 * @return 组装后的hql
	 */
	public static String HQL(String hql,Object[] values)
	{
		for(int i=0;i<values.length;i++)
		{
			hql = HQL(hql,values[i]);
		}
		
		return hql;
	}
	
	/**
	 * 打印 对象是否为空
	 * @param obj 原始对象
	 */
	public static void isNull(Object obj)
	{
		log.debug("NULL status:"+obj==null);
	}
	
	/**
	 * 调试hibernate hql
	 * @param hql hibernate HQL
	 * @param value 传入的对象
	 * @return 组装后的hql
	 */
	public static String HQL(String hql,Object value)
	{
		if(value == null){
			return hql.replaceFirst("\\?","Null");
		}else{
			return hql.replaceFirst("\\?",value.toString());
		}
	}
	
}
