package machinelearningexperimentation;

import java.util.ArrayList;

/**
 *
 * @author Greg Neznanski
 */
public class ID3 extends classAlg {
	Tree decisionTree;
	
    @Override
    public int[] algorithm(int[][] train, int[][] test) {
    	int start;
    	if(Integer.toString(train[0][0]).length() >= 6){
    		start = 1;
    	}else{
    		start = 0;
    	}
    	decisionTree = new Tree();
    	decisionTree = trainID3(train, start); //Set up the decision tree from the training set
    	
        int[] classes = new int[test.length];
        if(Integer.toString(train[0][0]).length() >= 6){
    		int width = test[0].length-1;
    		System.out.println(width);
        	for(int i = 0; i < test.length; i++){
    			int[] removeID = new int[width];
    			int count = 1;
    			for(int j = 0; j < width; j++){
    				removeID[j] = test[i][count];
    				count++;
    			}
    			test[i] = removeID;
    		}
    	}
        
        classes = testID3(test, decisionTree, classes);
        return classes;
    }
    
    private Tree trainID3(int[][] trainData, int start){
    	ArrayList<ArrayList<Integer>> dataset = new ArrayList<ArrayList<Integer>>(); //Holds data in arraylists
    	
    	//Count Data
    	//Seperate the classifications and determine class types
    	ArrayList<Integer> classList = new ArrayList<Integer>(); //Holds the classification values seperate from the rest of the data
    	ArrayList<Integer> classType = new ArrayList<Integer>(); //Holds the different types of classifications
    	for(int i = 0; i < trainData.length; i++){
    		classList.add(trainData[i][trainData[0].length-1]);
    		if(!classType.contains(trainData[i][trainData[0].length-1])){
    			classType.add(trainData[i][trainData[0].length-1]);
    		}
    	}
        
    	//Add List to hold the int[] for each attribute list
    	ArrayList<ArrayList<int[]>> counts = new ArrayList<ArrayList<int[]>>(); //List to hold unique entries and count of each entry for each attribute
    	for(int i = start; i < trainData[0].length; i++){
    		counts.add(new ArrayList<int[]>()); //Add arraylist to count for each attribute
    	}
    	for(int i = 0; i < trainData.length; i++){
    		dataset.add(new ArrayList<Integer>()); //Add arraylist to hold colums of values from trainData
    	}
    	
    	//Count the number of classes for each entry type
    	for(int i = 0; i < trainData.length; i++){
    		int count = 0;
    		for(int j = start; j < trainData[0].length-1; j++){
    			dataset.get(i).add(trainData[i][j]); //Convert int[][] to arraylist for easier removing of subsets
    			int[] checkValue = arrayContain(counts, trainData[i][j], count); //Check the list of arrays to see if value has been seen before
    			if(checkValue[0] == 0){
    				int[] newEntry = new int[3]; //Create a new entry to the list of entries
    				newEntry[0] = trainData[i][j];
    				newEntry[1] = 0;
    				newEntry[2] = 0;
					if(classList.get(count) == classType.get(0)){ //Add to class count for that entry
						newEntry[1]++; //Class 1
					}else{
						newEntry[2]++; //Class 2
					}
    				counts.get(count).add(newEntry); //Add new entry to attribute list
    			}else{
    				if(counts.get(count).get(checkValue[1])[0] == classType.get(0)){ //Add to class count for that entry
    					counts.get(count).get(checkValue[1])[1]++; //Class 1
    				}else{
    					counts.get(count).get(checkValue[1])[2]++; //Class 2
    				}
    			}
    			count++;
    		}
    	}
    	
    	decisionTree.add(new Node(null));
        decisionTree = split(counts, decisionTree, classList, dataset, decisionTree.root); //Create the decision tree by splitting on attributes until all data is classified
    	
    	//Print tree?
    	
    	return decisionTree;
    }
    
    private int[] testID3(int[][] testData, Tree tree, int[] classes){ //include full data
    	
    	for(int i = 0; i < testData.length; i++){
    		classes[i] = tree.classify(testData[i]);
    	}
    	
    	return classes;
    }
    
    //Recursive function for determining which attribute to split on
    private Tree split(ArrayList<ArrayList<int[]>> counts, Tree tree, ArrayList<Integer> classList, ArrayList<ArrayList<Integer>> dataset, Node current){
    	if(counts.isEmpty()){
    		return tree;
    	}else if(tree.current.leaf){
    		return tree;
    	}else if (tree.current == null){////////////////////////////////////////////////
    		return tree;
    	}else{
    		double[] startEntropy = new double[counts.size()]; //Holds starting entropy value of attributes
    		double[] expectEntropy = new double[counts.size()]; //Holds expected entropy value of attributes
	    	double highestGain = 0;
	    	int index = 0;
	    	
	    	for(int i = 0; i < counts.size(); i++){ //Calculate the starting entropy for each attribute
	    		int[] type = new int[]{0,0,0};
	    		for(int j = 0; j < counts.get(i).size(); j++){
	    			type[1] += counts.get(i).get(j)[1];
	    			type[2] += counts.get(i).get(j)[2];
	    		}
	    		startEntropy[i] = entropy(type);
	    	}
	    	
	    	for(int i = 0; i < counts.size(); i++){ //Calculate the expected entropy for each attribute
	    		for(int j = 0; j < counts.get(i).size(); j++){
	    			int numTerms = counts.get(i).get(j)[1] + counts.get(i).get(j)[2];
	    			double ratio = (double) numTerms/dataset.size();
	    			expectEntropy[i] += entropy(counts.get(i).get(j)) * ratio;
	    		}
	    	}
	    	
	    	//Something about if entropy = 0, return.  Here?
	    	
	    	for(int i = 0; i < startEntropy.length; i++){ //Calculate the Info Gain for each attribute
	    		double currentGain = startEntropy[i] - expectEntropy[i];
	    		if(currentGain > highestGain){
	    			highestGain = currentGain;
	    			index = i;
	    		}
	    	}
	    	
    		//Seperate data into subsets
    		ArrayList<ArrayList<ArrayList<Integer>>> subList = new ArrayList<ArrayList<ArrayList<Integer>>>();
    		ArrayList<ArrayList<Integer>> subClassList = new ArrayList<ArrayList<Integer>>();
    		
    		for(int i = 0; i < counts.size()-1; i++){ //For each count entry
    			ArrayList<ArrayList<Integer>> subset = new ArrayList<ArrayList<Integer>>();
    			ArrayList<Integer> subClassValues = new ArrayList<Integer>();
    			
    			for(int y = 0; y < dataset.size(); y++){ //For each row
    				//for(int x = 0; x < dataset.get(index).size(); x++){ //For each column
    				if(index < counts.get(i).size()){
    					if(dataset.get(y).get(index) == counts.get(i).get(index)[0]){ //If the data entry matches the count entry
    						if(i != index){ //Ignore the selected attribute column
    							subset.add(dataset.get(y)); //Add the values from dataset to the new subset
    							subClassValues.add(classList.get(y));
    						}
    					}
    				}
    				//}
    			}
    			if(i != index){
    				subClassList.add(subClassValues); //Add the new subset of class values to a class values list
    				subList.add(subset); //Add new subset to child option list
    			}
    		}
    		
    		ArrayList<int[]> temp = counts.get(index);
    		counts.remove(index); //Remove attribute from the list
    		
    		
	    	
//    		for(int i = 0; i < subList.size(); i++){
//    			ArrayList<ArrayList<int[]>> newCounts = new ArrayList<ArrayList<int[]>>();
//    			for(int j = 0; j < dataset.get(0).size(); j++){
//    				newCounts.add(new ArrayList<int[]>());
//    			}
//    			counts = updateCounts(subList.get(i), newCounts, subClassList.get(i));
//    		}
    		
    		tree.current.setAttr(temp, index); //Update values at the current node after split
    		
	    	//Recurse to split again
    		if(tree.current.children != null){
		    	for(int i = 0; i < tree.current.children.size(); i++){
		    		if(tree.current.branches[i][1] == 0){
		    			tree.current.setLeaf(i, 1);
		    		}else if(tree.current.branches[i][2] == 0){
		    			tree.current.setLeaf(i, 2);
		    		}else{
		    			tree.current = tree.current.children.get(i);
		    			tree = split(counts, tree, classList, subList.get(i), tree.current);
		    			tree.current = tree.current.parent;
		    		}
		    	}
    		}
    	}
    	//tree.current = tree.current.parent;
    	return tree;
    }
    
    //Calculate the entropy
    private double entropy(int[] entryTotals){
    	double entropy = 0;
    	int numValues = entryTotals[1] + entryTotals[2];
    	double prob1 = (double) entryTotals[1] / numValues;
    	double prob2 = (double) entryTotals[2] / numValues;
    	
    	entropy += Math.abs((prob1*(Math.log(prob1)/Math.log(2)))); //-P1*log2(P1)
    	entropy += Math.abs((prob2*(Math.log(prob2)/Math.log(2)))); //-P2*log2(P2)

    	return entropy;
    }
    
    private ArrayList<ArrayList<int[]>> updateCounts(ArrayList<ArrayList<Integer>> dataset, ArrayList<ArrayList<int[]>> counts, ArrayList<Integer> classList){
    	//Seperate the classifications and determine class types
    	ArrayList<Integer> classType = new ArrayList<Integer>(); //Holds the different types of classifications
    	for(int i = 0; i < dataset.size(); i++){ 
    		if(!classType.contains(classList.get(i))){
    			classType.add(classList.get(i));
    		}
    	}
    	
    	//Count the number of classes for each entry type
    	for(int i = 0; i < dataset.size(); i++){
    		for(int j = 0; j < dataset.get(0).size(); j++){
    			//dataset.get(i).add(dataset.get(i).get(j)); //Convert int[][] to arraylist for easier removing of subsets
    			int[] checkValue = arrayContain(counts, dataset.get(i).get(j), j); //Check the list of arrays to see if value has been seen before
    			if(checkValue[0] == 0){
    				int[] newEntry = new int[3]; //Create a new entry to the list of entries
    				newEntry[0] = dataset.get(i).get(j);
    				newEntry[1] = 0;
    				newEntry[2] = 0;
					if(classList.get(j) == classType.get(0)){
						newEntry[1]++; //Class 1
					}else{
						newEntry[2]++; //Class 2
					}
    				counts.get(j).add(newEntry); //Add new entry to attribute list
    			}else{
    				if(counts.get(j).get(checkValue[1])[0] == classType.get(0)){ //Add to class count for that entry
    					counts.get(j).get(checkValue[1])[1]++; //Class 1
    				}else{
    					counts.get(j).get(checkValue[1])[2]++; //Class 2
    				}
    			}
    		}
    	}
    	return counts;
    }
    
    public int[] arrayContain(ArrayList<ArrayList<int[]>> counts, int value, int in){
    	int[] found = new int[]{0,0};
    	for(int i = 0; i < counts.get(in).size(); i++){
    		if(counts.get(in).get(i)[0] == value){
    			found[0] = 1;
    			found[1] = i;
    		}
    	}
    	return found;
    }
    
    @Override
    public String getName() {
        return "ID3";
    }

    //Class for holding node information for the tree
    private class Node{
    	private int[][] branches;
    	private int value;
    	private Node parent;
    	private ArrayList<Node> children;
    	private boolean leaf;
    	
    	//Create a basic node
    	public Node(Node parent){
    		this.branches = null;
    		this.value = -1;
    		this.parent = parent;
    		this.children = null;
    		this.leaf = false;
    	}
    	
    	//Update the node after a split has been made
    	public void setAttr(ArrayList<int[]> attrInfo, int attrIndex){
    		this.value = attrIndex; //The attribute that this node represents
    		this.branches = new int[attrInfo.size()][3];
    		this.children = new ArrayList<Node>();
    		for(int i = 0; i < attrInfo.size(); i++){
    			this.branches[i] = attrInfo.get(i); //Hold info on branches of the node
    			this.children.add(new Node(this)); //Create children for the entry options
    		}
    	}
    	
    	//Set the node to a leaf by setting its classification
    	public void setLeaf(int i, int classIndex){
    		this.value = branches[i][classIndex];
    		this.leaf = true;
    	}    	
    }
    
    //Class for holding the decision tree
    private class Tree{
    	private Node root, current;
    	private int size;
    	
    	public Tree(){
    		this.size = 0;
    	}
    	
    	public void add(Node newNode){
    		if(this.size == 0){ //Handle tree with no nodes
    			this.current = newNode;
    			this.root = newNode;
    			newNode.parent = null;
    		}
    		
    		//Increase size of tree
    		this.size++;
    	}
    	
    	//Find class?
    	public int classify(int[] testCase){
    		this.current = this.root;
    		
    		//Traverse tree to make a decision
    		while(this.current.children != null){
    			for(int i = 0; i < current.children.size(); i++){
    				if(testCase[this.current.value] == current.branches[i][0]){
						this.current = current.children.get(i);
					}
    			}
    		}
    		return this.current.value; //Return the classification
    	}
    }
}
