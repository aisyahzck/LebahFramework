/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or





but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
package lebah.portal.db;

import java.util.Hashtable;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class UserRole {
	public static final String ROOT = "root"; //Root user
	public static final String ADMIN = "admin"; //Administrator user
	public static final String USER = "user"; //Registered portal's user
	public static final String TEACHER = "teacher"; //Registered portal's user
	public static final String STUDENT = "student"; //Registered portal's user
	public static final String ANON = "anon"; //Anonymous user as guest
	
	public static String[] roles = {ANON, STUDENT, TEACHER, USER, ADMIN, ROOT};
	public static String[] roleDescription = {	"Anonymous users (not registered or as guest to portal)",
															"Student",
															"Teacher",
															"Registered users",
															"Administrator users",
															"Root users"
														};
														
	public static Hashtable tbRoles = new Hashtable();
	static {
		for ( int i=0; i < roles.length; i++ ) {
			tbRoles.put(roles[i], roleDescription[i]);	
		}	
	}
	
	public static Hashtable getTbRoles() {
		return tbRoles;	
	}
}
