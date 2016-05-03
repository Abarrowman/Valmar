package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Vector;

//documented

/**
 * SkillTree is an used class for representing a players skills.
 * Please note this class is not being used in Valmar v 1.0.
 * @author Adam
 */
public class SkillTree extends Renderable implements ValmarButtonListener {
	
	private ValmarRender parent;
	private Vector<ValmarButton> buttons;
	private ValmarButton str;
	private ValmarButton wis;
	private ValmarButton dex;
	private ValmarButton end;
	//private ValmarButton magic;

	public SkillTree(ValmarRender game) {
		parent = game;
		buttons = new Vector<ValmarButton>();
		
		str = new ValmarButton(new Rectangle(5,30,20,20), "images/icons/strength.png", "str");
		str.altText="Strength";
		str.hotKeyName="s";
		str.hotkey=KeyEvent.VK_S;
		buttons.add(str);
		
		wis = new ValmarButton(new Rectangle(5,50,20,20), "images/icons/intellegence.png", "wis");
		wis.altText="Wisdom";
		wis.hotKeyName="w";
		wis.hotkey=KeyEvent.VK_W;
		buttons.add(wis);
		
		dex = new ValmarButton(new Rectangle(5,70,20,20), "images/icons/hand.png", "dex");
		dex.altText="Dexterity";
		dex.hotKeyName="d";
		dex.hotkey=KeyEvent.VK_D;
		buttons.add(dex);
		
		end = new ValmarButton(new Rectangle(5,90,20,20), "images/icons/endurance.png", "end");
		end.altText="Endurance";
		end.hotKeyName="e";
		end.hotkey=KeyEvent.VK_E;
		buttons.add(end);
		
		/*magic = new ValmarButton(new Rectangle(95,30,20,20), "images/icons/magic.png", "magic");
		magic.altText="Magic";
		magic.hotKeyName="m";
		magic.hotkey=KeyEvent.VK_M;
		buttons.add(magic);*/
	}

	public void paint(Graphics g) {
		// backing
		Image image = parent.getCache().getImage("images/ui/graybacking.png");
		g.drawImage(image, 0, 0, parent.getObserver());

		// read input
		Point mouseCord = globalToLocal(parent.getListener().getMouseCord());

		//stats
		
		//text
		parent.drawMultilineText(g, "Stats", 5, 5);
		parent.drawMultilineText(g, parent.getWorld().player.str.toModified(), 25, 30);
		parent.drawMultilineText(g, parent.getWorld().player.wis.toModified(), 25, 50);
		parent.drawMultilineText(g, parent.getWorld().player.dex.toModified(), 25, 70);
		parent.drawMultilineText(g, parent.getWorld().player.end.toModified(), 25, 90);
		
		//parent.drawMultilineText(g, "Skills", 95, 5);
		//parent.drawMultilineText(g, parent.getWorld().player.magic.toModified(), 115, 30);
		
		parent.drawMultilineText(g, "Points Left: "+parent.getWorld().player.skillPointsLeft, 5, 320);

		// draws the buttons
		for (int n = 0; n < buttons.size(); n++) {
			buttons.get(n).render(g, parent, mouseCord);
		}
	}

	public void tick() {
		Point mouseCord = globalToLocal(parent.getListener().getMouseCord());
		
		// buttons
		for (int n = 0; n < buttons.size(); n++) {
			if (buttons.get(n).processEvents(this, mouseCord, parent)) {
				break;
			}
		}
	}
	
	public void runEvent(String command, ValmarButton button) {
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
			}/*else if(command.equals("magic")){
				parent.getWorld().player.magic.changeBaseValue(1);
				parent.getWorld().player.skillPointsLeft--;
				parent.getWorld().player.recalculateSecondaries();
			}*/
		}
	}
}
