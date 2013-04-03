package org.open.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.impl.dv.util.Base64;

// import sun.misc.BASE64Decoder;
// import sun.misc.BASE64Encoder;

/**
 * 提供各种编码解码算法类
 *
 * @author 覃芝鹏
 * @version $Id: CodeUtil.java,v 1.8 2009/07/08 06:59:54 moon Exp $
 */
public class CodeUtil {

    // 写日志.
    private static final Log log = LogFactory.getLog(CodeUtil.class);

    /**
     * 将BASE64编码的字符串进行解码
     */
    public static byte[] BASE64Decoder(String base64) {
        try {
            return (null == base64) ? null : Base64.decode(base64);
        } catch (Exception e) {
            log.error("BASE64Decoder(...) error!", e);
            return null;
        }
    }

    /**
     * 将data进行BASE64编码
     *
     * @param data byte[]
     * @return BASE64编码
     */
    public static String BASE64Encoder(byte[] data) {
        return Base64.encode(data);
    }

    /**
     * 将str进行BASE64编码
     */
    public static String BASE64Encoder(String str) {
        return (null == str) ? null : (Base64.encode(str.getBytes()));
    }

    public static String hex2string(String hex) {
        String[] data = hex.split(":");
        StringBuffer buf = new StringBuffer();
        for (String d : data) {
            buf.append((char) Integer.valueOf(d, 16).intValue());
        }
        return buf.toString();
    }

    public static boolean isURLCoder(String str) {
        return (str != null && str.matches("\\%(\\W){2}"));
    }

    public static byte[] md5(byte[] src) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        md.update(src);

        return md.digest();
    }

    /**
     * 采用md5方式加密输入的字符串
     *
     * @param src = 输入的需要加密的字符串
     * @return 返回md5加密过的32位字符串
     * @throws NoSuchAlgorithmException
     */
    public static String md5string2string(String src) {
        return (src == null) ? null : new BigInteger(1, md5(src.getBytes())).toString();
    }

    public static String md5byte2string(byte[] data) {
        return (data == null) ? null : new BigInteger(1, md5(data)).toString();
    }

    public static byte[] md5string2byte(String src) {
        return (src == null) ? null : md5(src.getBytes());
    }

    public static int md5string2int(String src) {
        return (src == null) ? null : new BigInteger(1, md5(src.getBytes())).intValue();
    }

    public static long md5string2long(String src) {
        return (src == null) ? null : new BigInteger(1, md5(src.getBytes())).longValue();
    }

    public static short md5string2short(String src) {
        return (src == null) ? null : new BigInteger(1, md5(src.getBytes())).shortValue();
    }

    public static String octal2string(String hex) {
        String[] data = hex.split(":");
        StringBuffer buf = new StringBuffer();
        for (String d : data) {
            buf.append((char) Integer.valueOf(d, 8).intValue());
        }
        return buf.toString();
    }

    public static String string2hex(String str) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            buf.append(Integer.toHexString((int) c)).append(':');
        }
        return buf.toString();
    }

    public static String string2octal(String str) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            buf.append(Integer.toOctalString((int) c)).append(':');
        }
        return buf.toString();
    }

    /**
     * @see java.net.URLDecoder#decode(String, String)
     * @return java.net.URLDecoder.decode(s,"utf-8");
     */
    public static String URLDecoder(String s) {
        try {
            return (null == s) ? null : java.net.URLDecoder.decode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("URLDecoder(" + s + ") error!", e);
            return s;
        }
    }

    /**
     * @see java.net.URLDecoder#decode(String, String)
     */
    public static String URLDecoder(String s, String charset) {
        try {
            return (null == s) ? null : java.net.URLDecoder.decode(s, charset);
        } catch (UnsupportedEncodingException e) {
            log.error("URLDecoder(" + s + "," + charset + ") error!", e);
            return s;
        }
    }

    /**
     * @see java.net.URLEncoder#encode(String, String)
     * @return java.net.URLEncoder.encode(s,"utf-8");
     */
    public static String URLEncoder(String s) {
        try {
            return (null == s) ? null : java.net.URLEncoder.encode(s, "utf-8");
        } catch (Exception e) {
            log.error("URLEncoder(" + s + ") error!", e);
            return s;
        }
    }

    /**
     * @see java.net.URLEncoder#encode(String, String)
     */
    public static String URLEncoder(String s, String charset) {
        try {
            return (null == s) ? null : java.net.URLEncoder.encode(s, charset);
        } catch (Exception e) {
            log.error("URLEncoder(" + s + "," + charset + ") error!", e);
            return s;
        }
    }

    /**
     * 非ASCII转换网页数字编码格式，格式 "&#3434;"
     *
     * @param str
     * @return
     */
    public static String non_ascii2number(String str) {
        if (null == str) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            int ascii = (int) charArray[i];
            // ASCII range
            if (127 >= ascii && ascii >= 0) {
                buf.append(charArray[i]);
            } else {
                buf.append("&#").append(ascii).append(";");
            }
        }
        return buf.toString();
    }

    /**
     * {@link #non_ascii2number(String)}反转函数
     *
     * @param str
     * @return
     */
    public static String non_ascii2number_back(String str) {
        if (null == str) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        StringBuilder number = new StringBuilder();
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            // if ‘&#’ beginning.
            if ((i + 1) < charArray.length && '&' == charArray[i] && '#' == charArray[i + 1]) {
                number.setLength(0);// clean previous value.
                // repeat number string.
                for (int j = i + 2; j < charArray.length; j++) {
                    // if number and append continue;
                    if ('0' <= charArray[j] && charArray[j] <= '9') {
                        number.append(charArray[j]);
                    }
                    // if end of number with ';',do convert to non_ascii char and append.
                    else if (';' == charArray[j]) {
                        // do like '&#number;'
                        if (number.length() != 0) {
                            char non_ascii;
                            try {
                                // do convert integer to char normally.
                                non_ascii = (char) (Integer.valueOf(number.toString()).intValue());
                                buf.append(non_ascii);
                            } catch (Exception e) {
                                // out of convert char integer. EXP:'&#1111111111;'.
                                buf.append("&#").append(number.toString()).append(";");
                            }
                        }
                        // do like '&#;'
                        else {
                            buf.append("&#;");
                        }
                        i = j;
                        break;
                    }
                    // if beginning with '&#' but not number follow it.
                    else {
                        buf.append("&#").append(number.toString());
                        i = j - 1;
                        break;
                    }
                }
            }
            // append redirect if non_ascii.
            else {
                buf.append(charArray[i]);
            }
        }
        return buf.toString();
    }

    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 根据时间产生UUID
     *
     * @param date
     * @return
     */
    public static String UUID(Date date) {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(date) + RandomUtil.nextInt(1000, 9999);
    }

}
