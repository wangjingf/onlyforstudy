package data_structure.acm.poj.leetcode;

import java.util.*;

/**
 * 字母异位词分组，关键要点：将字符串排好序
 */
public class Alpha_49 {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String,LinkedList> map = new HashMap<>();
        for (String str : strs) {
            String ret = reorg(str);
            map.putIfAbsent(ret,new LinkedList());
            map.get(ret).add(str);
        }
        List<List<String>> ret = new LinkedList<>();
        for (Map.Entry<String, LinkedList> entry : map.entrySet()) {
            ret.add(entry.getValue());
        }
        return ret;
    }
    String reorg(String str){
        char[] arr = str.toCharArray();
        Arrays.sort(arr);
        return String.valueOf(arr);
    }
    public static void main(String[] args){
        String[] strs = new String[]{"eat", "tea", "tan", "ate", "nat", "bat"};
        Alpha_49 alpha_49 = new Alpha_49();
        List<List<String>> list = alpha_49.groupAnagrams(strs);
        System.out.println(list);
    }
}
