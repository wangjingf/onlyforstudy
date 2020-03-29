package data_structure;

import data_structure.sort.CommonHeapSort;
import data_structure.sort.HeapSort;

import java.util.Comparator;

public class PriorityQueue<T> {
    CommonHeapSort<T> sort = null;
    public PriorityQueue(Comparator comparator){
        this.sort = new CommonHeapSort<>(comparator);
    }
    public PriorityQueue(){
        this.sort = new CommonHeapSort<>();
    }

    public void insert(T elm){
        sort.siftUp(elm);
    }
    public T pop(){
        return sort.siftDown();
    }
    public static void main(String[] args){
        PriorityQueue<Integer> queue = new PriorityQueue();
        for (int i = 0; i < 100; i++) {
            queue.insert(i);
        }
        Integer result = null;
        while((result = queue.pop()) != null){
            System.out.print(result+" ");
        }
    }
}
