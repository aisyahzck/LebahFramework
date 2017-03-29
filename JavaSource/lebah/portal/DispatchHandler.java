/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

public interface DispatchHandler {
	
	public void process(RequestDispatcher dispatcher) throws Exception;

}
