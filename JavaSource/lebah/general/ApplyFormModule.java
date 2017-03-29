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

public class ApplyFormModule extends VTemplate {

    public Template doTemplate()throws Exception {
        javax.servlet.http.HttpSession httpsession = super.request.getSession();
        String s = "vtl/apply_form/apply.vm";
        String s1 = getParam("command");
        if("send".equals(s1))
        {
            s = "vtl/email_enquiry_send.vm";
            String content = "First Name : " + getParam("firstname") +
            "Last Name : " + getParam("lastname") +
            "Designation : " +  getParam("designation") +
            "Company Name : " +  getParam("companyname") +
            "Street : " + getParam("street") +
            "City : " + getParam("city") +
            "State : " + getParam("stateprovince") +
            "Zip : " + getParam("zippostal") +
            "Email : " + getParam("email") +
            "Telephone : " + getParam("telephone") +
            "Hand Phone : " + getParam("handphone") +
            "Fax : " + getParam("fax");

            String subject = getParam("subject");
            String sendto = getParam("sendto");
            String cc = getParam("cc");
            String bcc = getParam("bcc");
            String host = getParam("host");

            MailService mailservice = new MailService();
            mailservice.setTo(sendto);
            mailservice.setCc(cc);
            mailservice.setBcc(bcc);
            mailservice.setFrom(getParam("email"));
            mailservice.setSubject(subject);
            mailservice.setBody(content);
            mailservice.setHost(host);
            mailservice.send();
        }
        Template template = super.engine.getTemplate(s);
        return template;
    }

}
