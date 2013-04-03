package org.open.lucene;

import java.io.Serializable;
import java.util.Properties;

import org.open.util.StringUtil;

public class Bean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7988564472365614305L;

	/**
	 * 当前索引的 关键字
	 */
	private String keyword;
	
	/**
	 * 当前bean的权重值
	 */
	private float weight;
	
	/**
	 * 存储所有字段 库
	 */
	private Properties lib = new Properties();
	
	/**
	 * 压入一个字段类型 库
	 * @param field 字段
	 */
	public void put(Fields field,String value)
	{
		if(value!=null){
			lib.put(field.getLabel(), value);
		}
	}
	
	/**
	 * 取得一个字段类型库
	 * @param field 字段
	 * @return 带数据 的字段
	 */
	public String get(Fields field)
	{
		return (String)lib.get(field.getLabel());
	}
	
	/**
	 * 按搜索的关键字 三分法 高亮显示定位字段
	 * @param field 需要高亮定位的字段
	 * @return 三分后的字符串
	 */
	public String fragment(Fields field)
	{
		return StringUtil.fragment((String)lib.get(field.getLabel()),keyword,3,200);
	}
	
	public void setKeyword(String keyword){	this.keyword = keyword;	}
	public String getKeyword(){	return this.keyword;	}
	
	public void setWeight(Float weight){	this.weight = weight;	}
	public Float getWeight(){	return this.weight;	}

}
