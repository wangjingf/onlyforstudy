package data_structure.string;

import java.util.Arrays;

public class KMPMatcher {
    /**
     * next数组的含义：
     * 针对next[j]的含义是：当子串的第j个字符串与主串的第i个字符不匹配时，下一个需要与i比较的index,也就意味着，前k-1个子串与后k-1个子串已经匹配上了
     * 求解基本思路：
     * 用subStr代表子串，str代表主串
     * 1. k=next[i-1]的值说明当subStr[i-1]与str[j]不匹配时，需要将subStr移动到k的位置重新匹配str[j]。也就是说：subStr[0,k)元素与subStr[i-k+1,i-2)的元素已经完全匹配上了
     * 如针对串abacbac,next[3]为1,相当于strStr[0]与subStr[3-1]已经匹配上了
     * 2. 下一个需要匹配subStr[k]与主串str[i-1]的值，若值一致，则next[i]=k+1，否则相当于已知[0,k)元素与[i-k+1,i-2)匹配，当第k与i-1不匹配时，求下一个需要与i-1匹配的元素。
     * 显然，这又是一个子串匹配问题
     * 下一个需要匹配的元素肯定是next[k]啊，以此类推。
     * @param str
     * @return
     */
    public static Integer[] getNext(String str){
        Integer[] next = new Integer[str.length()];
        next[0] = -1;
        //已知next[i-1]=k,求next[i] k=0说明一个字符也没匹配上
        for (int i = 1; i < str.length(); i++) {
            int k = next[i-1];
            while(k>-1) {
                if (str.charAt(k) == str.charAt(i-1)) {//next[i-1]说明第i-2个字符与第k-1个字符匹配上了，下一个需要匹配k与i-1
                    next[i] = k + 1;
                    break;
                } else {//没匹配上需要求下一个能与i-1匹配的元素，相当于已知k未匹配上，求下一个需要与i-1匹配的元素。下一个元素肯定是next[k]啊
                    k = next[k];
                }
            }
            if(next[i] == null){
                next[i] = 0;
            }
        }
        return next;
    }
    public static int indexOf(String str,String subStr){
        int i = 0,j=0;
        Integer[] next = getNext(subStr);
        while(i<str.length()){
            if(str.charAt(i) == subStr.charAt(j)){
                j++;
                i++;
                if(j == subStr.length()){
                    return i-subStr.length();
                }
            }else{
                if(j>0){ //已经匹配了部分子串，需要再次匹配
                    j = next[j];
                }else{
                    i++;
                }
            }
        }
        return -1;
    }
    public static void main(String[] args){
        String str = "aaaaaaaaaaaaaaaaaaaaaaaa";
        Integer[] result = getNext(str);
        System.out.println(Arrays.toString(result));
        // System.out.println(Arrays.toString(result));
        System.out.println(indexOf("ababccccababccccabababaceeeeabab","abababac"));
       // System.out.println(indexOf("abc","bc"));
        //System.out.println(getNext("abababac"));
    }
}
