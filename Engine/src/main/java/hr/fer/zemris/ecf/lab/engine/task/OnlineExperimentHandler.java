package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.console.JobObserver;
import hr.fer.zemris.ecf.lab.engine.console.ProcessOutput;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.engine.log.reader.LogReaderProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dstankovic on 4/5/16.
 */
public class OnlineExperimentHandler implements JobObserver {
  private JobListener listener;
  private File stdoutFile;
  private File stderrFile;

  private Timer timer;
  private static final long REFRESH_INTERVAL = 1000;

  public OnlineExperimentHandler(JobListener listener, File stdoutFile, File stderrFile) {
    this.listener = listener;
    this.stdoutFile = stdoutFile;
    this.stderrFile = stderrFile;
  }

  @Override
  public void jobStarted(Job job) {
    listener.jobStarted(job);
    initTimer(job);
  }

  @Override
  public void jobFinished(Job job, ProcessOutput output) {
    timer.cancel();
    ExperimentHandlerUtils.deleteConfIfNeeded(job);
    InputStream is = output.getStdout();
    LogModel log = LogReaderProvider.getReader().read(is);
    listener.jobFinished(job, log);
  }

  @Override
  public void jobFailed(Job job) {
    timer.cancel();
    ExperimentHandlerUtils.deleteConfIfNeeded(job);
    listener.jobFailed(job);
  }

  private void initTimer(Job job) {
    timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        try {
          LogModel log = LogReaderProvider.getReader().read(new FileInputStream(stdoutFile));
          listener.jobPartiallyFinished(job, log);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }
    }, REFRESH_INTERVAL, REFRESH_INTERVAL);
  }
}
