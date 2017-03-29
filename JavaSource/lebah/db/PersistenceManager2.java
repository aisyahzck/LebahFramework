package lebah.db;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class PersistenceManager2 extends PersistenceManager {
	
	/*
	private EntityManager em = null;
	private Class klas;
	private Object data;
	private String qs;
	private int pageNum;
	private int pageSize;
	


	public PersistenceManager2() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence");
		em = emf.createEntityManager();
	}

	public void close() {
		em.close();
	}
	
	public void add(Object object) throws Exception {
		em.getTransaction().begin();
		em.persist(object);
		em.getTransaction().commit();
	}	

	public Object find(Class klas, Object id) throws Exception {
		Object object = em.find(klas, id);
		return object;
	}
	
	public PersistenceManager2 find(Class klas) throws Exception {
		this.klas = klas;
		return this;
	}	
	
	public PersistenceManager2 whereId(Object id) throws Exception {
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

	
	public List list(String q, int start, int chunkSize) throws Exception {
        return em.createQuery(q).setFirstResult(start).setMaxResults(chunkSize).getResultList();
    }


	
	
	
	*/

}
