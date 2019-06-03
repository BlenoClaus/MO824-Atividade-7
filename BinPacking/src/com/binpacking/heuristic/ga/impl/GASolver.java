package com.binpacking.heuristic.ga.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.binpacking.heuristic.ga.framework.AbstractGA;
import com.binpacking.heuristic.ga.framework.Solution;
import com.binpacking.io.BPPUtils;
import com.binpacking.io.InstanceReader;
import com.binpacking.model.BinPacking;


public class GASolver extends AbstractGA<Integer, Integer> {
		
	private BinPacking binPacking;

	public GASolver(BinPacking bin, Integer generations, Integer popSize, Double mutationRate) {
		super(new BPPEvaluator(bin), generations, popSize, mutationRate);
		this.binPacking = bin;
	}

	@Override
	public Solution<Integer> createEmptySol() {
		return new Solution<Integer>();
	}
	@Override
	protected AbstractGA<Integer, Integer>.Population crossover(AbstractGA<Integer, Integer>.Population parents) {
		Population offsprings = new Population();

		for (int i = 0; i < popSize; i = i + 2) {

			Chromosome parent1 = parents.get(i);
			Chromosome parent2 = parents.get(i + 1);

			int crosspoint = rng.nextInt(chromosomeSize + 1);
			List<Integer> binsCapacity = new ArrayList<>();
			BPPUtils.fill(binsCapacity, 0, chromosomeSize);

			Chromosome offspring1 = new Chromosome();
			Chromosome offspring2 = new Chromosome();

			for (int j = 0; j < chromosomeSize; j++) {
				if (j >= crosspoint) {														
					offspring1.add(getValidBin(binsCapacity, parent2, j));						
					offspring2.add(getValidBin(binsCapacity, parent1, j));
				} else {
					offspring1.add(getValidBin(binsCapacity, parent1, j));						
					offspring2.add(getValidBin(binsCapacity, parent2, j));
				}
			}

			offsprings.add(offspring1);
			offsprings.add(offspring2);

		}

		return offsprings;
		
	}
	

	private Integer getValidBin(List<Integer> binsCapacity, Chromosome parent, int item) {
		int newBin = parent.get(item);
		int value = binPacking.getWeightOfItems().get(item);
		if (binsCapacity.get(newBin) + value > binPacking.getBinCapacity()) {
			newBin = findFirst(binsCapacity, value);						
		}
		binsCapacity.set(newBin, binsCapacity.get(newBin)+value);
		return newBin;
	}

	@Override
	protected Solution<Integer> decode(AbstractGA<Integer, Integer>.Chromosome chromosome) {
		Solution<Integer> sol = new Solution<>();
		Set<Integer> bins = new HashSet<>(chromosome); 
		sol.addAll(bins);	
		ObjFunction.evaluate(sol);
		return sol;
	}
	
	private boolean isValid(AbstractGA<Integer, Integer>.Chromosome chromosome) {
		if (chromosome == null) return false;
		List<Integer> binsCapacity = computeCapacity(chromosome);
		for (int i = 0; i < chromosome.size(); i++) {
			int binIndex = chromosome.get(i);
			if (binsCapacity.get(binIndex) > binPacking.getBinCapacity()) {
				return false;
			}
		}
		return true;
	}
	
	
	private List<Integer> computeCapacity(AbstractGA<Integer, Integer>.Chromosome chromosome) {
		List<Integer> binsCapacity = new ArrayList<>();
		BPPUtils.fill(binsCapacity, 0, binPacking.getNumberOfItems());
		for (int i = 0; i < chromosome.size(); i++) {
			int bin = chromosome.get(i);
			int binValue = binsCapacity.get(bin);
			int binNewValue = binValue + binPacking.getWeightOfItems().get(i);
			binsCapacity.set(bin, binNewValue);
		}
		return binsCapacity;
	}

	@Override
	protected AbstractGA<Integer, Integer>.Chromosome generateRandomChromosome() {
		  AbstractGA<Integer, Integer>.Chromosome chromosome = new Chromosome();
		List<Integer> binsCapacity = new ArrayList<>();
		BPPUtils.fill(binsCapacity, 0, binPacking.getNumberOfItems());
        for (int j = 0; j < binPacking.getNumberOfItems(); j++) {
        	int binIndex = rng.nextInt(binPacking.getNumberOfItems());        	
        	if (binsCapacity.get(binIndex)+binPacking.getWeightOfItems().get(j) > binPacking.getBinCapacity()) {
        		binIndex = findFirst(binsCapacity, binPacking.getWeightOfItems().get(j));
        	}
        	binsCapacity.set(binIndex, binsCapacity.get(binIndex)+binPacking.getWeightOfItems().get(j));
        	chromosome.add(binIndex);
        }
        return chromosome;
	}
	
	
	private int findFirst(List<Integer> binsCapacity, Integer value) {
		for (int i = 0; i < binsCapacity.size(); i++) {
			if (binsCapacity.get(i) + value < binPacking.getBinCapacity())
				return i;
		}
		return -1;
	}

	@Override
	protected Double fitness(AbstractGA<Integer, Integer>.Chromosome chromosome) {	
		return ObjFunction.evaluate(decode(chromosome));
	}

	@Override
	protected void mutateGene(AbstractGA<Integer, Integer>.Chromosome chromosome, Integer locus) {
		List<Integer> capacity = computeCapacity(chromosome);
		int first = findFirst(capacity, binPacking.getWeightOfItems().get(locus));
		chromosome.set(locus, first);
	}
	
	@Override
	protected AbstractGA<Integer, Integer>.Chromosome getBestChromosome(
			AbstractGA<Integer, Integer>.Population population) {
		return super.getWorseChromosome(population);
	}
	
	@Override
	protected AbstractGA<Integer, Integer>.Chromosome getWorseChromosome(
			AbstractGA<Integer, Integer>.Population population) {
		return super.getBestChromosome(population);
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		BinPacking bin = InstanceReader.build("instances/instance0.bpp");
		GASolver gaSolver = new GASolver(bin, 100000, 100, 1.0 / 100.0);
		Solution<Integer> bestSol = gaSolver.solve();
		System.out.println("maxVal = " + bestSol);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time = " + (double) totalTime / (double) 1000 + " seg");
	}
	
}

