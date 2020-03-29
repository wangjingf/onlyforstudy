package data_structure.sort;

import java.util.Arrays;

/**
 * 堆排序不是一种稳定的算法
 */
public  class HeapSort {

    int[] a = new int[10];
    int size;
    public void enlargeArray(int size){
        int[] newArr = new int[size];
        System.arraycopy(a,0,newArr,0,a.length);
        a = newArr;
    }
    public HeapSort(){}
    public HeapSort(int size){
        a = new int[size];
    }
    /**
     * 依次向上比较
     * 上滤的基本逻辑是：将当前元素插入到末尾，比较与父元素的大小，
     * 若小于父元素，则父元素下来，当前元素占据父元素的位置
     * @param elm
     */
    public  void siftUp(int elm){
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
            int parent = a[currIndex/2];

            if(elm<parent){ //当前元素小于父元素，向上移动
                a[currIndex] = parent;//父向下移动
                currIndex = currIndex/2;//更换sizeIndex
            }else{
                a[currIndex] = elm;
                break;
            }
        }
        size = size+1;
    }

    /**
     * 标准的实现,有bug
     * @param elm
     */
    public void siftUp1(int elm){
        if(size == a.length -1){
            enlargeArray(a.length*2+1);
        }

        int hole = size;
        for(;hole>0 && elm < a[hole/2];hole/=2){
            a[hole] = a[hole/2];
        }
        a[hole] = elm;
        size++;
    }
    public boolean isEmpty(){
        return size == 0;
    }
    /**
     * 将数组a的首元素移除,
     *  移除后依次比较末尾元素与首元素的左右两个节点，若末尾元素可插入，则直接放入。
     *  否则左右节点的较小值上去，空白节点变成较小值的位置
     *
     */
    public  Integer siftDown(){
        int currIndex = size;
        if(currIndex==0){
            return null;
        }
        int lastElm = a[currIndex-1];
        int index = 0;
        int ret = a[0];
        while(index < currIndex){
            int leftChildIndex = index*2+1;
            int rightChildIndex = index*2+2;
            int rightValue;
            if( leftChildIndex > size-1){ //index元素无子节点了，name移动到该元素即可
                a[index] = lastElm;
                break;
            }else if(rightChildIndex > size -1){//当前元素无右子节点
                rightValue = Integer.MAX_VALUE;
            }else{
                rightValue = a[rightChildIndex];
            }
            if(lastElm <= a[leftChildIndex] && lastElm <= rightValue ){ //小于左右项则刚好可以插入
                a[index] = lastElm;
                break;
            }else{
                if(a[leftChildIndex] <= rightValue){
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

    public int deleteMin(){
        if(isEmpty()){
            throw new ArrayIndexOutOfBoundsException();
        }
        int minItem = a[0];
        a[0] = a[size--];//将最后一个元素移动到第一位
        percolateDown(0); //
        return minItem;
    }
    private void percolateDown(int hole){
        int child;
        int tmp = a[hole];
        for (;hole*2<=size;hole=child){
            child = hole*2;
            if(child != size && a[child+1]<a[child]){
                child++;
            }
            if(a[child] < tmp){
                a[hole] = a[child];
            }else{
                break;
            }
        }
        a[hole] = tmp;
    }
    public void printArr(){
        for (int i = 0; i < size; i++) {
            System.out.print(a[i]+" ");
        }
        System.out.println();
    }
    public static void main(String[] args){
        HeapSort heapSort = new HeapSort();
        for (int i = 10;i>=0 ;i--) {
            heapSort.siftUp(i);
        }
        heapSort.printArr();
        System.out.println("begin siftDown");
        for (int i = 0; i <= 10; i++) {
           Integer outElm =  heapSort.siftDown();
            System.out.print(outElm+" ");
        }
        System.out.println();
    }
}
