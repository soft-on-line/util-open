package org.open.util;

import org.open.lang.NumberOperator;

/**
 * 与数据函数想关的函数包
 * 
 * @author 覃芝鹏
 * @version $Id: MathUtil.java,v 1.2 2008/07/31 06:31:59 moon Exp $
 */
public class MathUtil {

    /**
     * 求组合值
     * 
     * @param m 表示元素个数
     * @param n 自由组合数
     * @return 返回可能的组合值
     */
    public static int combination(int m, int n) {
        if (m < n) {
            throw new IllegalArgumentException("Error   m   <   n");
        }
        int sum = 1;
        for (int i = m; i > m - n; i--) {
            sum *= i;
        }
        return sum / factorial(n);
    }

    /**
     * 求n的阶乘
     * 
     * @param n 数字
     * @return n的阶乘
     */
    public static int factorial(int n) {
        int sum = 1;
        for (int i = 0; i < n; i++) {
            sum *= (n - i);
        }
        return sum;
    }

    /**
     * 统计求和
     * 
     * @param array
     * @return
     */
    public static Number sum(Number[] array) {
        Number sum = 0;
        for (Number each : array) {
            sum = NumberOperator.add(sum, each);
        }
        return sum;
    }

    /**
     * 求平均数
     * 
     * @param array
     * @return
     */
    public static double average(Number[] array) {
        return NumberOperator.divide(sum(array), array.length).doubleValue();
    }

    /**
     * 求方差
     * 
     * @return
     */
    public static double variance(Number[] array) {
        double average = average(array);
        double sum = 0;
        for (Number each : array) {
            sum += Math.pow(average - each.doubleValue(), 2);
        }
        return sum / array.length;
    }

    /**
     * 求标准方差
     * 
     * @return
     */
    public static double standard_deviation(Number[] array) {
        return Math.sqrt(variance(array));
    }

    /**
     * 取一个数组中最小的值
     * 
     * @param array
     * @return
     */
    public static Number min(Number... array) {
        Number min = array[0];
        for (int i = 1; i < array.length; i++) {
            min = NumberOperator.min(array[i], min);
        }
        return min;
    }

    /**
     * 取一个数组中最大的值
     * 
     * @param array
     * @return
     */
    public static Number max(Number... array) {
        Number max = array[0];
        for (int i = 1; i < array.length; i++) {
            max = NumberOperator.max(array[i], max);
        }
        return max;
    }

}
