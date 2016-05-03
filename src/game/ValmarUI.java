package game;

import game.rpg.Entity;
import game.rpg.StatusInstance;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.AccessControlException;
import java.util.Vector;

//documented

/**
 * ValmarUI is the ui around the world.
 * @author Adam
 */
public class ValmarUI extends Renderable implements ValmarButtonListener {
	private ValmarRender parent;
	private Vector<ValmarButton> buttons;
	private ValmarButton peaceFight;
	private ValmarButton bag;
	private ValmarButton save;
	private ValmarButton open;
	private ValmarButton up;
	private ValmarButton down;
	private boolean dragging=false;
	/**
	 * The ValmarUi's output box.
	 */
	public OutputBox output;
	//private ValmarButton tree;
	//private ValmarButton powers;

	/**
	 * Makes a new ValmarUI.
	 */
	public ValmarUI(ValmarRender game) {
		parent = game;
		//buttons
		buttons = new Vector<ValmarButton>();
		peaceFight = new ValmarButton(new Rectangle(120, 368, 20, 20), "images/icons/fight.png", "fight");
		peaceFight.altText="Fight";
		peaceFight.hotKeyName="f";
		peaceFight.hotkey=KeyEvent.VK_F;
		buttons.add(peaceFight);
		
		bag = new ValmarButton(new Rectangle(150, 368, 20, 20), "images/icons/bag.png", "bag");
		bag.hotKeyName="i";
		bag.altText="Inventory";
		bag.hotkey=KeyEvent.VK_I;
		buttons.add(bag);
		
		/*tree = new ValmarButton(new Rectangle(180, 368, 20, 20), "images/icons/tree.png", "tree");
		tree.hotKeyName="k";
		tree.altText="Skill Tree";
		tree.hotkey=KeyEvent.VK_K;
		buttons.add(tree);*/
		
		/*powers = new ValmarButton(new Rectangle(210, 368, 20, 20), "images/icons/powers.png", "powers");
		powers.hotKeyName="p";
		powers.altText="Powers";
		powers.hotkey=KeyEvent.VK_P;
		buttons.add(powers);*/
		
		save = new ValmarButton(new Rectangle(180, 368, 20, 20), "images/icons/save.png", "save");
		save.altText="Save";
		save.hotkey=KeyEvent.VK_Z;
		save.hotKeyName="z";
		buttons.add(save);
		
		open = new ValmarButton(new Rectangle(210, 368, 20, 20), "images/icons/open.png", "open");
		open.altText="Open";
		open.hotkey=KeyEvent.VK_O;
		open.hotKeyName="o";
		buttons.add(open);
		
		//output box
		output=new OutputBox();
		output.addLine("Valmar");
		output.x=10;
		output.y=390;
		output.width=470;
		output.height=55;
		up = new ValmarButton(new Rectangle(output.x+output.width, output.y, 8, 8), "images/ui/up.png", "scrollUp");
		up.drawBack=false;
		buttons.add(up);
		down = new ValmarButton(new Rectangle(output.x+output.width, output.y+output.height-8, 8, 8), "images/ui/down.png", "scrollDown");
		down.drawBack=false;
		buttons.add(down);
	}

	/**
	 * Paints the ValmarUI.
	 */
	public void paint(Graphics g) {
		// draw the border
		Image image = parent.getCache().getImage("images/ui/border.png");
		g.drawImage(image, 0, 0, parent.getObserver());
		// read input
		Point mouseCord = parent.getListener().getMouseCord();

		// entity infos
		if (parent.getWorld().player != null) {
			renderEntityInfo(g, parent.getWorld().player, 5, 30);
		}
		if (parent.getWorld().selected != null) {
			renderEntityInfo(g, parent.getWorld().selected, 5, 30 + 105);
		}

		// output
		parent.drawMultilineText(g,  output.getText(), output.x, output.y-3);
		
		g.drawRect(output.x-5, output.y, output.width, output.height);
		g.drawLine(output.x+output.width+4, output.y+4, output.x+output.width+4, output.y+output.height-4);
		g.setColor(new Color(191, 191, 191));
		int scrollHigh=output.height-3*8;
		g.fillRect(output.x+output.width, (int)(output.y+8+scrollHigh*output.scrollPercent()), 8, 8);
		g.setColor(Color.BLACK);
		g.drawRect(output.x+output.width, (int)(output.y+8+scrollHigh*output.scrollPercent()), 8, 8);
		
		

		// draws the buttons
		for (int n = 0; n < buttons.size(); n++) {
			buttons.get(n).render(g, parent, mouseCord);
		}
	}

	
	private void renderEntityInfo(Graphics g, Entity entity, int x, int y) {
		// under
		// draw health
		float percent = entity.hp.getPercent();
		g.setColor(Color.RED);
		g.fillRect(x + 2, y + 66, (int) percent, 11);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((int) (x + 2 + percent), y + 66, (int) (100 - percent), 11);
		g.setColor(Color.BLACK);
		parent.drawMultilineText(g, "HP: " + entity.hp.toString(), x + 2 + 50, y + 66 - 7, true);

		// draw the energy
		percent = entity.ep.getPercent();
		g.setColor(Color.BLUE);
		g.fillRect(x + 2, y + 82, (int) percent, 11);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((int) (x + 2 + percent), y + 82, (int) (100 - percent), 11);
		g.setColor(Color.BLACK);
		parent.drawMultilineText(g, "EP: " + entity.ep.toString(), x + 2 + 50, y + 82 - 7, true);

		Image image;
		if(entity==parent.getWorld().player){
			//draw xp
			percent = entity.xp.getPercent();
			g.setColor(Color.YELLOW);
			g.fillRect(x + 2, y + 50, (int) percent, 11);
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect((int) (x + 2 + percent), y + 50, (int) (100 - percent), 11);
			g.setColor(Color.BLACK);
			parent.drawMultilineText(g, "XP: " + entity.xp.toString(), x + 2 + 50, y + 50 - 7, true);
			image  = parent.getCache().getImage("images/ui/playerinfo.png");
		}else{
			image = parent.getCache().getImage("images/ui/entityinfo.png");
		}
		// layer
		// draw the backing
		g.drawImage(image, x, y, parent.getObserver());

		// over
		// draw the portrait
		image = parent.getCache().getImage(entity.prtsrc);
		g.drawImage(image, x + 5, y + 4, parent.getObserver());

		// draw the name
		g.setColor(Color.BLACK);
		parent.drawMultilineText(g, entity.name, x + 50, y - 3);

		// draw the action points
		if (parent.getWorld().fighting) {
			g.setColor(Color.BLACK);
			parent.drawMultilineText(g, "AP: " + entity.actionPoints.toString(), x + 50, y + 7);
		}

		// draw the statuses
		for (int n = 0; n < entity.statuses.size(); n++) {
			StatusInstance status = entity.statuses.get(n);
			image = parent.getCache().getImage(status.status.src);
			g.drawImage(image, x + 50 + n * 8, y + 28, parent.getObserver());
		}
		parent.drawMultilineText(g, "Level: "+entity.level, x+50, y+29);
	}

	/**
	 * Makes the ValmarUI respond to input.
	 */
	public void tick() {
		Point mouseCord = parent.getListener().getMouseCord();
		// Is a button being clicked?
		for (int n = 0; n < buttons.size(); n++) {
			if(buttons.get(n).processEvents(this, mouseCord, parent)){
				break;
			}
		}
		//		
		int scrollHigh=output.height-3*8;
		Rectangle rect=new Rectangle(output.x+output.width, (int)(output.y+8+scrollHigh*output.scrollPercent()), 8, 8);
		int stepSize=(int)(output.stepPercent()*scrollHigh);
		if(!dragging){
			if (rect.contains(mouseCord)) {
				if (parent.getListener().isMousePressing()) {
					dragging=true;
				}
			}
		}else{
			if(parent.getListener().isMouseDown()){
				if(mouseCord.y<rect.y-stepSize){
					output.scrollUp(1);
				}else if(mouseCord.y>rect.y+rect.width+stepSize){
					output.scrollDown(1);
				}
			}else{
				dragging=false;
			}
		}
		//other
		if (parent.getWorld().fighting && peaceFight.command.equals("fight")) {
			peaceFight.icon = "images/icons/peace.png";
			peaceFight.command = "peace";
			peaceFight.altText="Peace";
		} else if (!parent.getWorld().fighting && peaceFight.command.equals("peace")) {
			peaceFight.command = "fight";
			peaceFight.icon = "images/icons/fight.png";
			peaceFight.altText="Fight";
		}
	}

	/**
	 * The button click call back of the ValmarUI.
	 */
	public void runEvent(String command, ValmarButton caller) {
		if (command.equals("fight")) {
			if (parent.getWorld().active) {
				// start the fight
				parent.getWorld().startFight();
				// image
				caller.icon = "images/icons/peace.png";
				caller.command = "peace";
				caller.altText="Peace";
			}
		} else if (command.equals("peace")) {
			if (parent.getWorld().active) {
				// start the fight
				if (parent.getWorld().endFight()) {
					// image
					caller.command = "fight";
					caller.icon = "images/icons/fight.png";
					caller.altText="Fight";
				}
			}
		} else if (command.equals("bag")) {
			if (parent.getWorld().player != null) {
				if (parent.getWorld().active) {
					parent.openInventory(null);
				} else {
					parent.backToGame();
				}
			}
		} else if (command.equals("save")) {
			if (!parent.getWorld().fighting) {
				// encoding
				String fil = parent.getWorld().save();
				// output
				System.out.println(fil);
				// saving
				// make the file dialog
				FileDialog fileDialog = new FileDialog(parent.findParentFrame(), "Save Valmar", FileDialog.SAVE);
				try {
					fileDialog.setDirectory(System.getProperty("user.dir"));
				} catch (AccessControlException e) {
					e.printStackTrace();
				}
				fileDialog.setVisible(true);
				// what file was selected
				String fileName = fileDialog.getFile();
				if (fileName == null) {
					System.out.println("Did not save file.");
				} else {
					FileOutputStream o;
					try {
						o = new FileOutputStream(new File(fileDialog.getDirectory(), fileDialog.getFile()));
						BufferedOutputStream out = new BufferedOutputStream(o);
						try {
							out.write(fil.getBytes());
							out.close();
							System.out.println("Saved to file: " + fileName);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			} else {
				// can't save because of fight
			}
		} else if (command.equals("open")) {
			FileDialog fileDialog = new FileDialog(parent.findParentFrame(), "Load Valmar", FileDialog.LOAD);
			try {
				fileDialog.setDirectory(System.getProperty("user.dir"));
			} catch (AccessControlException e) {
				e.printStackTrace();
			}
			fileDialog.setVisible(true);
			// what file was selected
			String fileName = fileDialog.getFile();
			if (fileName == null) {
				System.out.println("Did not load file.");
			} else {
				File fi = new File(fileDialog.getDirectory(), fileDialog.getFile());
				try {
					String zone = TextLoader.loadText(fi.toURI().toURL());
					parent.loadMap(zone);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

			}
		}else if(command.equals("scrollUp")){
			output.scrollUp(1);
		}else if(command.equals("scrollDown")){
			output.scrollDown(1);
		}else if(command.equals("tree")){
			if (parent.getWorld().player != null) {
				if (parent.getWorld().active) {
					//parent.openTree();
				} else {
					parent.backToGame();
				}
			}
		}
	}

}
