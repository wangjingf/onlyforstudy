package data_structure.acm.poj.leetcode;

public class Substr_214 {
    public String shortestPalindrome(String s) {
        if(s.length() == 0 || s.length()==1){
            return s;
        }
        int length = s.length();
        int mid = 0;
        boolean isOdd = false;
        if(length % 2 == 0){
            mid = length / 2-1;
        }else{
            isOdd = true;
            mid = length /2 ;
        }
        for (int i = mid; i >=0 ; i--) {


            if(isMatchOddStr(s,i)){
                String str = s.substring(i + 1);
                return reverse(str)+s.charAt(i)+str;
            }
            if(isOdd && i== mid){
                continue;
            }
            if(isMatchEvenStr(s,i)){
                String str = s.substring(i +1);
                return reverse(str)+str;
            }
        }
        return s;
    }
    public String reverse(String s){
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = s.length()-1; i >=0 ; i--) {
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }
    public boolean isMatchEvenStr(String s,int midIndex){
        for (int i = 1; i <=midIndex  ; i++) {
            if(s.charAt(midIndex-i+1) != s.charAt(midIndex+i)){
                return false;
            }
        }
        return true;
    }
    public boolean isMatchOddStr(String s,int midIndex){
        for (int i = 1; i <=midIndex  ; i++) {
            if(s.charAt(midIndex-i) != s.charAt(midIndex+i)){
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args){
        Substr_214 substr_214 = new Substr_214();

     /*  System.out.println(substr_214.shortestPalindrome(""));
       System.out.println(substr_214.shortestPalindrome("abcd"));
       System.out.println(substr_214.shortestPalindrome("123"));
       System.out.println(substr_214.shortestPalindrome("121"));
       System.out.println(substr_214.shortestPalindrome("12"));*/
        System.out.println(substr_214.shortestPalindrome("baa"));
    }
}
