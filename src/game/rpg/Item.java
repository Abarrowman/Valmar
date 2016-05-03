package game.rpg;

import game.aml.AMLBlock;
import game.aml.AMLInturpreter;
import game.aml.AMLLine;

//documented

/**
 * Item is the class for any WorldObject that is not an Entity or Terrain.
 * @author Adam
 */
public class Item {
	/**
	 * The Item's name.
	 */
	public String name;
	/**
	 * The Item's image.
	 */
	public String src;
	/**
	 * The Item's cost.
	 */
	public int cost=0;
	/**
	 * The Item's effect when used.
	 */
	public String effect="";
	/**
	 * The Item's AML callback after colliding with the player.
	 */
	public AMLLine onCollide=null;
	/**
	 * A boolean stating if the Item is passable.
	 */
	public boolean blocking=false;
	/**
	 * A boolean stating if the Item blocks vision.
	 */
	public boolean opaque=false;
	/**
	 * A boolean stating if the Item is stackable.
	 */
	public boolean stacks=false;
	/**
	 * A boolean statin if the Item can be carried.
	 */
	public boolean carriable=true;
	/**
	 * The minimum of the Item's effect.
	 */
	public int min=0;
	/**
	 * The maximum of the Item's effect.
	 */
	public int max=0;
	/**
	 * The equipment slot the Item occupies.
	 */
	public int equip=-1;
	/**
	 * The armor bonus the Item provides.
	 */
	public int armor=0;
	
	/**
	 * Make a new Item given its name and image.
	 */
	public Item(String name, String src)
	{
		this.name=name;
		this.src=src;
	}
	/**
	 * Parses an Item from an AMLBlock.
	 */
	public static Item parse(AMLBlock block)
	{
		Item item=new Item(block.getParameterString("name"), block.getParameterString("src"));
		item.cost=block.getParameterInt("cost", item.cost);
		item.effect=block.getParameterString("effect", item.effect);
		item.blocking=block.getParameterBoolean("blocking", item.blocking);
		item.opaque=block.getParameterBoolean("opaque", item.blocking);
		item.stacks=block.getParameterBoolean("stacks", item.stacks);
		item.carriable=block.getParameterBoolean("carriable", !item.blocking);
		item.min=block.getParameterInt("min",item.min);
		item.max=block.getParameterInt("max", item.min);
		item.equip=block.getParameterInt("equip", item.equip);
		item.armor=block.getParameterInt("armor", item.armor);
		//event listeners
		String event=block.getParameterString("onCollide", null);
		if(event!=null){
			item.onCollide=AMLInturpreter.getLine(event);
		}
		
		return item;
	}
	
	/**
	 * Returns the value of the Item's effect.
	 */
	public int getEffectValue(){
		return (int)(Math.round(Math.random()*(((double)max)-((double)min))+((double)min)));
	}

}
