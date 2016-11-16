/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machinelearningexperimentation;

/**
 *
 * @author s53q433
 */
public class dataObject {
    int size; //how many attributes an entry in a dataset has
    String[] dataEntry = new String[size]; //an array meant to hold each attribute of a dataset in it's own index
    String[] dataType = new String[size]; //lables each attribute continuous or discrete, if it's a classficiation, etc. How that data entry needs to be handled.
    
    //as of now, we think to read in each dataset, that can have 600+ entries, with up to 35 different kinds of attributes that need to be handled in different ways, 
    //we're going to need a soybean method, voting method, etc that reads in all the entries (and fills them, if need be) in their own distinct way.
    public dataObject(){
        
    }
}
