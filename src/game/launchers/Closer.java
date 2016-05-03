package game.launchers;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//documented

/**
 * Closer is a WindowAdapter that ends the program when the window is closed.
 * @author Adam
 */
public class Closer extends WindowAdapter {
	/**
	 * Ends the program when the window is closed.
	 */
    public void windowClosing (WindowEvent event) {
        System.exit (0);
    }
}