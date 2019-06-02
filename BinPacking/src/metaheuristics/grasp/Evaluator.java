package metaheuristics.grasp;

import java.util.List;

public interface Evaluator<E> {

	public int getNumberOfItems();

	public int getBinCapacity();

	public List<Integer> getWeightOfItems();

	public abstract Double evaluate(Solution<E> sol);

}
