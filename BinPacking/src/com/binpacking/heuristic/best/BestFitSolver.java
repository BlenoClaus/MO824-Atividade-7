package com.binpacking.heuristic.best;

import java.util.ArrayList;
import java.util.List;

import com.binpacking.io.InstanceReader;
import com.binpacking.model.Bin;
import com.binpacking.model.BinPacking;

public class BestFitSolver {

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
			Integer min = Integer.MAX_VALUE;
			Integer minIndex = -1;
			for (int i = 0; i < bins.size(); i++) {
				int residue = bins.get(i).residue(weight);
				if (bins.get(i).residue(weight) > 0) {
					if (residue < min) {
						min = residue;
						minIndex = i;
					}
				}
			}
			
			if (minIndex >= 0) {
				bins.get(minIndex).append(weight);
				return;
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
		new BestFitSolver().solver(bin, "best");
	}
	
}
