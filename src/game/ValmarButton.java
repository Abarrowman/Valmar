package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

//documented

/**
 * ValmarButton is a simple button widget.
 * @author Adam
 */
public class ValmarButton {
	/**
	 * The space the ValmarButton's bounds.
	 */
	public Rectangle rectangle;
	/**
	 * The background of the ValmarButton rendered when the mouse is out of the ValmarButton's bounds.
	 */
	public String offImage="images/ui/buttonback.png";
	/**
	 * The background of the ValmarButton rendered when the mouse is in the ValmarButton's bounds.
	 */
	public String onImage="images/ui/darkbuttonback.png";
	/**
	 * The ValmarButton's icon.
	 */
	public String icon;
	/**
	 * The ValmarButton's command string.
	 */
	public String command;
	/**
	 * The keyCode of the ValmarButton's hot key.
	 */
	public int hotkey=-1;
	/**
	 * The text displayed representing the ValmarButton's hot key's name.
	 */
	public String hotKeyName="";
	/**
	 * The ValmarButton's mouse over text.
	 */
	public String altText="";
	/**
	 * A boolean stating wether or not to draw the ValmarButton's background.
	 */
	public boolean drawBack=true;
	
	/**
	 * Makes ValmarButtons with specified bounds, icons and commands.
	 */
	public ValmarButton(Rectangle rect, String ico, String comm){
		rectangle=rect;
		icon=ico;
		command=comm;
	}
	
	/**
	 * Moves a ValmarButton to a specified point.
	 */
	public void moveTo(int x, int y){
		setX(x);
		setY(y);
	}
	/**
	 * Sets the x coordinate of a ValmarButton.
	 */
	public int setX(int x){
		rectangle.x=x;
		return rectangle.x;
	}
	/**
	 * Sets the y coordinate of a ValmarButton.
	 */
	public int setY(int y){
		rectangle.y=y;
		return rectangle.y;
	}
	/**
	 * Gets the x coordinate of a ValmarButton.
	 */
	public int getX(){
		return rectangle.x;
	}
	/**
	 * Get the y coordinate of a ValmarButton.
	 */
	public int getY(){
		return rectangle.y;
	}
	
	/**
	 * Returns a boolean stating if a point is inside the bounds of a ValmarButton.
	 */
	public boolean isInside(Point po){
		return rectangle.contains(po);
	}
	
	/**
	 * Renders a ValmarButton.
	 */
	public void render(Graphics g, ValmarRender parent, Point mouseCord){
		String img = "";
		// mouse over code
		if (isInside(mouseCord)) {
			img = onImage;
			if(!altText.equals("")){
				parent.setAltText(altText+" ["+hotKeyName+"]");
			}
		} else {
			img = offImage;
		}
		// draw the back
		Image image;
		if(drawBack){
			image = parent.getCache().getImage(img);
			g.drawImage(image, getX(), getY(), 
				rectangle.width, rectangle.height, parent.getObserver());
		}
		// draw the icon
		image = parent.getCache().getImage(icon);
		g.drawImage(image, getX(), getY(), parent.getObserver());
	}
	
	/**
	 * Makes a ValmarButton respond to input.
	 */
	public boolean processEvents(ValmarButtonListener listen, Point mouseCord, ValmarRender parent){
		if (isInside(mouseCord)) {
			if (parent.getListener().isMousePressing()) {
				listen.runEvent(command, this);
				return true;
			}
		}
		if(hotkey!=-1){
			if(parent.getListener().isKeyPressed(hotkey)){
				listen.runEvent(command, this);
				return true;
			}
		}
		return false;
	}
	
}
