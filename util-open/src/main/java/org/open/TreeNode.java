package org.open;

import java.util.Vector;

public class TreeNode<T> extends TreeObject<T> implements TreeInterface<T>
{
	private T t;
	
	private Vector<TreeObject<T>> children = new Vector<TreeObject<T>>();
	   
	private Vector<TreeObject<T>> brother = new Vector<TreeObject<T>>();
	
	public TreeNode(T t) {
		super(Tree.Type.NODE);
		this.t = t;
//		children = new Vector<TreeObject<T>>();
//		brother = new Vector<TreeObject<T>>();
	}

	@Override
	public T getElement() {
		return t;
	}

	public void addBrother(TreeObject<T> brother) {
		this.brother.add(brother);
	}

	public void addChild(TreeObject<T> child) {
		this.children.add(child);
	}
	
//	private String chapter(String chapterExp,int chapterNum)
//	{
//		return chapterExp.replaceAll("\\$\\{.*\\}", String.valueOf(chapterNum));
//	}
	
//	public String toString(String chapter)
//	{
//		StringBuffer buf = new StringBuffer();
//		String subCapter = "${chapter}.";
//		for(int i=0;i<brother.size();i++)
//		{
//			String _subCapter = chapter + chapter(subCapter,i+1);
//			TreeObject<T> subTree = brother.elementAt(i);
//			if(subTree instanceof TreeNode){
//				TreeNode<T> tmp = (TreeNode<T>)subTree;
//				buf.append(_subCapter).append(tmp.getElement()).append("\r\n");
//				buf.append(tmp.toString(_subCapter)).append("\r\n");
//			}else if(subTree instanceof TreeLeaf){
//				TreeLeaf<T> tmp = (TreeLeaf<T>)subTree;
//				buf.append(_subCapter).append(tmp.getElement()).append("\r\n");
//			}
//		}
//		for(int i=0;i<children.size();i++)
//		{
//			String _subCapter = chapter + chapter(subCapter,i+1);
//			TreeObject<T> subTree = children.elementAt(i);
//			if(subTree instanceof TreeNode){
//				TreeNode<T> tmp = (TreeNode<T>)subTree;
//				buf.append(_subCapter).append(tmp.getElement()).append("\r\n");
//				buf.append(tmp.toString(_subCapter)).append("\r\n");
//			}else if(subTree instanceof TreeLeaf){
//				TreeLeaf<T> tmp = (TreeLeaf<T>)subTree;
//				buf.append(_subCapter).append(tmp.getElement()).append("\r\n");
//			}
//			
//		}
//		return buf.toString();
//	}

	public Vector<TreeObject<T>> getChildren() {
		return children;
	}

	public void setChildren(Vector<TreeObject<T>> children) {
		this.children = children;
	}

	public Vector<TreeObject<T>> getBrother() {
		return brother;
	}

	public void setBrother(Vector<TreeObject<T>> brother) {
		this.brother = brother;
	}
	
//	public void addBrother(TreeObject<T> brother) {
//		this.brother.add(brother);
//	}
//
//	public void addChild(TreeObject child) {
//		this.children.add(child);
//	}

//	public Tree getChildren() {
//		return children;
//	}
//
//	public void setChildren(Tree children) {
//		this.children = children;
//	}
}
