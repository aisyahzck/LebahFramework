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

import javax.servlet.http.HttpSession;

import lebah.util.FilesRepositoryModule.Resource;

import org.apache.velocity.Template;

public class FlashBrowseModule  extends FilesRepositoryModule {
	
	public Template doTemplate() throws Exception {
			HttpSession session = request.getSession();
			String template_name = "vtl/files_repository/flash_browse.vm";
			return doTask(session, template_name);		
	}
	
    protected void listFiles(HttpSession session, String dir) throws Exception {
        String current_dir = Resource.getFlashPath() + (!"".equals(dir) ? dir.substring(1) : "");
        context.put("fileUrl", Resource.getImageUrl());
        context.put("fileDir", current_dir);
        listFilesInDirectory(session, dir, current_dir);
    }


}
