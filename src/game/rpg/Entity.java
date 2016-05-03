package game.rpg;

import java.awt.Point;
import java.util.Vector;

import game.aml.AMLBlock;
import game.aml.AMLInturpreter;
import game.aml.AMLLine;

//documented

/**
 * Entity is a class for any player or npc in Valmar.
 * @author Adam
 */
public class Entity extends WorldObject {
	// vital
	/**
	 * The Entity's name.
	 */
	public String name;
	/**
	 * The Entity's id. Used for quests.
	 */
	public String id;
	/**
	 * The Entity's image.
	 */
	public String src;
	/**
	 * The Entity's attacking image.
	 */
	public String atksrc;
	/**
	 * The Entity's portrait image.
	 */
	public String prtsrc;
	/**
	 * A boolean stating whether or not to save specific details about the Entity.
	 */
	public boolean dynamic = false;

	//
	/**
	 * The Entity's hit points.
	 */
	public Stat hp;
	/**
	 * The Entity's energy points.
	 */
	public Stat ep;
	/**
	 * The Entity's experience points.
	 */
	public Stat xp;
	/**
	 * The Entity's level.
	 */
	public int level;
	/**
	 * The Entity's skill points left to be used.
	 */
	public int skillPointsLeft;

	// stats
	/**
	 * The Entity's strength.
	 */
	public Stat str;
	/**
	 * The Entity's dexterity.
	 */
	public Stat dex;
	/**
	 * The Entity's wisdom.
	 */
	public Stat wis;
	/**
	 * The Entity's endurance.
	 */
	public Stat end;

	// skills
	/**
	 * The Entity's magical ability.
	 */
	public Stat magic;
	/**
	 * The Entity's armor.
	 */
	private Stat armor;

	// non-vital strings
	/**
	 * This ai command is run on the Entity when the player is nearby and no combat is
	 * occurring. still -> stands still random -> moves randomly
	 */
	public String peaceAi = "still";
	
	/**
	 * fightAi This ai command is run every time the Entity takes a turn. still
	 * -> stands still random -> moves randomly drunk -> if there is an adjacent
	 * entity attacks otherwise moves randomly turret -> stands still and
	 * attacks if in range hunt -> uses path finding to move towards a target
	 * then attacks ranger -> uses pathfinding until a line of sight on the target
	 * is reached than attacks
	 */
	public String fightAi = "still";
	

	/**
	 * The Entity's team. 0 -> the player's team 1 -> neutral 2 -> evil
	 */
	public int team = 1;

	/**
	 * A CSV of non equipped items held by the Entity.
	 */
	public String itemCSV = "";

	/**
	 * CSV of equipped items held by the Entity.
	 */
	private String equipmentCSV = "";

	/**
	 * CSV of statuses afflicting the Entity.
	 */
	private String statusCSV = "";

	/**
	 * CSV of the statuses a basic attacks from this Entity inflicts.
	 */
	public String atkInflicts = "";
	
	/**
	 * How many gold pieces the Entity is holding.
	 */
	public int gold = 0;

	/**
	 * The minimum damage done by the natural weapons of this Entity.
	 */
	private int min = 1;

	/**
	 * The maximum damage done by the natural weapons of this Entity.
	 */
	private int max = 1;

	// event handlers
	/**
	 * The AMLLine callback after colliding with the player.
	 */
	public AMLLine onCollide = null;

	// vectors
	/**
	 * The items equipped to the Entity.
	 */
	public Vector<ItemInstance> equipement;
	/**
	 * The items held by the Entity.
	 */
	public Vector<ItemInstance> items;
	/**
	 * The statuses a basic attacks from this Entity inflicts.
	 */
	public Vector<StatusInstance> inflictedStatuses;
	/**
	 * The statuses affecting the Entity.
	 */
	public Vector<StatusInstance> statuses;
	

	// core
	/**
	 * The Entity's action points.
	 */
	public Stat actionPoints;
	/**
	 * The direction the Entity is facing. 0-> left 1 -> right
	 */
	public int facing = 1;
	/**
	 * The current pose the Entity is in. 0 -> normal 1 -> attacking
	 */
	public int pose = 0;

	// only used some times
	/**
	 * The path the Entity is moving on.
	 */
	public Vector<Point> path;
	/**
	 * The step in the path the Entity has reached.
	 */
	public int pathIndex;
	/**
	 * The target of the Entity.
	 */
	public Entity target;

	/**
	 * Makes a new Entity with a given name and image.
	 */
	public Entity(String name, String src) {
		// vital
		this.name = name;
		atksrc = this.src = src;
		prtsrc = "images/portraits/default.png";

		//
		id="unnamed";
		level = 1;
		skillPointsLeft = 0;

		hp = new Stat(1, 1);
		ep = new Stat(1, 1);
		xp = new Stat(0, 100 * level);

		// stats
		str = new Stat(1, 1);
		dex = new Stat(1, 1);
		end = new Stat(1, 1);
		wis = new Stat(1, 1);
		
		//skills
		magic = new Stat(0, 0);
		armor = new Stat(0, 0);
		

		// vectors
		statuses = new Vector<StatusInstance>();

		// core
		actionPoints = new Stat(8, 8);
	}
	
	/**
	 * Recalculates the Entity's secondary stats.
	 */
	public void recalculateSecondaries(){
		actionPoints.setBaseValue((int)(8+Math.floor(dex.getValued()/5d)));
		ep.setBaseValue((int)((level+wis.getBaseValue())*5));
		if(world!=null){
			if(this==world.player){
				hp.setBaseValue((int)((level+end.getBaseValue())*5));
			}
		}
	}

	/**
	 * Parses a generic Entity out of an AMLBlock.
	 */
	public static Entity parse(AMLBlock block) {
		Entity entity = new Entity(block.getParameterString("name"), block.getParameterString("src"));

		entity.atksrc = block.getParameterString("atk-src", entity.src);
		entity.prtsrc = block.getParameterString("prt-src", entity.prtsrc);

		entity.hp = new Stat(block.getParameterInt("hp", entity.hp.getValue()));
		entity.ep = new Stat(block.getParameterInt("ep", entity.ep.getValue()));

		entity.str = new Stat(block.getParameterInt("str", entity.str.getValue()));
		entity.dex = new Stat(block.getParameterInt("dex", entity.dex.getValue()));
		entity.end = new Stat(block.getParameterInt("end", entity.end.getValue()));
		entity.wis = new Stat(block.getParameterInt("wis", entity.wis.getValue()));

		entity.magic = new Stat(block.getParameterInt("magic", entity.magic.getValue()));
		entity.armor = new Stat(block.getParameterInt("armor", entity.armor.getValue()));

		entity.min = block.getParameterInt("min", entity.min);
		entity.max = block.getParameterInt("max", entity.max);

		entity.atkInflicts = block.getParameterString("atk-inflicts", entity.atkInflicts);

		entity.peaceAi = block.getParameterString("peaceAi", entity.peaceAi);
		entity.fightAi = block.getParameterString("fightAi", entity.fightAi);

		entity.itemCSV = block.getParameterString("items", entity.itemCSV);
		entity.equipmentCSV = block.getParameterString("equipped", entity.equipmentCSV);

		entity.gold = block.getParameterInt("gold", entity.gold);
		entity.team = block.getParameterInt("team", entity.team);
		entity.level = block.getParameterInt("level", entity.level);
		entity.xp = new Stat(0, 100 * entity.level);

		// event handlers
		String event = block.getParameterString("onCollide", null);
		if (event != null) {
			entity.onCollide = AMLInturpreter.getLine(event);
		}

		entity.dynamic = block.getParameterBoolean("dynamic", entity.dynamic);
		
		entity.recalculateSecondaries();
		return entity;
	}

	/**
	 * Parses a specific Entity form an AMLBlock.
	 */
	public static Entity parseDynamic(Entity entity, AMLBlock block) {

		entity.atksrc = block.getParameterString("atk-src", entity.src);
		entity.prtsrc = block.getParameterString("prt-src", entity.prtsrc);

		entity.hp = new Stat(block.getParameterInt("hp", entity.hp.getValue()), block.getParameterInt("maxHp", entity.hp.getMaxValue()));
		entity.ep = new Stat(block.getParameterInt("ep", entity.ep.getValue()), block.getParameterInt("maxEp", entity.ep.getMaxValue()));

		entity.str = new Stat(block.getParameterInt("str", entity.str.getValue()));
		entity.dex = new Stat(block.getParameterInt("dex", entity.dex.getValue()));
		entity.end = new Stat(block.getParameterInt("end", entity.end.getValue()));
		entity.wis = new Stat(block.getParameterInt("wis", entity.wis.getValue()));

		entity.magic = new Stat(block.getParameterInt("magic", entity.magic.getValue()));
		entity.armor = new Stat(block.getParameterInt("armor", entity.armor.getValue()));
		
		entity.x = block.getParameterInt("x", entity.x);
		entity.y = block.getParameterInt("y", entity.y);

		entity.min = block.getParameterInt("min", entity.min);
		entity.max = block.getParameterInt("max", entity.max);

		entity.atkInflicts = block.getParameterString("atk-inflicts", entity.atkInflicts);

		entity.peaceAi = block.getParameterString("peaceAi", entity.peaceAi);
		entity.fightAi = block.getParameterString("fightAi", entity.fightAi);

		entity.itemCSV = block.getParameterString("items", entity.itemCSV);
		entity.equipmentCSV = block.getParameterString("equipped", entity.equipmentCSV);
		entity.statusCSV = block.getParameterString("statuses", entity.statusCSV);

		entity.gold = block.getParameterInt("gold", entity.gold);
		entity.team = block.getParameterInt("team", entity.team);
		entity.level = block.getParameterInt("level", entity.level);
		entity.xp = new Stat(block.getParameterInt("currentXP", 0), 100 * entity.level);
		entity.skillPointsLeft = block.getParameterInt("skillPointsLeft", entity.skillPointsLeft);

		// event handlers
		String event = block.getParameterString("onCollide", null);
		if (event != null) {
			entity.onCollide = AMLInturpreter.getLine(event);
		}

		entity.dynamic = true;
		entity.id=block.getParameterString("id", entity.id);

		entity.recalculateSecondaries();
		
		return entity;
	}

	/**
	 * Parses an instance of a generic Entity from an AMLBlock.
	 */
	public Entity parseStatic(AMLBlock block) {
		x = block.getParameterInt("x", x);
		y = block.getParameterInt("y", y);
		dynamic = block.getParameterBoolean("dynamic", dynamic);
		id=block.getParameterString("id", id);
		return this;
	}

	/**
	 * Makes an identical copy of the Entity.
	 */
	public Entity clone() {
		Entity entity = new Entity(name, src);
		// ensures new variables are created on stack
		entity.atksrc = atksrc + "";
		entity.prtsrc = prtsrc + "";

		entity.hp = hp.clone();
		entity.ep = ep.clone();

		entity.str = str.clone();
		entity.dex = dex.clone();
		entity.end = end.clone();
		entity.wis = wis.clone();
		
		entity.magic = magic.clone();
		entity.armor = armor.clone();

		entity.min = min;
		entity.max = max;

		entity.atkInflicts = atkInflicts + "";

		entity.peaceAi = peaceAi + "";
		entity.fightAi = fightAi + "";

		entity.itemCSV = itemCSV + "";
		entity.equipmentCSV = equipmentCSV + "";
		entity.statusCSV = statusCSV + "";

		entity.gold = gold + 0;
		entity.team = team + 0;
		entity.level = level + 0;
		entity.xp = xp.clone();
		entity.skillPointsLeft = skillPointsLeft + 0;
		
		id="unnamed";

		if (onCollide != null) {
			entity.onCollide = onCollide.clone();
		}

		entity.dynamic = dynamic;

		return entity;
	}
	
	public void collideWith(Entity entity) {
		if (entity != this) {
			if (world.fighting) {
				if (world.isNextTo(entity.x, entity.y, x, y)) {
					if (team == 0 && entity.team == 2 || team == 1 && entity.team == 2 || team == 2 && entity.team != 2) {
						world.attack(entity, this);
					}
				}
			} else {
				if (onCollide != null) {
					if (entity == world.player) {
						if(entity.x<this.x){
							facing=-1;
						}else if(entity.x>this.x){
							facing=1;
						}
						if (onCollide.command.equals("sell")) {
							world.parent.openInventory(this);
						} else if (onCollide.command.equals("say")) {
							say(onCollide.getParameterString("text", ""));
						} else if (onCollide.command.equals("quest")) {
							if(world.getEntityWithId(onCollide.getParameterString("target", "unnamed"))==null){
								//reward
								String its=onCollide.getParameterString("endItems", "");
								String[] values = its.split("\\.");
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
										world.addItem(new ItemInstance(it, world.player.x, world.player.y, count));
									}
								}
								//xp
								int xpe=onCollide.getParameterInt("rewardXp",0);
								world.parent.trace("You have completed a quest earning "+xpe+" XP!");
								entity.awardXP(xpe);
								//win
								if(onCollide.getParameterBoolean("win", false)){
									world.parent.wonGame();
								}
								//message
								String end=onCollide.getParameterString("endMessage", "");
								if(!end.equals("")){
									say(end);
									onCollide=new AMLLine("say:text-"+end+",");
								}else{
									onCollide=null;
								}
							}else{
								say(onCollide.getParameterString("startMessage", ""));
							}
						} else if (onCollide.command.equals("fight")) {
							if (team == 1) {
								team = 2;
								world.startFight();
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Makes text appear at the Entity's location.
	 */
	public void say(String text) {
		if (!text.equals("")) {
			world.animationX = x;
			world.animationY = y;
			world.animating = true;
			world.animationText = text;
			world.animation = new Vector<String>();
			world.animationDuration = (int) Math.max(Math.round(((float) text.length()) / 3.5f), 2);
			world.animationIndex = 0;
		}
	}

	/**
	 * Gets the Entity's initiative modifier.
	 */
	public int getInitiative() {
		return dex.getValue();
	}

	public boolean getBlocking() {
		return true;
	}

	public boolean getOpaque() {
		return true;
	}

	/**
	 * Spends the specified number of action points.
	 */
	public void spendActionPoint(int points) {
		actionPoints.setValue(actionPoints.getValue() - points);
	}

	/**
	 * Gets the src of the Entity's current pose.
	 */
	public String getSrc() {
		if (pose == 0) {
			return src;
		} else {
			return atksrc;
		}
	}

	/**
	 * Reads the contents of an Entity's equipmentCSV into its equipment
	 * vector.
	 */
	public void readEquipment() {
		if (equipement == null) {
			equipement = new Vector<ItemInstance>();
			for (int n = 0; n < 10; n++) {
				equipement.add(null);
			}
			String[] values = equipmentCSV.split(",");
			for (String value : values) {
				int num;
				String name;
				int spaceIndex = value.lastIndexOf("_");
				if (spaceIndex != -1) {
					name = value.substring(0, spaceIndex);
					num = Integer.parseInt(value.substring(spaceIndex + 1));
				} else {
					name = value;
					num = 1;
				}

				Item it = world.catalog.getItem(name);
				if (it != null) {
					ItemInstance item = new ItemInstance(it, num);
					if (item != null) {

						if (item.item.equip >= 0 && item.item.equip < 10) {
							equipement.set(item.item.equip, item);
						}
					}
				}
			}
			// analyse equipment
			analyseEquipment();
		}
	}

	/**
	 * Reads the contents of an Entity's itemCSV into its items vector.
	 */
	public void readItems() {
		if (items == null) {
			items = new Vector<ItemInstance>();
			String[] values = itemCSV.split(",");
			for (String value : values) {
				int num;
				String name;
				int spaceIndex = value.lastIndexOf("_");
				if (spaceIndex != -1) {
					name = value.substring(0, spaceIndex);
					num = Integer.parseInt(value.substring(spaceIndex + 1));
				} else {
					name = value;
					num = 1;
				}

				Item it = world.catalog.getItem(name);
				if (it != null) {
					ItemInstance item = new ItemInstance(it, num);
					if (item != null) {
						items.add(item);
					}
				}
			}
		}
	}

	/**
	 * Reads the contents of an Entity's statusCSV into its statuses vector.
	 */
	public void readStatuses() {
		if (!statusCSV.equals("")) {
			String[] values = this.statusCSV.split(",");
			for (String value : values) {
				int num;
				String name;
				int spaceIndex = value.lastIndexOf("_");
				if (spaceIndex != -1) {
					name = value.substring(0, spaceIndex);
					num = Integer.parseInt(value.substring(spaceIndex + 1));
				} else {
					name = value;
					num = 1;
				}
				Status st = world.catalog.getStatus(name);
				if (st != null) {
					StatusInstance status = new StatusInstance(st, num);
					if (status != null) {
						addStatus(status);
					}
				}
			}
			statusCSV = "";
		}
	}

	/**
	 * Reads the atkInflicts CSV of the Entity to create a vector of inflicted statuses.
	 */
	public void readInflictedStatuses() {
		if (inflictedStatuses == null) {
			inflictedStatuses = new Vector<StatusInstance>();
			String[] values = this.atkInflicts.split(",");
			for (String value : values) {
				int num;
				String name;
				int spaceIndex = value.lastIndexOf("_");
				if (spaceIndex != -1) {
					name = value.substring(0, spaceIndex);
					num = Integer.parseInt(value.substring(spaceIndex + 1));
				} else {
					name = value;
					num = 1;
				}
				Status st = world.catalog.getStatus(name);
				if (st != null) {
					StatusInstance status = new StatusInstance(st, num);
					if (status != null) {
						inflictedStatuses.add(status);
					}
				}
			}
		}
	}

	/**
	 * Reads atkInflicts of the Entity into its inflictedStatuses and returns it.
	 */
	public Vector<StatusInstance> getInflictedStatuses() {
		readInflictedStatuses();
		return inflictedStatuses;
	}

	/**
	 * Reads itemCSV of the Entity into its items and returns it.
	 */
	public Vector<ItemInstance> getItems() {
		readItems();
		return items;
	}

	/**
	 * Reads equipmentCSV of the Entity into its equipment and returns it.
	 */
	public Vector<ItemInstance> getEquipment() {
		readEquipment();
		return equipement;
	}

	/**
	 * Removes modifications cause by an Entity's equipment.
	 */
	public void removeEquipmentModifiers() {
		armor.setModifier(0);
		magic.setModifier(0);
	}

	/**
	 * Applies the modifications to the Entity caused by its equipment. 
	 */
	public void analyseEquipment() {
		// reset
		removeEquipmentModifiers();
		// read
		for (int n = 0; n < equipement.size(); n++) {
			ItemInstance ite = equipement.get(n);
			if (ite != null) {
				Item it = ite.item;
				armor.changeModifier(it.armor);
			}
		}
		
		recalculateSecondaries();
	}

	/**
	 * Returns the amount of damage delt by an attack from the Entity.
	 */
	public int getDamage() {
		// How much damage are you dealing?
		float damage = (((float) str.getValue()) / 10f + 1);
		if (equipement.get(0) != null) {
			Item weapon = equipement.get(0).item;
			damage = (float) ((weapon.min + (weapon.max - weapon.min) * Math.random()) * damage);
		} else if (min != 1) {
			damage = (float) ((min + (max - min) * Math.random()) * damage);
		}

		return (int) Math.max(Math.round(damage), 1);
	}

	/**
	 * Returns the value rolled for the Entity's attack roll.
	 */
	public int attackRoll() {
		double atk = dex.getValued() + 5d + Math.random() * 15d;
		atk += str.getValued(0.25d);
		return (int) Math.max(Math.round(atk), 1);
	}

	/**
	 * Returns the value needed to hit the Entity.
	 */
	public int getDodge() {
		float dg = (float) (10 + dex.getValue());
		return (int) dg;
	}

	/**
	 * Causes the Entity to take damage (reduced by the Entity's armor value.
	 */
	public int takeDamage(int damage) {
		
		// handles armor
		double res = 100d - armor.getValued();
		double dmg = ((double) damage) * res / 100d;
		damage = (int) (Math.max(dmg, 1));
		hp.changeValue(-damage);
		if(world!=null){
			world.parent.trace(name + " took " + damage + " damage.");
		}
		return damage;
	}

	/**
	 * Inflicts the Entity's inflectedStatuses onto an Entity.
	 */
	public void inflictStatusesOn(Entity target) {
		Vector<StatusInstance> sts = getInflictedStatuses();
		for (int n = 0; n < sts.size(); n++) {
			target.addStatus(sts.get(n).clone());
		}
	}

	/**
	 * Adds a status to the Entity.
	 */
	private void addStatus(StatusInstance status) {
		int index = -1;
		for (int n = 0; n < statuses.size(); n++) {
			if (status.status == statuses.get(n).status) {
				index = n;
				break;
			}
		}
		if (index == -1) {
			// start the status
			startStatus(status);
			// add the status
			statuses.add(status);
		} else {
			if (statuses.get(index).turnsLeft < status.turnsLeft) {
				// don't start the status
				// change the turns left
				statuses.get(index).turnsLeft = status.turnsLeft;
			}
		}
	}

	/**
	 * Starts a status afflicting the Entity.
	 */
	public void startStatus(StatusInstance status) {
		// do nothing for now
	}

	/**
	 * Ends a status afflicting the Entity.
	 */
	public void endStatus(StatusInstance status) {
		// do nothing for now
		// remove the status
		int index = statuses.indexOf(status);
		statuses.remove(index);
	}

	/**
	 * Called every turn for every status affecting the Entity.
	 */
	public void statusTurn(StatusInstance status) {
		status.turnsLeft--;
		// do something
		if (status.status.onTurn != null) {
			// has a turn callback
			if (status.status.onTurn.command.equals("damage")) {
				takeDamage(status.status.onTurn.getParameterInt("amount", 1));
				if (hp.getValue() <= 0) {
					world.killEntity(this);
				}
			} else if (status.status.onTurn.command.equals("stun")) {
				this.spendActionPoint(status.status.onTurn.getParameterInt("amount", 1));
			}
		}
		// do I end the status
		if (status.turnsLeft <= 0) {
			endStatus(status);
		}
	}

	/**
	 * Called when a turn passes.
	 */
	public void turnPasses() {
		// important
		for (int n = statuses.size() - 1; n >= 0; n--) {
			statusTurn(statuses.get(n));
		}
		if (!statusCSV.equals("")) {
			readStatuses();
			statusCSV = "";
		}
	}

	/**
	 * Writes the Entity's items into a CSV.
	 */
	public String writeItemCSV() {
		String str = "";
		readItems();
		for (int n = 0; n < items.size(); n++) {
			ItemInstance it = items.get(n);
			str += it.item.name + "_" + it.getQuantity();
			// add comma
			if (n != items.size() - 1) {
				str += ",";
			}
		}
		return str;
	}

	/**
	 * Writes the Entity's equipment into a CSV.
	 */
	public String writeEquipementCSV() {
		String str = "";
		readItems();
		for (int n = 0; n < equipement.size(); n++) {
			ItemInstance it = equipement.get(n);
			if (it != null) {
				str += it.item.name + "_" + it.getQuantity();
			}
			// add comma
			if (n != equipement.size() - 1) {
				str += ",";
			}
		}
		return str;
	}

	/**
	 * Writes the Entity's statuses into a CSV.
	 */
	private String writeStatusesCSV() {
		String str = "";
		readStatuses();
		for (int n = 0; n < statuses.size(); n++) {
			StatusInstance status = statuses.get(n);
			if (status != null) {
				str += status.status.name + "_" + status.turnsLeft;
			}
			// add comma
			if (n != statuses.size() - 1) {
				str += ",";
			}
		}
		return str;
	}

	/**
	 * Returns the String of the AMLBlock that can be parsed to return the Entity.
	 */
	public String save() {
		AMLBlock block = new AMLBlock();
		block.command = "Entity";
		if (this.dynamic) {
			if (this == world.player) {
				block.command = "Player";
			}
			// remove equipment modifiers
			removeEquipmentModifiers();
			// encode all the player's parameters
			block.addParameter("src", src);
			block.addParameter("atk-src", atksrc);
			block.addParameter("prt-src", prtsrc);

			block.addParameter("hp", hp.getValue() + "");
			block.addParameter("maxHp", hp.getMaxValue() + "");

			block.addParameter("ep", ep.getValue() + "");
			block.addParameter("maxEp", ep.getMaxValue() + "");

			block.addParameter("str", str.getBaseValue() + "");
			block.addParameter("dex", dex.getBaseValue() + "");
			block.addParameter("end", end.getBaseValue() + "");
			block.addParameter("wis", wis.getBaseValue() + "");
			
			block.addParameter("magic",magic.getBaseValue()+"");
			block.addParameter("armor",armor.getBaseValue()+"");

			block.addParameter("min", min + "");
			block.addParameter("max", max + "");

			block.addParameter("atk-inflicts", "" + atkInflicts);

			block.addParameter("peaceAi", peaceAi);
			block.addParameter("fightAi", fightAi);

			block.addParameter("items", writeItemCSV());
			block.addParameter("equipped", writeEquipementCSV());
			block.addParameter("statuses", writeStatusesCSV());

			block.addParameter("gold", gold + "");
			block.addParameter("level", level + "");
			block.addParameter("currentXP", xp.getValue() + "");
			block.addParameter("skillPointsLeft", skillPointsLeft + "");
			block.addParameter("team", team + "");

			if (onCollide != null) {
				block.addParameter("onCollide", onCollide.encode());
			}

			block.addParameter("dynamic", "true");
			// add equipment
			analyseEquipment();
		}
		if(!id.equals("unnamed")){
			block.addParameter("id", id);
		}
		block.addParameter("name", name);
		block.addParameter("x", x + "");
		block.addParameter("y", y + "");
		return block.encode();
	}

	/**
	 * Awards a given amount of XP to the Entity.
	 */
	public void awardXP(int amount) {
		int oldMax = xp.getMaxValue();
		int newVal = xp.getValue() + amount;
		if (newVal >= oldMax) {
			world.parent.trace("You have gained a level.");
			xp.changeModifier(50);
			xp.setValue(newVal - oldMax);
			level++;
			skillPointsLeft++;
		} else {
			xp.setValue(newVal);
		}
	}

}
