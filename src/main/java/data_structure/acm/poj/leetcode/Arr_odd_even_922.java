package data_structure.acm.poj.leetcode;

import java.util.Arrays;

/**
 * 一般奇数一般偶数，a[i]是奇数返回奇数，a[i]是偶数时返回偶数
 */
public class Arr_odd_even_922 {
    public int[] sortArrayByParityII(int[] A) {
        if(A == null){
            return null;
        }
        int[] odd = new int[A.length/2];
        int[] even = new int[A.length/2];
        int oddIndex = 0;
        int evenIndex = 0;
        for (int i = 0; i < A.length; i++) {
            if(A[i]%2 == 0){
                odd[oddIndex++] = A[i];
            }else{
                even[evenIndex++] = A[i];
            }
        }
        for (int i = 0; i < A.length / 2; i++) {
            A[i*2] = odd[i];
            A[i*2+1] = even[i];
        }
        return A;
    }
    public static void main(String[] args){
        Arr_odd_even_922 oldEven = new Arr_odd_even_922();
        int[] A = new int[]{4,2,5,7};
        oldEven.sortArrayByParityII(A);
        System.out.println(Arrays.toString(A));
    }
}
