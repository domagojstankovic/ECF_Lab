package hr.fer.zemris.ecf.lab.engine.param;

import java.util.ArrayList;
import java.util.List;
/**
 * This class is a container for the initial parameters dumped by the ECF.
 * @version 1.0
 *
 */
public class ParametersList {
	
	/**
	 * Array list of all available algorithms from ECF.
	 */
	public List<EntryBlock> algorithms;
	/**
	 * Array list of all available genotypes form ECF.
	 */
	public List<EntryBlock> genotypes;
	/**
	 * {@link Registry} from ECF.
	 */
	public Registry registry;
	
	/**
	 * Constructor, it initializes algorithms and genotypes list to new array lists. 
	 */
	public ParametersList() {
		algorithms = new ArrayList<>();
		genotypes = new ArrayList<>();
	}

	/**
	 * Constructor it gets references to algorithms and genotypes list and registry
	 * @param algorithms given algorithms list pointer
	 * @param genotypes given genotypes list pointer
	 * @param registry given registry pointer
	 */
	public ParametersList(List<EntryBlock> algorithms, List<EntryBlock> genotypes,
                          Registry registry) {
		this.algorithms = algorithms;
		this.genotypes = genotypes;
		this.registry = registry;
	}
	
	

}
