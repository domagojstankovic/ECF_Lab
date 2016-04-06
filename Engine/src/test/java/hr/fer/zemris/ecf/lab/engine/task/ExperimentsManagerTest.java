package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationReader;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationWriter;
import hr.fer.zemris.ecf.lab.engine.console.DetectOS;
import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Domagoj on 07/05/15.
 */
public class ExperimentsManagerTest {

    private static boolean isSetUpDone = false;

    @Before
    public void setUp() throws Exception {
        if (!isSetUpDone) {
            ConfigurationService.getInstance().setReader(new XmlConfigurationReader());
            ConfigurationService.getInstance().setWriter(new XmlConfigurationWriter());
            isSetUpDone = true;
        }
    }

    @Test
    public void testRunParallelExperiment() throws Exception {
		boolean disableTest = true;
		if (disableTest) {
			return;
		}
        Map<Job, boolean[]> map = new ConcurrentHashMap<>();
        Set<String> logSet = Collections.newSetFromMap(new ConcurrentHashMap<>()); // concurrent HashSet
        Set<String> delSet = new HashSet<>();

        JobListener listener = new JobListener() {
            @Override
            public void jobInitialized(Job job) {
                assertFalse("This job is already initialized", map.containsKey(job));
                boolean[] arr = new boolean[2];
                map.put(job, arr);
                assertTrue("Configuration file should exist", new File(job.getConfigPath()).exists());
            }

            @Override
            public void jobStarted(Job job) {
                assertTrue("This job has not been initialized", map.containsKey(job));
                boolean[] arr = map.get(job);
                assertFalse("This job has already started", arr[0]);
                arr[0] = true;
            }

            @Override
            public void jobPartiallyFinished(Job job, LogModel log) {
            }

            @Override
            public void jobFinished(Job job, LogModel log) {
                checkJobFinish(job);
                assertFalse("More jobs than log files", logSet.isEmpty());
                String logPath = null;
                for (String path : logSet) {
                    File file = new File(path);
                    if (file.exists()) {
                        logPath = path;
                        break;
                    }
                }
                assertFalse("Job has finished but there is no log file", logPath == null);
                logSet.remove(logPath);
            }

            @Override
            public void jobFailed(Job job) {
                checkJobFinish(job);
            }

            private void checkJobFinish(Job job) {
                assertTrue("This job has not been initialized", map.containsKey(job));
                boolean[] arr = map.get(job);
                assertTrue("This job has not started yet", arr[0]);
                assertFalse("This job has already finished or failed", arr[1]);
                arr[1] = true;
                if (job.shouldDeleteConf()) {
                    assertFalse("Configuration file should be deleted", new File(job.getConfigPath()).exists());
                } else {
                    assertTrue("Configuration file should exist", new File(job.getConfigPath()).exists());
                }
            }
        };

        ExperimentsManager manager = new ExperimentsManager(listener);
        Configuration conf = ConfigurationService.getInstance().getReader()
                .readArchive(new File("res/test/parallel/parameters.xml"));
        for (int i = 1; i < 10; i++) {
            logSet.add("res/test/parallel/__parallel__log_0" + i + ".txt");
        }
        logSet.add("res/test/parallel/__parallel__log_10.txt");
        delSet.addAll(logSet);

        String ecfPath;
        if (DetectOS.isMac()) {
            ecfPath = "res/test/onemax";
        } else if (DetectOS.isWindows()) {
            ecfPath = "res/test/GAOneMax.exe";
        } else {
			// TODO for other operating systems
			return;
		}
        String confPath = "res/test/parallel/__ExperimentsManagerTest_config.xml";
        delSet.add(confPath);
        int threads = 2;
        manager.setDaemon(true);
        manager.runExperiment(conf, ecfPath, confPath, threads, false);

        // this has to be done in order to prevent test from finishing until other threads are done
        while (true) {
            Thread.sleep(500);
            if (logSet.isEmpty()) {
                // delete unnecessary files generated by experiment
                for (String path : delSet) {
                    File file = new File(path);
                    assertTrue("File does not exist after experiment termination but it should", file.exists());
                    file.delete();
                }
                break;
            }
        }
    }

    @Test
    public void testRunSerialExperiment() throws Exception {
        assertTrue(true);
    }
}