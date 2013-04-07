package org.open.util.debug;


import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.open.util.DateUtil;
import org.open.util.FormatUtil;
import org.open.util.SystemUtil;
import org.open.util.WriterUtil;

/**
 * 调试代码 工具包类
 * @author 覃芝鹏
 * @version $Id: DebugUtil.java,v 1.15 2008/10/17 07:26:59 moon Exp $
 */
public class DebugUtil {

	/**
	 * 辅助类，定义父类的各种不同实例化方式 枚举类型定义
	 * @author 覃芝鹏
	 * @version $Id: DebugUtil.java,v 1.15 2008/10/17 07:26:59 moon Exp $
	 */
	public static enum InstanceModel {
		ConsoleModel,

		FileModel,

		LogModel;
	}

	private static final String DebugFileName = "debug.txt";

	private static final Log    log           = LogFactory.getLog(DebugUtil.class);

	private InstanceModel       instanceModel;

	private static final String NullMessage   = "This's null.";

	public DebugUtil(InstanceModel instanceModel) {
		this.instanceModel = instanceModel;
		WriterUtil.delete(DebugFileName);
	}

	public void print(double[] obj) {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		for (int i = 0; i < obj.length; i++) {
			buf.append(obj[i]).append(";");
		}
		buf.append("]");
		run(buf.toString());
	}

	public void print(double[][] obj) {
		print(obj, 4);
	}

	public void print(double[][] obj, int subString) {
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
		run(buf.toString());
	}

	public void print(int[] obj) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < obj.length; j++) {
			buf.append(obj[j]).append("\t");
		}
		run(buf.toString());
	}

	public void print(int[][] obj) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < obj.length; i++) {
			for (int j = 0; j < obj[i].length; j++) {
				buf.append(obj[i][j]).append("\t");
			}
			buf.append("\n");
		}
		run(buf.toString());
	}

	public <T> void print(List<T> list) {
		// StringBuffer buf = new StringBuffer();
		// for(int i=0;i<list.size();i++)
		// {
		// buf.append(i);
		// buf.append("=>");
		// buf.append(list.get(i).toString());
		// }
		// run(buf.toString());
		run(list);
	}

	public void print(Object obj) {
		run(obj);
	}

	public void print(Object[] obj) {
		if (null == obj) {
			run(null);
		} else {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < obj.length; i++) {
				buf.append(i);
				buf.append("=>");
				buf.append(obj[i]);
				buf.append("\r\n");
			}
			run(buf.toString());
		}
	}

	public void print(Object[][] obj) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < obj.length; i++) {
			buf.append("\r\n");
			buf.append(i);
			buf.append("=>");
			for (int j = 0; j < obj[i].length; j++) {
				buf.append(obj[i][j] + "\t");
			}
		}
		run(buf.toString());
	}

	@SuppressWarnings({ "rawtypes" })
	public void print(Properties p) {
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
		run(buf.toString());
	}

	public void print(String[] str) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < str.length; i++) {
			buf.append(i);
			buf.append("=>");
			buf.append(str[i]);
			buf.append("\r\n");
		}
		run(buf.toString());
	}

	public void printFormatMemory(long m) {
		print(FormatUtil.formatMeminfo(m));
	}

	public void printMemoryInfo() {
		StringBuffer buf = new StringBuffer();
		buf.append("Used:");
		buf.append(FormatUtil.formatMeminfo(SystemUtil.memoryUsed()));
		buf.append(" Max:");
		buf.append(FormatUtil.formatMeminfo(SystemUtil.memoryMax()));
		buf.append(" Total:");
		buf.append(FormatUtil.formatMeminfo(SystemUtil.memoryTotal()));
		buf.append(" Free:");
		buf.append(FormatUtil.formatMeminfo(SystemUtil.memoryFree()));
		run(buf.toString());
	}

	public void printUsedTime(long time) {
		print("Used Time:" + DateUtil.convert(time));
	}

	private void run(Object obj) {
		String str = (obj == null) ? NullMessage : obj.toString();
		switch (instanceModel) {
			case LogModel:
				log.info(str);
				break;
			case ConsoleModel:
				System.out.println(str);
				break;
			case FileModel:
				WriterUtil.writeFile(DebugFileName, str, true);
				break;
			default:
				break;
		}
	}
}
