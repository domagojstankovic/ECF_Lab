package hr.fer.zemris.ecf.lab.view.chart;

import javax.swing.*;
import java.awt.*;

/**
 * Frame for displaying chart with results.
 *
 * @author Domagoj Stanković
 * @version 1.0
 */
public class ChartFrame extends JFrame {

  private static final long serialVersionUID = 1L;

  public ChartFrame(LineChartPanel panel, String solution) {
    super();
    setLayout(new BorderLayout());
    setLocation(400, 200);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    add(panel, BorderLayout.CENTER);
    JTextArea textArea = new JTextArea(solution != null ? solution.trim() : "");
    textArea.setEditable(false);
    add(textArea, BorderLayout.SOUTH);
    pack();
  }
}
