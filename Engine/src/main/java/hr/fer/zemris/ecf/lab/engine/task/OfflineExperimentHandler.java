package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.console.JobObserver;
import hr.fer.zemris.ecf.lab.engine.console.ProcessOutput;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.engine.log.reader.LogReaderProvider;

import java.io.InputStream;

/**
 * Created by dstankovic on 4/5/16.
 */
public class OfflineExperimentHandler implements JobObserver {
  protected JobListener listener;
  private StatsHandler statsHandler;

  public OfflineExperimentHandler(JobListener listener) {
    this.listener = listener;
  }

  public OfflineExperimentHandler(JobListener listener, StatsHandler statsHandler) {
    this(listener);
    this.statsHandler = statsHandler;
  }

  @Override
  public void jobStarted(Job job) {
    listener.jobStarted(job);
  }

  @Override
  public void jobFinished(Job job, ProcessOutput output) {
    ExperimentHandlerUtils.deleteConfIfNeeded(job);
    InputStream is = output.getStdout();
    LogModel log = LogReaderProvider.getReader().read(is);
    listener.jobFinished(job, log);

    if (statsHandler != null) {
      statsHandler.finished();
    }
  }

  @Override
  public void jobFailed(Job job) {
    ExperimentHandlerUtils.deleteConfIfNeeded(job);
    listener.jobFailed(job);
  }
}
