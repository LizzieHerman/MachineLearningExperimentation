package machinelearningexperimentation;

import java.util.Arrays;
import java.util.Random;
import java.io.*;

/**
 * @author Lizzie Herman
 * @author Ryan Freivald

 */

//IMPORTANT! This class is abstact! We only ever make instances of the classification algorithms as subclasses to this abstract class!
//This class generalizes the process each alg is going follow-- it takes filled input, makes testing and training sets, 
//passes those to a method that trains the algorithm, tracks our hit/miss rate and calling 
//the actual algorithm implementation from it's subclass objects.
public abstract class classAlg {
    public void run(int[][] input){
        int[][] totalSet = input;
        int size = (input.length / 2);
        int[][] trainSet = new int[size][input[0].length];
        int[][] testSet = new int[input.length - size][input[0].length];
        
        // print out the name of the algorithm being used
        // System.out.println(getName());
        writeToResult(getName());
                
        // run the algorithm
        // 5x2 cross validation means we ran the program 5 times
        for(int i = 0; i < 5; i++){
            totalSet = shuffleSets(totalSet);
            //separate sets
            trainSet = Arrays.copyOfRange(input, 0, trainSet.length);
            testSet = Arrays.copyOfRange(input, trainSet.length, input.length);
            
            int[] classes = algorithm(trainSet, testSet);
            
            // count the number of right and wrong classifications
            int right = 0;
            int wrong = 0;
            for(int j = 0; j < testSet.length; j++){
                if(classes[j] == testSet[j][testSet[j].length - 1]) right++;
                else wrong++;
            }
            
            double propRight = right/testSet.length;
            
            // print out the proportion of right answers
            //System.out.println(propRight);
            writeToResult(Double.toString(propRight));
        }
    }
    
    public void writeToResult(String str){
        try {
            PrintWriter result = new PrintWriter(new BufferedWriter(new FileWriter("results.csv", true)));
            result.print("," + str);
            result.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private int[][] shuffleSets(int[][] input){
        int[][] shuffled = new int[input.length][input[0].length];
        Random rand = new Random();
        // using the Fisher-Yates algorithm to shuffle entries
        for(int i = input[0].length - 1; i > 0; i--){
            int j = rand.nextInt(i);
            shuffled[j] = input[i];
        }
        return shuffled;
    }
    public abstract String getName();
    public abstract int[] algorithm(int[][] train, int[][] test);
        /**
         * implemented by each individual class/algorithm. Not here. 
         * Returns an integer array of the classes given to the entries in the test set
         */     

}
