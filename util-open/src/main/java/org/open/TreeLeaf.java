package org.open;

public class TreeLeaf<T> extends TreeObject<T> 
{
    private T t;

    public TreeLeaf(T t) {
        super(Tree.Type.LEAF);
        this.t = t;
    }

    @Override
	public T getElement() {
		return t;
	}
} 
