package game.rpg;

//documented

/**
 * Terrain Instance is a class for instances of Terrain.
 * @author Adam
 */
public class TerrainInstance extends WorldObject {
	/**
	 * The TerrainInstance's Terrain.
	 */
	public Terrain terrain;
	
	/**
	 * Makes a new TerrainInstance with a given Terrain.
	 */
	public TerrainInstance(Terrain terrain)
	{
		this.terrain=terrain;
	}
	/**
	 * Makes a new TerrainInstance with a given Terrain and coordinate.
	 */
	public TerrainInstance(Terrain terrain, int x, int y)
	{
		this.x=x;
		this.y=y;
		this.terrain=terrain;
	}
	
	
	public boolean getBlocking() {
		return terrain.blocking;	
	}
	
	public boolean getOpaque() {
		return terrain.opaque;
	}
	
	public void collideWith(Entity entity) {
		
	}
}
