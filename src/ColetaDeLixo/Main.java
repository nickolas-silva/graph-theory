package ColetaDeLixo;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
 
  public static void main(String[] args){
    // primeiro verifica se o grafo é euleriano
    // se for, então executa o algoritmo de Hierholzer
    // se não, então acha o menor caminho que passe por todos os vértices impares usando djkistra e em seguida gera o supergrafo G*
    // duplicando as arestas do caminho. Aí em seguida, usa o algoritmo de Hierholzer.
    Vertice[] vertices = new Vertice[6];
    vertices[0] = new Vertice("A");
    vertices[1] = new Vertice("B");
    vertices[2] = new Vertice("C");
    vertices[3] = new Vertice("D");
    vertices[4] = new Vertice("E");
    vertices[5] = new Vertice("F");
    ArrayList<Aresta> arestas = new ArrayList<>();
    arestas.add(new Aresta(vertices[0],vertices[1], 5)); // A - B
    arestas.add(new Aresta(vertices[0],vertices[3], 2)); // A - C
    arestas.add(new Aresta(vertices[1],vertices[3], 7));  // C - B
    arestas.add(new Aresta(vertices[2],vertices[1], 8));  // C - E
    arestas.add(new Aresta(vertices[3],vertices[2], 8)); // B - D
    arestas.add(new Aresta(vertices[3],vertices[4], 3)); //  E - F
    arestas.add(new Aresta(vertices[4],vertices[2], 6)); // E - D
    arestas.add(new Aresta(vertices[5],vertices[2], 4)); // C - D
    arestas.add(new Aresta(vertices[4],vertices[5], 4)); // F - D
    Graph graph = new Graph(vertices, arestas);
    if(hasEulerTour(graph)){
      System.out.println("É euleriano");
    } else {
      System.out.println("Não é euleriano");
      Graph minimumCostPath = dijkistra(graph, graph.getVertices()[0]); // usando A de origem
      duplicateEdges(graph, minimumCostPath);
      if(hasEulerTour(graph))
          System.out.println("Funfa");
      else
          System.out.println("N funfa");
      hierholzer(graph);
    }
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
          int lmbdaAtualizado = Math.min(lambdas.get(a.getV2()), removedCost + a.getWeight());
          if(lmbdaAtualizado < lambdas.get(a.getV2())){ // atualiza o lmbda
            lambdas.put(a.getV2(), lmbdaAtualizado);
            // att predecessores
            predecessors.put(a.getV2(), removedVertice);
          }
            
        } else if(a.getV2().getName().equals(removedName) && lambdas.containsKey(a.getV1())) {
          // se entrar, o vizinho é v1 e o removido foi v2
          
          int lmbdaAtualizado = Math.min(lambdas.get(a.getV1()), removedCost + a.getWeight());
          if (lmbdaAtualizado < lambdas.get(a.getV1())) {
            lambdas.put(a.getV1(), lmbdaAtualizado);
            // att predecessores
            predecessors.put(a.getV1(), removedVertice);
          }
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
    Graph minimumCostPath = new Graph(vertices, arestas);

    return minimumCostPath;
  }
  public static boolean hasEulerTour(Graph graph){
    // verificar se há algum vertice com grau impar 
    int impares = 0;
    for(Vertice v: graph.getVertices()){
      int grau = 0;
      for(Aresta a: graph.getEdges()){
        if(a.getV1().getName().equals(v.getName()) || a.getV2().getName().equals(v.getName())){
          // se o vertice participar de uma aresta
          grau++;
        }
      }
      if(grau % 2 != 0)
        impares++;
    }
    if(impares == 2)
      return false;
    if(impares == 0)
      return true;

    return false; // se tiver mais do que 2 impares, o algoritmo já não funfa
  }
  public static Graph hierholzer(Graph graph){
    return null;
  }
  public static void duplicateEdges(Graph graph, Graph minimumCostPath){
    for(Aresta a: graph.getEdges()){
      for(Aresta b: minimumCostPath.getEdges()){
        if(a.getV1().getName().equals(b.getV1().getName()) && a.getV2().getName().equals(b.getV2().getName()) ){
          // verifica se é a mesma aresta para em seguida duplicar
          graph.getEdges().add(new Aresta(a.getV1(), a.getV2(), a.getWeight()));
        }
      }
    }
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
