package game;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

//documented

/**
 * ValmarListener is a class that abstracts keyboard and mouse input.
 * @author Adam
 */
public class ValmarListener implements MouseListener, MouseMotionListener, KeyListener {

	private Vector<Boolean> keys;
	private Vector<Boolean> pressing;
	
	private boolean mousePressing=false;
	private boolean mouseDown=false;
	private int mouseX=0;
	private int mouseY=0;
	
	//Makes a new ValmarListener.
	public ValmarListener()
	{
		keys=new Vector<Boolean>();
		pressing=new Vector<Boolean>();
		for(int n=0;n<256;n++)
		{
			pressing.add(false);
			keys.add(false);
		}
	}
	
	//keyboard
	/**
	 * Called when a key is pressed.
	 */
	public void keyPressed(KeyEvent ke) {
		keys.set(ke.getKeyCode(), true);
		pressing.set(ke.getKeyCode(), true);
	}
	/**
	 * Called when a key is released.
	 */
	public void keyReleased(KeyEvent ke) {
		keys.set(ke.getKeyCode(), false);
		pressing.set(ke.getKeyCode(), false);
	}
	/**
	 * Called when a key is typed.
	 */
	public void keyTyped(KeyEvent ke) {
		//for text input
	}
	/**
	 * Returns a boolean stating if a key is down.
	 */
	public boolean isKeyDown(int keyCode)
	{
		return keys.get(keyCode);
	}
	
	/**
	 * Returns a boolean is a key has just been pressed.
	 */
	public boolean isKeyPressed(int keyCode)
	{
		boolean val=keys.get(keyCode)&&pressing.get(keyCode);
		pressing.set(keyCode, false);
		return val;
	}
	
	//mouse
	/**
	 * Called when the mouse is dragged.
	 */
	public void mouseDragged(MouseEvent me) {	
		mouseMoved(me);
	}
	/**
	 * Called when the mouse moves.
	 */
	public void mouseMoved(MouseEvent me) {
		mouseX=me.getX();
		mouseY=me.getY();
	}

	/**
	 * Called when the mouse is clicked.
	 */
	public void mouseClicked(MouseEvent me) {	
		//process the click event
	}

	/**
	 * Called when the mouse enters the object.
	 */
	public void mouseEntered(MouseEvent me) {	
	}

	/**
	 * Called when the mouse exits the object.
	 */
	public void mouseExited(MouseEvent me) {
		//mouse is out
	}

	/**
	 * Called when the mouse is pressed.
	 */
	public void mousePressed(MouseEvent me) {
		mouseDown=true;
		mousePressing=true;
	}

	/**
	 * Called when the mouse is released.
	 */
	public void mouseReleased(MouseEvent me) {	
		mouseDown=false;
		mousePressing=false;
	}
	
	/**
	 * Returns the coordinates of the mouse.
	 */
	public Point getMouseCord(){
		return new Point(mouseX, mouseY);
	}
	
	/**
	 * Returns the mouse's x coordinate.
	 */
	public int getMouseX(){
		return mouseX;
	}
	/**
	 * Returns the mouse's Y coordinate.
	 */
	public int getMouseY(){
		return mouseY;
	}
	/**
	 * Returns a boolean stating if the mouse is down.
	 */
	public boolean isMouseDown(){
		return mouseDown;
	}
	/**
	 * Returns a boolean stating if the mouse has just been pressed.
	 */
	public boolean isMousePressing(){
		boolean val=mouseDown&&mousePressing;
		mousePressing=false;
		return val;
	}

	/**
	 * Confirms that events have passed.
	 */
	public void tick() {
		mousePressing=false;
		for(int n=0;n<256;n++)
		{
			pressing.set(n, false);
		}
		
	}

}
