/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

import java.util.Enumeration;
import java.util.Hashtable;

public class ClassLoadManager {
	
	static Hashtable loadedList = new Hashtable();

	public static synchronized Object load(String className, String module, String cacheId) throws Exception {
		if ( className == null ) return null;
		Object object = null;
		String id = className + module + cacheId;
		//find if this name exists in the cache
		object = loadedList.get(id);
		//if not load it   
		if ( object == null ) {
			//Class klazz = Class.forName(className);
			Thread t = Thread.currentThread();
			ClassLoader cl = t.getContextClassLoader();
			Class klazz = cl.loadClass(className);			
			object = klazz.newInstance();	
			//put into cache  
			loadedList.put(id, object);
			//System.out.println(className + " was put into cache =" + cacheId);
		}
		else {
			//System.out.println(className + " GET FROM CACHE = " + cacheId);	
		}
		return object; 
	}	
	
	public static synchronized Object load(String className) throws Exception {
		if ( className == null ) return null;
		Object object = null;
		//if not load it   
		if ( object == null ) {
			//Class klazz = Class.forName(className);
			Thread t = Thread.currentThread();
			ClassLoader cl = t.getContextClassLoader();
			Class klazz = cl.loadClass(className);			
			object = klazz.newInstance();	
		}
		return object; 
	}	
	
//	public static synchronized void status() {
//		int sizeOfLoadedClass = loadedList.size();
//		System.out.println("Loaded classes = " + sizeOfLoadedClass);
//
//		System.out.println("Show all: ");
//	     for (Enumeration e = loadedList.keys() ; e.hasMoreElements() ;) {
//	         System.out.println(e.nextElement());
//	     }
//	}
	
	public static synchronized Hashtable getCaches() {
		return loadedList;
	}
	
	public static synchronized void clearCache() {
		Hashtable copies = new Hashtable();
		copies.putAll(loadedList);
		loadedList.clear();
		for ( Enumeration e = copies.keys(); e.hasMoreElements(); ) {
			String key = (String) e.nextElement();
			Object obj = copies.get(key);
			obj = null;
		}
	}
	
	public static synchronized void clearCache(String cacheId) {
	     for (Enumeration e = loadedList.keys() ; e.hasMoreElements() ;) {
	         String key = (String) e.nextElement();
	         if ( key.lastIndexOf(cacheId) > 0 ) {
	        	 Object obj = loadedList.get(key);
	        	 obj = null;
	        	 loadedList.remove(key);
	         }
	     }
		
	}
	
}
