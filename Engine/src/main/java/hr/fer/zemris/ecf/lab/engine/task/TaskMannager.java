package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.console.ConsoleFactory;
import hr.fer.zemris.ecf.lab.engine.console.Console;
import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.param.ParametersList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class is a manager for tasks and threads that are running those tasks.
 * It's main use is to talk to GUI. It can gather initial parameters, it can
 * find out how many CPU cores current PC has, and of course it's main use
 * startOfflineTasks methodd. It gets list of {@link Job} and number of threads to run
 * them on.
 *
 * @version 1.0
 */
public class TaskMannager {

  private Console console;
  private int cpuCors;

  /**
   * Constructor, it automatically detects operating system, stores
   * {@link Console} needed for that OS, and it get the number of CPU cores and
   * stores it also.
   */
  public TaskMannager() {
    console = ConsoleFactory.createConsole();
    cpuCors = Runtime.getRuntime().availableProcessors();
  }

  /**
   * Getter for the number of CPU cores on current PC.
   *
   * @return number of CPU cores on current PC
   */
  public int getCpuCors() {
    return cpuCors;
  }

  /**
   * This meted gets initial ECF parameters dumped by the ECF.
   *
   * @param ecfPath     path to the ECF
   * @param pardumpPath path to where the parameters will be dumped
   * @return initial compilation of algorithms, genotypes, and registry.
   */
  public ParametersList getInitialECFparams(String ecfPath, String pardumpPath) {
    console.pardump(ecfPath, pardumpPath);
    File paramsFile = new File(pardumpPath);
    return ConfigurationService.getInstance().getReader().readInitial(paramsFile);
  }

  /**
   * This method is used for running {@link Task}s. It gets list of
   * {@link Job} and number of threads to run them on.
   *
   * @param taskDescriptions array list of jobs needed to do.
   * @param numOfThreads     number of threads to run them on.
   * @return if all done with no problem, false if problem.
   * @throws Exception If problem occurs while running
   */
  public boolean startOfflineTasks(List<Job> taskDescriptions, int numOfThreads) throws Exception {
    List<Task> tasks = new ArrayList<>();

    for (int i = 0; i < taskDescriptions.size(); i++) {
      tasks.add(new Task(taskDescriptions.get(i), console));
    }

    return startTasks(tasks, numOfThreads);
  }

  public boolean startOnlineTasks(List<OnlineJobDescriptor> taskDescriptions, int numOfThreads) throws Exception {
    List<Task> tasks = new ArrayList<>();

    for (int i = 0; i < taskDescriptions.size(); i++) {
      OnlineJobDescriptor descriptor = taskDescriptions.get(i);
      tasks.add(new Task(descriptor.getJob(), console, descriptor.getStdoutFile(), descriptor.getStderrFile()));
    }

    return startTasks(tasks, numOfThreads);
  }

  private boolean startTasks(List<Task> tasks, int numOfThreads) throws Exception {
    ExecutorService service = Executors.newFixedThreadPool(numOfThreads);
    List<Future<Void>> results;
    try {
      results = service.invokeAll(tasks);
    } catch (InterruptedException e) {
      System.err.println("Fatal error! Can't do parallelization");
      service.shutdown();
      return false;
    }
    for (Future<Void> res : results) {
      try {
        res.get();
      } catch (InterruptedException | ExecutionException e) {
        service.shutdown();
        throw e;
      }
    }
    service.shutdown();
    return true;
  }
}
