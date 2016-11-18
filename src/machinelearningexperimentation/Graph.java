package machinelearningexperimentation;

/**
 *
 * @author Lizzie Herman
 */

import java.util.*;

public class Graph {
    ArrayList<Node> nodes;
    ArrayList<Edge> edges;
    Comparator edgeWeight = new Comparator<Edge>(){ // used to sort edges by weight descending
        @Override
        public int compare(Edge o1, Edge o2) {
            return (int)((o2.weight - o1.weight) * 10000);
        }
    };
    
    public Graph(){
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
    }
    public Graph(String[] names) {
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
        for(String n : names){
            nodes.add(new Node(n));
        }
    }
    
    public Edge addEdge(String a, String b){
        Node one = null;
        Node two = null;
        for(Node n : nodes){
            if(n.isNode(a)) one = n;
            if(n.isNode(b)) two = n;
        }
        Edge e = new Edge(one, two);
        edges.add(e);
        return e;
    }
    
    public boolean addWeight(double w, Hashtable<Integer,Hashtable<Integer,Hashtable<Integer,Double>>> prob, String a, String b){
        for(int i = 0; i < edges.size(); i++){
            if((edges.get(i)).isEdge(a, b)){
                (edges.get(i)).setWeight(w);
                (edges.get(i)).prob = prob;
                //System.out.println(w);
                return true;
            }
        }
        return false;
    }
    
    public void createFullGraph(Hashtable<Integer,Hashtable<Integer,Integer>> uncondValues, Hashtable<Integer,Hashtable<Integer,Hashtable<Integer,Double>>> condValues, int total){
        for(int i = 0; i < uncondValues.size() - 1; i++){
            nodes.add(new Node(Integer.toString(i), uncondValues.get(i), condValues.get(i), total));
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
    
    public Edge getEdge(String a, String b){
        for(int i = 0; i < edges.size(); i++){
            if(edges.get(i).isEdge(a, b))return edges.get(i);
        }
        return null;
    }
    
    public Node getNode(String a){
        for(Node n : nodes){
            if(n.isNode(a))return n;
        }
        return null;
    }
    
    public Tree findMaxSpanTree(){
        Tree tree = new Tree(nodes);
        Collections.sort(edges, edgeWeight);
        tree.createTree(edges);
        /*ArrayList<Edge> edgesForTree = new ArrayList<Edge>();
        edgesForTree.add(edges.get(0));
        while(edgesForTree.size() < nodes.size()){
            ArrayList<Edge> connected = new ArrayList<Edge>();
        }*/
        return tree;
    }
    
    class Node {
        ArrayList<Node> connections;
        String name;
        Hashtable<Integer,Double> proportions = new Hashtable<Integer,Double>();
        Hashtable<Integer,Hashtable<Integer,Double>> condProb;
        
        public Node(String n){
            connections = new ArrayList<Node>();
            name = n;
        }
        public Node(String n, Hashtable<Integer,Integer> counts, int total){
            connections = new ArrayList<Node>();
            name = n;
            for(Enumeration<Integer> e = counts.keys(); e.hasMoreElements();){
                Integer i = e.nextElement();
                proportions.put(i, ((double)counts.get(i)/total));
            }
        }
        public Node(String n, Hashtable<Integer,Integer> counts, Hashtable<Integer,Hashtable<Integer,Double>> prob, int total){
            connections = new ArrayList<Node>();
            name = n;
            for(Enumeration<Integer> e = counts.keys(); e.hasMoreElements();){
                Integer i = e.nextElement();
                proportions.put(i, ((double)counts.get(i)/total));
            }
            condProb = prob;
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
    public class Edge {
        Node one;
        Node two;
        double weight;
        Hashtable<Integer,Hashtable<Integer,Hashtable<Integer,Double>>> prob = new Hashtable<Integer,Hashtable<Integer,Hashtable<Integer,Double>>>();

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

        public String toString(){
            return "Node One: " + one.name + " Weight: " + weight + " Node Two: " + two.name;
        }
    }
}
