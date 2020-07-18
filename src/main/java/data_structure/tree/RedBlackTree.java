package data_structure.tree;

import io.study.lang.Guard;

import java.util.ArrayList;
import java.util.List;

/**
 * 这里实现的是数据结构与算法分析里的自顶向下的插入、删除操作。感觉还比较简单
 *
 * 旋转节点就完事了
 * 为了正常旋转，需要持有节点的父亲、祖父、曾祖父信息
 * 基本原则：
 * 1. 根节点是黑色
 * 2. 红色节点的子节点必须是黑色节点
 * 3. 从根节点到任何Null节点的黑色节点数量必须一致
 * 4. 每个节点要么是红色，要么是黑色
 *
 *  插入基本原理：
 *  1. 新插入的节点必须是红色
 *  2. 根节点必须一直是黑色，即使由于旋转根节点变成红色了，将其变为黑色仍然符合红黑树的原理
 *  3. 插入的节点父节点是黑色的，可以直接返回。
 *  4. 插入的节点父节点是红色的，需要进行旋转操作。这时候需要判断当前节点与父节点、祖父节点的位置进行对应的旋转操作。节点位置有4种情况：
 *     左左(父节点是祖父的左节点，当前节点是父节点的左节点0)、左右(父节点是祖父节点的左节点，当前节点是父节点的右节点)、右左、右右 。
 *     对应这四种情况，需要分别执行左单旋、左双旋、右双旋、右单旋
 *  5. 旋转时需要自顶向下，当遇到当前节点是黑色、两个子节点是红色的情况时，需要反转，将当前节点变成红色、子节点变成黑色。这样可能导致当前节点与父节点都是红色节点，
 *   但是无所谓，经过一次旋转操作就行啦。旋转后不会再次出现两个红节点的情况。因为是从上面向下旋转的。
 * 删除基本原理：
 *  1. 删除时不能直接跳过(将当前的子节点直接设置为子节点的子节点)只有一个子节点的节点，因为会导致当前节点和父节点都是红色，增加了复杂性。节点还有可能不平衡了
 *  2. 要确保每次删除都删除叶子节点，当节点有2个子节点时，删除右边的最小节点，并替换当前节点；有一个右节点时，同两个节点；当只有一个左节点时，删除左边最大的节点，并替换当前节点。
 *  3. 当要删除的叶子节点是红色时，直接删除即可
 *  4. 当要删除的叶子节点是黑色时，略微复杂了点： https://blog.csdn.net/javyzheng/article/details/12339463
 *  https://juejin.im/post/5b35f9c1e51d4558e03cf3be 有源码
 *
 * @param <T>
 */
public class RedBlackTree<T extends Comparable> {
    RedBlackTreeNode root = null;
    /**
     * 最小节点的node，虚拟的红节点，方便移除
     */
   final RedBlackTreeNode MIN_NODE = new RedBlackTreeNode(null);

    public void insert(T e){
        if(e == null){
            return;
        }else if(root == null){
            root = new RedBlackTreeNode(e);
            root.toBlack();
            return;
        }
        insert(root,e);
    }


    boolean isRed(RedBlackTreeNode node){
        return node != null && node.getColor() == RedBlackTreeNode.COLOR_RED;
    }
    boolean isBlack(RedBlackTreeNode node){
        return node != null && node.getColor() == RedBlackTreeNode.COLOR_BLACK;
    }
    void resetNode(RedBlackTreeNode parent,RedBlackTreeNode child,boolean isLeft){
        if(parent == null){
            root = child;
            child.setParent(null);
        }else if(isLeft){
            parent.setLeft(child);
        }else{
            parent.setRight(child);
        }
    }
    void handleReorient(RedBlackTreeNode node){
        node.toRed();
        node.getLeft().toBlack();
        node.getRight().toBlack();
        rotate(node);
        root.toBlack();//根节点一直是黑色就行了，不会影响平衡
    }
    void rotate(RedBlackTreeNode node){
        if(node.getParent() != null && node.getParent().isRed()){  //2个红节点上面一定有黑色节点
            RedBlackTreeNode grand = node.getParent().getParent();// 祖父
            RedBlackTreeNode great = node.getParent().getParent().getParent();// 曾祖父
            boolean grandIsLeftChildOfGreat = great != null && great.getLeft() == grand;

            RedBlackTreeNode rotateResult = null;
            if(node.getParent().getLeft() == node){//当前节点是父节点的左节点
                if(grand.getLeft() == node.getParent()){ //左左
                    rotateResult = rotateWithLeftChild(grand);
                }else if(grand.getRight() == node.getParent()){ //右左
                    rotateResult = doubleWithRightChild(grand);
                }
            }else{ //右节点
                if(grand.getRight() == node.getParent()){ //右右
                    rotateResult =   rotateWithRightChild(grand);
                }else if(grand.getLeft() == node.getParent()){ //左右
                    rotateResult = doubleWithLeftChild(grand);
                }
            }
            if(rotateResult != null){
                resetNode(great,rotateResult,grandIsLeftChildOfGreat);
            }
        }
    }
    void insert(RedBlackTreeNode parent,T e){
        int i = e.compareTo(parent.getElement());
        if(parent.isBlack() && isRed(parent.getLeft()) && isRed(parent.getRight())){
            handleReorient(parent);
        }
        if(i > 0){ // 需要在parent的右侧插入
            if(parent.getRight() == null){
                RedBlackTreeNode node = new RedBlackTreeNode(e);
                parent.setRight(node);
                if(parent.isRed()){
                    rotate(node);
                }
            }else{
                insert(parent.getRight(),e);
            }
        }else{
            if(parent.getLeft() == null){
                RedBlackTreeNode node = new RedBlackTreeNode(e);
                parent.setLeft(node);
                if(parent.isRed()){
                    rotate(node);
                }
            }else{
                insert(parent.getLeft(),e);
            }
        }
    }
     RedBlackTreeNode doubleWithLeftChild(RedBlackTreeNode node){
        node.toRed();
        node.getLeft().getRight().toBlack();
        node.setLeft(rotateWithRightChild(node.getLeft()));
       return rotateWithLeftChild(node);

    }
     RedBlackTreeNode doubleWithRightChild(RedBlackTreeNode node){
        node.toRed();
        node.getRight().getLeft().toBlack();
        node.setRight(rotateWithLeftChild(node.getRight()));
        return rotateWithRightChild(node);

    }

    RedBlackTreeNode rotateWithRightChild(RedBlackTreeNode node){
        RedBlackTreeNode g = node;
        RedBlackTreeNode p = node.getRight();
        g.toRed();
        p.toBlack();
        g.setRight(p.getLeft());
        p.setLeft(g);
        return p;
    }
     RedBlackTreeNode rotateWithLeftChild(RedBlackTreeNode node) {
        RedBlackTreeNode g = node;
        RedBlackTreeNode p = node.getLeft();
        g.setLeft(p.getRight());
        g.toRed();
        p.toBlack();
        p.setRight(g);
        return p;
    }
    public void dump(){
        List<RedBlackTreeNode> ret = new ArrayList<>();
        traverse(root,ret);
        for (int i = 0; i < ret.size(); i++) {
            System.out.print(ret.get(i).toString() +" ");
        }
    }
    private void traverse(RedBlackTreeNode<T> node,List<RedBlackTreeNode> list){
        if(node == null) return;
        traverse(node.getLeft(),list);
        list.add(node);
        traverse(node.getRight(),list);
    }
     RedBlackTreeNode rotateWithRightChildNoColor(RedBlackTreeNode node){
        RedBlackTreeNode g = node;
        RedBlackTreeNode a = node.getRight();
        g.setRight(a.getLeft());
        a.setLeft(g);
        return a;
    }
    RedBlackTreeNode rotateWithLeftChildNoColor(RedBlackTreeNode node){
        RedBlackTreeNode g = node;
        RedBlackTreeNode p = node.getLeft();
        g.setLeft(p.getRight());
        p.setRight(g);
        return p;
    }


    int compare(T e,RedBlackTreeNode node){
        if(node == MIN_NODE){ //固定为最大值
            return 1;
        }else {
            return e.compareTo(node);
        }
    }
    boolean nullOrBlack(RedBlackTreeNode node){
        return node == null || node.isBlack();
    }
    public void remove(T e){
        if(root == null){
            return;
        }
        MIN_NODE.setRight(root);
        p = MIN_NODE;
        remove(p,e);
        root.toBlack();
    }

    RedBlackTreeNode p; //父元素
    RedBlackTreeNode x;//当前搜索的方向对应的元素
    RedBlackTreeNode t;//x 的兄弟元素

    /**
     * matchNode必须是红色的
     * @param matchNode
     * @param e
     */
    void removeMatchNode(RedBlackTreeNode matchNode,T e){
        Guard.assertTrue(matchNode.isRed(),"match_node_must_be_red");
        if(matchNode.isLeaf() ){
            matchNode.removeFromParent();
        }else if(matchNode.getRight()!=null){ //移除最右边的最小节点
            RedBlackTreeNode newNode = matchNode;
            while (newNode.getRight() != null){
                newNode = newNode.getRight();
            }
            T newValue = (T) newNode.getElement();
            if(newNode.getLeft() != null){ //左节点为空
                newValue = (T) newNode.getLeft().getElement();
            }
            remove(matchNode,newValue);
            matchNode.setElement(newValue);
        }else if(p.getLeft() != null){//移除左边的最大节点
            RedBlackTreeNode newNode = matchNode.getLeft();
            while (newNode.getRight() != null){
                newNode = newNode.getRight();
            }
            T newValue = (T) newNode.getElement();
            remove(matchNode,newValue);
            matchNode.setElement(newValue);
        }
    }
    /**
     * node对应当前节点，需要一直是红色的
     * @param node
     * @param e
     * @return
     */
    public void  remove(RedBlackTreeNode<T> node,T e){
        Guard.assertTrue(node.isRed(),"remove_node_must_be_red");
        int result = compare(e,p);//寻找节点
        if(result > 0){
            x = p.getRight();
            t = p.getLeft();
        }else if(result < 0){
            x = p.getLeft();
            t = p.getRight();
        }else{
             removeMatchNode(p,e);
        }
        RedBlackTreeNode parent = p.getParent();
        boolean isLeft = parent.getLeft() == p;

        if(nullOrBlack(x.getLeft()) && nullOrBlack(x.getRight()) ){//t的左右节点都是黑色
            if(nullOrBlack(t) || isBlack(t.getLeft()) && isBlack(t.getRight())  ){// t节点2个儿子
                p.toBlack();
                toRed(t);
                toRed(x);
                p = x;
                remove(x,e);
            }else {
                RedBlackTreeNode newRoot = null;
                boolean tIsRightOfP = p.getRight() == t;
                if(isRed(t.getLeft())){
                    if(tIsRightOfP){ //t是p的右节点
                        p.toBlack();
                        x.toRed();
                        t.getLeft().toRed();
                        p.setRight(rotateWithLeftChildNoColor(t));
                        newRoot = rotateWithRightChildNoColor(p);
                    }else{
                        p.toBlack();
                        t.toRed();
                        x.toRed();
                        newRoot = rotateWithLeftChildNoColor(p);
                    }
                }else {
                    if(tIsRightOfP){
                        t.toRed();
                        t.getRight().toBlack();
                        p.toBlack();
                        newRoot  = rotateWithRightChildNoColor(t);
                    }else{
                        x.toRed();
                        p.toBlack();
                        p.setLeft(rotateWithRightChildNoColor(t));
                        newRoot = rotateWithLeftChildNoColor(p);
                    }
                }

                p = x;
                resetNode(parent,newRoot,isLeft);
                remove(x,e);;
            }
        }else{ //x节点里有一个为红色
            p = x;
            parent = p.getParent();
             isLeft = parent.getLeft() == p;
             result = compare(e, p);
             if(result > 0){ //落到右节点
                 if(isRed(p.getRight())){
                     p = x.getRight();
                     remove(p,e);
                 }else if(isRed(p.getLeft())){
                     p.toRed();
                     p.getLeft().toBlack();
                     RedBlackTreeNode child = rotateWithLeftChildNoColor(p);
                     resetNode(parent,child,isLeft);
                     remove(child,e);
                 }
             }else if(result < 0){
                 if(isRed(p.getLeft())){
                     p = x.getLeft();
                     remove(p,e);
                 }else if(isRed(p.getRight())){
                     p.toRed();
                     p.getRight().toBlack();
                     RedBlackTreeNode child = rotateWithLeftChildNoColor(p);
                     resetNode(parent,child,isLeft);
                     remove(child,e);
                 }
             }else{
                 removeMatchNode(p,e);
             }
        }

    }
    void toBlack(RedBlackTreeNode node){
        if(node != null) node.toBlack();
    }
    void toRed(RedBlackTreeNode node){
        if(node != null) node.toRed();
    }
    public boolean contains(T e){
        if(root == null){
            return false;
        }
        return contains(root,e);
    }
    private boolean contains(RedBlackTreeNode<T> node,T e){
        int result = node.getElement().compareTo(e);
        if(result > 0){
            return contains(node.getLeft(),e);
        }else if(result < 0){
            return contains(node.getRight(),e);
        }else{
            return true;
        }
    }
    public static void main(String[] args){
         Integer[] arr = new Integer[]{10,85,15};//,70,20,60,30,50,65,80,90,40,5,55,45.
        RedBlackTree tree = new RedBlackTree();
        for (Integer integer : arr) {
            tree.insert(integer);
        }
        tree.dump();

    }

}
