package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;

/**
 * Created by Domagoj on 03/05/15.
 */
public interface JobListener {

  void jobInitialized(Job job);

  void jobStarted(Job job);

  void jobPartiallyFinished(Job job, LogModel log);

  void jobFinished(Job job, LogModel log);

  void jobFailed(Job job);

}
