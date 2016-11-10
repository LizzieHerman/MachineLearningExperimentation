package machinelearningexperimentation;

/**
 *
 * @author Lizzie Herman
 * @author Ryan Freivalds
 * @author Connor O’Leary
 * @author Greg Neznanski
 */

import java.io.*;
import java.util.Random;
public class MachineLearningExperimentation {
    static int[][] cancer = new int[699][11]; // missing values are recorded in array -1
    static double[][] contGlass = new double[214][11]; // all double values, except id num and class which are ints
    static double[][] contIris = new double[150][5]; // Iris-setosa = 1; Iris-versicolor = 2; Iris-virginica = 3
    static int[][] soybean = new int[47][36]; // class values D1-D4 to 1-4
    static char[][] vote = new char[435][17]; // republican = r; democrat = d
    static int[][] glass = new int[214][11]; // for when we discretize the data
    static int[][] iris = new int[150][5]; // for when we discretize the data
    

    public static void main(String[] args) {
       readFiles();
       // TO-DO get rid of the unknown values
       replaceUnknowns();
       // TO-DO convert continuous values to discrete ones
       discretize(10);
       // split data into training and data sets
       
       // Run these for each algorithm
       // TO-DO run training set
       // TO-DO run test set
       // TO-DO check how test set did
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
                    vote[i][j] = split[j].charAt(0);
                }
                i++;
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
                if(irisVals[j][0] == -1){
                    iris[i][j] = (int)current;
                    continue;
                }
                int k = 0;
                while(k < n && current <= irisVals[j][k]){
                    k++;
                }    
                iris[i][j] = k;
            }
        }
        for(int i = 0; i < contGlass.length; i++){
            for(int j = 0; j < contGlass[i].length; j++){
                current = contGlass[i][j];
                if(glassVals[j][0] == -1){
                    glass[i][j] = (int)current;
                    continue;
                }
                int k = 0;
                while(k < n && current <= glassVals[j][k]){
                    k++;
                }    
                glass[i][j] = k;
            }
        }
    }
}

