package machinelearningexperimentation;

/**
 *
 * @author Lizzie Herman
 * @author Ryan Freivalds
 * @author Connor O’Leary
 * @author Greg Neznanski
 */

import java.io.*;
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
       // TO-DO convert continuous values to discrete ones
       
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
}

