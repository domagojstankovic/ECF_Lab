package hr.fer.zemris.ecf.lab.engine.log;

import java.util.List;

/**
 * Class that represents one log file. It has multiple experiment runs.
 */
public class LogModel {

	private String error;
	private List<ExperimentRun> runs;

	public LogModel(List<ExperimentRun> runs) {
		this.runs = runs;
	}

	public List<ExperimentRun> getRuns() {
		return runs;
	}

	public LogModel(String error) {
		this.error = error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public boolean errorOccured() {
		return error != null;
	}
}
