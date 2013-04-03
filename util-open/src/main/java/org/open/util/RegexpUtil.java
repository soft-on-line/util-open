package org.open.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式处理类
 * @author 覃芝鹏
 * @version $Id: RegexpUtil.java,v 1.7 2009/06/17 09:06:54 moon Exp $
 */
public class RegexpUtil
{
	/**
	 * 用来匹配地址的正则表达式
	 */
	public static Properties ADDRESS_REGEXP_LIB = new Properties();

	/**
	 * 用来匹配公司的正则表达式
	 */
	public static Properties CORP_REGEXP_LIB = new Properties();

	/**
	 * 匹配年月日的正则表达式库(必须是3组正则返回值的正则表达式，即年、月、日)
	 */
	private static String[] DATE_REGEXP_LIB	= {
		"(2008)[^\\d]{0,5}年[^\\d]{0,5}([1-9]|0[1-9]|1[0-2])[^\\d]{0,5}月[^\\d]{0,5}([1-9]|0[1-9]|[1-2][0-9]|31)[^\\d]{0,5}日",
		"(2008)-([1-9]|0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|31)"
	};

	/**
	 * 用来匹配时间的正则表达式库,年,月,日等.
	 */
	public static List<String> DATETIME_REGEXP_LIB = new ArrayList<String>();


	public static String[] NEWS_DATETIME_REGEXP_LIB =
	        {"([0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日[0-9]{1,2}:[0-9]{1,2})"
			,"([0-9]{4}/[0-9]{1,2}/[0-9]{1,2}\\s[0-9]{1,2}:[0-9]{1,2})"
			,"([0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日 [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2})"
			,"([0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2})"
			,"([0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{2}:[0-9]{2})"
			,"([0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日)"
			,"([0-9]{4}-[0-9]{1,2}-[0-9]{1,2})"
	        };
	/**
	 * 用来匹配email地址的正则表达式
	 */
//	public static final String EMAIL_REGEXP = "([\\w]+@[^\\s]+)";
	//"^[a-z]([a-z0-9]*[-_]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[\.][a-z]{2,3}([\.][a-z]{2})?$"
	//public static final String EMAIL_REGEXP = "(^[a-z]([a-z0-9]*[-_]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[\\.][a-z]{2,3}([\\.][a-z]{2})?$)";
	public static final String EMAIL_REGEXP = "^(.+)@(.+)$";

	/**
	 * 取得主页的正则表达式
	 */
	private final static String HOMEPAGE_REGEXP
		= "(http://www\\.[^\\s]+|https://www\\.[^\\s]+|www\\.[^\\s]+)";

	/**
	 * 匹配手机号码的正则表达式
	 */
	public static final String MOBILE_REGEXP = "([1][3|5]\\d{9})";

	/**
	 * 用来匹配座机号码的正则表达式
	 */
	public static List<String> PHONE_REGEXP_LIB = new ArrayList<String>();

	/**
	 * 用来匹配url链接的主域名地址
	 */
	//public static List<String> URL_REGEXP_LIB = new ArrayList<String>();

	/*static{
		URL_REGEXP_LIB.add("\\.([^\\.]+)\\.com");
		URL_REGEXP_LIB.add("\\.([^\\.]+)\\.com\\.cn");
		URL_REGEXP_LIB.add("\\.([^\\.]+)\\.cn");
		URL_REGEXP_LIB.add("\\.([^\\.]+)\\.org");
		URL_REGEXP_LIB.add("\\.([^\\.]+)\\.net");
	}*/

	static{
		//PHONE_REGEXP_LIB.add("(\\d{2,3}[-|－| ]*\\d{3,4}[-|－| ]*\\d{7,8}$)");
		//PHONE_REGEXP_LIB.add("(\\d{3,4}[-|－| ]+\\d{7,8}$)");
		PHONE_REGEXP_LIB.add("\\((\\d{2,5})\\)(\\d{7,8})");
		PHONE_REGEXP_LIB.add("(\\d{2,5})[-|－| ]+(\\d{7,8})");
		PHONE_REGEXP_LIB.add("(\\d{7,8})");
	}

	static{
		ADDRESS_REGEXP_LIB.put("( :)",false);
		ADDRESS_REGEXP_LIB.put("(、)",false);
		ADDRESS_REGEXP_LIB.put("(，)",false);
		ADDRESS_REGEXP_LIB.put("(：)",false);
		ADDRESS_REGEXP_LIB.put("(;)",false);
		ADDRESS_REGEXP_LIB.put("(...)",false);

		ADDRESS_REGEXP_LIB.put("(省)",true);
		ADDRESS_REGEXP_LIB.put("(市)",true);
		ADDRESS_REGEXP_LIB.put("(县)",true);
		ADDRESS_REGEXP_LIB.put("(区)",true);
		ADDRESS_REGEXP_LIB.put("(镇)",true);
		ADDRESS_REGEXP_LIB.put("(乡)",true);
		ADDRESS_REGEXP_LIB.put("(村)",true);
		ADDRESS_REGEXP_LIB.put("(组)",true);
		ADDRESS_REGEXP_LIB.put("(路)",true);
		ADDRESS_REGEXP_LIB.put("(大道)",true);
		ADDRESS_REGEXP_LIB.put("(街)",true);
		ADDRESS_REGEXP_LIB.put("(栋)",true);
		ADDRESS_REGEXP_LIB.put("(单元)",true);
		ADDRESS_REGEXP_LIB.put("(公寓)",true);
		ADDRESS_REGEXP_LIB.put("(楼)",true);
		ADDRESS_REGEXP_LIB.put("(厦)",true);
		ADDRESS_REGEXP_LIB.put("(座)",true);
		ADDRESS_REGEXP_LIB.put("(园)",true);
		ADDRESS_REGEXP_LIB.put("(室)",true);
		ADDRESS_REGEXP_LIB.put("(号)",true);
	}

	static{
		CORP_REGEXP_LIB.put("(&nbsp;)", false);
		CORP_REGEXP_LIB.put("(公司.{3,})", false);
		CORP_REGEXP_LIB.put("(厂.+)", false);
		CORP_REGEXP_LIB.put("(\\d)", false);
		CORP_REGEXP_LIB.put("(（)", false);
		CORP_REGEXP_LIB.put("(未上传公司标志)", false);

		CORP_REGEXP_LIB.put("(.+公司)", true);
		CORP_REGEXP_LIB.put("(集团)", true);
		CORP_REGEXP_LIB.put("(厂)", true);
		CORP_REGEXP_LIB.put("(Co\\.Ltd)", true);
	}

	static{
		DATETIME_REGEXP_LIB.add("([1|2]\\d{3})/([0|1]?\\d{1})/([0|1|2|3]?\\d{1})");
		DATETIME_REGEXP_LIB.add("([1|2]\\d{3})\\-([0|1]?\\d{1})\\-([0|1|2|3]?\\d{1})");
	}

	/**
	 * 匹配字符串中的地址.
	 * @param text
	 * @return 匹配到的地址字符串,否则返回null.
	 */
	public static String addressMatch(String text)
	{
		int count = 0;

		Enumeration<?> e = ADDRESS_REGEXP_LIB.keys();
		while(e.hasMoreElements())
		{
			String regexp = (String)e.nextElement();
			if((Boolean)ADDRESS_REGEXP_LIB.get(regexp)){
				if(isMatch(text,regexp)){
					count++;
				}
			}else{
				if(isMatch(text,regexp)){
					return null;
				}
			}
		}

		return (count>1)? text : null;
	}

	public static String[] batchMobileMatch(String text){
		return matchGroups(text,MOBILE_REGEXP);
	}

	/**
	 * 转换网页可以拨打的电话.
	 * @param phone
	 * @return
	 */
	public static String callPhoneMatch(String phone)
    {
		if(!isMobileNum(phone)){
			phone = phoneMatch(phone);
		}

		if(phone==null){
			return null;
		}

		Matcher matcher = matcher(phone, "(\\d)");

        String newPhone = "8";

        if(isMobileNum(phone)){
        	newPhone += "0";
        }

        while(matcher.find()){
        	newPhone += matcher.group();
        }

        return newPhone;
    }

	/**
	 * 匹配字符串中的公司,企业信息名称.
	 * @param text
	 * @return 匹配到的地址字符串,否则返回null.
	 */
	public static String corpMatch(String text)
	{
		int count = 0;

		Enumeration<?> e = CORP_REGEXP_LIB.keys();
		while(e.hasMoreElements())
		{
			String regexp = (String)e.nextElement();

			if((Boolean)CORP_REGEXP_LIB.get(regexp)){
				if(isMatch(text,regexp)){
					count++;
				}
			}else{
				if(isMatch(text,regexp)){
					return null;
				}
			}
		}

		return (count>0)? text : null;
	}

	/**
	 * 从原始字符串中匹配到日期字符串数组（年月日）
	 * @param text 原始字符串
	 * @return 匹配到的日期字符串数组 格式举例（20080101）
	 */
	public static String[] dateMatch(String text)
	{
		List<String> dates = new ArrayList<String>();
		for(int i=0;i<DATE_REGEXP_LIB.length;i++)
		{
			String[][] _date = RegexpUtil.matchMultiGroups(text,DATE_REGEXP_LIB[i]);
			for(int j=0;j<_date.length;j++)
			{
				String year = _date[j][0];
				String month = _date[j][1];
				month = (month.length()==1)? "0"+month : month;
				String day = _date[j][2];
				day = (day.length()==1)? "0"+day : day;

				//插入时排序 按 大至小
				String strDate = year + month  + day;
				int intDate = Integer.valueOf(strDate);
				//数据为空则初始化数组
				if(dates.isEmpty()){
					dates.add(strDate);
				}
				//数组里面包含该日期则跳过
				if(dates.indexOf(strDate)!=-1){
					continue;
				}
				for(int k=0;k<dates.size();k++)
				{
					int tmp = Integer.valueOf(dates.get(k));
					//大于则插入
					if(intDate>tmp){
						dates.add(k,strDate);
						break;
					}else{
						//小于则继续找，直到找到队列末尾再插入
						if(k==(dates.size()-1)){
							dates.add(k+1,strDate);
							break;
						}
					}
				}
			}
		}
		String[] tmp = dates.toArray(new String[dates.size()]);
		for(int i=0;i<tmp.length;i++)
		{
			String t = tmp[i];
			tmp[i] = t.substring(0,4) + "-" + t.substring(4,6) + "-" + t.substring(6,8);
		}

		return tmp;
	}


	/**
	 * 匹配常见新闻资讯中时间字符串
	 * @param text
	 * @return
	 */
	public static String newsDateTimeMatch(String text)
	{
	    String newsTime = null;
		 for (String regexp : NEWS_DATETIME_REGEXP_LIB)
		  {
			  newsTime =  RegexpUtil.matchGroup(text
					  , regexp);
	   		  if (newsTime != null)
	   		  {
	   		     break;
	   		  }
		  }
		return newsTime;
	}

	/**
	 * 匹配时间字符串
	 * @param text
	 * @return
	 */
	public static String dateTimeMatch(String text)
	{
		for(int i=0;i<DATETIME_REGEXP_LIB.size();i++)
		{
			String regexp = DATETIME_REGEXP_LIB.get(i);
			//if(matcher(text,regexp).find())
				//return text;
			String tmp[] = RegexpUtil.matchMultiGroup(text, regexp);
			if(tmp.length>0)
			{
				String buf = "";
				for(int j=0;j<tmp.length;j++) {
					buf += tmp[j];
				}
				return buf;
			}
		}

		return null;
	}

	/**
	 * 匹配字符串中的email地址.
	 * @param text
	 * @return 匹配到返回完整的email,否则返回null.
	 */
	public static String emailMatch(String text){
		return matchGroup(text,EMAIL_REGEXP);
	}

	/**
	 * 从传入文本中提取主页 地址链接 信息
	 * @param text 原始文本字符串
	 * @return 查询后的带修正的http头主页链接地址
	 */
	public static String homepageMatch(String text)
	{
		String homepage = RegexpUtil.matchGroup(text, HOMEPAGE_REGEXP);
		if(StringUtil.isBlank(homepage)){
			return null;
		}else if("www".equalsIgnoreCase(homepage.substring(0,3))){
			return "http://"+homepage;
		}else{
			return homepage;
		}
	}

	/**
	 * 按正则表达式查找字符串中是否匹配
	 * @param str 要匹配的字符串
	 * @param regexp 匹配用的正则表达式
	 * @return true代表匹配到内容,false没有匹配到相关内容
	 */
	public static boolean isMatch(String str,String regexp){
		return matcher(str,regexp).find();
	}

	/**
	 * 判断一个字符串是否为手机号码字符串
	 * @param text 原始文本
	 * @return true是手机号码false非手机号码
	 */
	public static boolean isMobileNum(String text)
	{
		if(text==null)
			return false;

		return isMatch(text,MOBILE_REGEXP);
	}

	/**
	 * 用正则表达式匹配指定字符串中的内容
	 * @param str 要匹配的字符串
	 * @param regexp 匹配用的正则表达式
	 * @return java.util.regexp.Matcher
	 */
	public static Matcher matcher(String str, String regexp)
	{
		Pattern pattern = Pattern.compile(regexp,Pattern.DOTALL); // 编译解析正则表达式
		return pattern.matcher((str==null)? "" : str); // 开始检查匹配情况
	}

	/**
	 * <font color='red'>匹配正则第一个有效括号内第一个结果。</font>
	 * <br/>
	 * <font color='green'>该四组matchGroup,matchGroups,matchMultiGroup,matchMultiGroups方法体是有区别的，请务必认真看手册</font>
	 * <p>举例:</p>
	 * <p>正则表达式：(\d*?)</p>
	 * <P>匹配内容：abc123cde456</p>
	 * <p>匹配结果：123</p>
	 * <br/>
	 * @param text 需要匹配的字符串
	 * @param regexp 正则表达式
	 * @return String
	 */
	public static String matchGroup(String text,String regexp)
	{
		if(text == null){
			return null;
		}

		Matcher m = matcher(text,regexp);
		return (m.find())? m.group(1).trim() : null;
	}

	/**
	 * <font color='red'>匹配正则第一个有效括号内所有结果，结果集是一维数组形式。</font>
     * <br/>
     * <font color='green'>该四组matchGroup,matchGroups,matchMultiGroup,matchMultiGroups方法体是有区别的，请务必认真看手册</font>
	 * <p>举例:</p>
     * <p>正则表达式：(\d*?)</p>
     * <P>匹配内容：abc123cde456</p>
     * <p>匹配结果(一维数组)：{123,456}</p>
     * <br/>
	 * @param text 需要匹配的字符串
	 * @param regexp 正则表达式
	 * @return String
	 */
	public static String[] matchGroups(String text,String regexp)
	{
		List<String> list = new ArrayList<String>();
		Matcher m = matcher(text,regexp);

		while(m.find()){
			list.add(m.group(1));
		}

		return list.toArray(new String[list.size()]);
	}

	/**
	 * <font color='red'>匹配正则所有有效括号内第一组结果，结果集是一维数组形式，分别对应每个有效括号匹配到的结果集。</font>
     * <br/>
     * <font color='green'>该四组matchGroup,matchGroups,matchMultiGroup,matchMultiGroups方法体是有区别的，请务必认真看手册</font>
     * <p>举例:</p>
     * <p>正则表达式：(\w*?)(\d*?)</p>
     * <P>匹配内容：abc123cde456</p>
     * <p>匹配结果(一维数组)：{abc,123}</p>
     * <br/>
	 * @param text 需要匹配的字符串
	 * @param regexp 正则表达式
	 * @return String[]
	 */
	public static String[] matchMultiGroup(String text,String regexp)
	{
		List<String> _list = new ArrayList<String>();
		Matcher m = matcher(text,regexp);
		if(m.find())
		{
			for(int i=0;i<m.groupCount();i++)
			{
				_list.add(m.group(i+1));
			}
		}
		String[] groups = new String[_list.size()];
		return _list.toArray(groups);
	}

	/**
	 * <font color='red'>匹配正则所有有效括号内所有结果，结果集是二维数组形式，一维表式结果集总数，二维对应每个有效括号匹配到的结果集。</font>
     * <br/>
     * <font color='green'>该四组matchGroup,matchGroups,matchMultiGroup,matchMultiGroups方法体是有区别的，请务必认真看手册</font>
     * <p>举例:</p>
     * <p>正则表达式：(\w*?)(\d*?)</p>
     * <P>匹配内容：abc123cde456</p>
     * <p>匹配结果(二维数组)：{{abc,123},{cde,456}}</p>
     * <br/>
     * @param text 需要匹配的字符串
     * @param regexp 正则表达式
	 * @return String[][]
	 */
	public static String[][] matchMultiGroups(String text,String regexp)
	{
		List<String[]> _list = new ArrayList<String[]>();
		Matcher m = matcher(text,regexp);
		while(m.find())
		{
			String[] _groups = new String[m.groupCount()];

			for(int i=0;i<_groups.length;i++)
			{
				_groups[i] = m.group(i+1);
			}

			_list.add(_groups);
		}
		String[][] groups = new String[_list.size()][];
		return _list.toArray(groups);
	}


	/**
	 * 匹配成功的第一个字符串
	 * @param text 匹配成功的字符串
	 * @param regexp 正则表达式
	 * @return String
	 */
	public static String matchTitle(String text,String regexp)
	{
		if(text == null){
			return null;
		}
		Matcher m = matcher(text,regexp);
		return (m.find())? m.group(1).trim() : null;
	}

	/**
	 * 匹配url的主域名字段
	 * @param url
	 * @return
	 */
	/*public static String domainNameMatch(String url)
	{
		for(int i=0;i<URL_REGEXP_LIB.size();i++)
		{
			String regexp = (String)URL_REGEXP_LIB.get(i);
			Matcher m = matcher(url,regexp);
			if(m.find())
				return m.group();
		}

		return null;
	}*/

	/**
	 * 匹配字符串中的手机号码.
	 * @param text
	 * @return 匹配到返回完整的电话号码,否则返回null.
	 */
	public static String mobileMatch(String text){
		return matchGroup(text,MOBILE_REGEXP);
	}

	/**
	 * 匹配字符串中的座机号码.
	 * @param text
	 * @return 匹配到返回完整的电话号码,否则返回null.
	 */
	public static String phoneMatch(String text)
	{
		if(text == null){
			return null;
		}

		for(int i=0;i<PHONE_REGEXP_LIB.size();i++)
		{
			Matcher m = matcher(text,PHONE_REGEXP_LIB.get(i));
			if(m.find())
			{
				switch(m.groupCount())
				{
					case 1:	return m.group();
					case 2:
						String regionNo = m.group(1);
						if(regionNo.charAt(0)!='0'){
							regionNo = "0" + regionNo;
						}
						return regionNo + "-" + m.group(2);
					default: break;
				}
			}
		}

		return null;
	}
}
