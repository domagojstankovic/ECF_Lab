package hr.fer.zemris.ecf.lab.view.layout;

import hr.fer.zemris.ecf.lab.view.display.TextButtonPanel;

import java.awt.*;

/**
 * Created by Domagoj on 04/06/15.
 */
public class TextButtonListFrame extends ListFrame {

    public TextButtonListFrame(String title) {
        super(title);
    }

    public TextButtonPanel createComp(String lblText) {
        TextButtonPanel jpp = new TextButtonPanel(lblText);
        jpp.setMaximumSize(new Dimension(Integer.MAX_VALUE, jpp.getPreferredSize().height));
        add(jpp);
        return jpp;
    }
}
