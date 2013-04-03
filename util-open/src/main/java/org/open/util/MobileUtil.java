package org.open.util;

import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 手机号码 工具包类
 * @author ibm
 * @version $Id:$
 */
@SuppressWarnings("unchecked")
public class MobileUtil 
{
	//写日志.
	private static final Log log = LogFactory.getLog(MobileUtil.class);
	
	private static Map<String,List<Integer>> AreaToMobileMap = new HashMap<String,List<Integer>>();
	private static Map<Integer,String> MobileToAreaMap = new HashMap<Integer,String>();
	
	static{
		try{
			//loading ziped object stream.
			GZIPInputStream gis = new GZIPInputStream(MobileUtil.class.getResourceAsStream("mobile.map"));
			ObjectInputStream ois = new ObjectInputStream(gis);
			AreaToMobileMap = (Map<String, List<Integer>>) ois.readObject();
			
			//converting key and values.
			Iterator<String> areas = AreaToMobileMap.keySet().iterator();
			while(areas.hasNext()){
				String area = areas.next();
				List<Integer> mobileList = AreaToMobileMap.get(area);
				for(Integer mobile : mobileList){
					if(!MobileToAreaMap.containsKey(mobile)){
						MobileToAreaMap.put(mobile, area.intern());
					}
				}
			}
			
		}catch(Exception e){
			log.error(BeanUtil.getMethodName(e)+" static load error!", e);
		}
	}
	
	public static String getArea(String mobile)
	{
		try{
			return MobileToAreaMap.get(Integer.valueOf(mobile.substring(0, 7)));
		}catch(Exception e){
			return null;
		}
	} 
}
