package machinelearningexperimentation;

/**
 *
 * @author Lizzie Herman
 * Ryan Freivalds
 */
public class NaiveBayes extends classAlg{

    @Override
    public int[] algorithm(int[][] train, int[][] test) {
        // TO-DO train your algorithm
        int[] classes = new int[test.length];
        // TO-DO test your algorithm
        return classes;
    }
    //needs a constructor to accept a dataset that's been filled if needed, and it's own classification implementation

    @Override
    public String getName() {
        return "Naive Bayes";
    }

 
}
