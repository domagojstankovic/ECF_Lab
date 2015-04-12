package hr.fer.zemris.ecf.lab.view.layout;

import hr.fer.zemris.ecf.lab.engine.param.Algorithm;

import java.util.List;

/**
 * Panel for algorithm selection.
 * @author Domagoj Stanković
 * @version 1.0
 */
public class AlgorithmSelection extends DropDownPanel<Algorithm> {

	private static final long serialVersionUID = 1L;

	public AlgorithmSelection(List<Algorithm> list) {
		super(list);
	}

}
