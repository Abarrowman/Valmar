package game.rpg;

//documented

/**
 * StatusInstance is the class used to represent an instance of a status.
 * @author Adam
 */
public class StatusInstance {
	/**
	 * The Status of the StatusInstance.
	 */
	public Status status;
	/**
	 * The number of remaining turns of the StatusInstance.
	 */
	public int turnsLeft;
	/**
	 * Makes a new StatusInstance with a given status and number of turns left.
	 */
	public StatusInstance(Status status, int turns){
		this.status=status;
		turnsLeft=turns;
	}
	
	/**
	 * Returns an identical copy of the StatusInstance.
	 */
	public StatusInstance clone(){
		return new StatusInstance(status, turnsLeft);
	}
}
