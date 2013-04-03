package org.open;

public abstract class TreeObject<T> 
{
    private Tree.Type type;

    public TreeObject(Tree.Type type) {
        this.type = type;
    }
    
    public Tree.Type getType() {
        return type;
    }
    
    public abstract T getElement();
} 