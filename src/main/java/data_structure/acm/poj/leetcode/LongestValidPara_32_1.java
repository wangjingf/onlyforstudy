package data_structure.acm.poj.leetcode;

import java.util.Stack;

/**
 * 前一个节点的length用来存储后一个节点的匹配值
 */
public class LongestValidPara_32_1 {
    static class Node { // length代表了当前节点向后的长度

        public Node(char c) {
            this.c = c;
            length = 0;
        }

        int length;
        char c;

    }
    public int longestValidParentheses(String s) {
        if("".equals(s)){
            return 0;
        }
        int max = 0;
        int match = 0;
        Stack<Node> stack = new Stack<>();
        stack.push(new Node('-'));
        int[] arr = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);
            if(c == '('){
                stack.push(new Node('('));
            }else if(c == ')'){
                Node peek = stack.peek();
                if(peek.c == '('){
                    Node top = stack.pop();
                    int length = stack.peek().length=stack.peek().length+top.length+1;
                    max = Math.max(length,max);
                }else{//重新清0
                    stack.peek().length = 0;
                }
            }
        }
        return max*2;
    }
    public static void main(String[] args){
        LongestValidPara_32_1 paramMatch = new LongestValidPara_32_1();
        System.out.println(paramMatch.longestValidParentheses(")()())()()("));
    }
}
