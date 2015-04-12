package hr.fer.zemris.ecf.lab.view;

import hr.fer.zemris.ecf.lab.model.info.InfoService;
import hr.fer.zemris.ecf.lab.model.logger.LoggerProvider;
import hr.fer.zemris.ecf.lab.model.settings.SettingsProvider;
import hr.fer.zemris.ecf.lab.view.display.*;
import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.engine.param.*;
import hr.fer.zemris.ecf.lab.engine.task.TaskMannager;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationReader;
import hr.fer.zemris.ecf.lab.engine.conf.xml.XmlConfigurationWriter;
import hr.fer.zemris.ecf.lab.view.layout.EntryBlockSelection;
import hr.fer.zemris.ecf.lab.view.layout.EntryFieldPanel;
import hr.fer.zemris.ecf.lab.view.layout.EntryListPanel;
import hr.fer.zemris.ecf.lab.view.layout.ParametersSelection;
import hr.fer.zemris.ecf.lab.model.settings.SettingsKey;
import hr.fer.zemris.ecf.lab.model.settings.Settings;
import hr.fer.zemris.ecf.lab.model.settings.impl.PropertyFileSettings;
import hr.fer.zemris.ecf.lab.model.logger.Logger;
import hr.fer.zemris.ecf.lab.model.logger.impl.FileLogger;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 * Main frame of the application.
 * 
 * @author Domagoj Stanković
 * @version 1.0
 */
public class ECFLab extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String CONFIGURATION_FILE = "res/settings/settings.properties";
	private static final String APP_TITLE = "ECF Lab";

	private Map<String, Action> actions = new HashMap<>();
	private JMenuBar menuBar = new JMenuBar();
	private JTabbedPane tabbedPane;
	private JToolBar toolbar;
	private String ecfPath;
	private String parDumpPath;
	private ParametersList parDump;
	private IResultDisplay resultDisplay;
	private IResultDisplay openResultDisplay;

	/**
	 * Creates a new main frame for ECF Lab.
	 */
	public ECFLab() {
		try {
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			setLookAndFeel(true);
			initGUI();

			setTitle(APP_TITLE);
			try {
				Image image = ImageIO.read(new FileInputStream(SettingsProvider.getSettings().getValue(SettingsKey.ICON_APP_PATH)));
				setIconImage(image);
			} catch (IOException e) {
				LoggerProvider.getLogger().log(e);
			}

			setLocation(300, 100);
			setSize(1000, 600);
			setLayout(new BorderLayout());

			add(toolbar, BorderLayout.NORTH);

			tabbedPane = new JTabbedPane();
			add(tabbedPane, BorderLayout.CENTER);

			openResultDisplay = new FrameDisplayer();
			resultDisplay = new ResultProgressFrameDisplayer();
			
			setVisible(true);
			chooseECFExe();
		} catch (Exception e) {
			LoggerProvider.getLogger().log(e);
			reportError(e.getMessage());
		}
	}

	/**
	 * Displays dialog for choosing ECF executable file.
	 */
	private void chooseECFExe() {
		BrowsePanel ecfExePanel = new BrowsePanel();
		int retVal = JOptionPane.showConfirmDialog(this, ecfExePanel, "Choose executable ECF file",
				JOptionPane.OK_CANCEL_OPTION);

		if (retVal == JOptionPane.OK_OPTION) {
			ecfPath = ecfExePanel.getText();
			parDumpPath = SettingsProvider.getSettings().getValue(SettingsKey.DEFAULT_PARAMS_DUMP);
			setTitle(APP_TITLE + " - " + ecfPath);
			parDump = callParDump();
			InfoService.setLastSelectedECFexecutable(ecfPath);
		}
	}

	/**
	 * Initializes exit action, other actions and menu bar.
	 */
	private void initGUI() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		initActions();
		initMenuBar();
		initToolbar();
	}

	/**
	 * Initializes toolbar.
	 */
	private void initToolbar() {
		toolbar = new JToolBar();
		toolbar.setFloatable(false);

		JButton button = makeToolbarButton(SettingsProvider.getSettings().getValue(SettingsKey.ICON_NEW_CONF_PATH), "NewConf", "New configuration");
		toolbar.add(button);

		button = makeToolbarButton(SettingsProvider.getSettings().getValue(SettingsKey.ICON_OPEN_CONF_PATH), "OpenConf", "Open existing configuration");
		toolbar.add(button);

		button = makeToolbarButton(SettingsProvider.getSettings().getValue(SettingsKey.ICON_SAVE_CONF_PATH), "SaveConf", "Save configuration");
		toolbar.add(button);

		button = makeToolbarButton(SettingsProvider.getSettings().getValue(SettingsKey.ICON_SAVE_CONF_AS_PATH), "SaveConfAs", "Save configuration As");
		toolbar.add(button);
		
		toolbar.addSeparator();
		
		button = makeToolbarButton(SettingsProvider.getSettings().getValue(SettingsKey.ICON_OPEN_LOG_PATH), "OpenLog", "Open log file");
		toolbar.add(button);
		
		button = makeToolbarButton(SettingsProvider.getSettings().getValue(SettingsKey.ICON_SAVE_LOG_PATH), "SaveLog", "Save log file");
		toolbar.add(button);
	}

	protected JButton makeToolbarButton(String imgPath, String action, String toolTipText) {
		JButton button = new JButton(actions.get(action));
		button.setText("");
		button.setToolTipText(toolTipText);;
		ImageIcon icon = new ImageIcon(imgPath);
		button.setIcon(icon);
		return button;
	}

	/**
	 * Initializes main actions for ECF Lab GUI.
	 */
	private void initActions() {
		Action action;
		action = new AbstractAction("New") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				newTab("New configuration");
			}
		};
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		action.putValue(Action.SHORT_DESCRIPTION, "Create new configuration");
		actions.put("NewConf", action);

		action = new AbstractAction("Open") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				openConf();
			}
		};
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		action.putValue(Action.SHORT_DESCRIPTION, "Open existing configuration");
		actions.put("OpenConf", action);

		action = new AbstractAction("Save") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				saveConf();
			}
		};
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		action.putValue(Action.SHORT_DESCRIPTION, "Save configuration");
		actions.put("SaveConf", action);

		action = new AbstractAction("Save As") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				saveConfAs();
			}
		};
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		action.putValue(Action.SHORT_DESCRIPTION, "Save configuration as");
		actions.put("SaveConfAs", action);

		action = new AbstractAction("Open") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				openLog();
			}
		};
		action.putValue(Action.SHORT_DESCRIPTION, "Open log file");
		actions.put("OpenLog", action);

		action = new AbstractAction("Save") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				saveLog();
			}
		};
		action.putValue(Action.SHORT_DESCRIPTION, "Save log file");
		actions.put("SaveLog", action);

		action = new AbstractAction("Results frame") {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				ResultProgressFrame.getInstance().setVisible(true);
			}
		};
		actions.put("ResultsFrame", action);
		
		action = new AbstractAction("Change ECF") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				chooseECFExe();
			}
		};
		action.putValue(Action.SHORT_DESCRIPTION, "Change ECF executable file");
		actions.put("ChangeECFExe", action);

		action = new AbstractAction("ECF home page") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				ecfHomePage();
			}
		};
		action.putValue(Action.SHORT_DESCRIPTION, "Go to ECF home page");
		actions.put("ecfHomePage", action);
	}

	/**
	 * Copies log file created during last experiment to the destination path.
	 */
	protected void saveLog() {
		ParametersSelection ps = (ParametersSelection) tabbedPane.getSelectedComponent();
		boolean b = ps.wasRunBefore();
		if (!b) {
			JOptionPane.showMessageDialog(this, "This experiment was never run before!", "Action unavailable",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		JFileChooser fc = new JFileChooser();
		int retVal = fc.showSaveDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			Path source = Paths.get(ps.getLastLogFilePath());
			Path target = Paths.get(file.getAbsolutePath());
			CopyOption options = StandardCopyOption.REPLACE_EXISTING;
			try {
				Files.copy(source, target, options);
			} catch (IOException e) {
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(this, "Log file copied successfully!", "Saved successfully",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Opens dialog for choosing log file to be viewed. Then displays results.
	 */
	protected void openLog() {
		BrowsePanel logPathPanel = new BrowsePanel();
		int retVal = JOptionPane.showConfirmDialog(this, logPathPanel, "Choose log file", JOptionPane.OK_CANCEL_OPTION);

		if (retVal == JOptionPane.OK_OPTION) {
			try {
				openResultDisplay.displayResult(logPathPanel.getText());
			} catch (Exception e) {
				LoggerProvider.getLogger().log(e);
				reportError(e.getMessage());
			}
		}
	}

	/**
	 * Displays ECF home page in users default browser.
	 */
	protected void ecfHomePage() {
		URI uri;
		try {
			uri = new URI(SettingsProvider.getSettings().getValue(SettingsKey.ECF_HOME_PAGE));
			Desktop.getDesktop().browse(uri);
		} catch (URISyntaxException e) {
			LoggerProvider.getLogger().log(e);
			e.printStackTrace();
		} catch (IOException e) {
			LoggerProvider.getLogger().log(e);
			e.printStackTrace();
		}
	}

	/**
	 * Saves current configuration under the name chosen in the file chooser
	 * dialog.
	 */
	protected void saveConfAs() {
		JFileChooser fc = new JFileChooser();
		int retVal = fc.showSaveDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String path = file.getAbsolutePath();
			ParametersSelection ps = (ParametersSelection) tabbedPane.getSelectedComponent();
			ConfigurationService.getInstance().getWriter().write(new File(path), ps.getParameters());
			JOptionPane.showMessageDialog(this, "Saved under name: " + path, "Saved succesfully",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Saves current configuration under the name written in the define panel of
	 * the selected {@link ParametersSelection} panel.
	 */
	protected void saveConf() {
		ParametersSelection ps = (ParametersSelection) tabbedPane.getSelectedComponent();
		String path = ps.getDefinePanel().getParamsPath();
		ConfigurationService.getInstance().getWriter().write(new File(path), ps.getParameters());
		tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), path);
	}

	protected void openConf() {
		try {
			JFileChooser fc = new JFileChooser(InfoService.getLastSelectedConfPath());
			int retVal = fc.showOpenDialog(this);
			if (retVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File file = fc.getSelectedFile();
			String absolutePath = file.getAbsolutePath();
			AlgGenRegUser agru = ConfigurationService.getInstance().getReader().readArchive(file);
			ParametersSelection ps = newTab(file.getAbsolutePath());

			List<EntryBlock> algs = agru.algorithms;
			for (EntryBlock alg : algs) {
				List<Entry> entries = alg.getEntryList();
				ps.getAlgSel().show(alg.getName());
				EntryListPanel enp = ps.getAlgSel().getSelectedEntryList();
				for (Entry entry : entries) {
					EntryFieldPanel efp = enp.getEntryField(entry.key);
					efp.setSelected(true);
					efp.setText(entry.value);
				}
				EntryBlockSelection<EntryBlock> algSel = ps.getAlgSel();
				algSel.add();
			}

			List<EntryBlock> gens = agru.genotypes.get(0);
			for (EntryBlock gen : gens) {
				List<Entry> entries = gen.getEntryList();
				ps.getGenSel().show(gen.getName());
				EntryListPanel enp = ps.getGenSel().getSelectedEntryList();
				for (Entry entry : entries) {
					EntryFieldPanel efp = enp.getEntryField(entry.key);
					efp.setSelected(true);
					efp.setText(entry.value);
				}
				EntryBlockSelection<EntryBlock> genSel = ps.getGenSel();
				genSel.add();
			}

			Registry reg = agru.registry;
			List<Entry> entries = reg.getEntryList();
			EntryListPanel enp = ps.getRegList();
			for (Entry entry : entries) {
				EntryFieldPanel efp = enp.getEntryField(entry.key);
				efp.setSelected(true);
				efp.setText(entry.value);
			}

			ps.getDefinePanel().setParamsPath(absolutePath);
			InfoService.setLastSelectedConfPath(absolutePath);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Error";
			}
			message = message.trim();
			reportError(message.isEmpty() ? "Error" : message);
			LoggerProvider.getLogger().log(e);
		}
	}

	/**
	 * Creates new tab with {@link ParametersSelection} panel.
	 * 
	 * @param tabName
	 *            Name of the new tab
	 * @return Created {@link ParametersSelection} panel
	 */
	protected ParametersSelection newTab(String tabName) {
		ParametersSelection parSel = new ParametersSelection(this);
		tabbedPane.add(tabName, parSel);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
		return parSel;
	}

	/**
	 * Calls parameters dump from ECF executable file.
	 * 
	 * @return
	 */
	protected ParametersList callParDump() {
		TaskMannager tm = new TaskMannager();
		return tm.getInitialECFparams(ecfPath, parDumpPath);
	}

	/**
	 * Initializes menu bar with all main actions.
	 */
	private void initMenuBar() {
		JMenu confMenu = new JMenu("Configuration");
		confMenu.add(actions.get("NewConf"));
		confMenu.add(actions.get("OpenConf"));
		confMenu.add(actions.get("SaveConf"));
		confMenu.add(actions.get("SaveConfAs"));

		JMenu logMenu = new JMenu("Log");
		logMenu.add(actions.get("OpenLog"));
		logMenu.add(actions.get("SaveLog"));
		logMenu.add(actions.get("ResultsFrame"));

		JMenu exeMenu = new JMenu("ECF");
		exeMenu.add(actions.get("ChangeECFExe"));
		exeMenu.add(actions.get("ecfHomePage"));

		menuBar.add(confMenu);
		menuBar.add(logMenu);
		menuBar.add(exeMenu);

		setJMenuBar(menuBar);
	}

	/**
	 * Displays warning dialog with specified message and "Error" title.
	 * 
	 * @param errorMessage
	 *            Message to be shown
	 */
	public void reportError(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Exit method. Defines actions that have to be done before closing the
	 * frame.
	 */
	protected void exit() {
		boolean b = Boolean.parseBoolean(SettingsProvider.getSettings().getValue(SettingsKey.CONFIRM_EXIT));
		if (b) {
			int ret = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Really exit?",
					JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION) {
				exitConfirmed();
			}
		} else {
			exitConfirmed();
		}
	}

	private void exitConfirmed() {
		ResultProgressFrame.disposeInstance();
		dispose();
	}

	/**
	 * All main actions.
	 * 
	 * @return Main actions.
	 */
	public Map<String, Action> getActions() {
		return actions;
	}

	/**
	 * Current ECF executable file path.
	 * 
	 * @return ECF exe path
	 */
	public String getEcfPath() {
		return ecfPath;
	}

	/**
	 * Path to the parameters dump file.
	 * 
	 * @return Path to the parameters dump file
	 */
	public String getParDumpPath() {
		return parDumpPath;
	}

	/**
	 * Object with all parameters from the selected ECF exe.
	 * 
	 * @return {@link hr.fer.zemris.ecf.lab.engine.param.ParametersList} object with all the parameters from the
	 *         current ECF executable file.
	 */
	public ParametersList getParDump() {
		return parDump;
	}
	
	/**
	 * @return Object which main purpose is to display a result of experiment.
	 */
	public IResultDisplay getResultDisplay() {
		return resultDisplay;
	}

	/**
	 * Runs this application.
	 * 
	 * @param args
	 *            Not used
	 */
	public static void main(String[] args) {
		Settings settings = new PropertyFileSettings(CONFIGURATION_FILE);
		SettingsProvider.setSettings(settings);

		Logger log = new FileLogger(settings.getValue(SettingsKey.LOG_FILE_PATH));
		LoggerProvider.setLogger(log);

		ConfigurationService.getInstance().setReader(new XmlConfigurationReader());
		ConfigurationService.getInstance().setWriter(new XmlConfigurationWriter());

		InfoService.setLastSelectedConfPath(new File("").getAbsolutePath());

		Thread.setDefaultUncaughtExceptionHandler(new EDTExceptionHandler(log));
		System.setProperty("sun.awt.exception.handler", EDTExceptionHandler.class.getName());

		SwingUtilities.invokeLater(() -> startGUIApp());
	}

	/**
	 * Starts GUI.
	 */
	protected static void startGUIApp() {
		new ECFLab();
	}

	/**
	 * Sets {@link javax.swing.LookAndFeel} to the system or to the java look.
	 * 
	 * @param system
	 *            True for system look, false for java look
	 */
	protected void setLookAndFeel(boolean system) {
		String newLookAndFeel = system ? UIManager.getSystemLookAndFeelClassName() : UIManager
				.getCrossPlatformLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(newLookAndFeel);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Class for handling EDT exceptions.
	 * 
	 * @author Domagoj Stanković
	 * @version 1.0
	 */
	public static class EDTExceptionHandler implements Thread.UncaughtExceptionHandler {

		private Logger log;

		public EDTExceptionHandler(Logger log) {
			super();
			this.log = log;
		}

		public void handle(Throwable thrown) {
			log.log(getStackTraceString(thrown.getStackTrace()));
		}

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			e.printStackTrace(ps);
			try {
				String message = baos.toString(StandardCharsets.UTF_8.name());
				log.log(message);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}

		private String getStackTraceString(StackTraceElement[] stackTrace) {
			StringBuilder sb = new StringBuilder();
			for (StackTraceElement ste : stackTrace) {
				sb.append(ste.toString() + "\n");
			}
			return sb.toString();
		}

	}

}
