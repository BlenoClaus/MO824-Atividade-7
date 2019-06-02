package com.binpacking.heuristic.first;

import java.util.ArrayList;
import java.util.List;

import com.binpacking.io.InstanceReader;
import com.binpacking.model.Bin;
import com.binpacking.model.BinPacking;

public class FirstFitSolver {
	
	private List<Bin> bins = new ArrayList<>();
	private BinPacking binPacking;
	
	public void solver(BinPacking binPacking, String logName)  {
		this.binPacking = binPacking;
		for (int i = 0; i < binPacking.getNumberOfItems(); i++) {
			addItem(binPacking.getWeightOfItems().get(i));
		}
		System.out.println("Used bins = "+bins.size());
	}

	private void addItem(Integer weight) {
		if (bins.size() > 0 ) {
			for (int i = 0; i < bins.size(); i++) {
				if (bins.get(i).append(weight)) {
					return;
				}
			}
		} 
		Bin newBin = newBin();
		newBin.append(weight);
		bins.add(newBin);
	}
	
	private Bin newBin() {
		return new Bin(binPacking.getBinCapacity());
	}
	
	public static void main(String[] args) {
		BinPacking bin = InstanceReader.build("instances/instance0.bpp");
		new FirstFitSolver().solver(bin, "first");
	}
}
