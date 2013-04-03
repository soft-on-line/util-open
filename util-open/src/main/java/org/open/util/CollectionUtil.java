package org.open.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CollectionUtil {

    /**
     * 交换空间a值和空间b值的List数组 例如：exchange(([2, 3, 1]),1,3)返回[1, 3, 2];
     *
     * @param list 原始List数组空间
     * @param a 需要交换定位a
     * @param b 需要交换定位b
     * @return 交换空间a值和空间b值的List数组
     */
    public static <T> void exchange(List<T> list, int indexA, int indexB) {
        indexA -= 1;
        indexB -= 1;

        if (indexA >= list.size() || indexB >= list.size() || indexA < 0 || indexB < 0 || indexA == indexB) {
            return;
        } else {
            T _a = list.get(indexA);
            T _b = list.get(indexB);
            list.remove(indexA);
            list.add(indexA, _b);
            list.remove(indexB);
            list.add(indexB, _a);
            return;
        }
    }

    /**
     * 前移或后移obj在list数组中的位置，direction大于0前移direction位，direction小于0后移direction位
     *
     * @param list
     * @param obj
     * @param direction
     */
    public static <T> void ranking(List<T> list, T obj, int direction) {
        int index = list.indexOf(obj) + 1;
        if (0 == index) {
            return;
        } else {
            exchange(list, index, -direction + index);
        }
    }

    /**
     * @see #ranking(List, Object, int)
     * @param <T>
     * @param list
     * @param obj
     * @param direction
     */
    public static <T> void ranking(Collection<T> list, T obj, int direction) {
        ranking(new ArrayList<T>(list), obj, direction);
    }

    /**
     * 反转排序后的队列
     *
     * @param list 原始队列
     */
    public static void reverse(List<?> list) {
        Collections.reverse(list);
    }

    public static <T> List<T> setToList(Set<T> set) {
        List<T> list = new ArrayList<T>();
        Iterator<T> e = set.iterator();
        while (e.hasNext()) {
            list.add(e.next());
        }
        return list;
    }
}
