package data_structure.acm.poj.leetcode;

public class BalancedTree_110 {
    public class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
  }

    public boolean isBalanced(TreeNode root) {
        if(root == null){
            return true;
        }
      calcHeight(root);
      return innerIsBalanced(root);
    }
    private boolean innerIsBalanced(TreeNode root){
        if(root == null){
            return true;
        }
        int left = height(root.left);
        int right = height(root.right);
        if(Math.abs(left-right) >1){
            return false;
        }else{
            boolean isLeftBalanced = innerIsBalanced(root.left);
            boolean isRightBalanced = innerIsBalanced(root.right);
            if(isLeftBalanced && isRightBalanced ){
                return true;
            }else{
                return false;
            }
        }
    }
    private int height(TreeNode root){
        if(root == null){
            return -1;
        }
        return root.val;
    }
    public int calcHeight(TreeNode  root){
        if(root == null){
            return -1;
        }
        root.val = Math.max(calcHeight(root.left),calcHeight(root.right))+1;
        return root.val;
    }
    private   TreeNode[] buildBinaryTreeNode(Integer[] arr){
        TreeNode[] nodes= new TreeNode[arr.length];
        for (int i = 0; i < arr.length; i++) {
            Integer elm = arr[i];
            if(elm == null){
                nodes[i] = null;
            }else{
                nodes[i] = new TreeNode(elm);
            }

            if(i>0){
                if(i%2==0){
                    nodes[i/2-1].right  = nodes[i];

                }else{
                    nodes[i/2].left = nodes[i];
                }
            }
        }
        return nodes;
    }
    public static void main(String[] args){
        BalancedTree_110 balancedTree_110 = new BalancedTree_110();
        TreeNode[] root = balancedTree_110.buildBinaryTreeNode(new Integer[]{3, 8, 20, null, null, 15, 7});
        balancedTree_110.calcHeight(root[0]);
        for (int i = 0; i < root.length; i++) {
            System.out.println(balancedTree_110.height(root[i]));
        }
        System.out.println(balancedTree_110.isBalanced(root[0]));

        root = balancedTree_110.buildBinaryTreeNode(new Integer[]{1,2,2,3,3,null,null,4,4});
        balancedTree_110.calcHeight(root[0]);
        for (int i = 0; i < root.length; i++) {
            System.out.println(balancedTree_110.height(root[i]));
        }
        System.out.println(balancedTree_110.isBalanced(root[0]));
    }
}
