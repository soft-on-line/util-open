package org.open.util.debug;


import java.util.Enumeration;
import java.util.Properties;

import org.open.util.DateUtil;
import org.open.util.FormatUtil;
import org.open.util.SystemUtil;

/**
 * 调试代码 工具包类
 * @author 覃芝鹏
 * @version $Id: DebugUtil.java,v 1.15 2008/10/17 07:26:59 moon Exp $
 */
public class DebugUtil {

	public static String print(double[] obj) {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		for (int i = 0; i < obj.length; i++) {
			buf.append(obj[i]).append(";");
		}
		buf.append("]");
		return buf.toString();
	}

	public static String print(double[][] obj) {
		return print(obj, 4);
	}

	public static String print(double[][] obj, int subString) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < obj.length; i++) {
			for (int j = 0; j < obj[i].length; j++) {
				String _subString = String.valueOf(obj[i][j]);
				if (_subString.length() > subString) {
					_subString = _subString.substring(0, subString);
				}
				buf.append(_subString);
				for (int k = 0; k < subString / 2; k++) {
					buf.append("\t");
				}
			}
			buf.append("\n");
		}
		return buf.toString();
	}

	public static String print(int[] obj) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < obj.length; j++) {
			buf.append(obj[j]).append("\t");
		}
		return buf.toString();
	}

	public static String print(int[][] obj) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < obj.length; i++) {
			for (int j = 0; j < obj[i].length; j++) {
				buf.append(obj[i][j]).append("\t");
			}
			buf.append("\n");
		}
		return buf.toString();
	}

	public static String print(Object obj) {
		return obj.toString();
	}

	public static String print(Object[] obj) {
		if (null == obj) {
			return null;
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < obj.length; i++) {
			buf.append(i);
			buf.append("=>");
			buf.append(obj[i]);
			buf.append("\r\n");
		}
		return buf.toString();
	}

	public static String print(Object[][] obj) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < obj.length; i++) {
			buf.append("\r\n");
			buf.append(i);
			buf.append("=>");
			for (int j = 0; j < obj[i].length; j++) {
				buf.append(obj[i][j] + "\t");
			}
		}
		return buf.toString();
	}

	@SuppressWarnings({ "rawtypes" })
	public static String print(Properties p) {
		int count = 0;
		Enumeration e = p.keys();
		StringBuffer buf = new StringBuffer();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = (String) p.get(key);
			buf.append(++count);
			buf.append("#");
			buf.append(key);
			buf.append("#:#");
			buf.append(value);
			buf.append("#");
			buf.append("\r\n");
		}
		return buf.toString();
	}

	public static String print(String[] str) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < str.length; i++) {
			buf.append(i);
			buf.append("=>");
			buf.append(str[i]);
			buf.append("\r\n");
		}
		return buf.toString();
	}

	public static String printFormatMemory(long m) {
		return print(FormatUtil.formatMeminfo(m));
	}

	public static String printMemoryInfo() {
		StringBuffer buf = new StringBuffer();
		buf.append("Used:");
		buf.append(FormatUtil.formatMeminfo(SystemUtil.memoryUsed()));
		buf.append(" Max:");
		buf.append(FormatUtil.formatMeminfo(SystemUtil.memoryMax()));
		buf.append(" Total:");
		buf.append(FormatUtil.formatMeminfo(SystemUtil.memoryTotal()));
		buf.append(" Free:");
		buf.append(FormatUtil.formatMeminfo(SystemUtil.memoryFree()));
		return buf.toString();
	}

	public static String printUsedTime(long time) {
		return print("Used Time:" + DateUtil.convert(time));
	}

}
