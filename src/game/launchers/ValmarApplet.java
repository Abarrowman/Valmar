package game.launchers;

import game.ValmarPanel;

import java.applet.Applet;
import java.awt.Dimension;

//documented

/**
 * ValmarApplet is an applet containing a ValmarPanel.
 * @author Adam
 */
public class ValmarApplet extends Applet {
	private static final long serialVersionUID = 1L;
	/**
	 * Inializes the ValmarApplet.
	 */
	public void init()
    {
       resize(500, 450);
       Dimension dim = getSize();
       setLayout(null);
       add(new ValmarPanel(createImage(dim.width, dim.height), getCodeBase()));
    }
}
