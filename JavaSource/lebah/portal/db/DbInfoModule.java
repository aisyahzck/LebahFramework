package lebah.portal.db;

import java.io.File;

import javax.servlet.http.HttpSession;

import lebah.util.FilesRepositoryModule.Resource;

import org.apache.velocity.Template;
import lebah.db.*;

public class DbInfoModule extends lebah.portal.velocity.VTemplate {
	
	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		
		Db db = null;
		try {
			db = new Db();
			context.put("dbUrl", db.getConnectionURL());
		} finally {
			if ( db != null ) db.close();
		}

		Template template = engine.getTemplate("vtl/main/dbinfo.vm");	
		return template;		
	}

}
