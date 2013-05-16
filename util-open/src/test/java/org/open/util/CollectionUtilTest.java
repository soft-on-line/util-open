package org.open.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class CollectionUtilTest {

	@Test
	public void testExchange() {
		// fail("Not yet implemented");
	}

	@Test
	public void testRanking() {
		// fail("Not yet implemented");
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);

		System.out.println(list);
		CollectionUtil.ranking(list, Integer.valueOf(3), -1);
		System.out.println(list);
	}

	@Test
	public void test() {
		long min = 0;
		long max = 50000;
		long count = 50000;
		List<Long> l1 = new ArrayList<Long>();
		while (l1.size() <= count) {
			l1.add(RandomUtil.nextLong(min, max));
		}

		List<Long> l2 = new ArrayList<Long>();
		while (l2.size() <= count) {
			l2.add(RandomUtil.nextLong(min, max));
		}

		//		System.out.println(l1);
		//		System.out.println(l2);

		Collection<?> intersectList = CollectionUtil.intersect(l1, l2);
		System.out.println("交集：" + intersectList.size());
		System.out.println("交集：" + intersectList);
		//		for (int i = 0; i < intersectList.size(); i++) {
		//			System.out.print(intersectList.get(i) + " ");
		//		}
		//		System.out.println();

		Collection<?> unionList = CollectionUtil.union(l1, l2);
		System.out.println("并集：" + unionList.size());
		System.out.println("并集：" + unionList);
		//		for (int i = 0; i < unionList.size(); i++) {
		//			System.out.print(unionList.get(i) + " ");
		//		}
		//		System.out.println();
	}

	@Test
	public void testDiff() {
		long min = 0;
		long max = 50000;
		long count = 100000;
		List<Long> l1 = new ArrayList<Long>();
		while (l1.size() <= count) {
			l1.add(RandomUtil.nextLong(min, max));
		}

		List<Long> l2 = new ArrayList<Long>();
		while (l2.size() <= count) {
			l2.add(RandomUtil.nextLong(min, max));
		}

		//		System.out.println(l1);
		//		System.out.println(l2);

		long st = new Date().getTime();
		Collection<Long> diffList = CollectionUtil.diff(l1, l2);
		System.out.println(DateUtil.convert(new Date().getTime() - st));
		System.out.println("差集：" + diffList.size());
		System.out.println("差集：" + diffList);

		st = new Date().getTime();
		diffList = CollectionUtil.diff(l2, l1);
		System.out.println(DateUtil.convert(new Date().getTime() - st));
		System.out.println("差集：" + diffList.size());
		System.out.println("差集：" + diffList);

		//		System.out.println(l1);
		//		System.out.println(l2);
	}

}
