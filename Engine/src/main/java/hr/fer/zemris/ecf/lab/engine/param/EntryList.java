package hr.fer.zemris.ecf.lab.engine.param;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domagoj on 12/04/15.
 */
public class EntryList {

    protected List<Entry> entryList;

    public EntryList() {
        entryList = new ArrayList<>();
    }

    public EntryList(List<Entry> entryList) {
        this.entryList = entryList;
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
        StringBuilder sb = new StringBuilder();
        for (Entry entry : entryList) {
            sb.append(entry.toString() + "\n");
        }
        return sb.toString();
    }
}
