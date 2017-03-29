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
package lebah.tools;

import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class TreeLayout {
	PrintWriter out;
	HttpServletRequest request;
	String target;
	String pageback;
	String pagego;
	private int counter;

	public TreeLayout(PrintWriter out, HttpServletRequest request) {
		this.out = out;
		this.request = request;
	}
	public TreeLayout(PrintWriter out, HttpServletRequest request, String target) {
		this.out = out;
		this.request = request;
		this.target = target;
	}

	public void setTarget(String target) { this.target = target; }
	public void setPageBack(String pageback) { this.pageback = pageback; }
	public void setPageGo(String pagego) { this.pagego = pagego; }

	public void display(Tree tree) {
    	out.println("<html>");
    	out.println("<head>");
    	out.println("<title></title>");

		out.println("<script>");
		out.println("function showhide(what){");
   		out.println("  if (what.style.display=='none'){");
     	out.println("    what.style.display='';");
   		out.println("  } ");
   		out.println("    else {");
     	out.println("    what.style.display='none'");
		out.println("  }");
		out.println("}");
		out.println("</script>");

    	out.println("</head>");
    	bodyTag();
		out.println("<form name=\"f\" method=\"post\" target=\"" + target + "\">");


		out.println("<table width=500 cellpadding=0 cellspacing=0 border=0>");
		out.println("<tr><td class=\"lev0\">");

		out.println("<span id=\"m1\" style=\"cursor:hand\" onClick=\"showhide(c1)\">");
		out.println("<b>+</b></span>");
		out.println("<a href=\"javascript:goView(" + tree.getId() + ")\">" +
					"<b>" + tree.getTitle() + "</b>");

		out.println("</td></tr></table>");
		out.println("<span id=\"c1\" style=\"display:''\">");
		out.println("<table width=500 cellpadding=0 cellspacing=0 border=0>");
		displayTree(tree);
		out.println("</td></tr></table>");
		out.println("</span>");


		//OTHER MODULES
		out.println("<hr>");
		out.println("<table width=200 cellpadding=0 cellspacing=3 border=0 bgcolor=white>");
		out.println("<tr><td bgcolor=white>");
		out.println("<a href=\"javascript:goForum(" + tree.getId() + ")\"><b>Discussion Board</b></td></tr>");
		//out.println("<tr><td bgcolor=white>");
		//out.println("<a href=\"javascript:goBulletin(" + tree.getId() + ")\"><b>Bulletin</b></td></tr>");
		out.println("</table>");
		out.println("<input type=\"hidden\" name=\"id\">");
		out.println("</form>");
    	out.println("</body>");
		out.println("<script>");
		out.println("function go(url) {");
		out.println("  document.f.action = url;");
		out.println("  document.f.submit();");
		out.println("}");
		out.println("function goView(id) {");
		out.println("  document.f.action = 'info.PageViewTree3';");
		out.println("  document.f.id.value = id;");
		out.println("  document.f.submit();");
		out.println("}");
		out.println("function goForum(id) {");
		out.println("  document.f.action = 'info.PageInfoForum1';");
		out.println("  document.f.id.value = id;");
		out.println("  document.f.submit();");
		out.println("}");
		out.println("</script>");
    	out.println("</html>");
	}


	private void displayTree(Tree tree) {
		Iterator subs = tree.getChildIterator();
		int i = 0;
		while(subs.hasNext()) {
			Tree child = (Tree) subs.next();
			int n = child.getLevel();

			out.println("<tr><td class=\"lev" + n + "\">");
			if (child.hasChild()) {
				for (int k = 0; k < n; k++) {
					out.print("&nbsp;&nbsp;&nbsp;&nbsp;");
				}
				String url = child.getUrl();
				if (url == null) url = "about:blank";
				out.println("<a href=\"javascript:goView(" + child.getId() + ")\">" +
				child.getTitle() +
				"</a>");

				out.println("</td></tr>");
				displayTree(child);
			} else {
				out.println("<tr><td class=\"lev" + n + "\">");
				for (int k = 0; k < n; k++) {
					out.print("&nbsp;&nbsp;&nbsp;&nbsp");
				}
				String url = child.getUrl();
				if (url == null) url = "about:blank";
				out.println("<a href=\"javascript:goView(" + child.getId() + ")\">" +
				child.getTitle() +
				"</a>");
				out.println("</td></tr>");
			}
			i++;
		}
	}

	protected void bodyTag() {
		out.println("<style>");
		out.println("a{color:black; text-decoration:none;}");
		out.println("a:hover{color:maroon;text-decoration:none;}");
		out.println("input {font-family:verdana; font-size:8pt;}");
		out.println("textarea {font-family:verdana; font-size:8pt;}");
		out.println("td {font-family:verdana; font-size:8pt;}");

		out.println("td.title1 {color:white; font-family:verdana; font-size:8pt;font-weight:bold; }");

		out.println("td.lev0 {color:black; font-family:verdana; font-size:10pt;font-weight:bold; background=silver; }");
		out.println("td.lev1 {color:black; font-family:verdana; font-size:8pt;font-weight:bold; background=orange; }");
		out.println("td.lev2 {color:black; font-family:verdana; font-size:8pt;font-weight:bold; background=normal; }");

		out.println("</style>");
		out.println("<body style=\"font-family:verdana; font-size:10pt\" nowrap>");
	}
}
