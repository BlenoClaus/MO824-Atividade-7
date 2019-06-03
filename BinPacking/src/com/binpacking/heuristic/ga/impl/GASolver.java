package com.binpacking.heuristic.ga.impl;

import java.awt.List;
import java.util.ArrayList;

import com.binpacking.heuristic.ga.framework.AbstractGA;
import com.binpacking.heuristic.ga.framework.Solution;
import com.binpacking.heuristic.ga.framework.AbstractGA.Chromosome;
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
	protected Solution<Integer> decode(Chromosome chromosome) {
		Solution<Integer> sol = new Solution<>();
		chromosome.stream().forEach(cross -> sol.add(cross));
		ObjFunction.evaluate(sol);
		return sol;
	}

	@Override
	protected Chromosome generateRandomChromosome() {
		Chromosome chromosome = new Chromosome();
		int[] weightPacket;
		int packet;
		for (int i = 0; i < chromosomeSize; i++) {
			weightPacket[i] = 0;
		}

		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int item = 0; item < chromosomeSize; item++) {
			do {
				packet = rng.nextInt(chromosomeSize);
			} while (capacity < weightItems.get(item) + weightPacket[packet]);

			weightPacket[packet] += weightItems.get(item);

			for (int j = 0; j < chromosomeSize; j++) {
				if (j == packet) {
					temp.add(1);
				} else {
					temp.add(0);
				}
			}
			chromosome.add(temp);
		}
		return chromosome;
	}

	@Override
	protected Double fitness(AbstractGA<Integer, Integer>.Chromosome chromosome) {
		return ObjFunction.evaluate(decode(chromosome));
	}

	@Override
	protected void mutateGene(AbstractGA<Integer, Integer>.Chromosome chromosome, Integer locus) {
		chromosome.set(locus, 1 - chromosome.get(locus));
	}

	@Override
	protected AbstractGA<Integer, Integer>.Chromosome getBestChromosome(
			AbstractGA<Integer, Integer>.Population population) {
		return super.getBestChromosome(population);
	}

	@Override
	protected AbstractGA<Integer, Integer>.Chromosome getWorseChromosome(
			AbstractGA<Integer, Integer>.Population population) {
		return super.getWorseChromosome(population);
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		BinPacking bin = InstanceReader.build("instances/instance0.bpp");
		GASolver gaSolver = new GASolver(bin, 1000, 100, 1.0 / 100.0);
		Solution<Integer> bestSol = gaSolver.solve();
		System.out.println("maxVal = " + bestSol);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time = " + (double) totalTime / (double) 1000 + " seg");
	}

}
