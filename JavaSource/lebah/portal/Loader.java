/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.portal;

import java.util.Hashtable;


public class Loader{
	
	static Hashtable loadedList = new Hashtable();

	public static Class load(String className) throws Exception {
		Class klazz = null;
		klazz = (Class) loadedList.get(className);
		//if not load it
		if ( klazz == null ) {
			//klazz = Class.forName(className);
			Thread t = Thread.currentThread();
			ClassLoader cl = t.getContextClassLoader();
			klazz = cl.loadClass(className);
			//put into cache
			loadedList.put(className, klazz);
		}
		return klazz;
	}
}
