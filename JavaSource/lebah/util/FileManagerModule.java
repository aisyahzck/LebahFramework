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

package lebah.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.util.Unzip;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.jspsmart.upload.Files;

/**
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class FileManagerModule extends lebah.portal.velocity.VTemplate {
	
    public FileManagerModule() {
        
    }       
    
    public FileManagerModule(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res) {
        super(engine, context, req, res);
    }	

    public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		String template_name = "vtl/admin/file_manager.vm";
		String submit = getParam("command");
		if ( "createfolder".equals(submit) ) {
			createFolder(session);	
		} 
		else if ( "uploadfile".equals(submit) ) {
			uploadFile(session);
		}
		else if ( "uploadpif".equals(submit) ) {
			uploadPIF(session);
		}
		else if ( "delete".equals(submit) ) {
			deleteFile(session);
		} 
		else if ( "deletefiles".equals(submit) ) {
			deleteMultipleFiles(session);
		}		
		else {
			String dir = request.getParameter("dir") != null ? request.getParameter("dir") : "";
			listFiles(session, dir);
		}
		Template template = engine.getTemplate(template_name);	
		return template;		
	}

	protected void deleteMultipleFiles(HttpSession session) throws Exception {
		String current_dir = (String) session.getAttribute("current_dir");
		String[] files = request.getParameterValues("files");
		String[] folders = request.getParameterValues("folders");
		if ( files != null ) {
			for ( int i=0; i < files.length; i++ ) {
				deleteDir(new File(current_dir + "/" + files[i]));
			}
			
		}
		if ( folders != null ) {
			for ( int i=0; i < folders.length; i++ ) {
				deleteDir(new File(current_dir + "/" + folders[i]));
			}				
		}
		String dir = getParam("dir");
		listFiles(session, dir);
	}

	protected void deleteFile(HttpSession session) throws Exception {
		String current_dir = (String) session.getAttribute("current_dir");	
		String name = getParam("file");
		deleteDir(new File(current_dir + "/" + name));
		String dir = getParam("dir");
		
		listFiles(session, dir);
	}
    
    protected void listFiles(HttpSession session, String dir) throws Exception {
        String current_dir = Resource.getPATH() + dir;
        listFilesInDirectory(session, dir, current_dir);
    }
	
	protected void listFilesInDirectory(HttpSession session, String dir, String current_dir) throws Exception {
        session.setAttribute("current_dir", current_dir);
        context.put("current_dir", current_dir);
        context.put("dir", dir);        
		//to determine up dir
		String upDir = "";
		int last = dir.lastIndexOf('/');
		if ( last > -1 ) upDir = dir.substring(0, last);
		context.put("upDir", upDir);
		
        
		Vector dirs = new Vector();
		for ( StringTokenizer st = new StringTokenizer(dir, "/"); st.hasMoreTokens();) {
			dirs.addElement(st.nextToken());
		}
		context.put("dirs", dirs);
		Hashtable goDirTbl = new Hashtable(); 
		for ( int i=0; i < dirs.size(); i++ ) {
			String goDir = "";
			for ( int k = 0; k < i; k++ ) {
				goDir += "/" + (String) dirs.elementAt(k);
			}
			goDir += "/" + (String) dirs.elementAt(i);
			goDirTbl.put(dirs.elementAt(i), goDir);
		}
		context.put("goDirTbl", goDirTbl);
		
		List names = new ArrayList();
		List folders = new ArrayList();
		//System.out.println(current_dir);
		File files[] = new File(current_dir).listFiles();
		if ( files != null ) {
			for ( int i = 0; i < files.length; i++ ) {
				if ( files[i].isDirectory() ) folders.add(files[i].getName());
				else {
					if ( files[i].getName().indexOf("._dat") < 0 ) names.add(files[i].getName());
				}
			}	
		}
		
		Iterator ifolders = folders.iterator();
		context.put("ifolders", ifolders);
		
		Iterator inames = names.iterator();
		context.put("inames", inames);	
	}	
	
	protected void createFolder(HttpSession session) throws Exception {
		String current_dir = (String) session.getAttribute("current_dir");	
		String dir = request.getParameter("dir");
		String foldername = request.getParameter("foldername");
		if ( !foldername.equals("") ) {
			new File(current_dir + "/" + foldername).mkdir();
		}
		listFiles(session, dir);
	}
	
    protected void uploadFile(HttpSession session) throws Exception {
		String current_dir = (String) session.getAttribute("current_dir");	
		//com.jspsmart.upload.SmartUpload myUpload = new com.jspsmart.upload.SmartUpload();
		//myUpload.initialize(getServletConfig(), request, response);
		lebah.upload.SmartUpload2 myUpload = new lebah.upload.SmartUpload2();
		myUpload.initialize2(session.getServletContext(), request, response);			
		myUpload.upload();
		int count2 = myUpload.save( current_dir, myUpload.SAVE_PHYSICAL);
		String dir = getParam("dir");
		listFiles(session, dir);		
	}
	
    protected void uploadPIF(HttpSession session) throws Exception {
		String current_dir = (String) session.getAttribute("current_dir");	
		//com.jspsmart.upload.SmartUpload myUpload = new com.jspsmart.upload.SmartUpload();
		//myUpload.initialize(getServletConfig(), request, response);
		lebah.upload.SmartUpload2 myUpload = new lebah.upload.SmartUpload2();
		myUpload.initialize2(session.getServletContext(), request, response);		
		myUpload.upload();
		int count2 = myUpload.save( current_dir, myUpload.SAVE_PHYSICAL);
		String dir = getParam("dir");
		Files files =myUpload.getFiles();
		if ( files.getCount() > 0 ) {
			com.jspsmart.upload.File file = files.getFile(0);
			Unzip.unzipFiles(current_dir + "/" + file.getFileName(), current_dir);
			deleteDir(new java.io.File(current_dir + "/" + file.getFileName()));
		}
		listFiles(session, dir);
	}
	
		
	
	//recursively delete files and directory
    protected boolean deleteDir(java.io.File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new java.io.File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
	
    protected static class Resource {
		private static ResourceBundle rb;
		private static String PATH;
		private static String CSS;
        private static String APP;
		
		static {
			try {
				rb = ResourceBundle.getBundle("files");
				read();
			} catch ( MissingResourceException e ) {
				System.out.println("MissingResourceException: " + e.getMessage());	
			}
		}

		public static String getPATH() { return PATH; }
        public static String getCSS() { return CSS; }
        public static String getAPP() { return APP; }

		public static void read() {
			readPATH();
            readCSS();
            readAPP();
		}

		private static void readPATH() {
			try {
				PATH = rb.getString("dir");
			} catch ( MissingResourceException e ) { 
				System.out.println("Resource - " + e.getMessage());	
			}
		}
        private static void readCSS() {
            try {
                CSS = rb.getString("css");
            } catch ( MissingResourceException e ) { 
                System.out.println("Resource - " + e.getMessage()); 
            }
        }
        private static void readAPP() {
            try {
                APP = rb.getString("app");
            } catch ( MissingResourceException e ) { 
                System.out.println("Resource - " + e.getMessage()); 
            }
        }        

	}	
			
}
