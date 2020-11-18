package data_structure.acm.poj.leetcode;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 实现一个比较接口就好啦
 */
public class Array_sort_1356 {
    public int[] sortByBits(int[] arr) {
        Integer[] wrapArr = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            wrapArr[i] = arr[i];
        }
        Arrays.sort(wrapArr,new Comparator<Integer>(){

            @Override
            public int compare(Integer o1, Integer o2) {
                int i1 = Integer.bitCount(o1);
                int i2 = Integer.bitCount(o2);
                if(i1>i2){
                    return 1;
                }else if(i1==i2){
                    return o1 - o2;
                }else{
                    return -1;
                }
            }
        });
        for (int i = 0; i < wrapArr.length; i++) {
            arr[i] = wrapArr[i];
        }
        return arr;
    }
    public static void main(String[] args){
        Array_sort_1356 obj = new Array_sort_1356();
        int[] arr = new int[]{0,1,2,3,4,5,6,7,8};
        System.out.println(Arrays.toString(obj.sortByBits(arr)));
        arr = new int[]{1024,512,256,128,64,8,4,2,1};
        System.out.println(Arrays.toString(obj.sortByBits(arr)));
    }
}
