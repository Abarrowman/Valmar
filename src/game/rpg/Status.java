package game.rpg;

import game.aml.AMLBlock;
import game.aml.AMLInturpreter;
import game.aml.AMLLine;

//documented

/**
 * Status is a class that represents a status that is affecting an Entity.
 * @author Adam
 */
public class Status {
	/**
	 * The Status' name.
	 */
	public String name="";
	/**
	 * The Status' image.
	 */
	public String src="";
	/**
	 * The Status' start call back.
	 */
	public AMLLine onStart;
	/**
	 * The Status' turn passing call back.
	 */
	public AMLLine onTurn;
	/**
	 * The Status' end call back.
	 */
	public AMLLine onEnd;
	
	/**
	 * Makes a Status with a given name and image.
	 */
	public Status(String name, String src) {
		this.name = name;
		this.src = src;
	}
	
	/**
	 * Parses a Status out of a given AMLBlock.
	 */
	public static Status parse(AMLBlock block) {
		Status status = new Status(block.getParameterString("name"), block.getParameterString("src"));
		//event handlers
		String event=block.getParameterString("onTurn", null);
		if(event!=null){
			status.onTurn=AMLInturpreter.getLine(event);
		}
		event=block.getParameterString("onStart", null);
		if(event!=null){
			System.out.println("["+event+"]");
			status.onStart=AMLInturpreter.getLine(event);
		}
		event=block.getParameterString("onEnd", null);
		if(event!=null){
			status.onEnd=AMLInturpreter.getLine(event);
		}
		return status;
	}
}
