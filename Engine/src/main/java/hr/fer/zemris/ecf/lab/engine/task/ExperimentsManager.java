package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.console.JobObserver;
import hr.fer.zemris.ecf.lab.engine.console.ProcessOutput;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.engine.log.reader.LogReaderProvider;
import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.param.Entry;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domagoj on 03/05/15.
 */
public class ExperimentsManager implements JobObserver {

    private static final boolean DAEMON = false;

    private JobListener listener;

    public ExperimentsManager(JobListener listener) {
        this.listener = listener;
    }

    public void runExperiment(Configuration conf, String ecfPath, String confPath, int threads) {
        try {
            boolean change = false;
            int repeats = 1;
            List<Entry> registryList = conf.registry.getEntryList();
            Entry batchRepeatEntry = Utils.findEntry(registryList, "batch.repeats");
            if (threads > 1) {
                if (batchRepeatEntry != null) {
                    repeats = Integer.parseInt(batchRepeatEntry.value);
                    if (repeats > 1) {
                        // N repeats, N threads -> separate repeats in N jobs (1 repeat per job)
                        batchRepeatEntry.value = "1";
                        change = true;
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
            if (change) {
                jobs = new ArrayList<>(repeats);
                for (int i = 0; i < repeats; i++) {
                    Job job = new Job(ecfPath, confPath);
                    job.setObserver(this);
                    jobs.add(job);
                }
            } else {
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
            t.setDaemon(DAEMON);
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
        InputStream is = output.getStdout();
        LogModel log = LogReaderProvider.getReader().read(is);
        listener.jobFinished(job, log);
    }

    @Override
    public void jobFailed(Job job) {
        listener.jobFailed(job);
    }

}
