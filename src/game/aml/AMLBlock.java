package game.aml;

import java.util.Vector;
//import java.util.regex.*;

//documented

/**
 * AMLBlock is an AML object of the form:
 * Command{
 * name1=value1;
 * name2="value2";
 * } 
 * @author Adam
 */
public class AMLBlock extends AMLBase {
	
	/**
	 * Makes a new AMLBlock.
	 */
	public AMLBlock()
	{
		super();
	}
	/**
	 * Makes a new AMLBlock from the given AML code.
	 */
	public AMLBlock(String code)
	{
		super(code);
	}
	/**
	 * Makes a new AMLBlock given it's command, parameter names and values.
	 */
	public AMLBlock(String command, Vector<String> names, Vector<String> values)
	{
		super(command, names, values);
	}
	
	/**
	 * Decodes an AMLBlock.
	 */
	public void decode(String code)
	{
		String block=code+"";
		parameterNames=new Vector<String>();
		parameterValues=new Vector<String>();
		int start=block.indexOf("{");
		if(start!=-1)
		{
			command=block.substring(0,start);
			int lastNewLine=command.lastIndexOf("\n");
			if(lastNewLine!=-1){
				command=command.substring(lastNewLine+1);
			}
			lastNewLine=command.lastIndexOf("\r");
			if(lastNewLine!=-1){
				command=command.substring(lastNewLine+1);
			}
			if(!command.equals("")){
				String content = block.substring(start+1);
				//take out newlines
				content=content.replaceAll("\n", "");
				content=content.replaceAll("\r", "");
				int equalIndex=content.indexOf("=");
				while(equalIndex!=-1){
					String parameterName=content.substring(0, equalIndex);
					content=content.substring(equalIndex+1);
					equalIndex=content.indexOf("=");
					int semiColonIndex;
					if(content.charAt(0)=='"')
					{
						//remove the open quote
						content=content.substring(1);
						/*
						Pattern pat=Pattern.compile("[^\\]\"");
						*/
						
						int quoteIndex=content.indexOf('"');
						content=content.substring(0, quoteIndex)+content.substring(quoteIndex+1);
						
					}
					semiColonIndex=content.indexOf(';');
					String parameterValue=content.substring(0, semiColonIndex);
					parameterNames.add(parameterName);
					parameterValues.add(parameterValue);
					//repeat
					content=content.substring(semiColonIndex+1);
					equalIndex=content.indexOf("=");
				}
			}else{
				System.out.println("Malformed Block:\n"+block);
			}
		}else{
			System.out.println("Malformed Block:\n"+block);
		}
	}
	
	/**
	 * Encodes an AMLBlock.
	 */
	public String encode()
	{
		String str=command+"{\n";
		System.out.println();
		for(int n=0;n<parameterNames.size();n++)
		{
			str+=parameterNames.get(n)+"="+parameterValues.get(n)+";\n";
		}
		str+="}\n";
		return str;
	}
}
