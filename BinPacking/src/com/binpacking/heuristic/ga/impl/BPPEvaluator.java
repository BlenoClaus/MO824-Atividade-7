package com.binpacking.heuristic.ga.impl;

import com.binpacking.heuristic.ga.framework.Evaluator;
import com.binpacking.heuristic.ga.framework.Solution;
import com.binpacking.model.BinPacking;

public class BPPEvaluator implements Evaluator<Integer>{
	
	private BinPacking binPacking;
	
	public BPPEvaluator(BinPacking bin) {
		this.binPacking = bin;
	}

	@Override
	public Integer getDomainSize() {
		return binPacking.getNumberOfItems();
	}

	@Override
	public Double evaluate(Solution<Integer> sol) {
		sol.cost = (double) sol.size();
		return sol.cost ; 
	}

	@Override
	public Double evaluateInsertionCost(Integer elem, Solution<Integer> sol) {
		return (sol.contains(elem))? 0.0 : 1.0;
	}

	@Override
	public Double evaluateRemovalCost(Integer elem, Solution<Integer> sol) {
		return sol.contains(elem)? -1.0 : 0.0;
	}

	@Override
	public Double evaluateExchangeCost(Integer elemIn, Integer elemOut, Solution<Integer> sol) {
		return evaluateInsertionCost(elemIn, sol) + evaluateRemovalCost(elemOut, sol);
	}

}
