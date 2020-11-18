package data_structure.acm.poj.leetcode;

/**
 * 给出 R 行 C 列的矩阵，其中的单元格的整数坐标为 (r, c)，满足 0 <= r < R 且 0 <= c < C。
 *
 * 另外，我们在该矩阵中给出了一个坐标为 (r0, c0) 的单元格。
 *
 * 返回矩阵中的所有单元格的坐标，并按到 (r0, c0) 的距离从最小到最大的顺序排，其中，两单元格(r1, c1) 和 (r2, c2) 之间的距离是曼哈顿距离，|r1 - r2| + |c1 - c2|。（你可以按任何满足此条件的顺序返回答案。）
 *
 *  
 *
 * 示例 1：
 *
 * 输入：R = 1, C = 2, r0 = 0, c0 = 0
 * 输出：[[0,0],[0,1]]
 * 解释：从 (r0, c0) 到其他单元格的距离为：[0,1]
 * 基本思路：先找出最远的距离，从小到大遍历，去掉不满足条件的选项
 */
public class Matrix_order_1030 {
    public int[][] allCellsDistOrder(int R, int C, int r0, int c0) {
        int max = Math.max(r0-0+c0-0,R-r0+C-c0);
        int max1 = Math.max(R-r0+c0,r0 + C-c0);
         max = Math.max(max,max1);

        int[][] ret= new int[R*C][2];
        int index = 0;
        for (int i = 0; i <= max; i++) {
            for (int j = 0; j <= i; j++) {
                int d1 = j;
                int d2 = i - j;
                if(d1 == 0){
                    if(d2 == 0){
                        index = addIfInRange(ret,index,R,C,r0,c0);
                    }else{
                        index = addIfInRange(ret,index,R,C,r0,c0 - d2);
                        index = addIfInRange(ret,index,R,C,r0,c0 + d2);
                    }
                }else{
                    if(d2 == 0){
                        index = addIfInRange(ret,index,R,C,r0-d1,c0);
                        index = addIfInRange(ret,index,R,C,r0+d1,c0);
                    }else{
                        index = addIfInRange(ret,index,R,C,r0-d1,c0 - d2);
                        index = addIfInRange(ret,index,R,C,r0-d1,c0 + d2);
                        index = addIfInRange(ret,index,R,C,r0+d1,c0 - d2);
                        index = addIfInRange(ret,index,R,C,r0+d1,c0 + d2);
                    }
                }
            }
        }
        return ret;
    }

    public int addIfInRange(int[][] arr,int index,int R,int C,int r,int c){
        if(r>=0 && r<R && c >=0 && c<C){
            arr[index++] = new int[]{r,c};
        }
        return index;
    }
    static void output(int[][] arr){
        for (int[] item : arr) {
            System.out.print(item[0]+" "+item[1]+" ");
        }
        System.out.println();
    }
    public static void main(String[] args){
        Matrix_order_1030 order_1030 = new Matrix_order_1030();
        output(order_1030.allCellsDistOrder(4,7,3,2));
        /*output(order_1030.allCellsDistOrder(2,2,0,1));
        output(order_1030.allCellsDistOrder(2,3,1,2));
        output(order_1030.allCellsDistOrder(3,3,0,0));
        output(order_1030.allCellsDistOrder(1,1,0,0));
        output(order_1030.allCellsDistOrder(2,2,1,1));*/
    }
}
