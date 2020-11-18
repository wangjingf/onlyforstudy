package data_structure.acm.poj.leetcode;

/**
 * 假设有打乱顺序的一群人站成一个队列。 每个人由一个整数对(h, k)表示，其中h是这个人的身高，k是排在这个人前面且身高大于或等于h的人数。 编写一个算法来重建这个队列。
 *
 * 注意：
 * 总人数少于1100人。
 *
 * 示例
 *
 * 输入:
 * [[7,0], [4,4], [7,1], [5,0], [6,1], [5,2]]
 *
 * 输出:
 * [[5,0], [7,0], [5,2], [6,1], [4,4], [7,1]]
 *
 * 基本思路：先找出k=0，且h最小的元素h1，排到第一个。
 * 然后更新剩余元素的k值，若h<=h1,则更新k值为k-1
 * 暴力法复杂度为n*n
 * 还有别的方式吗？暂时想不到啦
 */
public class Height_topo_406 {
    public int[][] reconstructQueue(int[][] people) {
        if(people == null || people.length <=1 ){
            return people;
        }
        int[][] copyPeople = new int[people.length][2];
        for (int i = 0; i < people.length; i++) {
            copyPeople[i][0] = people[i][0];
            copyPeople[i][1] = people[i][1];
        }
        int[][] ret = new int[people.length][2];
        for (int i = 0; i < people.length; i++) {
            int index  = findMinAndRemove(people);
            ret[i] = copyPeople[index];
            updateK(people,ret[i][0]);
        }
        return ret;
    }
    public void updateK(int[][] people,int h){
        for (int i = 0; i < people.length; i++) {
            int[] elm = people[i];
            if(elm != null){
                if(h>=elm[0]){
                    people[i][1] = elm[1]-1;
                }
            }
        }
    }
    public int findMinAndRemove(int[][] people){
        int[] min = null;
        int minIndex = -1;
        for (int i = 0; i < people.length; i++) {
            int[] elm = people[i];
            if(elm == null){
                continue;
            }
            if(elm[1] == 0){
                if(min == null){
                    min = elm;
                    minIndex = i;
                }else if(elm[0] < min[0]){
                    min = elm;
                    minIndex = i;
                }
            }
        }
        people[minIndex]=null;
        return minIndex;
    }

    static int[][] buildArr(int[] arr){
        int[][] ret = new int[arr.length/2][2];
        for (int i = 0; i < arr.length; i+=2) {
            ret[i/2][0] = arr[i];
            ret[i/2][1] = arr[i+1];
        }
        return ret;
    }
    static void output(int[][] arr){
        for (int[] item : arr) {
            System.out.print(item[0]+" "+item[1]+" ");
        }
        System.out.println();
    }
    public static void main(String[] args){
        Height_topo_406 topo = new Height_topo_406();
        int[] arr = new int[]{7,0,4,4,7,1,5,0,6,1,5,2};
        int[][] newArr = buildArr(arr);
        int[][] ret = topo.reconstructQueue(newArr);
        output(ret);
    }
}
