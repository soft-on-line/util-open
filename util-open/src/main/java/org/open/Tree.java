package org.open;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.open.util.RegexpUtil;

public class Tree<T> implements TreeInterface<T> {

    public enum Type {
        /** 节点 */
        NODE,

        /** 叶子 */
        LEAF;
    }

    private Vector<TreeObject<T>> children = new Vector<TreeObject<T>>();

    private Vector<TreeObject<T>> brother  = new Vector<TreeObject<T>>();

    public Tree() {
    }

    public void addChild(TreeObject<T> child) {
        this.children.addElement(child);
    }

    public void addBrother(TreeObject<T> brother) {
        this.brother.addElement(brother);
    }

    private TreeObject<T> getTreeObject(TreeNode<T> treeNode, T t) {
        for (int i = 0; i < treeNode.getBrother().size(); i++) {
            TreeObject<T> subTree = treeNode.getBrother().elementAt(i);
            if (subTree instanceof TreeNode<?>) {
                TreeNode<T> tmp = (TreeNode<T>) subTree;
                if (t.equals(tmp.getElement())) {
                    return tmp;
                } else {
                    TreeObject<T> to = getTreeObject(tmp, t);
                    if (to != null) {
                        return to;
                    }
                }
            } else if (subTree instanceof TreeLeaf<?>) {
                TreeLeaf<T> tmp = (TreeLeaf<T>) subTree;
                if (t.equals(tmp.getElement())) {
                    return tmp;
                }
            }
        }
        for (int i = 0; i < treeNode.getChildren().size(); i++) {
            TreeObject<T> subTree = treeNode.getChildren().elementAt(i);
            if (subTree instanceof TreeNode<?>) {
                TreeNode<T> tmp = (TreeNode<T>) subTree;
                if (t.equals(tmp.getElement())) {
                    return tmp;
                } else {
                    TreeObject<T> to = getTreeObject(tmp, t);
                    if (to != null) {
                        return to;
                    }
                }
            } else if (subTree instanceof TreeLeaf<?>) {
                TreeLeaf<T> tmp = (TreeLeaf<T>) subTree;
                if (t.equals(tmp.getElement())) {
                    return tmp;
                }
            }
        }
        return null;
    }

    public TreeObject<T> getTreeObject(T t) {
        for (int i = 0; i < brother.size(); i++) {
            TreeObject<T> subTree = brother.elementAt(i);
            if (subTree instanceof TreeNode<?>) {
                TreeNode<T> tmp = (TreeNode<T>) subTree;
                if (t.equals(tmp.getElement())) {
                    return tmp;
                } else {
                    TreeObject<T> to = getTreeObject(tmp, t);
                    if (to != null) {
                        return to;
                    }
                }
            } else if (subTree instanceof TreeLeaf<?>) {
                TreeLeaf<T> tmp = (TreeLeaf<T>) subTree;
                if (t.equals(tmp.getElement())) {
                    return tmp;
                }
            }
        }
        for (int i = 0; i < children.size(); i++) {
            TreeObject<T> subTree = children.elementAt(i);
            if (subTree instanceof TreeNode<?>) {
                TreeNode<T> tmp = (TreeNode<T>) subTree;
                if (t.equals(tmp.getElement())) {
                    return tmp;
                } else {
                    TreeObject<T> to = getTreeObject(tmp, t);
                    if (to != null) {
                        return to;
                    }
                }
            } else if (subTree instanceof TreeLeaf<?>) {
                TreeLeaf<T> tmp = (TreeLeaf<T>) subTree;
                if (t.equals(tmp.getElement())) {
                    return tmp;
                }
            }
        }
        return null;
    }

    public Type getType(T t) {
        TreeObject<T> treeObject = getTreeObject(t);
        return treeObject.getType();
    }

    public boolean isLeaf(T t) {
        return (getType(t) == Type.LEAF);
    }

    public boolean isNode(T t) {
        return (getType(t) == Type.NODE);
    }

    private void _toList(List<T> list, TreeNode<T> treeNode) {
        for (int i = 0; i < treeNode.getBrother().size(); i++) {
            TreeObject<T> subTree = treeNode.getBrother().elementAt(i);
            if (subTree instanceof TreeNode<?>) {
                TreeNode<T> tmp = (TreeNode<T>) subTree;
                list.add(tmp.getElement());
                _toList(list, tmp);
            } else if (subTree instanceof TreeLeaf<?>) {
                TreeLeaf<T> tmp = (TreeLeaf<T>) subTree;
                list.add(tmp.getElement());
            }
        }
        for (int i = 0; i < treeNode.getChildren().size(); i++) {
            TreeObject<T> subTree = treeNode.getChildren().elementAt(i);
            if (subTree instanceof TreeNode<?>) {
                TreeNode<T> tmp = (TreeNode<T>) subTree;
                list.add(tmp.getElement());
                _toList(list, tmp);
            } else if (subTree instanceof TreeLeaf<?>) {
                TreeLeaf<T> tmp = (TreeLeaf<T>) subTree;
                list.add(tmp.getElement());
            }
        }
    }

    public List<T> toList() {
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < brother.size(); i++) {
            TreeObject<T> subTree = brother.elementAt(i);
            if (subTree instanceof TreeNode<?>) {
                TreeNode<T> tmp = (TreeNode<T>) subTree;
                list.add(tmp.getElement());
                _toList(list, tmp);
            } else if (subTree instanceof TreeLeaf<?>) {
                TreeLeaf<T> tmp = (TreeLeaf<T>) subTree;
                list.add(tmp.getElement());
            }
        }
        for (int i = 0; i < children.size(); i++) {
            TreeObject<T> subTree = children.elementAt(i);
            if (subTree instanceof TreeNode<?>) {
                TreeNode<T> tmp = (TreeNode<T>) subTree;
                list.add(tmp.getElement());
                _toList(list, tmp);
            } else if (subTree instanceof TreeLeaf<?>) {
                TreeLeaf<T> tmp = (TreeLeaf<T>) subTree;
                list.add(tmp.getElement());
            }
        }
        return list;
    }

    public String getChapter(T t) {
        String toString = this.toString();
        String[] groups = toString.split("\\s");
        for (int i = 0; i < groups.length; i++) {
            if (-1 != groups[i].indexOf(t.toString())) {
                return RegexpUtil.matchGroup(groups[i], "(\\.[\\.|\\d]+\\.)");
            }
        }
        return null;
    }

    public int getFloor(T t) {
        return RegexpUtil.matchGroups(getChapter(t), "([\\.|\\d]+?\\.)").length;
    }

    public Vector<TreeObject<T>> getChildren() {
        return this.children;
    }

    public void setChildren(Vector<TreeObject<T>> children) {
        this.children = children;
    }

    public Vector<TreeObject<T>> getBrother() {
        return this.brother;
    }

    public void setBrother(Vector<TreeObject<T>> brother) {
        this.brother = brother;
    }

    private String chapter(String chapterExp, int chapterNum) {
        return chapterExp.replaceAll("\\$\\{.*\\}", String.valueOf(chapterNum));
    }

    private void _toString(TreeNode<T> treeNode, StringBuffer buf, String chapter) {
        String subCapter = "${chapter}.";
        for (int i = 0; i < treeNode.getBrother().size(); i++) {
            String _subCapter = chapter + chapter(subCapter, i + 1);
            TreeObject<T> subTree = treeNode.getBrother().elementAt(i);
            if (subTree instanceof TreeNode<?>) {
                TreeNode<T> tmp = (TreeNode<T>) subTree;
                buf.append(_subCapter).append(tmp.getElement()).append("\r\n");
                _toString(tmp, buf, _subCapter);
            } else if (subTree instanceof TreeLeaf<?>) {
                TreeLeaf<T> tmp = (TreeLeaf<T>) subTree;
                buf.append(_subCapter).append(tmp.getElement()).append("\r\n");
            }
        }
        for (int i = 0; i < treeNode.getChildren().size(); i++) {
            String _subCapter = chapter + chapter(subCapter, i + 1);
            TreeObject<T> subTree = treeNode.getChildren().elementAt(i);
            if (subTree instanceof TreeNode<?>) {
                TreeNode<T> tmp = (TreeNode<T>) subTree;
                buf.append(_subCapter).append(tmp.getElement()).append("\r\n");
                _toString(tmp, buf, _subCapter);
            } else if (subTree instanceof TreeLeaf<?>) {
                TreeLeaf<T> tmp = (TreeLeaf<T>) subTree;
                buf.append(_subCapter).append(tmp.getElement()).append("\r\n");
            }

        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        String chapter = ".${chapter}.";
        for (int i = 0; i < brother.size(); i++) {
            String subChapter = chapter(chapter, i + 1);
            TreeObject<T> subTree = brother.elementAt(i);
            if (subTree instanceof TreeNode<?>) {
                TreeNode<T> tmp = (TreeNode<T>) subTree;
                buf.append(subChapter).append(tmp.getElement()).append("\r\n");
                _toString(tmp, buf, subChapter);
            } else if (subTree instanceof TreeLeaf<?>) {
                TreeLeaf<T> tmp = (TreeLeaf<T>) subTree;
                buf.append(subChapter).append(tmp.getElement()).append("\r\n");
            }
        }
        for (int i = 0; i < children.size(); i++) {
            String subChapter = chapter(chapter, i + 1);
            TreeObject<T> subTree = children.elementAt(i);
            if (subTree instanceof TreeNode<?>) {
                TreeNode<T> tmp = (TreeNode<T>) subTree;
                buf.append(subChapter).append(tmp.getElement()).append("\r\n");
                _toString(tmp, buf, subChapter);
            } else if (subTree instanceof TreeLeaf<?>) {
                TreeLeaf<T> tmp = (TreeLeaf<T>) subTree;
                buf.append(subChapter).append(tmp.getElement()).append("\r\n");
            }
        }
        return buf.toString();
    }

}
