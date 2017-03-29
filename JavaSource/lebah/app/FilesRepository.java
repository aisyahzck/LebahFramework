/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */
package lebah.app;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class FilesRepository {
	private static ResourceBundle rb;
	private static String uploadDir;
	private static String resourceDir;
	private static String resourcePath;
	
	static {
		try {
			rb = ResourceBundle.getBundle("files");
			read();
		} catch ( MissingResourceException e ) {
			System.out.println(e.getMessage());	
		}
	}

	public static String getUploadDir() { return uploadDir; }
	public static String getResourceDir() { return resourceDir; }
	public static String getResourcePath() { return resourcePath; }

	public static void read() {
		readUploadDir();
		readResourceDir();
		readResourcePath();
	}

	private static void readUploadDir() {
		try {
			uploadDir = rb.getString("uploadDir");
		} catch ( MissingResourceException e ) { 
			System.out.println("Resource - " + e.getMessage());	
		}
	} 
	
	private static void readResourceDir() {
		try {
			resourceDir = rb.getString("resourceDir");
		} catch ( MissingResourceException e ) { 
			System.out.println("Resource - " + e.getMessage());	
		}
	} 	
	
	private static void readResourcePath() {
		try {
			resourcePath = rb.getString("resourcePath");
			
		} catch ( MissingResourceException e ) { 
			System.out.println("Resource - " + e.getMessage());	
		}
	} 	
	
}
