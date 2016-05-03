package game.pathfinding;

import java.awt.Point;
import java.util.Vector;

//documented

/**
 * PathLocation is the class for a point in the path used by the PathFinder.
 * @author Adam
 */
public class PathLocation {
	/**
	 * The previous point in the path.
	 */
	public PathLocation parent;
	/**
	 * The location of the PathLocation.
	 */
	public Point point;
	/**
	 * The distance of the point from the destination.
	 */
	public float G;
	
	/**
	 * Makes a new PathLocation.
	 */
	public PathLocation(Point point){
		this.point=point;
	}
	/**
	 * Makes a new PathLocation with a specified parent.
	 */
	public PathLocation(Point point, PathLocation parent){
		this.point=point;
		this.parent=parent;
	}
	/**
	 * Returns the first point in the PathLocation's path.
	 */
	public PathLocation ancestor(){
		if(parent==null){
			return this;
		}else{
			return parent.ancestor();
		}
	}
	
	/**
	 * Returns all the points in the PathLocation's path.
	 */
	public Vector<PathLocation> ancestory() {
		Vector<PathLocation> ancestors = new Vector<PathLocation>();
		ancestors.add(this);
		if(this.parent!=null){
			ancestors.addAll(0, this.parent.ancestory());
		}
		return ancestors;
	}
}
