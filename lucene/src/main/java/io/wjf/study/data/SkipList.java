package io.wjf.study.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 每个上级节点除了需要存储指向本级下一个元素的索引，还需要存储指向下级元素的索引
 * 比如，level1的第一个元素c,除了需要指向本级别的下一个元素c，还需要指向level0的x节点
 * c                                                   c            (skip level 2) (insert level 3)
 * c               c                 c                 c            (skip level 1) (insert level 2)
 * x   x     x     x     x     x     x     x     x     x     x      (skip level 0) (insert level 1)
 * d d d d d d d d d d d d d d d d d d d d d d d d d d d d d d d d  (posting list) (insert level 0)
 *     3     6     9     12    15    18    21    24    27    30     (df)
 *
 *  认为key不可重复。若key可重复，值应该串到一起
 */
public class  SkipList<K extends Comparable,V> {
    int maxLevel;
    /**
     * 跳表间隔
     */
    int skipInterval;
    int size;

    Node<K,V> start;

    public SkipList(int maxLevel,int skipInterval){
        this.maxLevel = maxLevel;
        this.skipInterval = skipInterval;
        start = new Node<>();
        start.nexts = new Node[maxLevel];
        size = 0;
    }
    public Node<K,V> add(K key,V value){
        Node<K,V>[] prevNodes = new Node[maxLevel];
        Node<K, V> targetNode = findPos(key, prevNodes);
        if(targetNode != null){
            targetNode.value = value;
            return targetNode;
        }
        size++;
        int level = random();
        Node newNode = new Node();
        newNode.key = key;
        newNode.value = value;
        newNode.nexts = new Node[level];
        for (int i = 0; i < level; i++) {
            newNode.nexts[i] = prevNodes[i].nexts[i];
            prevNodes[i].nexts[i] = newNode;
        }
        return newNode;
    }

    /**
     * 根据key查找前继节点
     * @param key
     * @param prevNodes 前继节点列表
     * @return 目标节点
     */
    public Node<K,V> findPos(K key,Node<K,V>[] prevNodes){
        Node<K,V> node = start;
        int i = maxLevel-1;
        /**
         * 用来保存新插入节点的前驱节点
         */
        //Node<K,V>[] prevNodes = new Node[maxLevel];
        Node<K,V> ret = null;
        while (i >= 0){
            Integer result = null;
            // node为空或者下一个元素大于当前元素，说明当前元素就是前驱
            if(node.nexts[i] == null || (result = node.nexts[i].key.compareTo(key)  ) >= 0 ){
                prevNodes[i] = node;
                if(result != null && result == 0){ // result==0说明当前元素的下一个元素就是要找的元素
                    ret = node.nexts[i];
                }
                i--;

            }else if(result < 0){
                node = node.nexts[i];
            }
        }
        return ret;
    }
    public Node<K,V> get(K key){
        Node<K,V>[] prevNodes = new Node[maxLevel];
        return findPos(key, prevNodes);
    }
    static String repeat(String key,int length){
        StringBuilder sb = new StringBuilder(key.length()*length);
        for (int i = 0; i < length; i++) {
            sb.append(key);
        }
        return sb.toString();
    }
    /**
     * 打印生成的跳表
     */
    public void show() {
        System.out.println("-------------------------");
        Object[][] arr = new Object[maxLevel][size];
        int index = 0;
        Node<K,V> node = start.nexts[0];
        while (node != null){
            for (int i = 0; i < maxLevel; i++) {
                    if(i < node.nexts.length){
                        arr[maxLevel-1-i][index] = node.key;
                    }else{
                        arr[maxLevel-1-i][index] = repeat(" ",node.key.toString().length());
                    }


            }

            node = node.nexts[0];
            index++;
        }
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if(arr[i][j] != null){
                    System.out.print(arr[i][j]+"\t");
                }else{
                    System.out.print(" \t");
                }

            }
            System.out.println();
        }
        /*for (int level = this.maxLevel - 1; level >= 0; level--) {
            System.out.printf("level: %d\t", level);
            Node<K, V> q = this.start.nexts[level];
            while (null != q) {
                System.out.print(q.key+"\t");
                q = q.nexts[level];
            }
            System.out.println();
        }*/
        System.out.println("-------------------------");
    }
    public Node<K,V> remove(K key){
        Node<K,V>[] prevNodes = new Node[maxLevel];
        Node<K,V> target = findPos(key, prevNodes);
        if(target == null){
            return null;
        }
        size--;
        for (int i = 0; i < target.nexts.length; i++) {
            prevNodes[i].nexts[i] = prevNodes[i].nexts[i].nexts[i];
        }
        return target;
    }
    /**
     * 随机出链表的层级
     * @return
     */
    int random(){
        int level = 1;
        while (Math.random() < 1.0/skipInterval && level < maxLevel){
            level+=1;
        }
        return level;
    }
    public static class Node<K extends Comparable,V> {
        K key;
        V value;
        Node[] nexts;

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

}


