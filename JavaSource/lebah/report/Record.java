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

package lebah.report;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Record {
	private String id;
	private RecordHeader header;
	private List items;
	private int col;
	private int itemnum;
	private boolean moreItem;
	private List properties;
	public Record(RecordHeader h) {
		header = h;
		items = new ArrayList();
		properties = new ArrayList();
	}
	public void add(String data) {
		if (col < header.getCols()) {
			items.add(data);
			properties.add(new Property());
			col++;
			moreItem = true;
		}
	}
	public String next() {
		String data = "";
		if (itemnum < header.getCols()) {
			data = (String) items.get(itemnum);
			itemnum++;
			if (itemnum == header.getCols()) moreItem = false;
		}
		return data;
	}
	public boolean hasNext() {
		return moreItem;
	}
	public void setProperty(int i, Property prop) {
		properties.set(i, prop);
	}
	public Property getPropertyAt(int i) {
		return (Property) properties.get(i);
	}
	public void setId(String id) { this.id = id; }
	public String getId() { return this.id; }
	public RecordHeader getHeader() { return header; }
}
