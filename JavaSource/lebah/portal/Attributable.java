/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public interface Attributable {
	String[] getNames();
	java.util.Hashtable getValues();
	void setValues(java.util.Hashtable hashtable);
}
