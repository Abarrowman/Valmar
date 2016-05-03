package game;

//documented

/**
 * ValmarButtonListener is a simple interface for a object that responds to ValmarButtons being activated.
 * @author Adam
 */
public interface ValmarButtonListener {
	/**
	 * Runs the specified event.
	 */
	public void runEvent(String command, ValmarButton button);
}
