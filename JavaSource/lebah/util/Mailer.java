/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
package lebah.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Mailer {

	public Mailer() { }

	public String send(String strToAddress, String strFromAddress, String strCcAddress, String strBccAddress, String strMailHost, String strSubject, String strMessage) {

		boolean debug = true;

		// create some properties and get the default Session
		Properties props = System.getProperties();
		props.put("mail.smtp.host", strMailHost);

		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(debug);

		if(strCcAddress.trim().equals("")) {
			strCcAddress = null;
		}

		if(strBccAddress.trim().equals("")) {
			strBccAddress = null;
		}

		if(strSubject.trim().equals("")) {
			strSubject = null;
		}

		try {
			// create a message
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(strFromAddress));

			InternetAddress[] toaddress = {new InternetAddress(strToAddress)};
			msg.setRecipients(Message.RecipientType.TO, toaddress);

			if(strCcAddress != null) {
				InternetAddress[] ccaddress = {new InternetAddress(strCcAddress)};
				msg.setRecipients(Message.RecipientType.CC, ccaddress);
			}

			if(strBccAddress != null) {
				InternetAddress[] bccaddress = {new InternetAddress(strBccAddress)};
				msg.setRecipients(Message.RecipientType.BCC, bccaddress);
			}

			if(strSubject != null) {
				msg.setSubject(strSubject);
			}

			msg.setHeader("X-Mailer", "Yee-haaaw Anonymizer, ver 1.0");
			msg.setText(strMessage);

			// set the Date: header
			msg.setSentDate(new Date());

			// send the message
			Transport.send(msg);

		} catch(Exception ex) {
			return("Unable to send mail : " + ex.toString());
		}

		return("Success sending mail.");
    }

}
