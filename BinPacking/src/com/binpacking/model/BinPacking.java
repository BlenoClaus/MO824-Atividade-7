package com.binpacking.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinPacking {

	private int numberOfItems;
	private int binCapacity;
	private List<Integer> weightOfItems;
	private List<Integer> weightOfItemsSorted;
	
	public BinPacking(int numberOfItems, int binCapacity, List<Integer> weightOfItems) {
		this.numberOfItems = numberOfItems;
		this.binCapacity = binCapacity;
		this.weightOfItems = weightOfItems;
		this.weightOfItemsSorted = new ArrayList<>(weightOfItems);
		Collections.sort(this.weightOfItemsSorted);
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	public int getBinCapacity() {
		return binCapacity;
	}

	public void setBinCapacity(int binCapacity) {
		this.binCapacity = binCapacity;
	}

	public List<Integer> getWeightOfItems() {
		return weightOfItems;
	}
	
	public List<Integer> getSortedWeightOfItems() {
		return weightOfItemsSorted;
	}

	public void setWeightOfItems(List<Integer> weightOfItems) {
		this.weightOfItems = weightOfItems;
	}

}
