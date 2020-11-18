package data_structure.acm.poj.leetcode;

/**
 * https://leetcode-cn.com/problems/unique-binary-search-trees/
 * 给定一个整数 n，求以 1 ... n 为节点组成的二叉搜索树有多少种？
 * 可以将问题分解：
 * 二叉搜索树的性质是：二叉搜索树的根节点的左子树逗比根节点小，二叉搜索树的右子树逗比根节点要大。
 * 因此，以根节点i为分界线，问题被分解为：f(i-1)*f(n-i)
 * 3个节点分解逻辑为：5
 *  1位根节点： 0*f(2) 2
 *  2为根节点 f(1)*f(1) 1
 *  3为根节点 f(2)*0 2
 * 4个节点分解逻辑为：14
 *   1位根节点： 0*f(3) 5
 *   2为根节点：f(1)*f(2)   2
 *   3为根节点: f(2)*f(1) 2
 *   4为根节点：f(3)*f(0) 5
 */
public class Num_binary_tree96 {
    public int numTrees(int n) {
        int cnt = n+3;
        int[] result = new int[cnt];
        result[0] = 1;
        result[1] = 1;
        result[2] = 2;
        if(n < 3){
            return result[n];
        }
        int sum = 0;
        for (int i = 3; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                sum+=result[j-1]*result[i-j];
            }
            result[i] = sum;
            sum = 0;
        }

        return result[n];
    }
   public static void main(String[] args){
        Num_binary_tree96 tree96 = new Num_binary_tree96();
       for (int i = 0; i < 5; i++) {
           System.out.println(i+"="+tree96.numTrees(i));
       }
   }
}
