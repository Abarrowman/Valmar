package game.launchers;

import game.ValmarPanel;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Panel;

//documented

/**
 * ValmarFrame is a frame containg a ValmarPanel.
 * @author Adam
 */
public class ValmarFrame extends Frame {
	private static final long serialVersionUID = 1L;
	/**
	 * Makes an new ValmarFrame.
	 */
	public ValmarFrame(){
		setTitle("Valmar");
	    setSize(500, 450);
	    addWindowListener (new Closer());
	    setVisible(true);
	    setResizable(false);
	    //add the game panel
	    Dimension dim = getSize();
	    Insets in=getInsets();
	    setSize(dim.width+in.right+in.left, dim.height+in.top+in.bottom);
	    Panel pan=new ValmarPanel(createImage(dim.width, dim.height),null);
	    pan.setBounds(in.left, in.top, dim.width, dim.height);
	    add(pan);
	}
}
