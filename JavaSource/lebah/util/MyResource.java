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
import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class MyResource extends ListResourceBundle implements Serializable {
 	public Object[][] getContents() {
 		return contents;
	}
	public static final Object[][] contents = {

		{"directory_banner", "/apps/ias6/ias/APPS/upvoiss/upvoiss/upload/hrdx/banner"},
		{"directory_upload_forum", "/apps/ias6/ias/APPS/upvoiss/upvoiss/upload/hrdx/forum"},
		{"directory_upload_message", "/apps/ias6/ias/APPS/upvoiss/upvoiss/upload/hrdx/message"},
		{"directory_upload_announcement", "/apps/ias6/ias/APPS/upvoiss/upvoiss/upload/hrdx/announcement"},
		{"directory_upload_bulletin", "/apps/ias6/ias/APPS/upvoiss/upvoiss/upload/hrdx/bulletin"},
		{"directory_upload_coursenote", "/apps/ias6/ias/APPS/upvoiss/upvoiss/upload/hrdx/coursenote"},
		{"directory_upload_g_ann", "/apps/ias6/ias/APPS/upvoiss/upvoiss/upload/hrdx/g_ann"},
		{"directory_upload_g_bull", "/apps/ias6/ias/APPS/upvoiss/upvoiss/upload/hrdx/g_bull"},
		{"directory_upload_assignment", "/apps/ias6/ias/APPS/upvoiss/upvoiss/upload/hrdx/assignment"},

		{"url_upload_forum", "http://10.1.1.105/NASApp/siber/uploads/forum/"},
		{"url_upload_message", "http://10.1.1.105/NASApp/siber/uploads/message/"},
		{"url_upload_announcement", "http://10.1.1.105/NASApp/siber/uploads/announcement"},
		{"url_upload_bulletin", "http://10.1.1.105/NASApp/siber/uploads/bulletin/"},
		{"url_upload_coursenote", "http://10.1.1.105/NASApp/siber/uploads/bulletin/"},
		/*

		{"directory_banner", "D:/Apache Tomcat 4.0/webapps/siber/upload/banner"},
		{"directory_upload_forum", "D:/Apache Tomcat 4.0/webapps/siber/upload/forum"},
		{"url_upload_forum", "http://sbahrin:8080/siber/upload/forum/"},
		{"directory_upload_message", "D:/Apache Tomcat 4.0/webapps/siber/upload/message"},
		{"url_upload_message", "http://sbahrin:8080/siber/upload/message/"},
		{"directory_upload_announcement", "D:/Apache Tomcat 4.0/webapps/siber/upload/announcement"},
		{"url_upload_announcement", "http://sbahrin:8080/siber/upload/announcement"},
		{"directory_upload_bulletin", "D:/Apache Tomcat 4.0/webapps/siber/upload/bulletin"},
		{"url_upload_bulletin", "http://sbahrin:8080/siber/upload/bulletin/"},
		*/
	};
}

