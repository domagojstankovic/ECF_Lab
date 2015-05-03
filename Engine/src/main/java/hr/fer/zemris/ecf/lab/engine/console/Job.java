package hr.fer.zemris.ecf.lab.engine.console;

import hr.fer.zemris.ecf.lab.engine.task.Task;
import hr.fer.zemris.ecf.lab.engine.task.TaskMannager;

/**
 * This class is the list of parameters that are required for {@link Console} to
 * communicate to ECF. It consists of path to the ECF, path to the parameters
 * that are given to the ECF, path to where the log file will be placed. And
 * this also contains isDone variable that is internally used for {@link Task}
 * and {@link TaskMannager} to tell GUI that this computation is finished.
 * 
 * @version 1.0
 * 
 */
public class Job {

	public String ecfPath;
	public String configPath;
	
	private JobObserver observer = null;

	/**
	 * Main constructor, it gets all that is needed for communication to the
	 * ECF.
	 * 
	 * @param ecfPath
	 *            path to the ECF.
	 * @param configPath
	 *            path to the parameters that are given to the ECF.
	 */
	public Job(String ecfPath, String configPath) {
		this.ecfPath = ecfPath;
		this.configPath = configPath;
	}

	public void setObserver(JobObserver observer) {
		this.observer = observer;
	}

	public void started() {
		if (observer != null) {
			observer.jobStarted(this);
		}
	}

	public void finished(ProcessOutput output) {
		if (observer != null) {
			observer.jobFinished(this, output);
			observer = null;
		}
	}

	public void failed() {
		if (observer != null) {
			observer.jobFailed(this);
			observer = null;
		}
	}

}
