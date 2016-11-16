package machinelearningexperimentation;

/**
 *
 * @author Lizzie Herman
 * @author Ryan Freivalds
 * @author Connor O’Leary
 * @author Greg Neznanski
 */

import java.io.*;
import java.util.*;
public class MachineLearningExperimentation {
    
    /**
     * the last integer of each array is the classification of that entry
     */
    static int[][] cancer = new int[699][11]; // missing values are recorded in array -1
    static double[][] contGlass = new double[214][11]; // all double values, except id num and class which are ints
    static double[][] contIris = new double[150][5]; // Iris-setosa = 1; Iris-versicolor = 2; Iris-virginica = 3
    static int[][] soybean = new int[47][36]; // class values D1-D4 to 1-4
    static int[][] vote = new int[435][17]; // republican = 1; democrat = 2; y = 1; n = 0; ? = -1
    static int[][] glass = new int[214][11]; // for when we discretize the data
    static int[][] iris = new int[150][5]; // for when we discretize the data
    

    public static void main(String[] args) {
        // read txt files into arrays
        readFiles();
        // get rid of the unknown values
        replaceUnknowns();
        //  convert continuous values to discrete ones
        discretize(10);
        // split data into training and data sets
        classToLast();
        // Run these for each algorithm
        int[][] datasets[] = {cancer, glass, iris, soybean, vote};
        classAlg[] algorithms = {new NearestNeighbor(), new ID3(), new NaiveBayes(), new TAN()};
        int i = 0;
        for(int[][] dataset : datasets){
            for(classAlg algorithm : algorithms){
                writeToResult(i);
                algorithm.run(dataset);
                writeToResult(10);
            }
            i++;
        }
    }
    
    static void writeToResult(int dataset){
        try {
            PrintWriter result = new PrintWriter(new BufferedWriter(new FileWriter("results.csv", true)));
            switch(dataset){
                case 0:
                    result.print("cancer");
                    break;
                case 1:
                    result.print("glass");
                    break;
                case 2:
                    result.print("iris");
                    break;
                case 3:
                    result.print("soybean");
                    break;
                case 4:
                    result.print("vote");
                    break;
                default:
                    result.println();
                    break;
            }
            result.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
    static void readFiles(){
        String cancerFile = "breast-cancer-wisconsin.data.txt";
        String glassFile = "glass.data.txt";
        String irisFile = "iris.data.txt";
        String soybeanFile = "soybean-small.data.txt";
        String voteFile = "house-votes-84.data.txt";
        // these files need to be in theproject folder for this to work
        
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        String[] split;
        
        try {
            br = new BufferedReader(new FileReader(cancerFile));
            int i = 0;
            line = br.readLine();
            while (line != null) {
                split = line.split(cvsSplitBy);
                for(int j = 0; j < cancer[i].length; j++){
                    if(split[j].equalsIgnoreCase("?")) cancer[i][j] = -1;
                    else cancer[i][j] = Integer.parseInt(split[j]);
                }
                i++;
                line = br.readLine();
            }
            br.close();
        
            br = new BufferedReader(new FileReader(glassFile));
            i = 0;
            line = br.readLine();
            while (line != null) {
                split = line.split(cvsSplitBy);
                for(int j = 0; j < contGlass[i].length; j++){
                    contGlass[i][j] = Double.parseDouble(split[j]);
                }
                i++;
                line = br.readLine();
            }
            br.close();
        
            br = new BufferedReader(new FileReader(irisFile));
            i = 0;
            line = br.readLine();
            while (line != null) {
                split = line.split(cvsSplitBy);
                for(int j = 0; j < contIris[i].length; j++){
                    if(split[j].equalsIgnoreCase("iris-setosa")) contIris[i][j] = 1;
                    else if(split[j].equalsIgnoreCase("iris-versicolor")) contIris[i][j] = 2;
                    else if(split[j].equalsIgnoreCase("iris-virginica")) contIris[i][j] = 3;
                    else contIris[i][j] = Double.parseDouble(split[j]);
                }
                i++;
                line = br.readLine();
            }
            br.close();
            
            br = new BufferedReader(new FileReader(soybeanFile));
            i = 0;
            line = br.readLine();
            while (line != null) {
                split = line.split(cvsSplitBy);
                for(int j = 0; j < soybean[i].length; j++){
                    if(split[j].equalsIgnoreCase("d1")) soybean[i][j] = 1;
                    else if(split[j].equalsIgnoreCase("d2")) soybean[i][j] = 2;
                    else if(split[j].equalsIgnoreCase("d3")) soybean[i][j] = 3;
                    else if(split[j].equalsIgnoreCase("d4")) soybean[i][j] = 4;
                    else soybean[i][j] = Integer.parseInt(split[j]);
                }
                i++;
                line = br.readLine();
            }
            br.close();
       
            br = new BufferedReader(new FileReader(voteFile));
            i = 0;
            line = br.readLine();
            while (line != null) {
                split = line.split(cvsSplitBy);
                for(int j = 0; j < vote[i].length; j++){
                    char current = split[j].charAt(0);
                    switch(current){
                        case 'y':
                            vote[i][j] = 1;
                            break;
                        case 'n':
                            vote[i][j] = 0;
                            break;
                        case '?':
                            vote[i][j] = -1;
                            break;
                        case 'r':
                            vote[i][j] = 1;
                            break;
                        case 'd':
                            vote[i][j] = 2;
                            break;
                        default:
                            vote[i][j] = 10;
                    }       
                }
                i++;
                line = br.readLine();
            }
            br.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    static void replaceUnknowns(){
        Random rand = new Random();
        for(int i = 0; i < cancer.length; i++){
            for(int j = 0; j < cancer[i].length; j++){
                if(cancer[i][j] == -1){
                    cancer[i][j] = 1 + rand.nextInt(10);
                    /**
                     * this just randomly assigns a number in the range (1 - 10)
                     * to the unknown value, we might want to make it less random
                     * so it fits with the other data better
                     */
                }
            }
        }
    }
    
    static void discretize(int n){
        double[][] irisExtrema = {{4.3,2,1,0.1,-1},{7.9,4.4,6.9,2.5,-1}};
        double[][] glassExtrema = {{-1,1.5112,10.73,0,0.29,69.81,0,5.43,0,0,-1},
            {-1,1.5339,17.38,4.49,3.5,75.41,6.21,16.19,3.15,0.51,-1}};
        
        double increment = 0;
        double[][] irisVals = new double[5][n];
        for(int i = 0; i < irisVals.length; i++){
            irisVals[i][0] = irisExtrema[0][i];
            increment = (irisExtrema[1][i] - irisExtrema[0][i]) / n;
            for(int j = 1; j < n-1; j++){
                irisVals[i][j] = irisVals[i][j-1] + increment;
            }
            irisVals[i][n-1] = irisExtrema[1][i];
        }
        double[][] glassVals = new double[11][n];
        for(int i = 0; i < glassVals.length; i++){
            glassVals[i][0] = glassExtrema[0][i];
            increment = (glassExtrema[1][i] - glassExtrema[0][i]) / n;
            for(int j = 1; j < n-1; j++){
                glassVals[i][j] = glassVals[i][j-1] + increment;
            }
            glassVals[i][n-1] = glassExtrema[1][i];
        }
        
        double current = 0;
        for(int i = 0; i < contIris.length; i++){
            for(int j = 0; j < contIris[i].length; j++){
                current = contIris[i][j];
                if(j == contIris[0].length - 1 ){
                    iris[i][j] = (int)current;
                    continue;
                }
                for(int k = 0; k < n; k++){
                    if(current < irisVals[j][k]){
                        iris[i][j] = k;
                        break;
                    }
                }
            }
        }
        for(int i = 0; i < contGlass.length; i++){
            for(int j = 0; j < contGlass[i].length; j++){
                current = contGlass[i][j];
                if(j == contGlass[0].length - 1 || j == 0){
                    glass[i][j] = (int)current;
                    continue;
                }
                for(int k = 0; k < n; k++){
                    if(current < glassVals[j][k]){
                        glass[i][j] = k;
                        break;
                    }
                }
            }
        }
    }
    
    static void classToLast(){
        for(int i = 0; i < vote.length; i++){
            int classification = vote[i][0];
            for(int j = 0; j < vote[i].length - 1; j++){
                vote[i][j] = vote[i][j+1];
            }
            vote[i][16] = classification;
        }
    }
}

