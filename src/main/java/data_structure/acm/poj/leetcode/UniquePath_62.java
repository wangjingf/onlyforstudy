package data_structure.acm.poj.leetcode;

/**
 * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
 *
 * 示例:
 *
 * 输入: [0,1,0,3,12]
 * 输出: [1,3,12,0,0]
 * 说明:
 *
 * 必须在原数组上操作，不能拷贝额外的数组。
 * 尽量减少操作次数。
 *
 * 基本思路：非0的元素移动到头部，剩余的都填0
 */
public class UniquePath_62{
    public int uniquePaths(int m, int n) {
        int[][] ret = new int[m][n];
        for (int i = m-1; i >= 0 ; i--) {
            for (int j = n-1; j >= 0 ; j--) {
                if(i==m-1 && j== n-1){
                    ret[i][j] = 1;
                    continue;
                }
               ret[i][j] = getArrVal(ret,i+1,j)+getArrVal(ret,i,j+1);      
            }
        }
        return ret[0][0];
    }
    public int getArrVal(int[][] arr,int i,int j){
        if(i >= arr.length ){
            return 0;
        }
        if(j >= arr[i].length){
            return 0;
        }
        return arr[i][j];
    }
    public static void main(String[] args){
        UniquePath_62 uniquePath_62 = new UniquePath_62();
        System.out.println(uniquePath_62.uniquePaths(3,2));
        System.out.println(uniquePath_62.uniquePaths(7,3));
    }
}