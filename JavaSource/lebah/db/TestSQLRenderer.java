/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.db;

public class TestSQLRenderer {
	
	public static void main(String[] args) {
		SQLRenderer r = new SQLRenderer();
		r
		.add("d.student_id")
		.add("s.name")
		.add("p.program_code")
		.add("p.program_name")
		.add("f.fee_code")
		.add("sum(d.amount) AS amount")
		.add("s.id", r.unquote("d.student_id"))
		.add("sc.program_code", r.unquote("p.program_code"))
		.add("b.student_id", r.unquote("d.student_id"))
		.add("b.bill_date", "2006-01-01", ">=")
		.add("b.bill_date", "2006-12-31", "<=");
		String sql = r.getSQLSelect("student s, student_course sc, student_billing b, student_billing_detail d").concat(" GROUP BY program_code");
		//-
	}

}
