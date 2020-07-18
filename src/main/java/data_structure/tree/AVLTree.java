package data_structure.tree;

/**
 * AVL树，左节点右节点高度最多差1
 *  最小节点数s(h) = t(h-1) + t(h-2)+1
 * @param <T>
 */
public class AVLTree<T extends Comparable> {
    AVLNode<T> root = null;
    static final int ALLOW_DIFF_HEIGHT = 1;
    /**
     *
     *
     * @param e
     */
    public void insert(T e){
        if(root == null){
            root = new AVLNode<>(e);
            return ;
        }
        insert(root,e);

    }
    private AVLNode insert(AVLNode<T> node,T e){
        if(node == null){
            return new AVLNode(e);
        }
        int i = e.compareTo(node.getElement());
        if(i>0){
            node.setRight(insert(node.getRight(),e));
        }else if(i<0){
            node.setLeft(insert(node.getLeft(),e));
        }
        return balance(node);// 这个时候子节点的高度信息是已经更新过了的，因为递归遍历的时候左右节点肯定遍历过了，因此左右节点的高度信息被更新了
    }
    int height(AVLNode node){

        return node == null ? -1 : node.getHeight();
    }
    private AVLNode balance(AVLNode<T> node) {
        node.height = Math.max(height(node.getLeft()),height(node.getRight()))+1;
        if(height(node.getLeft())-height(node.getRight()) > ALLOW_DIFF_HEIGHT){ //当前节点出现了节点差异
            if(height(node.getLeft().getLeft()) >= height(node.getLeft().getRight())){//针对左儿子的左子树执行了插入操作
               return rotateWithLeftChild(node);
            }else {//针对左儿子的右子树执行插入操作
                return doubleWithLeftChild(node);
            }
        }else if(height(node.getRight())-height(node.getLeft()) > ALLOW_DIFF_HEIGHT){
            if(height(node.getRight().getRight()) >= height(node.getRight().getLeft())){//针对左儿子的左子树执行了插入操作
                return rotateWithRightChild(node);
            }else {//针对左儿子的右子树执行插入操作
                return doubleWithRightChild(node);
            }
        }
        return node;
    }

    private AVLNode doubleWithRightChild(AVLNode<T> node) {
        AVLNode k3 = node;
        AVLNode k1 = k3.getRight();
        AVLNode k2 = k1.getLeft();
        k3.setLeft(rotateWithLeftChild(k1));
        return rotateWithRightChild(k3);
    }

    private void updateHeight(AVLNode node){
        node.height = Math.max(height(node.getLeft()),height(node.getRight()))+1;
    }

    /**
     * 将 左节点高度-右节点高度=1 的节点高度持平
     *
     * @param node
     * @return
     */
    private AVLNode rotateWithLeftChild(AVLNode<T> node){
        AVLNode k2 = node;
        AVLNode k1 = node.getLeft();
        k2.setLeft(k1.getRight());
        k1.setRight(k2);
        updateHeight(k2);
        updateHeight(k1);
        return k1;
    }

    /**
     * 将 右节点高度-左节点高度=1 的节点高度持平
     * @param node
     * @return
     */
    private AVLNode rotateWithRightChild(AVLNode<T> node){
        AVLNode k1 = node;
        AVLNode k2 = node.getRight();
        k1.setRight(k2.getLeft());
        k2.setLeft(k1);
        updateHeight(k1);
        updateHeight(k2);
        return k2;
    }
    private AVLNode doubleWithLeftChild(AVLNode<T> node) {
       AVLNode  k3 = node;
       AVLNode k1 = k3.getLeft();
       AVLNode k2 = k1.getRight();
       k3.setLeft(rotateWithRightChild(k1));
       return rotateWithLeftChild(k3);
    }
    
    public boolean contains(T e){
        if(root == null){
            return false;
        }
        return contains(root,e);
    }
    private boolean contains(AVLNode<T> node,T e){
        int result = node.getElement().compareTo(e);
        if(result > 0){
            return contains(node.getLeft(),e);
        }else if(result < 0){
            return contains(node.getRight(),e);
        }else{
            return true;
        }
    }
    public T findMax(){
        AVLNode<T> node = root;
        while(true){
            if(node == null){
                return node.getElement();
            }
            node = node.getRight();
        }
    }
    public T findMin(){
        AVLNode<T> node = root;
        while(true){
            if(node == null){
                return node.getElement();
            }
            node = node.getLeft();
        }
    }

    /**
     * 基本逻辑：
     * 1. 若 e是叶子节点，直接删除即可
     * 2. 若 e 有1个子节点；将父元素指向子节点即可
     * 3. 若 e 有2个子节点：用右节点的最小数据替代当前节点，并递归删除右节点的数据
     * @param e
     */
    public void remove(T e){
        if(root != null && root.getElement().equals(e)){
            root = null;
            return;
        }
        remove(root,e);
    }
    private AVLNode remove(AVLNode<T> node,T e){
        if(node == null){
            return null;
        }

        int i = e.compareTo(node.getElement());
        if(i > 0){
            node.right = remove(node.right,e);
        }else if(i < 0){
            node.left = remove(node.left,e);
        }else { // 找到要移除的节点了
            if(node.getLeft() != null && node.getRight()!=null){//2节点都不为空，需要找到右边值最小的节点返回。
                AVLNode<T> minNode = removeMin(node.getRight());
                minNode.setLeft(node.getLeft());
                minNode.setRight(node.getRight());
                return minNode;
            }else{ //其中一个为空或者2个都为空
                return node.getLeft() != null? node.getLeft() : node.getRight();
            }
        }
        return balance(node);
    }
    AVLNode<T>  removeMin(AVLNode node){
        AVLNode parent = null;
        while (node.getLeft() != null){
            parent = node;
            node = node.getLeft();
        }
        parent.setLeft(node.getRight());
        return node;
    }
}
