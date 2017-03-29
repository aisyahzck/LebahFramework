/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or




This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.util;

import java.io.File;
import java.util.Hashtable;

import javax.servlet.http.HttpSession;

import lebah.util.FileManagerModule.Resource;

public class RepositoryModule extends FilesRepositoryModule implements lebah.portal.Attributable {
	
	//Attributable implementations
	protected String[] names = {"FolderName"};
	protected Hashtable values = new Hashtable();
	
	public String[] getNames() {
		return names;
	}
	
	public Hashtable getValues() {
		return values;
	}
	
	public void setValues(java.util.Hashtable hashtable) {
		values = hashtable;
	}	
	//-- end attributable implementations
	
    protected void listFiles(HttpSession session, String dir) throws Exception {
    	String folderName = values.get(names[0]) != null ? (String) values.get(names[0]) : "x_unspecified";
        String current_dir = Resource.getPATH() + folderName + "/" + dir;
        File file = new File(current_dir);
		if ( !file.exists() ) {
			new File(current_dir).mkdir();
		}
        listFilesInDirectory(session, dir, current_dir);
    }    

}
