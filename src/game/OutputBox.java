package game;

import java.util.Vector;

//documented

/**
 * OutputBox is a simple text output box widget.
 * @author Adam
 */
public class OutputBox {
	
	private Vector<String> lines;
	private int lineIndex;
	private int showLines=3;
	/**
	 * The x coordinate of the OutputBox.
	 */
	public int x=0;
	/**
	 * The x coordinate of the OutputBox.
	 */
	public int y=0;
	/**
	 * The width coordinate of the OutputBox.
	 */
	public int width=100;
	/**
	 * The height coordinate of the OutputBox.
	 */
	public int height=100;
	
	/**
	 * Makes a new OutputBox.
	 */
	public OutputBox(){
		lines=new Vector<String>();
	}
	
	/**
	 * Adds a line of text to the OutputBox.
	 */
	public void addLine(String line){
		lines.add(line);
		if(lines.size()>100){
			lines.remove(0);
		}
		lineIndex=Math.max(0, lines.size()-showLines);
	}
	
	/**
	 * Returns the concatenation of all the lines of text in the OutputBox.
	 */
	public String getText(){
		String text="";
		for(int n=lineIndex;n<lines.size()&&n<lineIndex+showLines;n++){
			if(n>lineIndex){
				text+="\n";
			}
			text+=lines.get(n);
		}
		return text;
	}

	/**
	 * Scrolls the OutputBox up a number of lines.
	 */
	public void scrollUp(int amount) {
		lineIndex=Math.max(0, lineIndex-amount);
	}
	
	/**
	 * Scrolls the OutputBox down a number of lines.
	 */
	public void scrollDown(int amount) {
		lineIndex=Math.max(Math.min(lineIndex+amount, lines.size()-showLines), 0);
	}

	/**
	 * Returns a value from 0 (scrolled all the way up) to 1 specifying how far down the OutputBox is scrolled.
	 */
	public float scrollPercent() {
		float perc=((float)lineIndex)/((float)lines.size()-showLines);
		if(Float.isNaN(perc)){
			return 0;
		}else{
			return perc;
		}
	}

	/**
	 * Returns the percentage scrolled by the OutputBox each line.
	 */
	public float stepPercent() {
		return 1f/((float)lines.size());
	}
}
