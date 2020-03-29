package data_structure.map.entity;

public class Edge {
    Node to;
    int weight;

    public Edge(Node to, int weight) {
        this.to = to;
        this.weight = weight;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
