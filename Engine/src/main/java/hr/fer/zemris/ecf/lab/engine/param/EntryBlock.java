package hr.fer.zemris.ecf.lab.engine.param;

import java.util.ArrayList;
import java.util.List;

public class EntryBlock {
	
	protected String name;
	protected List<Entry> entryList;
	
	public EntryBlock() {
		name = "";
		entryList = new ArrayList<>();
	}
	
	public EntryBlock(String name) {
		super();
		this.name = name;
		entryList = new ArrayList<>();
	}

	public EntryBlock(String name, List<Entry> entryList) {
		super();
		this.name = name;
		this.entryList = entryList;
	}

	/**
	 * Getter for the algorithms/genotype name.
	 * @return algorithms/genotype name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter for the algorithms/genotype name.
	 * @param name algorithms/genotype name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter for the entry list reference.
	 * @return reference to the entry list for this algorithms/genotype.
	 */
	public List<Entry> getEntryList() {
		return entryList;
	}
	
	/**
	 * Setter for the entry list reference.
	 * @param entryList pointer to the entry list for this algorithms/genotype.
	 */
	public void setEntryList(List<Entry> entryList) {
		this.entryList = entryList;
	}
	
	/**
	 * Returns entry at the specified index.
	 * @param index specified index
	 * @return entry at the specified index
	 */
	public Entry getEntryAt(int index) {
		return entryList.get(index);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
