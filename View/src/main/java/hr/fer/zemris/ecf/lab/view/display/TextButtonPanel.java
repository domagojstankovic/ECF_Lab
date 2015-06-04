package hr.fer.zemris.ecf.lab.view.display;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Domagoj on 03/05/15.
 */
public class TextButtonPanel extends JPanel {

    private JLabel label;
    private JButton button;

    public TextButtonPanel(String text) {
        super();
        setLayout(new BorderLayout());
        label = new JLabel(text);
        button = new JButton();
        add(button, BorderLayout.EAST);
        add(label, BorderLayout.CENTER);
    }

    public JLabel getLabel() {
        return label;
    }

    public JButton getButton() {
        return button;
    }

    public void setLabelText(String text) {
        label.setText(text);
    }

    public void setButtonText(String text) {
        button.setText(text);
    }
}
