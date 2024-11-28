/* WUGraph.java */

package graph;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {

  // This stores the vertex as the key and the Vertex object as the value
  Hashtable<Object, Vertex> vertices;
  Hashtable<String, Edge> edges;
  int edgeNumber;

  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */
  public WUGraph() {
    this.vertices = new Hashtable<Object, Vertex>();
    this.edges = new Hashtable<String, Edge>();
    this.edgeNumber = 0;
  }

  /**
   * vertexCount() returns the number of vertices in the graph.
   *
   * Running time:  O(1).
   */
  public int vertexCount() {
    int count = vertices.size();
    return count;
  }

  /**
   * edgeCount() returns the total number of edges in the graph.
   *
   * Running time:  O(1).
   */
  public int edgeCount() {
    return edgeNumber;
  }

  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */
  public Object[] getVertices() {
    // Turns the HashMap into a Set and then turns it into an Array
    return vertices.keySet().toArray();
  }

  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.
   * The vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
  public void addVertex(Object vertex) {
    if (vertices.containsKey(vertex)) {
      return;
    }
    vertices.put(vertex, new Vertex(vertex));
  }

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public void removeVertex(Object vertex) {
    if (vertices.containsKey(vertex)) {
      Vertex removedVertex= vertices.get(vertex);

      // Iterates through and removes all the edges associated with the vertex
      LinkedList<Edge> tempEdgeList = new LinkedList<Edge>();
      for (Edge edge : removedVertex.getEdges()) {
        tempEdgeList.add(edge);
      }
      for (Edge edge : tempEdgeList) {
        Object u = edge.getV1().getData();
        Object v = edge.getV2().getData();

        removeEdge(u, v);
      }

      vertices.remove(vertex);
    }
  }

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
  public boolean isVertex(Object vertex) {
    if (vertices.containsKey(vertex)) {
      return true;
    }
    return false;
  }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
  public int degree(Object vertex) {
    if (!vertices.containsKey(vertex)) {
      return 0;
    }
    Vertex v1 = vertices.get(vertex);
    return v1.getEdges().size();
  }

  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public Neighbors getNeighbors(Object vertex) {
    if (!vertices.containsKey(vertex) || vertices.get(vertex).getEdges().size() == 0) {
      System.out.println(vertex);
      return null;
    }

    Neighbors neighborsObject = new Neighbors();
    Vertex vertex1 = vertices.get(vertex);
    // Intializes lists
    neighborsObject.weightList = new int[vertex1.getEdges().size()];
    neighborsObject.neighborList = new Object[vertex1.getEdges().size()];

    int i =0;
    for (Edge edge : vertex1.getEdges()) {
      neighborsObject.weightList[i] = edge.getWeight();
      neighborsObject.neighborList[i] = edge.getV2().getData();
      i++;
    }

    return neighborsObject;
  }

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the graph already contains
   * edge (u, v), the weight is updated to reflect the new value.  Self-edges
   * (where u.equals(v)) are allowed.
   *
   * Running time:  O(1).
   */
  public void addEdge(Object u, Object v, int weight) {
    // Checks to see that the vertices are in the graph
    if (!vertices.containsKey(u) || !vertices.containsKey(v)) {
      return;
    }
    Vertex vertex1 = vertices.get(u);
    Vertex vertex2 = vertices.get(v);

    String edgeKey1 = createEdgeKey(u, v);
    String edgeKey2 = createEdgeKey(v, u);

    // Checks whether the edge already exists
    if (edges.containsKey(edgeKey1) || edges.containsKey(edgeKey2)) {
      edges.get(edgeKey1).setWeight(weight);
      edges.get(edgeKey2).setWeight(weight);
      return;
    }

    // If it is a self edge it only adds it to one vertex
    if (vertex1 == vertex2) {
      Edge selfEdge = new Edge(vertex1, vertex2, weight);
      vertex1.getEdges().add(selfEdge);
      edges.put(edgeKey1, selfEdge);
    }
    else { // Creates the edge and adds it to the list of each
      Edge newEdge1 = new Edge(vertex1, vertex2, weight);
      vertex1.getEdges().add(newEdge1);
      edges.put(edgeKey1, newEdge1);


      Edge newEdge2 = new Edge(vertex2, vertex1, weight);
      vertex2.getEdges().add(newEdge2);
      edges.put(edgeKey2, newEdge2);
    }

    edgeNumber++;
    return;
  }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
  public void removeEdge(Object u, Object v) {
    // Checks to see that the vertices are in the graph
    if (!vertices.containsKey(u) || !vertices.containsKey(v)) {
      return;
    }

    Vertex vertex1 = vertices.get(u);
    Vertex vertex2 = vertices.get(v);

    String edgeKey1 = createEdgeKey(u, v);
    String edgeKey2 = createEdgeKey(v, u);

    // If the edges exists it gets deleted from vertex1 vertex2 and edges HashTable
    if (edges.containsKey(edgeKey1) || edges.containsKey(edgeKey2)) {
      Edge edge1 = edges.get(edgeKey1);
      vertex1.removeEdge(edge1);
      edges.remove(edgeKey1);

      Edge edge2 = edges.get(edgeKey2);
      vertex2.removeEdge(edge2);
      edges.remove(edgeKey2);

      edgeNumber--;
    }
  }

  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
  public boolean isEdge(Object u, Object v) {
    // Checks to see that the vertices are in the graph
    if (!vertices.containsKey(u) || !vertices.containsKey(v)) {
      return false;
    }
    Vertex vertex1 = vertices.get(u);
    Vertex vertex2 = vertices.get(v);

    String edgeKey1 = createEdgeKey(u, v);
    String edgeKey2 = createEdgeKey(v, u);
    
    if (edges.containsKey(edgeKey1) || edges.containsKey(edgeKey2)) {
      return true;
    }
    return false;
  }

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but also more
   * annoying.)
   *
   * Running time:  O(1).
   */
  public int weight(Object u, Object v) {
    // Checks to see that the vertices are in the graph
    if (!vertices.containsKey(u) || !vertices.containsKey(v)) {
      return 0;
    }

    Vertex vertex1 = vertices.get(u);
    Vertex vertex2 = vertices.get(v);
    
    String edgeKey = createEdgeKey(u, v);

    // Checks whether the edge is in the graph
    if (edges.containsKey(edgeKey)) {
      int weight = edges.get(edgeKey).getWeight();
      return weight;
    }

    return 0;
  }

  private String createEdgeKey(Object u, Object v) {
    return u.toString() + "-" + v.toString();
  }

}

class Vertex {
  private LinkedList<Edge> edges;
  private Object data;

  public Vertex(Object data) {
    this.edges = new LinkedList<>();
    this.data = data;
  }

  public void addEdge(Edge edge) {
    edges.add(edge);
  }

  public void removeEdge(Edge edge) {
    edges.remove(edge);
  }
 
  public LinkedList<Edge> getEdges() {
    return edges;
  }

  // Getter for data
  public Object getData() {
    return data;
  }

  // Setter for data
  public void setData(Object data) {
    this.data = data;
  }
  
}

class Edge {
  private Vertex v1;
  private Vertex v2;
  private int weight;

  public Edge(Vertex v1, Vertex v2, int weight) {
    this.v1 = v1;
    this.v2 = v2;
    this.weight = weight;
  }

  // Getter for v1
  public Vertex getV1() {
    return v1;
  }

  // Setter for v1
  public void setV1(Vertex v1) {
    this.v1 = v1;
  }

  // Getter for v2
  public Vertex getV2() {
    return v2;
  }

  // Setter for v2
  public void setV2(Vertex v2) {
    this.v2 = v2;
  }

  // Getter for weight
  public int getWeight() {
    return weight;
  }

  // Setter for weight
  public void setWeight(int weight) {
    this.weight = weight;
  }
}

