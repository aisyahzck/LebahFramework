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
package lebah.mail;

import javax.mail.*;

/**
 * 
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class MsgStorer {
	private MsgStorer instance;
	private String user;
	private Message message;
	
	private MsgStorer(String user) {
		this.user = user;
	}
	
	public MsgStorer getInstance(String user) {
		instance = new MsgStorer(user);
		return instance;
	}	
	
	public void setMessage(Message m) {
		this.message = m;
	}
	
	public Message getMessage() {
		return message;
	}

}
