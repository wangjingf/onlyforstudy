package data_structure.acm.poj.leetcode;

import java.util.Arrays;

/**
 * 给定两个大小为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的中位数。
 *
 * 进阶：你能设计一个时间复杂度为 O(log (m+n)) 的算法解决此问题吗
 * 基本思路：淘汰机制,依次遍历m+n/2/2 个元素，淘汰，然后
 * 注意事项：一次淘汰的数据只能有(n+1)/2个
 */
public class Arr_find_median {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if(nums1 == null){
            nums1 = new int[0];
        }
        if(nums2 == null){
            nums2 = new int[0];
        }
        int totalLength = nums1.length + nums2.length;
        if(totalLength %2 == 0){
            int mid_1 = findMedianSortedArrays(nums1, nums2,totalLength / 2 );
            int mid_2 = findMedianSortedArrays(nums1, nums2,totalLength / 2-1);
            return ( mid_1 + mid_2 ) / 2.0;
        }else{
            return findMedianSortedArrays(nums1, nums2,totalLength / 2 );
        }
    }
    private int findMedianSortedArrays(int[] nums1, int[] nums2,int n) {
        if(nums1 == null|| nums1.length == 0){
            return nums2[n];
        }else if(nums2 == null || nums2.length == 0){
            return nums1[n];
        }

        if(n == 0){
            Integer num = getNum(nums1,0);
            Integer num2 = getNum(nums2,0);
            return Math.min(num,num2);
        }


        int mid  = (n+1) / 2 -1 ;


        Integer num = getNum(nums1, mid);
        Integer num2 = getNum(nums2, mid);
        if(num == null){
            num = nums1[nums1.length - 1];
            if(num > num2){ // num2的元素需要淘汰
                return findMedianSortedArrays(nums1,slice(nums2,mid),n-mid-1 );
            }
            return findMedianSortedArrays(null,nums2,n-nums1.length);
        }else if(num2 == null){
            num2 = nums2[nums2.length - 1];
            if(num2 > num){// nums1的元素需要淘汰
                return findMedianSortedArrays(slice(nums1,mid),nums2,n-mid-1 );
            }
            return findMedianSortedArrays(nums1,null,n-nums2.length);
        }else{
            if(num > num2){
                //淘汰掉的元素数量为mid_x+1
                return findMedianSortedArrays(nums1,slice(nums2,mid),n-mid-1 );
            }else{
                return findMedianSortedArrays(slice(nums1,mid),nums2,n-mid-1 );
            }
        }
    }
    private int[] slice(int[] arr,int i){
        if(i >= arr.length){
            return null;
        }
        int[] newArr = new int[arr.length -i-1];
        int index = 0;
        for (int j = i+1; j < arr.length; j++) {
            newArr[index++] = arr[j];
        }
        return newArr;
    }
    public Integer getNum(int[] nums1,int n){
        if(nums1 == null || nums1.length <= n){
            return null;
        }
        return nums1[n];
    }
    public static void main(String[] args){
      /*  int[] nums1 = new int[]{1,3};
        int[] nums2 = new int[]{2};*/
        Arr_find_median find_median = new Arr_find_median();
       /* System.out.println(find_median.findMedianSortedArrays(nums1,nums2));
        nums1 = new int[]{0,0};
        nums2 = new int[]{0,0};
        System.out.println(find_median.findMedianSortedArrays(nums1,nums2));
         nums1 = new int[]{1,2};
         nums2 = new int[]{3,4};
        System.out.println(find_median.findMedianSortedArrays(nums1,nums2));*/
        int[] nums1 = new int[]{1};
        int[] nums2 = new int[]{2,3,4,5,6};
        System.out.println(find_median.findMedianSortedArrays(nums1,nums2));
        //int[] nums = new int[]{1,2,3,4,5,6};
        //System.out.println(Arrays.toString(find_median.slice(nums,1)));
    }
}
