package game.aml;

import java.util.Vector;

//documented

/**
 * AMLLine is an AML object of the form:
 * command:name1-value1,name2-value2,
 * or
 * command:
 * @author Adam
 */
public class AMLLine extends AMLBase {
	/**
	 * Makes a new AMLLine.
	 */
	public AMLLine()
	{
		super();
	}
	/**
	 * Makes a new AMLLine from the given AML code.
	 */
	public AMLLine(String code)
	{
		super(code);
	}
	/**
	 * Makes a new AMLLine given it's command, parameter names and values.
	 */
	public AMLLine(String command, Vector<String> names, Vector<String> values)
	{
		super(command, names, values);
	}
	
	/**
	 * Decodes an AMLLine.
	 */
	public void decode(String code)
	{
		parameterNames=new Vector<String>();
		parameterValues=new Vector<String>();
		
		String line=code+"";
		int colonIndex=line.indexOf(":");
		
		if(colonIndex!=-1){
			command=line.substring(0, colonIndex);
			if(!command.equals("")){
				String params=line.substring(colonIndex+1);
				int commaIndex=params.indexOf(",");
				while(commaIndex>0){
					String param=params.substring(0, commaIndex);
					int dashIndex=param.indexOf("-");
					String paramName=param.substring(0, dashIndex);
					String paramValue=param.substring(dashIndex+1);
					parameterNames.add(paramName);
					parameterValues.add(paramValue);
					//next
					params=params.substring(commaIndex+1);
					commaIndex=params.indexOf(",");
				}
			}else{
				System.out.println("Malformed Line:\n"+line);
			}
		}else{
			System.out.println("Malformed Line:\n"+line);
		}
	}
	
	/**
	 * Encodes an AMLLine.
	 */
	public String encode()
	{
		String str=command+":";
		System.out.println();
		for(int n=0;n<parameterNames.size();n++)
		{
			str+=parameterNames.get(n)+"-"+parameterValues.get(n)+",";
		}
		return str;
	}
	
	/**
	 * Clones and AMLLine.
	 */
	public AMLLine clone(){
		return new AMLLine(command, parameterNames, parameterValues);
	}
}
