package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Vector;

//documented

/**
 * Renderables is a simple 2D rendering tree structured class.
 * @author Adam
 */
public class Renderable implements RenderableParent {
	/**
	 * The x coordinate of the Renderable.
	 */
	public float x=0;
	/**
	 * The y coordinate of the Renderable.
	 */
	public float y=0;
	/**
	 * The rotation of the Renderable in degrees.
	 */
	public float rotation=0;
	/**
	 * The horizontal stretch coefficient of the Renderable.
	 */
	public float scaleX=1;
	/**
	 * The vertical stretch coefficient of the Renderable.
	 */
	public float scaleY=1;
	/**
	 * A boolean stating if the Renderable is to be rendered.
	 */
	public boolean visible=true;
	/**
	 * The parent of the Renderable.
	 */
	public RenderableParent parent;
	private Vector<Renderable> children;
	private AffineTransform oldTransform;
	/**
	 * Makes a new renderable.
	 */
	public Renderable()
	{
		children=new Vector<Renderable>();
	}
	/**
	 * Transforms and then renders the Renderable and its children.
	 */
	public final void render(Graphics g)
	{
		/*
		 * Apply the renderable's transformation.
		 * Then renders the renderable.
		 * The removes the renderable's transformation.
		 */
		//setup
		AffineTransform transformation=new AffineTransform();
		transformation.translate(x, y);
		transformation.rotate(rotation/180*Math.PI);
		transformation.scale(scaleX, scaleY);
		Graphics2D g2d = (Graphics2D)g;
		g2d.transform(transformation);
		//save the transformation
		oldTransform=(AffineTransform)g2d.getTransform().clone();
		//paint
		if(visible){
			paint(g);
			//render children
			for(int n=0;n<children.size();n++){
				children.get(n).render(g);
			}
		}
		//clean up
		try {
			g2d.transform(transformation.createInverse());
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * An abstracted painting method for the Renderable.
	 */
	public void paint(Graphics g)
	{
	}
	
	/**
	 * Transforms a point from the Renderable's coordinate scheme to the global coordinate scheme.
	 */
	public Point localToGlobal(Point po)
	{
		Point point=(Point)po.clone();
		Point2D p=oldTransform.transform(point, null);
		return new Point((int)p.getX(), (int)p.getY());
	}
	
	/**
	 * Transforms a point form the global coordinate scheme to the Renderable's coordinate scheme.
	 */
	public Point globalToLocal(Point po)
	{
		try {
			AffineTransform trans=oldTransform.createInverse();
			Point point=(Point)po.clone();
			Point2D p = trans.transform(point, null);
			return new Point((int)p.getX(), (int)p.getY());
		} catch (NoninvertibleTransformException e) {
			return new Point();
		} catch (NullPointerException e){
			return new Point();
		}
	}	
	
	/**
	 * Adds a child to the Renderable.
	 */
	public Renderable removeChild(Renderable child)
	{
		int index=children.indexOf(child);
		if(index!=-1)
		{
			child.parent=null;
			children.remove(child);
		}
		return child;
	} 
	/**
	 * Removes a child from the Renderable.
	 */
	public Renderable addChild(Renderable child)
	{
		int index=children.indexOf(child);
		if(index==-1)
		{
			child.parent=this;
			children.add(child);
		}
		return child;
	}


}
