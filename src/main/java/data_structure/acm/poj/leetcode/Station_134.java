package data_structure.acm.poj.leetcode;

/**
 * 在一条环路上有 N 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
 *
 * 你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。你从其中的一个加油站出发，开始时油箱为空。
 *
 * 如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
 *
 * 说明: 
 *
 * 如果题目有解，该答案即为唯一答案。
 * 输入数组均为非空数组，且长度相同。
 * 输入数组中的元素均为非负数。
 * 示例 1:
 *
 * 输入:
 * gas  = [1,2,3,4,5]
 * cost = [3,4,5,1,2]
 *
 * 输出: 3
 *
 * 解释:
 * 从 3 号加油站(索引为 3 处)出发，可获得 4 升汽油。此时油箱有 = 0 + 4 = 4 升汽油
 * 开往 4 号加油站，此时油箱有 4 - 1 + 5 = 8 升汽油
 * 开往 0 号加油站，此时油箱有 8 - 2 + 1 = 7 升汽油
 * 开往 1 号加油站，此时油箱有 7 - 3 + 2 = 6 升汽油
 * 开往 2 号加油站，此时油箱有 6 - 4 + 3 = 5 升汽油
 * 开往 3 号加油站，你需要消耗 5 升汽油，正好足够你返回到 3 号加油站。
 * 因此，3 可为起始索引。
 *
    基本思路：暴力计算
 */
public class Station_134 {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int[] arr = new int[gas.length];
        for (int i = 0; i < gas.length; i++) {
            arr[i] = gas[i] - cost[i];
        }
        for (int i = 0; i < arr.length; i++) {
            if(canComplete(arr,i)){
                return i;
            }
        }
        return -1;
    }
    public boolean canComplete(int[] arr,int i){
        int curr = i;
        int sum = 0;
        while (true){
            sum+=arr[curr];
            if(sum < 0 ){
                return false;
            }
            curr++;
            curr = curr % arr.length;
            if(curr == i){
                break;
            }
        }
        return true;
    }
    public static void main(String[] args){
        Station_134 station_134 = new Station_134();
        int[] gas = new int[]{1,2,3,4,5};
        int[] cost = new int[]{3,4,5,1,2};
        System.out.println(station_134.canCompleteCircuit(gas,cost));
        gas = new int[]{2,3,4};
        cost = new int[]{3,4,3};
        System.out.println(station_134.canCompleteCircuit(gas,cost));
        gas = new int[]{2};
        cost = new int[]{3};
        System.out.println(station_134.canCompleteCircuit(gas,cost));
        gas = new int[]{3};
        cost = new int[]{3};
        System.out.println(station_134.canCompleteCircuit(gas,cost));
        gas = new int[]{3};
        cost = new int[]{2};
        System.out.println(station_134.canCompleteCircuit(gas,cost));
    }
}
