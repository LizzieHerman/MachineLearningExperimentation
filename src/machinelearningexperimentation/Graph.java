package machinelearningexperimentation;

/**
 *
 * @author Lizzie Herman
 */

import java.util.ArrayList;

public class Graph {
    Node[] nodes;
    ArrayList edges;
    
    public Graph(int n){
        nodes = new Node[n];
        edges = new ArrayList();
    }
    
    public void createFullGraph(String[] names){
        int i = 0;
        for(String name : names){
            nodes[i] = new Node(name);
            i++;
        }
        for(Node n : nodes){
            for(int j = 0; j < nodes.length; j++){
                if(n.equals(nodes[j])) continue;
                nodes[j].addConnection(n);
            }
        }
        for(i = 0; i < nodes.length; i++){
            for(int j = i+1; j < nodes.length; j++){
                Edge cur = new Edge(nodes[i], nodes[j]);
                edges.add(cur);
            }
        }
    }
    
    public void findMaxSpanTree(){
        
    }
    
    class Node {
        ArrayList connections;
        String name;
        
        public Node(String n){
            connections = new ArrayList();
            name = n;
        }
        
        public void addConnection(Node c){
            connections.add(c);
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
