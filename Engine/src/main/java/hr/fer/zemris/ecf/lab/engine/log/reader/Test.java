package hr.fer.zemris.ecf.lab.engine.log.reader;

import hr.fer.zemris.ecf.lab.engine.log.Generation;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;

import java.io.FileInputStream;
import java.util.List;

public class Test {
	
	public static void main(String[] args) {
		LogReader off = LogReaderProvider.getReader();
		try {
			LogModel logModel = off.read(new FileInputStream("test/log.txt"));
			List<Generation> gen = logModel.getGenerations();
			System.out.println(gen.get(gen.size()-1).population.stats.avg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
