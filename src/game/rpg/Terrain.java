package game.rpg;

import game.aml.AMLBlock;

//documented

/**
 * Terrain is a class for terrain tiles.
 * @author Adam
 */
public class Terrain {
	/**
	 * The Terrain's name.
	 */
	public String name;
	/**
	 * The Terrain's image.
	 */
	public String src;
	/**
	 * The Terrain's key.
	 */
	public String key;
	/**
	 * A boolean stating if the Terrain is impassable.
	 */
	public boolean blocking=false;
	/**
	 * A boolean stating if the Terrain can be seen through.
	 */
	public boolean opaque=false;
	/**
	 * Makes a new Terrain with a given name, image and key.
	 */
	public Terrain(String name, String src, String key)
	{
		this.name=name;
		this.src=src;
		this.key=key;
	}
	/**
	 * Parses a Terrain from an AMLBlock.
	 */
	public static Terrain parse(AMLBlock block)
	{
		Terrain terrain = new Terrain(block.getParameterString("name"),
									  block.getParameterString("src"),
									  block.getParameterString("key"));
		terrain.blocking=block.getParameterBoolean("blocking", terrain.blocking);
		terrain.opaque=block.getParameterBoolean("opaque", terrain.blocking);
		return terrain;
	}
}
