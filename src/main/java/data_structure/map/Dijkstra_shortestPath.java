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
 */
public class Dijkstra_shortestPath {
    PriorityQueue queue = new PriorityQueue();
    public static Integer[] getShortestPath(Graph graph, int nodeIndex){
        Node startNode = graph.getNodes().get(nodeIndex-1);
        Integer[] result = new Integer[graph.size()];

        return null;
    }
}
