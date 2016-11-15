package machinelearningexperimentation;

import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 *
 * @author Lizzie Herman Ryan Freivalds
 */
public class NearestNeighbor extends classAlg {

    //needs a constructor to accept a dataset that's been filled if needed, and it's own classification implementation
    //to allow vdm to use these variables:
    private ArrayList output[]; //how many values an attribute can have
    private String[][] train;
    private ArrayList classes; //how many classifications
    private int[][] outputCounter;//contains the how many of a specific outputs for each attribute of our dataset, for whatever attribute is held at index *          

    @Override
    public double algorithm(String[][] train, String[][] test) { //current implementation assumes String[*][0] holds our classification for an entry.
        int k = 8; //how many nearest neighbours we compare against

        //because the training for nearest neighbour is simply storing out training sets for comparison, we have to extract the details of outcomes.
        this.classes = new ArrayList(); //how many classifications
        ArrayList[] output = new ArrayList[train[0].length]; //how many values an attribute can have
       // System.out.println("arraylist array size of "+ train[0].length);
        for (int i = 1; i < train[0].length; i++) {
            output[i] = new ArrayList();
        }
        this.output = output;
        this.train = train;
        System.out.println("Detected " + train[0].length + " attribute fields."); //testline
        for (int n = 0; n < train.length; n++) {
            if (!classes.contains(train[n][0])) { //if this classification hasn't been seen
                classes.add(train[n][0]); //then add it to our list of classifications
            }
        }
        System.out.println("Detected " + classes.size() + " classifications."); //testlines
        for (int i = 1; i < train[0].length; i++) {//Counting through the attribute TYPES. n=1 because we are not currently conserned with the classification of either set. 
            //System.out.println("testing initialization of output["+i+"]: "+ output[i]);
            //output[i] = new ArrayList();
            for (int m = 0; m < train.length; m++) { //checking how many outcomes for a SINGLE TYPE of attribute across MULTIPLE DATA ENTRIES
                if (output[i].contains(train[m][i]) == false) { //if this outcome hasn't been seen
                    output[i].add(train[m][i]); //then add it to our list of outcomes for that SPECIFIC attribute [i]
                }//DIFFICULTY: The number of values for an attribute is not likely to be uniform, making it difficult to instanciate properly sized 2d array to count instances of outcomes.
            }
            System.out.println("Detected " + output[i].size() + " different outcomes for attribute " + i + ".");//testlines
        }
        //now we know all possible outcomes, but we still need to tally know how many.
        //this is the most brutal nesting of loops I've ever done. IF THERE IS A PROBLEM IT'S PROBABLY HERE.        
        outputCounter = new int[train[0].length][];
        //outputCounter[a][b] where [a] is the attribute, [b] is the type of outcome witnessed, holding the number of times it was seen.
        //we know how many attributes there are right now, but not nessessarily how many outcomes there were per attribute.
        for (int a = 1; a < train[0].length; a++) {//Counting through the attribute TYPES.
            outputCounter[a] = new int[output[a].size()]; //so for attribute [a], outputCounter[a][] becomes outputCounter[a][how many kinds of output were seen]
            for (int m = 0; m < train.length; m++) { //checking how many outcomes for a SINGLE TYPE of attribute across MULTIPLE DATA ENTRIES
                for (int b = 0; b < output[a].size(); b++) { //for each outcome for that attribute
                    if (train[m][a] == output[a].get(b)) { //if that outcome was seen for that attribute
                        outputCounter[a][b] += 1; //iterate that outcome. This will give us the total number of times that outcome per attribute was seen.
                    }
                }
            }
        }
        //I can only really, really hope that works as intended. Going to have to cram a shit ton of print statements in there later to make sure, probably.
        //So, assumably, we know now have how many outcomes were seen for each outcome for each attribute of the training set. That should be enough to vdm.
        //So, now that we can execute the VDM, we take a test entry and use the vdm find it's K closest entries, and have them vote on test's class.

        String[][] candidate = new String[k][];//holds our ENTRIES for our nearest neighbours
        for (int i = 0; i < test.length; i++) {// for every entry in our TESTing set
            for (int j = 0; j < test.length; j++) {// for every entry in our TRAINING set
                double[] singdist = new double[test[i].length];//short for "single distences, represents the distences between individual attribues, to be used to calculate the distences of entries from one another.
                //should be cleared out for every training entry than, right?
                for (int a = 1; a < test[i].length; a++) { //for every attribute of both entries that must be compared (excludes the classification).
                    singdist[a] = vdm(a, test[i][a], train[j][a]); //add a new value to the double array for individual attribute distences here.
                } //end of "for every attribute" 
                double[] canddist = new double[k]; //candidate distences to our test point
                //calculate distences between entries here, find the closest ones.
                double entrydist = dist(singdist);
                //insert sort into our array of nearest neighbours? 
                for (int l = 0; l < k - 1; l++) {
                    /*
                        if (canddist[l] == null){ 
                            canddist[l] = entrydist;
                            candidate[l] = train[j];
                            break;
                        }
                       else */
                    if (entrydist < canddist[l]) {
                        String[] temp = candidate[l];
                        candidate[l + 1] = candidate[l];
                        candidate[l] = train[j];
                        double tempdist = canddist[l];
                        canddist[l + 1] = canddist[l];
                        canddist[l] = entrydist;
                        for (int m = l + 1; m < k; m++) { //new distence is inserted, now swap out other values with temp 
                            temp = candidate[m];
                            candidate[m + 1] = candidate[m];
                            candidate[m] = train[j];
                            tempdist = canddist[m];
                            canddist[m + 1] = canddist[m];
                            canddist[m] = entrydist;
                        }
                        break;
                    } else if (entrydist < canddist[k - 1]) { //if we're just dealing with replacing the last&largest value
                        canddist[k - 1] = entrydist;
                        candidate[k - 1] = train[j];
                    }
                }
            } //end of "for every entry" TRAINING
            //we now have a test entry's K nearest neighbours stored in candidate[0] through candidate[k-1]
            //assign a class to the individual test entry here based on the classes of its nearest neighbours
            int winner = -1;
            for (int nn = 0; nn < k; nn++) {
                int[] vote = new int[classes.size()]; //how we tally the votes
                for (int a = 0; a < classes.size(); a++) {
                    vote[a] = 0;//zeroing out the array, just to be safe.
                }

                for (int a = 0; a < classes.size(); a++) {//for all our classifications
                    if (candidate[nn][0] == classes.get(a)) { //if our current nearest neighbour had that classification
                        vote[a]++; //increment that vote, then stop.
                        break;
                    }
                }
                //we now have all the votes
                int mostPop = -1;
                for (int a = 0; a < classes.size(); a++) {
                    if (vote[a] > mostPop) {
                        mostPop = vote[a]; //find the classification with the highest number of votes
                        winner = a; //and save its index for reassignment
                    }
                }
            }
            System.out.println("Assigning entry "+ i + " the classification of " +classes.get(winner) +".");
            test[i][0] = (String) classes.get(winner); //we have now made our choice for a single test entry
        } //end of "for every entry" TESTING
        //now tally our accuracy
        int hit = 0;
        int tally = 0;
        for (int i = 0; i < test[0].length; i++) {//for every test entry
            tally++;
            if( test[i][0] == answerKey[i][0]){//if our newly assigned classification is what the entry actually was
                hit++; //increment our number of correct guesses.
            }
        }
        System.out.println("");
        System.out.println("----------------------------------------------------------");
        System.out.println("For this test set we guessed correctly "+hit+" out of "+tally+" entries. Our accuracy was "+hit/tally+"%." );
        System.out.println("----------------------------------------------------------");
        System.out.println("");
        return hit/tally;
    }

    public double vdm(int attr, String v1, String v2) { //v for "value," attr is the index of the "attribute" we're finding distences for the two values being compaired
        //value difference metric method, called on by the algorithm
        double distence = 0;
        int v1outCountIndex = -5000; //gives us the proper index of outputCounter[attr][] to reference for the vdm formula. This value is found in the loop below.
        int v2outCountIndex = -5000;
        boolean ready1 = false; //to stop some unnessissary checks.
        boolean ready2 = false;
        System.out.println("Checking "+output[attr].size()+ " output types.");//testline
        System.out.println("Searching for outputs '"+v1+ "' and '"+v2+"'.");
        for (int b = 0; b < output[attr].size(); b++) { //for each kind of output that was seen
            System.out.println("Checking output type "+b+": "+output[attr].get(b)+".");//testline
            if (output[attr].get(b) == v1) {
                System.out.println("Value One match found.");//testline
                v1outCountIndex = b;
                ready1 = true;
            }
            if (output[attr].get(b) == v2) {
                System.out.println("Value Two match found.");//testline
                v2outCountIndex = b;
                ready2 = true;
            }
            if (ready1 == true && ready2 == true) {
                System.out.println("Critical break condition met."); //testline
                break;
            }
        }

        for (int i = 0; i < classes.size(); i++) { //for each classification type
            int val1 = 0; //how many times v1 occured for a given class
            int val2 = 0;
            for (int e = 1; e < train.length; e++) {//Counting through the data entries
                //train[e][attr]
                if (train[e][attr].equals(v1)) { //if that entry's attribute equals one of the values we're compairing. 
                    val1++;
                }
                if (train[e][attr].equals(v2)) { //if that entry's attribute the other value we're compairing. 
                    val2++;
                }
            }
            distence += abs((val1 / outputCounter[attr][v1outCountIndex]) - (val2 / outputCounter[attr][v2outCountIndex]));
            //now we square it for use in the conventional distence formula
            distence = distence * distence;
        }
        return distence;
    }

    public double dist(double[] attdist) { //takes an array of distences between attributes and finds the distence between entries. Short for "Distence" and "attribute distences" respectively.
        //the input's values should already be squared, so we add them all up, then take the square root of that total for the distence between two entries.
        double total = 0;
        for (int i = 0; i < attdist.length; i++) { //for every attribute we've found a distence for
            total += attdist[i];
        }
        total = sqrt(total);
        return total;
    }

}
