package org.open.lucene;

/**
 * lucene数据库所有存储字段定义，申明。
 * 构造函数参数1 为lucene索引字段标识，参数2为是否需要索引定义（true需要索引，false不需要索引）
 * @author 覃芝鹏
 * @version $Id: Field.java,v 1.3 2008/11/19 06:43:30 moon Exp $
 */
public enum Field 
{
	/**主键 Field 标识*/
	SID(false,true,(float)1.0),

	/**标题 Field 标识*/
	TITLE(true,true,(float)1.5),

	/**全文索引内容 Field 标识*/
	CONTENT(true,false,(float)1.0),

	/**URL Field 标识*/
	URL(false,true,(float)1.0),

	/**时间 Field 标识*/
	DATE(false,true,(float)1.0);

	private boolean index;
	
	private boolean store;
	
	private float weight;

	private Field(boolean index,boolean store,float weight) {
		this.index = index;
		this.weight = weight;
	}

	/**
	 * 得到其是否需要lucene索引
	 * @return true需要索引，false反之。
	 */
	public boolean getIndex() {
		return index;
	}
	
	public boolean getStore() {
		return store;
	}
	
	/**
	 * 得到字段在lucene中索引权重
	 * @return 以1.0为中心点的浮点数权重值
	 */
	public float getWeight(){
		return weight;
	}

	/**
	 * 得到该枚举类型定义的所有 Field字段数据
	 * @return Field字段数组
	 */
	public static Field[] getFields() 
	{
		java.lang.reflect.Field[] fields = Field.class.getFields();
		Field[] _fields = new Field[fields.length];
		for (int i = 0; i < fields.length; i++) {
			_fields[i] = Enum.valueOf(Field.class, fields[i].getName());
		}
		return _fields;
	}
}