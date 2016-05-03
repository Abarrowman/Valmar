package game;

import java.awt.Image;
import java.awt.MediaTracker;
import java.util.Vector;

//documented

/**
 * ImageCache is a class designed to track and cache images.
 * @author Adam
 */
public class ImageCache {

	private Vector<Image> images;
	private Vector<String> imageNames; 
	private MediaTracker mediaTracker;
	private ValmarRender parent;
	
	/**
	 * Makes an ImageCache with a specified ValmarRender that will load the images.
	 */
	public ImageCache(ValmarRender creator){
		images=new Vector<Image>();
		imageNames=new Vector<String>();
		parent=creator;
		mediaTracker=new MediaTracker(parent.getComponent());
		
	}
	
	
	/**
	 * Loads an image from a specific relative url.
	 * Gives it a name identical to the inputed url.
	 */
	public Image loadImage(String url)
	{
		
		return addImage(parent.getImage(parent.getBase(),url), url);
	}
	
	/**
	 * Loads an image from a specific relative url.
	 * Gives it a specified name.
	 */
	public Image loadImage(String url, String name)
	{
		
		return addImage(parent.getImage(parent.getBase(),url), name);
	}
	
	/**
	 * Adds an image to the ImageCache.
	 */
	public Image addImage(Image image, String name)
	{
		mediaTracker.addImage(image, images.size());
		images.add(image);
		imageNames.add(name);
		return image;
	}
	
	/**
	 * Searches the ImageCache for an image of a given name.
	 */
	public Image getImage(String name)
	{
		
		int index=imageNames.indexOf(name);
		if(index!=-1){
			return images.get(index);
		}else{
			throw new NullPointerException("No image in cache has name'"+name+"'.");
		}
	}
	/**
	 * Searches the ImageCache for the nth image.
	 */
	public Image getImageAt(int n)
	{
		/*
		 * Returns the nth image added to the cache.
		 */
		if(n<images.size()){
			return images.get(n);
		}else{
			throw new NullPointerException("No image in cache is at index'"+n+"'.");
		}
	}
	
	/**
	 * Returns the number of images in the ImageCache.
	 */
	public int getNumberOfImage()
	{
		return images.size();
	}
	
	/**
	 * Returns a boolean specifying if all the images are done loading.
	 */
	public boolean isDone()
	{
		
		int status = mediaTracker.statusAll(true);
		if (status == MediaTracker.COMPLETE) {
			return true;
		} else if (status == MediaTracker.LOADING) {
			return false;
		} else {
		  Object[] errors = mediaTracker.getErrorsAny();
		  if (errors != null && errors.length > 0) {
		    throw new RuntimeException("Failed to load media.");
		  }
		  return false;
		}
	}
	/**
	 * Returns a boolean specifying if an image is done loading.
	 */
	public boolean isDone(int id)
	{
		
		return mediaTracker.checkID(id);
	}
	
}
