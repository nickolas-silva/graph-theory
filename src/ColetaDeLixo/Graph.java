package ColetaDeLixo;

import java.util.HashMap;

public class Graph {
  private Vertice[] vertices;
  private HashMap<Vertice,Integer> edges; // o inteiro é o peso da aresta

  public Vertice[] getVertices() {
    return vertices;
  }
  public void setVertices(Vertice[] vertices) {
    this.vertices = vertices;
  }
  public HashMap<Vertice, Integer> getEdges() {
    return edges;
  }
  public void setEdges(HashMap<Vertice, Integer> edges) {
    this.edges = edges;
  }

  public Graph(Vertice[] vertices, HashMap<Vertice, Integer> edges) {
    this.vertices = vertices;
    this.edges = edges;
  }

  
}
