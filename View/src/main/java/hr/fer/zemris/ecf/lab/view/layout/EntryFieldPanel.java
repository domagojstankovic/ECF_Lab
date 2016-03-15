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

	private JCheckBox checkBox;
	private JLabel label;
	private JPanel textFieldsPanel;
	private List<JTextField> textFields;
	private boolean mandatory = false;
	private String description;

	/**
	 * @param label Parameter name
	 * @param textFields Parameter values
	 * @param @param tooltipText Parameter description
	 */
	public EntryFieldPanel(JLabel label, List<JTextField> textFields, String description) {
		super();
		this.label = label;
		this.textFields = textFields;
		this.description = description;

		setup();
	}

	/**
	 * @param entry Entry to be displayed
	 */
	public EntryFieldPanel(Entry entry) {
		super();
		this.label = new JLabel(entry.key);
		this.textFields = new ArrayList<>(1);
		this.textFields.add(new JTextField(entry.value));
		this.description = entry.desc;

		setup();
		boolean b = entry.isMandatory();
		if (b) {
			setMandatory();
		}
	}

	private void setup() {
		textFieldsPanel = new JPanel();
		textFieldsPanel.setLayout(new BoxLayout(textFieldsPanel, BoxLayout.Y_AXIS));
		label.setToolTipText(description);

		Dimension lblDim = new Dimension(130, 50);
		Dimension txtDim = new Dimension(Integer.MAX_VALUE, 50);

		label.setPreferredSize(lblDim);
		label.setMaximumSize(lblDim);
		label.setMinimumSize(lblDim);

		textFieldsPanel.setPreferredSize(lblDim);
		textFieldsPanel.setMaximumSize(txtDim);
		textFieldsPanel.setMinimumSize(lblDim);

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		checkBox = new JCheckBox();
		checkBox.setSelected(false);

		for (JTextField field : this.textFields) {
			field.getDocument().addDocumentListener(new DocumentListener() {
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
		}

		add(checkBox);
		add(label);
		for (JTextField textField : textFields) {
			textFieldsPanel.add(textField);
		}
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
						addTextField();
					}
				});

				PopUpMenu menu = new PopUpMenu(actions);
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	private void addTextField() {
		JTextField textField = new JTextField("");
		textFields.add(textField);
		textFieldsPanel.add(textField);
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
