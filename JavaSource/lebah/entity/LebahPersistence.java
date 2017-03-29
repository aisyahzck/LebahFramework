package lebah.entity;

import java.util.*;
import javax.persistence.*;


public class LebahPersistence {
	
	private static EntityManager em;
	
	static {
		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("lebahpersistence");
			em = emf.createEntityManager();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		LebahPersistence p = new LebahPersistence();
	}
	
	
	public EntityManager getEntityManager() {
		return em;
	}
	
	
	public void begin() {
		try {
			em.getTransaction().begin();
		} catch ( Exception e ) {
			System.out.println("Error begin transaction: " + e.getMessage());
			em.getTransaction().rollback();
			em.getTransaction().begin();
		}
	}


	public void commit() throws Exception {
		try {
			em.getTransaction().commit();
		} catch ( Exception e ) {
			e.printStackTrace();
			if ( em.getTransaction().isActive()) {
				System.out.println("...doing ROLLBACK.");
				em.getTransaction().rollback();
			}
		}
	}

	public <T> T find(Class<T> klass, Object id) {
		return (T) em.find(klass, id);
	}
	
	public void persist(Object object) {
		em.persist(object);
	}	
	
	public void rollback() {
		em.getTransaction().rollback(); 
	}
	
	public void remove(Object object) {
		em.remove(object);
	}

	public void executeUpdate(String ql) {
		em.createQuery(ql).executeUpdate();
	}
	
	public List list(String q) {
		List list = em.createQuery(q).getResultList();
		return list;
	}
	
	public List list(String q, int chunkSize) {
        Query query = em.createQuery(q);
        query = query.setFirstResult(0);
        query = query.setMaxResults(chunkSize);
        return query.getResultList();
    }
	
	public List list(String q, Hashtable h) {
		Query query = em.createQuery(q);
		for ( Enumeration e = h.keys(); e.hasMoreElements(); ) {
			String key = (String) e.nextElement();
			Object value = h.get(key);
			query.setParameter(key, value);
		}
		List list = query.getResultList();
		return list;
	}
	
	public List list(String q, int chunkSize, Hashtable h) {
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
	
	public List list(String q, int start, int chunkSize, Hashtable h) {
        Query query = em.createQuery(q);
		for ( Enumeration e = h.keys(); e.hasMoreElements(); ) {
			String key = (String) e.nextElement();
			Object value = h.get(key);
			query.setParameter(key, value);
		}        
        query = query.setFirstResult(start);
        query = query.setMaxResults(chunkSize);
        return query.getResultList();
    }	
	
	public List list(String q, int start, int chunkSize) {
        return em.createQuery(q).setFirstResult(start).setMaxResults(chunkSize).getResultList();
    }
	
	public Object get(String q) {
		List l = list(q);
		return l.size() > 0 ? l.get(0) : null;
	}
	

	
}
