package hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.task;

import hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.console.Console;
import hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.console.Job;

import java.util.concurrent.Callable;

/**
 * This class represents one thread task a.k.a. one ECF task. The thread that gets this is stopped until the ECF finishes this specific task.
 * The task for the ECF is given by the {@link Job}.
 * It uses {@link Console} to start ECF task.
 * @version 1.0
 *
 */
public class Task implements Callable<Void> {
	
	private Job job;
	private Console console;

	/**
	 * Constructor it gets the specific {@link Job} and a specific {@link Console} for current pc.
	 * @param job given job
	 * @param console console type determined by {@link Console} implementation of specific pc this task will bi ran on.
	 */
	public Task(Job job, Console console){
		this.job = job;
		this.console = console;
	}


	@Override
	public Void call() throws Exception {
		console.execute(job);
		job.isDone = true;
		job.finished();
		return null;
	}

}
