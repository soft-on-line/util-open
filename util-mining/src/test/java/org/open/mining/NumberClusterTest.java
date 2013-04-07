package org.open.mining;

import junit.framework.TestCase;

import org.junit.Test;
import org.open.util.debug.DebugUtil;

public class NumberClusterTest extends TestCase {

    DebugUtil du = new DebugUtil(DebugUtil.InstanceModel.ConsoleModel);

    @Test
    public strictfp void testNumberCluster() {
//         Integer[] src = { -1, 199, 198, 45, 65, 0, -8, -9 };
//         Integer[] src = {199,198,197,196,195,194,193};
//         Integer[] src = {199,199,199,199,199,199,198};
        Double[] src = { -0.75, -0.75, -0.6666667, -0.6666667, -0.6666667, -0.5, -0.5, -0.5, -0.5, -0.46153846, -0.0,
                -0.0, -0.0, -0.0, -0.0, -0.0, -0.0, -0.0, -0.0, -0.0, -0.0, -0.0, -0.0, -0.0, -0.0, -0.0, -0.0, -0.0,
                -0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.3333334, 1.3333334, 1.3333334,
                1.3333334, 1.4444444, 1.5, 1.6666666, 2.0, 2.0, 2.0, 2.0, 2.0, 2.5, 2.6666667, 3.0, 3.6, 3.8333333,
                4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 5.0, 5.0, 5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0, 8.0, 8.0, 9.0, 10.0,
                10.0, 10.0, 14.0, 15.0, 15.25, 16.0, 19.0, 19.0, 24.0, 28.0, 29.0, 33.0, 36.419754, 36.419754,
                37.333332, 55.807693, 114.52, 164.66667, 319.33334, 350.6, 581.0, 903.0, 954.0, 1712.0, 1739.0 };

        // List<Double> src = new ArrayList<Double>();
        // double average = MathUtil.average(src1);
		// for(Double each : src1){
		// if(each>average){
		// src.add(each);
		// }
		// }

		//        for (int i = 0; i < src.length; i++) {
		//            if (src[i] >= 0) {
		//                src[i] = Math.pow(src[i], 1.0 / 5);
		//            } else {
		//                src[i] = -Math.pow(Math.abs(src[i]), 1.0 / 5);
		//            }
		//        }

		// double max = src[src.length-1];
		// for (int i = 0; i < src.length; i++) {
		// src[i] /= max;
		// }

		// du.print(src);

		NumberCluster numberCluster = new NumberCluster(src);

		du.print(numberCluster.getCluster());
		// du.print(numberCluster.getCluster()[2][0]);
	}
}
