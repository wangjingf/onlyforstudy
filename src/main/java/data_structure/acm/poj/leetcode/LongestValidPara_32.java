package data_structure.acm.poj.leetcode;

/**
 * 最长有效括号。n^2解法 。一次遍历第一个元素、第二个元素、第三个元素,......
 */
public class LongestValidPara_32 {
    public int longestValidParentheses(String s) {
        if(s==null || "".equals(s)){
            return 0;
        }
        int i = 0;
        int max = 0;
        char c = s.charAt(i);
        int prev = 0;
        while (i<s.length()){
            int match = longestChar(s, i);
            if(match > 0){
                i+=match;
                if(prev>0){
                    match = prev+match;
                }
                prev = match;
                if(max<match){
                    max = match;
                }

            }else{
                prev = 0;
                i++;
            }
        }
        return max;
    }
    public int longestChar(String s,int i){
        int originalIndex = i;
        char c = s.charAt(i);
        if(c == ')'){
            return 0;
        }
        int match = 0;
        while(i<s.length()){
            if(s.charAt(i) == '('){
                match++;
            }else if(s.charAt(i) == ')'){
                match--;
            }
            if(match==0){
                return i-originalIndex+1;
            }
            i++;
        }
        return 0;
    }
    public static void main(String[] args){
        LongestValidPara_32 paramMatch = new LongestValidPara_32();
        System.out.println(paramMatch.longestValidParentheses("(()()(())(("));
        System.out.println(paramMatch.longestValidParentheses("(()"));
    }
}
