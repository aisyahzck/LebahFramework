package lebah.db;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.0
 */
public class CopyOfPersistenceManager {
	
	private static EntityManager em = null;
	//private EntityManager em = null;
	private Class klas;
	private Object data;
	private String qs;
	private int pageNum;
	private int pageSize;
	


	static {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence");
		em = emf.createEntityManager();
	}


	/*
	public PersistenceManager() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence");
		em = emf.createEntityManager();
	}
	*/

	

	public void close() {
		//em.close();
	}

	
	/*
	public void add(Object object) throws Exception {
		em.getTransaction().begin();
		em.persist(object);
		em.getTransaction().commit();
	}
	*/
	
	public static void add(Object object) throws Exception {
		em.getTransaction().begin();
		em.persist(object);
		em.getTransaction().commit();
	}	
	

	public Object find(Class klas, Object id) throws Exception {
		Object object = em.find(klas, id);
		return object;
	}
	
	public CopyOfPersistenceManager find(Class klas) throws Exception {
		this.klas = klas;
		return this;
	}	
	
	public CopyOfPersistenceManager whereId(Object id) throws Exception {
		data = find(klas, id);
		return this;
	}
	
	public Object getObject() {
		return data;
	}
	
	public Object forUpdate() throws Exception {
		em.getTransaction().begin();
		return data;
	}
	
	/*
	 * This method only can be called after the
	 * forUpdate() method has been invoked
	 */
	   
	public void update() throws Exception {
		try {
			em.getTransaction().commit();
		} catch ( Exception e ) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	
	public void rollback() throws Exception {
		try {
			em.getTransaction().rollback(); 
		}
		catch ( Exception e ) {
			System.out.println("Persistence Manager rollback ERROR: " + e.getMessage());
		}
	}
	
	
	public void delete(Object object) throws Exception {
		em.getTransaction().begin();
		em.remove(object);
		em.getTransaction().commit();
	}
	
	public void refresh(Object object) throws Exception {
		em.refresh(object);
	}
	
	public void executeUpdate(String ql) throws Exception {
		em.getTransaction().begin();
		em.createQuery(ql).executeUpdate();
		em.getTransaction().commit();
	}
	
	public List list(String q) throws Exception {
		List list = em.createQuery(q).getResultList();
		return list;
	}
	
	public List list(String q, int chunkSize) throws Exception {
        Query query = em.createQuery(q);
        query = query.setFirstResult(0);
        query = query.setMaxResults(chunkSize);
        return query.getResultList();
    }
	
	public List list(String q, Hashtable h) throws Exception {
		Query query = em.createQuery(q);
		for ( Enumeration e = h.keys(); e.hasMoreElements(); ) {
			String key = (String) e.nextElement();
			Object value = h.get(key);
			query.setParameter(key, value);
		}
		List list = query.getResultList();
		return list;
	}
	
	public List list(String q, int chunkSize, Hashtable h) throws Exception {
        Query query = em.createQuery(q);
		for ( Enumeration e = h.keys(); e.hasMoreElements(); ) {
			String key = (String) e.nextElement();
			Object value = h.get(key);
			query.setParameter(key, value);
		}        
        query = query.setFirstResult(0);
        query = query.setMaxResults(chunkSize);
        return query.getResultList();
    }	
	
	/**
	 * Pagination Support
	 * @param q
	 * @param start
	 * @param chunkSize
	 * @return
	 */
	
	public List list(String q, int start, int chunkSize) throws Exception {
        return em.createQuery(q).setFirstResult(start).setMaxResults(chunkSize).getResultList();
    }
	
	
	
	/*
	public List list(int pageNum) throws Exception {
		this.pageNum = pageNum;
		int start = pageNum * pageSize;
		if ( pageNum == 1 ) return list(0, pageSize);
		else return list(start, pageSize);
	}
	
	
	public List list(int start, int chunkSize) throws Exception {
		if ( "".equals(qs) || qs == null ) return null;
		return list(qs, start, chunkSize);
	}


	public String getQuery() {
		return qs;
	}


	public void setQuery(String query) {
		this.qs = query;
	}


	public int getPageNum() {
		return pageNum;
	}


	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int size) {
		this.pageSize = size;
	}

	 */
	
}
