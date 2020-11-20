package data_structure.acm.poj.leetcode;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Arr_349 {
        public int[] intersection(int[] nums1, int[] nums2) {
            if(nums1 == null || nums2 == null){
                return new int[0];
            }
            Set<Integer> set = new HashSet<>();
            Set<Integer> result = new HashSet<>();
            for (int i : nums1) {
                set.add(i);
            }
            for (int i : nums2) {
                if(set.contains(i)){
                    result.add(i);
                }

            }
            int[] ret = new int[result.size()];
            int index= 0;
            for (Integer i : result) {
                ret[index++] = i;
            }
            return ret;
        }
        public static void main(String[] args){
            int[] num1 = new int[]{1,2,2,1};
            int[] num2 = new int[]{2,2};
            Arr_349 arr = new Arr_349();
            int[] result = arr.intersection(num1, num2);
            System.out.println(Arrays.toString(result));
        }
}
