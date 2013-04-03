package org.open.lang;

import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class TreeTest {

    @Test
    public void test() {
        SortableTree<String> root = new SortableTree<String>("菜单列表");
        SortableTree<String> f1 = new SortableTree<String>("开始菜单");
        root.addChild(f1);
        SortableTree<String> f1_1 = new SortableTree<String>("程序");
        f1.addChild(f1_1);
        SortableTree<String> f1_1_1 = new SortableTree<String>("附件");
        f1_1.addChild(f1_1_1);
        SortableTree<String> f1_1_1_1 = new SortableTree<String>("娱乐1");
        f1_1_1.addChild(f1_1_1_1);
        SortableTree<String> f1_1_1_3 = new SortableTree<String>("娱乐3");
        f1_1_1.addChild(f1_1_1_3);
        SortableTree<String> f1_1_1_2 = new SortableTree<String>("娱乐2");
        f1_1_1.addChild(f1_1_1_2);
        SortableTree<String> f1_2 = new SortableTree<String>("辅助工具");
        f1.addChild(f1_2);
        SortableTree<String> f2 = new SortableTree<String>("My Documents ");
        root.addChild(f2);
        SortableTree<String> f3 = new SortableTree<String>("My Documents2 ");
        root.addChild(f3);

        root.sort(new Comparator<SortableTree<String>>() {

            @Override
            public int compare(SortableTree<String> o1, SortableTree<String> o2) {
                return o1.getT().compareTo(o2.getT());
            }

        });

        // System.out.println(root.index("附件"));
        System.out.println(root);
        String[] split = root.toString().split("\n");
        System.out.println(split.length);
        List<String> list = root.toList();
        for (int i = 0; i < list.size(); i++) {
            String each = list.get(i);
            SortableTree<String> treeEach = root.index(each);
            System.out.println(split[i] + "=>" + treeEach.getFloor() + "=>" + treeEach.isLastChild());
        }
    }
}
