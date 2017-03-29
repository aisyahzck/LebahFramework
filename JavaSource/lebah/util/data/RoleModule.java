package lebah.util.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import lebah.db.Db;
import lebah.db.SQLRenderer;

public class RoleModule {
	
	static String filename = "sql/portal_modules.dat";
	static String outfile = "sql/role_modules.sql";
	
    public static void main(String[] args) throws Exception {
        Db db = null;
        String sql = "";
        SQLRenderer r = new SQLRenderer();
        String fields[] = new String[] {"module_group", "module_title", "module_id", "module_class", "module_description"};
        try {
            //db = new Db();
            //Statement stmt = db.getStatement();
            BufferedReader in = new BufferedReader(new FileReader(filename));
            BufferedWriter out = new BufferedWriter(new FileWriter(outfile));
            String str;
            while ((str = in.readLine()) != null) {
                StringTokenizer tok = new StringTokenizer(str, ",");
                r.clear();
                int i = 0;
                while ( tok.hasMoreTokens() ) {
                    String s = tok.nextToken().trim();
                    if ( "module_class".equals(fields[i]) ) {
                    	
                    	r.add("module_id", s);
                    	r.add("user_role", "root");
                    }
                    i++;
                }
                
                sql = r.getSQLInsert("role_module");
                out.write(sql + ";\n");
                System.out.println(sql);
                //stmt.executeUpdate(sql);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( db != null ) db.close();
        }
    }

}
