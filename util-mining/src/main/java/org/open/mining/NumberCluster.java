package org.open.mining;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.open.util.MathUtil;

/**
 * 关于Number的聚类
 * 
 * @author Qin Zhipeng
 * @date 2010-09-20 17:01
 * @param <Number>
 */
public class NumberCluster {

    private Number[] array;

    public NumberCluster(Collection<? extends Number> array) {
        this.array = array.toArray(new Number[array.size()]);
    }

    public NumberCluster(Number[] array) {
        // clone array.
        this.array = Arrays.copyOf(array, array.length);
    }

    /**
     * default cluster 3 groups.
     * 
     * @return
     */
    public Number[][] getCluster() {
        // must sort array before cluster.
        Arrays.sort(this.array);

        // initialize 3 groups.
        LinkedList<Number> low = new LinkedList<Number>();
        LinkedList<Number> middle = new LinkedList<Number>();
        LinkedList<Number> high = new LinkedList<Number>();

        // add minimum number to low array.
        low.add(array[0]);
        // add maximum number to high array.
        high.add(array[array.length - 1]);

        // add average of array to middle array.
        double average = MathUtil.average(array);
        middle.add(average);

        // do each from 1 to (array.length-1).
        for (int i = 1; i < array.length - 1; i++) {
            // prepare put in low array.
            low.add(array[i]);
            double low_standard_deviation = MathUtil.standard_deviation(low.toArray(new Number[low.size()]));
            // prepare put in middle array.
            middle.add(array[i]);
            double middle_standard_deviation = MathUtil.standard_deviation(middle.toArray(new Number[middle.size()]));
            // prepare put in high array.
            high.add(array[i]);
            double high_standard_deviation = MathUtil.standard_deviation(high.toArray(new Number[high.size()]));
            // calculate best put in groups.
            double min_standard_deviation = MathUtil.min(low_standard_deviation, middle_standard_deviation,
                                                         high_standard_deviation).doubleValue();

            // and then do back with prepare data.
            if (min_standard_deviation == low_standard_deviation) {
                middle.remove(array[i]);
                high.remove(array[i]);
            } else if (min_standard_deviation == middle_standard_deviation) {
                low.remove(array[i]);
                high.remove(array[i]);
            } else if (min_standard_deviation == high_standard_deviation) {
                low.remove(array[i]);
                middle.remove(array[i]);
            } else {
                throw new RuntimeException("access error!");
            }
        }

        // last remove average value from arrays.
        middle.remove(average);

        // for output grouped data.
        Number[][] data = new Number[3][];

        data[0] = low.toArray(new Number[low.size()]);
        data[1] = middle.toArray(new Number[middle.size()]);
        data[2] = high.toArray(new Number[high.size()]);

        // last order by.
        Arrays.sort(data[0]);
        Arrays.sort(data[1]);
        Arrays.sort(data[2]);

        return data;
    }
}
