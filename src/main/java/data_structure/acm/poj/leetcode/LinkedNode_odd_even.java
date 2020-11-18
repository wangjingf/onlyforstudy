package data_structure.acm.poj.leetcode;

/**
 * 我的思路不太方便，要想快速写出来，应该是：
 * even = odd.next;
 * odd = even.next;
 */
public class LinkedNode_odd_even {
    public ListNode oddEvenList(ListNode head) {
        ListNode evenPointer = null;
        ListNode oddPointer = null;
        ListNode firstNode = head;
        ListNode evenFirstNode = null;
        int i = 0;

        while (head != null){
            if(i % 2 == 0){
                if(oddPointer == null){
                    oddPointer = head;
                }else{
                    oddPointer.next = head;
                    oddPointer = oddPointer.next;
                }
            }else{
                if(evenPointer == null){
                    evenPointer = head;
                    evenFirstNode = head;
                }else{
                    evenPointer.next = head;
                    evenPointer = evenPointer.next;
                }

            }
            head = head.next;
            i++;
        }
        if(evenPointer != null){
            evenPointer.next = null;
            oddPointer.next = evenFirstNode;
        }
        return firstNode;
    }
    static class   ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      public void dump(){
         ListNode curr = this;
         while (curr != null){
             System.out.print(curr.val+" ");
             curr = curr.next;

         }System.out.println();
      }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
  }
  public static ListNode newNodeList(int[] arr){
        ListNode head = new ListNode();
        ListNode first = head;
      for (int i = 0; i < arr.length; i++) {
          head.next = new ListNode(arr[i]);
          head = head.next;
      }
      return first.next;
  }

  public static void main(String[] args){
      LinkedNode_odd_even even = new LinkedNode_odd_even();
      ListNode node = newNodeList(new int[]{1, 2, 3, 4, 5});
      ListNode newNode = even.oddEvenList(node);
      newNode.dump();
      node = newNodeList(new int[]{2,1,3,5,6,4,7});
      newNode = even.oddEvenList(node);
      newNode.dump();
  }
}
