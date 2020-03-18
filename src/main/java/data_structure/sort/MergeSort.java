package data_structure.sort;

import java.util.Arrays;

/**
 * 归并排序的基本思路:[8,7,6,5,4,3,2,1]
 * 将数据分为2部分,使两部分均有序：[5,6,7,8]、[1,2,3,4]
 * 然后有第三个数组[0,0,0,0,0,0,0,0]，然后依次比较两部分的第一个元素，小的放到数组里面。
 *
 * 可以使用递归排序完成这个过程，若数组元素数量为1，直接返回该数组元素，否则将数组分为前后2个部分，依次进行归并排序
 */
public class MergeSort {
    public static void sort(int[] a){
        sort(a,0,a.length -1);
    }
    /**
     * 对[left,right]区间的数据执行归并排序，需要注意的是，left、right为闭区间
     * @param a
     * @param left
     * @param right
     */
    private static void sort(int[] a,int left,int right){
        if(right  == left){
            return;
        }
      //  System.out.println("left:"+left+" right:"+right);
        int center = (left+right)/2;
        sort(a,left,center);
        sort(a,center+1,right);
        int[] newArr = new int[right - left+1];
        int i = left,j=center+1,index=0;
        while(index<newArr.length){ //左右有序后需要将左右有序的数据拷贝到一个新的数组里面重新排序
            if(i > center){//左边的数组元素插入完成
              System.arraycopy(a,j,newArr,index,right - j+1);
              break;
            }
            if(j > right){//右边的数组元素插入完成
                System.arraycopy(a,i,newArr,index,center - i+1);
                break;
            }
            if(a[i] < a[j]){
                newArr[index] = a[i++];
            }else{
                newArr[index] = a[j++];
            }
            index++;
        }
        System.arraycopy(newArr,0,a,left,newArr.length);
    }

    public static void main(String[] args){
        int[] a = new int[100];
        for (int i = 99; i >=0 ; i--) {
            a[i] = i;
        }
        System.out.println(Arrays.toString(a));
        sort(a);

        System.out.println(Arrays.toString(a));
    }
}
