package metaheuristics.grasp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import metaheuristics.grasp.*;


public class BP implements Evaluator<Integer> {

	private int numberOfItems;
	private int binCapacity;
	private List<Integer> weightOfItems;
	public final Double[] usedPacket;

	public BP(String fileName) {
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
			this.numberOfItems = numberOfItems;
			this.binCapacity = binCapacity;
			this.weightOfItems = weightOfItems;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		usedPacket = allocatePackets();
	}

	@Override
	public int getNumberOfItems() {
		return numberOfItems;
	}

	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	@Override
	public int getBinCapacity() {
		return binCapacity;
	}

	public void setBinCapacity(int binCapacity) {
		this.binCapacity = binCapacity;
	}

	@Override
	public List<Integer> getWeightOfItems() {
		return weightOfItems;
	}

	public void setWeightOfItems(List<Integer> weightOfItems) {
		this.weightOfItems = weightOfItems;
	}

	/* Avaliação das soluções */
	protected Double[] allocatePackets() {
		Double[] _packets = new Double[numberOfItems];
		return _packets;
	}

	public void resetPackets() {
		Arrays.fill(usedPacket, 0.0);
	}

	public void setPackets(Solution<Integer> sol) {

		resetPackets();
		if (!sol.isEmpty()) {
			for (Integer elem : sol) {
				usedPacket[elem] = 1.0;
			}
		}

	}

	@Override
	public Double evaluate(Solution<Integer> usedPacket) {

		setPackets(usedPacket);
		return usedPacket.cost = evaluateBP();

	}

	public Double evaluateBP() {

		Double sum = (double) 0;

		for (int i = 0; i < numberOfItems; i++)
			sum += usedPacket[i];

		return sum;

	}

	public static void main(String[] args) throws IOException {

	}

}
