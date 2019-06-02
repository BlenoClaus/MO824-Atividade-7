package com.binpacking.model;

public class Bin {
	
	private int capacity;
	private int index = 0;
	
	public Bin(Integer n) {
		this.capacity = n;		
	}
	
	public boolean append(Integer weight) {
		if (index + weight < capacity) {
			index += weight;
			return Boolean.TRUE;
		} 
		return Boolean.FALSE;
	}
	
	public int getIndex() {
		return index;
	}

}
