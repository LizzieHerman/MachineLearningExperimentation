package machinelearningexperimentation;

/**
 *
 * @author Lizzie Herman
 */

import java.util.ArrayList;

public class Graph {
    ArrayList<Node> nodes;
    ArrayList<Edge> edges;
    boolean directed;
    
    public Graph(){
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
        directed = false;
    }
    
    public boolean addWeight(double w, String a, String b){
        for(int i = 0; i < edges.size(); i++){
            if((edges.get(i)).isEdge(a, b)){
                (edges.get(i)).setWeight(w);
                return true;
            }
        }
        return false;
    }
    
    public void createFullGraph(String[] names){
        for(String name : names){
            nodes.add(new Node(name));
        }
        for(Node n : nodes){
            for(Node m : nodes){
                if(n.equals(m)) continue;
                if(n.hasConnection(m)) continue;
                n.addConnection(m);
                Edge cur = new Edge(n,m);
                if(hasEdge(cur)) continue;
                edges.add(cur);
            }
        }
    }
    
    public boolean hasEdge(Edge other){
        for(Edge e : edges){
            if(e.equals(other)) return true;
        }
        return false;
    }
    
    public void findMaxSpanTree(){
        
    }
    
    class Node {
        ArrayList<Node> connections;
        String name;
        
        public Node(String n){
            connections = new ArrayList<Node>();
            name = n;
        }
        
        public boolean isNode(String n){
            return name.equalsIgnoreCase(n);
        }
        
        public void addConnection(Node c){
            connections.add(c);
        }
        
        public boolean hasConnection(Node other){
            for(Node n : connections){
                if(n.equals(other)) return true;
            }
            return false;
        }
        
        public boolean equals(Node other){
            return name.equalsIgnoreCase(other.name);
        }
    }
    class Edge {
        Node one;
        Node two;
        double weight;
        
        public Edge(Node o, Node t){
            one = o;
            two = t;
        }
        
        public void setWeight(double w){
            weight = w;
        }
        
        public boolean equals(Edge other){
            return (one.equals(other.one) && two.equals(other.two)) || (one.equals(other.two) && two.equals(other.one));
        }
        
        public boolean isEdge(String a, String b){
            return (one.isNode(a) && two.isNode(b)) || (one.isNode(b) && two.isNode(a));
        }
    }
    class DirectedEdge {
        Node parent;
        Node child;
        double weight;
        
        public DirectedEdge(Node p, Node c){
            parent = p;
            child = c;
        }
        
        public void setWeight(double w){
            weight = w;
        }
    }
}
