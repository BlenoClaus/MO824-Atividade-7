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
	
	public boolean fit(Integer weight) {
		if (index + weight < capacity) {			
			return Boolean.TRUE;
		} 
		return Boolean.FALSE;
	}
	
	public int residue(Integer weight) {
		return capacity - (index+weight) ;
	}
	
	public int getIndex() {
		return index;
	}

}
