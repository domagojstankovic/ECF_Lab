package hr.fer.zemris.ecf.lab.view.display;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import hr.fer.zemris.ecf.lab.view.chart.ChartFrame;
import hr.fer.zemris.ecf.lab.view.chart.LineChartPanel;
import hr.fer.zemris.ecf.lab.engine.log.Generation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Frame that displays result in a form of chart.
 * 
 * @author Domagoj
 *
 */
public class FrameDisplayer implements LogDisplayer {

	@Override
	public void displayLog(LogModel log) {
		if (log.errorOccured()) {
			// error occured
			String errorMessage = log.getError();
			JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.INFORMATION_MESSAGE);
		} else {
			// no errors
			List<Generation> generations = log.getGenerations();
			String solution = log.getHallOfFame();
			int size = generations.size();
			XYSeries sMinFit = new XYSeries("Min Fit");
			XYSeries sMaxFit = new XYSeries("Max Fit");
			XYSeries sAvgFit = new XYSeries("Avg Fit");
			for (int i = 0; i < size; i++) {
				Generation generation = generations.get(i);
				if (generation.population != null) {
					sMinFit.add(Integer.valueOf(generation.id), Double.valueOf(generation.population.stats.min));
					sMaxFit.add(Integer.valueOf(generation.id), Double.valueOf(generation.population.stats.max));
					sAvgFit.add(Integer.valueOf(generation.id), Double.valueOf(generation.population.stats.avg));
				} else {
					sMinFit.add(Integer.valueOf(generation.id), Double.valueOf(generation.demes.get(0).stats.min));
					sMaxFit.add(Integer.valueOf(generation.id), Double.valueOf(generation.demes.get(0).stats.max));
					sAvgFit.add(Integer.valueOf(generation.id), Double.valueOf(generation.demes.get(0).stats.avg));
				}
			}
			XYSeriesCollection coll = new XYSeriesCollection();
			coll.addSeries(sMinFit);
			coll.addSeries(sMaxFit);
			coll.addSeries(sAvgFit);
			List<Color> colors = new ArrayList<>(3);
			colors.add(Color.BLACK);
			colors.add(Color.RED);
			colors.add(Color.BLUE);
			String chartTitle = "Log";
			String xAxisLabel = "Generation";
			String yAxisLabel = "Fitness";
			LineChartPanel lineChart = new LineChartPanel(coll, colors, chartTitle, xAxisLabel, yAxisLabel, true, false);
			JFrame frame = new ChartFrame(lineChart, solution);
			frame.setVisible(true);
		}
	}

}
