package org.open.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * List处理工具包类
 *
 * @author 覃芝鹏
 * @version $Id: ListUtil.java,v 1.5 2008/07/17 09:33:44 moon Exp $
 */
public class ArraysUtil {

    /**
     * 从字符串数组 构建 <code>List</code>对象
     *
     * @param list 字符串数组
     * @return 压入内容的<code>List</code>对象
     */
    public static List<String> build(String[] list) {
        List<String> _list = new ArrayList<String>();
        for (int i = 0; i < list.length; i++) {
            _list.add(list[i]);
        }
        return _list;
    }

    /**
     * 反转数组
     *
     * @param <T> 数组元素定义
     * @param arr 原始数组
     */
    public static <T> void reverse(T[] arr) {
        if (arr.length <= 0) {
            return;
        }
        int l = arr.length - 1;
        int s = arr.length / 2;
        for (int i = 0; i < s; i++) {
            T t = arr[i];
            arr[i] = arr[l - i];
            arr[l - i] = t;
        }
    }

    /**
     * 扩展了java.util.Arrays#sort(Object[])功能，并实现了类似“字符串+数字”组合串的排序，类似该串将以数字为主排序。 example:
     * t10,t5,t1,默认排序为t1,t10,t5;改进后排序为t1,t5,t10;(注意事项：本方法体会匹配组里面是否全部以"字符串+数字"组合， 如果一组不符合将不会采用数字优先排序，譬如序列中有t10t,)
     *
     * @see java.util.Arrays#sort(Object[])
     * @deprecated
     */
    public static void sort(String[] str) {
        Map<String, String> map = new Hashtable<String, String>();
        for (String r : str) {
            map.put(r, r);
        }
        Object[] keys = map.keySet().toArray();
        Arrays.sort(keys);
        for (int i = 0; i < str.length; i++) {
            str[i] = map.get(keys[i]);
        }

        String regexp = "^([[a-z]|[A-Z]]+?)([\\d]+?)$";
        for (int i = 0; i < str.length; i++) {
            if (!str[i].matches(regexp)) {
                return;
            }
        }

        // order by number.
        String[][] kv = new String[str.length][2];
        String k = null;
        int bi = 0;
        for (int i = 0; i < str.length; i++) {
            kv[i] = RegexpUtil.matchMultiGroup(str[i], regexp);
            if (k == null) {
                k = kv[i][0];
            }
            if (!k.equals(kv[i][0])) {
                for (int m = bi; m < i; m++) {
                    for (int n = m + 1; n < i; n++) {
                        if (Integer.valueOf(kv[m][1]).intValue() > Integer.valueOf(kv[n][1]).intValue()) {
                            String[] kvTmp = kv[m];
                            kv[m] = kv[n];
                            kv[n] = kvTmp;
                            String tmp = str[n];
                            str[n] = str[m];
                            str[m] = tmp;
                        }
                    }
                }
                bi = i;
                k = kv[i][0];
            }
        }

        // continue sort last group.
        for (int m = bi; m < str.length; m++) {
            for (int n = m + 1; n < str.length; n++) {
                if (Integer.valueOf(kv[m][1]).intValue() > Integer.valueOf(kv[n][1]).intValue()) {
                    String[] kvTmp = kv[m];
                    kv[m] = kv[n];
                    kv[n] = kvTmp;
                    String tmp = str[n];
                    str[n] = str[m];
                    str[m] = tmp;
                }
            }
        }
    }

    /**
     * 转换 对象数组 至 List队列
     *
     * @param <T> 原始对象
     * @param elements 对象队列
     * @return 装满对象的List
     */
    public static <T> List<T> toList(T[] elements) {
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < elements.length; i++) {
            list.add(elements[i]);
        }
        return list;
    }
}
