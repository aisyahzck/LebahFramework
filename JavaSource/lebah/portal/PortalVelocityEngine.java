package lebah.portal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class PortalVelocityEngine {
	
	VelocityEngine engine;
	VelocityContext context;
	ServletConfig servletConfig;
	static PortalVelocityEngine instance;
	
	private PortalVelocityEngine(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
	}
	
	public static PortalVelocityEngine getInstance(ServletConfig servletConfig) {
		if ( instance == null ) {
			instance = new PortalVelocityEngine(servletConfig);
		}
		return instance;
	}
	

	public static Properties loadConfiguration(ServletContext svtContext) throws IOException, FileNotFoundException {
		String propsFile = "/velocity.properties";
		Properties p = new Properties();
        if ( propsFile != null ) {
        	System.out.println("Loading Property File..");
			String realPath = svtContext.getRealPath(propsFile);
            if ( realPath != null ) propsFile = realPath;
            
            System.out.println("---" + propsFile);
            
            p.load( new FileInputStream(propsFile) );
        }
        else {
        	System.out.println("VELOCITY PROPERTY FILE NOT FOUND!");
        }
		/*
        *  first, normalize our velocity log file to be in the 
        *  webapp
        */
		String log = p.getProperty( Velocity.RUNTIME_LOG);
		if (log != null ) {
            log = svtContext.getRealPath( log );
			if (log != null) 
				p.setProperty( Velocity.RUNTIME_LOG, log );
        }
        /*
         *  now, if there is a file loader resource path, treat it the
         *  same way.
         */
        String path = p.getProperty( Velocity.FILE_RESOURCE_LOADER_PATH );
        if ( path != null) {
            path = svtContext.getRealPath(  path );
            if ( path != null) {
                p.setProperty( Velocity.FILE_RESOURCE_LOADER_PATH, path );
            }
        }
        
        //IN DEPLOYMENT ENVIRONMENT MUST REMOVE THIS COMMENT
        p.setProperty( Velocity.FILE_RESOURCE_LOADER_CACHE, "true" );
        
        return p;
    }		

}
