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
        int size1 = (input.length / 2);
        int size2 = (input.length - size1);
        int[][] setOne = new int[size1][input[0].length];
        int[][] setTwo = new int[size2][input[0].length];
        
        
        // print out the name of the algorithm being used
        // System.out.println(getName());
        writeToResult(getName());
                
        // run the algorithm
        // 5x2 cross validation means we ran the program 5 times
        for(int i = 0; i < 5; i++){
            totalSet = shuffleSets(totalSet);
            //separate sets
            setOne = Arrays.copyOfRange(totalSet, 0, size1);
            setTwo = Arrays.copyOfRange(totalSet, size1, input.length);
            
            int[] classes = algorithm(Arrays.copyOf(setOne, size1), Arrays.copyOf(setTwo, size2));
            // count the number of right and wrong classifications
            int right = 0;
            for(int j = 0; j < size2; j++){
                if(classes[j] == setTwo[j][setTwo[j].length - 1]) right++;
            }
            double propRight = right/size2;
            // print out the proportion of right answers
            //System.out.println(propRight);
            writeToResult(Double.toString(propRight));
            
            // flip test/train sets and run again
            classes = algorithm(Arrays.copyOf(setTwo, size2), Arrays.copyOf(setOne, size1));
            // count the number of right and wrong classifications
            right = 0;
            for(int j = 0; j < size1; j++){
                if(classes[j] == setOne[j][setOne[j].length - 1]) right++;
            }
            propRight = right/size1;
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
        Random rand = new Random();
        // using the Fisher-Yates algorithm to shuffle entries
        for(int i = input.length - 1; i > 0; i--){
            int j = rand.nextInt(i);
            int[] temp = input[j];
            input[j] = input[i];
            input[i] = temp;
        }
        return input;
    }
    public abstract String getName();
    public abstract int[] algorithm(int[][] train, int[][] test);
        /**
         * implemented by each individual class/algorithm. Not here. 
         * Returns an integer array of the classes given to the entries in the test set
         */     

}
