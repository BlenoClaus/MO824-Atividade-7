package com.binpacking.heuristic.ga.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.binpacking.heuristic.ga.framework.AbstractGA;
import com.binpacking.heuristic.ga.framework.Solution;
import com.binpacking.heuristic.next.NextFitSolver;
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
	protected Population initializePopulation() {

		Population population = new Population();

		while (population.size() < popSize) {
			population.add(generateRandomChromosome());
		}
		
		NextFitSolver nextFitSolver = new NextFitSolver();
		nextFitSolver.solver(binPacking, "GA");
		AbstractGA<Integer, Integer>.Chromosome chromosome = new Chromosome();
		nextFitSolver.getItems().stream().forEach(item -> chromosome.add(item));
		population.add(chromosome);
		return population;

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

	@Override
	protected Population selectPopulation(Population offsprings) {
		ArrayList<AbstractGA<Integer, Integer>.Chromosome> pop = new ArrayList<>(offsprings);
		Collections.sort(pop, new Comparator<Chromosome>() {
			@Override
			public int compare(AbstractGA<Integer, Integer>.Chromosome o1, AbstractGA<Integer, Integer>.Chromosome o2) {
				Double f1 = fitness(o1);
				Double f2 = fitness(o2);
				if (f1 > f2) return -1;
				if (f1 < f2) return 1;
				return 0;
			}
		});
		
		int size = (int) ((int) pop.size() * 0.05);
		
		List<AbstractGA<Integer, Integer>.Chromosome> subListWorst = pop.subList(0, size);
		List<AbstractGA<Integer, Integer>.Chromosome> subListBest = pop.subList(pop.size()-size, pop.size());
		Double f1 = fitness(subListWorst.get(0));
		Double f2 = fitness(subListBest.get(subListBest.size()-1));
		if (f1 > f2) {
			offsprings.removeAll(subListBest);
			offsprings.addAll(subListBest);
		}
		
		return offsprings;
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
		BinPacking bin = InstanceReader.build("instances/instance1.bpp");
		GASolver gaSolver = new GASolver(bin, 1000000, 100, 1.0 / 100.0);
		Solution<Integer> bestSol = gaSolver.solve();
		System.out.println("maxVal = " + bestSol);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time = " + (double) totalTime / (double) 1000 + " seg");
	}
	
}

