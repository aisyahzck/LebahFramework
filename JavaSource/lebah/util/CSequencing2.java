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
package lebah.util;

import lebah.portal.db.PrepareModule;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class CSequencing2 {
	public static void main(String[] args) throws Exception {
		String user = args[0];
		String tab = args[1];
		PrepareModule.fixModuleSequence(user, tab);
	}	
}
