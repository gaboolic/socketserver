package load;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;



/**
 * 用来测试的。。。
 * @author Don'T PH 7.0
 * 2013-12-7
 */
public class LoadTest {
	
	public static void main(String[] args) throws Exception{

		URL urls[] = new URL[1]; 
		urls[0] = new URL("file:/C:/Users/Administrator/Desktop/");
		URLClassLoader loader = new URLClassLoader(urls);
		Class c = loader.loadClass("util.FileUtil");
		System.out.println(c.getName());
		
		loader = new URLClassLoader(urls);
		c = loader.loadClass("util.FileUtil");
		System.out.println(c.getName());
		
		//System.setProperty("java.class.path", "C:/Users/Administrator/Desktop/");  
//		String s = System.getProperty("java.class.path");
//		System.out.println(s);
//		Class.forName("Up7");
		
	}

}
