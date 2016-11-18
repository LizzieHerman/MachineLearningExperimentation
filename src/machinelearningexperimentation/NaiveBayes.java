package src.machinelearningexperimentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author Connor O'Leary
 */
public class NaiveBayes extends classAlg {

	@Override
	public int[] algorithm(int[][] train, int[][] test) {
		// training
		HashMap<Integer, Integer> outcomes = new HashMap();

		for (int[] arr : train) {
			if (!outcomes.containsKey(arr[train[0].length - 1])) {
				// System.out.println(arr[i]);
				outcomes.put(arr[train[0].length - 1], 1);
			}
			// increment the number of times that specific class occurs
			else {
				outcomes.put(arr[train[0].length - 1],
						outcomes.get(arr[train[0].length - 1]) + 1);
			}
		}

		// first layer: each possible class outcome
		// second layer: each dimension
		// third layer: each class in each outcome
		ArrayList<ArrayList<HashMap<Integer, Integer>>> classChoices = new ArrayList<ArrayList<HashMap<Integer, Integer>>>(
				outcomes.size());

		// for each possible outcome, initialize the ArrayLists for the dim
		for (int j = 0; j < outcomes.size(); j++) {
			classChoices.add(new ArrayList());
			// add each dimension to an array
			for (int i = 0; i < train[0].length; i++) {
				classChoices.get(j).add(new HashMap<Integer, Integer>());
			}
		}
		
		int i = -1;
		// create the probability table
		for (Integer key : outcomes.keySet()) {
			i++;
			// add each possible class and their frequency to each dim
			for (int[] arr : train) {
				if (key == arr[arr.length - 1])
					for (int j = 0; j < arr.length - 1; j++) {
						if (!classChoices.get(i).get(j).containsKey(arr[j])) {
							classChoices.get(i).get(j).put(arr[j], 1);
						}
						// increment the number of times that specific class occurs
						else {
							classChoices.get(i).get(j)
									.put(arr[j],classChoices.get(i).get(j).get(arr[j]) + 1);
						}
					}
			}
		}
		double probability;
		double num = 0.0;
		String s;

		Iterator it = outcomes.entrySet().iterator();
		for (ArrayList<HashMap<Integer, Integer>> dim : classChoices) {
			Map.Entry temp = (Map.Entry) it.next();
			System.out.println("Probability Table for output class: "
					+ temp.getKey());
			for (HashMap<Integer, Integer> count : dim) {

				for (Integer key : count.keySet()) {
					num += count.get(key); // get the total number of the class
											// given an outcome
				}
				for (Integer key : count.keySet()) {
					probability = (double) count.get(key) / num;
					s = "p(" + key + "|" + temp.getKey() + "):";
					System.out.format("%10s%8f", s, probability);
				}

				num = 0.0;
				System.out.println();
			}
			System.out.println();
		}

		// testing
		double maxProb = 0;
		double probC = 0;
		int C = 0;
		int j = 0;
		ArrayList<HashMap<Integer, Integer>> a;
		HashMap<Integer, Integer> b;
		int c = 0;
		int iterOutcomes;
		int[] classes = new int[test.length];
		for (i = 0; i < test.length; i++) { //for each data point
			iterOutcomes = -1;
			for (Integer loopC : outcomes.keySet()) {
				iterOutcomes++;
				C = loopC;
				probC = (double) outcomes.get(C) / train.length; // P(C)
				j = 0;
				for (int dim : test[i]) {
					num=0;
					for (Integer key : classChoices.get(iterOutcomes).get(j).keySet()) {
						num += classChoices.get(iterOutcomes).get(j).get(key); // get the total number of the class given an outcome
					}
					if (classChoices.get(iterOutcomes).get(j).containsKey(dim))
						probC *= (double)(classChoices.get(iterOutcomes).get(j).get(dim))/num;
					else {
						probC *= .1/num; //very unlikely if there is none is this class but not impossible
						break;
					}
					j++;

				}
			}
			if (probC > maxProb) { // select the maximum probability
				maxProb = probC;
				classes[i] = C;
			}
		}
		return classes;
	}

	@Override
	public String getName() {
		return "Naive Bayes";
	}

}
