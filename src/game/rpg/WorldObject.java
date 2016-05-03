package game.rpg;

//documented

/**
 * WorldObject is the class for objects in the World.
 * @author Adam
 */
public abstract class WorldObject {
	/**
	 * The x coordinate of a WorldObject on the map.
	 */
	public int x=0;
	/**
	 * The y coordinate of a WorldObject on the map.
	 */
	public int y=0;
	/**
	 * The WorldObject's World.
	 */
	public World world;
	
	/**
	 * Returns a boolean stating if the WorldObject is impassable.
	 */
	public abstract boolean getBlocking();
	
	/**
	 * Returns a boolean stating if the WorldObject can be seen through.
	 */
	public abstract boolean getOpaque();
	
	/**
	 * Called when an Entity collides with the WorldObject.
	 */
	public abstract void collideWith(Entity entity);
}
