package game.rpg;

//documented

/**
 * Stat is the class for an Entity's class.
 * @author Adam
 */
public class Stat {
	private int baseValue=0;
	private int value=0;
	private int maxValue=1;
	private int modifier=0;
	
	/**
	 * Creates a new Stat.
	 */
	public Stat()
	{
	}
	/**
	 * Creates a new Stat with a given value.
	 */
	public Stat(int baseVal)
	{
		value=baseValue=setBaseValue(baseVal);
	}
	/**
	 * Creates a new Stat with a given current and maximum value.
	 */
	public Stat(int val, int baseVal)
	{
		setBaseValue(baseVal);
		setValue(val);
	}
	/**
	 * Creates a new Stat with a given current value, base maximum value, and a maximum value modifier.
	 */
	public Stat(int val, int baseVal, int mod)
	{
		setBaseValue(baseVal);
		setModifier(mod);
		setValue(val);
	}


	//setters
	/**
	 * Sets the current value of the Stat.
	 */
	public int setValue(int val){
		if(val<0){
			value=0;
		}else if(val>maxValue){
			value=maxValue;
		}else{
			value=val;
		}
		return value;
	}

	/**
	 * Sets the unmodified maximum value of the Stat.
	 */
	public int setBaseValue(int baseVal){
		if(baseVal<0){
			baseValue=0;
		}else{
			baseValue=baseVal;
		}
		recalculateMaxValue();
		return baseValue;
	}

	/**
	 * Sets the maximum value modifier of the Stat.
	 */
	public int setModifier(int mod){
		modifier=mod;
		recalculateMaxValue();
		return modifier;
	}

	//getters
	/**
	 * Returns the current value of the Stat.
	 */
	public int getValue(){
		return value;
	}
	/**
	 * Returns the maximum value of the Stat.
	 */
	public int getMaxValue(){
		return maxValue;
	}
	/**
	 * Returns the unmodified maximum value of the Stat.
	 */
	public int getBaseValue(){
		return baseValue;
	}

	/**
	 * Returns the maxiumum value modifier of the Stat.
	 */
	public int getModifier(){
		return modifier;
	}
	/**
	 * Gets the value of the Stat as a double.
	 */
	public double getValued() {
		return (double)getValue();
	}
	/**
	 * Gets the value of the Stat as a double times a given double.
	 */
	public double getValued(double d) {
		return d*getValued();
	}

	//methods
	/**
	 * Recalculates the maximum value of the Stat.
	 */
	public int recalculateMaxValue(){
		int max=Math.max(modifier+baseValue, 0);
		int old=maxValue;
		maxValue=max;
		changeValue(max-old);
		return maxValue;
	}

	/**
	 * Adds a given amount to the Stat's maximum value modifier.
	 */
	public int changeModifier(int amount){
		setModifier(modifier+amount);
		return modifier;
	}
	
	/**
	 * Adds a given amount to the Stat's value.
	 */
	public int changeValue(int amount){
		setValue(value+amount);
		return value;
	}
	
	/**
	 * Adds an amount to the Stat's unmodified maximum value.
	 */
	public int changeBaseValue(int amount){
		baseValue+=amount;
		recalculateMaxValue();
		setValue(value+amount);
		return value;
	}
	
	/**
	 * Sets the Stat's value to its modified maximum value.
	 */
	public int restore(){
		value=maxValue;
		return value;
	}
	
	/**
	 * Sets the Stat's value to 0.
	 */
	public int deplete(){
		value=0;
		return 0;
	}
	
	/**
	 * Gets the percent of the Stat's maximum value its value occupies, from 0 to 100.
	 */
	public float getPercent(){
		return 100*((float)value)/((float)maxValue);
	}
	
	/**
	 * Returns a string of the Stat's value over its maximum value.
	 */
	public String toString(){
		return value+"/"+maxValue;
	}
	
	/**
	 * Returns a string showing a Stat's unmodified maximum value and it's modifier.
	 */
	public String toModified(){
		return baseValue+"("+modifier+")";
	}
	
	/**
	 * Returns an identical copy of the Stat.
	 */
	public Stat clone()
	{
		return new Stat(value, baseValue, modifier);
	}
}
