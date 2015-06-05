package hr.fer.zemris.ecf.lab.view.layout;

import hr.fer.zemris.ecf.lab.engine.param.Entry;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
	private JTextField text;
	private boolean mandatory = false;

	/**
	 * @param label Parameter name
	 * @param text Parameter value
	 * @param tooltipText Parameter description
	 */
	public EntryFieldPanel(JLabel label, JTextField text, String tooltipText) {
		this(label, text, BoxLayout.X_AXIS, tooltipText);
	}

	/**
	 * @param label Parameter name
	 * @param text Parameter value
	 * @param axis List to be laid horizontally {@link javax.swing.BoxLayout}.X_AXIS or vertically {@link javax.swing.BoxLayout}.Y_AXIS
	 */
	public EntryFieldPanel(JLabel label, JTextField text, int axis) {
		super();
		this.label = label;
		this.text = text;

		Dimension lblDim = new Dimension(130, 20);
		Dimension txtDim = new Dimension(Integer.MAX_VALUE, 20);

		label.setSize(lblDim);
		label.setPreferredSize(lblDim);
		label.setMaximumSize(lblDim);
		label.setMinimumSize(lblDim);

		text.setSize(lblDim);
		text.setPreferredSize(lblDim);
		text.setMaximumSize(txtDim);
		text.setMinimumSize(lblDim);

		setLayout(new BoxLayout(this, axis));
		checkBox = new JCheckBox();
		checkBox.setSelected(false);

		text.getDocument().addDocumentListener(new DocumentListener() {
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

		add(checkBox);
		add(label);
		add(text);
	}

	private void textUpdated() {
		checkBox.setSelected(true);
	}

	/**
	 * @param entry Entry to be displayed
	 */
	public EntryFieldPanel(Entry entry) {
		this(new JLabel(entry.key), new JTextField(entry.value), entry.desc);
		boolean b = entry.isMandatory();
		if (b) {
			setMandatory();
		}
	}

	/**
	 * @param label Parameter name
	 * @param text Parameter value
	 * @param axis List to be laid horizontally {@link javax.swing.BoxLayout}.X_AXIS or vertically {@link javax.swing.BoxLayout}.Y_AXIS
	 * @param tooltipText Parameter description
	 */
	public EntryFieldPanel(JLabel label, JTextField text, int axis, String tooltipText) {
		this(label, text, axis);
		label.setToolTipText(tooltipText);
	}

	/**
	 * @return Text from the text field
	 */
	public String getText() {
		return text.getText();
	}

	/**
	 * @param text Text to be set in the text field
	 */
	public void setText(String text) {
		this.text.setText(text);
	}

	/**
	 * @return Parameter name
	 */
	public String getLabelText() {
		return label.getText();
	}

	/**
	 * @param text Name of the parameter
	 */
	public void setLabelText(String text) {
		label.setText(text);
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
		return text.getToolTipText();
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
		return label.getText() + ": " + text.getText();
	}
	
	public EntryFieldPanel copy() {
		EntryFieldPanel efp = new EntryFieldPanel(new JLabel(getLabelText()), new JTextField(getText()), getDescription());
		efp.setSelected(isSelected());
		if (isMandatory()) {
			efp.setMandatory();
		}
		return efp;
	}

}
