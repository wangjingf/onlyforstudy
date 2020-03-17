package data_structure.sort;

import java.util.Arrays;

public class ShellSort{
    public static void main(String[] args){
        int[] arr = new int[]{8,7,6,5,4,3,2,1};
        sort(arr);
        System.out.println(Arrays.toString(arr));
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
    public static void sort(int[] a){
        int j;
        for (int gap = a.length/2; gap >0; gap/=2) {
            for (int i = gap; i < a.length; i++) {
                int tmp = a[i];
                for(j = i; j>= gap && tmp<a[j - gap];j-=gap)
                    a[j] = a[j - gap];
                a[j] = tmp;
            }
        }
    }

}
