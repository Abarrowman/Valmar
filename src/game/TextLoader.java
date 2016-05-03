package game;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

//documented

/**
 * TextLoader is a simple class that loads a text file's contents into the ram.
 * @author Adam
 */
public class TextLoader {
	/**
	 * Load the text file at the specified url and returns its contents.
	 */
	public static String loadText(URL url)
	{
		String str="";
		//read
		try {
			InputStream in=url.openStream();
			while(true){
				int val=in.read();
				if(val!=-1){
					str+=(char)val;
				}else{
					break;
				}
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Failed to load: "+url.toString());
		}
		return str;
	}
}
