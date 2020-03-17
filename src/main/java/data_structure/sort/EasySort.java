package data_structure.sort;

import java.util.Arrays;

public class EasySort {


    /**
     * 冒泡排序的基本原理：第一个比第二个大就交换它们两个
     * 4,3,2,1
     * @param a
     * @return
     */
    public static int[] easySort1(int[] a){
        for (int i = 1; i < a.length; i++) {
            for (int j = 0; j < a.length - i; j++) {
                if(a[j] > a[j+1] ){ //小的往前冒泡
                    int temp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = temp;
                    System.out.println(Arrays.toString(a));
                }
            }
        }
        return a;
    }

    /**
     * 插入排序，基本思路，将后面每个元素放在应该被放在的位置上
     * 如针对数组：[4,3,2,1]
     * 第一趟：比较 4,3 将3,4更换位置
     * 第二趟2依次与3,4比较后，确定应该插入到3之前，序列变为2,3,4,1
     * 第四趟将1与2,3,4比较，确定1应该插入到2之前
     * a[2] 比较a[1]
     * a[3]与a[1],a[2]比较
     * @param a
     * @return
     */
    public static int[] insertSort(int[] a){
        for (int i = 0; i < a.length-1; i++) { //i维持了当前有序的指针列表
            int j = i+1;
            for (int k = 0; k < j; k++) {//依次比较j与0~i之间的数值
                if(a[j] < a[k]){ //找到j的插入位置，j应该插入到k之前
                    int temp = a[j];
                    for (int l = j ; l > k; l--) {//移动k~j之间的元素
                        a[l] = a[l-1];
                    }
                    a[k] = temp;
                }
            }
        }
        return a;
    }

    /**
     * insertSort的主要问题是，每次都从0开始比较，找到插入位置后才将元素后移，
     * 实际上可以有一种更简单的方法，从i开始依次比较i-1,i-2,...0之间的元素，若i-1不满足要求，则直接就能开始移动元素了
     * @param a
     */
    public static void insertSort1(int[] a){
        int j;
        for (int i = 0; i < a.length-1; i++) {
            int temp = a[i+1];
            for (j=i; j >=0  ; j--) {
                if(a[j] > temp){
                    a[j+1] = a[j];
                }else{ //应该插入到j的后一个位置
                    break;
                }
            }
            a[j+1] = temp;
        }
    }

    /**
     * 更简单的方式
     * @param a
     */
    public static void insertSort2(int[] a){
        int j;
        for (int i = 1; i < a.length; i++) {
            int temp = a[i];
            for (j = i; j >0 && temp < a[j-1]  ; j--) {
                a[j] = a[j - 1];
            }
            a[j] = temp;
        }
    }

    /**
     * // 8,7,6,5,4,3,2,1 要完成8,6,4,2的有序
     * 写shell排序的基本转换思路，先将插入排序改造为基于n有序的，如
     *  int gap = 2;
     *  for (int i = 2; i < a.length; i+=2) {
     *             int temp = a[i];
     *             for (j = i; j >1 && temp < a[j-2]  ; j-=2) {
     *                 a[j] = a[j - 2];
     *             }
     *             a[j] = temp;
     *         }
     *   然后再改造成gap
     *   }
     * @param a
     * @param gapItems
     */
    public static void shellSort(int []a,int[] gapItems){
        int j;
        for(int gapIndex = 0;gapIndex<gapItems.length;gapIndex++){
            int gap = gapItems[gapIndex];
            for (int index = 0; index  < gap; index++) {
                for (int i = 1+index; i < a.length; i+=gap) {
                    int temp = a[i];
                    for (j = i; j >gap-1 && temp < a[j-gap]  ; j-=gap) {
                        a[j] = a[j - gap];
                    }
                    a[j] = temp;
                }
            }
            System.out.println(Arrays.toString(a));
        }
    }

    public static void shellSort1(int []a,int[] gapItems){
        int j;
        for(int gapIndex = 0;gapIndex<gapItems.length;gapIndex++){
            int gap = gapItems[gapIndex];
                for (int i = gap; i < a.length; i++) {
                    int temp = a[i];
                    for (j = i; j >gap-1 && temp < a[j-gap]  ; j-=gap) {
                        a[j] = a[j - gap];
                    }
                    a[j] = temp;
                }

            System.out.println(Arrays.toString(a));
        }
    }
    public static void main(String[] args){
        int[] arr = new int[]{8,7,6,5,4,3,2,1};
        // easySort1(arr);
        //shellSort(arr,new int[]{3,2,1});
        shellSort1(arr,new int[]{3,2,1});
        System.out.println(Arrays.toString(arr));
    }
}
