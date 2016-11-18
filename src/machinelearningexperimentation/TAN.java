package machinelearningexperimentation;

/**
 *
 * @author Lizzie Herman
 */

import java.util.*;

public class TAN extends classAlg{

    @Override
    public int[] algorithm(int[][] train, int[][] test) {
        Tree tree = train(train);
        int[] classes = new int[test.length];
        // TO-DO test your algorithm
        for(int i = 1; i < test.length; i++){
            int[] data = test[i];
            ArrayList<Integer> list = numClasses(train);
            // update this code when done
            // classes[i] = test(data, tree);
            Random next = new Random();
            classes[i] = list.get(next.nextInt(list.size()));
            System.out.println("Assigned this entry to class " + classes[i]);
            
        }
        return classes;
    }
    
    private ArrayList<Integer> numClasses(int[][] dataset){
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < dataset.length; i++){
            int a = dataset[i][dataset[0].length - 1];
            if(list.contains(a)) continue;
            list.add(a);
        }
        return list;
    }
    
    private Tree train(int[][] dataset){
        int numAttr = dataset[0].length - 1;
        int[] countVal = new int[numAttr + 1];
        Hashtable<Integer,Hashtable<Integer,Integer>> uncondValues = new Hashtable<Integer,Hashtable<Integer,Integer>>();
        for(int i = 0; i <= numAttr; i++){
            Hashtable<Integer,Integer> temp = new Hashtable<Integer,Integer>();
            for(int j = 0; j < dataset.length; j++){
                int cur = dataset[j][i];
                if(temp.containsKey(cur)){
                    int a = temp.get(cur);
                    temp.replace(cur, a+1);
                } else {
                    temp.put(cur, 1);
                    countVal[i]++;
                }
            }
            uncondValues.put(i, temp);
        }
        //System.out.println(Arrays.toString(countVal));
        Hashtable<Integer,Hashtable<Integer,Hashtable<Integer,Double>>> condValues = new Hashtable<Integer,Hashtable<Integer,Hashtable<Integer,Double>>>();
        // <which attribute X,<attribute value xi,<class value c, P(xi|c)>>>
        Hashtable<Integer,Integer> classes = uncondValues.get(numAttr);
        for(int i = 0; i < numAttr; i++){
            Hashtable<Integer,Hashtable<Integer,Double>> attribute = new Hashtable<Integer,Hashtable<Integer,Double>>();
            for(Enumeration<Integer> e = uncondValues.get(i).keys(); e.hasMoreElements();){
                int attKey  = e.nextElement();
                Hashtable<Integer,Double> classification = new Hashtable<Integer,Double>();
                for(Enumeration<Integer> u = classes.keys(); u.hasMoreElements();){
                    int classKey = u.nextElement();
                    int countBoth = 0;
                    int countClass = 0;
                    for(int j = 0; j < dataset.length; j++){
                        int cur = dataset[j][i];
                        int cla = dataset[j][numAttr];
                        if(cla == classKey){
                            countClass++;
                            if(cur == attKey) countBoth++;
                        }
                    }
                    double prob = (double)countBoth/countClass;
                    classification.put(classKey, prob);
                }
                attribute.put(attKey, classification);
            }
            condValues.put(i, attribute);
        }
        Graph graph = new Graph();
        graph.createFullGraph(uncondValues, condValues, dataset.length);
        //System.out.println("num nodes: " + graph.nodes.size() + " num edges: " + graph.edges.size());
        for(int i = 0; i < numAttr; i++){
            for(int j = i+1; j < numAttr; j++){
                Hashtable<Integer,Hashtable<Integer,Hashtable<Integer,Double>>> att1 = new Hashtable<Integer,Hashtable<Integer,Hashtable<Integer,Double>>>();
                //<att_val_one<att_val 2< class,weight>>>        
                Graph.Edge edge = graph.getEdge(Integer.toString(i), Integer.toString(j));
                double totalProb = 0;
                for(Enumeration<Integer> e = uncondValues.get(i).keys(); e.hasMoreElements();){
                    Hashtable<Integer,Hashtable<Integer,Double>> att2 = new Hashtable<Integer,Hashtable<Integer,Double>>();
                    int keyI = e.nextElement();
                    for(Enumeration<Integer> u = uncondValues.get(j).keys(); u.hasMoreElements();){
                        Hashtable<Integer,Double> clss = new Hashtable<Integer,Double>();
                        int keyJ = u.nextElement();
                        for(Enumeration<Integer> r = classes.keys(); r.hasMoreElements();){
                            int classKey = r.nextElement();
                            int countAll = 0;
                            int countClass = 0;
                            for(int k = 0; k < dataset.length; k++){
                                if(dataset[k][numAttr] == classKey){
                                    countClass++;
                                    if(dataset[k][i] == keyI && dataset[k][j] == keyJ) countAll++;
                                }
                            }
                            double probAll = (double)countAll/dataset.length;
                            //System.out.print("probAll: " + probAll);
                            double probCondDepend = (double)countAll/countClass;
                            //System.out.print(" probCondDepend: " + probCondDepend);
                            double probCondIndepend = 0;
                            if(edge.one.isNode(Integer.toString(i))){
                                probCondIndepend = edge.one.condProb.get(keyI).get(classKey) * edge.two.condProb.get(keyJ).get(classKey);
                            } else {
                                probCondIndepend = edge.one.condProb.get(keyJ).get(classKey) * edge.two.condProb.get(keyI).get(classKey);
                            }
                            //System.out.print(" probCondIndepend: " + probCondIndepend);
                            double logVal = Math.log(probCondDepend / probCondIndepend);
                            //System.out.println(" logVal: " + logVal);
                            double curProb = 0;
                            if(probAll != 0 && probCondDepend != 0 && probCondIndepend != 0) curProb = (probAll * logVal);
                            totalProb += curProb;
                            clss.put(classKey, curProb);
                        }
                        att2.put(keyJ, clss);
                    }
                    att1.put(keyI, att2);
                }
                graph.addWeight(totalProb, att1, Integer.toString(i), Integer.toString(j));
            }
        }
        System.out.println("Calculated Weights: " + graph.edges);
        return graph.findMaxSpanTree();
    }
    
    public int test(int[] data, Tree tree){
        return 1;
    }
    

    @Override
    public String getName() {
        return "TAN";
    }


}
