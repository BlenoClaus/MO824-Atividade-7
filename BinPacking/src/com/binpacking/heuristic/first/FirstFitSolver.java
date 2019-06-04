package com.binpacking.heuristic.first;

import java.util.ArrayList;
import java.util.List;

import com.binpacking.io.InstanceReader;
import com.binpacking.model.Bin;
import com.binpacking.model.BinPacking;

public class FirstFitSolver {
	
	private List<Bin> bins = new ArrayList<>();
	private List<Integer> items = new ArrayList<>();
	private BinPacking binPacking;	
	
	public void solver(BinPacking binPacking, String logName)  {		
		this.binPacking = binPacking;
		for (int i = 0; i < binPacking.getNumberOfItems(); i++) {
			addItem(binPacking.getWeightOfItems().get(i), i);
		}
		System.out.println("Used bins = "+bins.size());
	}

	private void addItem(Integer weight, Integer item) {
		if (bins.size() > 0 ) {
			for (int i = 0; i < bins.size(); i++) {
				if (bins.get(i).append(weight)) {
					getItems().add(i);
					return;
				}
			}
		} 
		Bin newBin = newBin();
		newBin.append(weight);
		bins.add(newBin);
		getItems().add(bins.size());
	}
	
	private Bin newBin() {
		return new Bin(binPacking.getBinCapacity());
	}
	
	public static void main(String[] args) {
		BinPacking bin = InstanceReader.build("instances/instance6.bpp");
		new FirstFitSolver().solver(bin, "first");
	}

	public List<Integer> getItems() {
		return items;
	}

	public void setItems(List<Integer> items) {
		this.items = items;
	}
}
