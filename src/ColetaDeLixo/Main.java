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
  }


  public static Vertice[] dijkistra(Graph graph){
    // definir a raiz
    Vertice root = graph.getVertices()[0]; // tá um aleatório por enquanto
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
      // Após encontrar os vizinhos, é necessário calcular e atualizar o lambda ( valor minimo entre : custo atual do vizinho,
      // custo do vertice removido + o custo da aresta entre o removido e o vizinho)
      for(Vertice v: graph.getVertices()){
        for(Vertice x:v.getEdges().keySet()){
          if(x.getName().equals(removedName)){
            if(removedName.equals(root.getName())){
              predecessors.put(removedVertice, x); // add vizinho na predecessor stack
              lambdas.put(x, Math.min(lambdas.get(x), (lambdas.get(removedVertice) + v.getEdges().get(x)))       ); // lambda de x = min(lambda(x) atual, lambda(removed) + custo da aresta entre removed e x ) 
            }
            //neighbors.add(v);
          }
      }



      }


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
    retorno.put(removed, priorityQueue.get(removed));
    priorityQueue.remove(removed);
    return retorno ; // retorna o vértice que foi removido da fila e sua prioridade.
  }


}
