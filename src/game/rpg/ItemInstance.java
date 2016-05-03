package game.rpg;

import java.util.Vector;

import game.aml.AMLBlock;

//documented

/**
 * ItemInstance is the class for an instance of an Item.
 * @author Adam
 */
public class ItemInstance extends WorldObject {
	/**
	 * The ItemInstance's Item.
	 */
	public Item item;
	
	private int count=1;
	
	/**
	 * Makes a new ItemInstance given its item.
	 */
	public ItemInstance(Item item)
	{
		this.item=item;
	}
	/**
	 * Makes a new ItemInstance given its item and quantity.
	 */
	public ItemInstance(Item item, int quan)
	{
		this.item=item;
		setQuantity(quan);
	}
	/**
	 * Makes a new ItemInstance given its item and coordinates.
	 */
	public ItemInstance(Item item, int x, int y)
	{
		this.x=x;
		this.y=y;
		this.item=item;
	}
	/**
	 * Makes a new ItemInstance givne its item, coordinates and quantity.
	 */
	public ItemInstance(Item item, int x, int y, int quan)
	{
		this.x=x;
		this.y=y;
		this.item=item;
		setQuantity(quan);
	}
	
	/**
	 * Returns a boolean stating if the ItemInstance is stackable.
	 */
	public boolean getStacks(){
		return item.stacks;
	}
	
	/**
	 * Sets the number of an Item an ItemInstance has.
	 */
	public int setQuantity(int quantity){
		if(item.stacks){
			count=quantity;
		}
		return count;
	}
	
	/**
	 * Returns the number of its Item an ItemInstance has. 
	 */
	public int getQuantity(){
		return count;
	}
	
	public boolean getBlocking() {
		return item.blocking;
	}
	
	public boolean getOpaque() {
		return item.opaque;
	}
	
	/**
	 * Returns the cost of the ItemInstance.
	 */
	public int getValue(){
		return item.cost*count;
	}
	
	/**
	 * Returns a boolean stating if the ItemInstance can be carried.
	 */
	public boolean getCarriable() {
		return item.carriable;
	}
	
	/**
	 * Parses an ItemInstance from AMLBlock.
	 */
	public ItemInstance parseStatic(AMLBlock block)
	{
		x=block.getParameterInt("x", x);
		y=block.getParameterInt("y", y);
		
		//avoid declaring team in map
		count=block.getParameterInt("quantity", count);
		
		return this;
	}
	
	public void collideWith(Entity entity) {
		if(item.onCollide!=null){
			//has a collide callback
			if(item.onCollide.command.equals("delete")){
				world.removeItem(this);
			}else{
				if(item.onCollide.command.equals("changeTo")){
					Item it=world.catalog.getItem(item.onCollide.getParameterString("type", item.name));
					if(it!=null){
						item=it;
					}
				}else if(item.onCollide.command.equals("replace")){
					String with=item.onCollide.getParameterString("with", item.name);
					String[] values = with.split("\\.");
					Vector<ItemInstance> its=new Vector<ItemInstance>();
					for (String value : values) {
						int index=value.indexOf("_");
						int count=1;
						if(index!=-1){
							try{
								count=Integer.parseInt(value.substring(index+1));
							}catch(NumberFormatException e){
								count=1;
							}
							value=value.substring(0, index);
						}
						Item it=world.catalog.getItem(value);
						
						if(it!=null){
							its.add(new ItemInstance(it, x, y, count));
						}
					}
					World wor=world;
					world.removeItem(this);
					for(int n=0;n<its.size();n++){
						wor.addItem(its.get(n));
					}
				}
			}
		}
	}
	
	/**
	 * Gets the text description of the ItemInstance.
	 */
	public String getDescription() {
		String str="";
		//name
		if(item.stacks){
			str+=item.name+" ("+count+")"+"\n";
		}else{
			str+=item.name+"\n";
		}
		//effect
		if(!item.effect.equals("")){
			//effect
			str+="effect: "+item.effect+" ("+item.min+"-"+item.max+")\n";
		}else if(item.equip==0){
			//damage
			str+="damage: ("+item.min+"-"+item.max+")\n";
		}
		//armor
		if(item.armor!=0){
			str+="armor: "+item.armor+"\n";
		}
		//value
		if(count==1){
			str+="value: "+item.cost;
		}else{
			str+="value: "+count+"x"+item.cost;
		}
		return str;
	}
	
	/**
	 * Returns the String of the AMLBlock that can be parsed to return the ItemInstance.
	 */
	public String save() {
		AMLBlock block=new AMLBlock();
		block.command="Item";
		block.addParameter("name", item.name);
		block.addParameter("x", x+"");
		block.addParameter("y", y+"");
		block.addParameter("quantity", count+"");
		return block.encode();
	}
}
