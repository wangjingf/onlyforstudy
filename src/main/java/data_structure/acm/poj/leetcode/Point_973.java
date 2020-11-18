package data_structure.acm.poj.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 最接近远点的K个点
 */
public class Point_973 {
    public int[][] kClosest(int[][] points, int K) {
        Arrays.sort(points, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                int d1 = o1[0]*o1[0]+o1[1]*o1[1];
                int d2 = o2[0]*o2[0]+o2[1]*o2[1];
                return d1-d2;
            }
        });
        int[][] ret = new int[K][2];
        for (int i = 0; i < K; i++) {
            ret[i][0] = points[i][0];
            ret[i][1] = points[i][1];
        }
        return ret;
    }
     public static void main(String[] args){
         Point_973 point = new Point_973();
         int[][] points = new int[][]{new int[]{1,3},new int[]{-2,-2}};
         System.out.println(Arrays.asList(point.kClosest(points,1)));
         //[[3,3],[5,-1],[-2,4]]
         points = new int[][]{new int[]{3,3},new int[]{5,-1},new int[]{-2,4}};
         System.out.println(Arrays.asList(point.kClosest(points,2)));
     }
}
