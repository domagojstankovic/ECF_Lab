package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.console.ProcessOutput;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.engine.log.reader.LogReaderProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dstankovic on 4/5/16.
 */
public class OnlineExperimentHandler extends OfflineExperimentHandler {
  private File stdoutFile;
  private File stderrFile;

  private Timer timer;
  private static final long REFRESH_INTERVAL = 1000;

  public OnlineExperimentHandler(JobListener listener, File stdoutFile, File stderrFile) {
    super(listener);
    this.stdoutFile = stdoutFile;
    this.stderrFile = stderrFile;
  }

  @Override
  public void jobStarted(Job job) {
    super.jobStarted(job);
    initTimer(job);
  }

  @Override
  public void jobFinished(Job job, ProcessOutput output) {
    timer.cancel();
    super.jobFinished(job, output);
  }

  @Override
  public void jobFailed(Job job) {
    timer.cancel();
    super.jobFailed(job);
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
