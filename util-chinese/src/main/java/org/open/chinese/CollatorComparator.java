package org.open.chinese;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;

public class CollatorComparator implements Comparator<Object> 
{
	Collator collator = Collator.getInstance();

	//public int compare(Object e1, Object e2) 
	{
		//return e1.toString().length() - e2.toString().length();
	}
	
	public int compare(Object element1, Object element2) 
	{
		//未区分大小写
		CollationKey key1 = collator.getCollationKey(element1.toString());
		CollationKey key2 = collator.getCollationKey(element2.toString());
		
		//区分大小写
		//CollationKey key1 = collator.getCollationKey(element1.toString().toLowerCase());
		//CollationKey key2 = collator.getCollationKey(element2.toString().toLowerCase());

		//正向排序
		return key1.compareTo(key2);
		
		//反向排序
		//return -key1.compareTo(key2);
	}

}
