package hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.console;

/**
 * This class is the list of parameters that are required for {@link Console} to
 * communicate to ECF. It consists of path to the ECF, path to the parameters
 * that are given to the ECF, path to where the log file will be placed. And
 * this also contains isDone variable that is internally used for {@link hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.task.Task}
 * and {@link hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.task.TaskMannager} to tell GUI that this computation is finished.
 * 
 * @version 1.0
 * 
 */
public class Job implements Subject {

	public String ecfPath;
	public String paramsPath;
	public String logFilePath;
	public boolean isDone;
	
	private Observer observer = null;

	/**
	 * Main constructor, it gets all that is needed for communication to the
	 * ECF.
	 * 
	 * @param ecfPath
	 *            path to the ECF.
	 * @param logFilePath
	 *            path to where the log file will be placed.
	 * @param paramsPath
	 *            path to the parameters that are given to the ECF.
	 */
	public Job(String ecfPath, String logFilePath, String paramsPath) {
		this.ecfPath = ecfPath;
		this.paramsPath = paramsPath;
		this.logFilePath = logFilePath;
		this.isDone = false;
	}

	/**
	 * Secondary constructor, used only if path's are needed to be add manually.
	 */
	public Job() {
		isDone = false;
	}

	@Override
	public void setObserver(Observer observer) {
		this.observer = observer;
	}

	@Override
	public void removeObserver() {
		observer = null;
	}

	@Override
	public void finished() throws Exception {
		if (observer != null) {
			observer.update(this);
		}
	}

	@Override
	public String getMessage() {
		return logFilePath;
	}

}
