package _2xb3.finalProject.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * An individual data point. (Nested Class)
 */
public class SeriesData {
	// name of the data i.e. McMaster University
	public String name;
	// list of integers in the data i.e. [20,1000,12,4000,500]
	public List<Integer> data;
	
	/**
	 * Standard constructor which creates a data set with a given name.
	 * @param name name of dataset.
	 */
	public SeriesData(String name) {
		this.name = name;
		this.data = new ArrayList<Integer>();
	}
	
	/**
	 * Returns the sum of all the values
	 */
	public int sumOfData() {
		int x = 0;
		
		for(Integer i : data)
			x += i;
		
		return x;
	}

}
