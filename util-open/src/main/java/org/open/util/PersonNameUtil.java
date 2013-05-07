package org.open.util;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
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

	public static char               CHINESE_NAME_CONNECTOR_OF_CHAR   = '·';

	public static String             CHINESE_NAME_CONNECTOR_OF_STRING = String.valueOf(CHINESE_NAME_CONNECTOR_OF_CHAR);

	public static char               ENGLISH_NAME_CONNECTOR_OF_CHAR   = '.';

	public static String             ENGLISH_NAME_CONNECTOR_OF_STRING = String.valueOf(ENGLISH_NAME_CONNECTOR_OF_CHAR);

	public static String             BAI_JIA_XING_2013                = "1陈2李3张4刘5王6杨7黄8孙9周10吴11徐12赵13朱14马15胡16郭17林18何19高20梁21郑22罗23宋24谢25唐26韩27曹28许29邓30萧31冯32曾33程34蔡35彭36潘37袁38于39董40余41苏42叶43吕44魏45蒋46田47杜48丁49沈50姜51范52江53傅54钟55卢56汪57戴58崔59任60陆61廖62姚63方64金65邱66夏67谭68韦69贾70邹71石72熊73孟74秦75阎76薛77侯78雷79白80龙81段82郝83孔84邵85史86毛87常88万89顾90赖91武92康93贺94严95尹96钱97施98牛99洪100龚101汤102陶103黎104温105莫106易107樊108乔109文110安111殷112颜113庄114章115鲁116倪117庞118邢119俞120翟121蓝122聂123齐124向125申126葛127柴128伍129覃130骆131关132焦133柳134欧135祝136纪137尚138毕139耿140芦141左142季143管144符145辛146苗147詹148曲149欧阳150靳151祁152路153涂154兰155甘156裴157梅158童159翁160霍161游162阮163尤164岳165柯166牟167滕168谷169舒170卜171成172饶173宁174凌175盛176查177单178冉179鲍180华181包182屈183房184喻185解186蒲187卫188简189时190连191车192项193闵194邬195吉196党197阳198司199费200蒙201席202晏203隋204古205强206穆207姬208宫209景210米211麦212谈213柏214瞿215艾216沙217鄢218桂219窦220郁221缪222畅223巩224卓225褚226栾227戚228全229娄230甄231郎232池233丛234边235岑236农237苟238迟239保240商241臧242佘243卞244虞245刁246冷247应248匡249栗250仇251练252楚253揭254师255官256佟257封258燕259桑260巫261敖262原263植264邝265仲266荆267储268宗269楼270干271苑272寇273盖274南275屠276鞠277荣278井279乐280银281奚282明283麻284雍285花286闻287冼288木289郜290廉291衣292蔺293和294冀295占296公297门298帅299利300满";

	public static String             BAI_JIA_XING_2012                = "1王2李3张4刘5陈6杨7黄8孙9周10吴11徐12赵13朱14马15胡16郭17林18何19高20梁21郑22罗23宋24谢25唐26韩27曹28许29邓30萧31冯32曾33程34蔡35彭36潘37袁38于39董40余41苏42叶43吕44魏45蒋46田47杜48丁49沈50姜51范52江53傅54钟55卢56汪57戴58崔59任60陆61廖62姚63方64金65邱66夏67谭68韦69贾70邹71石72熊73孟74秦75阎76薛77侯78雷79白80龙81段82郝83孔84邵85史86毛87常88万89顾90赖91武92康93贺94严95尹96钱97施98牛99洪100龚101汤102陶103黎104温105莫106易107樊108乔109文110安111殷112颜113庄114章115鲁116倪117庞118邢119俞120翟121蓝122聂123齐124向125申126葛127柴128伍129覃130骆131关132焦133柳134欧135祝136纪137尚138毕139耿140芦141左142季143管144符145辛146苗147詹148曲149欧阳150靳151祁152路153涂154兰155甘156裴157梅158童159翁160霍161游162阮163尤164岳165柯166牟167滕168谷169舒170卜171成172饶173宁174凌175盛176查177单178冉179鲍180华181包182屈183房184喻185解186蒲187卫188简189时190连191车192项193闵194邬195吉196党197阳198司199费200蒙201席202晏203隋204古205强206穆207姬208宫209景210米211麦212谈213柏214瞿215艾216沙217鄢218桂219窦220郁221缪222畅223巩224卓225褚226栾227戚228全229娄230甄231郎232池233丛234边235岑236农237苟238迟239保240商241臧242佘243卞244虞245刁246冷247应248匡249栗250仇251练252楚253揭254师255官256佟257封258燕259桑260巫261敖262原263植264邝265仲266荆267储268宗269楼270干271苑272寇273盖274南275屠276鞠277荣278井279乐280银281奚282明283麻284雍285花286闻287冼288木289郜290廉291衣292蔺293和294冀295占296公297门298帅299利300满";

	protected static HashSet<String> BAI_JIA_XING_INDEX               = new HashSet<String>();
	static {
		BAI_JIA_XING_INDEX.addAll(Arrays.asList(BAI_JIA_XING_2013.split("\\d+")));
		BAI_JIA_XING_INDEX.addAll(Arrays.asList(BAI_JIA_XING_2012.split("\\d+")));
		BAI_JIA_XING_INDEX.remove("");
	}

	public static String formatChineseName(String chineseName) {
		if (null == chineseName) {
			return null;
		}
		chineseName = chineseName.trim();
		//英文式简写下标换为中文式简写下标
		chineseName = chineseName.replaceAll("\\.", CHINESE_NAME_CONNECTOR_OF_STRING);

		StringBuilder buf = new StringBuilder(chineseName.length() + 5);
		char[] array = chineseName.toCharArray();
		for (int i = 0; i < array.length; i++) {
			char each = array[i];
			if (StringUtil.isLowerOrUpperCase(each)) {
				//约翰A.戴维斯 转换为 约翰·A·戴维斯
				if (buf.length() > 1 && !StringUtil.isLowerOrUpperCase(array[i - 1]) && buf.charAt(buf.length() - 1) != CHINESE_NAME_CONNECTOR_OF_CHAR) {
					buf.append(CHINESE_NAME_CONNECTOR_OF_CHAR);
				}

				//后置‘.’
				if ((i + 1) < array.length && !StringUtil.isLowerOrUpperCase(array[i + 1])) {
					//前一个是英文字母，即本身是单词一部分不转化大写
					if (i > 0 && StringUtil.isLowerOrUpperCase(array[i - 1])) {
						buf.append(String.valueOf(each));
					} else {
						buf.append(String.valueOf(each).toUpperCase());
					}

					if (array[i + 1] != CHINESE_NAME_CONNECTOR_OF_CHAR) {
						buf.append(CHINESE_NAME_CONNECTOR_OF_CHAR);
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
		englishName = englishName.replaceAll(CHINESE_NAME_CONNECTOR_OF_STRING, "\\.");

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
				buf.append(each.toUpperCase()).append(ENGLISH_NAME_CONNECTOR_OF_CHAR);
			} else if (each.contains(ENGLISH_NAME_CONNECTOR_OF_STRING)) {
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
			InputStream is = Thread.class.getResourceAsStream("/especial_pn.properties");
			if (null != is) {
				p.load(is);
				is.close();
			}
			if (null != Thread.currentThread().getClass().getClassLoader()) {
				is = Thread.currentThread().getClass().getClassLoader().getResourceAsStream("/especial_pn.properties");
				if (null != is) {
					p.load(is);
					is.close();
				}
			}
		}
		catch (Throwable e) {
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
	 * 是否包含百家姓的字
	 * @param name
	 * @return
	 */
	public static boolean containBaiJiaXing(String name) {
		if (null == name) {
			return false;
		}
		for (int i = 0; i < name.length(); i++) {
			boolean cotainBJX = BAI_JIA_XING_INDEX.contains(String.valueOf(name.charAt(i)));
			if (cotainBJX) {
				return true;
			}
		}
		return false;
	}

	/**
	 * name是否百家姓字
	 * @param name
	 * @return
	 */
	public static boolean containBaiJiaXing(char name) {
		return BAI_JIA_XING_INDEX.contains(String.valueOf(name));
	}

	/**
	 * 有左斜杠分割的特殊处理
	 * @param srcName
	 * @return
	 */
	private static List<PersonName> splitSmartlyWithZuoXieGang(String srcName) {
		List<PersonName> listPersonName = new ArrayList<PersonName>();
		String[] data = srcName.split("/");
		for (String name : data) {
			name = name.trim();
			int indexBlank = name.indexOf(' ');
			if (indexBlank < 0) {
				listPersonName.add(new PersonName(name, null));
			} else {
				String n1 = name.substring(0, indexBlank).trim();
				String n2 = name.substring(indexBlank + 1, name.length() - 1).trim();
				if (n1.length() <= 0 || n2.length() <= 0) {
					listPersonName.add(new PersonName(name, null));
				} else {
					//如果一个是中文，一个是英文名则拆分
					boolean n1_cc = StringUtil.containChinese(n1);
					boolean n2_cc = StringUtil.containChinese(n2);
					if (n1_cc && !n2_cc) {
						listPersonName.add(new PersonName(n1, n2));
					} else if (!n1_cc && n2_cc) {
						listPersonName.add(new PersonName(n2, n1));
					}
					//都是中文，则百家姓中找找，没有找到，则断定是外国人翻译中文名，中间空格用‘·’连接
					else if (n1_cc && n2_cc && (!containBaiJiaXing(n1.charAt(0)) || !containBaiJiaXing(n2.charAt(0))) && n2.split("\\s").length <= 2) {
						listPersonName.add(new PersonName(name.replaceAll("\\s", CHINESE_NAME_CONNECTOR_OF_STRING), null));
					} else {
						listPersonName.add(new PersonName(name, null));
					}
				}
			}
		}
		return listPersonName;
	}

	/**
	 * 智能分割名字
	 * @param srcName
	 * @return
	 */
	protected static List<PersonName> splitSmartly(String srcName) {
		//第一阶段：前期优化去杂处理
		srcName = srcName.replaceAll("、", " ");
		srcName = srcName.replaceAll("，", " ");
		srcName = srcName.replaceAll(",", " ");
		srcName = srcName.replaceAll(";", " ");
		srcName = srcName.replaceAll("；", " ");
		//		srcName = srcName.replaceAll("/", " ");
		srcName = srcName.replaceAll("\\(", " ");
		srcName = srcName.replaceAll("\\)", " ");
		srcName = srcName.replaceAll("（", " ");
		srcName = srcName.replaceAll("）", " ");
		srcName = srcName.replaceAll(StringUtil.SpaceStringB, " ");
		srcName = srcName.replaceAll(StringUtil.SpaceStringC, " ");
		srcName = srcName.replaceAll(StringUtil.SpaceStringD, " ");
		srcName = srcName.replaceAll("\\s{2,}", " ");

		StringBuilder buf = new StringBuilder(srcName);
		StringBuilder clone = new StringBuilder(srcName.length() + 5);
		for (int i = 0; i < buf.length(); i++) {
			char curChar = buf.charAt(i);
			//在范围‘A’-‘Z’内
			if ('A' < curChar && curChar < 'Z') {
				//前面是中文后面是英文则人为增加空格切分中英文混合的字符串
				if (i > 0 && i < buf.length() - 1 && StringUtil.isChinese(buf.charAt(i - 1)) && StringUtil.isLowerOrUpperCase(buf.charAt(i + 1))) {
					clone.append(" ");
				}
			}
			clone.append(curChar);
			//在范围‘a’-‘z’内
			if ('a' < curChar && curChar < 'z') {
				//前面是中文后面是英文则人为增加空格切分中英文混合的字符串
				if (i > 0 && i < buf.length() - 1 && StringUtil.isLowerOrUpperCase(buf.charAt(i - 1)) && StringUtil.isChinese(buf.charAt(i + 1))) {
					clone.append(" ");
				}
			}
		}

		srcName = clone.toString();

		if (srcName.indexOf('/') > 0) {
			return splitSmartlyWithZuoXieGang(srcName);
		}

		//第二阶段：空格分开
		String[] data = srcName.split("\\s");

		List<PersonName> personNames = new ArrayList<PersonName>();

		//双汉字构成的名字
		if (data.length == 2 && isSingleChinese(data[0]) && isSingleChinese(data[1])) {
			personNames.add(new PersonName(data[0] + data[1], null));

			return personNames;
		}

		List<String> names = new ArrayList<String>();
		StringBuilder englishName = new StringBuilder();
		for (String each : data) {
			//是中文名字就直接压入队列
			if (StringUtil.containChinese(each)) {
				//先判断如果上次的是英文且不为空则压入名字队列
				if (englishName.length() > 0) {
					names.add(englishName.toString());

					//重置为下一次缓存做准备
					englishName.setLength(0);
				}

				names.add(each);
			}
			//是英文的话就需要再判断后面的切割字符串，还是英文就需要拼接
			else {
				if (englishName.length() > 0) {
					englishName.append(" ");
				}
				englishName.append(each);
			}
		}

		//最后一组拼接
		if (englishName.length() > 0) {
			names.add(englishName.toString());

			englishName.setLength(0);
		}

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

	/**
	 * 根据传入的原始人名分析出中文名和英文名，下标0为中文名，下标1为英文名
	 * @param srcName
	 * @return
	 */
	public static List<PersonName> parse(String srcName) {
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

		personNames = splitSmartly(srcName);

		//判断本组分析是否有效，如果没有效或错的很离谱，直接返回空数组
		if (check(personNames)) {
			return personNames;
		}

		return new ArrayList<PersonName>();
	}

	/**
	 * 判断本组分析是否有效，中文名字 2-15字符；英文名字2（包含）-30字符
	 * @param personNames
	 * @return
	 */
	private static boolean check(List<PersonName> personNames) {
		for (PersonName pn : personNames) {
			if (pn.chineseName != null) {
				int pn_c_length = pn.chineseName.length();
				if (pn_c_length < 2 || pn_c_length > 15) {
					return false;
				}
			}
			if (pn.englishName != null) {
				int pn_e_length = pn.englishName.length();
				if (pn_e_length <= 2 || pn_e_length > 30) {
					return false;
				}
			}
		}
		return true;
	}

}
