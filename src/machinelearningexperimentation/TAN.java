package machinelearningexperimentation;

/**
 *
 * @author Lizzie Herman
 */

import java.util.ArrayList;
import java.util.Arrays;

public class TAN extends classAlg{

    @Override
    public int[] algorithm(int[][] train, int[][] test) {
        train(train);
        int[] classes = new int[test.length];
        // TO-DO test your algorithm
        return classes;
    }
    
    private void train(int[][] dataset){
        int numAttr = dataset[0].length - 1;
        int[] countVal = new int[numAttr + 1];
        ArrayList[] vals = new ArrayList[numAttr + 1];
        String[] nameVal = new String[numAttr];
        for(int i = 0; i <= numAttr; i++){
            vals[i] = new ArrayList();
            if(i >= numAttr) continue;
            nameVal[i] = Integer.toString(i);
        }
        for(int i = 0; i < dataset.length; i++){
            for(int j = 0; j <= numAttr; j++){
                int cur = dataset[i][j];
                if(! vals[j].contains(cur)){
                    vals[j].add(cur);
                    countVal[j]++;
                }
            }
        }
        //System.out.println(Arrays.toString(countVal));
        
        Graph graph = new Graph();
        graph.createFullGraph(nameVal);
        //System.out.println("num nodes: " + graph.nodes.size() + " num edges: " + graph.edges.size());
        for(int i = 0; i < numAttr; i++){
            for(int j = i+1; j < numAttr; j++){
                double totalProportion = 0;
                // TO-DO calculate proportions of these to attributes together
                graph.addWeight(totalProportion, Integer.toString(i), Integer.toString(j));
            }
        }
        
    }
    

    @Override
    public String getName() {
        return "TAN";
    }


}
