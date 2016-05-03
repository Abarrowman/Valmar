package game;

//documented

/**
 * RenderableParent is an interface for an object with a Renderable tree structure.
 * @author Adam
 */
public interface RenderableParent {
	/**
	 * Removes a child from the RenderableParent.
	 */
	public abstract Renderable removeChild(Renderable child);
	/**
	 * Adds a child to the RenderableParnet.
	 */
	public abstract Renderable addChild(Renderable child);
}
