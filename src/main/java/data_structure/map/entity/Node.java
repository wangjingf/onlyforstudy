package data_structure.map.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Node {
    Integer nodeId;
    List<Edge> edges = new LinkedList<>();

    public Node(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(nodeId, node.nodeId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nodeId);
    }

    @Override
    public String toString() {
        return "Node{" +
                "nodeId=" + nodeId +
                '}';
    }
}
