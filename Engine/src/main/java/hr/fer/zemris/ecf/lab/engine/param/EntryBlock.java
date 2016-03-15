package hr.fer.zemris.ecf.lab.engine.param;

import java.util.ArrayList;
import java.util.List;

public class EntryBlock extends EntryList {

	protected String name;

	public EntryBlock() {
		super();
		name = "";
	}

	public EntryBlock(String name) {
		super();
		this.name = name;
	}

	public EntryBlock(String name, List<Entry> entryList) {
		super(entryList);
		this.name = name;
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
		return name + "\n" + entryList.toString();
	}

	public EntryBlock copy() {
		List<Entry> entries = new ArrayList<>(entryList.size());
		for (Entry entry : entryList) {
			entries.add(entry.copy());
		}
		return new EntryBlock(name, entries);
	}
}
