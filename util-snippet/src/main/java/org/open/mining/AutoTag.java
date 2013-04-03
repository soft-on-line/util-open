package org.open.mining;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.open.chinese.Chinese;
import org.open.chinese.ChineseUtil;

public class AutoTag 
{
	//写日志.
	private static final Log log = LogFactory.getLog(AutoTag.class);
	
	private static ChineseUtil cu = ChineseUtil.instance(ChineseUtil.InstanceModel.MiningModel);
	
	private final static int PARSE_GROUP_NUM = 20;
	
	private final static int HIGH_FREQUENCY_NUM = Float.valueOf(PARSE_GROUP_NUM * 0.3f).intValue();
	
	public static String[] parseToString(String text,int size)
	{
		Set<String> lib = new HashSet<String>();
		
		Chinese[] words = ChineseUtil.sort(cu.parserMaxFirst(text));
		for(int i=0;i<words.length && lib.size()<HIGH_FREQUENCY_NUM;i++)
		{
			String word = words[i].getWord();
			if(word.length()>1){
				lib.add(word);
			}
		}
		
		log.info("High frequency words is "+lib);
		
		String[] pieceText = cu.parserMaxFirstBySeparator(text).split("\\s+");
		boolean flag = false;
		for(int i=0;i<pieceText.length && lib.size()<PARSE_GROUP_NUM;i++)
		{
			if(flag){
				lib.add(pieceText[i]);
				flag = false;
			}
			
			if(pieceText[i].equals("的")){
				flag = true;
			}
		}
		
		return lib.toArray(new String[lib.size()]);
	}
}
