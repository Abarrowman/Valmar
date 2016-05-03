package game.pathfinding;

import game.rpg.World;
import java.awt.Point;
import java.util.Vector;

//documented

/**
 * ValmarPathFinder is a PathFinder designed for Valmar.
 * @author Adam
 */
public class ValmarPathFinder extends PathFinder {

	private World world;
	
	/**
	 * Makes a new ValmarPathFinder.
	 */
	public ValmarPathFinder(World world) {
		super();
		this.world=world;
	}

	protected Vector<Point> addjacentLocations(Point point) {
		Vector<Point> locations=new Vector<Point>();
		
		//locations.add(new Point(point.x+1, point.y+1));
		//locations.add(new Point(point.x-1, point.y-1));
		//locations.add(new Point(point.x-1, point.y+1));
		//locations.add(new Point(point.x+1, point.y-1));
		
		locations.add(new Point(point.x+1, point.y));
		locations.add(new Point(point.x-1, point.y));
		locations.add(new Point(point.x, point.y+1));
		locations.add(new Point(point.x, point.y-1));
		return locations;
	}

	protected float calculateH(Point start, Point end) {
		float dx=(float)(start.x-end.x);
		float dy=(float)(start.y-end.y);
		//return dx+dy;
		return (float)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}

	protected boolean canGoOn(Point point) {
		return !world.getBlocking(point.x, point.y);
	}

	protected float stepSize(Point start, Point end) {
		/*int dx=(int)Math.abs(start.x-end.x);
		int dy=(int)Math.abs(start.y-end.y);
		if(dx==1&&dy==0){
			return 1;
		}else if(dy==1&&dx==0){
			return 1;
		}else{
			return (float)1.4;
		}*/
		return 1;
	}

}
