package hr.fer.zemris.ecf.lab.engine.console;

public interface JobObserver {

	void jobStarted(Job job);
	
	void jobFinished(Job job, ProcessOutput output);

	void jobFailed(Job job);
	
}
