package io.wjf.study.data;

import junit.framework.TestCase;

public class TestSkipList extends TestCase {

    public SkipList<Integer,Integer> randomSkipList(int size){
        SkipList<Integer,Integer> skipList = new SkipList<>(4,2);

        for (int i = 0; i < size; i++) {
            int value = (int)(Math.random()*1000);
            skipList.add(value,value);
        }
        return skipList;
    }
    public void testPrint(){
        SkipList<Integer, Integer> skipList = randomSkipList(1000);
        skipList.show();
    }
    public void testSkip(){
        SkipList<Integer,String> skipList = new SkipList<>(4,2);
        skipList.add(5,"5");
        skipList.add(1,"1");
        skipList.add(1,"1");
        skipList.add(3,"3");
        skipList.add(4,"4");
        skipList.add(6,"6");
        skipList.add(100,"100");
        skipList.add(-1,"-1");
        SkipList.Node<Integer, String> node = skipList.get(31);
        assertNull(node);
        testNode(skipList,5);
        testNode(skipList,1);
        testNode(skipList,3);
        testNode(skipList,4);
        testNode(skipList,6);
        testNode(skipList,100);

    }
    void testNode(SkipList skipList,Integer key){
        String value = ""+key;
        SkipList.Node<Integer, String> node = skipList.get(key);
        assertEquals(value,node.getValue());
        node = skipList.remove(key);
        assertEquals(value,node.getValue());
        SkipList.Node<Integer, String> newNode = skipList.get(key);
        assertNull(newNode);
    }
}
