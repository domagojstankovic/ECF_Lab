package hr.fer.zemris.ecf.lab.engine.console;

public interface Subject {
	
	void setObserver(Observer observer);
	
	void removeObserver();
	
	void finished() throws Exception;
	
	String getMessage();
	
}
