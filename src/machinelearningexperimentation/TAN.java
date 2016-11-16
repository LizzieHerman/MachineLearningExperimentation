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
        System.out.println(Arrays.toString(countVal));
        for(int i = 0; i < numAttr; i++){
            for(Object v : vals[i]){
                for(Object c : vals[numAttr]){
                    //blah
                }
            }
        }
        
        Graph graph = new Graph(countVal.length - 1);
        graph.createFullGraph(nameVal);
    }
    

    @Override
    public String getName() {
        return "TAN";
    }


}
