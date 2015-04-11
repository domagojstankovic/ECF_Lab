package hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.console;

public interface Subject {
	
	void setObserver(Observer observer);
	
	void removeObserver();
	
	void finished() throws Exception;
	
	String getMessage();
	
}
