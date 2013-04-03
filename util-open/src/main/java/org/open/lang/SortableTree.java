package org.open.lang;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 可排序Tree结构
 *
 * @author Qin Zhipeng
 * @version $Id:$
 * @param <T>
 */
public class SortableTree<T> {

    private T                     t;
    private SortableTree<T>       parent;
    private List<SortableTree<T>> children = new LinkedList<SortableTree<T>>();
    private int                   floor    = 0;

    public SortableTree(T t) {
        this.t = t;
    }

    public SortableTree<T> index(T t) {
        return _index(this, t);
    }

    private static <T> SortableTree<T> _index(SortableTree<T> tree, T t) {
        if (tree.t.equals(t)) {
            return tree;
        }
        if (tree.getChildren().size() > 0) {
            for (SortableTree<T> _tree : tree.getChildren()) {
                if (_tree.getT().equals(t)) {
                    return _tree;
                }
                _tree = _index(_tree, t);
                if (_tree != null) {
                    return _tree;
                }
            }
        }
        return null;
    }

    public void addChild(SortableTree<T> child) {
        children.add(child);
        child.parent = this;
        child.floor = this.floor + 1;
    }

    public void addChild(int index, SortableTree<T> child) {
        children.add(index, child);
        child.parent = this;
        child.floor = this.floor + 1;
    }

    public List<SortableTree<T>> getChildren() {
        return children;
    }

    public void setChildren(List<SortableTree<T>> children) {
        this.children = children;
    }

    public SortableTree<T> getParent() {
        return parent;
    }

    public int getFloor() {
        return floor;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public boolean isLastChild() {
        if (null == this.parent) {
            return false;
        }
        return (this.parent.getChildren().indexOf(this) == (this.parent.children.size() - 1));
    }

    private static <T> void _sort(SortableTree<T> tree, Comparator<? super SortableTree<T>> c) {
        if (tree.getChildren().size() > 0) {
            for (SortableTree<T> _tree : tree.getChildren()) {
                _sort(_tree, c);
            }
            Collections.sort(tree.getChildren(), c);
        }
    }

    public void sort(Comparator<? super SortableTree<T>> c) {
        _sort(this, c);
    }

    private static <T> void _toList(SortableTree<T> tree, List<T> list) {
        if (tree.getChildren().size() > 0) {
            for (int i = 0; i < tree.getChildren().size(); i++) {
                list.add(tree.getChildren().get(i).getT());
                _toList(tree.getChildren().get(i), list);
            }
        }
    }

    public List<T> toList() {
        List<T> list = new LinkedList<T>();
        list.add(this.t);
        _toList(this, list);
        return list;
    }

    private String toString(String lftStr, String append) {
        StringBuilder b = new StringBuilder();
        b.append(append + t);
        b.append("\n");
        if (children.size() > 0) {
            for (int i = 0; i < children.size() - 1; i++) {
                b.append(lftStr + children.get(i).toString(lftStr + "│", "├"));
            }
            b.append(lftStr + children.get(children.size() - 1).toString(lftStr + " ", "└"));
        }
        return b.toString();
    }

    public String toString() {
        return toString(" ", "");
    }

}
