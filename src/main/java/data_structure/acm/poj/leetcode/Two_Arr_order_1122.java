package data_structure.acm.poj.leetcode;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一个数组相对另一个数组排序
 */
public class Two_Arr_order_1122 {
    public int[] relativeSortArray(int[] arr1, int[] arr2) {
        Map<Integer,Integer> treeMap =  new TreeMap<>();
        for (int i : arr1) {
            Integer elm = treeMap.get(i);
            if(elm == null){
                treeMap.put(i,1);
            }else{
                treeMap.put(i,elm+1);
            }

        }
        int[] newArr = new int[arr1.length];
        int index = 0;
        for (int i = 0; i < arr2.length; i++) {
            int elm = treeMap.remove(arr2[i]);
            for (int j = 0; j < elm; j++) {
                newArr[index++] = arr2[i];
            }
        }
        for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
            Integer val = entry.getValue();
            for (int i = 0; i < val; i++) {
                newArr[index++] = entry.getKey();
            }
        }

        return newArr;
    }
    public static void main(String[] args){
        Two_Arr_order_1122 order = new Two_Arr_order_1122();
        int[] arr1 = new int[]{2,3,1,3,2,4,6,7,9,2,19};
        int[] arr2 = new int[]{2,1,4,3,9,6};
        System.out.println(Arrays.toString(order.relativeSortArray(arr1,arr2)));

        arr1 = new int[]{2,21,43,38,0,42,33,7,24,13,12,27,12,24,5,23,29,48,30,31};
        arr2 = new int[]{2,42,38,0,43,21};
        System.out.println(Arrays.toString(order.relativeSortArray(arr1,arr2)));

    }
}
