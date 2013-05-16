package org.open.test;


import java.util.ArrayList;
import java.util.List;

public class SetOpt {

	public List<?> intersect(List<?> ls, List<?> ls2) {
		List<?> list = new ArrayList<Object>(ls);
		list.retainAll(ls2);
		return list;
	}

	public <T> List<?> union(List<T> ls, List<T> ls2) {
		List<T> list = new ArrayList<T>(ls);
		for (T obj : ls2) {
			if (!list.contains(obj)) {
				list.add(obj);
			}
		}
		return list;
	}

	public List<?> diff(List<?> ls, List<?> ls2) {
		List<?> list = new ArrayList<Object>(ls);
		list.removeAll(ls2);
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static void main(String[] args) {
		SetOpt opt = new SetOpt();
		List l1 = new ArrayList();
		l1.add(1);
		l1.add(2);
		l1.add(3);
		l1.add(4);
		List l2 = new ArrayList();
		l2.add(3);
		l2.add(4);
		l2.add(5);
		l2.add(6);

		System.out.println(l1);
		System.out.println(l2);

		List intersectList = opt.intersect(l1, l2);
		System.out.println("交集：" + intersectList);
		//		for (int i = 0; i < intersectList.size(); i++) {
		//			System.out.print(intersectList.get(i) + " ");
		//		}
		//		System.out.println();

		List unionList = opt.union(l1, l2);
		System.out.println("并集：" + unionList);
		//		for (int i = 0; i < unionList.size(); i++) {
		//			System.out.print(unionList.get(i) + " ");
		//		}
		//		System.out.println();

		List diffList = opt.diff(l1, l2);
		System.out.println("差集：" + diffList);

		diffList = opt.diff(l2, l1);
		System.out.println("差集：" + diffList);
		//		for (int i = 0; i < diffList.size(); i++) {
		//			System.out.print(diffList.get(i) + " ");
		//		}
		//		System.out.println();

		System.out.println(l1);
		System.out.println(l2);
	}

}