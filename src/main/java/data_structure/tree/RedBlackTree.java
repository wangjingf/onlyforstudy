package data_structure.tree;

import io.study.exception.StdException;
import io.study.lang.Guard;

import java.util.ArrayList;
import java.util.LinkedList;
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
        ensureIsRedBlackTree();
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
        }else if(i < 0){
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
        list.add(node);
        traverse(node.getLeft(),list);
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
    public int size(){
        if(root == null){
            return 0;
        }
        List<RedBlackTreeNode> nodes = new ArrayList<>();
        traverse(root,nodes);
        return nodes.size();
    }
    /**
     *  判断当前树是不是还是红黑树,若不是，则抛出异常
     * @return
     */
    public boolean ensureIsRedBlackTree(){
        if(root == null){
            return true;
        }else if(root.isRed()){
            throw new StdException("root is black");
        }
        List<Integer> levels = new LinkedList<>();
        traverse(root,1,levels);
        int level = levels.get(0);
        for (Integer integer : levels) {
            if(integer != level){
                throw new StdException("not a red black tree");
            }
        }
        return true;
    }
    void traverse(RedBlackTreeNode node,int level,List<Integer> levels){
        if(node == null){
            levels.add(level);
            return ;
        };
        if(isRed(node) && (isRed(node.getLeft()) || isRed(node.getRight()))){
            throw new StdException("invalid red black tree");
        }
        if(node.isRed()){
            traverse(node.getLeft(),level,levels);
            traverse(node.getRight(),level,levels);
        }else{
            traverse(node.getLeft(),level+1,levels);
            traverse(node.getRight(),level+1,levels);
        }
    }
    int compare(T e,RedBlackTreeNode node){
        if(node == MIN_NODE){ //固定为最大值
            return 1;
        }else {
            return e.compareTo(node.getElement());
        }
    }
    boolean nullOrBlack(RedBlackTreeNode node){
        return node == null || node.isBlack();
    }
    public void remove(T e){
        if(root == null){
            return;
        }
        p = root;
        t = null;
        x = null;
        if(nullOrBlack(root.getLeft()) && nullOrBlack(root.getRight()) ){//|| isRed(root.getLeft()) && isRed(root.getRight())
            root.toRed();
            remove(p,e);
        }else{
            removeWithBlackRoot(p,e);
        }
        if(root != null){
            root.toBlack();
        }

        ensureIsRedBlackTree();
    }

    RedBlackTreeNode p; //父元素
    RedBlackTreeNode x;//当前搜索的方向对应的元素
    RedBlackTreeNode t;//x 的兄弟元素

    /**
     * 寻找非叶子节点的前驱或者后继
     * @param matchNode
     * @return
     */
     T findPrevOrNextElm(RedBlackTreeNode matchNode){
        if(matchNode.isLeaf()){
            throw new StdException("invalid node");
        }else if(matchNode.getRight()!=null){ //移除最右边的最小节点
            RedBlackTreeNode newNode = matchNode.getRight();
            while (newNode.getLeft() != null){
                newNode = newNode.getLeft();
            }
           return  (T) newNode.getElement();

        }else {//移除左边的最大节点
            RedBlackTreeNode newNode = matchNode.getLeft();
            while (newNode.getRight() != null){
                newNode = newNode.getRight();
            }
           return  (T) newNode.getElement();

        }
    }
    /**
     * matchNode必须是红色的
     * @param matchNode
     * @param e
     */
    void removeMatchNode(RedBlackTreeNode matchNode,T e){
        Guard.assertTrue(matchNode.isRed(),"match_node_must_be_red");
        if(matchNode.isLeaf() ){
            if(matchNode.getParent() == null) {
                root = null;
                return ;
            }
            matchNode.removeFromParent();
        }else if(matchNode.getLeft()==null && matchNode.getRight()!= null && matchNode.getRight().getRight()==null){ //只有一个右子节点
            resetNode(matchNode.getParent(),matchNode.getRight(),false);
        }else if(matchNode.getRight()==null && matchNode.getLeft()!= null && matchNode.getLeft().getLeft()==null){//只有一个左子节点
            resetNode(matchNode.getParent(),matchNode.getLeft(),true);
        }else if(matchNode.getRight()!=null){ //移除最右边的最小节点
            RedBlackTreeNode newNode = matchNode.getRight();
            while (newNode.getLeft() != null){
                newNode = newNode.getLeft();
            }
            T newValue = (T) newNode.getElement();
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
        Guard.assertTrue(node.getElement() == p.getElement(),"element must be same");
        int result = compare(e,p);//寻找节点
        if(result > 0){
            x = p.getRight();

            t = p.getLeft();
        }else if(result < 0){
            x = p.getLeft();
            t = p.getRight();
        }else{
             removeMatchNode(p,e);
             return;
        }
        if(x == null){ //要移除的元素不存在
            return;
        }
        RedBlackTreeNode parent = p.getParent();
        boolean isLeft = false;
        if(parent != null && parent.getLeft() == p){
            isLeft = true;
        }

        if(nullOrBlack(x.getLeft()) && nullOrBlack(x.getRight()) ){//x的左右节点都是黑色
            if(t== null || nullOrBlack(t) && nullOrBlack(t.getLeft()) && nullOrBlack(t.getRight())  ){// t节点2个儿子都为黑色
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
                        /**
                         *            R(p)
                         *       x         t
                         *    x1  x2    R(t1) t2
                         *
                         */
                        p.toBlack();
                        x.toRed();
                        p.setRight(rotateWithLeftChildNoColor(t));
                        newRoot = rotateWithRightChildNoColor(p);
                    }else{
                        /**
                         *             R(p)
                         *        t          x
                         *    R(t1)  t2    x1 x2
                         *
                         */
                        p.toBlack();
                        t.getLeft().toBlack();//t1变黑
                        t.toRed();
                        x.toRed();
                        newRoot = rotateWithLeftChildNoColor(p);
                    }
                }else {
                    /**
                     *            R(p)
                     *       x         t
                     *    x1  x2    t1 R(t2)
                     *
                     */
                    if(tIsRightOfP){
                        t.toRed();
                        toBlack(t.getRight());;
                        p.toBlack();
                        x.toRed();
                        newRoot  = rotateWithRightChildNoColor(p);
                    }else{
                        /**
                         *             R(p)
                         *        t          x
                         *    t1  R(t2)    x1 x2
                         *
                         */
                        x.toRed();
                        p.toBlack();
                        p.setLeft(rotateWithRightChildNoColor(t));
                        newRoot = rotateWithLeftChildNoColor(p);
                    }
                }

                p = x;
                resetNode(parent,newRoot,isLeft);
                remove(x,e);
            }
        }else{ //x节点里有一个为红色
            p = x;
            removeWithBlackRoot(p,e);
        }

    }

    /**
     * 根节点是黑色的，且两个子节点有一个是红色的
     * @param node
     * @param e
     */
    void removeWithBlackRoot(RedBlackTreeNode node,T e){
        Guard.assertTrue(p.getElement() == node.getElement(),"node must be same");
        RedBlackTreeNode parent = p.getParent();
        boolean isLeft = false;
        if(parent != null && parent.getLeft() == p){
            isLeft = true;
        }
        int result = compare(e, p);
        if(result > 0){ //落到右节点
            /**
             *             p
             *        t          R(x)
             *    t1  t2    x1 x2
             *
             */
            if(isRed(p.getRight())){ // x是红色的，直接移除即可
                p = p.getRight();
                remove(p,e);
            }else if(isRed(p.getLeft())){ //x是黑色的,旋转使x的父变成红色
                /**
                 *             p
                 *        R(t)      x
                 *    t1   t2    x1 x2
                 *
                 */
                p.toRed();
                p.getLeft().toBlack();
                RedBlackTreeNode child = rotateWithLeftChildNoColor(p);
                resetNode(parent,child,isLeft);
                remove(p,e);
            }
        }else if(result < 0){
            /**
             *             p
             *       R(x)        t
             *    x1   x2    t1  t2
             *
             */
            if(isRed(p.getLeft())){
                p = p.getLeft();
                remove(p,e);
            }else if(isRed(p.getRight())){
                /**
                 *             p
                 *        x       R(t)
                 *    x1   x2    t1  t2
                 *
                 */
                p.toRed();
                p.getRight().toBlack();
                RedBlackTreeNode child = rotateWithRightChildNoColor(p);
                resetNode(parent,child,isLeft);
                remove(p,e);
            }
        }else{ //x是要移除的节点，x是黑色且x有子节点是红色
           e =findPrevOrNextElm(p); //找到前驱或者后继节点,再次进入循环
            RedBlackTreeNode temp = p;
            removeWithBlackRoot(p,e);
            temp.setElement(e);
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
        if(node == null){
            return false;
        }
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
         Integer[] arr = new Integer[]{10,85,15,70,10,60,30,50,80,80,90,40,5,54,45};//;10,85,15,70,20,60,30,50,65,80,90,40,5,55,45};
        RedBlackTree tree = new RedBlackTree();
        for (Integer integer : arr) {
            tree.insert(integer);
        }
        tree.dump();
        int size = tree.size();
        for (int i = size-1; i >=0 ; i--) {
            System.out.println();
            Guard.assertTrue(size == i+1,"size not true");
            int elm = arr[i];
            tree.remove(elm);
            if(tree.contains(elm)){
                throw new StdException("err_remove");
            }
            tree.dump();
            size--;
        }
    }

}
