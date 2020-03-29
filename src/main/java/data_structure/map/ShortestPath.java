package data_structure.map;

import data_structure.map.entity.Edge;
import data_structure.map.entity.Graph;
import data_structure.map.entity.Node;

import java.util.*;

/**
 * 带负边值的最短路径问题
 */
public class ShortestPath {

    public static Collection<Integer> getShortestPath(Graph graph, int nodeIndex){
        Node headNode = graph.getNodes().get(nodeIndex-1);
        List<Integer> result = new ArrayList<>(graph.getNodes().size());
        for (Node node : graph.getNodes()) {
            result.add(Integer.MAX_VALUE);
        }
        //用来累计迭代次数
        Map<Integer/*nodeId*/,Integer/* count */> nodePopCount = new LinkedHashMap<>();
        result.set(headNode.getNodeId()-1,0);
        Queue<Node> q = new LinkedList();
        q.add(headNode);
        while(!q.isEmpty()){
            Node e = q.poll();
            Integer count = nodePopCount.get(e.getNodeId());
            if(count == null){
                nodePopCount.put(e.getNodeId(),1);
            }else{
                if(count+1>graph.getNodes().size()){
                    System.out.println("find wrong input,break at"+e);
                    break;
                }
                nodePopCount.put(e.getNodeId(),count+1);
            }
            for (Edge edge : e.getEdges()) { //这个操作相当于把所有的边都遍历一遍
                // 当前存在的点 > 从某个点到达该点的权重
                int toIndex = edge.getTo().getNodeId()-1;
                if(result.get(toIndex) > result.get(e.getNodeId()-1) + edge.getWeight()){
                    result.set(toIndex,result.get(e.getNodeId()-1) + edge.getWeight());
                    /**
                     * 需要注意的是，即使是节点to以前被遍历过，这里仍然需要再次遍历，因为负值圈可能存在，导致
                     * 每次遍历一下逗比之前的值小
                     */
                    if(!q.contains(edge.getTo())){
                        q.add(edge.getTo());
                    }
                }
            }
        }
        return result;
    }

    public static  void main(String[] args){
        int nodeLength = 4;
        List<Node> nodes = new ArrayList<>();
        for (int i = 1; i < nodeLength+1; i++) {
            nodes.add(new Node(i));
        }
        Graph g = new Graph();
        g.setNodes(nodes);
        g.addEdge(1,2,-1);
        g.addEdge(2,4,3);
        g.addEdge(4,3,-1);
        g.addEdge(1,3,3);
        g.addEdge(1,4,-3);
        g.addEdge(4,1,-1);
        Collection<Integer> result =  getShortestPath(g,1);
        System.out.println(result);
    }
}
