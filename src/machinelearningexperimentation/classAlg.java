/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machinelearningexperimentation;

/**
 *
 * @Ryan Freivalds s53q433
 */

//IMPORTANT! This class is abstact! We only ever make instances of the classification algorithms as subclasses to this abstract class!
//This class generalizes the process each alg is going follow-- it takes filled input, makes testing and training sets, 
//passes those to a method that trains the algorithm, tracks our hit/miss rate and calling 
//the actual algorithm implementation from it's subclass objects.
public abstract class classAlg { 
    public int size; //how many entries are being read in
	public int attSize; //how many attributes belong to a single entry
    String[][] sortSet; //represents our raw input
    String[][] readySet; //uses the filled/read in input to produce an array representing our training and testing set.
    String[][] trainingSet;
    String[][] testSet; 
    
//might be a pain to read in all those values-- since we'll need to differentiate what we want the variables to be
//or we take the time to hard code them in, unless someone is expereience in how to read in largs amounts of data.
    
    public void run(){//***THIS OCCURES FOR EVERY ALGORITHM IMPLEMENTATION.***
        /*This method represents the general outline of the process every algorithm will make use of.*/
        //fillSet(sortSet[]); //fills in any missing values, if nessissary, for the dataset.
        //Fillset will need to occure before run is called, to ensure the exact same filled in values are used for each algorithm.
       
        sortSet = readSet(null); //accepts our data for local object use. 
        
        readySet = newSets(sortSet); 
        /*randomly seperates our data entries into a test and training sets. 
        The training set will be the first half of the array and the testing set will be the later half.
        This will be  be called multiple times by the sorting alg.*/
        
        classifyTest(readySet);
        /*
        Encompases the sorting algorithm's execution (another method in each class) coupled with multiple train/test runs
        returns our accuracy, or issues that occured while sorting.
        */
    } 
    
    
    
    public String[][] readSet(String[][] newInput){
        //needs to find or read in one data set for the algorithm to run on, and put it in an array.
        //returns that array for use.
	    
        return null;
    }
    
    public void fillSet(String[][] dataEntries){
        //returns nothing, as it fills in any empty values for use in all future runs.
        //this may need to occure "outside" of this process, because EVERY algorithm needs to use the same filled in values.
        //enless we can ensure this method fills in the entries with the same values each time (unlikely).
    }

    private String[][] newSets(String[][] sortSet) {
        //This method represents our 5 x 2 "folding" for cross validation.
        //All this method does is take our filled in input array, and shuffles the indexes into a new array, using every index
        //the first half of this new array represents out training set, the last half our testing set.
        //This method will be called many times while an algorithm is being assessed.
	String[][] shuffle = new String[sortSet.length][sortSet[1].length];
	String[][] temp = new String[sortSet.length][sortSet[1].length]
	int rand = 0;
	    for (int k = 0; k<3; k++)
		    for (int i = 0; i<sortSet.length; i++){
			 rand = (int)math.random();   
			 temp[i] = sortSet[i];
			 sortSet[i] = sortSet[rand];
			 sortSet[rand] = temp[i];
		    }
		}
        return shuffle;
    }
    
    private void Seperate(){
        //helper method that cuts our sortSet in half, actually setting trainingSet = to the first half and test set = to the last half
        //should be it's own method, because it will need to happen many times
        //returns nothing, as it sets the public variables equal to the halves they need to be
	   for(int i =0; i < (int)(readySet.length)/2; i++){
		trainingSet[i] = readySet[i];
	   }
	   for(int k =(int)(readySet.length)/2; k < readySet.length; k++){
		testSet[k] = readySet[k];
	   }
		   
    }
    

    private double classifyTest(String[][] test) {
        /*this method accepts the ready datasets, calls seperate to cut them into training/testing halves,
        then runs the classification algorithm as persribed. It then calls newSets and repeats however many times we need it to
        before returning the average of each newSet call and classification run*/
        double accuracy;
        double average = 0;
        int trailCount = 0;
        for (int i=0; i < 100; i++){
        Seperate();
        accuracy = algorithm(trainingSet, testSet);
        average = average + accuracy;
        trailCount++;
        }
        average = average/trailCount;
        return 0;
    }
    
    public abstract double algorithm(String[][] train, String[][] test);
        //implemented by each individual class/algorithm. Not here. returns the accuracy for a single training/test set run
    
    
    
}
