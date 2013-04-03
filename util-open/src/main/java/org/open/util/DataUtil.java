package org.open.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 各种数据处理类
 *
 * @author moon
 * @version $Id: DataUtil.java,v 1.2 2009/06/08 09:18:09 moon Exp $
 */
public class DataUtil {

    private static final Log log = LogFactory.getLog(DataUtil.class);

    public static <T> String join(Collection<T> listId) {
        return join(listId, ",");
    }

    /**
     * 格式化数据堆栈
     *
     * @param listId 数据堆栈
     * @param split 数据分隔符
     * @return 用数据分隔符隔开的数据字符串
     */
    public static <T> String join(Collection<T> collection, String split) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }

        return join(collection.iterator(), split);
    }

    public static <T> String join(Iterator<T> iterator, String split) {
        if (null == iterator) {
            return "";
        }

        StringBuffer buf = new StringBuffer();
        while (iterator.hasNext()) {
            buf.append(iterator.next()).append(split);
        }
        if (buf.length() > 0) {
            buf.delete(buf.length() - split.length(), buf.length());
        }

        return buf.toString();
    }

    public static <T> String join(T[] listId) {
        return join(listId, ",");
    }

    /**
     * @param <T> 操作数据原始模型
     * @param listId 原始数据队列
     * @param split 组装分隔符
     * @return 用分隔符连接的拼装字符串
     */
    public static <T> String join(T[] listId, String split) {
        if (listId == null || listId.length == 0) {
            return "";
        }

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < listId.length - 1; i++) {
            buf.append(listId[i].toString()).append(split);
        }
        buf.append(listId[listId.length - 1].toString());

        return buf.toString();
    }

    /**
     * @param <T> 操作数据原始模型
     * @param data 原始数据队列
     * @param name 模型数据的反射取值
     * @param split 组装分隔符
     * @return 用分隔符连接的拼装字符串
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static <T> String join(Collection<T> data, String name, String split) {
        if (data == null || data.size() == 0) {
            return "";
        }

        return join(data.iterator(), name, split);
    }

    public static <T> String join(Iterator<T> data, String name, String split) {
        if (data == null) {
            return "";
        }

        try {
            StringBuffer buf = new StringBuffer();
            while (data.hasNext()) {
                buf.append(BeanUtils.getProperty(data.next(), name)).append(split);
            }
            if (buf.length() > 0) {
                buf.delete(buf.length() - split.length(), buf.length());
            }

            return buf.toString();
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);

            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            log.error(e.getMessage(), e);

            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(), e);

            throw new RuntimeException(e);
        }
    }

    /**
     * @param <T> 操作数据原始模型
     * @param data 原始数据队列
     * @param name 模型数据的反射取值
     * @param split 组装分隔符
     * @return 用分隔符连接的拼装字符串
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static <T> String join(T[] data, String name, String split) {
        if (data == null || data.length == 0) {
            return "";
        }

        try {
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < data.length - 1; i++) {
                buf.append(BeanUtils.getProperty(data[i], name)).append(split);
            }
            buf.append(BeanUtils.getProperty(data[data.length - 1], name));

            return buf.toString();
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);

            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            log.error(e.getMessage(), e);

            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(), e);

            throw new RuntimeException(e);
        }
    }

    /**
     * 对比2个byte数组对象是否相同
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean compare(byte[] a, byte[] b) {
        if (null == a && null == b) {
            return true;
        }
        if ((null == a && null != b) || (null == b && null != a)) {
            return false;
        }
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }
}
