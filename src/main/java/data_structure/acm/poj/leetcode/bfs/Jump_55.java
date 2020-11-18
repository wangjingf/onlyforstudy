package data_structure.acm.poj.leetcode.bfs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Jump_55 {
    /**
     * 给定一个非负整数数组，你最初位于数组的第一个位置。
     *
     * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
     *
     * 判断你是否能够到达最后一个位置。
     *
     * 示例 1:
     *
     * 输入: [2,3,1,1,4]
     * 输出: true
     * 解释: 我们可以先跳 1 步，从位置 0 到达 位置 1, 然后再从位置 1 跳 3 步到达最后一个位置。
     *  基本思路：从最后一个元素，执行宽度遍历
     *
     *  比较复杂，应该用贪心简单点
     *
     * @param nums
     * @return
     */
    public boolean canJump(int[] nums) {
        if(nums == null || nums.length <=1 ){
            return true;
        }
        Set<Integer> processed = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(nums.length-1);
        processed.add(nums.length-1);
        do{
            Integer index = queue.poll();
            if(index == 0){
                return true;
            }
            for (int i = index-1; i >=0 ; i--) {
                if(nums[i]+i >= index){
                    if(!processed.contains(i)){
                        processed.add(i);
                        queue.add(i);
                    }
                }
            }
        }while (!queue.isEmpty());
        return false;
    }
    public static void main(String[] args){
        Jump_55 jump55 = new Jump_55();
        int[] arr = new int[]{2,3,1,1,4};

        System.out.println(jump55.canJump(arr)); // true
        arr = new int[]{3,2,1,0,4};
        System.out.println(jump55.canJump(arr));// false
        arr = new int[]{0,1};
        System.out.println(jump55.canJump(arr)); // false

        arr = new int[]{1,0,1,0};
        System.out.println(jump55.canJump(arr));//false
    }
}
