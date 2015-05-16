package hr.fer.zemris.ecf.lab.view.layout;

import hr.fer.zemris.ecf.lab.engine.param.EntryBlock;
import hr.fer.zemris.ecf.lab.model.logger.LoggerProvider;
import hr.fer.zemris.ecf.lab.model.settings.SettingsKey;
import hr.fer.zemris.ecf.lab.model.settings.SettingsProvider;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Abstact panel for displaying added entry and button for removing that entry.
 * 
 * @author Domagoj
 * 
 * @param <T>
 *            {@link EntryBlock}
 */
public abstract class EntryFieldDisplay<T extends EntryBlock> extends JPanel {

	private static final long serialVersionUID = 1L;

	protected JButton display;
	protected JButton delete;
	protected T block;
	protected EntryListPanel blockDisplay;

	public EntryFieldDisplay(final IFieldListener listener, T block, EntryListPanel blockDisplay) {
		this.block = block;
		this.blockDisplay = blockDisplay;
		setLayout(new BorderLayout());
		Action displayAction = displayAction();
		display = new JButton(displayAction);
		display.setText(block.getName());

		Action deleteAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				listener.removeField(EntryFieldDisplay.this);
			}
		};
		delete = new JButton(deleteAction);

		String imgPath = SettingsProvider.getSettings().getValue(SettingsKey.ICON_ERASE_PATH);
		try {
			Image image = ImageIO.read(ClassLoader.getSystemClassLoader().getResourceAsStream(imgPath));
			ImageIcon icon = new ImageIcon(image);
			delete.setIcon(icon);
		} catch (IOException e) {
			e.printStackTrace();
			LoggerProvider.getLogger().log(e);
		}

		add(display, BorderLayout.CENTER);
		add(delete, BorderLayout.EAST);
	}

	public T getBlock() {
		return block;
	}

	public EntryListPanel getBlockDisplay() {
		return blockDisplay;
	}

	protected abstract Action displayAction();

}
