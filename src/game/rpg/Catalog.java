package game.rpg;

import java.util.Vector;

//documented

/**
 * Catalog is a class that stores parsed WorldObjects.
 * @author Adam
 */
public class Catalog {
	/**
	 * A Vector of parsed Terrain.
	 */
	public Vector<Terrain> terrains;
	/**
	 * A Vector of single character strings identifying a terrain used in map files.
	*/
	public Vector<String> terrainKeys;
	
	/**
	 * A Vector of parsed Items.
	 */
	public Vector<Item> items;
	/**
	 * A Vector of parsed Items' names.
	 */
	public Vector<String> itemNames;
	
	/**
	 * A Vector of parsed Entities.
	 */
	public Vector<Entity> entities;
	/**
	 * A Vector of parsed Entities' names.
	 */
	public Vector<String> entityNames;
	
	/**
	 * A Vector of parsed Statuses.
	 */
	public Vector<Status> statuses;
	/**
	 * A Vector of parsed Statuses' names.
	 */
	public Vector<String> statusNames;
	
	/**
	 * Makes a new empty catalog.
	 */
	public Catalog()
	{
		//terrain
		terrainKeys=new Vector<String>();
		terrains=new Vector<Terrain>();
		//items
		items=new Vector<Item>();
		itemNames=new Vector<String>();
		//entities
		entities=new Vector<Entity>();
		entityNames=new Vector<String>();
		//statuses
		statuses=new Vector<Status>();
		statusNames=new Vector<String>();
	}
	
	/**
	 * Returns a Status with the given name.
	 */
	public Status getStatus(String name)
	{
		int index=statusNames.indexOf(name);
		if(index!=-1){
			return statuses.get(index);
		}else{
			return null;
		}
	}
	/**
	 * Adds a Status to the Catalog.
	 */
	public Status addStatus(Status status)
	{
		int index=statusNames.indexOf(status.name);
		if(index==-1)
		{
			statuses.add(status);
			statusNames.add(status.name);
		}else{
			statuses.set(index, status);
			statusNames.set(index, status.name);
		}
		return status;
	}
	
	/**
	 * Returns a Terrain with the given key.
	 */
	public Terrain getTerrain(String key)
	{
		int index=terrainKeys.indexOf(key);
		if(index!=-1){
			return terrains.get(index);
		}else{
			return null;
		}
	}
	/**
	 * Adds a Terrain to the Catalog.
	 */
	public Terrain addTerrain(Terrain terrain)
	{
		int index=terrainKeys.indexOf(terrain.key);
		if(index==-1)
		{
			terrains.add(terrain);
			terrainKeys.add(terrain.key);
		}else{
			terrains.set(index, terrain);
			terrainKeys.set(index, terrain.key);
		}
		return terrain;
	}
	
	/**
	 * Returns the Item with the given name.
	 */
	public Item getItem(String name)
	{
		int index=itemNames.indexOf(name);
		if(index!=-1){
			return items.get(index);
		}else{
			return null;
		}
	}
	
	/**
	 * Adds an Item to the Catalog.
	 */
	public Item addItem(Item item)
	{
		int index=itemNames.indexOf(item.name);
		if(index==-1)
		{
			items.add(item);
			itemNames.add(item.name);
		}else{
			items.set(index, item);
			itemNames.set(index, item.name);
		}
		return item;
	}
	
	
	/**
	 * Returns the Entity with a given name.
	 */
	public Entity getEntity(String name)
	{
		int index=entityNames.indexOf(name);
		if(index!=-1){
			return entities.get(index);
		}else{
			return null;
		}
	}
	
	/**
	 * Adds an Entity to the Catalog.
	 */
	public Entity addEntity(Entity entity)
	{
		int index=entityNames.indexOf(entity.name);
		if(index==-1)
		{
			entities.add(entity);
			entityNames.add(entity.name);
		}else{
			entities.set(index, entity);
			entityNames.set(index, entity.name);
		}
		return entity;
	}

}
