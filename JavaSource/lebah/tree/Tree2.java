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
package lebah.tree;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Tree2 extends Tree {
	private Attachment attachment;
	private String attachment_folder;
	private boolean attached = false;
	private String notes;
	public Tree2(String title) {
		super(title);
		attachment = new Attachment();
	}
	public void setAttachment(Attachment attach) {
		attachment = attach;
		attached = true;
	}
	public Attachment getAttachment() { return attachment; }
	public void setNotes(String notes) { this.notes = notes; }
	public String getNotes() { return notes; }
	public boolean isAttached() { return attached; }
	public boolean hasAttachment() { return attached; }
	public void setAttachmentFolder(String s) { attachment_folder = s; }
	public String getAttachmentFolder() { return attachment_folder; }

}
