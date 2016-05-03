package game;

import game.rpg.Entity;
import game.rpg.Item;
import game.rpg.ItemInstance;
import game.rpg.Slot;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Vector;

//documented

/**
 * Inventory is the visual representation of the player's Inventory.
 * @author Adam
 */
public class Inventory extends Renderable implements ValmarButtonListener {

	private Vector<ItemInstance> grounds;
	private Vector<ItemInstance> items;
	private Vector<ValmarButton> buttons;
	private Vector<ItemInstance> equipment;
	private Vector<ItemInstance> stores;
	private Entity storeOwner;
	private ItemInstance selectedItem;
	private ValmarRender parent;
	private ValmarButton drop;
	private ValmarButton grab;
	private ValmarButton equip;
	private ValmarButton buy;
	private Item gold;
	private ValmarButton unequip;
	private ValmarButton sell;
	private ValmarButton buyAll;
	private ValmarButton sellAll;
	private ValmarButton dex;
	private ValmarButton end;
	private ValmarButton str;
	private ValmarButton wis;

	/**
	 * Makes a new Inventory for the specified ValmarRender.
	 */
	public Inventory(ValmarRender game) {
		parent = game;
		items = new Vector<ItemInstance>();
		grounds = new Vector<ItemInstance>();
		equipment = new Vector<ItemInstance>();
		buttons = new Vector<ValmarButton>();
		selectedItem = null;

		drop = new ValmarButton(new Rectangle(290, 25, 20, 20),
				"images/icons/nograb.png", "drop");
		drop.hotkey = KeyEvent.VK_D;
		drop.hotKeyName = "d";
		drop.altText = "Drop";
		buttons.add(drop);

		grab = new ValmarButton(new Rectangle(320, 25, 20, 20),
				"images/icons/grab.png", "grab");
		grab.hotkey = KeyEvent.VK_G;
		grab.hotKeyName = "g";
		grab.altText = "Grab";
		buttons.add(grab);

		equip = new ValmarButton(new Rectangle(290, 55, 20, 20),
				"images/icons/equip.png", "equip");
		equip.hotkey = KeyEvent.VK_E;
		equip.hotKeyName = "e";
		equip.altText = "Equip";
		buttons.add(equip);

		unequip = new ValmarButton(new Rectangle(320, 55, 20, 20),
				"images/icons/unequip.png", "unequip");
		unequip.hotkey = KeyEvent.VK_U;
		unequip.hotKeyName = "u";
		unequip.altText = "Unequip";
		buttons.add(unequip);

		buy = new ValmarButton(new Rectangle(290, 85, 20, 20),
				"images/icons/buy.png", "buy");
		buy.hotkey = KeyEvent.VK_B;
		buy.hotKeyName = "b";
		buy.altText = "Buy";
		buttons.add(buy);

		sell = new ValmarButton(new Rectangle(320, 85, 20, 20),
				"images/icons/sell.png", "sell");
		sell.hotkey = KeyEvent.VK_S;
		sell.hotKeyName = "s";
		sell.altText = "Sell";
		buttons.add(sell);

		buyAll = new ValmarButton(new Rectangle(290, 115, 20, 20),
				"images/icons/buyall.png", "buyAll");
		buyAll.hotkey = KeyEvent.VK_L;
		buyAll.hotKeyName = "l";
		buyAll.altText = "Buy All";
		buttons.add(buyAll);

		sellAll = new ValmarButton(new Rectangle(320, 115, 20, 20),
				"images/icons/sellall.png", "sellAll");
		sellAll.hotkey = KeyEvent.VK_A;
		sellAll.hotKeyName = "a";
		sellAll.altText = "Sell All";
		buttons.add(sellAll);
		
		str = new ValmarButton(new Rectangle(105,180,20,20), "images/icons/strength.png", "str");
		str.altText="Strength";
		str.hotKeyName="v";
		str.hotkey=KeyEvent.VK_V;
		buttons.add(str);
		
		wis = new ValmarButton(new Rectangle(105,205,20,20), "images/icons/intellegence.png", "wis");
		wis.altText="Wisdom";
		wis.hotKeyName="w";
		wis.hotkey=KeyEvent.VK_W;
		buttons.add(wis);
		
		dex = new ValmarButton(new Rectangle(105,230,20,20), "images/icons/hand.png", "dex");
		dex.altText="Dexterity";
		dex.hotKeyName="x";
		dex.hotkey=KeyEvent.VK_X;
		buttons.add(dex);
		
		end = new ValmarButton(new Rectangle(105,255,20,20), "images/icons/endurance.png", "end");
		end.altText="Endurance";
		end.hotKeyName="c";
		end.hotkey=KeyEvent.VK_C;
		buttons.add(end);

		gold = parent.getWorld().catalog.getItem("Gold");
	}

	/**
	 * Paints the Inventory.
	 */
	public void paint(Graphics g) {
		// backing
		Image image = parent.getCache().getImage("images/ui/graybacking.png");
		g.drawImage(image, 0, 0, parent.getObserver());

		// read input
		Point mouseCord = globalToLocal(parent.getListener().getMouseCord());

		// labels
		g.setColor(Color.BLACK);
		parent.drawMultilineText(g, "Items", 50, 0, true);
		parent.drawMultilineText(g, "Ground", 140, 0, true);
		if (stores != null) {
			parent.drawMultilineText(g, storeOwner.name + "'s Store", 230, 0,
					true);
		}
		parent.drawMultilineText(g, "Commands", 320, 0, true);
		parent.drawMultilineText(g, "" + parent.getWorld().player.gold, 30, 133);
		if (selectedItem != null) {
			parent.drawMultilineText(g, selectedItem.getDescription(), 190,
					150, false);
		}
		
		//stats
		parent.drawMultilineText(g, "Stats", 105, 160);
		parent.drawMultilineText(g, parent.getWorld().player.str.toModified(), 125, 180);
		parent.drawMultilineText(g, parent.getWorld().player.wis.toModified(), 125, 205);
		parent.drawMultilineText(g, parent.getWorld().player.dex.toModified(), 125, 230);
		parent.drawMultilineText(g, parent.getWorld().player.end.toModified(), 125, 255);
		parent.drawMultilineText(g, "Points Left: "+parent.getWorld().player.skillPointsLeft, 5, 320);
		
		// borders
		g.drawRect(10, 25, 80, 100);
		g.drawRect(100, 25, 80, 100);
		if (stores != null) {
			g.drawRect(190, 25, 80, 100);
		}

		// draw the gold icon
		image = parent.getCache().getImage(gold.src);
		g.drawImage(image, 10, 135, parent.getObserver());

		// variables
		ItemInstance item;
		Point po;

		// items
		for (int n = 0; n < items.size(); n++) {
			item = items.get(n);
			po = getRelativeItemCords(n);
			drawItem(g, item, 10 + po.x, 25 + po.y);
		}

		// grounds
		for (int n = 0; n < grounds.size() && n < 20; n++) {
			item = grounds.get(n);
			po = getRelativeItemCords(n);
			drawItem(g, item, 100 + po.x, 25 + po.y);
		}

		// equipment slots
		image = parent.getCache().getImage("images/ui/equipmentslots.png");
		g.drawImage(image, 10, 160, parent.getObserver());

		// draw the player's equipped items
		for (int n = 0; n < equipment.size(); n++) {
			if (equipment.get(n) != null) {
				po = getequippedItemCords(n);
				drawItem(g, equipment.get(n), po.x, po.y);
			}
		}

		// draw the store items
		if (stores != null) {
			for (int n = 0; n < stores.size(); n++) {
				item = stores.get(n);
				po = getRelativeItemCords(n);
				drawItem(g, item, 190 + po.x, 25 + po.y);
			}
		}

		// draws the buttons
		for (int n = 0; n < buttons.size(); n++) {
			buttons.get(n).render(g, parent, mouseCord);
		}
	}

	private Point getequippedItemCords(int index) {
		Point po = new Point(10, 160);
		if (index == 0) {
			// weapon
			po.x += 4;
			po.y += 31;
		} else if (index == 1) {
			// sheild
			po.x += 55;
			po.y += 31;
		} else if (index == 2) {
			// hat
			po.x += 29;
			po.y += 5;
		} else if (index == 3) {
			// torso
			po.x += 29;
			po.y += 31;
		} else if (index == 4) {
			// boots
			po.x += 4;
			po.y += 57;
		}
		return po;
	}

	private Point getRelativeItemCords(int index) {
		return new Point(((index - (index % 5)) / 5) * 20, (index % 5) * 20);
	}

	private void drawItem(Graphics g, ItemInstance item, int x, int y) {
		Image image;
		// usable
		if (!item.item.effect.equals("")) {
			image = parent.getCache().getImage("images/icons/use.png");
			g.drawImage(image, x, y + 12, parent.getObserver());
		}
		// image
		image = parent.getCache().getImage(item.item.src);
		g.drawImage(image, x, y, parent.getObserver());
		// border
		if (selectedItem == item) {
			g.setColor(Color.BLUE);
		} else {
			g.setColor(Color.BLACK);
		}
		g.drawRect(x, y, 20, 20);
		// draw the number of the item there are
		if (item.getStacks()) {
			// find the number
			String num = "" + "" + item.getQuantity();
			// setup
			FontMetrics fontMets = g.getFontMetrics();
			Rectangle bounds = fontMets.getStringBounds(num, g).getBounds();
			// positioning
			int lineY = y + 20;
			int lineX = x + 20 - bounds.width;
			// draw string
			g.setColor(Color.BLACK);
			g.drawString(num, lineX, lineY);
		}
	}

	/**
	 * Makes the Inventory responds to input.
	 */
	public void tick() {
		Point mouseCord = globalToLocal(parent.getListener().getMouseCord());
		Rectangle bounds;
		Point po;
		// select items
		for (int n = 0; n < items.size(); n++) {
			po = getRelativeItemCords(n);
			bounds = new Rectangle(10 + po.x, 25 + po.y, 20, 20);
			if (bounds.contains(mouseCord)) {
				if (parent.getListener().isMousePressing()) {
					ItemInstance i = items.get(n);
					Rectangle use = new Rectangle(bounds.x, bounds.y + 12, 8, 8);
					if (use.contains(mouseCord) && !i.item.effect.equals("")) {
						// select the item
						selectedItem = i;
						// return to the game
						parent.backToGame();
						// run the item's effect
						parent.getWorld().itemEffect(i.item.effect, i.item);
						//
					} else {
						if (i == selectedItem) {
							selectedItem = null;
						} else {
							selectedItem = i;
						}
					}
				}
			}
		}
		// select the grounds
		for (int n = 0; n < grounds.size() && n < 20; n++) {
			po = getRelativeItemCords(n);
			bounds = new Rectangle(100 + po.x, 25 + po.y, 20, 20);
			if (bounds.contains(mouseCord)) {
				if (parent.getListener().isMousePressing()) {
					if (grounds.get(n) == selectedItem) {
						selectedItem = null;
					} else {
						selectedItem = grounds.get(n);
					}
				}
			}
		}
		// select the equipped items
		for (int n = 0; n < equipment.size(); n++) {
			po = getequippedItemCords(n);
			bounds = new Rectangle(po.x, po.y, 20, 20);
			if (bounds.contains(mouseCord)) {
				if (parent.getListener().isMousePressing()) {
					if (equipment.get(n) == selectedItem) {
						selectedItem = null;
					} else {
						selectedItem = equipment.get(n);
					}
				}
			}
		}
		// select store items
		if (stores != null) {
			for (int n = 0; n < stores.size(); n++) {
				po = getRelativeItemCords(n);
				bounds = new Rectangle(190 + po.x, 25 + po.y, 20, 20);
				if (bounds.contains(mouseCord)) {
					if (parent.getListener().isMousePressing()) {
						if (stores.get(n) == selectedItem) {
							selectedItem = null;
						} else {
							selectedItem = stores.get(n);
						}
					}
				}
			}
		}

		// buttons
		for (int n = 0; n < buttons.size(); n++) {
			if (buttons.get(n).processEvents(this, mouseCord, parent)) {
				break;
			}
		}

		// select next item
		if (!parent.getWorld().active) {
			if (parent.getListener().isKeyPressed(KeyEvent.VK_LEFT)) {
				// what index are we at?
				int index = calculateIndex();
				// How many items are there?
				int totalSize = calculateTotalSize();
				// deselect
				selectedItem = null;
				// loop
				while (selectedItem == null) {
					index--;
					int oldIndex = index+0;
					if (index < 0) {
						index = totalSize - 1;
					}
					// select next item
					if (selectItemAt(index)) {
						break;
					}
					index = oldIndex;
					if (index < 0) {
						index = totalSize - 1;
					}
				}
			} else if (parent.getListener().isKeyPressed(KeyEvent.VK_RIGHT)) {
				// what index are we at?
				int index = calculateIndex();
				// How many items are there?
				int totalSize = calculateTotalSize();
				// deselect
				selectedItem = null;
				// loop
				while (selectedItem == null) {
					index++;
					int oldIndex = index+0;
					if (index >= totalSize) {
						index -= totalSize;
					}
					// select next item
					if (selectItemAt(index)) {
						break;
					}
					index = oldIndex;
					if (index >= totalSize) {
						index -= totalSize;
					}
				}
			}
		}
	}

	private boolean selectItemAt(int index) {
		if (index < 0) {
			return true;
		} else {
			if (index < items.size()) {
				selectedItem = items.get(index);
			} else {
				index -= items.size();
				if (index < grounds.size()) {
					selectedItem = grounds.get(index);
				} else {
					index -= grounds.size();
					if (index < equipment.size()) {
						selectedItem = equipment.get(index);
					} else if (stores != null) {
						index -= equipment.size();
						if (index < stores.size()) {
							selectedItem = stores.get(index);
						}
					}
				}
			}
			return false;
		}
	}

	private int calculateTotalSize() {
		int totalSize = items.size() + grounds.size() + equipment.size();
		if (stores != null) {
			totalSize += stores.size();
		}
		return totalSize;
	}

	private int calculateIndex() {
		int index = -1;
		if (selectedItem != null) {
			index = items.indexOf(selectedItem);
			if (index == -1) {
				index = grounds.indexOf(selectedItem);
				if (index == -1) {
					index = equipment.indexOf(selectedItem);
					if (index == -1) {
						if (stores != null) {
							index = stores.indexOf(selectedItem) + items.size()
									+ grounds.size() + equipment.size();
						}
					} else {
						index += items.size() + grounds.size();
					}
				} else {
					index += items.size();
				}
			}
		}
		return index;
	}

	/**
	 * The button click call back of the Inventory.
	 */
	public void runEvent(String command, ValmarButton button) {
		if (command.equals("drop")) {
			if (selectedItem != null) {
				int index = items.indexOf(selectedItem);
				if (index != -1) {
					// remove the item
					removeItem(selectedItem);
					// add it to the world
					selectedItem.x = parent.getWorld().player.x;
					selectedItem.y = parent.getWorld().player.y;
					parent.getWorld().addItem(selectedItem);
					// add it to the grounds list
					grounds.add(selectedItem);
				}
			}
		} else if (command.equals("grab")) {
			if (selectedItem != null) {
				int index = grounds.indexOf(selectedItem);
				if (index != -1) {
					// pick up the item
					if (addItem(selectedItem)) {
						// remove the item
						parent.getWorld().removeItem(selectedItem);
						grounds.remove(index);
					}
				}
			}
		} else if (command.equals("equip")) {
			if (selectedItem != null) {
				int index = items.indexOf(selectedItem);
				if (index != -1) {
					if (selectedItem.item.equip >= 0
							&& selectedItem.item.equip < 10) {
						removeItem(selectedItem);
						if (equipment.get(selectedItem.item.equip) != null) {
							ItemInstance eq = equipment
									.get(selectedItem.item.equip);
							addItem(eq);
						}
						equipment.set(selectedItem.item.equip, selectedItem);
						// update equipment
						parent.getWorld().player.analyseEquipment();
					}
				}
			}
		} else if (command.equals("unequip")) {
			if (selectedItem != null) {
				int index = equipment.indexOf(selectedItem);
				if (index != -1) {
					equipment.set(selectedItem.item.equip, null);
					if (items.size() >= 20) {
						// add it to the world
						selectedItem.x = parent.getWorld().player.x;
						selectedItem.y = parent.getWorld().player.y;
						parent.getWorld().addItem(selectedItem);
						// add it to the grounds list
						grounds.add(selectedItem);
					} else {
						// pickup
						addItem(selectedItem);
					}
					// update equipment
					parent.getWorld().player.analyseEquipment();
				}
			}
		} else if (command.equals("sellAll")) {
			if (selectedItem != null&&stores!=null) {
				int index = items.indexOf(selectedItem);
				if (index != -1) {
					sellAllMethod();
				}
			}
		} else if (command.equals("buyAll")) {
			if (selectedItem != null&&stores!=null) {
				int index = stores.indexOf(selectedItem);
				if (index != -1) {
					if (parent.getWorld().player.gold >= selectedItem
							.getValue()) {
						buyAllMethod();
					}
				}
			}
		} else if (command.equals("buy")) {
			if (selectedItem != null&&stores!=null) {
				
				int index = stores.indexOf(selectedItem);
				if (index != -1) {
					if (parent.getWorld().player.gold >= selectedItem.item.cost) {
						if (selectedItem.getQuantity() == 1) {
							buyAllMethod();
						} else {
							selectedItem
									.setQuantity(selectedItem.getQuantity() - 1);
							ItemInstance clone = new ItemInstance(
									selectedItem.item);
							clone.setQuantity(1);
							if (addItem(clone)) {
								parent.getWorld().player.gold -= selectedItem.item.cost;
							} else {
								selectedItem.setQuantity(selectedItem
										.getQuantity() + 1);
							}
						}
					}
				}
			}
		} else if (command.equals("sell")) {
			if (selectedItem != null&&stores!=null) {
				int index = items.indexOf(selectedItem);
				if (index != -1) {
					if (selectedItem.getQuantity() == 1) {
						sellAllMethod();
					} else {
						selectedItem
								.setQuantity(selectedItem.getQuantity() - 1);
						ItemInstance clone = new ItemInstance(selectedItem.item);
						clone.setQuantity(1);
						if (addStoreItem(clone)) {
							parent.getWorld().player.gold += selectedItem.item.cost;
						} else {
							selectedItem
									.setQuantity(selectedItem.getQuantity() + 1);
						}
					}
				}
			}
		}
		if(parent.getWorld().player.skillPointsLeft>0){
			if(command.equals("str")){
				parent.getWorld().player.str.changeBaseValue(1);
				parent.getWorld().player.skillPointsLeft--;
				parent.getWorld().player.recalculateSecondaries();
			}else if(command.equals("wis")){
				parent.getWorld().player.wis.changeBaseValue(1);
				parent.getWorld().player.skillPointsLeft--;
				parent.getWorld().player.recalculateSecondaries();
			}else if(command.equals("dex")){
				parent.getWorld().player.dex.changeBaseValue(1);
				parent.getWorld().player.skillPointsLeft--;
				parent.getWorld().player.recalculateSecondaries();
			}else if(command.equals("end")){
				parent.getWorld().player.end.changeBaseValue(1);
				parent.getWorld().player.skillPointsLeft--;
				parent.getWorld().player.recalculateSecondaries();
			}
		}
	}

	private void buyAllMethod() {
		removeStoreItem(selectedItem);
		if (addItem(selectedItem)) {
			parent.getWorld().player.gold -= selectedItem.getValue();
		} else {
			addStoreItem(selectedItem);
		}
	}

	private void sellAllMethod() {
		removeItem(selectedItem);
		if (addStoreItem(selectedItem)) {
			parent.getWorld().player.gold += selectedItem.getValue();
		} else {
			addItem(selectedItem);
		}
	}

	/**
	 * Resets the Inventory.
	 */
	public void reset() {
		items = parent.getWorld().player.getItems();
		equipment = parent.getWorld().player.getEquipment();
		grounds = new Vector<ItemInstance>();
		Vector<Slot> reachables = parent.getWorld().reachableSlots(
				parent.getWorld().player.x, parent.getWorld().player.y);
		for (int n = 0; n < reachables.size(); n++) {
			grounds.addAll(reachables.get(n).getCarriableItems());
		}
		selectedItem = null;
		stores = null;
		storeOwner = null;
	}

	/**
	 * Sets the Inventory's store.
	 */
	public void setStore(Entity entity) {
		storeOwner = entity;
		stores = entity.getItems();
	}

	/**
	 * Expends the selected item.
	 */
	public void expendSelected() {
		if (selectedItem.getQuantity() == 1) {
			removeItem(selectedItem);
		} else {
			selectedItem.setQuantity(selectedItem.getQuantity() - 1);
		}
	}

	private boolean addStoreItem(ItemInstance item) {
		if (items.size() < 20) {
			ItemInstance ins = firstInstanceOfStoreItem(item.item);
			if (ins == null) {
				stores.add(item);
			} else {
				if (ins.getStacks()) {
					ins.setQuantity(ins.getQuantity() + item.getQuantity());
				} else {
					stores.add(item);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private boolean addItem(ItemInstance item) {
		if (items.size() < 20) {
			if (item.item == gold) {
				// item.
				parent.getWorld().player.gold += item.getValue();
			} else {
				ItemInstance ins = firstInstanceOfItem(item.item);
				if (ins == null) {
					items.add(item);
				} else {
					if (ins.getStacks()) {
						ins.setQuantity(ins.getQuantity() + item.getQuantity());
					} else {
						items.add(item);
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private void removeItem(ItemInstance item) {
		int index = items.indexOf(item);
		if (index != -1) {
			items.remove(item);
		}
	}

	private void removeStoreItem(ItemInstance item) {
		int index = stores.indexOf(item);
		if (index != -1) {
			stores.remove(item);
		}
	}

	private ItemInstance firstInstanceOfItem(Item item) {
		ItemInstance ins = null;
		for (int n = 0; n < items.size(); n++) {
			Item it = items.get(n).item;
			if (item == it) {
				ins = items.get(n);
				break;
			}
		}
		return ins;
	}

	private ItemInstance firstInstanceOfStoreItem(Item item) {
		ItemInstance ins = null;
		for (int n = 0; n < stores.size(); n++) {
			Item it = stores.get(n).item;
			if (item == it) {
				ins = stores.get(n);
				break;
			}
		}
		return ins;
	}
}
