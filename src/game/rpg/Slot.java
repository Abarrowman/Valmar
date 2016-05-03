package game.rpg;

import java.util.Vector;

//documented

/**
 * Slot is a class that contains all the WorldObjects at a given coordinate.
 * @author Adam
 */
public class Slot {
	
	/**
	 * The x coordinate of the Slot.
	 */
	public int x=0;
	/**
	 * The y coordinate of the Slot.
	 */
	public int y=0;
	/**
	 * The World the Slot is in.
	 */
	public World world;
	
	private TerrainInstance terrain;
	private Vector<ItemInstance> items;
	private Entity entity;
	
	/**
	 * Makes a new slot given its coordinate, World and TerrainInstance.
	 */
	public Slot(int x, int y, World world, TerrainInstance terrain){
		this.x=x;
		this.y=y;
		this.world=world;
		items=new Vector<ItemInstance>();
		addTerrain(terrain);
	}
	/**
	 * Makes a new slot given its coordinate, World, TerrainInstance and Entity.
	 */
	public Slot(int x, int y, World world, TerrainInstance terrain, Entity entity){
		this.x=x;
		this.y=y;
		this.world=world;
		items=new Vector<ItemInstance>();
		addEntity(entity);
		addTerrain(terrain);
	}
	
	/**
	 * Returns a boolean stating if the slot can be seen through.
	 */
	public boolean getOpaque() {
		if(terrain.getBlocking()&&terrain.getOpaque()){
			return true;
		}else{
			boolean ok=false;
			for(int n=0;n<items.size();n++)
			{
				ItemInstance item=items.get(n);
				if(item.getOpaque()){
					ok=true;
					break;
				}
			}
			return ok;
		}
	}
	
	/**
	 * Returns a boolean stating if the slot is impassable.
	 */
	public boolean getBlocking() {
		if(entity!=null)
		{
			return true;
		}else{
			if(terrain.getBlocking()){
				return true;
			}else{
				boolean ok=false;
				for(int n=0;n<items.size();n++)
				{
					ItemInstance item=items.get(n);
					if(item.getBlocking()){
						ok=true;
						break;
					}
				}
				return ok;
			}
		}
	}
	
	/**
	 * Returns the items that can be carried that are on the Slot.
	 */
	public Vector<ItemInstance> getCarriableItems(){
		Vector<ItemInstance> carriable=new Vector<ItemInstance>();
		for(int n=0;n<items.size();n++){
			if(items.get(n).getCarriable()){
				carriable.add(items.get(n));
			}
		}
		return carriable;
	}
	
	/**
	 * Returns the Slot's Terrain.
	 */
	public TerrainInstance getTerrain()
	{
		return terrain;
	}
	
	/**
	 * Returns the Slot's Entity.
	 */
	public Entity getEntity()
	{
		return entity;
	}
	/**
	 * Returns the Slot's Items.
	 */
	public Vector<ItemInstance> getItems()
	{
		return items;
	}
	
	/**
	 * Adds a Terrain to the Slot.
	 */
	public TerrainInstance addTerrain(TerrainInstance terrain)
	{
		if(this.terrain==null)
		{
			this.terrain=terrain;
			this.terrain.world=this.world;
			this.terrain.x=x;
			this.terrain.y=y;
		}
		return terrain;
	}
	
	/**
	 * Removes the Slot's Terrain.
	 */
	public TerrainInstance removeTerrain(TerrainInstance terrain){
		
		if(this.terrain==terrain){
			this.terrain.world=null;
			this.terrain=null;
		}
		return terrain;
	}
	
	/**
	 * Adds an Entity to the Slot.
	 */
	public Entity addEntity(Entity entity)
	{
		if(this.entity==null)
		{
			this.entity=entity;
			this.entity.world=this.world;
			this.entity.x=x;
			this.entity.y=y;
		}
		return entity;
	}
	/**
	 * Removes an Entity form the Slot.
	 */
	public Entity removeEntity(Entity entity){
		if(this.entity==entity){
			entity.world=null;
			this.entity=null;
		}
		return entity;
	}
	
	/**
	 * Adds an Item to the Slot.
	 */
	public ItemInstance addItem(ItemInstance item) {
		if(!getBlocking()||(getBlocking()&&entity!=null)){
			int index=items.indexOf(item);
			if(index==-1){
				item.x=x;
				item.y=y;
				item.world=this.world;
				items.add(item);
			}
		}
		return item;
	}
	
	/**
	 * Removes an Item from the Slot.
	 */
	public ItemInstance removeItem(ItemInstance item) {
		int index=items.indexOf(item);
		if(index!=-1){
			item.world=null;
			items.remove(item);
		}
		return item;
	}
	
	/**
	 * Calls the collide callbacks of the Slot's WorldObjects.
	 */
	public void collideWith(Entity entity) {
		if(terrain.getBlocking()){
			terrain.collideWith(entity);
		}else{
			for(int n=0;n<items.size();n++)
			{
				ItemInstance item=items.get(n);
				if(item.getBlocking()){
					item.collideWith(entity);
					break;
				}
			}
			if(this.entity!=null){
				this.entity.collideWith(entity);
			}
		}
	}
	/**
	 * Returns the AMLCode for all the Items and Entities in the Slot.
	 */
	public String save() {
		String fil="";
		if(entity!=null){	
			fil+=entity.save();
		}
		for(int n=0;n<items.size();n++){
			fil+=items.get(n).save();
		}
		return fil;
	}
}
