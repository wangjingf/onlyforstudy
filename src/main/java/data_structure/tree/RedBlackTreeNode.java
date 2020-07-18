package data_structure.tree;

public class RedBlackTreeNode<T extends Comparable> {
    public static final int COLOR_RED = 0;
    public static final int COLOR_BLACK = 1;
    RedBlackTreeNode<T> left;
    RedBlackTreeNode<T> right;
    RedBlackTreeNode parent;
    T element;
    int color;

    public RedBlackTreeNode(T e) {
        this.color = COLOR_RED;
        this.element = e;
    }



    public RedBlackTreeNode<T> getLeft() {
        return left;
    }
    public boolean isLeaf(){
        return getLeft() == null && getRight() == null;
    }
    public void setLeft(RedBlackTreeNode<T> left) {
        this.left = left;
        if(left!=null){
            left.setParent(this);
        }

    }

    public RedBlackTreeNode<T> getRight() {
        return right;
    }
    public void removeFromParent(){
        if(getParent().getLeft() == this){
            getParent().setLeft(null);
        }else{
            getParent().setRight(null);
        }
    }
    public void setRight(RedBlackTreeNode<T> right) {
        this.right = right;
        if(right != null){
            right.setParent(this);
        }

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }
    public void toRed(){
        setColor(COLOR_RED);
    }
    public void toBlack(){
        setColor(COLOR_BLACK);
    }
    public boolean isRed(){
        return color == COLOR_RED;
    }
    public boolean isBlack(){
        return color == COLOR_BLACK;
    }

    public RedBlackTreeNode getParent() {
        return parent;
    }

    public void setParent(RedBlackTreeNode parent) {
        this.parent = parent;
    }

    public String toString(){
        String e = element.toString();
        if(isBlack()){
            e+=" black";
        }else{
            e+=" red";
        }
        return e;
    }
}
