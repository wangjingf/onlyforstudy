package data_structure.map;

import data_structure.PriorityQueue;
import data_structure.map.entity.Edge;
import data_structure.map.entity.Graph;
import data_structure.map.entity.Node;

import java.util.*;

/**
 对于所有边长度相同的情况，比如地图的模型，bfs第一次遇到目标点，
 此时就一定是从根节点到目标节点最短的路径（因为每一次所有点都是向外扩张一步，你先遇到，那你就一定最短）。bfs先找到的一定是最短的。
 但是如果是加权边的话这样就会出问题了，bfs传回的是经过边数最少的解，但是因为加权了，这个解到根节点的距离不一定最短。比如1000+1000是只有两段，
 1+1+1+1有4段，由于bfs返回的经过边数最少的解，这里会返回总长度2000的那个解，显然不是距离最短的路径。此时我们就应该采用Dijkstra最短路算法解决加权路径的最短路了。
(a,b,1000)
 (a,c,1000)
(a,d,1)
 (d,e,1);
 (e,c,1);
 示例：https://www.cnblogs.com/biyeymyhjob/archive/2012/07/31/2615833.html
 */
public class Dijkstra_shortestPath {
    /**
     * 构造优先队列时对node权值进行排序。
     */
    static class WeightNode extends Node implements Comparable{
        int weight = Integer.MAX_VALUE;

        public WeightNode(Integer nodeId) {
            super(nodeId);
        }

        public WeightNode(Integer nodeId, int weight) {
            super(nodeId);
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }



        @Override
        public int compareTo(Object o) {
            return getWeight()-((WeightNode)o).getWeight();
        }
    }

    public static Integer[] shortestPath(Graph graph, int nodeIndex){
        WeightNode startNode = (WeightNode) graph.getNodes().get(nodeIndex-1);
        PriorityQueue<WeightNode> queue = new PriorityQueue();
        startNode.setWeight(0);
        queue.add(startNode);
        boolean[] known = new boolean[graph.size()];
        for (int i = 0; i < known.length; i++) {
            known[i] = false;
        }
        Integer[] result = new Integer[graph.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.MAX_VALUE;
        }
        result[nodeIndex-1]=0;
        while (!queue.isEmpty()){
            Node shortestNode =  queue.pop();
            if(known[shortestNode.getNodeId()-1]){//node在之前就已经被添加到队列里了
                continue;
            }
            known[shortestNode.getNodeId()-1] = true;
            //result[shortestNode.getNodeId()-1] = ((WeightNode) shortestNode).getWeight();
            for (Edge edge : shortestNode.getEdges()) {
                WeightNode toNode =  (WeightNode) edge.getTo();
                if(!known[toNode.getNodeId()-1]){
                    int srcWeight = result[shortestNode.getNodeId()-1];
                    if(result[toNode.getNodeId()-1] > srcWeight + edge.getWeight()){//在这里queue节点有可能被重复添加,由于后添加的节点总是比先添加的节点靠前，因此保证新添加节点权值的有效性
                        //toNode.setWeight(srcWeight+edge.getWeight());
                        result[toNode.getNodeId()-1] = srcWeight + edge.getWeight();
                        WeightNode newNode = new WeightNode(toNode.getNodeId(), srcWeight+edge.getWeight());
                        newNode.setEdges(toNode.getEdges());
                        queue.add(newNode);
                    }

                }
            }

        }
        return result;
    }
    public static void main(String[] args){
        int nodeCount = 6;
        List<WeightNode> nodes = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            nodes.add(new WeightNode(i));
        }
        Graph g = new Graph();
        g.getNodes().addAll(nodes);
        g.addEdge(1,2,7);
        g.addEdge(1,3,9);
        g.addEdge(1,6,14);
        g.addEdge(2,3,10);
        g.addEdge(2,4,15);
        g.addEdge(3,6,2);
        g.addEdge(3,4,11);
        g.addEdge(4,5,6);
        g.addEdge(5,6,9);
        Integer[] weights = shortestPath(g,1);
        System.out.println(Arrays.toString(weights));

    }
}
