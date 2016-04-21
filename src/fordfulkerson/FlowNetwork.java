/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fordfulkerson;

/**
 *
 * @author kefei
 */
public class FlowNetwork {
    private int V;
    private int E;
    private Bag<FlowEdge>[] adj;
    
     public FlowNetwork(int V, int E, int[][] edge) {
        this.V = V;
        this.E = E;
        adj = (Bag<FlowEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<FlowEdge>();
        
        for (int i = 0; i < E; i++) {
                int v = edge[i][0];
                int w = edge[i][1];
                int capacity = edge[i][2];         
            addEdge(new FlowEdge(v, w, capacity));
        }     
    }
     
    public int V() {
        return V;
    }
    public int E() {
        return E;
    }
    public void addEdge(FlowEdge e) {
        int v = e.from();
        int w = e.to();
        adj[v].add(e);
        adj[w].add(e);
    }
    public Iterable<FlowEdge> adj(int v) {
        
        return adj[v];
    }
    
    
}
