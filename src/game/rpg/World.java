package game.rpg;

import game.*;
import game.pathfinding.PathLocation;
import game.pathfinding.ValmarPathFinder;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.font.LineMetrics;
import java.util.Vector;

//documented

/**
 * World is the world in which Valmar occurs.
 * @author Adam
 */
public class World extends Renderable {

	// game
	private Vector<Vector<Slot>> slots;
	private String map;
	/**
	 * The World's ValmarRender.
	 */
	public ValmarRender parent;
	/**
	 * The World's Catalog.
	 */
	public Catalog catalog;
	/**
	 * The World's player.
	 */
	public Entity player;
	/**
	 * The World's player's selected target.
	 */
	public Entity selected;
	/**
	 * A boolean stating if a fight is occurring in the World.
	 */
	public boolean fighting = false;
	/**
	 * All the Entities in the World.
	 */
	public Vector<Entity> entities;

	// input
	private Point mouseCords;
	private ValmarListener listener;

	// diaplay
	private int width;
	private int height;
	private int screenX = 0;
	private int screenY = 0;
	private int screenWidth = 18;
	private int screenHeight = 18;
	private boolean shiftWasDown = false;

	// fighting
	/**
	 * The current Entity who's turn it is in the World.
	 */
	public Entity currentEntity;
	/**
	 * A boolean stating if the World's player is already performing a task.
	 */
	public boolean notActing = false;
	/**
	 * A boolean stating if it is the World's player's turn.
	 */
	public boolean yourTurn = true;
	private int fightIndex = 0;
	private boolean anyoneHasActed=false;
	

	// effects
	/**
	 * A boolean stating if an animation is occurring in the World.
	 */
	public boolean animating = false;
	/**
	 * The x coordinate of the animation in the World.
	 */
	public int animationX = 0;
	/**
	 * The y coordinate of the animation in the World.
	 */
	public int animationY = 0;
	/**
	 * The index of the image the animation in the World is displaying.
	 */
	public int animationIndex;
	/**
	 * The duration of the animation in the World in frames.
	 */
	public int animationDuration = 1;
	/**
	 * The text displayed by the animation in the World.
	 */
	public String animationText;
	/**
	 * The images the animation in the World displays.
	 */
	public Vector<String> animation;

	// core
	/**
	 * A boolean stating if the World is active.
	 */
	public boolean active = true;

	/**
	 * Makes a new World with a given ValmarRender, map, Catalog and ValmarListener.
	 */
	public World(ValmarRender game, String mapData, Catalog worldCatalog, ValmarListener listener) {
		super();
		parent = game;
		// catalog
		catalog = worldCatalog;
		// entites
		entities = new Vector<Entity>();
		// listener
		this.listener = listener;
		// setup map
		setMapCode(mapData);
	}

	/**
	 * Sets the map of the World.
	 */
	public void setMapCode(String mapData) {
		map = mapData;
		slots = new Vector<Vector<Slot>>();
		int tileX = 0;
		int tileY = 0;
		width = 0;
		height = 0;
		for (int n = 0; n < map.length(); n++) {
			if (map.charAt(n) == '\n' || map.charAt(n) == '\r') {
				if (tileX != 0) {
					tileY++;
					tileX = 0;
					height++;
				}
			} else {
				Terrain terrain = catalog.getTerrain("" + map.charAt(n));
				Vector<Slot> coloum;
				if (tileX < width) {
					coloum = slots.get(tileX);
				} else {
					coloum = new Vector<Slot>();
					slots.add(coloum);
					width++;
					if (tileY != 0) {
						System.out.println("Fuck!");
					}
				}
				TerrainInstance ter = new TerrainInstance(terrain, tileX, tileY);
				coloum.add(tileY, new Slot(tileX, tileY, this, ter));
				tileX++;

			}
		}
	}

	/**
	 * Paints the World.
	 */
	public void paint(Graphics g) {
		// render the map
		for (int yn = screenY; yn < height && yn < screenY + screenHeight; yn++) {
			Vector<Entity> visibleEntities = new Vector<Entity>();
			for (int xn = screenX; xn < width && xn < screenX + screenWidth; xn++) {
				Slot slot = getSlot(xn, yn);
				// render the terrain
				TerrainInstance terrain = slot.getTerrain();
				Image image = parent.getCache().getImage(terrain.terrain.src);
				drawImage(g, image, xn, yn);
				boolean ok = false;
				if (player != null) {
					if (entityCanSee(player, xn, yn)) {
						ok = true;
					}
				} else {
					ok = true;
				}
				if (ok) {
					// render the items
					Vector<ItemInstance> items = slot.getItems();
					for (int n = 0; n < items.size(); n++) {
						ItemInstance item = items.get(n);
						image = parent.getCache().getImage(item.item.src);
						drawImage(g, image, xn, yn);
					}
					// render the entity
					if (slot.getEntity() != null) {
						visibleEntities.add(slot.getEntity());
					}
				}
			}
			if (visibleEntities.size() != 0) {
				for (int n = 0; n < visibleEntities.size(); n++) {
					Entity entity = visibleEntities.get(n);
					Image image = parent.getCache().getImage(entity.getSrc());
					drawImage(g, image, entity.x, entity.y, entity.facing);
				}
			}
		}
		// out of sight
		if (player != null) {
			for (int xn = screenX; xn < width && xn < screenX + screenWidth; xn++) {
				for (int yn = screenY; yn < height && yn < screenY + screenHeight; yn++) {
					if (!entityCanSee(player, xn, yn)) {
						Image image = parent.getCache().getImage("images/misc/outofsight.png");
						drawImage(g, image, xn, yn);
					}
				}
			}
		}
		// selected
		if (player != null && mouseCords != null) {
			selected = null;
			Point local = gridToLocal(mouseCords);
			if (onScreen(mouseCords.x, mouseCords.y)) {
				Slot slot = getSlot(mouseCords.x, mouseCords.y);
				if (!slot.getTerrain().getBlocking()) {
					if (entityCanSee(player, mouseCords.x, mouseCords.y)) {
						g.setColor(new Color(192, 192, 192));
						if (slot.getEntity() != null) {
							selected = slot.getEntity();
							if (selected == player) {
								selected = null;
							} else if (selected.team == 2) {
								g.setColor(Color.RED);
							}
						}
						g.drawOval(local.x, local.y, 20, 20);
					}
				}
			}
		}
		// animation
		if (animating) {
			if (animation.size() > animationIndex) {
				Image image = parent.getCache().getImage(animation.get(animationIndex));
				drawImage(g, image, animationX, animationY);
			}
			if (animationText != null) {
				g.setColor(Color.WHITE);
				FontMetrics fontMets = g.getFontMetrics();
				LineMetrics mets = fontMets.getLineMetrics(animationText, g);
				Point str = gridToLocal(new Point(animationX, animationY));
				str.x += 10 - fontMets.stringWidth(animationText) / 2;
				str.y += mets.getHeight();
				g.drawString(animationText, str.x, str.y);
			}
		}
	}

	private void takeTurn() {
		if (fighting) {
			// move on to next entity
			fightIndex++;
			if (fightIndex >= entities.size()) {
				fightIndex = 0;
			}
			currentEntity = entities.get(fightIndex);
			// restore ap
			currentEntity.actionPoints.restore();
			currentEntity.turnPasses();
			// do something
			if (currentEntity == player) {
				//has somebody done anything?
				if(!anyoneHasActed){
					if(endFight()){
						return;
					}
				}
				//fight is ongoing
				anyoneHasActed=false;
				yourTurn = true;
			} else {
				if (isNear(currentEntity)) {
					anyoneHasActed=true;
					yourTurn = false;
					if (currentEntity.fightAi.equals("hunt")) {
						// pathfind
						if (isNextTo(currentEntity.x, currentEntity.y, player.x, player.y)) {
							currentEntity.path = null;
						} else {
							Vector<Point> path = getShortestBetweenEntities(currentEntity, player);
							if (path != null) {
								currentEntity.path = path;
								currentEntity.pathIndex = -1;
							} else {
								currentEntity.path = null;
							}
						}
					}else if (currentEntity.fightAi.equals("ranger")) {
						// pathfind
						if (entityCanSee(currentEntity, player.x, player.y)) {
							currentEntity.path = null;
						} else {
							Vector<Point> path = getShortestBetweenEntities(currentEntity, player);
							if (path != null) {
								currentEntity.path = path;
								currentEntity.pathIndex = -1;
							} else {
								currentEntity.path = null;
							}
						}
					}
				} else {
					currentEntity.actionPoints.deplete();
					takeTurn();
				}
			}
		} else {
			for (int n = 0; n < entities.size(); n++) {
				Entity entity = entities.get(n);
				if (entity == player) {
					// restore action points
					entity.actionPoints.restore();
					// entity.actionPoints.setValue(1);
					entity.turnPasses();
				} else {
					// if the entity is nearby
					if (disBetween(entity.x, entity.y, player.x, player.y) <= 10) {
						// restore action points
						entity.actionPoints.setValue(1);
						entity.turnPasses();
						// ai
						if (entity.peaceAi.equals("still")) {
							// don't do anything
							entity.spendActionPoint(1);
						} else if (entity.peaceAi.equals("random")) {
							// take a step in a random direction
							int amount = (int) Math.round(Math.random() * 2 - 1);
							if (Math.random() < 0.5) {
								moveEntity(entity, amount, 0);
							} else {
								moveEntity(entity, 0, amount);
							}
						}

					}
				}
			}
		}
	}

	/**
	 * Causes one Entity to attack another, displaying an animation in the World.
	 */
	public void attack(Entity actor, Entity target) {
		if (!animating) {
			// facing
			if (target.x > actor.x) {
				actor.facing = 1;
			} else if (target.x < actor.x) {
				actor.facing = -1;
			}
			actor.spendActionPoint(8);

			// Does it hit?
			int atk = actor.attackRoll();
			int dg = target.getDodge();
			// System.out.println(actor.name+" attacks "+target.name);
			// System.out.println(atk+" "+dg);
			if (atk > dg) {
				// damage
				int damage = actor.getDamage();
				damage = target.takeDamage(damage);
				actor.inflictStatusesOn(target);

				// animation
				actor.pose = 1;
				animationText = "" + damage;
				animationX = target.x;
				animationY = target.y;
				animating = true;
				animation = new Vector<String>();
				animationDuration = 1;
				animation.add("images/effects/blood_1.png");
				animation.add("images/effects/blood_2.png");
				animation.add("images/effects/blood_3.png");
				animation.add("images/effects/blood_4.png");
				animationIndex = 0;

				// Does it die?
				if (target.hp.getValue() <= 0) {
					if (target == player) {
						// stops the game
						yourTurn=true;
					} else {
						killEntity(target);
					}
				}
			} else {
				// miss animation
				actor.pose = 1;
				animationX = target.x;
				animationY = target.y;
				animating = true;
				animationText = "Miss";
				animation = new Vector<String>();
				animationDuration = 4;
				animationIndex = 0;
			}
		}
	}

	/**
	 * Kills an Entity in the World.
	 */
	public void killEntity(Entity target) {
		// drop gold
		if (target.gold > 0) {
			Item gold = catalog.getItem("Gold");
			ItemInstance golds = new ItemInstance(gold, target.x, target.y);
			golds.setQuantity(target.gold);
			addItem(golds);
		}
		// drop items
		Vector<ItemInstance> its = target.getItems();
		for (int n = 0; n < its.size(); n++) {
			ItemInstance it = its.get(n);
			it.x = target.x;
			it.y = target.y;
			addItem(it);
		}
		//drop equipment
		its = target.getEquipment();
		for (int n = 0; n < its.size(); n++) {
			ItemInstance it = its.get(n);
			if(it!=null){
				it.x = target.x;
				it.y = target.y;
				addItem(it);
			}
		}
		// the target dies
		if (target.team == 2) {
			parent.trace("You have defeated "+target.name+" earning "+(5*target.level)+" XP.");
			player.awardXP(5 * target.level);
		}
		// remove entity
		removeEntity(target);
		// end fight
		endFight(); 
	}

	/**
	 * Runs the effect of an item in the World used by the World's player.
	 */
	public void itemEffect(String effect, Item item) {
		if (!notActing && yourTurn && player.actionPoints.getValue() >= 0) {
			if (effect.equals("Purge")) {
				// Totally heals you.
				player.hp.restore();
				expendItem();
			} else if (effect.equals("Heal")) {
				// Heals you.
				player.hp.setValue(player.hp.getValue() + item.getEffectValue());
				expendItem();
			} else if (effect.equals("Envigorate")) {
				// Heals you.
				player.ep.setValue(player.ep.getValue() + item.getEffectValue());
				expendItem();
			}
		}
	}

	/**
	 * Uses up an Item used by the World's player.
	 */
	public void expendItem() {
		parent.getInventory().expendSelected();
		player.spendActionPoint(4);
	}

	/**
	 * Causes the World to respond to input.
	 */
	public void tick() {
		if (active) {
			if (parent.getListener().isKeyPressed(KeyEvent.VK_SPACE)) {
				parent.trace(player.x + ", " + player.y);
			}
			// show highlighted square
			mouseCords = globalToGrid(parent.getListener().getMouseCord());

			// move the hero
			if (animating) {
				if (animationIndex >= animation.size() && animationIndex > animationDuration) {
					animating = false;
				}
				animationIndex++;
			} else {
				if (currentEntity != null) {
					currentEntity.pose = 0;
				}
				if (yourTurn) {
					if (player != null) {
						if (player.hp.getValue() == 0) {
							parent.playerDied();
						}
						// check for detection
						boolean ok = false;
						if (!fighting) {
							// check for detection
							ok = canAnEnemySeeMe();
						}
						// continue
						if (ok) {
							startFight();
						} else if (player.actionPoints.getValue() <= 0) {
							if (fighting) {
								player.path = null;
								notActing = false;
							}
							takeTurn();
						} else if (!notActing) {
							if (listener.isKeyDown(KeyEvent.VK_SHIFT)) {
								// move the screen
								if (listener.isKeyDown(KeyEvent.VK_RIGHT)) {
									moveScreen(1, 0);
								} else if (listener.isKeyDown(KeyEvent.VK_LEFT)) {
									moveScreen(-1, 0);
								} else if (listener.isKeyDown(KeyEvent.VK_DOWN)) {
									moveScreen(0, 1);
								} else if (listener.isKeyDown(KeyEvent.VK_UP)) {
									moveScreen(0, -1);
								}
								shiftWasDown = true;
							} else {
								// shift isn't down
								if (shiftWasDown) {
									shiftWasDown = false;
									centerScreenOn(player.x, player.y);
								}
								// now what

								if (listener.isKeyDown(KeyEvent.VK_RIGHT)) {
									moveEntity(player, 1, 0);
									centerScreenOn(player.x, player.y);
								} else if (listener.isKeyDown(KeyEvent.VK_LEFT)) {
									moveEntity(player, -1, 0);
									centerScreenOn(player.x, player.y);
								} else if (listener.isKeyDown(KeyEvent.VK_DOWN)) {
									moveEntity(player, 0, 1);
									centerScreenOn(player.x, player.y);
								} else if (listener.isKeyDown(KeyEvent.VK_UP)) {
									moveEntity(player, 0, -1);
									centerScreenOn(player.x, player.y);
								} else if (onScreen(mouseCords.x, mouseCords.y)) {
									if (listener.isMousePressing()) {
										Slot slot = getSlot(mouseCords.x, mouseCords.y);
										Vector<Point> points;
										if (slot.getBlocking()) {
											if (slot.getEntity() != null) {
												Entity target = slot.getEntity();
												if (target != player && target.team == 2) {
													if (fighting) {
														if (isNextTo(target.x, target.y, player.x, player.y)) {
															// melee attack
															attack(player, target);
														} else {
															points = getShortestBetweenEntities(player, target);
															if (points != null) {
																player.path = points;
																player.pathIndex = -1;
																player.target = target;
																notActing = true;
															}
														}
													}
												} else {
													// you are walking into
													// somebody
													if (isNextTo(mouseCords.x, mouseCords.y, player.x, player.y)) {
														tryToMoveTo(player, mouseCords.x, mouseCords.y);
														centerScreenOn(player.x, player.y);
													}
												}
											} else {
												// you are walking into a wall
												if (isNextTo(mouseCords.x, mouseCords.y, player.x, player.y)) {
													tryToMoveTo(player, mouseCords.x, mouseCords.y);
													centerScreenOn(player.x, player.y);
												}
											}
										} else {
											ValmarPathFinder pathFinder = new ValmarPathFinder(this);
											// add paths
											boolean success = pathFinder.pathFind(new Point(player.x, player.y), mouseCords, 16);
											if (success) {
												points = new Vector<Point>();
												for (int n = 0; n < pathFinder.path.size(); n++) {
													points.add(pathFinder.path.get(n).point);
												}
												player.path = points;
												player.pathIndex = -1;
												notActing = true;
											}
										}
									}
								}
							}
						} else {
							player.pathIndex++;
							if (player.pathIndex >= player.path.size()) {
								if (player.target != null) {
									attack(player, player.target);
								}
								player.path = null;
								player.target = null;
								notActing = false;
							} else {
								// move the player to its next spot on its path
								Point inPath = player.path.get(player.pathIndex);
								moveEntityTo(player, inPath.x, inPath.y);
								centerScreenOn(player.x, player.y);
							}
						}
					}
				} else {
					// somebody else's turn
					int points = currentEntity.actionPoints.getValue();
					if (points > 0) {
						if (currentEntity.fightAi.equals("still")) {
							currentEntity.spendActionPoint(8);
						} else if (currentEntity.fightAi.equals("random")) {
							// take a step in a random direction
							int amount = (int) Math.round(Math.random() * 2 - 1);
							if (Math.random() < 0.5) {
								moveEntity(currentEntity, amount, 0);
							} else {
								moveEntity(currentEntity, 0, amount);
							}
						} else if (currentEntity.fightAi.equals("drunk")) {
							if (isNextTo(player.x, player.y, currentEntity.x, currentEntity.y)) {
								// add attack code
								attack(currentEntity, player);
							} else {
								// take a step in a random direction
								int amount = (int) Math.round(Math.random() * 2 - 1);
								if (Math.random() < 0.5) {
									moveEntity(currentEntity, amount, 0);
								} else {
									moveEntity(currentEntity, 0, amount);
								}
							}
						} else if (currentEntity.fightAi.equals("turret")) {
							currentEntity.spendActionPoint(8);
							if (entityCanSee(currentEntity, player.x, player.y)) {
								// add attack code
								// player.hp.setValue(player.hp.getValue()-1);
							}
						} else if (currentEntity.fightAi.equals("hunt") || currentEntity.fightAi.equals("ranger")) {
							if (currentEntity.path != null) {
								if ((currentEntity.fightAi.equals("ranger") && entityCanSee(currentEntity, player.x, player.y))) {
									attack(currentEntity, player);
								} else {
									currentEntity.pathIndex++;
									if (currentEntity.pathIndex >= currentEntity.path.size()) {
										// add attack code
										attack(currentEntity, player);
									} else {
										Point inPath = currentEntity.path.get(currentEntity.pathIndex);
										moveEntityTo(currentEntity, inPath.x, inPath.y);
									}
								}
							} else {
								// no path exists
								if ((currentEntity.fightAi.equals("hunt") && isNextTo(player.x, player.y, currentEntity.x, currentEntity.y))
										|| (currentEntity.fightAi.equals("ranger") && entityCanSee(currentEntity, player.x, player.y))) {
									// add attack code
									attack(currentEntity, player);
								} else {
									// stand still
									currentEntity.spendActionPoint(8);
								}
							}
						}
					} else {
						// Who's next?
						takeTurn();
					}
				}
			}
		}
	}

	/**
	 * Starts a fight in the World.
	 */
	public void startFight() {
		if (player != null) {
			if (!fighting) {
				notActing = false;
				fighting = true;
				yourTurn = false;
				fightIndex = -1;
				takeTurn();
			}
		}
	}

	/**
	 * Tries to end a fight in the World returning a boolean stating if the fight was ended.
	 */
	public boolean endFight() {
		if (fighting) {
			if (!isAnEnemyNerby()) {
				fighting = false;
				yourTurn = true;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Determines if a specified entity is near the player.
	 */
	public boolean isNear(Entity entity) {
		if (entity != player && disBetween(player.x, player.y, entity.x, entity.y) < 10) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Determines if a hostile entity is the near the player.
	 */
	public boolean isAnEnemyNerby() {
		boolean ok = false;
		for (int n = 0; n < entities.size(); n++) {
			Entity entity = entities.get(n);
			if (entity.team == 2) {
				if (isNear(entity)) {
					ok = true;
					break;
				}
			}
		}
		return ok;
	}
	
	/**
	 * Determines if a hostile entity can see the player.
	 */
	public boolean canAnEnemySeeMe() {
		boolean ok = false;
		for (int n = 0; n < entities.size(); n++) {
			Entity entity = entities.get(n);
			if (entity != player && entity.team == 2) {
				if (entityCanSee(player, entity.x, entity.y)) {
					if (player.x > entity.x) {
						entity.facing = 1;
					} else if (player.x < entity.x) {
						entity.facing = -1;
					}
					if (!ok) {
						ok = true;

					}
				}
			}
		}
		return ok;
	}
	
	/**
	 * Returns the first entity with a given id.
	 */
	public Entity getEntityWithId(String id){
		if(id.equals("unnamed")){
			return null;
		}else{
			for(int n=0;n<entities.size();n++){
				if(entities.get(n).id.equals(id)){
					return entities.get(n);
				}
			}
			return null;
		}
	}
	
	/**
	 * Returns the shortest path between two Entities in the World.
	 */
	public Vector<Point> getShortestBetweenEntities(Entity mover, Entity target) {
		ValmarPathFinder pathFinder = new ValmarPathFinder(this);
		Vector<Vector<PathLocation>> paths = new Vector<Vector<PathLocation>>();
		Point start = new Point(mover.x, mover.y);

		// add paths
		boolean success = pathFinder.pathFind(start, new Point(target.x - 1, target.y), 16);
		if (success) {
			paths.add(pathFinder.path);
		}
		success = pathFinder.pathFind(start, new Point(target.x + 1, target.y), 16);
		if (success) {
			paths.add(pathFinder.path);
		}
		success = pathFinder.pathFind(start, new Point(target.x, target.y - 1), 16);
		if (success) {
			paths.add(pathFinder.path);
		}
		success = pathFinder.pathFind(start, new Point(target.x, target.y + 1), 16);
		if (success) {
			paths.add(pathFinder.path);
		}

		if (paths.size() == 0) {
			// no path found
			return null;
		} else {
			int smallestSize = Integer.MAX_VALUE;
			int index = -1;
			for (int n = 0; n < paths.size(); n++) {
				if (paths.get(n).size() < smallestSize) {
					index = n;
					smallestSize = paths.get(n).size();
				}
			}
			Vector<PathLocation> path = paths.get(index);
			Vector<Point> points = new Vector<Point>();
			for (int n = 0; n < path.size(); n++) {
				points.add(path.get(n).point);
			}
			return points;
		}
	}

	/**
	 * Returns a boolean stating if an Entity in the World can see a coordinate.
	 */
	public boolean entityCanSee(Entity entity, int x, int y) {
		if (disBetween(x, y, entity.x, entity.y) <= 7) {
			if (lineofSight(entity.x, entity.y, x, y)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Determines if there is a line of sight between two coordinates in the World.
	 * This code was converted from ActionScript code designed to raster lines,
	 * from the url: http://www.bytearray.org/?p=67
	 */
	public boolean lineofSight(int x0, int y0, int x1, int y1) {
		boolean last = false;
		int dx;
		int dy;
		int i;
		int xinc;
		int yinc;
		int cumul;
		int x;
		int y;
		x = x0;
		y = y0;
		dx = x1 - x0;
		dy = y1 - y0;
		xinc = (dx > 0) ? 1 : -1;
		yinc = (dy > 0) ? 1 : -1;
		dx = dx < 0 ? -dx : dx;
		dy = dy < 0 ? -dy : dy;
		if (dx > dy) {
			cumul = dx >> 1;
			for (i = 1; i <= dx; ++i) {
				x += xinc;
				cumul += dy;
				if (cumul >= dx) {
					cumul -= dx;
					y += yinc;
				}
				// are we blocked
				if (x != x0 || y != y0) {
					if (getOpaque(x, y) || last) {
						if (last) {
							return false;
						} else {
							last = true;
						}
					}
				}
			}
		} else {
			cumul = dy >> 1;
			for (i = 1; i <= dy; ++i) {
				y += yinc;
				cumul += dx;
				if (cumul >= dy) {
					cumul -= dy;
					x += xinc;
				}
				if (x != x0 || y != y0) {
					if (getOpaque(x, y) || last) {
						if (last) {
							return false;
						} else {
							last = true;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Returns the distance between two coordinates in the World.
	 */
	public float disBetween(int x0, int y0, int x1, int y1) {
		float dx = x0 - x1;
		float dy = y0 - y1;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Determines if two squares in the World are adjacent.
	 */
	public boolean isNextTo(int x0, int y0, int x1, int y1) {
		if (Math.abs(x0 - x1) + Math.abs(y0 - y1) < 2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns a vector of the adjacent Slots to the specified coordinate in the World.
	 */
	public Vector<Slot> reachableSlots(int x, int y) {
		Vector<Slot> adjs = new Vector<Slot>();
		Slot slot = getSlot(x, y);
		if (slot != null) {
			adjs.add(slot);
		}
		slot = getSlot(x + 1, y);
		if (slot != null) {
			adjs.add(slot);
		}
		slot = getSlot(x - 1, y);
		if (slot != null) {
			adjs.add(slot);
		}
		slot = getSlot(x, y + 1);
		if (slot != null) {
			adjs.add(slot);
		}
		slot = getSlot(x, y - 1);
		if (slot != null) {
			adjs.add(slot);
		}
		return adjs;
	}

	/**
	 * Determines if a point in the World is on being rendered on the screen.
	 */
	public boolean onScreen(int x, int y) {
		if (x >= screenX && y >= screenY && x < screenX + screenWidth && y < (screenY + screenHeight - 1)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Converts a point from the World's local coordinate scheme to the World's grid's coordinate scheme.
	 */
	public Point localToGrid(Point po) {
		Point point = new Point();
		float ex = ((float) po.x) / 20f + screenX;
		float ey = ((float) po.y) / 20f + screenY;
		if (ex < 0) {
			ex--;
		}
		if (ey < 0) {
			ey--;
		}
		point.x = (int) ex;
		point.y = (int) ey;
		return point;
	}

	/**
	 * Converts a point from the World's grid's coordinate scheme to the World's local coordinate scheme.
	 */
	public Point gridToLocal(Point po) {
		Point point = new Point();
		point.x = (po.x - screenX) * 20;
		point.y = (po.y - screenY) * 20;
		return point;
	}

	/**
	 * Converts a point from the World's grid's coordinate scheme to the global coordinate scheme.
	 */
	public Point gridToGlobal(Point po) {
		return localToGlobal(gridToLocal(po));
	}

	/**
	 * Converts a point from the global coordinate scheme to the World's grid's coordinate scheme.
	 */
	public Point globalToGrid(Point po) {
		return localToGrid(globalToLocal(po));
	}

	/**
	 * Draws an image at the specified grid coordinate.
	 */
	public void drawImage(Graphics g, Image image, int x, int y) {
		int height = image.getHeight(parent.getObserver());
		g.drawImage(image, (x - screenX) * 20, (y - screenY) * 20 + (20 - height), parent.getObserver());
	}

	/**
	 * Draws an image at that specified grid coordinate and flip -> 1 render normally, flip -> -1 flip horizontally.
	 */
	public void drawImage(Graphics g, Image image, int x, int y, int flip) {
		int height = image.getHeight(parent.getObserver());
		int width = image.getWidth(parent.getObserver()) * flip;
		int adj = 0;
		if (flip == -1) {
			adj = 1;
		}
		g.drawImage(image, (x - screenX + adj) * 20, (y - screenY) * 20 + (20 - height), width, height, parent.getObserver());
	}

	/**
	 * Adds a ItemInstance to the World.
	 */
	public ItemInstance addItem(ItemInstance item) {
		if (onGrid(item.x, item.y)) {
			Slot slot = getSlot(item.x, item.y);
			slot.addItem(item);
		}
		return item;
	}

	/**
	 * Removes an ItemInstance from the World.
	 */
	public ItemInstance removeItem(ItemInstance item) {
		if (onGrid(item.x, item.y)) {
			Slot slot = getSlot(item.x, item.y);
			slot.removeItem(item);
		}
		return item;
	}

	/**
	 * Adds an Entity to the World.
	 */
	public Entity addEntity(Entity entity) {
		boolean ok = false;
		if (onGrid(entity.x, entity.y)) {
			Slot slot = getSlot(entity.x, entity.y);
			if (!slot.getBlocking()) {
				slot.addEntity(entity);
				entities.add(entity);
				ok = true;
			}
		}
		if (ok) {
			sortInitiative();
			return entity;
		} else {
			return null;
		}
	}

	/**
	 * Removes an Entity from the World.
	 */
	public Entity removeEntity(Entity entity) {
		if (onGrid(entity.x, entity.y)) {
			Slot slot = getSlot(entity.x, entity.y);
			if (slot.getEntity() != null) {
				slot.removeEntity(entity);
				entities.remove(entity);
			}
		}
		sortInitiative();
		return entity;
	}

	/**
	 * Temporarily removes an Entity from the World.
	 */
	public Entity tempRemoveEntity(Entity entity) {
		if (onGrid(entity.x, entity.y)) {
			Slot slot = getSlot(entity.x, entity.y);
			if (slot.getEntity() != null) {
				slot.removeEntity(entity);
			}
		}
		return entity;
	}

	/**
	 * Re adds a temporarily removed Entity to the World.
	 */
	public Entity tempAddEntity(Entity entity) {
		if (onGrid(entity.x, entity.y)) {
			Slot slot = getSlot(entity.x, entity.y);
			if (slot.getEntity() == null) {
				slot.addEntity(entity);
			}
		}
		return entity;
	}

	/**
	 * Teleports an Entity in the World to the specified coordinates if they are at a leagal location.
	 */
	public Entity moveEntityTo(Entity entity, int x, int y) {
		// spend action points
		int dx = entity.x - x;
		entity.spendActionPoint((int) (Math.abs(dx) + Math.abs(entity.y - y)));
		// move
		if (!tryToMoveTo(entity, x, y)) {
			tempRemoveEntity(entity);
			entity.x = x;
			entity.y = y;
			tempAddEntity(entity);
		}
		if (dx < 0) {
			entity.facing = 1;
		} else if (dx > 0) {
			entity.facing = -1;
		}
		return entity;
	}

	/**
	 * Moves an Entity in the World by x to the right and y down if they end in a legal location.
	 */
	public Entity moveEntity(Entity entity, int x, int y) {
		// spend action points
		entity.spendActionPoint((int) Math.abs(x + y));
		// move
		if (!tryToMoveTo(entity, entity.x + x, entity.y + y)) {
			tempRemoveEntity(entity);
			entity.x += x;
			entity.y += y;
			tempAddEntity(entity);
		}
		// adjust facing
		if (x > 0) {
			entity.facing = 1;
		} else if (x < 0) {
			entity.facing = -1;
		}
		return entity;
	}

	/**
	 * Sorts the order of iniative of the Entities in the World.
	 */
	public void sortInitiative() {
		Vector<Entity> copy = new Vector<Entity>();
		for (int n = 0; n < entities.size(); n++) {
			copy.add(entities.get(n));
		}
		entities = new Vector<Entity>();
		for (int n = 0; n < copy.size(); n++) {
			int x = 0;
			while (true) {
				if (x >= entities.size()) {
					entities.add(copy.get(n));
					break;
				} else if (entities.get(x).getInitiative() < copy.get(n).getInitiative()) {
					entities.insertElementAt(copy.get(n), x);
					break;
				}
				x++;
			}
		}
	}

	/**
	 * Determines if a coordinate is on the map of the World.
	 */
	public Boolean onGrid(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Gets a slot from the World at a given coordinate.
	 */
	public Slot getSlot(int x, int y) {
		if (!onGrid(x, y)) {
			return null;
		} else {
			return slots.get(x).get(y);
		}
	}

	/**
	 * Tries to move an Entity to a possibly legal location returning a boolean stating if the move was successful.
	 */
	public boolean tryToMoveTo(Entity entity, int x, int y) {
		if (!onGrid(x, y)) {
			return true;
		} else {
			boolean blocking = getBlocking(x, y);
			if (blocking) {
				Slot slot = getSlot(x, y);
				slot.collideWith(entity);
			}
			return blocking;
		}
	}

	/**
	 * Returns if the slot at the specified coordinate in the World is impassable.
	 */
	public boolean getBlocking(int x, int y) {
		if (!onGrid(x, y)) {
			return true;
		} else {
			Vector<Slot> coloum = slots.get(x);
			Slot slot = coloum.get(y);
			return slot.getBlocking();
		}
	}

	/**
	 * Returns if the slot at the specified coordinate in the World can be seen through.
	 */
	public boolean getOpaque(int x, int y) {
		if (!onGrid(x, y)) {
			return true;
		} else {
			Vector<Slot> coloum = slots.get(x);
			Slot slot = coloum.get(y);
			return slot.getOpaque();
		}
	}

	/**
	 * Centres the screen on a specified coordinate in the World.
	 */
	public void centerScreenOn(int x, int y) {
		setScreenX(x - 9);
		setScreenY(y - 8);
	}

	/**
	 * Moves the screen moveX to the right and moveY down.
	 */
	public void moveScreen(int moveX, int moveY) {
		setScreenX(screenX + moveX);
		setScreenY(screenY + moveY);
	}

	/**
	 * Sets the x coordinate of the screen.
	 */
	public void setScreenX(int x) {
		screenX = x;
		if (screenX > width - screenWidth) {
			screenX = width - screenWidth;
		}
		if (screenX < 0) {
			screenX = 0;
		}
	}

	/**
	 * Sets the y coordinate of the screen.
	 */
	public void setScreenY(int y) {
		screenY = y;
		if (screenY > height - screenHeight + 1) {
			screenY = height - screenHeight + 1;
		}
		if (screenY < 0) {
			screenY = 0;
		}
	}

	/**
	 * Returns the String of the map file that can be parsed to return the World.
	 */
	public String save() {
		// map
		String fil = map + "#\n";
		// contents
		for (int xn = 0; xn < width; xn++) {
			for (int yn = 0; yn < height; yn++) {
				fil += getSlot(xn, yn).save();
			}
		}
		//
		return fil;
	}

}
