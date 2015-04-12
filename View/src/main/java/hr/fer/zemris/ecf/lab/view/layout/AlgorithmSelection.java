package hr.fer.zemris.ecf.lab.view.layout;

import hr.fer.zemris.ecf.lab.engine.param.EntryBlock;

import java.util.List;

/**
 * Panel for algorithms selection.
 * @author Domagoj Stanković
 * @version 1.0
 */
public class AlgorithmSelection extends DropDownPanel<EntryBlock> {

	private static final long serialVersionUID = 1L;

	public AlgorithmSelection(List<EntryBlock> list) {
		super(list);
	}

}
