package hr.fer.zemris.ecf.lab.view.layout;

import hr.fer.zemris.ecf.lab.engine.param.EntryBlock;

import java.util.List;

/**
 * Panel for genotype selection.
 * @author Domagoj Stanković
 * @version 1.0
 */
public class GenotypeSelection extends DropDownPanel<EntryBlock> {

	private static final long serialVersionUID = 1L;
	
	public GenotypeSelection(List<EntryBlock> blocks) {
		super(blocks);
	}

}
