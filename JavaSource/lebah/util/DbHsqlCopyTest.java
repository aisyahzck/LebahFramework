package lebah.util;

import java.util.List;

import lebah.entity.LebahPersistence;
import lebah.entity.PageCss;

public class DbHsqlCopyTest {
	
	public static void main(String[] args) throws Exception {
		
		LebahPersistence p = new LebahPersistence();
		List<PageCss> list = p.list("select x from PageCss x");
		System.out.println(list.size());
		for ( PageCss x : list ) {
			System.out.println(x.getName() + ", " + x.getTitle());
		}
	}

}
