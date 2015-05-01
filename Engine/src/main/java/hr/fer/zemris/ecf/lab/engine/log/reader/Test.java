package hr.fer.zemris.ecf.lab.engine.log.reader;

import hr.fer.zemris.ecf.lab.engine.log.Generation;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;

import java.util.List;

public class Test {
	
	public static void main(String[] args) {
		OfflineReading off = new OfflineReading();
		try {
			LogModel logModel = off.read("test/log.txt");
			List<Generation> gen = logModel.getGenerations();
			System.out.println(gen.get(gen.size()-1).population.avgFitness);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
