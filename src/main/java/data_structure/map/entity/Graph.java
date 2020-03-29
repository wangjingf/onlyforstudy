package data_structure.map.entity;

import java.util.LinkedList;
import java.util.List;

public class Graph {
    List<Node> nodes = new LinkedList<>();

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void addEdge(int srcNode,int targetNode,int weight){
        nodes.get(srcNode-1).getEdges().add(new Edge(nodes.get(targetNode-1),weight));
    }
    public int size(){
        return nodes.size();
    }
    public boolean isEmpty(){
        return nodes.isEmpty();
    }
}
