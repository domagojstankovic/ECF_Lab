package hr.fer.zemris.ecf.lab.engine.log;

/**
 * This class  is container for all parameters that are in one population in one {@link Generation} in one {@link LogModel}.
 * All parameters are public and they are to be dealt with manually, although there are constructors to help.
 * @version 1.0
 *
 */
public class Population {
	
	/**
	 * Number of evaluations in the population.
	 */
	public int evaluations;

	/**
	 * Stats in the population.
	 */
	public Stats stats;

}
