package data_structure.acm.poj.leetcode;

import java.util.*;

public class Sum_1 {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer,LinkedList> map = new HashMap<>();
        int index = 0;
        for (int num : nums) {
            map.putIfAbsent(num,new LinkedList());
            map.get(num).add(index);
            index++;
        }
        index = 0;
        for (int num : nums) {
            int sub = target - num;

            if(!map.containsKey(sub)){
                index++;
                continue;
            }
            LinkedList<Integer> list =  map.get(sub);
            if(sub == num  ){
                if(list.size() > 1){
                    return new int[]{list.get(0),list.get(1)};
                }
            }else{
                return new int[]{index,(int)map.get(sub).get(0)};
            }
            index++;
        }
        return new int[0];
    }
    public static void main(String[] args){
        Sum_1 sum_1 = new Sum_1();
        int[] nums = new int[]{2,4,11,2};
        int target = 13;
        System.out.println(Arrays.toString(sum_1.twoSum(nums,target)));
    }
}
