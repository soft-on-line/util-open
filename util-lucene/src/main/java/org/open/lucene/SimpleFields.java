package org.open.lucene;

import java.util.Properties;

public class SimpleFields implements Fields 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8747125801472332533L;

	/**字段标识 名称*/
	private String label;
	
	/**是否需要索引*/
	private boolean index = true;
	
	/**是否需要存储*/
	private boolean store = false;
	
	/**该字段索引 在排序中所需的权重（中性值是1.0）*/
	private float weight = 1.0f;
	
	private static Properties lib;
	
	public SimpleFields()
	{
		if(lib == null)
		{
			lib = new Properties();
			Field[] fields = Field.getFields();
			for(int i=0;i<fields.length;i++)
			{
				Field field = fields[i];
				lib.put(field.toString(),new SimpleFields(field.toString(),field.getIndex(),field.getStore(),field.getWeight()));
			}
		}
	}
		
	private SimpleFields(String label,boolean index,boolean store,float weight)
	{
		this.label = label;
		this.index = index;
		this.store = store;
		this.weight = weight;
	}
	
	public static Fields getFields(Field field)
	{
		if(lib == null){
			new SimpleFields();
		}
		return (Fields)lib.get(field.toString());
	}
	
	public Fields[] getFields() 
	{
		return (Fields[])lib.values().toArray(new Fields[lib.size()]);
	}
	
	public boolean getIndex() {
		return index;
	}

	public float getWeight() {
		return weight;
	}

	public String getLabel(){
		return this.label;
	}
	
	public Fields getTermFields(){
		return (SimpleFields)lib.get(Field.SID.toString());
	}

	public boolean getStore() {
		return store;
	}

	public void setStore(boolean store) {
		this.store = store;
	}
	
}
