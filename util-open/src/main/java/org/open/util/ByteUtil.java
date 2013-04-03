package org.open.util;

/**
 * Byte处理工具包类
 *
 * @author 覃芝鹏
 * @version $Id: ByteUtil.java,v 1.1 2008/03/21 07:40:10 moon Exp $
 */
public class ByteUtil {

    /**
     * byte[] 数组扩容 每次 在原始大小基础上2倍扩容
     *
     * @param rece 原始byte[]数组队列
     * @return 扩容后的byte[]数组队列
     */
    public static byte[] arraycopy(byte rece[]) {
        byte temp[] = new byte[rece.length * 2];
        System.arraycopy(rece, 0, temp, 0, rece.length);
        return temp;
    }

    /**
     * To16进制字符串
     *
     * @param b
     * @return
     */
    public static String toHexString(byte[] b) {
        if (null == b || b.length <= 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hv = Integer.toHexString(b[i] & 0xFF);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
