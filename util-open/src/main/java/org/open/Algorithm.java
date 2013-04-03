package org.open;

import java.util.BitSet;

import org.open.util.MathUtil;

/**
 * 各种各样算法计算公式类
 * 
 * @author 覃芝鹏
 * @version $Id: Algorithm.java,v 1.5 2008/07/31 06:19:31 moon Exp $
 */
public final class Algorithm {

    /**
     * 方差公式：sqrt(((a1-b1)^2+(a2-b2)^2+...+(an-bn)^2)/n)
     * 
     * @param a 第一组n维向量
     * @param b 第二组n维向量
     * @return double 类型方差值
     */
    public static double variance(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            double v = a[i] - b[i];
            sum += v * v;
        }
        return Math.sqrt(sum / a.length);
    }

    /**
     * 利用余弦定律判断两个向量的相似度，由于余玄取值在-1~1之间，1代表重合（即最接近）0代表没有直接关系，-1代表完全是相反关系。 两组n维向量是 a{a1, a2, a3, ..., an},b{b1, b2, b3,
     * ..., bn} 模： |a| = sqrt ( a1^2 + a2^2 + ... + an^2 ) 点乘： a*b = a1*b1 + a2*b2 + ...... + an*bn 相似度 = (a*b)
     * /(|a|*|b|) cos(A) = (a1*b1 + a2*b2 + ...... + an*bn)/(sqrt( a1^2 + a2^2 + ... + an^2 )*sqrt( b1^2 + b2^2 + ... +
     * bn^2 ))
     * 
     * @param a 第一组n维向量
     * @param b 第二组n维向量
     * @return cos(a,b);
     */
    public static double cos(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum / (mod(a) * mod(b));
    }

    /**
     * 求得n维向量数组 的中心向量 a[i] = (data[1][i] + data[2][i]+ ... data[n][i])/n;
     * 
     * @param data 原始n维向量数组
     * @return 中心向量
     */
    public static double[] average(double[][] data) {
        if (data.length < 0) {
            throw new RuntimeException("parameter data'length must biger than 0;");
        }
        double[] center = new double[data[0].length];
        for (int i = 0; i < center.length; i++) {
            double sum = 0.0;
            for (int j = 0; j < data.length; j++) {
                sum += data[j][i];
            }
            center[i] = sum / data.length;
        }
        return center;
    }

    /**
     * 由于余玄取值在-1~1之间,1代表重合（即最接近）,0代表没有直接关系,-1代表完全是相反关系（距离最远）。 便于人的正常判别距离之间的递增关系，用1-cos(a,b)调整取值区间在 0~2
     * 之间，同理0代表重合（即最接近）,1代表没有直接关系,2代表完全是相反关系（距离最远）。
     * 
     * @see #cos(double[], double[])
     * @param a 第一组n维向量
     * @param b 第二组n维向量
     * @return 1-cos(a,b);
     */
    public static double distanceCos(double[] a, double[] b) {
        return 1 - cos(a, b);
    }

    /**
     * @see #variance(double[], double[])
     * @param a 第一组n维向量
     * @param b 第二组n维向量
     * @return variance(a,b);
     */
    public static double distanceVariance(double[] a, double[] b) {
        return variance(a, b);
    }

    /**
     * 模： |a| = sqrt ( a1^2 + a2^2 + ... + an^2 )
     * 
     * @param a n维向量
     * @return double 类型向量模值
     */
    public static double mod(double[] a) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * a[i];
        }
        return Math.sqrt(sum);
    }

    /**
     * 自由组合函数：取得M的元素中任意取N的组合。
     * 
     * @param m m个元素
     * @param n 任意n的元素组合
     * @return m个元素n的自由组合
     */
    public static int[][] combination(int m, int n) {
        if (m < n) {
            throw new IllegalArgumentException("Error   m   <   n");
        }
        int[][] data = new int[MathUtil.combination(m, n)][n];
        int[] array = new int[m];
        for (int i = 0; i < m; i++)
            array[i] = i + 1;
        BitSet bs = new BitSet(m);
        for (int i = 0; i < n; i++) {
            bs.set(i, true);
        }
        int index = 0;
        do {
            put(data, index++, array, bs);
        } while (moveNext(bs, m));
        return data;
    }

    private static boolean moveNext(BitSet bs, int m) {
        int start = -1;
        while (start < m)
            if (bs.get(++start)) break;
        if (start >= m) return false;

        int end = start;
        while (end < m)
            if (!bs.get(++end)) break;
        if (end >= m) return false;
        for (int i = start; i < end; i++)
            bs.set(i, false);
        for (int i = 0; i < end - start - 1; i++)
            bs.set(i);
        bs.set(end);
        return true;
    }

    private static void put(int[][] data, int index, int[] array, BitSet bs) {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (bs.get(i)) {
                data[index][count++] = array[i];
            }
        }
    }
}
