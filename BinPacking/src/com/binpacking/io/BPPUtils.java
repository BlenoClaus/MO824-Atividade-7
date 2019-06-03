package com.binpacking.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.binpacking.heuristic.ga.framework.AbstractGA;
import com.binpacking.model.BinPacking;

public class BPPUtils {
	
	public static int ramdomIndex(Random rng, int n, List<Integer> excludeItems) {
		List<Integer> possibleItem = new ArrayList<>();
		for (int i = 0 ; i< n; i++) possibleItem.add(i);
		if (excludeItems.containsAll(possibleItem))
			return -1;
	    int random = rng.nextInt(n);
	    while(excludeItems.contains(random)) {
	        random = rng.nextInt(n);
	    }
	    return random;
	}

	public static void put(AbstractGA<Integer, Integer>.Chromosome chromosome, BinPacking packing, int newBin,
			int newItem) {
		int n = packing.getNumberOfItems();
		chromosome.set(n * newBin + newItem, 1);
	}

	public static void fill(List<Integer> chromosome, int value, int n) {
		for (int i = 0; i < n; i++) {
			chromosome.add(value);
		}
	}
	

}
