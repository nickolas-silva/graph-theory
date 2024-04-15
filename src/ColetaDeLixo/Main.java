package ColetaDeLixo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphicEdge;

public class Main {
 
  public static void main(String[] args){
    // primeiro verifica se o grafo é euleriano
    // se for, então executa o algoritmo de Hierholzer
    // se não, então acha o menor caminho que passe por todos os vértices impares usando djkistra e em seguida gera o supergrafo G*
    // duplicando as arestas do caminho. Aí em seguida, usa o algoritmo de Hierholzer.
    System.setProperty("org.graphstream.ui", "swing");
    System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer"); // util para o graphstream
    Vertice[] vertices = new Vertice[5];
    vertices[0] = new Vertice("V1");
    vertices[1] = new Vertice("V2");
    vertices[2] = new Vertice("V3");
    vertices[3] = new Vertice("V4");
    vertices[4] = new Vertice("V5");
    ArrayList<Aresta> arestas = new ArrayList<>();
    arestas.add(new Aresta(vertices[0],vertices[2], 4)); // V1 - V3
    arestas.add(new Aresta(vertices[0],vertices[3], 1)); // v1 - V4
    arestas.add(new Aresta(vertices[0],vertices[4], 1));  // V1 - V5
    arestas.add(new Aresta(vertices[1],vertices[0], 3));  // v1 - V2
    arestas.add(new Aresta(vertices[1],vertices[2], 1)); // V3 - V2
    arestas.add(new Aresta(vertices[2],vertices[3], 5)); //  V3 - V4
    arestas.add(new Aresta(vertices[4],vertices[1], 1)); // V5 - V2
    arestas.add(new Aresta(vertices[1],vertices[3], 4)); // V2 - V4
    Graph exemplo2 = new Graph(vertices, arestas);

    Vertice[] vertices2 = new Vertice[5];
    vertices2[0] = new Vertice("Cruzamento X");
    vertices2[1] = new Vertice("Cruzamento Y");
    vertices2[2] = new Vertice("Cruzamento Z");
    vertices2[3] = new Vertice("Cruzamento W");
    vertices2[4] = new Vertice("Cruzamento V");
    ArrayList<Aresta> arestas2 = new ArrayList<>();
    arestas2.add(new Aresta(vertices2[0],vertices2[1], 10)); // x y
    arestas2.add(new Aresta(vertices2[0],vertices2[2], 12)); // x z
    arestas2.add(new Aresta(vertices2[1],vertices2[2], 20));  // y z
    arestas2.add(new Aresta(vertices2[2],vertices2[3], 6));  // z w
    arestas2.add(new Aresta(vertices2[2],vertices2[4], 14)); // z v
    arestas2.add(new Aresta(vertices2[3],vertices2[4], 8)); //  v w
    Graph exemplo = new Graph(vertices2,arestas2);


    execute(exemplo);
    execute(exemplo2);
  }
  public static void execute(Graph graph){
    ArrayList<Vertice> verticesImpares = hasEulerTour(graph);
    if(verticesImpares.isEmpty()){ // não tem nenhum vértice impar
      System.out.println("É euleriano");
      hierholzer(graph);
    } else if(verticesImpares.size() == 2){
      System.out.println("Não é euleriano");
      //visualize(graph);
      Graph minimumCostPath = dijkistra(graph, verticesImpares); // usando A de origem
      duplicateEdges(graph, minimumCostPath);
      //visualize(graph);
      System.out.println(hasEulerTour(graph).isEmpty() ? "Euleriano" : "Nao euleriano");
      hierholzer(graph);
    } else {
      System.out.println("Algoritmo só suporta o caso trivial no qual um grafo não euleriano possui vertices de grau impar == 2");
    }
  }

  public static Graph dijkistra(Graph graph, ArrayList<Vertice> verticesImpares){

    // definir e inicializar a fila dos lambdas
    Vertice root = verticesImpares.get(0);
    HashMap<Vertice, Integer> lambdas = new HashMap<>(); // guarda o vertice e qual a prioridade dele
    // inicialmente o root tem prioridade máxima (0) e os outros prioridade minima (infinito)
    inicializeLambdas(lambdas, graph, root);
    int totalCost = 0;
    HashMap<Vertice,Vertice> predecessors = new HashMap<>();
    Vertice removedVertice = new Vertice("Random");
    while(removedVertice != verticesImpares.get(1)){
      // agora, é necessário remover da fila de prioridade o vértice com menor prioridade
      HashMap<Vertice,Integer> removed = removeLessPriorityValue(lambdas);
      String removedName = ((Vertice)removed.keySet().toArray()[0]).getName();
      Integer removedCost = (Integer) removed.values().toArray()[0];
      removedVertice = (Vertice)removed.keySet().toArray()[0];
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
  public static ArrayList<Vertice> hasEulerTour(Graph graph){
    // verificar se há algum vertice com grau impar
    ArrayList<Vertice> impares = new ArrayList<>();
    for(Vertice v: graph.getVertices()){
      int grau = 0;
      for(Aresta a: graph.getEdges()){
        if(a.getV1().getName().equals(v.getName()) || a.getV2().getName().equals(v.getName())){
          // se o vertice participar de uma aresta
          grau++;
        }
      }
      if(grau % 2 != 0)
        impares.add(v);
    }
    return impares;
  }
  public static void hierholzer(Graph graph){
    Stack<Vertice> pilha = new Stack(); // pilha usada no algoritmo
    ArrayList<Aresta> arestasDesmarcadas = new ArrayList<>(graph.getEdges()); // todas começam desmarcadas
    pilha.push(graph.getVertices()[0]); // pega um vertice aleatório
    ArrayList<Vertice> tourDeEuler = new ArrayList<>();
    Vertice inicio = pilha.peek();
    while(!pilha.isEmpty()){
      Vertice u = pilha.peek();
      boolean temArestaDesmarcada = false;
      for(Aresta a: graph.getEdges()){
        if(a.getV1().getName().equals(u.getName()) && arestasDesmarcadas.contains(a)){
          // o vizinho é v2
          pilha.push(a.getV2());
          arestasDesmarcadas.remove(a);
          temArestaDesmarcada = true;
          break;
        }
        if(a.getV2().getName().equals(u.getName()) && arestasDesmarcadas.contains(a)){
          // o vizinho é v1
          pilha.push(a.getV1());
          arestasDesmarcadas.remove(a);
          temArestaDesmarcada = true;
          break;
        }
        // se não possui aresta incidente desmarcada então remove u do topo da pilha e imprime u
      }
      if(!temArestaDesmarcada)
        tourDeEuler.add(pilha.pop());
    }
    visualizeTour(graph,tourDeEuler);
  }
  public static void duplicateEdges(Graph graph, Graph minimumCostPath){

    ArrayList<Aresta> arestas = new ArrayList<>();
    for(Aresta a: minimumCostPath.getEdges()){
      for(Aresta b: graph.getEdges()){
        if(a.getV1().getName().equals(b.getV1().getName()) && a.getV2().getName().equals(b.getV2().getName()) ){
          // verifica se é a mesma aresta para em seguida duplicar
          arestas.add(new Aresta(b.getV1(), b.getV2(), b.getWeight()));
        }
        if(a.getV2().getName().equals(b.getV1().getName()) && a.getV1().getName().equals(b.getV2().getName()) ){
          // verifica se é a mesma aresta para em seguida duplicar
          arestas.add(new Aresta(b.getV1(), b.getV2(), b.getWeight()));
        }
      }
    }
    for(Aresta a: arestas){
      graph.getEdges().add(a); // adicionar as arestas extras formando o supergrafo g*
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

  public static void visualize(Graph graph){
    org.graphstream.graph.Graph graph2 = new MultiGraph("Coleta de lixo");

    for(Vertice v:graph.getVertices()){
      Node added = graph2.addNode(v.getName());
      added.setAttribute("ui.label", v.getName());
      added.setAttribute("ui.style", "shape:circle;fill-color: yellow;size: 50px; text-alignment: center;");
    }
    int cont = 0;
    for(Aresta a: graph.getEdges()){
      String id = Integer.toString(cont);
      Edge added = graph2.addEdge(id,a.getV1().getName(),a.getV2().getName());
      String label = a.getV1().getName() + a.getV2().getName();
      added.setAttribute("ui.label", label);
      cont++;
    }
    graph2.display();
  }
  public static void visualizeTour(Graph graph, ArrayList<Vertice> tour){
    org.graphstream.graph.Graph graph2 = new MultiGraph("Tour de euler");

    for(Vertice v: tour){
      System.out.print(v.getName() + " --> ");
    }
    System.out.println();
    int cont  = 0;
    for(Vertice v:graph.getVertices()){
      Node added = graph2.addNode(v.getName());
      added.setAttribute("ui.label", v.getName());
      added.setAttribute("ui.style", "shape:circle;fill-color: yellow;size: 50px; text-alignment: center;");
    }
    cont = 0;
    // formar as arestas de acorod com a lista
    for(int i = 0; i +1 < tour.size(); i++){
      Vertice v1 = tour.get(i);
      Vertice v2 = tour.get(i+1);
      boolean existe = false;
      for(int j = 0; j < graph2.getEdgeCount(); j++){
        Node zero = graph2.getEdge(j).getNode0();
        Node um = graph2.getEdge(j).getNode1();

        if( (zero.getId().equals(v1.getName()) && um.getId().equals(v2.getName()))
                || (zero.getId().equals(v2.getName()) && um.getId().equals(v1.getName())) ){
          existe = true;
        }

      }
      Edge e =graph2.addEdge(Integer.toString(i), v1.getName(), v2.getName());
      e.setAttribute("ui.label", i);
      if(existe){
        e.setAttribute("ui.style", "text-alignment:under;text-size:15;");
      }else {
        e.setAttribute("ui.style", "text-alignment:above;text-size:15;");
      }
    }
    graph2.display();
  }
}
