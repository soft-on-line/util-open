package org.open.util;

public class CharUtil
{
	public static enum CharType {
		DELIMITER, //非字母截止字符，例如，．）（　等等　（ 包含U0000-U0080）
		NUM, //2字节数字１２３４
		LETTER, //gb2312中的，例如:ＡＢＣ，2字节字符 同时包含 1字节能表示的 basic latin and latin-1
		OTHER, // 其他字符
		CHINESE;//中文字
	}

	public static CharType checkType(char c) 
	{
		CharType ct = null;

		//中文，编码区间0x4e00-0x9fbb
		if ((c >= 0x4e00) && (c <= 0x9fbb)) {
			ct = CharType.CHINESE;
		}

		//Halfwidth and Fullwidth Forms， 编码区间0xff00-0xffef
		else if ((c >= 0xff00) && (c <= 0xffef)) {
			//        2字节英文字
			if (((c >= 0xff21) && (c <= 0xff3a))
					|| ((c >= 0xff41) && (c <= 0xff5a))) {
				ct = CharType.LETTER;
			}

			//2字节数字
			else if ((c >= 0xff10) && (c <= 0xff19)) {
				ct = CharType.NUM;
			}

			//其他字符，可以认为是标点符号
			else
				ct = CharType.DELIMITER;
		}

		//basic latin，编码区间 0000-007f
		else if ((c >= 0x0021) && (c <= 0x007e)) {
			//1字节数字
			if ((c >= 0x0030) && (c <= 0x0039)) {
				ct = CharType.NUM;
			}
			//1字节字符
			else if (((c >= 0x0041) && (c <= 0x005a))
					|| ((c >= 0x0061) && (c <= 0x007a))) {
				ct = CharType.LETTER;
			}
			//其他字符，可以认为是标点符号
			else
				ct = CharType.DELIMITER;
		}

		//latin-1，编码区间0080-00ff
		else if ((c >= 0x00a1) && (c <= 0x00ff)) {
			if ((c >= 0x00c0) && (c <= 0x00ff)) {
				ct = CharType.LETTER;
			} else
				ct = CharType.DELIMITER;
		} else
			ct = CharType.OTHER;

		return ct;
	}
	
	
	
}
