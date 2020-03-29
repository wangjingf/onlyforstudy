package data_structure.sort;

import java.lang.reflect.Array;
import java.util.Comparator;

public class CommonHeapSort<T> {
    Object[] a = null;
    int size;
     Comparator comparator;
     public CommonHeapSort(){
         this(null);
     }
    public CommonHeapSort(Comparator comparator){
        a = new Object[10];
        size = 0;
        this.comparator = comparator;
    }
    public void enlargeArray(int size){
        Object[] newArr = new Object[size];
        System.arraycopy(a,0,newArr,0,a.length);
        a = newArr;
    }
    int compare(Object elm,Object elm2){
        if(comparator != null){
            return comparator.compare(elm,elm2);
        }else{
            if(elm == null){
                if(elm2==null){
                    return 0;
                }else {
                    return -1;
                }
            }else{
                return ((Comparable)elm).compareTo(elm2);
            }
        }
    }
    /**
     * 依次向上比较
     * 上滤的基本逻辑是：将当前元素插入到末尾，比较与父元素的大小，
     * 若小于父元素，则父元素下来，当前元素占据父元素的位置
     * @param elm
     */
    public  void siftUp(T elm){

        int currIndex = size;
        if(currIndex>a.length -1){
            enlargeArray(a.length*2+1);
        }
        int parentIndex;
        while(true){
            if(currIndex == 0){ //等于0说明size到达第一个元素了了
                a[currIndex] = elm;
                break;
            }
            T parent = (T) a[currIndex/2];

            if(compare(elm,parent) < 0){ //当前元素小于父元素，向上移动
                a[currIndex] = parent;//父向下移动
                currIndex = currIndex/2;//更换sizeIndex
            }else{
                a[currIndex] = elm;
                break;
            }
        }
        size = size+1;
    }
    public  T siftDown(){
        int currIndex = size;
        if(currIndex==0){
            return null;
        }
        T lastElm = (T) a[currIndex-1];
        int index = 0;
        T ret = (T) a[0];
        while(index < currIndex){
            int leftChildIndex = index*2+1;
            int rightChildIndex = index*2+2;
            T rightValue;
            if( leftChildIndex > size-1){ //index元素无子节点了，name移动到该元素即可
                a[index] = lastElm;
                break;
            }else if(rightChildIndex > size -1){//当前元素无右子节点
                rightValue = null;
            }else{
                rightValue = (T) a[rightChildIndex];
            }
            if(compare(lastElm,a[leftChildIndex]) <= 0 && (rightValue == null || compare(lastElm,rightValue) <= 0 )){ //小于左右项则刚好可以插入
                a[index] = lastElm;
                break;
            }else{//当前元素需要与左元素或者右元素置换
                if(rightValue == null || compare(a[leftChildIndex],rightValue) <= 0){//做元素小于右元素
                    a[index] = a[leftChildIndex];
                    index =  leftChildIndex;
                }else{
                    a[index] = rightValue;
                    index = rightChildIndex;
                }
            }
        }
        size=size-1;
        return ret;
    }
}
