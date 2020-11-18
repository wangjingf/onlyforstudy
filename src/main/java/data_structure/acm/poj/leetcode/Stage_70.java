package data_structure.acm.poj.leetcode;

/**
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 *
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 *
 * 注意：给定 n 是一个正整数。
 */
public class Stage_70 {
    // f(n) = f(n-1) + f(n-2)
    public int climbStairs(int n) {
        int[] result = new int[n+2];
        result[0] = 0;
        result[1] = 1;
        result[2] = 2;
        if(n <= 2){
            return result[n];
        }
        for (int i = 3; i <= n; i++) {
            result[i] = result[i-1]+result[i-2];
        }
        return result[n];
    }
    public static void main(String[] args){
        Stage_70 stage_70 = new Stage_70();
        System.out.println(stage_70.climbStairs(3));
        System.out.println(stage_70.climbStairs(4));
    }
}
