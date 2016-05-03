package game;

import game.rpg.Entity;
import game.rpg.World;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.net.URL;

//documented

/**
 * ValmarRender is an interface of the methods an object needs to be a Valmar game.
 * @author Adam
 */
public interface ValmarRender {
	/**
	 * Returns the base URL of the application.
	 */
	public URL getBase();
	
	/**
	 * Returns a Component.
	 */
	public Component getComponent();
	/**
	 * Loads an image.
	 */
	public Image getImage(URL base, String url);
	
	/**
	 * Returns an ImageObserver.
	 */
	public ImageObserver getObserver();
	/**
	 * Returns an ImageCache.
	 */
	public ImageCache getCache();
	
	/**
	 * Returns a ValmarListener.
	 */
	public ValmarListener getListener();
	/**
	 * Returns a World.
	 */
	public World getWorld();
	
	/**
	 * Draws multiple lines of text.
	 */
	public Rectangle drawMultilineText(Graphics g, String text, int x, int i);
	/**
	 * Draws multiple lines of text.
	 */
	public Rectangle drawMultilineText(Graphics g, String string, int i, int j,boolean b);
	/**
	 * Exits a menu.
	 */
	public void backToGame();
	
	/**
	 * Open the inventory to a specified shop.
	 */
	public void openInventory(Entity entity);
	/**
	 * Loads a map.
	 */
	public void loadMap(String zone);
	/**
	 * Returns a Frame.
	 */
	public Frame findParentFrame();

	/**
	 * Returns an Inventory.
	 */
	public Inventory getInventory();

	/**
	 * Traces a line of text to an output box.
	 */
	public void trace(String string);
	
	/**
	 * Sets the mouse over text of the ValmarRender.
	 */
	public void setAltText(String text);

	/**
	 * Called when the player dies.
	 */
	public void playerDied();

	/**
	 * Called when the player wins.
	 */
	public void wonGame();
}
