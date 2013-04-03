package org.open.util;

/**
 * 验证类库工具包
 * @author moon
 * @version $Id: ValidateUtil.java,v 1.2 2009/06/17 09:06:55 moon Exp $
 */
public class ValidateUtil 
{
	public static boolean isEmpty(String str){
		return StringUtil.isEmpty(str);
	}
	
	public static boolean isEmail(String email){
		return !isEmpty(RegexpUtil.emailMatch(email));
	}
}
