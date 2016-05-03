package game.pathfinding;

import java.awt.Point;
import java.util.Vector;


//documented

/**
 * PathFinder is an abstract A* path finder.
 * @author Adam
 */
public abstract class PathFinder {
	private Vector<PathLocation> points;
	private Point destination;
	/**
	 * The points that make up the shortest path form the start to the destination.
	 */
	public Vector<PathLocation> path;
	/**
	 * A boolean stating if a path was found.
	 */
	public boolean success;
	
	/**
	 * Makes a new PathFinder.
	 */
	public PathFinder()
	{
		//setup
		points=new Vector<PathLocation>();
		path=new Vector<PathLocation>();
	}
	
	/**
	 * Unsafely finds the shortest path between two points without a distance limit.
	 */
	public boolean pathFind(Point start, Point destination){
		return pathFind(start, destination, -1);
	}
	
	/**
	 * Finds the shortest path between two points within a distance limit.
	 */
	public boolean pathFind(Point start, Point destination, int maxLength){
		//reset
		points=new Vector<PathLocation>();
		path=new Vector<PathLocation>();
		
		this.destination=destination;
		if(canGoOn(destination)){
			findPointsAroundAndFinalize(new PathLocation(start));
			while(true){
				if(path.size()==0){
					//fail
					success=false;
					break;
				}else{
					PathLocation lowestF=findLowestF();
					if(lowestF==null){
						success=false;
						break;
					}else{
						if(maxLength!=-1&&calculateF(lowestF)>maxLength){
							success=false;
							break;
						}else if(lowestF.point.equals(destination)){
							success=true;
							path=lowestF.ancestory();
							break;
						}else{
							findPointsAroundAndFinalize(lowestF);
						}
					}
				}
			}
		}else{
			success=false;
		}
		return success;
	}
	
	private PathLocation findLowestF()
	{
		float minF=Float.MAX_VALUE;
		PathLocation lowest=null;
		for(int n=points.size()-1;n>=0;n--)
		{
			PathLocation location=points.get(n);
			float f=calculateF(location);
			if(f<minF){
				minF=f;
				lowest=location;
			}
		}
		return lowest;
	}
	
	private void findPointsAroundAndFinalize(PathLocation location)
	{
		points.remove(location);
		//reconstruct path
		//path=location.ancestory();
		path.add(location);
		
		boolean worthWhile=addLegalLocationsAround(location);	
		if(!worthWhile){
			path.remove(location);
		}
	}
	
	private float calculateF(PathLocation location){
		return location.G+calculateH(location.point, destination);
	}
	
	private boolean addLegalLocationsAround(PathLocation location){
		boolean worthWhile=false;
		Vector<Point> locations=addjacentLocations(location.point);
		for(int n=0;n<locations.size();n++){
			Point point=locations.get(n);
			if(canGoOn(point)){
				
				boolean ok=true;
				
				PathLocation loc=new PathLocation(point,location);
				loc.G=calculateG(loc);
				//Is it in path list?
				for(int m=0;m<path.size();m++){
					PathLocation inPath=path.get(m);
					if(inPath.point.equals(point)){
						if(inPath.G > loc.G){
							inPath.parent=location;
							inPath.G=loc.G;
							worthWhile=true;
						}
						ok=false;
						break;
					}
				}
				
				if(ok){
					//not in path
					for(int m=0;m<points.size();m++){
						PathLocation inPoints=points.get(m);
						if(inPoints.point.equals(point)){
							if(inPoints.G > loc.G){
								//the new path is better
								inPoints.parent=location;
								inPoints.G=loc.G;
								worthWhile=true;
							}
							ok=false;
							break;
						}
					}
				}
				
				if(ok){
					//not in path or points
					worthWhile=true;
					points.add(loc);
				}
			}
		}
		//return true;
		return worthWhile;
	}
	
	private float calculateG(PathLocation location){
		if(location.parent==null){
			return 0;
		}else{
			float G=stepSize(location.point, location.ancestor().point)+calculateG(location.parent);
			return G;
		}
	}
	
	/**
	 * Returns the adjacent points to a point.
	 */
	protected abstract Vector<Point> addjacentLocations(Point point);
	
	/**
	 * Returns a boolean stating if a point is valid choice to be used in a path.
	 */
	protected abstract boolean canGoOn(Point point);
	
	/**
	 * Calculates the estimated distance between two points.
	 */
	protected abstract float calculateH(Point start, Point end);
	
	/**
	 * Returns the distance between two adjacent points.
	 */
	protected abstract float stepSize(Point start, Point end);
}
