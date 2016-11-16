package machinelearningexperimentation;

/**
 *
 * @author Lizzie Herman
 */

import java.util.ArrayList;

public class TAN extends classAlg{

    @Override
    public int[] algorithm(int[][] train, int[][] test) {
        train(train);
        int[] classes = new int[test.length];
        // TO-DO test your algorithm
        return classes;
    }
    
    private void train(int[][] dataset){
        int[] countVal = new int[dataset[0].length];
        ArrayList[] vals = new ArrayList[dataset[0].length];
        String[] nameVal = new String[dataset[0].length - 1];
        for(int i = 0; i < dataset[0].length; i++){
            vals[i] = new ArrayList();
            if(i >= nameVal.length) continue;
            nameVal[i] = Integer.toString(i);
        }
        for(int i = 0; i < dataset.length; i++){
            for(int j = 0; j < dataset[i].length; j++){
                int cur = dataset[i][j];
                if(! vals[j].contains(cur)){
                    vals[j].add(cur);
                    countVal[j]++;
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
