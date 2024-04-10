package ColetaDeLixo;

import java.util.ArrayList;

public class teste {
    public static void main(String[] args){
        Vertice[] vertices = new Vertice[6];
        vertices[0] = new Vertice("0");
        vertices[1] = new Vertice("1");
        vertices[2] = new Vertice("2");
        vertices[3] = new Vertice("3");
        vertices[4] = new Vertice("4");
        vertices[5] = new Vertice("5");
        ArrayList<Aresta> arestas = new ArrayList<>();
        arestas.add(new Aresta(vertices[0], vertices[1], 0));
        arestas.add(new Aresta(vertices[0], vertices[3], 0));
        arestas.add(new Aresta(vertices[5], vertices[1], 0));
        arestas.add(new Aresta(vertices[4], vertices[1], 0));
        arestas.add(new Aresta(vertices[2], vertices[1], 0));
        arestas.add(new Aresta(vertices[3], vertices[2], 0));
        arestas.add(new Aresta(vertices[2], vertices[4], 0));
        arestas.add(new Aresta(vertices[2], vertices[5], 0));
        Graph grafo = new Graph(vertices, arestas);
        Main.hierholzer(grafo);
    }
}
