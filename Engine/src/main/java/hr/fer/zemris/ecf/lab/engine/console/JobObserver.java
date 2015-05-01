package hr.fer.zemris.ecf.lab.engine.console;

public interface JobObserver {
	
	void jobFinished(Job job, ProcessOutput output);

	void jobFailed(Job job);
	
}
