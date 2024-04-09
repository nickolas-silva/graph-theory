package ColetaDeLixo;

import java.util.HashMap;

;

public class Vertice{
    private String name; // label
    private HashMap<Vertice,Integer> edges; // o inteiro é o peso da aresta

    public Vertice(String name){
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public HashMap<Vertice, Integer> getEdges() {
      return edges;
    }

    public void setEdges(HashMap<Vertice, Integer> edges) {
      this.edges = edges;
    }
    
}