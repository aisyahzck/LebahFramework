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
package lebah.general;

import lebah.app.MailService;
import lebah.portal.velocity.VTemplate;

import org.apache.velocity.Template;
/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class EmailEnquiryModule extends VTemplate
{

    public EmailEnquiryModule()
    {
    }

    public Template doTemplate()
        throws Exception
    {
        //javax.servlet.http.HttpSession httpsession = super.request.getSession();
        String s = "vtl/email_enquiry.vm";
        String s1 = getParam("command");
        if("send".equals(s1))
        {
            s = "vtl/email_enquiry_send.vm";
            String msg = "";
            String s2 = getParam("name");
            String s3 = getParam("contactno");
            String s4 = getParam("address");
            String s5 = getParam("email");
            String s6 = getParam("subject");
            String s7 = getParam("sendto");
            String s8 = getParam("cc");
            String s9 = getParam("bcc");
            String s10 = getParam("comments");
            String s11 = getParam("host");
            //String s12 = s2 + "\n" + s3 + "\n" + s4 + "\n\n\n" + s10 + "\n";
            msg = "------------------------------\n";
            msg = msg + s6 + "\n";
            msg = msg + "------------------------------\n\n";
            msg = msg + " Name: " + s2 + "\n";
            msg = msg + " Contact Number: " + s3 + "\n";
            msg = msg + " Address: " + s4 + "\n";
            msg = msg + " E-mail: " + s5 + "\n";
            msg = msg + " Message: " + s10 + "\n";
            
            MailService mailservice = new MailService();
            mailservice.setTo(s7);
            mailservice.setCc(s8);
            mailservice.setBcc(s9);
            mailservice.setFrom(s5);
            mailservice.setSubject(s6);
            mailservice.setBody(msg);
            mailservice.setHost(s11);
            mailservice.send();
        }
        Template template = super.engine.getTemplate(s);
        return template;
    }
}
