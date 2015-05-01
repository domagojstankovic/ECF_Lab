package hr.fer.zemris.ecf.lab.view.display;

import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.model.logger.LoggerProvider;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Object that handles incoming requests for result display. Method
 * displayLog is usually invoked when ECF Lab has generated a new log file.
 * 
 * @author Domagoj
 * 
 */
public class ResultProgressFrameDisplayer implements LogDisplayer {

	private ResultProgressFrame frame = null;

	public ResultProgressFrameDisplayer() {
		super();
	}

	@Override
	public void displayLog(LogModel log) throws Exception {
		if (frame == null) {
			frame = ResultProgressFrame.getInstance();
		}
		addComp(log);
		frame.setVisible(true);
	}

	private void addComp(final LogModel log) {
		JButton openButton = new JButton(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new FrameDisplayer().displayLog(log);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getMessage(), "An error occured while reading log file",
							JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
					LoggerProvider.getLogger().log(e1);
				}
			}
		});
		openButton.setText("Open");
		JButton closeButton = new JButton();
		final OpenResultPanel comp = new OpenResultPanel("TODO", openButton, closeButton);
		closeButton.setAction(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.remove(comp);
				frame.repaint();
			}
		});
		closeButton.setText("Close");
		frame.add(comp);
	}

}
