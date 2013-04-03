package org.open.crawler.html;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.HtmlCleaner;
import org.open.util.RegexpUtil;
import org.open.util.StringUtil;

/**
 * 网页html代码处理工具包类
 * @author 覃芝鹏
 * @version $Id: HtmlDataUtil.java,v 1.8 2009/07/04 09:01:51 ibm Exp $
 */
public class HtmlDataUtil 
{
	/**
	 * 写日志.
	 */
	private static final Log log = LogFactory.getLog(HtmlDataUtil.class);
	
	/**
	 * 定义 键值对应对 键 长度
	 */
	public static final int KEY_LENGTH = 20;
	
	/**
	 * 默认 键值 分割符号库
	 */
	public static final String[] DEFAULT_SPLIT_LIB = {
		"：",
		":"
	};
	
	/**
	 * 整理html代码 踢出html注释、js代码、样式等等
	 * @param html 原始html代码
	 * @return 整理后的html代码
	 */
	public static String tidy(String html)
	{
		//html = html.toLowerCase();
		//log.info("TIDY BEFORE:"+html);
		
		html = html.replaceAll("<!--.*?-->","");
		html = html.replaceAll("&nbsp;"," ");
		html = html.replaceAll("[\\s]+"," ");
		
		html = getPrettyXmlAsString(html);
		
		html = cutHtmlTag(html,"font");
		html = cutHtmlTag(html,"strong");
		
		html = cutHtmlNode(html,"script");
		html = cutHtmlNode(html,"style");
		
		//log.info("TIDY AFTER:"+html);
		
		return html;
	}
	
	/**
	 * 剪切html中 指定节点 及其 其下的树节点（包括内容）
	 * @param html 原始html代码
	 * @param nodeName html节点名称
	 * @return 剪切后的html代码
	 */
	public static String cutHtmlNode(String html,String nodeName)
	{
		return html.replaceAll("<"+nodeName+".*?>.*?</"+nodeName+">","");
	}
	
	/**
	 * 剪切html中 指定一组节点 及其 其下的树节点（包括内容）
	 * @see #cutHtmlNode(String, String)
	 * @param html 原始html代码
	 * @param nodeNames html节点名称 数组
	 * @return 剪切后的html代码
	 */
	public static String cutHtmlNode(String html,String[] nodeNames)
	{
		for(int i=0;i<nodeNames.length;i++)
		{
			html = cutHtmlNode(html,nodeNames[i]);
		}
		
		return html;
	}
	
	/**
	 * 剪切html代码中 指定 的节点 且 不删除 其子节点及其内容
	 * @param html 原始html代码
	 * @param tag html标签节点名称
	 * @return 剪切后的html代码
	 */
	public static String cutHtmlTag(String html,String tag)
	{
		return html.replaceAll("<"+tag+".*?>"," ").replaceAll("</"+tag+">"," ");
	}
	
	/**
	 * 剪切html代码中 指定的 一组节点 且 不删除 其子节点及其内容
	 * @see #cutHtmlTag(String, String)
	 * @param html 原始html代码
	 * @param tags html标签节点名称 数组
	 * @return 剪切后的html代码
	 */
	public static String cutHtmlTag(String html,String[] tags)
	{
		for(int i=0;i<tags.length;i++)
		{
			html = cutHtmlTag(html,tags[i]);
		}
		
		return html;
	}
	
	/**
	 * 剪除html代码中所有节点 属性值
	 * @param html 原始html代码
	 * @return 剪除后的html代码
	 */
	public static String cutHtmlTagAttribute(String html)
	{
		return cutHtmlTagAttribute(html,getAllTags(html));
	}
	
	/**
	 * 剪除html代码中指定 标签节点 属性值
	 * @param html 原始html代码
	 * @param tag html标签名称
	 * @return 剪除后的html代码
	 */
	public static String cutHtmlTagAttribute(String html,String tag)
	{
		return html.replaceAll("<"+tag+" .*?>","<"+tag+">");
	}
	
	/**
	 * 剪除html代码中指定 一组标签节点 属性值
	 * @see #cutHtmlNode(String, String)
	 * @param html 原始html代码
	 * @param tags html标签名称 数组
	 * @return 剪除后的html代码
	 */
	public static String cutHtmlTagAttribute(String html,String tags[])
	{
		for(int i=0;i<tags.length;i++)
		{
			html = cutHtmlTagAttribute(html,tags[i]);
		}
		
		return html;
	}
	
	/**
	 * 得到html里面纯文本信息
	 * @param html 原始html代码
	 * @return html里面纯文本信息
	 */
	public static String getInnerText(String html)
	{
		html = tidy(html);
		html = html.replaceAll("<.*?>", "");
		
		return html;
	}
	
	/**
	 * 得到整理后的html代码
	 * @param html
	 * @return
	 * @throws IOException
	 */
	private static HtmlCleaner getHtmlCleaner(String html) throws IOException
	{
		HtmlCleaner cleaner = new HtmlCleaner(new StringReader(html));
		
		cleaner.clean();
		
		return cleaner;
	}
	
	/**
	 * 整理出漂亮 xml 树结构 html代码
	 * @param html 原始的html代码
	 * @return 整理后的html代码
	 */
	public static String getPrettyXmlAsString(String html)
	{
		try {
			return getHtmlCleaner(html).getPrettyXmlAsString();
		} catch (IOException e) {
			log.error("getPrettyXmlAsString(...) error!", e);
			return html;
		}
	}
	
	/**
	 * 得到html里面纯文本信息，但是跳开一些指定的html tag标签
	 * @param html 原始html代码
	 * @param tags 指定的html tags标签
	 * @return 包含跳过标签的文本信息
	 */
	public static String getInnerTextWithEscapeTag(String html,String[] tags)
	{
		try{
			HtmlCleaner cleaner = getHtmlCleaner(html);
			
			html = cleaner.getPrettyXmlAsString();
			
			html = cutHtmlNode(html,"script");
			html = cutHtmlNode(html,"style");
			
			Iterator<?> allTags = cleaner.getAllTags().iterator();
			List<String> cuteTag = new ArrayList<String>();
			while(allTags.hasNext()) {
				cuteTag.add(allTags.next().toString());
			}
			
			cuteTag.add("\\?xml");
			cuteTag.add("html");
			cuteTag.add("head");
			cuteTag.add("!--");
			
			//去掉排除的tag
			for(String tag : tags){
				cuteTag.remove(tag);
			}
			
			//"</?((div)|(body)).*?>"
			String regexp = "</?((";
			for(int i=0;i<cuteTag.size()-1;i++)
			{
				String tag = cuteTag.get(i);
				regexp += tag + ")|(";
			}
			regexp += cuteTag.get(cuteTag.size()-1);
			regexp += ")).*?>";
			
			html = html.replaceAll(regexp, "");
			
			return html;
		}catch(Exception e){
			log.error("getInnerTextWithEscapeTag(...) error!", e);
			return null;
		}
	}
	
	/**
	 * @see #getNode(String, String[])
	 */
	public static String[] getNode(String html,String tag)
	{
		return getNode(html,new String[]{tag});
	}
	
	/**
	 * 得到html内指定tags节点<br/>
	 * 【BUG提示】当指定的tags节点在html里面有嵌套关系时，会出现匹配层错误，请注意使用。
	 * @param html 原始html
	 * @param tags 指定tags
	 * @return 匹配到的节点数组
	 */
	public static String[] getNode(String html,String[] tags)
	{
		try{
			HtmlCleaner cleaner = getHtmlCleaner(html);
			
			html = cleaner.getPrettyXmlAsString();
			
			String regexp_seg = "((";
			for(int i=0;i<tags.length-1;i++)
			{
				String tag = tags[i];
				regexp_seg += tag + ")|(";
			}
			regexp_seg += tags[tags.length-1] + "))";
			
			String regexp = "((<" + regexp_seg + "[^<]*?/>)|(<" + regexp_seg + ".*?>.*?</" + regexp_seg + ">))";
			
			//((<((img)|(a))[^<]*?/>)|(<((img)|(a)).*?>.*?</((img)|(a))>))
			return RegexpUtil.matchGroups(html, regexp);
		}catch(Exception e){
			log.error("getTags(...) error!", e);
			return null;
		}
	}
	
	/**
	 * 提取html代码中 指定节点 标签里面 的内容
	 * @param html html代码
	 * @param tag 指定的节点标签
	 * @return html指定节点标签里面的内容 数组
	 */
	public static String[] getHtmlInnerHTMLByTag(String html,String tag)
	{
		return RegexpUtil.matchGroups(html,"<"+tag+">(.*?)</"+tag+">");
	}
	
	/**
	 * 提取html代码中 指定一组节点 标签里面 的内容
	 * @see #getHtmlInnerHTMLByTag(String, String)
	 * @param html html代码
	 * @param tags 一组节点标签
	 * @return 二维数组 一维下标对应各个节点标签 二维下标对应其标题匹配的内容数组
	 */
	public static String[][] getHtmlInnerHTMLByTag(String html,String[] tags)
	{
		String[][] strs = new String[tags.length][];
		for(int i=0;i<tags.length;i++)
		{
			strs[i] = getHtmlInnerHTMLByTag(html,tags[i]);
		}
		
		return strs;
	}
	
	/**
	 * 提取html代码中 指定节点 标签里面 的文本内容
	 * @param html html代码
	 * @param tag 节点标签
	 * @return html指定节点标签里面的文本内容 数组
	 */
	public static String[] getHtmlInnerTextByTag(String html,String tag)
	{
		String[] _html = RegexpUtil.matchGroups(html,"<"+tag+">(.*?)</"+tag+">");
		for(int i=0;i<_html.length;i++)
		{
//			_html[i] = StringUtil.tidy(getHtmlInnerText(_html[i]));
			_html[i] = getHtmlInnerText(_html[i]);
			_html[i] = StringUtil.cutPunctuation(_html[i]);
		}
		return _html;
	}
	
	/**
	 * 提取html代码中 指定一组节点 标签里面 的文本内容
	 * @param html html代码
	 * @param tags 一组节点标签
	 * @return 二维数组 一维下标对应各个节点标签 二维下标对应其标题匹配的文本内容数组
	 */
	public static String[][] getHtmlInnerTextByTag(String html,String[] tags)
	{
		String[][] strs = new String[tags.length][];
		for(int i=0;i<tags.length;i++)
		{
			strs[i] = getHtmlInnerTextByTag(html,tags[i]);
		}
		
		return strs;
	}
	
	/**
	 * 提取html代码中所有的文字 信息
	 * @param html 原始html代码
	 * @return 提取的页面文字
	 */
	public static String getHtmlInnerText(String html)
	{
		return html.replaceAll("<.*?>","");
	}
	
	/**
	 * 得到网页中所有的标签
	 * @param html html代码
	 * @return 返回包含标签的数组
	 */
	@SuppressWarnings("unchecked")
	public static String[] getAllTags(String html)
	{
		try{
			HtmlCleaner cleaner = new HtmlCleaner(new StringReader(html));
			
			cleaner.clean();
			
			Set<String> set = cleaner.getAllTags();
			
			return (String[])set.toArray(new String[set.size()]);
		}catch(Exception e){
			log.error("HtmlUril getAllTags(...) error!", e);
			return null;
		}
	}
	
	/**
	 * 删除除了输入定义为例外的标签以外的所有HTML标签节点（注意 此方法不删除内容）
	 * @param html
	 * @param exceptionTags
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static String cuteAllTagsExceptInput(String html, String[] exceptionTags)
	{
		String[] allTags = getAllTags(html);
		boolean dealFlag = true;
		for (String tag: allTags)
		{
			tag = tag.toUpperCase();
			dealFlag = true;
			for (String exceptionTag : exceptionTags)
			{
				if (tag.equalsIgnoreCase(exceptionTag))
				{
					dealFlag = false;
					break;
				}
			}
			if (dealFlag)
			{
				html = html.replaceAll("<"+tag+".*?>","").replaceAll("</"+tag+">","");
			}
		}
		return html;
	}
	
	/**
	 * 替换键 中的分隔符
	 * @param key 键
	 * @param correctLib 分隔符库
	 * @return 更新后的键
	 */
	//private static String correctKey(String key,String[] correctLib)
	private static String correctKey(String key)
	{
//		for(int i=0;i<correctLib.length;i++)
//		{
//			String regexpKey = key;
//			
//			regexpKey = regexpKey.replaceAll("\\[","");
//			regexpKey = regexpKey.replaceAll("\\]","");
//			regexpKey = regexpKey.replaceAll("\\*","");
//			
//			key = regexpKey.replaceAll(correctLib[i],"");
//		}
		//key = StringUtil.tidyStrictly(key);
		//key = StringUtil.cutPunctuation(key);
		key = StringUtil.tidyStrictly(key);
		
		return key;
	}
	
	/**
	 * 辅助分析方法体
	 * @param map 键值库
	 * @param start 奇偶起点（0 or 1）
	 * @param content 键值数组
	 * @param haveDefTitle 是否包含自定义字段
	 */
	private static void buildMap(Properties map,int start,String[] content,boolean haveDefTitle)
	{
		for(int i=start;i<content.length;i+=2)
		{
			//插入 自定义标题 避免 可能漏掉 公司简介 等大量的文字内容
			if(content[i].length()>0 && haveDefTitle){
				map.put("自定义标题"+i,content[i]);
			}
			
			//越界跳出
			if((i+1) == content.length){
				break;
			}
			
			String key = content[i];
			String value = content[i+1];
			
//			key = correctKey(key,DEFAULT_SPLIT_LIB);
			key = correctKey(key);
			
			//log.info("Key:["+key+"]");
			//log.info("Value:["+value+"]");
			
			//键 长度大于10的跳过
			if(key.length()>KEY_LENGTH){
				continue;
			}
			
			//值中 为空值 或 值中包含键 的跳过
			if(value.length()<=0 && map.containsKey(key)){
				continue;
			}
			
			//压入键值对应对
			map.put(key, value);
		}
	}
	
	/**
	 * 得到html table 单元格里面 文本内容
	 * @param html 原始html代码
	 * @return 文本内容 字符串数组
	 */
	private static String[] getHtmlTableCellText(String html)
	{
		//初步整理html
		html = tidy(html);
		//log.info(html);
		
		//去掉所有标签属性值
		html = cutHtmlTagAttribute(html);
		//log.info(html);
		
		//得到整理后html
		html = getPrettyXmlAsString(html);
		//log.info(html);
		
		//替换所有th到td
		html = html.replaceAll("th","td");
		//log.info(html);
		
		//得到td的innerText数组
		return getHtmlInnerTextByTag(html,"td");
	}
	
	private static String[] BracketRegexpLib =
	{
		"【(.+?)】(.+)"
	};
	
	public static Properties getHtmlTableBracketKeyValueMap(String html)
	{
		Properties map = new Properties();
		
		//得到html table 单元格里面内容
		String[] content = getHtmlTableCellText(html);
		
		for(int i=0;i<content.length;i++)
		{
			for(int j=0;j<BracketRegexpLib.length;j++)
			{
				String[] tmp = RegexpUtil.matchMultiGroup(content[i], BracketRegexpLib[j]);
				if(2 == tmp.length)
				{
					if(StringUtil.isBlank(tmp[0]) || StringUtil.isBlank(tmp[1])) {
						continue;
					}else{
						//key
						tmp[0] = StringUtil.cutPunctuation(tmp[0]);
						//value
						tmp[1] = StringUtil.cutPunctuation(tmp[1]);
						
						//修正key
						tmp[0] = StringUtil.tidyStrictly(tmp[0]);
						
						map.put(tmp[0], tmp[1]);
					}
				}
			}
		}
		
		return map;
	}
	
	/**
	 * 从html table中提取 可能键值对应对
	 * @param html html代码
	 * @return 键值对应对 库
	 */
	public static Properties getHtmlTableKeyValueMap(String html,boolean haveDefTitle)
	{
		Properties map = new Properties();
		
		//得到html table 单元格里面内容
		String[] content = getHtmlTableCellText(html);
		
		//偶数为key，奇数为value。
		buildMap(map,0,content,haveDefTitle);
		
		//奇数为key，偶数为value。
		buildMap(map,1,content,haveDefTitle);
		
		return map;
	}
	
	/**
	 * 从html代码中提取 可能键值对应对
	 * @see #getHtmlKeyValueMap(String, String[])
	 * @param html html代码
	 * @return 键值对应对 库
	 */
	public static Properties getHtmlKeyValueMap(String html)
	{
		Properties p = getHtmlTableKeyValueMap(html,false);
		p.putAll(getHtmlTableBracketKeyValueMap(html));
		p.putAll(getHtmlKeyValueMap(html,getAllTags(html)));
		
		return p;
	}
	
	/**
	 * 从html代码中提取 可能键值对应对
	 * @see #getHtmlKeyValueMap(String, String[], String)
	 * @param html html代码
	 * @param tags 指定的标签数组
	 * @return 键值对应对 库
	 */
	public static Properties getHtmlKeyValueMap(String html,String[] tags)
	{
		return getHtmlKeyValueMap(html,tags,DEFAULT_SPLIT_LIB);
	}
	
	/**
	 * 从html代码中提取 可能键值对应对
	 * @param html 原始html代码
	 * @param tags 指定的标签数组
	 * @param split 提取键值对 的分隔标识符
	 * @return 键值对应对 库
	 */
	public static Properties getHtmlKeyValueMap(String html,String[] tags,String[] split)
	{
		Properties map = new Properties();
		
		//初步整理html
		html = tidy(html);
		//log.info(html);
		
		//去掉所有标签属性值
		html = cutHtmlTagAttribute(html);
		//log.info(html);
		
		//得到整理后html
		html = getPrettyXmlAsString(html);
		//log.info(html);
		
		//替换html中回车，用于文字分段时用
		html = html.replaceAll("<br/>","br");
		
		log.info(html);
		
		//包含键值对 的标签
		String[][] content = getHtmlInnerHTMLByTag(html,tags);
		
		for(int i=0;i<content.length;i++)
		{
			for(int j=0;content[i]!=null && j<content[i].length;j++)
			{
				content[i][j] = getHtmlInnerText(content[i][j]);
				content[i][j] = StringUtil.tidy(content[i][j]);
				
				for(int k=0;k<split.length;k++){
					split(map,content[i][j],"br",split[k]);
				}
			}
		}
		
		return map;
	}
	
	private static String[] MOBILE_LIB = {
		"手机",
		"移动电话",
		"手 机",
	};
	
	/**
	 * 从键值对应库中提取 手机号码信息
	 * @param p 键值对应对 库
	 * @return 手机号码
	 */
	public static String getMobile(Properties p){
		return RegexpUtil.mobileMatch(findKeyValue(p,MOBILE_LIB));
	}
	
	private static String[] PHONE_LIB = {
		"电话",
		"电 话",
		"联系人电话",
		"联系电话",
		"电话号码"
	};
	
	/**
	 * 从键值对应库中提取 座机号码信息
	 * @param p 键值对应对 库
	 * @return 座机号码
	 */
	public static String getPhone(Properties p)
	{
		String _txt = findKeyValue(p,PHONE_LIB);
		String _mobile = RegexpUtil.mobileMatch(_txt);
		if(_mobile == null){
			return RegexpUtil.phoneMatch(_txt);
		}else{
			return _mobile;
		}
	}
	
	private static String[] EMAIL_LIB = {
		"e-mail",
		"email",
		"电子邮箱",
		"电子邮件"
	};
	
	/**
	 * 从键值对应库中提取 email信息
	 * @param p 键值对应对 库
	 * @return email
	 */
	public static String getEmail(Properties p){
		return RegexpUtil.emailMatch(findKeyValue(p,EMAIL_LIB));
	}
	
	private static String[] ADDRESS_LIB = {
		"地址",
		"地 址",
		"经营地址"
	};
	
	/**
	 * 从键值对应库中提取 地址信息
	 * @param p 键值对应对 库
	 * @return 地址
	 */
	public static String getAddress(Properties p)
	{
		String _address = findKeyValue(p,ADDRESS_LIB);
		if(_address==null){
			return null;
		}else{
			return (_address.length()>200)? null : _address;
		}
	}
	
	private static String[] ZIP_LIB = {
		"邮编",
		"邮政编码"
	};
	
	/**
	 * 从键值对应库中提取 邮编信息
	 * @param p 键值对应对 库
	 * @return 邮编
	 */
	public static String getZip(Properties p)
	{
		String _zip = findKeyValue(p,ZIP_LIB);
		if(_zip==null){
			return null;
		}else{
			return (_zip.length()>20)? null : _zip;
		}
	}
	
	/*private static String[] HOMEPAGE_LIB = {
		"公司主页",
		"主页"
	};*/
	
	/**
	 * 从键值对应库中提取 主页信息
	 * @param p 键值对应对 库
	 * @return 主页
	 */
	/*public static String getHomepage(Properties p)
	{
		String _homepage = findKeyValue(p,HOMEPAGE_LIB);
		if(_homepage==null){
			return null;
		}else{
			if(!_homepage.substring(0,7).equalsIgnoreCase("http://")){
				_homepage = "http://" + _homepage;
			}
			
			if(URLUtil.isTrue(_homepage)){
				return _homepage;
			}else{
				return null;
			}
		}
	}*/
	
	/**
	 * 从键值对应库中提取 主页信息
	 * @param p 键值对应对 库
	 * @return 主页
	 */
	public static String getHomepage(Properties p)
	{
		Enumeration<?> e = p.keys();
		while(e.hasMoreElements())
		{
			String key = (String)e.nextElement();
			String homepage = RegexpUtil.homepageMatch(key);
			if(homepage != null){
				return homepage;
			}
				
//			String value = (String)p.get(key);
			homepage = RegexpUtil.homepageMatch(key);
			if(homepage != null){
				return homepage;
			}
		}
		
		return null;
	}
	
	private static String[] LINKMAN_LIB = {
		"联系人"
	};
	
	/**
	 * 从键值对应库中提取 地址信息
	 * @param p 键值对应对 库
	 * @return 地址
	 */
	public static String getLinkman(Properties p)
	{
		String _linkman = findKeyValue(p,LINKMAN_LIB);
		if(_linkman==null){
			return null;
		}else{
			return (_linkman.length()>20)? null : _linkman;
		}
	}
	
	private static String[] FAX_LIB = {
		"传真",
		"传真号码"
	};
	
	/**
	 * 从键值对应库中提取 传真信息
	 * @param p 键值对应对 库
	 * @return 传真
	 */
	public static String getFax(Properties p)
	{
		String _fax = findKeyValue(p,FAX_LIB);
		if(_fax==null){
			return null;
		}else{
			return (_fax.length()>20)? null : _fax;
		}
	}
	
	private static String[] CORP_LIB = {
		"公司",
		"公司名",
		"公司名称"
	};
	
	/**
	 * 从键值对应库中提取 公司名称信息
	 * @param p 键值对应对 库
	 * @return 公司名称
	 */
	public static String getCorp(Properties p){
		return findKeyValue(p,CORP_LIB);
	}
	
	/**
	 * 在键值库 所有键中 查找 与key相关的一系列键
	 * @param p 键值库
	 * @param key 键包含关键字
	 * @return 查找到的相关系列键
	 */
	private static String[] getMapContainKeys(Properties p,String key)
	{
		List<String> _list = new ArrayList<String>();
		
		Enumeration<?> e = p.keys();
		while(e.hasMoreElements()){
			String _key = (String)e.nextElement();
			if(_key.toLowerCase().contains(key)){
				_list.add(_key);
			}
		}
		
		return (String[])_list.toArray(new String[_list.size()]);
	}
	
	/**
	 * 从键值对应库中提取 给定键数组 对应的值
	 * @param p 键值对应对 库
	 * @param keys 键 数组
	 * @return 匹配到的值
	 */
	public static String findKeyValue(Properties p,String[] keys)
	{
		for(int i=0;i<keys.length;i++)
		{
			String[] _keys = getMapContainKeys(p,keys[i]);
			for(int j=0;j<_keys.length;j++)
			{
				String value = (String)p.get(_keys[j]);
				if(value != null){
					return value;
				}
			}
		}
		return null;
	}
	
	/**
	 * 从<code>src</code>中提取键值对应对关系 并 继续保存至传入的键值对应库中
	 * @param p 键值对应对 库
	 * @param src 部分html代码段
	 * @param segment 分隔html代码文章段块
	 * @param split 键值对应对分隔符
	 * @return 键值对应对 库
	 */
	private static Properties split(Properties p,String src,String segment,String split)
	{
		log.debug(src);
		
		String[] dest = src.split(segment);
		for(int i=0;i<dest.length;i++)
		{
			int index = dest[i].indexOf(split);
			if(index != -1)
			{
				String key = StringUtil.tidy(dest[i].substring(0,index));
				String value = StringUtil.tidy(dest[i].substring(index+1,dest[i].length()));
				
				key = StringUtil.cutPunctuation(key);
				value = StringUtil.cutPunctuation(value);
				
//				key = key.replaceAll(split, "");
				//修正key
				key = correctKey(key);
				
				//键 长度大于10的跳过
				if(key.length()>KEY_LENGTH){
					continue;
				}
				
				//值中 为空值 或 值中包含键 的跳过
				if(value.length()<=0 || p.containsKey(key)){
					continue;
				}
				
				//值中 还包含 键值对的 跳过（不让其在用户 输入的文章块中 作继续分析）
				if(value.contains(split)){
					continue;
				}
				
				log.info("KEY:"+key);
				
				p.put(key, value);
			}
		}
		
		return p;
	}
	
}
