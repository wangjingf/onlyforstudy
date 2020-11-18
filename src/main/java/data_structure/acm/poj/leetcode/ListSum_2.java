package data_structure.acm.poj.leetcode;

/**
 * 反向链表加减：
 * 基本思路：先找到比较长的链表
 */
public class ListSum_2 {
    /**
     * Definition for singly-linked list.
     * public class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode() {}
     *     ListNode(int val) { this.val = val; }
     *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int length1 = length(l1);
        int length2 = length(l2);
        ListNode minNode = l1;
        ListNode maxNode = l2;
        if(length1 > length2){
            minNode = l2;
            maxNode = l1;
        }
        ListNode node = new ListNode(0);
        ListNode head = node;
        int temp = 0;
        while (maxNode != null){
            int maxVal = maxNode.val;
            int minVal = 0;
            if(minNode != null){
                minVal = minNode.val;
                minNode = minNode.next;
            }
            int sum = maxVal+minVal+temp;
            int val = sum % 10;
            temp = sum / 10;
            node.next = new ListNode(val);
            node = node.next;
            maxNode = maxNode.next;
        }
        if(temp != 0){
            node.next = new ListNode(temp);
        }
        return head.next;
    }
    int length(ListNode node){
        if(node == null){
            return 0;
        }
        int i = 0;
        while (node.next !=null){
            i++;
            node = node.next;
        }
        return i;
    }
    static class ListNode{
        int val;
        ListNode next;
        ListNode(int val){this.val = val;};
        ListNode(int val,ListNode next){
            this.val = val;
            this.next = next;
        }
    }
    public static ListNode buildListNode(int[] nums){
        ListNode node = new ListNode(0);
        ListNode head = node;
        for (int num : nums) {
            node.next = new ListNode(num);
            node = node.next;
        }
        return head.next;
    }
    static void dump(ListNode node){
        while (node != null){
            System.out.print(node.val+" ");
            node = node.next;
        }
        System.out.println();
    }
    public static void main(String[] args){
        ListSum_2 sum_2 = new ListSum_2();
        ListNode l1 = buildListNode(new int[]{2,4,3});
        ListNode l2 = buildListNode(new int[]{5,6,4});
        dump(sum_2.addTwoNumbers(l1,l2));

         l1 = buildListNode(new int[]{2,4,9});
         l2 = buildListNode(new int[]{5,6,4});
        dump(sum_2.addTwoNumbers(l1,l2));

        l1 = buildListNode(new int[]{});
        l2 = buildListNode(new int[]{5,6,4});
        dump(sum_2.addTwoNumbers(l1,l2));


        l1 = buildListNode(new int[]{2,4,9,2});
        l2 = buildListNode(new int[]{5,6,4});
        dump(sum_2.addTwoNumbers(l1,l2));
    }
}
