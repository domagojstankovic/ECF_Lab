package hr.fer.zemris.ecf.lab.view.layout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Domagoj on 04/06/15.
 */
public class ListFrame extends JFrame {

    protected JPanel panel = null;

    public ListFrame(String title) {
        super();
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLocation(300, 200);
        setSize(400, 350);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        getContentPane().add(new JScrollPane(panel));
    }

    @Override
    public Component add(Component comp) {
        return panel.add(comp);
    }
}
