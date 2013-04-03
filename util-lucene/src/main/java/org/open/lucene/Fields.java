package org.open.lucene;

import java.io.Serializable;

/**
 * lucene数据库所有存储字段定义，申明。
 * 构造函数参数1 为lucene索引字段标识，参数2为是否需要索引定义（true需要索引，false不需要索引）
 * @author 覃芝鹏
 * @version $Id: Fields.java,v 1.2 2008/11/19 06:43:30 moon Exp $
 */
public interface Fields extends Serializable
{
	/**
	 * 得到lucene实例化类id字段项
	 * @return Fields
	 */
	public Fields getTermFields();
	
	/**
	 * 得到其是否需要lucene索引
	 * @return true需要索引，false反之。
	 */
	public boolean getIndex();
	
	/**
	 * 得到其是否需要lucene存储
	 * @return ture需要存储，false反之。
	 */
	public boolean getStore();
	
	/**
	 * 得到字段权重
	 * @return 该字段在lucene排序中所占权重
	 */
	public float getWeight();
	
	/**
	 * 取得标签
	 * @return 标签
	 */
	public String getLabel();
	
	/**
	 * 得到该枚举类型定义的所有 Field字段数据
	 * @return Field字段数组
	 */
	public Fields[] getFields();
}