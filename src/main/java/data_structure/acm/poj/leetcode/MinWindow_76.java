package data_structure.acm.poj.leetcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MinWindow_76 {
    public String minWindow(String str, String t) {
        if("".equals(str)){
            return "";
        }
        Map<Character,Integer> map = new HashMap<>();
        for (int i = 0; i < t.length(); i++) {
            map.put(t.charAt(i),0);
        }
        int size = map.size();
        int minLength = Integer.MAX_VALUE,minIndex=0,highIndex = 0;
        int low=0,high = 0;
        int alreadyMatchCnt = 0;
        while (high< str.length()){
            char c = str.charAt(high);
            Integer old = map.get(c);
            if(old!=null && old == 0){
                map.put(c,1);
                alreadyMatchCnt++;
                if(alreadyMatchCnt == size){
                    while(map.get(str.charAt(low)) == null){
                        low++;
                    }
                     map.put(str.charAt(low),0);

                     if(minLength > high-low){
                        minLength = high-low;
                        minIndex = low;
                        highIndex = high+1;
                         System.out.println(str.substring(minIndex,highIndex));
                     }
                     alreadyMatchCnt--;
                    low++;
                    while(map.get(str.charAt(low)) == null){
                        low++;
                    }
                    System.out.println("low::"+low);
                }
            }
            high++;
        }
        return str.substring(minIndex,highIndex);
    }

    public static void main(String[] args){
        MinWindow_76 minWin = new MinWindow_76();
        System.out.println(minWin.minWindow("ADOBECODEBANC","ABC"));
    }
}
