package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.console.Job;

import java.io.File;

/**
 * Created by dstankovic on 4/5/16.
 */
public class OnlineJobDescriptor {
  private Job job;
  private File stdoutFile;
  private File stderrFile;

  public OnlineJobDescriptor(Job job, File stdoutFile, File stderrFile) {
    this.job = job;
    this.stdoutFile = stdoutFile;
    this.stderrFile = stderrFile;
  }

  public Job getJob() {
    return job;
  }

  public File getStdoutFile() {
    return stdoutFile;
  }

  public File getStderrFile() {
    return stderrFile;
  }
}
