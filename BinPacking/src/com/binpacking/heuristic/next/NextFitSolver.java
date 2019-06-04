package com.binpacking.heuristic.next;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.binpacking.io.InstanceReader;
import com.binpacking.model.Bin;
import com.binpacking.model.BinPacking;

public class NextFitSolver {
	
	private List<Integer> items = new ArrayList<>();
	private List<Bin> bins = new ArrayList<>();
	private BinPacking binPacking;
	
	public void solver(BinPacking binPacking, String logName)  {
		this.binPacking = binPacking;
		for (int i = 0; i < binPacking.getNumberOfItems(); i++) {
			addItem(binPacking.getWeightOfItems().get(i), i);
		}
		System.out.println("Used bins = "+bins.size());
    }
	
	private void addItem(int weight, int item) {
		Bin bin = (currentBin() < 0)? newBin() : bins.get(currentBin());					
		boolean added = bin.append(weight);
		if (!added) {
			bin = newBin();
			bin.append(weight);
		}
		items.add(bins.indexOf(bin));
	}
	
	private Bin newBin() {
		Bin bin = new Bin(binPacking.getBinCapacity());
		bins.add(bin);
		return bin;
	}
	
	private int currentBin() {
		return bins.size()-1;
	}
	
	public static void main(String[] args) {
		BinPacking bin = InstanceReader.build("instances/instance0.bpp");
		new NextFitSolver().solver(bin, "next");
	}

	public Collection<Integer> getItems() {
		return items;
	}

}
