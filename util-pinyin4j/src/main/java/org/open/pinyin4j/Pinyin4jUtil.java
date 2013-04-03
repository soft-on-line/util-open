package org.open.pinyin4j;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * pinyin4j utility class
 *
 * @author moon
 * @version $Id: Pinyin4jUtil.java,v 1.1 2009/05/09 04:20:10 moon Exp $
 */
public class Pinyin4jUtil {

    /**
     * get first pinyin of String.
     *
     * @param chinese String of chinese character array.
     * @return first char of Pinyin array.
     */
    public static Character getFirstPinyin(Character chinese) {
        if (null == chinese) {
            return null;
        }
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chinese);
        return (null == pinyinArray) ? chinese : pinyinArray[0].charAt(0);
    }

    public static String getFirstPinyin(char[] chinese) {
        if (null == chinese) {
            throw new NullPointerException("chinese is null!");
        }

        StringBuilder buf = new StringBuilder();
        for (char e : chinese) {
            buf.append(getFirstPinyin(e));
        }

        return buf.toString();
    }

    public static String getPinyin(String chinese) {
        return getPinyin(chinese, null);
    }

    /**
     * Notice: have a bug when Chinese have multi-Pinyin,default return the first Pinyin of the this Chinese.
     *
     * @param chinese String of Chinese character array.
     * @return a Chinese's Pinyin array.
     */
    public static String getPinyin(String chinese, String split) {
        if (null == chinese) {
            return null;
        }

        StringBuffer buf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c);
            if (pinyin == null) {
                buf.append(c);
            } else {
                buf.append(pinyin[0].substring(0, pinyin[0].length() - 1));
            }
            if (null != split && i < arr.length - 1) {
                buf.append(split);
            }
        }

        return buf.toString();
    }
}
