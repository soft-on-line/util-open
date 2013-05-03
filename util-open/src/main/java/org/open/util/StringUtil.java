package org.open.util;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * String处理工具包类
 * @author 覃芝鹏
 * @version $Id: StringUtil.java,v 1.25 2009/07/07 03:59:57 moon Exp $
 */
public class StringUtil {

	/**
	 * @see TestFinalClass.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
	 */
	private final static Character.UnicodeBlock chinese      = Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;

	private static final Log                    log          = LogFactory.getLog(StringUtil.class);

	/**
	 * unicode 0x0020
	 */
	public static final char                    SpaceCharA   = ' ';

	/**
	 * unicode 0x3000
	 */
	// public static final char SpaceCharB = ' ';
	// public static final char SpaceCharB = (char)12288;
	public static final char                    SpaceCharB   = (char) Integer.valueOf("3000", 16).intValue();

	/**
	 * unicode 0xe525
	 */
	// public static final char SpaceCharC = '';
	// public static final char SpaceCharC = (char)58661;
	public static final char                    SpaceCharC   = (char) Integer.valueOf("e525", 16).intValue();

	/**
	 * unicode 0xe5f1
	 */
	// public static final char SpaceCharD = '';
	// public static final char SpaceCharD = (char)58865;
	public static final char                    SpaceCharD   = (char) Integer.valueOf("e5f1", 16).intValue();

	/**
	 * String of unicode char 0x0020
	 */
	public static final String                  SpaceStringA = String.valueOf(SpaceCharA);

	/**
	 * String of unicode char 0x3000
	 */
	public static final String                  SpaceStringB = String.valueOf(SpaceCharB);

	/**
	 * String of unicode char 0xe525
	 */
	public static final String                  SpaceStringC = String.valueOf(SpaceCharC);

	/**
	 * String of unicode char 0xe5f1
	 */
	public static final String                  SpaceStringD = String.valueOf(SpaceCharD);

	/**
	 * [辅助方法体] 查询 原始字符串中 关键字 有多少处
	 * @param str 原始字符串
	 * @param index 查询关键字
	 * @param count 上次查找到 多少 处
	 * @return 查找到 多少 处
	 */
	private static int _findCount(String str, String index, int count) {
		int position = str.indexOf(index);
		if (-1 != position) {
			return _findCount(str.substring(position + index.length()), index, count + 1);
		}

		return count;
	}

	/**
	 * @param str 原型字符串
	 * @param repeat 重复次数
	 * @return 新建的字符串
	 */
	public static String build(String str, int repeat) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < repeat; i++) {
			buf.append(str);
		}
		return buf.toString();
	}

	/**
	 * 用分隔符连结字符串数组
	 * @param str String字符串数组
	 * @param separator 分隔符
	 * @return 连结后的字符串数组
	 */
	public static String concat(String[] str, String separator) {
		StringBuffer buf = new StringBuffer();
		if (str.length == 1) {
			return str[0];
		} else if (str.length > 1) {
			for (int i = 0; i < str.length - 1; i++) {
				buf.append(str[i]).append(separator);
			}
			buf.append(str[str.length - 1]).append(separator);
		}
		return buf.toString();
	}

	/**
	 * 判断一个字符串是否包含中文
	 * @param str
	 * @return
	 */
	public static boolean containChinese(String str) {
		if (null == str || str.isEmpty()) {
			return false;
		}
		char[] chars = str.toCharArray();
		for (char c : chars) {
			boolean isChinese = isChinese(c);
			if (isChinese) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否包含字母
	 * @param str
	 * @return
	 */
	public static boolean containLetter(String str) {
		if (null == str || str.isEmpty()) {
			return false;
		}
		char[] chars = str.toCharArray();
		for (char c : chars) {
			boolean isLetter = isLetter(c);
			if (isLetter) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断一个字符串是否包含字母（大写或小写）
	 * @param str
	 * @return
	 */
	public static boolean containLowerOrUpperCase(String str) {
		if (null == str || str.isEmpty()) {
			return false;
		}
		char[] chars = str.toCharArray();
		for (char c : chars) {
			boolean isLowOrUpperCase = isLowerOrUpperCase(c);
			if (isLowOrUpperCase) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see #convert(String, String)
	 */
	public static List<Integer> convert(String ids) {
		return convert(ids, ",");
	}

	/**
	 * @see #convert(String, String, Class)
	 */
	public static <T> List<T> convert(String ids, Class<T> clazz) {
		return convert(ids, ",", clazz);
	}

	/**
	 * 将以分隔符分开的Integer转换为list,默认分隔符为','
	 * @param ids
	 * @param split
	 * @return
	 */
	public static List<Integer> convert(String ids, String split) {
		List<Integer> listId = new ArrayList<Integer>();
		if (StringUtil.isEmpty(ids)) {
			return listId;
		}

		String[] arrayId = ids.split(split);
		for (int i = 0; i < arrayId.length; i++) {
			if (StringUtil.isEmpty(arrayId[i])) {
				continue;
			}
			listId.add(Integer.valueOf(arrayId[i]));
		}
		return listId;
	}

	/**
	 * 将以分隔符分开的<T>转换为list<T>,默认分隔符为','
	 * @param ids
	 * @param split
	 * @return
	 */
	public static <T> List<T> convert(String ids, String split, Class<T> clazz) {
		List<T> listId = new ArrayList<T>();
		if (StringUtil.isEmpty(ids)) {
			return listId;
		}

		String[] arrayId = ids.split(split);
		for (int i = 0; i < arrayId.length; i++) {
			if (StringUtil.isEmpty(arrayId[i])) {
				continue;
			}
			listId.add(clazz.cast(arrayId[i]));
		}
		return listId;
	}

	/**
	 * 处理html中的 ‘&nbsp;’、‘&amp;’等等
	 * @param str 需要处理的HMTL
	 * @return 处理后的HTML
	 */
	public static String cutHtmlTag(String str) {
		return (str == null) ? null : str.replaceAll("&.{1,4};", "").trim();
	}

	/**
	 * 清除词两边的标点符号
	 * @param word 原始词
	 * @return 处理后的词
	 */
	public static String cutPunctuation(String word) {
		if (word.length() < 1) {
			return word;
		}

		char c = word.charAt(0);
		if (isPunctuation(c)) {
			// 去掉末尾单个字符
			word = word.substring(1, word.length());
			// 递归遍历
			return cutPunctuation(word);
		}

		c = word.charAt(word.length() - 1);
		if (isPunctuation(c)) {
			// 去掉开始单个字符
			word = word.substring(0, word.length() - 1);
			// 递归遍历
			return cutPunctuation(word);
		}

		return word;
	}

	/**
	 * 一个字符串减去cut字符串后的新字符串
	 * @param str 原始字符串
	 * @param cut 需要剪切的字符串
	 * @return 剪切后的字符串
	 */
	public static String cutString(String str, String cut) {
		if (isEmpty(cut)) {
			return str;
		}
		int begin = str.indexOf(cut);
		int length = cut.length();
		if (-1 != begin) {
			do {
				str = str.substring(0, begin) + str.substring(begin + length);
			} while (-1 != (begin = str.indexOf(cut)));
		}

		return str;
	}

	/**
	 * @param str 原始字符串
	 * @param fromCharsetName 原始字符串编码
	 * @param toCharsetName 转换字符串编码
	 * @return 转换后的字符串
	 */
	public static String encode(String str, String fromCharsetName, String toCharsetName) {
		try {
			if (str == null) {
				return null;
			}
			return new String(str.getBytes(fromCharsetName), toCharsetName);
		}
		catch (UnsupportedEncodingException e) {
			log.error("encode(" + str + "," + fromCharsetName + "," + toCharsetName + ") error!\n", e);
			return str;
		}
	}

	/**
	 * 原始iso-8859-1字符串转换utf-8字符串。
	 * @param str 原始字符串
	 * @return 转换utf-8后的字符串
	 */
	public static String encodeIsoToGBK(String str) {
		return encode(str, "iso-8859-1", "gbk");
	}

	/**
	 * 原始iso-8859-1字符串转换utf-8字符串。
	 * @param str 原始字符串
	 * @return 转换utf-8后的字符串
	 */
	public static String encodeIsoToUtf8(String str) {
		return encode(str, "iso-8859-1", "utf-8");
	}

	/**
	 * 查询 原始字符串中 关键字 有多少处
	 * @param str 原始字符串
	 * @param keyword 查询关键字
	 * @return 查找到 多少 处
	 */
	public static int findCount(String str, String keyword) {
		return _findCount(str, keyword, 0);
	}

	/**
	 * 替换字符串中的回车为&lt;/br&gt;
	 * @param str 原始字符串
	 * @return 遵循html的字符串
	 */
	public static String formatToHtml(String str) {
		if (str == null) {
			return null;
		}
		str = str.replaceAll("\\r\\n", "<br>");
		str = str.replaceAll("\\r", "<br>");
		str = str.replaceAll("\\n", "<br>");
		return str;
	}

	/**
	 * 按搜索的关键字 segment分法 高亮显示定位maxTxtLength长度字段
	 * @param original 原始字符串
	 * @param keyword 查询关键字
	 * @param segment 切分segment段
	 * @param stringMaxLength 处理后的字符串最大长度
	 * @return 加亮后的字符串（html 加修饰字符串加红色）;
	 */
	public static String fragment(String original, String keyword, int segment, int stringMaxLength) {
		// String replace = "<font color='red'>"+keyword+"</font>";

		if (original.length() <= stringMaxLength) {
			// return original.replaceAll(keyword,replace);
			return original;
		}

		int count = findCount(original, keyword);

		// 句子中没有找到 关键词
		if (count == 0) {
			return original.substring(0, Math.min(original.length(), stringMaxLength));
		}

		int _segment = Math.min(count, segment);
		int _sect = stringMaxLength / _segment / 2;
		String _original = "";
		for (int i = 0; i < _segment; i++) {
			int index = original.indexOf(keyword);

			String _tmp = original.substring(Math.max(0, index - _sect), Math.min(index + _sect, original.length()));
			// _original += _tmp.replaceAll(keyword,replace)+"......";
			_original += _tmp + "...";

			original = original.substring(Math.min(index + _sect, original.length()));
		}

		return _original;
	}

	/**
	 * 获取字符串2中包含字符串1的最大连续长度
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static int getSameCharSum(String str1, String str2) {
		int sum = 0;
		for (int i = 1; i < str1.length() && i < str2.length(); i++) {
			if (str2.indexOf(str1.substring(0, i)) != -1) {
				sum = i;
			}
		}
		return sum;
	}

	/**
	 * 判断文字中是否还有中文
	 * @param str
	 * @return
	 */
	public static boolean haveChinese(String str) {
		if (str == null) {
			return false;
		}
		char[] arr = str.toCharArray();
		for (char c : arr) {
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串中是否有乱码
	 * @param str 字符串
	 * @return 是否为乱码
	 * @deprecated 不能判断为乱码
	 */
	public static boolean haveMessyCode(String str) {
		for (int i = 0; i < str.length(); i++) {
			String h = Integer.toHexString(str.charAt(i));
			h += build("0", 4 - h.length());
			if (h.compareTo("4e00") < 0 || h.compareTo("9fa5") > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断一个字符串中各个字符是否被包含在原始字符串里面
	 * @param origin 原始字符串
	 * @param sub 需要判断的字符串
	 * @return true包含，false不包含
	 */
	public static boolean ifContain(String origin, String sub) {
		if (origin == null || sub == null || sub.length() > origin.length()) {
			return false;
		}
		char[] arr = sub.toCharArray();
		for (char c : arr) {
			if (!origin.contains(String.valueOf(c))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @see #isEmpty(String)
	 */
	public static boolean isBlank(String str) {
		return ((null == str) || (str.trim().length() == 0)) ? true : false;
	}

	/**
	 * 判断一个char是否为中文。
	 * @param c 字符
	 * @return 是否为中文
	 */
	public static boolean isChinese(char c) {
		return (chinese == Character.UnicodeBlock.of(c));
	}

	/**
	 * 判断全为数字
	 * @param str
	 * @return
	 */
	public static boolean isDigit(String str) {
		return (str != null && str.matches("^(\\d)+$"));
	}

	/**
	 * 判断字符串是否为空。 null || 长度为0.
	 * @param str 输入字符串
	 * @return true空，false非空
	 */
	public static boolean isEmpty(String str) {
		return isBlank(str);
	}

	/**
	 * 判断字符串是否是整数
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 是否字母
	 * @param c
	 * @return
	 */
	public static boolean isLetter(char c) {
		return Character.isLetter(c);
	}

	/**
	 * 判断为数字或者字母
	 * @param c 字符
	 * @return 是否为数字或者字母
	 */
	public static boolean isLetterOrDigit(char c) {
		return Character.isLetterOrDigit(c);
	}

	/**
	 * 是否小写字母
	 * @param c
	 * @return
	 */
	public static boolean isLowerCase(char c) {
		return Character.isLowerCase(c);
	}

	/**
	 * 是否英文字母（大写或小写）
	 * @param c
	 * @return
	 */
	public static boolean isLowerOrUpperCase(char c) {
		return Character.isLowerCase(c) || Character.isUpperCase(c);
	}

	/**
	 * 判断为标点符号
	 * @param c 字符
	 * @return 是否为标点符号
	 */
	public static boolean isPunctuation(char c) {
		return isChinese(c) ? false : isLetterOrDigit(c) ? false : true;
	}

	/**
	 * 是否大写字母
	 * @param c
	 * @return
	 */
	public static boolean isUpperCase(char c) {
		return Character.isUpperCase(c);
	}

	/**
	 * 判断一个字符是否为空白字符（包括中文空白字符 和 英文空白字符）
	 * @param c 原始字符
	 * @return ture是空白字符，false不是空白字符。
	 */
	public static boolean isWhitespace(char c) {
		return (c == SpaceCharA || c == SpaceCharB || c == SpaceCharC || c == SpaceCharD);
	}

	/**
	 * 格式化数据堆栈
	 * @param listId 数据堆栈
	 * @param split 数据分隔符
	 * @return 用数据分隔符隔开的数据字符串
	 */
	public static String join(List<String> listStr, String split) {
		if (listStr == null || listStr.isEmpty()) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < listStr.size() - 1; i++) {
			buf.append(listStr.get(i)).append(split);
		}
		buf.append(listStr.get(listStr.size() - 1));

		return buf.toString();
	}

	/**
	 * 用分隔符连接的字符串
	 * @param arr 字符串队列
	 * @param separator 分隔符
	 * @return 返回用分隔符连接的字符串
	 */
	public static String join(String[] listStr, String split) {
		if (listStr == null || listStr.length == 0) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < listStr.length - 1; i++) {
			buf.append(listStr[i]).append(split);
		}
		buf.append(listStr[listStr.length - 1]);

		return buf.toString();
	}

	/**
	 * 用分隔符连接的字符串
	 * @param listStr 字符串队列
	 * @param regexp
	 * @param replacement
	 * @param split 分隔符
	 * @return 返回用分隔符连接的字符串
	 */
	public static String joinWithReplaceAll(List<String> listStr, String regexp, String replacement, String split) {
		if (listStr == null || listStr.isEmpty()) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < listStr.size() - 1; i++) {
			buf.append(listStr.get(i).replaceAll(regexp, replacement)).append(split);
		}
		buf.append(listStr.get(listStr.size() - 1));
		return buf.toString();
	}

	/**
	 * 用分隔符连接的字符串
	 * @param listStr 字符串队列
	 * @param regexp
	 * @param replacement
	 * @param split 分隔符
	 * @return 返回用分隔符连接的字符串
	 */
	public static String joinWithReplaceAll(String[] listStr, String regexp, String replacement, String split) {
		if (listStr == null || listStr.length == 0) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < listStr.length - 1; i++) {
			buf.append(listStr[i].replaceAll(regexp, replacement)).append(split);
		}
		buf.append(listStr[listStr.length - 1]);

		return buf.toString();
	}

	/**
	 * 字符串数值+1
	 * @param str 字符串数值串
	 * @return 加1后的字符串
	 */
	public static String plus(String str) {
		int length = str.length();
		str = String.valueOf(Integer.valueOf(str) + 1);
		if (length == str.length()) {
			return str;
		} else {
			return build("0", length - str.length()) + str;
		}
	}

	/**
	 * 替换字符串中的片段，并且兼容正则表达式关键字。
	 * @param original 原始字符串
	 * @param oldSeg 需要替换的字符串
	 * @param newSeg 替换的字符串
	 * @return 替换后的字符串
	 */
	public static String replaceAllContainRegexp(String original, String oldSeg, String newSeg) {
		if ((null == original) || (null == oldSeg) || (null == newSeg)) {
			return original;
		}

		StringBuffer buf = new StringBuffer(original);

		int index = buf.indexOf(oldSeg);
		while (-1 != index) {
			buf.replace(index, index + oldSeg.length(), newSeg);

			index = buf.indexOf(oldSeg);
		}

		return buf.toString();
	}

	/**
	 * 全角(SBC)转换至半角(DBC)
	 * @param text 原始文本
	 * @return 正常文本(半角)
	 */
	public static final String SBC2DBC(String text) {
		String outStr = "";
		String Tstr = "";
		byte[] b = null;

		for (int i = 0; i < text.length(); i++) {
			try {
				Tstr = text.substring(i, i + 1);
				b = Tstr.getBytes("unicode");
			}
			catch (java.io.UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (b[3] == -1) {
				b[2] = (byte) (b[2] + 32);
				b[3] = 0;

				try {
					outStr = outStr + new String(b, "unicode");
				}
				catch (java.io.UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else
				outStr = outStr + Tstr;
		}

		return outStr;
	}

	/**
	 * 取字符串str的index后字符串
	 * @param str
	 * @param index
	 * @return
	 */
	public static String substringAfter(String str, int index) {
		return (null == str) ? null : ((str.length() < index) ? str : str.substring(index));
	}

	/**
	 * 取字符串str的前index字符串
	 * @param str
	 * @param index
	 * @return
	 */
	public static String substringBefore(String str, int index) {
		return (null == str) ? null : ((str.length() < index) ? str : str.substring(0, index));
	}

	/**
	 * 清理字符串中连续的多个空格 为一个空格
	 * @param str 需要处理的字符串
	 * @return 处理后的字符串
	 */
	public static String tidy(String str) {
		return (str == null) ? null : new String(str.replaceAll("\\s{2,}", " ")).trim();
	}

	/**
	 * 变 "This is my code." 为 "ThisIsMyCode".
	 * @param line
	 * @return
	 */
	public static String tidyContractEnglish(String line) {
		if (StringUtil.isEmpty(line)) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		line = line.trim();
		line = line.toLowerCase();
		char[] c = line.toCharArray();
		boolean isUpper = false;
		buf.append(String.valueOf(c[0]).toUpperCase());
		for (int i = 1; i < c.length; i++) {
			if (StringUtil.isBlank(String.valueOf(c[i]))) {
				isUpper = true;
				continue;
			}

			if (isUpper) {
				buf.append(String.valueOf(c[i]).toUpperCase());
			} else {
				buf.append(c[i]);
			}

			isUpper = false;
		}
		return buf.toString();
	}

	/**
	 * 严格整理字符串 清空里面包含的三种特殊空格字符(c==' ' || c==' ' || c=='' || c=='')
	 * @param str 需要处理的字符串
	 * @return 处理后的字符串
	 */
	public static String tidyStrictly(String str) {
		if (str == null) {
			return null;
		}

		if (-1 != str.indexOf(SpaceCharD)) {
			str = str.replaceAll(SpaceStringD, SpaceStringA);
		}

		if (-1 != str.indexOf(SpaceCharC)) {
			str = str.replaceAll(SpaceStringC, SpaceStringA);
		}

		if (-1 != str.indexOf(SpaceCharB)) {
			str = str.replaceAll(SpaceStringB, SpaceStringA);
		}

		return tidy(str);
	}

	/**
	 * iso-8859-1字符串转换
	 * @param str
	 * @return
	 */
	public static String toISO_8859_1(String str) {
		try {
			return new String(str.getBytes("iso-8859-1"));
		}
		catch (UnsupportedEncodingException e) {
			log.error("StringUtil toISO_8859_1(" + str + ") error!\n", e);
			return null;
		}
	}

	/**
	 * 取str的前length长度字符串
	 * @param str 原始字符串
	 * @param length 取前length长度
	 * @return str的前length长度字符串
	 */
	public static String topString(String str, int length) {
		if (str == null || str.length() < length) {
			return str;
		} else {
			return str.substring(0, length) + "...";
		}
	}

	/**
	 * 取str的前length长度字符串(跳过xml格式处理)
	 * @param str 原始字符串
	 * @param length 取前length长度
	 * @return str的前length长度字符串
	 */
	public static String topStringEscapeXml(String str, int length) {
		StringBuffer buf = new StringBuffer();
		if (str == null) {
			return str;
		} else {
			boolean flag = false;
			char[] tmp = str.toCharArray();
			for (int i = 0; i < tmp.length && i < length; i++) {
				if (tmp[i] == '<') {
					flag = true;
				} else if (tmp[i] == '>') {
					flag = false;
				}
				if (flag) {
					length += 1;
				}
				buf.append(tmp[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * 仅仅大写第一个字母
	 * @param str
	 * @return
	 */
	public static String upperCaseFirstLetterOnly(String str) {
		if (null == str || str.length() <= 0) {
			return str;
		}

		StringBuilder buf = new StringBuilder(str.length());

		char[] array = str.toCharArray();

		buf.append(String.valueOf(array[0]).toUpperCase());

		for (int i = 1; i < array.length; i++) {
			buf.append(array[i]);
		}

		return buf.toString();
	}

	/**
	 * 大写第一个字母小写其他字母
	 * @param str
	 * @return
	 */
	public static String upperCaseFirstLetterOtherLowerCase(String str) {
		if (null == str || str.length() <= 0) {
			return str;
		}

		StringBuilder buf = new StringBuilder(str.length());

		char[] array = str.toCharArray();

		buf.append(String.valueOf(array[0]).toUpperCase());

		for (int i = 1; i < array.length; i++) {
			buf.append(String.valueOf(array[i]).toLowerCase());
		}

		return buf.toString();
	}

}
