package hr.fer.zemris.ecf.lab.view.layout;

/**
 * Object that executes an action when something happens with the {@link EntryFieldDisplay}.
 * @author Domagoj
 *
 */
public interface IFieldListener {

	/**
	 * Removes a {@link EntryBlock} from the configuration file.
	 * @param field {@link EntryFieldDisplay} to be removed from the configuration file
	 */
	public void removeField(EntryFieldDisplay<?> field);
	
}
