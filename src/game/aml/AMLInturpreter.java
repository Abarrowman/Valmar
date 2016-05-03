package game.aml;

import java.util.Vector;

//documented

/**
 * AMLInterpreter is an AML utility class.
 * @author Adam
 */
public class AMLInturpreter {
	
	/**
	 * Returns all the parsed AMLBlocks form a string of AML code.
	 */
	public static Vector<AMLBlock> getBlocks(String code)
	{
		Vector<AMLBlock> blocks=new Vector<AMLBlock>();
		int index=code.indexOf("}");
		while(index!=-1){
			String bock=code.substring(0, index);
			//
			blocks.add(new AMLBlock(bock));
			//
			code=code.substring(index+1);
			index=code.indexOf("}");
		}
		return blocks;
	}
	
	/**
	 * Parses an AMLLine.
	 */
	public static AMLLine getLine(String code){
		return new AMLLine(code);
	}
	
}
