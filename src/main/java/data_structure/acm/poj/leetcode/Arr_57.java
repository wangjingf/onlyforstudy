package data_structure.acm.poj.leetcode;

public class Arr_57 {
    public int[][] insert(int[][] intervals, int[] newInterval) {

        int[] arr = new int[intervals.length];
        int index = 0;
        for (int i = 0; i < intervals.length; i++) {
            for (int j = 0; j < 2; j++) {
                arr[index++] = intervals[i][j];
            }
        }
        int left = findInsertPos(arr,newInterval[0]);
        int right = findInsertPos(arr,newInterval[1]);
        if(left%2 == 1){
            left = left -1;
        }else{

        }
        return null;
    }
    int findInsertPos(int[] intervals,int val){
        int left = 0;
        for (int i = 0; i < intervals.length; i++) {
            if(val > intervals[i]){
                return i;
            }
        }
        return left;
    }
    public static void main(String[] args){

    }
}
