package hr.fer.zemris.ecf.lab.engine.log;

/**
 * This class is container for all parameters that are in one deme in one {@link Generation} in one {@link LogModel}.
 * All parameters are public and they are to be dealt with manually.
 *
 * @version 1.0
 */
public class Deme {

    /**
     * ID of the deme, it's number.
     */
    public int id;

    /**
     * Evaluations in a deme.
     */
    public int evaluations;

    /**
     * Stats in the deme.
     */
    public Stats stats;


}
