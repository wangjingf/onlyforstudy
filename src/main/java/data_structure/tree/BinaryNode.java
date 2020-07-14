package data_structure.tree;

public class BinaryNode<T extends Comparable> {
    T element;
    BinaryNode left;
    BinaryNode right;
    public BinaryNode(T element){
        this.element = element;
    }
    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public BinaryNode getLeft() {
        return left;
    }

    public void setLeft(BinaryNode left) {
        this.left = left;
    }

    public BinaryNode getRight() {
        return right;
    }

    public void setRight(BinaryNode right) {
        this.right = right;
    }
}
