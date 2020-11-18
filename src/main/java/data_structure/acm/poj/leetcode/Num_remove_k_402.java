package data_structure.acm.poj.leetcode;

import java.util.LinkedList;
import java.util.List;

/**
 *
 给定一个以字符串表示的非负整数 num，移除这个数中的 k 位数字，使得剩下的数字最小。

 注意:

 num 的长度小于 10002 且 ≥ k。
 num 不会包含任何前导零。
 示例 1 :

 输入: num = "1432219", k = 3
 输出: "1219"
 解释: 移除掉三个数字 4, 3, 和 2 形成一个新的最小的数字 1219。
 基本思路：从最高位开始，若当前位数比后面一位小，移除当前位，否则移除后一位。当前位是最后
 一位，直接移除。为了移除方便，将数字转换为链表

 这个解法有点复杂，用栈的话会简单一点。
 用栈保存当前最小序列结果。将栈顶元素与当前元素比较即可，当前元素小于栈顶元素，移除栈顶元素，否则入栈
 */
public class Num_remove_k_402 {
    public String removeKdigits(String num, int k) {
        if(k >= num.length() ){
            return "0";
        }
        LinkedList<Character> nums = new LinkedList<>();
        Node head = new Node(null);
        Node first = head;
        for (int i = 0; i < num.length(); i++) {
            Node node = new Node(num.charAt(i));
            head.next = node;
            node.prev = head;
            head = head.next;
        }
        head = first.next;
        for (int i = 0; i < k; i++) {
            while (true){
                Character c = head.letter;
                if(head.next == null){ // 当前元素是最后一个元素
                    head.prev.next = null;
                    head = head.prev;
                    break;
                }else{ // 当前元素不是最后一个
                    if(c > head.next.letter){// 当前大于后一个，应该移除当前元素,移除后，将当前元素置为前一个元素，若前一个元素为空，子置为后一个元素
                        head.prev.next = head.next;
                        head.next.prev = head.prev;
                        if(head.prev.letter == null){
                            head = head.prev.next;
                        }else{
                            head = head.prev;
                        }
                        break;
                    }else{ // 当前小于后一个元素，应该判断后一个元素
                        head = head.next;
                    }
                }
            }
        }
        return toString(first.next);
    }
    public String toString(Node head){
        if(head == null){
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        while (head!= null){
            if(head.letter != '0'){
                isFirst = false;
            }
            if(head.letter == '0'){
                if(!isFirst){
                    sb.append(head.letter);
                }
            }else{
                sb.append(head.letter);
            }
            head = head.next;
        }
        String s = sb.toString();
        if(s == null || "".equals(s)){
            return "0";
        }
        return s;
    }
    static class Node{
        Character letter;
        Node prev;
        Node next;

        public Node(Character letter) {
            this.letter = letter;
        }
    }
    public static void main(String[] args){
        Num_remove_k_402 numRemove = new Num_remove_k_402();
        String s = "1432219";
        System.out.println(numRemove.removeKdigits(s,1));
        System.out.println(numRemove.removeKdigits(s,2));
        System.out.println(numRemove.removeKdigits(s,3));

        s = "10200";
        System.out.println(numRemove.removeKdigits(s,1));
        System.out.println(numRemove.removeKdigits("10",2));
    }
}
