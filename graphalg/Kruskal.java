/* Kruskal.java */

package graphalg;

import graph.*;
import set.*;
import java.util.*;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {

  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g.  The original WUGraph g is NOT changed.
   *
   * @param g The weighted, undirected graph whose MST we want to compute.
   * @return A newly constructed WUGraph representing the MST of g.
   */
  public static WUGraph minSpanTree(WUGraph g) {
    final WUGraph mst = new WUGraph();
    final Object[] vertices = g.getVertices();

    // Adds all vertices from G.
    for (Object vertex : vertices) {
        mst.addVertex(vertex);
    }

    // Builds a list of all edges in G.
    final List<Edge> edges = new ArrayList<>();
    for (Object vertex : vertices) {
        Neighbors neighbors = g.getNeighbors(vertex);
        for (int i = 0; i < neighbors.neighborList.length; i++) {
            Object neighbor = neighbors.neighborList[i];
            int weight = neighbors.weightList[i];

            // Avoids duplicate edges.
            if (vertex.toString().compareTo(neighbor.toString()) < 0) {
                edges.add(new Edge(vertex, neighbor, weight));
            }
        }
    }

    // Sorts the edges by weight in O(|E| log |E|) time.
    List<Edge> sortedEdges = mergeSort(edges);

    // Uses disjoint sets to find the MST.
    final DisjointSets disjointSets = new DisjointSets(vertices.length);
    final HashMap<Object, Integer> vertexToIndex = new HashMap<>();
    for (int i = 0; i < vertices.length; i++) {
        vertexToIndex.put(vertices[i], i);
    }

    for (Edge edge : sortedEdges) {
        int root1 = disjointSets.find(vertexToIndex.get(edge.vertex1));
        int root2 = disjointSets.find(vertexToIndex.get(edge.vertex2));

        if (root1 != root2) { // no cycle is created.
            mst.addEdge(edge.vertex1, edge.vertex2, edge.weight);
            disjointSets.union(root1, root2);
        }
    }

    return mst;
}

/**
 * Implements merge sort to sort edges by weight.
 *
 * @param edges The list of edges to be sorted.
 * @return A sorted list of edges.
 */
private static List<Edge> mergeSort(List<Edge> edges) {
    if (edges.size() <= 1) {
        return edges;
    }
    int mid = edges.size() / 2;
    List<Edge> left = mergeSort(edges.subList(0, mid));
    List<Edge> right = mergeSort(edges.subList(mid, edges.size()));

    return merge(left, right);
}

/**
 * Merges two sorted lists of edges into one sorted list.
 *
 * @param left  The left sorted list.
 * @param right The right sorted list.
 * @return A merged sorted list.
 */
private static List<Edge> merge(List<Edge> left, List<Edge> right) {
    List<Edge> result = new ArrayList<>();
    int i = 0, j = 0;

    while (i < left.size() && j < right.size()) {
        if (left.get(i).weight <= right.get(j).weight) {
            result.add(left.get(i++));
        } else {
            result.add(right.get(j++));
        }
    }

    while (i < left.size()) {
        result.add(left.get(i++));
    }

    while (j < right.size()) {
        result.add(right.get(j++));
    }

    return result;
}
}

/**
* Represents an edge in a graph with two endpoints and weight.
*/
class Edge {
Object vertex1;
Object vertex2;
int weight;

/**
 * Constructs an edge between two vertices wiht given weight.
 *
 * @param vertex1 The first vertex.
 * @param vertex2 The second vertex.
 * @param weight  The weight of the edge.
 */
public Edge(Object vertex1, Object vertex2, int weight) {
    this.vertex1 = vertex1;
    this.vertex2 = vertex2;
    this.weight = weight;
}
}