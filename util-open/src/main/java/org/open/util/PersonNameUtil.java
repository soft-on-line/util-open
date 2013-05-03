package org.open.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;

/**
 * 人名分析处理工具包类
 * @author zhipeng.qzp
 */
public class PersonNameUtil {

	/**
	 * 人名的内部类，包括中文名和英文名
	 * @author zhipeng.qzp
	 */
	public static class PersonName {

		public String chineseName;

		public String englishName;

		public PersonName() {

		}

		public PersonName(String chineseName, String englishName) {
			boolean cn_containChinese = StringUtil.containChinese(chineseName);
			boolean en_containChinese = StringUtil.containChinese(englishName);

			if (cn_containChinese) {
				this.chineseName = formatChineseName(chineseName);
			}

			if (!en_containChinese) {
				this.englishName = formatEnglishName(englishName);
			}

			if (null == this.chineseName && en_containChinese) {
				this.chineseName = formatChineseName(englishName);
			}

			if (null == this.englishName && !cn_containChinese) {
				this.englishName = formatEnglishName(chineseName);
			}
		}

		public String getChineseName() {
			return chineseName;
		}

		public void setChineseName(String chineseName) {
			this.chineseName = chineseName;
		}

		public String getEnglishName() {
			return englishName;
		}

		public void setEnglishName(String englishName) {
			this.englishName = englishName;
		}

		@Override
		public String toString() {
			return "PersonName [chineseName=" + chineseName + ", englishName=" + englishName + "]";
		}

	}

	public static String formatChineseName(String chineseName) {
		if (null == chineseName) {
			return null;
		}
		chineseName = chineseName.trim();
		//英文式简写下标换为中文式简写下标
		chineseName = chineseName.replaceAll("\\.", "·");

		StringBuilder buf = new StringBuilder(chineseName.length() + 5);
		char[] array = chineseName.toCharArray();
		for (int i = 0; i < array.length; i++) {
			char each = array[i];
			if (StringUtil.isLowerOrUpperCase(each)) {
				//约翰A.戴维斯 转换为 约翰·A·戴维斯
				if (buf.length() > 1 && !StringUtil.isLowerOrUpperCase(array[i - 1]) && buf.charAt(buf.length() - 1) != '·') {
					buf.append('·');
				}

				//后置‘.’
				if ((i + 1) < array.length && !StringUtil.isLowerOrUpperCase(array[i + 1])) {
					buf.append(String.valueOf(each).toUpperCase());

					if (array[i + 1] != '·') {
						buf.append("·");
					}
				} else {
					buf.append(each);
				}
			} else {
				buf.append(each);
			}
		}

		return buf.toString();
	}

	public static String formatEnglishName(String englishName) {
		if (null == englishName) {
			return null;
		}

		englishName = englishName.trim();

		//中文式简写下标换为英文式简写下标
		englishName = englishName.replaceAll("·", "\\.");

		//John A Davis 转换为 John A. Davis
		//		String[] onlyUpperCaseWords = RegexpUtil.matchGroups(englishName, " (\\w) ");
		//		for (String upperCase : onlyUpperCaseWords) {
		//			englishName = englishName.replaceAll(" " + upperCase + " ", " " + upperCase.toUpperCase() + ". ");
		//		}

		StringBuilder buf = new StringBuilder(englishName.length() + 5);
		String[] array = englishName.split("\\s");
		for (String each : array) {
			if (each.length() == 1) {
				//John A Davis 转换为 John A. Davis
				buf.append(each.toUpperCase()).append(".");
			} else if (each.contains(".")) {
				buf.append(StringUtil.upperCaseFirstLetterOnly(each));
			} else {
				buf.append(StringUtil.upperCaseFirstLetterOtherLowerCase(each));
			}
			buf.append(" ");
		}

		buf.deleteCharAt(buf.length() - 1);

		return buf.toString();
	}

	private static Map<String, List<PersonName>> ESPECIAL_LIB = new HashMap<String, List<PersonName>>();
	static {
		Gson gson = new Gson();
		Properties p = new Properties();
		try {
			p.load(Thread.class.getResourceAsStream("/especial_pn.properties"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Enumeration<Object> keys = p.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = (String) p.get(key);

			//key有空格时候修正特定转义字符‘#’为空格
			key = key.replaceAll("#", " ");

			ArrayList<PersonName> pns = new ArrayList<PersonName>();
			ArrayList<?> fromJson = gson.fromJson(value, pns.getClass());
			for (Object obj : fromJson) {
				PersonName pn = new PersonName();
				if (obj instanceof StringMap) {
					try {
						BeanUtils.copyProperties(pn, obj);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				pns.add(pn);
			}

			ESPECIAL_LIB.put(key.toString(), pns);
		}
	}

	/**
	 * 匹配特殊库人名
	 * @param srcName
	 * @return
	 */
	public static List<PersonName> parseEspecial(String srcName) {
		List<PersonName> personNames;

		personNames = ESPECIAL_LIB.get(srcName);
		if (null == personNames) {
			return new ArrayList<PersonName>();
		}

		return personNames;
	}

	/**
	 * 是否但汉字
	 * @param str
	 */
	private static boolean isSingleChinese(String str) {
		return (str != null && str.length() == 1 && StringUtil.containChinese(str));
	}

	/**
	 * 智能分割名字
	 * @param srcName
	 * @return
	 */
	private static List<String> splitSmartly(String srcName) {
		List<String> outName = new ArrayList<String>();

		String[] data = srcName.split("\\s");

		//双汉字构成的名字
		if (data.length == 2 && isSingleChinese(data[0]) && isSingleChinese(data[1])) {
			outName.add(data[0] + data[1]);

			return outName;
		}

		StringBuilder englishName = new StringBuilder();
		for (String each : data) {
			//是中文名字就直接压入队列
			if (StringUtil.containChinese(each)) {
				outName.add(each);

				//另外判断如果上次的是英文且不为空则压入名字队列
				if (englishName.length() > 0) {
					outName.add(englishName.toString());

					//重置为下一次缓存做准备
					englishName.setLength(0);
				}
			}
			//是英文的话就需要再判断后面的切割字符串，还是英文就需要拼接
			else {
				if (englishName.length() > 0) {
					englishName.append(" ");
				}
				englishName.append(each);
			}
		}

		if (englishName.length() > 0) {
			outName.add(englishName.toString());

			englishName.setLength(0);
		}

		return outName;
	}

	/**
	 * 根据传入的原始人名分析出中文名和英文名，下标0为中文名，下标1为英文名
	 * @param srcName
	 * @return
	 */
	public static List<PersonName> parse(String srcName) {
		if (null != srcName && srcName.toLowerCase().contains("mcg")) {
			System.out.println("Danny Cannon");
		}

		List<PersonName> personNames = parseEspecial(srcName);

		//特殊匹配成功则直接返回
		if (!personNames.isEmpty()) {
			return personNames;
		}

		if (null == srcName) {
			return personNames;
		}

		srcName = srcName.trim();

		if (srcName.isEmpty()) {
			return personNames;
		}

		//第一阶段：前期优化去杂处理
		srcName = srcName.replaceAll("、", " ");
		srcName = srcName.replaceAll("，", " ");
		srcName = srcName.replaceAll("/", " ");
		srcName = srcName.replaceAll("\\(", " ");
		srcName = srcName.replaceAll("\\)", " ");
		srcName = srcName.replaceAll("\\s{2,}", " ");

		List<String> names = splitSmartly(srcName);

		//刚好2个要判断是否是中文英文名一组的
		if (names.size() == 2) {
			if (StringUtil.containChinese(names.get(0)) && !StringUtil.containChinese(names.get(1))) {
				personNames.add(new PersonName(names.get(0), names.get(1)));
			} else if (StringUtil.containChinese(names.get(1)) && !StringUtil.containChinese(names.get(0))) {
				personNames.add(new PersonName(names.get(1), names.get(0)));
			}
			//都不是，还是分开来存
			else {
				personNames.add(new PersonName(names.get(0), null));
				personNames.add(new PersonName(names.get(1), null));
			}
		} else {
			for (String name : names) {
				personNames.add(new PersonName(name, null));
			}
		}

		return personNames;
	}

}
