/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin







but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.listeners;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */

public final class ContextListener implements ServletContextListener, ServletContextAttributeListener {
	private static MessageService msgServ;
	private VelocityEngine engine;
	private VelocityContext context;

	public ContextListener() {}
	
	private Properties loadConfiguration(ServletContext svtContext) throws IOException, FileNotFoundException {
		String propsFile = "/velocity.properties";
		Properties p = new Properties();
        if ( propsFile != null ) {
        	
			String realPath = svtContext.getRealPath(propsFile);
            if ( realPath != null ) propsFile = realPath;
            p.load( new FileInputStream(propsFile) );
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


	public void contextInitialized(ServletContextEvent event) {
    	ServletContext svtContext = event.getServletContext();
		//runMessageService();

		/*
		try {
			Properties p = loadConfiguration(svtContext);
        	engine = new VelocityEngine();
        	context = new VelocityContext();
        	engine.init(p);
			svtContext.setAttribute("VELOCITY_CONTEXT", context);
			svtContext.setAttribute("VELOCITY_ENGINE", engine);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
	}
	
	public void contextDestroyed(ServletContextEvent event)  {
    	stopMessageService();
	}
	
	//Notification that a new attribute was added to the servlet context. 
	public void attributeAdded(ServletContextAttributeEvent event) {
		//System.out.println("a new attribute was added.");
	}
          
	//Notification that an existing attribute has been remved from the servlet context. 
	public void attributeRemoved(ServletContextAttributeEvent event) {
		//System.out.println("an attribute was removed.");
	}          
	
	//Notification that an attribute on the servlet context has been replaced. 
	public void attributeReplaced(ServletContextAttributeEvent event) {
		//System.out.println("An attribute has been replaced.");
	}
	

	
	void runMessageService() {
		msgServ = MessageService.getInstance();
	}
	
	void stopMessageService() {
		if ( msgServ != null ) msgServ.stop();
	}
          
	
}
