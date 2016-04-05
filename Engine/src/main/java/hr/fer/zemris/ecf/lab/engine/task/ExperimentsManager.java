package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.param.Entry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domagoj on 03/05/15.
 */
public class ExperimentsManager {

  private boolean daemon = false;
  private JobListener listener;

  public ExperimentsManager(JobListener listener) {
    this.listener = listener;
  }

  public void runExperiment(Configuration conf, String ecfPath, String confPath, int threads, boolean online) {
    try {
      boolean implicitParallelism = false;
      int repeats = 1;
      int threadsToUse = threads;
      List<Entry> registryList = conf.registry.getEntryList();
      Entry batchRepeatEntry = Utils.findEntry(registryList, "batch.repeats");
      if (threadsToUse > 1) {
        if (batchRepeatEntry != null) {
          repeats = Integer.parseInt(batchRepeatEntry.value);
          if (repeats > 1) {
            // N repeats, N threads -> separate repeats in N jobs (1 repeat per job)
            // implicit parallelism
            implicitParallelism = true;
          } else {
            // 1 job (1 repeat), N threads -> change to 1 thread
            threadsToUse = 1;
          }
        } else {
          // 1 job, N threads -> change to 1 thread
          threadsToUse = 1;
        }
      }

      // write configuration file to the disk
      ConfigurationService.getInstance().getWriter().write(new File(confPath), conf);

      // create jobs
      List<Job> jobs = createJobs(conf, ecfPath, confPath, implicitParallelism, repeats, batchRepeatEntry);

      // notify listeners
      for (Job job : jobs) {
        listener.jobInitialized(job);
      }

      // starting tasks
      startTasks(jobs, threadsToUse);
    } catch (Exception e) {
      throw new ExperimentException(e.getMessage(), e.getCause());
    }
  }

  private List<Job> createJobs(Configuration conf, String ecfPath, String confPath, boolean implicitParallelism,
                               int repeats, Entry batchRepeatEntry) {
    if (implicitParallelism) {
      // implicit parallelism
      return createParallelJobs(conf, ecfPath, confPath, repeats, batchRepeatEntry);
    } else {
      // no implicit parallelism, just 1 job
      return createSerialJob(ecfPath, confPath);
    }
  }

  private List<Job> createParallelJobs(Configuration conf, String ecfPath, String confPath, int repeats,
                                       Entry batchRepeatEntry) {
    List<Entry> registryList = conf.registry.getEntryList();
    batchRepeatEntry.value = "1";
    List<Job> jobs = new ArrayList<>(repeats);
    int len = Integer.valueOf(repeats).toString().length();
    Entry logFilenameEntry = Utils.findEntry(registryList, "log.filename");
    String originalLogFilename = null;
    if (logFilenameEntry != null) {
      originalLogFilename = logFilenameEntry.value;
    }
    for (int i = 0; i < repeats; i++) {
      // change configuration (log.filename) and write it to changed location
      String currConfPath = confPath + ".part__" + (i + 1);
      if (originalLogFilename != null) {
        String currLogFilename = Utils.addBeforeExtension(originalLogFilename, (i + 1), len);
        logFilenameEntry.value = currLogFilename;
      }

      ConfigurationService.getInstance().getWriter().write(new File(currConfPath), conf);

      Job job = new Job(ecfPath, currConfPath, true);
      job.setObserver(new OfflineExperimentHandler(listener));
      jobs.add(job);
    }

    return jobs;
  }

  private List<Job> createSerialJob(String ecfPath, String confPath) {
    List<Job> jobs = new ArrayList<>(1);
    Job job = new Job(ecfPath, confPath);
    job.setObserver(new OfflineExperimentHandler(listener));
    jobs.add(job);

    return jobs;
  }

  private void startTasks(List<Job> jobs, int threads) {
    final int tCount = threads;

    Thread t = new Thread(() -> {
      TaskMannager tm = new TaskMannager();
      try {
        tm.startTasks(jobs, tCount);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    t.setDaemon(daemon);
    t.start();
  }

  public void setDaemon(boolean daemon) {
    this.daemon = daemon;
  }
}
