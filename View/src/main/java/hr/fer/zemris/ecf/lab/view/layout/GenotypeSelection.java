package hr.fer.zemris.ecf.lab.view.layout;

import hr.fer.zemris.ecf.lab.engine.param.Genotype;

import java.util.List;

/**
 * Panel for genotype selection.
 * @author Domagoj Stanković
 * @version 1.0
 */
public class GenotypeSelection extends DropDownPanel<Genotype> {

	private static final long serialVersionUID = 1L;
	
	public GenotypeSelection(List<Genotype> blocks) {
		super(blocks);
	}

}
