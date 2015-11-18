package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.console.JobObserver;
import hr.fer.zemris.ecf.lab.engine.console.ProcessOutput;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.engine.log.reader.LogReaderProvider;
import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.param.Entry;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domagoj on 03/05/15.
 */
public class ExperimentsManager implements JobObserver {

    private boolean daemon = false;
    private JobListener listener;

    public ExperimentsManager(JobListener listener) {
        this.listener = listener;
    }

    public void runExperiment(Configuration conf, String ecfPath, String confPath, int threads) {
        try {
            boolean implicitParallelism = false;
            int repeats = 1;
            List<Entry> registryList = conf.registry.getEntryList();
            Entry batchRepeatEntry = Utils.findEntry(registryList, "batch.repeats");
            if (threads > 1) {
                if (batchRepeatEntry != null) {
                    repeats = Integer.parseInt(batchRepeatEntry.value);
                    if (repeats > 1) {
                        // N repeats, N threads -> separate repeats in N jobs (1 repeat per job)
                        // implicit parallelism
                        implicitParallelism = true;
                    } else {
                        // 1 job (1 repeat), N threads -> change to 1 thread
                        threads = 1;
                    }
                } else {
                    // 1 job, N threads -> change to 1 thread
                    threads = 1;
                }
            }

            // write configuration file to the disk
            ConfigurationService.getInstance().getWriter().write(new File(confPath), conf);

            // create jobs
            final List<Job> jobs;
            if (implicitParallelism) {
                // implicit parallelism
                batchRepeatEntry.value = "1";
                jobs = new ArrayList<>(repeats);
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
                    job.setObserver(this);
                    jobs.add(job);
                }
            } else {
                // no implicit parallelism, just 1 job
                jobs = new ArrayList<>(1);
                Job job = new Job(ecfPath, confPath);
                job.setObserver(this);
                jobs.add(job);
            }
            for (Job job : jobs) {
                listener.jobInitialized(job);
            }
            final int tCount = threads;

            // starting tasks
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
        } catch (Exception e) {
            throw new ExperimentException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void jobStarted(Job job) {
        listener.jobStarted(job);
    }

    @Override
    public void jobFinished(Job job, ProcessOutput output) {
        deleteConfIfNeeded(job);
        InputStream is = output.getStdout();
        LogModel log = LogReaderProvider.getReader().read(is);
        listener.jobFinished(job, log);
    }

    @Override
    public void jobFailed(Job job) {
        deleteConfIfNeeded(job);
        listener.jobFailed(job);
    }

    private void deleteConfIfNeeded(Job job) {
        if (job.shouldDeleteConf()) {
            // job was created with implicit parallelism, delete configuration file
            String confPath = job.getConfigPath();
            File configFile = new File(confPath);
            if (configFile.exists()) {
                configFile.delete();
            }
        }
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }
}
