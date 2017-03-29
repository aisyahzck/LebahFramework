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
package lebah.portal.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.db.Db;
import lebah.db.DbException;
import lebah.portal.Attributable;
import lebah.portal.velocity.VTemplate;

import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class DatabaseBrowserModule extends VTemplate implements Attributable
{

    private String names[] = {
        "Database Driver", "Connection Url", "User", "Password", "SQL Statement", "Number Of Rows"
    };
    private Hashtable values;
    private String columnNames[];
    private Vector dataVector;
    private int numOfRow;
    HttpSession session;

    public DatabaseBrowserModule()
    {
        values = new Hashtable();
        numOfRow = 5;
    }

    public String[] getNames()
    {
        return names;
    }

    public Hashtable getValues()
    {
        return values;
    }

    public void setValues(Hashtable hashtable)
    {
        values = hashtable;
    }

    public Template doTemplate()
        throws Exception
    {
        session = request.getSession();
        Hashtable hashtable = getValues();
        Hashtable hashtable1 = new Hashtable();
        String dbdriver = (String)hashtable.get(names[0]);
        String dburl = (String)hashtable.get(names[1]);
        String dbuser = (String)hashtable.get(names[2]);
        String dbpassword = (String)hashtable.get(names[3]);
        String sql = (String)hashtable.get(names[4]);
        numOfRow = Integer.parseInt((String)hashtable.get(names[5]));
        hashtable1.put("driver", dbdriver);
        hashtable1.put("url", dburl);
        hashtable1.put("user", dbuser);
        hashtable1.put("password", dbpassword);
        String submit = getParam("command");
        if("next".equals(submit))
        {
            int i = Integer.parseInt((String)session.getAttribute("_db_datavector_last"));
            Vector vector1 = getListData(i);
            context.put("dataVector", vector1);
        } else
        if("previous".equals(submit))
        {
            int j = Integer.parseInt((String)session.getAttribute("_db_datavector_last"));
            int k = Integer.parseInt((String)session.getAttribute("_db_datavector_count"));
            j = j - k - numOfRow;
            Vector vector2 = getListData(j);
            context.put("dataVector", vector2);
        } else
        {
            prepareData(hashtable1, sql);
            Vector vector = getListData(0);
            context.put("columnNames", columnNames);
            context.put("dataVector", vector);
        }
        Template template = engine.getTemplate("vtl/custom/databasebrowser.vm");
        return template;
    }

    private void prepareData(Hashtable hashtable, String s)
        throws Exception
    {
        Db db;
        Exception exception;
        dataVector = new Vector();
        db = null;
        try
        {
            db = new Db(hashtable);
            Statement statement = db.getStatement();
            ResultSet resultset = statement.executeQuery(s);
            ResultSetMetaData resultsetmetadata = resultset.getMetaData();
            int i = resultsetmetadata.getColumnCount();
            columnNames = new String[i + 1];
            columnNames[0] = "No.";
            for(int j = 1; j <= i; j++)
                columnNames[j] = resultsetmetadata.getColumnName(j);

            int k = 0;
            int l;
            for(l = 0; resultset.next(); l++)
            {
                String as[] = new String[i + 1];
                as[0] = Integer.toString(++k);
                for(int i1 = 1; i1 <= i; i1++)
                    as[i1] = resultset.getString(columnNames[i1]);

                dataVector.addElement(as);
            }

            session.setAttribute("_db_datavector", dataVector);
            session.setAttribute("_db_datavector_total", Integer.toString(l));
        }
        catch(DbException dbexception)
        {
            throw new Exception(dbexception.getMessage());
        }
        catch(SQLException sqlexception)
        {
            throw new Exception(sqlexception.getMessage());
        }
        finally
        {
            if(db == null) db.close();
        }
    }

    private Vector getListData(int i)
    {
        if(i == 0)
            context.put("_db_start", "yes");
        else
            context.put("_db_start", "no");
        dataVector = (Vector)session.getAttribute("_db_datavector");
        Vector vector = new Vector();
        int j = i;
        int k;
        for(k = 0; j < dataVector.size() && k < numOfRow; k++)
        {
            vector.addElement(dataVector.elementAt(j));
            j++;
        }

        session.setAttribute("_db_datavector_last", Integer.toString(j));
        session.setAttribute("_db_datavector_count", Integer.toString(k));
        int l = Integer.parseInt((String)session.getAttribute("_db_datavector_total"));
        if(j == l)
            context.put("_db_last", "yes");
        else
            context.put("_db_last", "no");
        return vector;
    }
}
