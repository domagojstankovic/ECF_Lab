package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.param.Entry;

import java.io.File;
import java.io.IOException;
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
      JobDescriptors jobDescriptors = createJobs(conf, ecfPath, confPath, implicitParallelism, repeats,
          batchRepeatEntry, online);
      List<Job> jobs = jobDescriptors.jobs;

      // notify listeners
      for (Job job : jobs) {
        listener.jobInitialized(job);
      }

      // starting tasks
      if (online) {
        List<OnlineJobDescriptor> descriptors = new ArrayList<>();
        int len = jobs.size();
        List<FileOutputPair> fileOutputPairs = jobDescriptors.fileOutputs;
        for (int i = 0; i < len; i++) {
          FileOutputPair pair = fileOutputPairs.get(i);
          descriptors.add(new OnlineJobDescriptor(jobs.get(i), pair.stdout, pair.stderr));
        }
        startOnlineTasks(descriptors, threadsToUse);
      } else {
        startOfflineTasks(jobs, threadsToUse);
      }
    } catch (Exception e) {
      throw new ExperimentException(e.getMessage(), e.getCause());
    }
  }

  private JobDescriptors createJobs(Configuration conf,
                                    String ecfPath,
                                    String confPath,
                                    boolean implicitParallelism,
                                    int repeats,
                                    Entry batchRepeatEntry,
                                    boolean online) {
    if (implicitParallelism) {
      // implicit parallelism
      return createParallelJobs(conf, ecfPath, confPath, repeats, batchRepeatEntry, online);
    } else {
      // no implicit parallelism, just 1 job
      return createSerialJob(ecfPath, confPath, online);
    }
  }

  private JobDescriptors createParallelJobs(Configuration conf,
                                            String ecfPath,
                                            String confPath,
                                            int repeats,
                                            Entry batchRepeatEntry,
                                            boolean online) {
    List<Entry> registryList = conf.registry.getEntryList();
    batchRepeatEntry.value = "1";
    List<Job> jobs = new ArrayList<>(repeats);
    List<FileOutputPair> fileOutputPairs = online ? new ArrayList<>(repeats) : null;

    int len = Integer.valueOf(repeats).toString().length();

    Entry logFilenameEntry = Utils.findEntry(registryList, "log.filename");
    String originalLogFilename = null;
    if (logFilenameEntry != null) {
      originalLogFilename = logFilenameEntry.value;
    }

    Entry statsfileEntry = Utils.findEntry(registryList, "batch.statsfile");
    String originalStatsfile = null;
    if (statsfileEntry != null) {
      originalStatsfile = statsfileEntry.value;
    }

    StatsSupervisor supervisor = new StatsSupervisor(originalStatsfile, repeats);

    for (int i = 0; i < repeats; i++) {
      // change configuration (log.filename) and write it to changed location
      String currConfPath = confPath + ".part__" + (i + 1);
      if (originalLogFilename != null) {
        logFilenameEntry.value = Utils.addBeforeExtension(originalLogFilename, (i + 1), len);
      }

      // statsfile
      if (originalStatsfile != null) {
        try {
          File tempFile = File.createTempFile("ecf-statsfile", ".txt");
          statsfileEntry.value = tempFile.getAbsolutePath();
        } catch (IOException e) {
          e.printStackTrace();
          statsfileEntry.value = Utils.addBeforeExtension(originalStatsfile, (i + 1), len);
        }
      }

      ConfigurationService.getInstance().getWriter().write(new File(currConfPath), conf);

      StatsHandler statsHandler = statsfileEntry != null ? new StatsHandler(statsfileEntry.value, supervisor) : null;

      Job job = new Job(ecfPath, currConfPath, true);
      if (online) {
        FileOutputPair fileOutputPair = generateOnlineFileOutputs();
        job.setObserver(new OnlineExperimentHandler(
            listener,
            fileOutputPair.stdout,
            fileOutputPair.stderr,
            statsHandler)
        );
        fileOutputPairs.add(fileOutputPair);
      } else {
        job.setObserver(new OfflineExperimentHandler(listener, statsHandler));
      }
      jobs.add(job);
    }

    return new JobDescriptors(jobs, fileOutputPairs);
  }

  private JobDescriptors createSerialJob(String ecfPath, String confPath, boolean online) {
    List<Job> jobs = new ArrayList<>(1);
    Job job = new Job(ecfPath, confPath);
    List<FileOutputPair> fileOutputPairs = null;
    if (online) {
      FileOutputPair fileOutputPair = generateOnlineFileOutputs();
      job.setObserver(new OnlineExperimentHandler(listener, fileOutputPair.stdout, fileOutputPair.stderr));
      fileOutputPairs = new ArrayList<>(1);
      fileOutputPairs.add(fileOutputPair);
    } else {
      job.setObserver(new OfflineExperimentHandler(listener));
    }
    jobs.add(job);

    return new JobDescriptors(jobs, fileOutputPairs);
  }

  private void startOfflineTasks(List<Job> jobs, int threads) {
    Thread t = new Thread(() -> {
      TaskMannager tm = new TaskMannager();
      try {
        tm.startOfflineTasks(jobs, threads);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    t.setDaemon(daemon);
    t.start();
  }

  private void startOnlineTasks(List<OnlineJobDescriptor> descriptors, int threads) {
    Thread t = new Thread(() -> {
      TaskMannager tm = new TaskMannager();
      try {
        tm.startOnlineTasks(descriptors, threads);
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

  private static FileOutputPair generateOnlineFileOutputs() {
    try {
      File stdoutFile = File.createTempFile("ecflab-stdout-online", ".txt");
      File stderrFile = File.createTempFile("ecflab-stderr-online", ".txt");
      return new FileOutputPair(stdoutFile, stderrFile);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static class FileOutputPair {
    File stdout;
    File stderr;

    public FileOutputPair(File stdout, File stderr) {
      this.stdout = stdout;
      this.stderr = stderr;
    }
  }

  private static class JobDescriptors {
    List<Job> jobs;
    List<FileOutputPair> fileOutputs;

    public JobDescriptors(List<Job> jobs, List<FileOutputPair> fileOutputs) {
      this.jobs = jobs;
      this.fileOutputs = fileOutputs;
    }
  }
}
