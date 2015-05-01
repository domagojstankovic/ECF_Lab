package hr.fer.zemris.ecf.lab.engine.log;

import java.util.List;

/**
 * This class is for use in off-line reading <b>only</b>.
 * For online reading use ArrayList of {@link Generation}.
 */
public class LogModel {

	private String error;
	private List<Generation> generations;
	private String hallOfFame;
	
	/**
	 * Constructor, it initializes generations and hallOfFame to given array lists.
	 * @param generations generations list
	 */
	public LogModel(List<Generation> generations){
		this.generations = generations;
	}

	public LogModel(String error) {
		this.error = error;
	}

	public List<Generation> getGenerations() {
		return generations;
	}

	public String getHallOfFame() {
		return hallOfFame;
	}

	public void setHallOfFame(String hallOfFame) {
		this.hallOfFame = hallOfFame;
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
