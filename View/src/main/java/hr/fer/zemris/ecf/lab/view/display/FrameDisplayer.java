package hr.fer.zemris.ecf.lab.view.display;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

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
	public void displayLog(LogModel log) throws Exception {
		List<Generation> generations = log.getGenerations();
		String solution = log.getHallOfFame().get(0).genotypes.get(0).toString();
		int size = generations.size();
		XYSeries sMinFit = new XYSeries("Min Fit");
		XYSeries sMaxFit = new XYSeries("Max Fit");
		XYSeries sAvgFit = new XYSeries("Avg Fit");
		for (int i = 0; i < size; i++) {
			Generation generation = generations.get(i);
			if (generation.population != null) {
				sMinFit.add(Integer.valueOf(generation.id), Double.valueOf(generation.population.minFitness));
				sMaxFit.add(Integer.valueOf(generation.id), Double.valueOf(generation.population.maxFitness));
				sAvgFit.add(Integer.valueOf(generation.id), Double.valueOf(generation.population.avgFitness));
			} else {
				sMinFit.add(Integer.valueOf(generation.id), Double.valueOf(generation.demes.get(0).minFitness));
				sMaxFit.add(Integer.valueOf(generation.id), Double.valueOf(generation.demes.get(0).maxFitness));
				sAvgFit.add(Integer.valueOf(generation.id), Double.valueOf(generation.demes.get(0).avgFitness));
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
