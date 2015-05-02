package hr.fer.zemris.ecf.lab.view.display;

import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.console.JobObserver;
import hr.fer.zemris.ecf.lab.engine.console.ProcessOutput;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.model.logger.LoggerProvider;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.*;

/**
 * Frame that displays list of all results that have been generated. This frame
 * is singleton.
 * 
 * @author Domagoj
 * 
 */
public class ResultProgressFrame extends JFrame implements LogDisplayer, JobObserver {

	private static final long serialVersionUID = 1L;

	private JPanel pan = null;

	public ResultProgressFrame() {
		super();
		setTitle("Results");
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setLocation(300, 200);
		setSize(400, 350);
		pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		getContentPane().add(new JScrollPane(pan));
	}

	@Override
	public Component add(Component comp) {
		return pan.add(comp);
	}

	@Override
	public void remove(Component comp) {
		pan.remove(comp);
		getContentPane().validate();
		getContentPane().repaint();
	}

	@Override
	public void displayLog(LogModel log) {
		addComp(log);
		setVisible(true);
	}

	private void addComp(final LogModel log) {
		JButton btn = new JButton(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new FrameDisplayer().displayLog(log);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(ResultProgressFrame.this, e1.getMessage(), "An error occured while reading log file",
							JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
					LoggerProvider.getLogger().log(e1);
				}
			}
		});
		btn.setText("TODO");
		add(btn);
	}

	@Override
	public void jobFinished(Job job, ProcessOutput output) {

	}

	@Override
	public void jobFailed(Job job) {

	}
}
