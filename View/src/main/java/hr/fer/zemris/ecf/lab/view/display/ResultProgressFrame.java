package hr.fer.zemris.ecf.lab.view.display;

import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.param.Entry;
import hr.fer.zemris.ecf.lab.engine.task.ExperimentsManager;
import hr.fer.zemris.ecf.lab.engine.task.JobListener;
import hr.fer.zemris.ecf.lab.model.util.DescriptorUtils;
import hr.fer.zemris.ecf.lab.model.util.Pair;
import hr.fer.zemris.ecf.lab.view.layout.TextButtonListFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Frame that displays list of all results that have been generated. This frame
 * is singleton.
 *
 * @author Domagoj
 */
public class ResultProgressFrame extends TextButtonListFrame implements JobListener {

    private static final long serialVersionUID = 1L;
    private static final String INITIALIZED = "Initialized";
    private static final String STARTED = "Started";
    private static final String FINISHED = "Finished";
    private static final String FAILED = "Failed";

    private ExperimentsManager manager;
    private Map<Job, TextButtonPanel> panelMap = new ConcurrentHashMap<>();
    private Map<Job, LogModel> logMap = new ConcurrentHashMap<>();

    public ResultProgressFrame() {
        super("Results");
        manager = new ExperimentsManager(this);
    }

    public void runExperiment(List<Pair<Configuration, List<Pair<String, String>>>> confs,
                              String ecfPath,
                              String confPath,
                              int threads) {
        panel.removeAll();
        panelMap.clear();
        logMap.clear();
        if (confs.size() == 1) {
            manager.runExperiment(confs.get(0).getFirst(), ecfPath, confPath, threads);
        } else {
            for (Pair<Configuration, List<Pair<String, String>>> confDesc : confs) {
                // change confPath and log path
                Configuration conf = confDesc.getFirst();
                String desc = DescriptorUtils.mergeDescriptor(confDesc.getSecond());

                String newConfPath = DescriptorUtils.modifiedString(confPath, desc);
                Entry logEntry = conf.registry.getEntryWithKey("log.filename");
                if (logEntry != null) {
                    logEntry.value = DescriptorUtils.modifiedString(logEntry.value != null ? logEntry.value : "", desc);
                }

                manager.runExperiment(conf, ecfPath, newConfPath, threads);
            }
        }
        setVisible(true);
    }

    @Override
    public void jobInitialized(Job job) {
        int cnt = panel.getComponentCount();
        TextButtonPanel jpp = createComp("Experiment " + (cnt + 1));
        jpp.setButtonText(INITIALIZED);
        jpp.getButton().setEnabled(false);
        panelMap.put(job, jpp);
    }

    @Override
    public void jobStarted(Job job) {
        SwingUtilities.invokeLater(() -> {
            if (panelMap.containsKey(job)) {
                TextButtonPanel jpp = panelMap.get(job);
                jpp.setButtonText(STARTED);
            }
        });
    }

    @Override
    public void jobFinished(Job job, LogModel log) {
        logMap.put(job, log);
        SwingUtilities.invokeLater(() -> {
            if (panelMap.containsKey(job)) {
                TextButtonPanel jpp = panelMap.get(job);
                jpp.getButton().setAction(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        LogModel l = logMap.get(job);
                        new FrameDisplayer().displayLog(l);
                    }
                });
                jpp.setButtonText(FINISHED);
                jpp.getButton().setEnabled(true);
            }
        });
    }

    @Override
    public void jobFailed(Job job) {
        SwingUtilities.invokeLater(() -> {
            if (panelMap.containsKey(job)) {
                TextButtonPanel jpp = panelMap.get(job);
                jpp.setButtonText(FAILED);
            }
        });
    }
}
