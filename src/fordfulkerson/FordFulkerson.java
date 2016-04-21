/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fordfulkerson;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 *
 * @author kefei
 */
public class FordFulkerson {
    
    /**
     * @param args the command line arguments
     */
    private boolean[] marked;     // marked[v] = true iff s->v path in residual graph
    private FlowEdge[] edgeTo;    // edgeTo[v] = last edge on shortest residual s->v path
    private int value;         // current value of max flow
    private int delta_flow =0;
    private FlowEdge[] eTo;
    
    private boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
        edgeTo = new FlowEdge[G.V()];
        marked = new boolean[G.V()];

        // breadth-first search
        Queue<Integer> queue = new Queue<Integer>();
        queue.enqueue(s);
        marked[s] = true;
        while (!queue.isEmpty() && !marked[t]) {
            int v = queue.dequeue();

            for (FlowEdge e : G.adj(v)) {
                int w = e.other(v);

                // if residual capacity from v to w
                if (e.residualCapacityTo(w) > 0) {
                    if (!marked[w]) {
                        edgeTo[w] = e;
                        marked[w] = true;
                        queue.enqueue(w);
                    }
                }
            }
        }
         
        // is there an augmenting path?
        return marked[t];
    }
    
    public void find_min_cut(FlowNetwork G){
        Queue<FlowEdge> mincut = new Queue<FlowEdge>();
        for(int v=0; v< G.V(); v++){
            for (FlowEdge e : G.adj(v)) {
                int w = e.to();
                if (e.residualCapacityTo(w) == 0 && marked[v]) {
                    mincut.enqueue(e);
                }
            }
                   
        }
        while(!mincut.isEmpty()){
            FlowEdge e = mincut.dequeue();
            System.out.printf("%d -> %d\n", e.from(),e.to());
        }
        
    }
    
    public FordFulkerson(FlowNetwork G, int s, int t) {
    
        if (s == t)               throw new IllegalArgumentException("Source equals sink");
      

        // while there exists an augmenting path, use it
       
        while (hasAugmentingPath(G, s, t)) {

            // compute bottleneck capacity
            int bottle = Integer.MAX_VALUE;
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                bottle =  Math.min(bottle, edgeTo[v].residualCapacityTo(v));
                
            }

            // augment flow
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                edgeTo[v].addResidualFlowTo(v, bottle); 
            }

            value += bottle;
        }
        if(hasAugmentingPath(G, s, t)!= true){
            if(value == 0)  {
                for (FlowEdge e : G.adj(s)) {
                     value += e.flow();
                }
            }
        }
        // check optimality conditions
        
    }
    
    public int value()  {
        return value;
    }
    public boolean update_needed(FlowNetwork G, int v, int w, int capacity){
        for (FlowEdge e : G.adj(v)) {
            if ( e.to() == w){
                if(e.capacity()==e.flow()) {
                    
                    delta_flow = e.flow()-capacity;
                    e.change_flow(delta_flow);
                    e.change_capacity(capacity);
                    return true;
                    
                }
    
                if(e.flow()> capacity){
                    delta_flow = e.flow() - capacity;
                    e.change_flow(delta_flow);
                    e.change_capacity(capacity);
                    return true;
                }
            }
        }
        return false;
    }
    
    public void update_max_flow(FlowNetwork G, int v, int w,int s, int t, int capacity){
        FlowEdge e;
        boolean[] mk = new boolean[G.V()];
        boolean found = false;
        eTo = new FlowEdge[G.V()];
        int node;
        mk[s]= true;
        
        Queue<Integer> queue = new Queue<Integer>();
        queue.enqueue(s);
        while (!queue.isEmpty() && found != true){
            int  from = queue.dequeue();
            for (FlowEdge eg : G.adj(from)){
                int to= eg.to();
                if(mk[to]!= true && eg.flow()> delta_flow){
                    queue.enqueue(to);
                    eTo[to] = eg;
                    mk[to] = true;
                    if(to == v){
                        found = true;
                        break;
                    }
                }
            }
            
        }
        found = false;
        Queue<Integer> q = new Queue<Integer>();
        q.enqueue(w);
        while (!q.isEmpty() && found != true){
            int  from = q.dequeue();
            for (FlowEdge eg : G.adj(from)){
                int to= eg.to();
                if(mk[to]!= true && eg.flow()> delta_flow){
                    q.enqueue(to);
                    eTo[to] = eg;
                    mk[to] = true;
                    if(to == t){
                        found = true;
                        break;
                    }
                }
            }
            
        }
        node = v;
        while(node!= s){
            e = eTo[node];
            e.change_flow(delta_flow);
            node = e.from();
        }
        node = t;
        while(node != w){
            e = eTo[node];
            e.change_flow(delta_flow);
            node = e.from();
        }
        FordFulkerson new_maxflow = new FordFulkerson(G,s,t);
        System.out.printf("updated max flow value: %d ",new_maxflow.value());
        
        
    }
    public static void main(String[] args) {
        // TODO code application logic here
         int V;
        int E;
        int[][] edgetb;
        int s, t;
        int v, w, capacity;
        String path = args[0];
        String[] edge;
        
        try{
            File input = new File(path);
            Scanner sc = new Scanner(input);
            int i=0, j =0;
            V = Integer.parseInt(sc.nextLine());
            E = Integer.parseInt(sc.nextLine());
            edgetb = new int[E][3];
            while(sc.hasNextLine()){
                edge = sc.nextLine().split(" ");
                for(j=0;j<3;j++){
                    edgetb[i][j]= Integer.parseInt(edge[j]);
                }
                i++;
            }
            
            FlowNetwork G = new FlowNetwork(V,E,edgetb);
            s = 0;
            t = 6;
            FordFulkerson maxflow = new FordFulkerson(G,s,t);
            System.out.print(maxflow.value());
            System.out.print("\n");
            maxflow.find_min_cut(G);
            if(args.length > 1){
                v= Integer.parseInt(args[1]);
                w= Integer.parseInt(args[2]);
                capacity= Integer.parseInt(args[3]);
                if(maxflow.update_needed(G, v, w, capacity)){
                    maxflow.update_max_flow(G, v, w, s, t, capacity);
                }
                else
                    System.out.print("no need to update");
            }
            
        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
    }
    
}
