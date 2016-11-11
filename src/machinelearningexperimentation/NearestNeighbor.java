package machinelearningexperimentation;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Lizzie Herman
 *Ryan Freivalds
 */
public class NearestNeighbor extends classAlg{
    //needs a constructor to accept a dataset that's been filled if needed, and it's own classification implementation

 @Override
public double algorithm(String[][] train, String[][] test){ //current implementation assumes String[*][0] holds our classification for an entry.
    int k = 7; //how many nearest neighbours we compare against
    //because the training for nearest neighbour is simply storing out training sets for comparison, we have to extract the details of outcomes.
    ArrayList classes = new ArrayList(); //how many classifications
    ArrayList output[] = new ArrayList[train[0].length]; //how many values an attribute can have

	    for (int n = 0; n<train.length; n++){ 
		    if(!classes.contains(train[n][0])){ //if this classification hasn't been seen
                        classes.add(train[n][0]); //then add it to our list of classifications
                    }
		}
            for (int i = 1; i<train[0].length; i++){//Counting through the attribute TYPES. n=1 because we are not currently conserned with the classification of either set. 
		for (int m = 0; m<train.length; m++){ //checking how many outcomes for a SINGLE TYPE of attribute across MULTIPLE DATA ENTRIES
                    if(!output[i].contains(train[m][i])){ //if this outcome hasn't been seen
                       output[i].add(train[m][i]); //then add it to our list of outcomes for that SPECIFIC attribute [i]
                    }//DIFFICULTY: The number of values for an attribute is not likely to be uniform, making it difficult to instanciate properly sized 2d array to count instances of outcomes.
                }
            }
            //now we know all possible outcomes, but we still need to tally know how many.
            //this is the most brutal nesting of loops I've ever done. IF THERE IS A PROBLEM IT'S PROBABLY HERE.
            int[][] outputCounter; //contains the how many of a specific outputs for each attribute of our dataset, for whatever attribute is held at index *          
            outputCounter = new int[train[0].length][]; 
            //outputCounter[a][b] where [a] is the attribute, [b] is the type of outcome witnessed, holding the number of times it was seen.
            //we know how many attributes there are right now, but not nessessarily how many outcomes there were per attribute.
            for (int a = 1; a<train[0].length; a++){//Counting through the attribute TYPES.
                outputCounter[a] = new int[output[a].size()]; //so for attribute [a], outputCounter[a][] becomes outputCounter[a][how many kinds of output were seen]
                for (int m = 0; m<train.length; m++){ //checking how many outcomes for a SINGLE TYPE of attribute across MULTIPLE DATA ENTRIES
                    for (int b = 0; b<output[a].size(); b++){ //for each outcome for that attribute
                        if (train[m][a] == output[a].get(b)){ //if that outcome was seen for that attribute
                            outputCounter[a][b] += 1; //iterate that outcome
                        }
                    }
                }
            }
            //I can only really, really hope that works as intended. Going to have to cram a shit ton of print statements in there later to make sure, probably.
            //So, assumably, we know now have how many outcomes were seen for each outcome for each attribute of the training set. That should be enough to vdm.
     return 0;
}

public double vdm(String attribute1, String attribute2, ArrayList classes){
    //value difference metric method, called on by the algorithm
    double distence = 0;
    double sum = 0;
    for (int i = 1; i<=classes.size(); i++ ){ //finding the summation of the number of classes
        
    }
    return distence;
}
}
