package hr.fer.zemris.ecf.lab.view.layout.impl;

import hr.fer.zemris.ecf.lab.engine.param.EntryBlock;
import hr.fer.zemris.ecf.lab.view.layout.EntryFieldDisplay;
import hr.fer.zemris.ecf.lab.view.layout.EntryListPanel;
import hr.fer.zemris.ecf.lab.view.layout.IFieldListener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Panel for displaying added entry and button for removing that entry. This
 * object displays entry in a new {@link javax.swing.JFrame}.
 * 
 * @author Domagoj
 * 
 * @param <T>
 *            {@link EntryBlock} instance
 */
public class FrameDisplay<T extends EntryBlock> extends EntryFieldDisplay<T> {

	private static final long serialVersionUID = 1L;

	public FrameDisplay(IFieldListener listener, T block, EntryListPanel blockDisplay) {
		super(listener, block, blockDisplay);
	}

	@Override
	protected Action displayAction() {
		return new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFrame frame = new JFrame(block.getName());
				frame.setLayout(new BorderLayout());
				frame.setLocation(200, 300);
				frame.add(blockDisplay, BorderLayout.CENTER);
				JButton button = new JButton(new AbstractAction() {

					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e) {
						frame.dispose();
					}
				});
				button.setText("Done");
				frame.add(button, BorderLayout.SOUTH);
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		};
	}

}
