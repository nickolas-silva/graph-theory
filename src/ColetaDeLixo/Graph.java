package ColetaDeLixo;

import java.util.ArrayList;

public class Graph {
  private Vertice[] vertices;
  private ArrayList<Aresta> edges; // o inteiro é o peso da aresta

  public Vertice[] getVertices() {
    return vertices;
  }
  public void setVertices(Vertice[] vertices) {
    this.vertices = vertices;
  }
  public ArrayList<Aresta> getEdges() {
    return edges;
  }
  public void setEdges(ArrayList<Aresta> edges) {
    this.edges = edges;
  }

  public Graph(Vertice[] vertices, ArrayList<Aresta> edges) {
    this.vertices = vertices;
    this.edges = edges;
  }
}
