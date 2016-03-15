package hr.fer.zemris.ecf.lab.view.layout;

import hr.fer.zemris.ecf.lab.engine.param.*;
import hr.fer.zemris.ecf.lab.model.info.InfoService;
import hr.fer.zemris.ecf.lab.view.display.ResultProgressFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel that displays available parameters for the selected ECF executable
 * file. When all the parameters are selected, configuration can be ran.
 * Configuration and log files are created on the specified paths.
 *
 * @author Domagoj Stanković
 * @version 1.0
 */
public class ParametersSelection extends JPanel {

    private static final long serialVersionUID = 1L;

    private EntryBlockSelection<EntryBlock> algSel;
    private EntryBlockSelection<EntryBlock> genSel;
    private EntryListPanel regList;
    private DefinePanel definePanel;
    private String lastLogFilePath = null;
    private ResultProgressFrame progressFrame = null;

    /**
     * Creates new {@link ParametersSelection} object for choosing ECF
     * parameters.
     *
     * @param params List of available parameters.
     */
    public ParametersSelection(ParametersList params) {
        super(new BorderLayout());
        algSel = new EntryBlockSelection<>(new DropDownPanel<>(params.algorithms));
        genSel = new EntryBlockSelection<>(new DropDownPanel<>(params.genotypes));
        regList = EntryListPanel.getComponent(params.registry.getEntryList());
        String file = new File("").getAbsolutePath();
        add(new TempPanel(algSel, genSel, new JScrollPane(regList)), BorderLayout.CENTER);
        JButton button = new JButton(new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                runClicked();
            }
        });
        button.setText("Run");
        int cores = Runtime.getRuntime().availableProcessors();
        definePanel = new DefinePanel(file, cores, button);
        add(definePanel, BorderLayout.SOUTH);
    }

    /**
     * Action performed when the "Run" button is clicked. Configuration file is
     * created under the specified path. Then the ECF exe is run and the results
     * are written to the log file under the specified path.
     */
    protected void runClicked() {
        List<Configuration> confs = getParameters();
        String ecfPath = InfoService.getEcfPath();
        String confPath = definePanel.getParamsPath();
        int threads = definePanel.getThreadsCount();
        getProgressFrame().runExperiment(confs, ecfPath, confPath, threads); // TODO
    }

    /**
     * Collects all the selected parameters from the selected
     * {@link ParametersSelection} panel.
     *
     * @return {@link Configuration} object containing all the selected
     * parameters
     */
    public List<Configuration> getParameters() {
        // Algorithm filling
        List<EntryFieldDisplay<EntryBlock>> algList = algSel.getAddedEntries();
        List<MultiEntryBlock> algs = new ArrayList<>(algList.size());
        for (EntryFieldDisplay<EntryBlock> a : algList) {
            algs.add(new MultiEntryBlock(a.getBlock().getName(), a.getBlockDisplay().getSelectedEntries()));
        }

        // Genotype filling
        List<EntryFieldDisplay<EntryBlock>> genList = genSel.getAddedEntries();
        List<MultiEntryBlock> gens = new ArrayList<>(genList.size());
        for (EntryFieldDisplay<EntryBlock> g : genList) {
            gens.add(new MultiEntryBlock(g.getBlock().getName(), g.getBlockDisplay().getSelectedEntries()));
        }
        List<List<MultiEntryBlock>> genBlock = new ArrayList<>(1);
        genBlock.add(gens);

        // Registry filling
        int size = regList.getEntriesCount();
        List<MultiEntry> entries = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (regList.isSelected(i)) {
                entries.add(new MultiEntry(regList.getKeyAt(i), regList.getDescriptionAt(i), regList.getValueAt(i)));
            }
        }
        MultiEntryBlock reg = new MultiEntryBlock(null, entries);

        ConfigurationsCreator configurationsCreator = ConfigurationsCreatorFactory.create();
        return configurationsCreator.createConfigurations(algs, genBlock, reg);
    }

    /**
     * @return {@link AlgorithmSelection} from the selected
     * {@link ParametersSelection} panel
     */
    public EntryBlockSelection<EntryBlock> getAlgSel() {
        return algSel;
    }

    /**
     * @return {@link GenotypeSelection} from the selected
     * {@link ParametersSelection} panel
     */
    public EntryBlockSelection<EntryBlock> getGenSel() {
        return genSel;
    }

    /**
     * @return {@link EntryFieldPanel} representing Registry from the selected
     * {@link ParametersSelection} panel
     */
    public EntryListPanel getRegList() {
        return regList;
    }

    /**
     * @return Selected algorithm from the selected
     * {@link ParametersSelection} panel
     */
    public EntryBlock getSelectedAlgorithm() {
        return algSel.getSelectedItem();
    }

    /**
     * @return Selected genotype from the selected
     * {@link ParametersSelection} panel
     */
    public EntryBlock getSelectedGenotype() {
        return genSel.getSelectedItem();
    }

    /**
     * @return {@link DefinePanel} containing information about configuration
     * and log files paths
     */
    public DefinePanel getDefinePanel() {
        return definePanel;
    }

    /**
     * Panel used for grouping {@link AlgorithmSelection},
     * {@link GenotypeSelection} and {@link EntryFieldPanel} panels.
     *
     * @author Domagoj Stanković
     * @version 1.0
     */
    private static class TempPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        public TempPanel(Component algSel, Component genSel, Component regList) {
            super();
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(algSel);
            add(new JSeparator(JSeparator.VERTICAL));
            add(genSel);
            add(new JSeparator(JSeparator.VERTICAL));
            add(regList);
        }
    }

    /**
     * @return <code>true</code> if "Run" button was ever run before,
     * <code>false</code> otherwise
     */
    public boolean wasRunBefore() {
        return lastLogFilePath == null ? false : true;
    }

    /**
     * @return Path to the log file created during last experiment,
     * <code>null</code> if selected experiment was never run before
     */
    public String getLastLogFilePath() {
        return lastLogFilePath;
    }

    public ResultProgressFrame getProgressFrame() {
        if (progressFrame == null) {
            progressFrame = new ResultProgressFrame();
        }
        return progressFrame;
    }
}
