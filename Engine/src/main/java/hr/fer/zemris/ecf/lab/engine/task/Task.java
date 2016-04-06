package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.console.Console;
import hr.fer.zemris.ecf.lab.engine.console.Job;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * This class represents one thread task a.k.a. one ECF task. The thread that gets this is stopped until the ECF finishes this specific task.
 * The task for the ECF is given by the {@link Job}.
 * It uses {@link Console} to start ECF task.
 *
 * @version 1.0
 */
public class Task implements Callable<Void> {

  private Job job;
  private Console console;

  private File stdout = null;
  private File stderr = null;

  /**
   * Constructor it gets the specific {@link Job} and a specific {@link Console} for current pc.
   *
   * @param job     given job
   * @param console console type determined by {@link Console} implementation of specific pc this task will bi ran on.
   */
  public Task(Job job, Console console) {
    this.job = job;
    this.console = console;
  }

  public Task(Job job, Console console, File stdout, File stderr) {
    this.job = job;
    this.console = console;
    this.stdout = stdout;
    this.stderr = stderr;
  }

  @Override
  public Void call() throws Exception {
    if (stdout == null && stderr == null) {
      console.execute(job);
    } else {
      console.execute(job, stdout, stderr);
    }
    return null;
  }

}
