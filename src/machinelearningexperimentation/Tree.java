package machinelearningexperimentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import machinelearningexperimentation.Graph.Node;

/**
 *
 * @author Lizzie Herman
 */
public class Tree {
    Node root;
    ArrayList<Node> nodes;
    ArrayList<Edge> edges;
    
    public Tree(Node n){
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
        root = n;
    }
    public Tree(ArrayList<Graph.Node> nos){
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
        for(Graph.Node n : nos){
            nodes.add(new Node(n));
        }
    }
    
    public void createTree(ArrayList<Graph.Edge> weighted){
        String[] names = new String[nodes.size()];
        for(int i = 0; i < nodes.size(); i++){
            names[i] = nodes.get(i).name;
        }
        Graph graph = new Graph(names);
        Graph.Edge oldEdge = weighted.get(0);
        weighted.remove(0);
        Graph.Edge newEdge = graph.addEdge(oldEdge.one.name, oldEdge.two.name);
        while(graph.edges.size() < nodes.size() && ! weighted.isEmpty()){
            oldEdge = weighted.get(0);
            weighted.remove(oldEdge);
            ArrayList<Graph.Edge> connected = new ArrayList<Graph.Edge>();
            connected.add(oldEdge);
            boolean addIt = true;
            for(int i = 0; i < connected.size(); i++){
                Graph.Edge edge2 = connected.get(i);
                for(Graph.Edge edge1 : graph.edges){
                    if(edge1.one.isNode(edge2.one.name) || edge1.one.isNode(edge2.two.name) || edge1.two.isNode(edge2.one.name) || edge1.two.isNode(edge2.two.name)){
                        if(connected.contains(edge1)){
                            addIt = false;
                            break;
                        }
                        connected.add(edge1);
                    }
                }
                if(! addIt) break;
            }
            if(addIt) newEdge = graph.addEdge(oldEdge.one.name, oldEdge.two.name);
        }
        Collections.sort(graph.edges, graph.edgeWeight);
        for(Graph.Edge edge : graph.edges){
            if(edge.one.connections.size() == 1){
                root = getNode(edge.one.name);
                break;
            }
            if(edge.two.connections.size() == 1){
                root = getNode(edge.two.name);
                break;
            }
        }
        if(root == null) root = getNode(graph.edges.get(0).one.name);
        addNodes(root, graph);
        System.out.println("Built This Max spanning tree " + edges);
    }
    
    public void addNodes(Node current, Graph graph){
        Graph.Node n1 = graph.getNode(root.name);
        for(Graph.Node n2 : n1.connections){
            Node n3 = getNode(n2.name);
            if(n3.parents.contains(current)) continue;
            current.children.add(n3);
            n3.parents.add(current);
            Edge edge = new Edge(current, n3);
            Graph.Edge edge1 = graph.getEdge(edge.parent.name, edge.child.name);
            edge.prob = edge1.prob;
            edges.add(edge);
            addNodes(n3, graph);
        }
    }
    
    public boolean wontCreateCycle(Graph.Edge e){
        Node n1 = getNode(e.one.name);
        Node n2 = getNode(e.two.name);
        
        ArrayList<Edge> connected = new ArrayList<Edge>();
                
        return false;
    }
    
    public Node getNode(String nam){
        for(Node n : nodes){
            if(n.isNode(nam)) return n;
        }
        return null;
    }

    class Node{
        String name;
        ArrayList<Node> parents;
        ArrayList<Node> children;
        Hashtable<Integer,Double> proportions = new Hashtable<Integer,Double>();
        Hashtable<Integer,Hashtable<Integer,Double>> condProb;
        
        public Node(String n){
            parents = new ArrayList<Node>();
            children = new ArrayList<Node>();
            name = n;
        }
        public Node(Graph.Node node){
            parents = new ArrayList<Node>();
            children = new ArrayList<Node>();
            name = node.name;
            proportions = node.proportions;
            condProb = node.condProb;
        }
        
        public boolean isNode(String n){
            return name.equalsIgnoreCase(n);
        }
        
        public boolean equals(Node other){
            return name.equalsIgnoreCase(other.name);
        }
    }
    class Edge{
        Node parent;
        Node child;
        double weight;
        Hashtable<Integer,Hashtable<Integer,Hashtable<Integer,Double>>> prob = new Hashtable<Integer,Hashtable<Integer,Hashtable<Integer,Double>>>();
        
        public Edge(Node p, Node c){
            parent = p;
            child = c;
        }

        
        public boolean equals(Edge other){
            return parent.equals(other.parent) && child.equals(other.child);
        }

        public boolean isEdge(String a, String b){
            return parent.isNode(a) && child.isNode(b);
        }

        public String toString(){
            return "Parent Node: " + parent.name + " Child Node: " + child.name;
        }
    }
}
