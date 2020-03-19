package data_structure.sort;

import java.util.Arrays;

public class QuickSort {
    static int median3(int[] a,int left,int right){
       if(left == right){
           return left;
       }
       int center = (left+right) / 2;
       if(a[left] > a[center]){
           swap(a,left,center);
       }
       if(a[left] > a[right]){ //经过前两部比较，已经确保了left是最小的
           swap(a,left,right);
       }
       if(a[center] > a[right]){
           swap(a,center,right);
       }
       int centerValue = a[center];
       swap(a,center,right-1);
       return centerValue;
   }
    /**
     * 排序[left,right],注意含left、right
     * 比jdk的实现慢
     * @param a
     * @param left
     * @param right
     */
    public static void sort(int[] a,int left,int right){
        if(left == right){
            return;
        }
        int centerValue = median3(a,left,right);

        //System.out.println("left:"+a[left]+" center:"+a[center]+" right:"+a[right]);
        if(left+2 >= right){
            return;
        }
        int i = left+1,j = right -2;

        while(i < j){

            while(a[i] < centerValue){i++;}
            while(a[j] > centerValue){j--;}
           // System.out.println(Arrays.toString(a));
            if(i<j){
                swap(a,i,j);
            }
           // System.out.println("i:"+i+" j:"+j);
            //System.out.println(Arrays.toString(a));
        }
        swap(a,i,right-1); //将k交换回来
        sort(a,left,i-1);
        sort(a,i+1,right);
    }

    public static void swap(int[] a,int i,int j){
        if(i==j){
            return;
        }
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    public static void main(String[] args){
        HeapSort heapSort = new HeapSort();
        int length = 100000000;
        int[] a = new int[length];
        int[] b = new int[length];
        for (int i = length - 1; i >= 0 ; i--) {
            a[i] = length-i;
            b[i] = a[i];
        }
     //   System.out.println(Arrays.toString(a));
        long start = System.currentTimeMillis();
        sort(a,0,a.length-1);
        System.out.println("cost::"+(System.currentTimeMillis()-start));

        for (int i = 0; i < 100; i++) {
            System.out.print(a[i]+" ");
        }
        System.out.println();
    }
}
