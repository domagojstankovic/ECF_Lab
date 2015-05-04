package hr.fer.zemris.ecf.lab.view.display;

import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.task.ExperimentsManager;
import hr.fer.zemris.ecf.lab.engine.task.JobListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

/**
 * Frame that displays list of all results that have been generated. This frame
 * is singleton.
 *
 * @author Domagoj
 */
public class ResultProgressFrame extends JFrame implements JobListener {

    private static final long serialVersionUID = 1L;
    private static final String INITIALIZED = "Initialized";
    private static final String STARTED = "Started";
    private static final String FINISHED = "Finished";
    private static final String FAILED = "Failed";

    private JPanel panel = null;
    private ExperimentsManager manager;
    private Map<Job, JobProgressPanel> panelMap = new HashMap<>();
    private Map<Job, LogModel> logMap = new HashMap<>();

    public ResultProgressFrame() {
        super();
        setTitle("Results");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLocation(300, 200);
        setSize(400, 350);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        getContentPane().add(new JScrollPane(panel));
        manager = new ExperimentsManager(this);
    }

    public void runExperiment(Configuration conf, String ecfPath, String confPath, int threads) {
        panel.removeAll();
        panelMap.clear();
        logMap.clear();
        manager.runExperiment(conf, ecfPath, confPath, threads);
        setVisible(true);
    }

    @Override
    public Component add(Component comp) {
        return panel.add(comp);
    }

    private JobProgressPanel createComp(String text) {
        JobProgressPanel jpp = new JobProgressPanel(text);
        jpp.setButtonText(INITIALIZED);
        jpp.getButton().setEnabled(false);
        jpp.setMaximumSize(new Dimension(Integer.MAX_VALUE, jpp.getPreferredSize().height));

        return jpp;
    }

    @Override
    public void jobInitialized(Job job) {
        JobProgressPanel jpp = createComp("Experiment");
        add(jpp);
        panelMap.put(job, jpp);
    }

    @Override
    public void jobStarted(Job job) {
        SwingUtilities.invokeLater(() -> {
            if (panelMap.containsKey(job)) {
                JobProgressPanel jpp = panelMap.get(job);
                jpp.setButtonText(STARTED);
            }
        });
    }

    @Override
    public void jobFinished(Job job, LogModel log) {
        logMap.put(job, log);
        SwingUtilities.invokeLater(() -> {
            if (panelMap.containsKey(job)) {
                JobProgressPanel jpp = panelMap.get(job);
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
                JobProgressPanel jpp = panelMap.get(job);
                jpp.setButtonText(FAILED);
            }
        });
    }
}
