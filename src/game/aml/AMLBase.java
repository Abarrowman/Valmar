package game.aml;

import java.util.Vector;

//documented

/**
 * Serves a base class for all Adam's Markup Language objects.
 * @author Adam
 */
public abstract class AMLBase {
	/**
	 * The AML object's command.
	 */
	public String command;
	/**
	 * The AML object's parameter's names.
	 */
	public Vector<String> parameterNames;
	/**
	 * The AML object's parameters's values as strings.
	 */
	public Vector<String> parameterValues;
	
	/**
	 * Makes a new AMLBase.
	 */
	public AMLBase()
	{
		parameterNames=new Vector<String>();
		parameterValues=new Vector<String>();
		command="";
	}
	/**
	 * Makes a new AMLBase from the given AML code.
	 */
	public AMLBase(String code)
	{
		decode(code);
	}
	/**
	 * Makes a new AMLBase given it's command, parameter names and values.
	 */
	public AMLBase(String command, Vector<String> names, Vector<String> values)
	{
		parameterNames=names;
		parameterValues=values;
		this.command=command;
	}
	
	/**
	 * Adds a parameter to an AML object.
	 */
	public void addParameter(String name, String value){
		//must not already have a parameter with that name
		if(parameterNames.indexOf(name)==-1){
			parameterNames.add(name);
			parameterValues.add(value);
		}
	}
	/**
	 * Unsafely gets the value of a specified parameter in an AML object as a string. 
	 */
	public String getParameterString(String name)
	{
		int index=parameterNames.indexOf(name);
		if(index!=-1)
		{
			return parameterValues.get(index);
		}else{
			throw new NullPointerException("Mandatory parameter not set.");
		}
	}
	/**
	 * Safely gets the value of a specified parameter in an AML object as a string or returns a default value if unspecified. 
	 */
	public String getParameterString(String name, String defaultValue)
	{
		int index=parameterNames.indexOf(name);
		if(index!=-1)
		{
			return parameterValues.get(index);
		}else{
			return defaultValue;
		}
	}
	
	/**
	 * Unsafely gets the value of a specified parameter in an AML object as an int. 
	 */
	public int getParameterInt(String name)
	{
		int index=parameterNames.indexOf(name);
		if(index!=-1)
		{
			return Integer.parseInt(parameterValues.get(index));
		}else{
			throw new NullPointerException("Mandatory parameter not set.");
		}
	}
	/**
	 * Safely gets the value of a specified parameter in an AML object as an int or returns a default value if unspecified. 
	 */
	public int getParameterInt(String name, int defaultValue)
	{
		int index=parameterNames.indexOf(name);
		if(index!=-1)
		{
			return Integer.parseInt(parameterValues.get(index));
		}else{
			return defaultValue;
		}
	}
	
	/**
	 * Unsafely gets the value of a specified parameter in an AML object as a boolean. 
	 */
	public boolean getParameterBoolean(String name)
	{
		int index=parameterNames.indexOf(name);
		if(index!=-1)
		{
			String value=parameterValues.get(index);
			if(value.equals("true")){
				return true;
			}else if(value.equals("false")){
				return false;
			}else{
				throw new NullPointerException("Mandatory parameter not set.");
			}
		}else{
			throw new NullPointerException("Mandatory parameter not set.");
		}
	}
	/**
	 * Safely gets the value of a specified parameter in an AML object as a boolean or returns a default value if unspecified. 
	 */
	public boolean getParameterBoolean(String name, boolean defaultValue)
	{
		int index=parameterNames.indexOf(name);
		if(index!=-1)
		{
			String value=parameterValues.get(index);
			if(value.equals("true")){
				return true;
			}else if(value.equals("false")){
				return false;
			}else{
				return defaultValue;
			}
		}else{
			return defaultValue;
		}
	}
	
	/**
	 * Decodes the code for an AML object.
	 */
	public abstract void decode(String code);
	
	
	/**
	 * Encodes the AML object.
	 */
	public abstract String encode();
}
