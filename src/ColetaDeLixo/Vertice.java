package ColetaDeLixo;
import java.util.HashMap;

public class Vertice{
    private String nome; // label
    private HashMap<Vertice,Integer> arestas; // o inteiro é o peso da aresta
    
    public Vertice(String nome, HashMap<Vertice,Integer> arestas){
      this.nome = nome;
      this.arestas = arestas;
    }

    public String getNome() {
      return nome;
    }

    public void setNome(String nome) {
      this.nome = nome;
    }

    public HashMap<Vertice,Integer> getArestas() {
      return arestas;
    }

    public void setArestas(HashMap<Vertice,Integer> arestas) {
      this.arestas = arestas;
    }
    
}