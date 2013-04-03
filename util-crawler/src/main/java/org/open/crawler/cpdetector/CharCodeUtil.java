package org.open.crawler.cpdetector;

import java.io.ByteArrayInputStream;

import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.HTMLCodepageDetector;
import info.monitorenter.cpdetector.io.JChardetFacade;


/**
 * 
 * @author peng
 *
 */
public class CharCodeUtil {

	static CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
	static{
		detector.add(new HTMLCodepageDetector(false));	
		detector.add(JChardetFacade.getInstance());
	}
	
	public static String getCharSet(String html)
	{
		return getCharSet(html.getBytes());
	}
	
	public static String getCharSet(byte[] stream)
	{
		ByteArrayInputStream in = new ByteArrayInputStream(stream);
		String code = "";
		try {
			code = detector.detectCodepage(in, Integer.MAX_VALUE).name();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return code;
	}
	
	
}
