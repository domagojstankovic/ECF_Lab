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
 */
public class Job {

  private String ecfPath;
  private String configPath;
  private boolean shouldDeleteConf;
  private Integer runId;

  private boolean finished;

  private JobObserver observer = null;

  /**
   * Main constructor, it gets all that is needed for communication to the
   * ECF.
   *
   * @param ecfPath    path to the ECF.
   * @param configPath path to the parameters that are given to the ECF.
   */
  public Job(String ecfPath, String configPath) {
    this(ecfPath, configPath, false);
  }

  public Job(String ecfPath, String configPath, boolean shouldDeleteConf) {
    this(ecfPath, configPath, shouldDeleteConf, null);
  }

  public Job(String ecfPath, String configPath, boolean shouldDeleteConf, Integer runId) {
    this.ecfPath = ecfPath;
    this.configPath = configPath;
    this.shouldDeleteConf = shouldDeleteConf;
    this.runId = runId;
  }

  public String getConfigPath() {
    return configPath;
  }

  public String getEcfPath() {
    return ecfPath;
  }

  public boolean shouldDeleteConf() {
    return shouldDeleteConf;
  }

  public void setObserver(JobObserver observer) {
    this.observer = observer;
  }

  public void started() {
    finished = false;
    if (observer != null) {
      observer.jobStarted(this);
    }
  }

  public void finished(ProcessOutput output) {
    finished = true;
    if (observer != null) {
      observer.jobFinished(this, output);
      observer = null;
    }
  }

  public void failed() {
    finished = true;
    if (observer != null) {
      observer.jobFailed(this);
      observer = null;
    }
  }

  public Integer getRunId() {
    return runId;
  }

  public boolean isFinished() {
    return finished;
  }
}
