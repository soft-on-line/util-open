package org.open.net.ip.query;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.open.util.BeanUtil;
import org.open.util.ReaderUtil;
import org.open.util.RegexpUtil;

/**
 * 
 * @author ibm
 * @deprecated
 */
public class IPQuery 
{
	private static final Log log = LogFactory.getLog(IPQuery.class);
	
	public static String sogouQuery(String ip)
	{
		String url = "http://www.sogou.com/web?query="+ip+"&sourceid=&_ast=1264409216&_asf=www.sogou.com&w=01029901&num=10";
		try{
			String html = ReaderUtil.read(new URL(url).openConnection().getInputStream(),"gb18030");
			return RegexpUtil.matchGroup(html,"地理位置：([^\\s]+)");
		}catch(Exception e){
			log.error(BeanUtil.getMethodName(e)+"(IPQueryURL:"+url+")", e);
			
			return ip;
		}
	}

}
