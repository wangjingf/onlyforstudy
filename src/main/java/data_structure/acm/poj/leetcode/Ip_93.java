package data_structure.acm.poj.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * ip复原
 */
public class Ip_93 {
    public List<String> restoreIpAddresses(String s) {
        List<String> ret = new LinkedList<>();
        if(s.length()<4 || s.length() > 12){
            return ret;
        }

        String p1,p2,p3,p4;
        for (int i = 1; i <= 3; i++) {
            p1 = s.substring(0,i);
            for (int j = 1; j <= 3; j++) {
                if(i+j>=s.length()){
                    break;
                }
                p2 = s.substring(i,i+j);
                for (int k = 1; k <= 3; k++) {
                     if(i+j+k>=s.length()){
                         break;
                     }
                     p3 = s.substring(i+j,i+j+k);
                     p4 = s.substring(i+j+k);
                     if(isValid(p1) && isValid(p2) && isValid(p3) && isValid(p4)){
                         ret.add(String.format("%s.%s.%s.%s",p1,p2,p3,p4));
                     }
                }
            }
        }
        return ret;
    }

    private boolean isValid(String p1) {

        if(p1.charAt(0) == '0' && !"0".equals(p1) ){
            return false;
        }else if(Integer.valueOf(p1)>255){
            return false;
        }
        return true;
    }
    public static void main(String[] args){
        Ip_93 ip = new Ip_93();
        System.out.println(ip.restoreIpAddresses("0279245587303"));
        System.out.println(ip.restoreIpAddresses("0000"));
        System.out.println(ip.restoreIpAddresses("21313122"));
    }
}
