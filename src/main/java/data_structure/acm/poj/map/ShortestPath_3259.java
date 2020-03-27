package data_structure.acm.poj.map;

import java.util.*;

/**
 * 需要注意的是两个农田之间的路径是双向的，而虫洞是单向的
 * 最短路径 测试数据 https://contest.usaco.org/TESTDATA/DEC06_4.htm
 */
public class ShortestPath_3259 {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        int count = cin.nextInt();



        for (int i = 0; i < count; i++) {
            if (checkEdge(cin)) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }
        }
    }

    static Integer edgeCnt = 1;




    static boolean checkEdge(Scanner cin) {
        int nodeCount, posCount, nagCount;
        nodeCount = cin.nextInt();
        Edge[] edges = new Edge[6000];
        edgeCnt = 1;
        posCount = cin.nextInt();
        nagCount = cin.nextInt();
        scannerInput(cin, posCount, edges, true);
        scannerInput(cin, nagCount, edges, false);

        int[] result = new int[nodeCount];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.MAX_VALUE;
        }
        result[0] = 0;

        for (int i = 0; i < nodeCount-1; i++) {
            boolean hasUpdate = false;
            for (int j = 1; j < edgeCnt; j++) {
                Edge edge = edges[j];

                if (result[edge.src - 1] != Integer.MAX_VALUE && result[edge.target - 1] > result[edge.src - 1] + edge.weight) {//有负值边
                    result[edge.target - 1] = result[edge.src - 1] + edge.weight;
                    if(result[edge.target - 1]<=-10000){
                        return true;
                    }
                    hasUpdate = true;
                }
            }

            if (!hasUpdate) {
                break;
            }

        }
        for (int j = 1; j < edgeCnt; j++) {
            Edge edge = edges[j];

            if (result[edge.src - 1] != Integer.MAX_VALUE && result[edge.target - 1] > result[edge.src - 1] + edge.weight) {//有负值边
                return true;
            }
        }

        return false;
    }

    static void scannerInput(Scanner cin, int posCount, Edge[] edges, boolean isPos) {
        for (int i = 0; i < posCount; i++) {
            int src = cin.nextInt();
            int target = cin.nextInt();
            int weight = cin.nextInt();
            //System.out.println("read::"+src+"->"+target+"::"+weight);
            if (!isPos) {
                weight = -weight;
            }
            edges[edgeCnt++] = new Edge(src, target, weight);
            if (isPos) {
                edges[edgeCnt++] = new Edge(target, src, weight);
            }
        }
    }

    static class Edge {
        public Edge(int src, int target, int weight) {
            this.src = src;
            this.target = target;
            this.weight = weight;
        }

        public int src;
        public int target;
        public int weight;

        @Override
        public String toString() {
            return "Edge{" +
                    "src=" + src +
                    ", target=" + target +
                    ", weight=" + weight +
                    '}';
        }
    }
}
