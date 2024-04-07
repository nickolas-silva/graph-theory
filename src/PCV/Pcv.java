package PCV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pcv {
    private int [][] graph;
    private int numNodes;
    private List<Integer> bestRoute;
    private int minDistance = Integer.MAX_VALUE;

    public Pcv(int [][] graph) {
        this.graph = graph;
        this.numNodes = graph.length;
    }

    public void solve() {
        List<Integer> route = new ArrayList<>();
        for(int i = 0; i < numNodes; i++){
            route.add(i);
        }
        permute(route, 0);

    }

    private void permute(List<Integer> route, int start) {
        if(start == route.size() - 1){
            int distance = calcDistance(route);
            if(distance < minDistance){
                minDistance = distance;
                bestRoute = new ArrayList<>(route);
            }
            return;
        }

        for(int i = start; i < route.size(); i++){
            Collections.swap(route, i, start);
            permute(route, start+1);
            Collections.swap(route, i, start);
        }
    }

    private int calcDistance(List<Integer> route) {
        int distance = 0;
        int prev = 0;
        for( int node : route){
            distance += graph[prev][node];
            prev = node;
        }
        distance += graph[prev][0];
        return distance;
    }

    public List<Integer> getBestRoute() {
        return bestRoute;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public static void main(String[] args) {
        int[][] graph = {
            { 0, 10, 15, 20 },
            { 10, 0, 35, 25 },
            { 15, 35, 0, 30 },
            { 20, 25, 30, 0 }
        };

        Pcv pcv = new Pcv(graph);
        pcv.solve();
        List<Integer> bestRoute = pcv.getBestRoute();
        int minDistance = pcv.getMinDistance();

        System.out.println("Melhor rota: " + bestRoute);
        System.out.println("Distância mínima: " + minDistance);
    }
}
