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
    static String[][] cancer = new String[699][11]; // all int values except those that are missing
    static String[][] glass = new String[214][11]; // all double values, except id num and class which are ints
    static String[][] iris = new String[150][5]; // all double values, except class which is string can easily be converted into int
    static String[][] soybean = new String[47][36]; // all int values, except class which is string can easily be converted into int
    static String[][] vote = new String[435][17]; // all char values, except class which is string can easily be converted into char

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
        
        try {
            br = new BufferedReader(new FileReader(cancerFile));
            int i = 0;
            line = br.readLine();
            while (line != null) {
                cancer[i] = line.split(cvsSplitBy);
                i++;
                line = br.readLine();
            }
            br.close();
        
            br = new BufferedReader(new FileReader(glassFile));
            i = 0;
            line = br.readLine();
            while (line != null) {
                glass[i] = line.split(cvsSplitBy);
                i++;
                line = br.readLine();
            }
            br.close();
        
            br = new BufferedReader(new FileReader(irisFile));
            i = 0;
            line = br.readLine();
            while (line != null) {
                iris[i] = line.split(cvsSplitBy);
                i++;
                line = br.readLine();
            }
            br.close();
            
            br = new BufferedReader(new FileReader(soybeanFile));
            i = 0;
            line = br.readLine();
            while (line != null) {
                soybean[i] = line.split(cvsSplitBy);
                i++;
                line = br.readLine();
            }
            br.close();
       
            br = new BufferedReader(new FileReader(voteFile));
            i = 0;
            line = br.readLine();
            while (line != null) {
                vote[i] = line.split(cvsSplitBy);
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

