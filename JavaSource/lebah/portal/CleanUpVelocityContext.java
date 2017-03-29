/**
 * 
 */
package lebah.portal;

import org.apache.velocity.VelocityContext;

/**
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 */
public class CleanUpVelocityContext {
	
	
	public static void run(VelocityContext context, String controllerName) {
		doCleanUp(context, controllerName);
	}
	
	public static void doCleanUp(VelocityContext context, String controllerName) {
		// CLEANUP VELOCITY CONTEXT: BEGIN 
		Object[] objArray = context.getKeys();
		if ( objArray != null ) {
			for(int i = 0; i < objArray.length; i++) {
				context.remove(objArray[i]);
			}
		} else {
			System.out.println("context objects IS NULL");
		}
		// CLEANUP VELOCITY CONTEXT: END
	}
	
	public static void run(VelocityContext context) {
		// CLEANUP VELOCITY CONTEXT: BEGIN 
		Object[] objArray = context.getKeys();
		if ( objArray != null ) {
			for(int i = 0; i < objArray.length; i++) {
				context.remove(objArray[i]);
			}
		} else {
			System.out.println("context objects IS NULL");
		}
		// CLEANUP VELOCITY CONTEXT: END
	}

}
