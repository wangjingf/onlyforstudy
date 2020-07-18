package data_structure.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 二叉查找树
 * @param <T>
 */
public class BinaryFindTree<T extends Comparable> {
    BinaryNode<T> root = null;

    /**
     * 插入逻辑基本逻辑：
     *
     * @param e
     */
    public void insert(T e){
        if(root == null){
            root = new BinaryNode<>(e);
            return ;
        }
        insert(root,e);

    }

    private void insert(BinaryNode<T> node,T e){
        int i = e.compareTo(node.getElement());
        if(i > 0){// 应该在右边插入
            if(node.getRight()!= null){
                insert(node.getRight(),e);
            }else{
                node.setRight(new BinaryNode(e));
            }
        }else if(i < 0){
            if(node.getLeft()!= null){
                insert(node.getLeft(),e);
            }else{
                node.setLeft(new BinaryNode(e));
            }
        }else{
            return ;
        }
    }
    public boolean contains(T e){
        if(root == null){
            return false;
        }
        return contains(root,e);
    }
    private boolean contains(BinaryNode<T> node,T e){
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
        BinaryNode<T> node = root;
        while(true){
            if(node == null){
                return node.getElement();
            }
            node = node.getRight();
        }
    }
    public T findMin(){
        BinaryNode<T> node = root;
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
    private BinaryNode remove(BinaryNode<T> node,T e){
        if(node == null){
            return null;
        }

        int i = e.compareTo(node.getElement());
        if(i > 0){
            node.right = remove(node.right,e);
        }else if(i < 0){
            node.left = remove(node.left,e);
        }else { // 找到要移除的节点了
            if(node.getLeft() != null && node.getRight()!=null){//2节点都 不为空，需要找到右边值最小的节点返回。
                BinaryNode<T> minNode = removeMin(node.getRight());
                minNode.setLeft(node.getLeft());
                minNode.setRight(node.getRight());
                return minNode;
            }else{ //其中一个为空或者2个都为空
                return node.getLeft() != null? node.getLeft() : node.getRight();
            }
        }
        return node;
    }
    BinaryNode<T>  removeMin(BinaryNode node){
       BinaryNode parent = null;
       while (node.getLeft() != null){
           parent = node;
           node = node.getLeft();
       }
        parent.setLeft(node.getRight());
        return node;
    }
    public void makeEmpty(){
         root = null;
    }
    public boolean isEmpty(){
        return root == null;
    }
    public void dump(){
        List<T> ret = new ArrayList<>();
        traverse(root,ret);
        System.out.println(Arrays.asList(ret));
    }
    private void traverse(BinaryNode<T> node,List<T> list){
        if(node == null) return;
        traverse(node.getLeft(),list);
        list.add(node.getElement());
        traverse(node.getRight(),list);
    }
    public static void main(String[] args){
        BinaryFindTree<Integer> tree = new BinaryFindTree<>();
        Integer[] data = new Integer[]{
                6,2,8,1,4,3
        };
        for (Integer i : data) {
            tree.insert(i);
        }
        tree.dump(); //1,2,3,4,6,8
        /*tree.remove(4);// 1,2,3,6,8
        tree.dump();*/
        tree.remove(2);// 1,3,4,6,8
        tree.dump();
    }
}
