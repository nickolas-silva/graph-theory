package ColetaDeLixo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Main {
 
  public static void main(String[] args){
    // primeiro verifica se o grafo é euleriano
    // se for, então executa o algoritmo de Hierholzer
    // se não, então acha o menor caminho que passe por todos os vértices impares usando djkistra e em seguida gera o supergrafo G*
    // duplicando as arestas do caminho. Aí em seguida, usa o algoritmo de Hierholzer.
    Vertice[] vertices = new Vertice[6];
    vertices[0] = new Vertice("S");
    vertices[1] = new Vertice("A");
    vertices[2] = new Vertice("B");
    vertices[3] = new Vertice("C");
    vertices[4] = new Vertice("D");
    vertices[5] = new Vertice("T");
    ArrayList<Aresta> arestas = new ArrayList<>();
    arestas.add(new Aresta(vertices[0],vertices[1], 18)); // S - A
    arestas.add(new Aresta(vertices[0],vertices[3], 15)); // S - C
    arestas.add(new Aresta(vertices[1],vertices[3], 2));  // A - C
    arestas.add(new Aresta(vertices[2],vertices[1], 9));  // B - A
    arestas.add(new Aresta(vertices[3],vertices[2], 14)); // C - B
    arestas.add(new Aresta(vertices[3],vertices[4], 7)); // C - D
    arestas.add(new Aresta(vertices[4],vertices[2], 10)); // D - B
    arestas.add(new Aresta(vertices[5],vertices[2], 28)); // T - B
    arestas.add(new Aresta(vertices[4],vertices[5], 36)); // D - T
    Graph graph = new Graph(vertices, arestas);
    dijkistra(graph, graph.getVertices()[0]); // usando S de origem
  }


  public static Graph dijkistra(Graph graph, Vertice root){

    // definir e inicializar a fila dos lambdas
    HashMap<Vertice, Integer> lambdas = new HashMap<Vertice,Integer>(); // guarda o vertice e qual a prioridade dele 
    // inicialmente o root tem prioridade máxima (0) e os outros prioridade minima (infinito)
    inicializeLambdas(lambdas, graph, root);
    int totalCost = 0;
    HashMap<Vertice,Vertice> predecessors = new HashMap<Vertice,Vertice>();
    while(lambdas.size() > 0){
      // agora, é necessário remover da fila de prioridade o vértice com menor prioridade
      HashMap<Vertice,Integer> removed = removeLessPriorityValue(lambdas);
      String removedName = ((Vertice)removed.keySet().toArray()[0]).getName();
      Integer removedCost = (Integer) removed.values().toArray()[0];
      Vertice removedVertice = (Vertice)removed.keySet().toArray()[0];
      totalCost += removedCost;
      // agora, é necessário descobrir os vizinhos de removed
      // Após encontrar os vizinhos, é necessário calcular e atualizar o lambda ( valor minimo entre : prioridades atual do vizinho,
      // prioridade do vertice removido + o custo da aresta entre o removido e o vizinho)

      predecessors.put(root, new Vertice("Nenhum")); // o root não tem predecessor
      for(Aresta a: graph.getEdges()){
        if(a.getV1().getName().equals(removedName) && lambdas.containsKey(a.getV2())){
          // se entrar, o vizinho é v2 e o removido foi v1
          System.out.println("Removido : " + removedName);
          System.out.println("Aresta : " + a.getV1().getName() + " -> " + a.getV2().getName());
          int lmbdaAtualizado = Math.min(lambdas.get(a.getV2()), removedCost + a.getWeight());
          if(lmbdaAtualizado < lambdas.get(a.getV2())){ // atualiza o lmbda
            lambdas.put(a.getV2(), lmbdaAtualizado);
            System.out.println("Lmbda que foi att: " + lmbdaAtualizado);
            // att predecessores
            predecessors.put(a.getV2(), removedVertice);
          }
            
        } else if(a.getV2().getName().equals(removedName) && lambdas.containsKey(a.getV1())) {
          // se entrar, o vizinho é v1 e o removido foi v2
          System.out.println("Removido : " + removedName);
          System.out.println("Aresta : " + a.getV1().getName() + " -> " + a.getV2().getName());
          int lmbdaAtualizado = Math.min(lambdas.get(a.getV1()), removedCost + a.getWeight());
          if (lmbdaAtualizado < lambdas.get(a.getV1())) {
            lambdas.put(a.getV1(), lmbdaAtualizado);
            // att predecessores
            System.out.println("Lmbda que foi att: " + lmbdaAtualizado);
            predecessors.put(a.getV1(), removedVertice);
          }
          System.out.println("");
        }
      }
    }
    // agora usando os predecessores, se constrói o caminho de custo mínimo
    Vertice[] vertices = new Vertice[predecessors.size()];
    ArrayList<Aresta> arestas = new ArrayList<>();
    int cont = 0;
    for(Vertice v: predecessors.keySet()){
      vertices[cont] = v; // adicionando os vértices do caminho
      if(predecessors.get(v) == null)
        continue;
      arestas.add(new Aresta(v, predecessors.get(v), 0));
      cont++;
    }
    for(Vertice v: predecessors.keySet()){
      System.out.println(v.getName() + " tem como predecessor : " + predecessors.get(v).getName());
    }
    Graph minimumCostPath = new Graph(vertices, arestas);

    for(Aresta a: minimumCostPath.getEdges()){
      System.out.println("Aresta: " + a.getV1().getName() +  " => " + a.getV2().getName());
    }
    for(Vertice v: minimumCostPath.getVertices()){
      System.out.println("Vertice: " + v.getName());
    }

    return null;
  }
  public static boolean hasEulerTour(Graph graph){
    return false;
  }
  public static Graph hierholzer(Graph graph){
    return null;
  }
  public static Graph duplicateEdges(Graph graph){
    return null;
  }
  public static void inicializeLambdas(HashMap<Vertice, Integer> lambdas, Graph graph, Vertice root){
    for (Vertice v : graph.getVertices()) {
        lambdas.put(v, 99999); // um valor alto para simbolizar infinito
    }    
    lambdas.put(root, 0);   
  }
  public static HashMap<Vertice,Integer> removeLessPriorityValue(HashMap<Vertice, Integer> lambdas){
    Vertice removed = (Vertice) lambdas.keySet().toArray()[0]; // pegando o primeiro
    for(Vertice v: lambdas.keySet()){
      if(lambdas.get(removed) > lambdas.get(v)){
        removed = v;
      }
    }
    HashMap<Vertice,Integer> retorno = new HashMap<>();
    retorno.put(removed, lambdas.get(removed));
    lambdas.remove(removed);
    return retorno ; // retorna o vértice que foi removido da fila e sua prioridade.
  }


}
