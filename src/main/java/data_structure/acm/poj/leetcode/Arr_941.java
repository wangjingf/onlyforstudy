package data_structure.acm.poj.leetcode;

/**
 * class Solution {
 *     public boolean validMountainArray(int[] A) {
 *         int N = A.length;
 *         int i = 0;
 *
 *         // 递增扫描
 *         while (i + 1 < N && A[i] < A[i + 1]) {
 *             i++;
 *         }
 *
 *         // 最高点不能是数组的第一个位置或最后一个位置
 *         if (i == 0 || i == N - 1) {
 *             return false;
 *         }
 *
 *         // 递减扫描
 *         while (i + 1 < N && A[i] > A[i + 1]) {
 *             i++;
 *         }
 *
 *         return i == N - 1;
 *     }
 * }
 *
 * 线性扫描方式更优化
 */
public class Arr_941 {
    public boolean validMountainArray(int[] a) {
        if(a==null || a.length <3){
            return false;
        }
        boolean isIncrease = true;
        boolean firstDesc = true;
        int prev = a[0];
        for (int i = 1; i < a.length; i++) {
            if(a[i] > prev ){
                if(!isIncrease){
                    return false;
                }
            }else if(a[i] < prev){
                if(i==1){
                    return false;
                }
                if(isIncrease){
                    if(!firstDesc){
                        return false;
                    }else{ // 切换
                        firstDesc = true;
                        isIncrease = false;
                    }
                }
            }else{
                return false;
            }
            prev = a[i];
        }
        if(isIncrease){
            return false;
        }
        return true;
    }
    public static void main(String[] args){
        Arr_941 arr_941 = new Arr_941();
        int[] arr1 = new int[]{0,2,3,4,5,2,1,0};//true
        int[] arr2 = new int[]{0,2,3,3,5,2,1,0};//false
        int[] arr3 = new int[]{0,2}; // false
        int[] arr4 = new int[]{0,2,1,5,1};//false
        int[] arr5 = new int[]{0,1,2,3,4};//false
        int[] arr6 = new int[]{4,3,2,1,0};//false
        System.out.println(arr_941.validMountainArray(arr1));
        System.out.println(arr_941.validMountainArray(arr2));
        System.out.println(arr_941.validMountainArray(arr3));
        System.out.println(arr_941.validMountainArray(arr4));
        System.out.println(arr_941.validMountainArray(arr6));
    }
}
