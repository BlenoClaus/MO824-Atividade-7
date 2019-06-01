package com.binpacking.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.binpacking.model.BinPacking;

/**
 * @author blenoclaus
 * formater: 
 *	<n -- number of itens>
 *	<bin capacity>
 *	<weight of item 1>
 *	...
 *	<weight of item n>
 */
public class InstanceReader {
	
	public static BinPacking build(String fileName) {
		try {
			int numberOfItems;
			int binCapacity;
			List<Integer> weightOfItems = new ArrayList<>();
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			numberOfItems = Integer.valueOf(br.readLine());
			binCapacity = Integer.valueOf(br.readLine());
			for (int i = 0; i < numberOfItems; i++) {
				weightOfItems.add(Integer.valueOf(br.readLine()));
			}
			br.close();
			return new BinPacking(numberOfItems, binCapacity, weightOfItems);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}

}
