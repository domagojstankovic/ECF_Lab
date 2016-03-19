package hr.fer.zemris.ecf.lab.view.layout;

import hr.fer.zemris.ecf.lab.engine.param.Entry;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Field for defining existence of specified parameter (check box), parameter
 * name (label) and parameter value (text field).
 * 
 * @author Domagoj Stanković
 * @version 1.0
 */
public class EntryFieldPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int TEXT_FIELD_WIDTH = 130;

	private JCheckBox checkBox;
	private JLabel label;
	private JPanel textFieldsPanel;
	private List<JTextField> textFields;
	private boolean mandatory = false;
	private String description;

	/**
	 * @param label Entry name label
	 * @param textFields Text fields to be added
	 * @param description Entry description
	 * @param @param tooltipText Parameter description
	 */
	public EntryFieldPanel(JLabel label, List<JTextField> textFields, String description) {
		super();
		this.label = label;
		this.textFields = new ArrayList<>(textFields.size());
		this.description = description;

		setup();
		for (JTextField textField : textFields) {
			addTextField(textField);
		}
	}

	/**
	 * @param entry Entry to be displayed
	 */
	public EntryFieldPanel(Entry entry) {
		super();
		this.label = new JLabel(entry.key);
		this.textFields = new ArrayList<>(1);
		this.description = entry.desc;

		setup();
		addTextField(entry.value);
		boolean b = entry.isMandatory();
		if (b) {
			setMandatory();
		}
	}

	private void setup() {
		textFieldsPanel = new JPanel() {
			@Override
			public Dimension getPreferredSize() {
				int height = 0;
				for (Component comp : this.getComponents()) {
					height += comp.getPreferredSize().getHeight();
				}
				return new Dimension(TEXT_FIELD_WIDTH, height);
			}

			@Override
			public Dimension getSize() {
				Dimension superSize = super.getSize();
				int height = 0;
				for (Component comp : this.getComponents()) {
					height += comp.getPreferredSize().getHeight();
				}
				return new Dimension((int)superSize.getWidth(), height);
			}
		};
		textFieldsPanel.setLayout(new BoxLayout(textFieldsPanel, BoxLayout.Y_AXIS));
		label.setToolTipText(description);

		Dimension lblDim = new Dimension(TEXT_FIELD_WIDTH, 20);

		label.setPreferredSize(lblDim);
		label.setMaximumSize(lblDim);
		label.setMinimumSize(lblDim);

		textFieldsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		textFieldsPanel.setMinimumSize(lblDim);

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		checkBox = new JCheckBox();
		checkBox.setSelected(false);

		add(checkBox);
		add(label);
		add(textFieldsPanel);

		this.label.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (e.isPopupTrigger()) {
					doPop(e);
				}
			}

			private void doPop(MouseEvent e) {
				List<AbstractAction> actions = new LinkedList<>();
				actions.add(new AbstractAction("Add") {
					@Override
					public void actionPerformed(ActionEvent e) {
						// Add action
						addTextField("");
					}
				});

				PopUpMenu menu = new PopUpMenu(actions);
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if (textFieldsPanel != null) {
			textFieldsPanel.setBackground(bg);
		}
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension((int)super.getMaximumSize().getWidth(), (int)getMinimumSize().getHeight());
	}

	@Override
	public Dimension getMinimumSize() {
		int maxHeight = 0;
		for (Component comp : this.getComponents()) {
			int currH = (int)comp.getPreferredSize().getHeight();
			if (currH > maxHeight) {
				maxHeight = currH;
			}
		}
		return new Dimension((int) super.getSize().getWidth(), maxHeight);
	}

	private void addTextField(String value) {
		addTextField(new JTextField(value));
	}

	private void addTextField(JTextField textField) {
		textFields.add(textField);
		textFieldsPanel.add(textField);
		Dimension dim = new Dimension(TEXT_FIELD_WIDTH, 20);
		textField.setMinimumSize(dim);
		textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		textField.setPreferredSize(dim);

		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (e.isPopupTrigger()) {
					doPop(e, textField, textFields);
				}
			}

			private void doPop(MouseEvent e, JTextField textField, List<JTextField> textFields) {
				if (textFields.size() > 1) {
					List<AbstractAction> actions = new LinkedList<>();
					actions.add(new AbstractAction("Remove") {
						@Override
						public void actionPerformed(ActionEvent e) {
							// Remove action
							Container parent = textField.getParent();
							parent.remove(textField);
							textFields.remove(textField);
							parent.revalidate();
						}
					});

					PopUpMenu menu = new PopUpMenu(actions);
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});

		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				textUpdated();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				textUpdated();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				textUpdated();
			}
		});

		revalidate();
	}

	private void textUpdated() {
		checkBox.setSelected(true);
	}

	/**
	 * @return Text from the text field
	 */
	public List<String> getText() {
		List<String> list = new ArrayList<>(textFields.size());
		for (JTextField textField : textFields) {
			list.add(textField.getText());
		}
		return list;
	}

	/**
	 * @param text Text to be set in the text field
	 */
	public void setText(String text) {
		this.textFields.get(0).setText(text);
	}

	/**
	 * @return Parameter name
	 */
	public String getLabelText() {
		return label.getText();
	}

	/**
	 * @return <code>true</code> if parameter is chosen (check box is selected), <code>false</code> otherwise
	 */
	public boolean isSelected() {
		return checkBox.isSelected();
	}

	/**
	 * @param selected <code>true</code> if parameter should be selected, <code>false</code> otherwise
	 */
	public void setSelected(boolean selected) {
		checkBox.setSelected(selected);
	}
	
	public String getDescription() {
		return label.getToolTipText();
	}
	
	public boolean isMandatory() {
		return mandatory;
	}
	
	public void setMandatory() {
		setSelected(true);
		checkBox.setEnabled(false);
		mandatory = true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(label.getText());
		sb.append(": ");
		for (JTextField textField : textFields) {
			sb.append(textField.getText()).append(" ");
		}
		return sb.toString();
	}
	
	public EntryFieldPanel copy() {
		List<JTextField> list = new ArrayList<>(textFields.size());
		for (JTextField field : textFields) {
			list.add(new JTextField(field.getText()));
		}
		EntryFieldPanel efp = new EntryFieldPanel(new JLabel(getLabelText()), list, getDescription());
		efp.setSelected(isSelected());
		if (isMandatory()) {
			efp.setMandatory();
		}
		return efp;
	}

	private static class PopUpMenu extends JPopupMenu {
		public PopUpMenu(List<AbstractAction> actions) {
			super();
			for (AbstractAction action : actions) {
				JMenuItem item = new JMenuItem(action);
				add(item);
			}
		}
	}
}
