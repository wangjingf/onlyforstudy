package data_structure.map;

import data_structure.map.entity.Edge;
import data_structure.map.entity.Graph;
import data_structure.map.entity.Node;

import java.util.*;

/**
 * 计算每个点的入度，入度为0的出栈即可。
 * 出栈后将相邻节点的入度减去1
 */
public class TopoSort {
    public static Queue<Node> sort(Graph graph){
        Queue<Integer> q = new LinkedList<>();
        Queue<Node> ret = new LinkedList<>();
        if(graph.isEmpty()){
            return ret;
        }
        Map<String,Integer> rdMap = new LinkedHashMap<>();/*计算入度*/
        Integer[] rds = new Integer[graph.size()];
        for (int i = 0; i < rds.length; i++) {
            rds[i] = 0;
        }
        int i = 0;
        for (Node node : graph.getNodes()) {
            for (Edge edge : node.getEdges()) {
                rds[edge.getTo().getNodeId()-1]++;//计算入度
            }
        }
        for (int j = 0; j < rds.length; j++) {
            if(rds[j]==0){
                q.add(j+1);
            }
        }
        if(q.isEmpty()){
            throw new RuntimeException("graph is a circle");
        }
        while(!q.isEmpty()){
            Integer nodeId = q.poll();
            ret.add(graph.getNodes().get(nodeId-1));
            for (Edge edge : graph.getNodes().get(nodeId - 1).getEdges()) {
                rds[edge.getTo().getNodeId()-1]--;
                if(rds[edge.getTo().getNodeId()-1] == 0){
                    q.add(edge.getTo().getNodeId());
                }
            }
        }
        return ret;
    }
    public static  void main(String[] args){
        int nodeLength = 4;
        List<Node> nodes = new ArrayList<>();
        for (int i = 1; i < nodeLength+1; i++) {
            nodes.add(new Node(i));
        }
        Graph g = new Graph();
        g.setNodes(nodes);
        g.addEdge(1,2,1);
        g.addEdge(2,3,1);
        g.addEdge(3,4,1);
       // g.addEdge(4,1,1);
        Queue<Node> q = sort(g);
        for (Node node : q) {
            System.out.print(node.getNodeId()+" ");
        }
    }
}
