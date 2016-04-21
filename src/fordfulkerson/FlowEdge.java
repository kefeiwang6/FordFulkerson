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
public class FlowEdge {
    private  int v;             // from
    private  int w;             // to 
    private  int  capacity;   // capacity
    private  int  flow;             // flow
 
    
    public FlowEdge(int v, int w, int capacity) {
        this.v         = v;
        this.w         = w;  
        this.capacity  = capacity;
        this.flow      = 0;
    }
    
     public FlowEdge(int v, int w, int capacity, int flow) {
        this.v         = v;
        this.w         = w;  
        this.capacity  = capacity;
        this.flow      = flow;
    }
     
     public int from() {
        return v;
    }  
     
    public int to() {
        return w;
    }  
    
     public int capacity() {
        return capacity;
    }
    
    public int flow() {
        return flow;
    }
    
    public void change_flow(int delta_flow){
        this.flow -= delta_flow;
    }
    
    public void change_capacity(int new_cap){
        this.capacity = new_cap;
    }
     public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }
     
    public int residualCapacityTo(int vertex) {
        if      (vertex == v) return flow;              // backward edge
        else if (vertex == w) return capacity - flow;   // forward edge
        else throw new IllegalArgumentException("Illegal endpoint");
    }
    
     public void addResidualFlowTo(int vertex, int delta) {
        if      (vertex == v) flow -= delta;           // backward edge
        else if (vertex == w) flow += delta;           // forward edge
        else throw new IllegalArgumentException("Illegal endpoint");
    }
     
     public void print_flowedge() {
        System.out.printf("%d -> %d  %d/%d", v , w , flow ,capacity);
    }


   /**
     * Unit tests the <tt>FlowEdge</tt> data type.
     */
   /* public static void main(String[] args) {
        FlowEdge e = new FlowEdge(12, 23, 3);
        e.print_flowedge();
    }*/
}
