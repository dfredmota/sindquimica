package br.developersd3.sindquimica.daos;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

public class GenericDao<T, PK extends Serializable> {

	private EntityManagerFactory entityManagerF;
	
	private EntityManager entityManager;

	protected Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public GenericDao() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		entityManagerF = Persistence.createEntityManagerFactory("entityManagerFactory");
		this.entityClass = ((Class<T>) genericSuperclass.getActualTypeArguments()[0]);
	}
 
	public T save(final T entity) {
		entityManager=entityManagerF.createEntityManager();
		Session session = (Session) entityManager.unwrap(Session.class);
		
		Transaction tx = null;
		
		try{
			
		tx = session.beginTransaction();
				
		session.save(entity);
		session.flush();
		
		tx.commit();
			
		}catch (Exception e) {
			if (tx!=null) tx.rollback();
		     throw e;
		}finally {
			session.close();
		 }
		
		return entity;
	}

	@Transactional
	public T getById(Class classe,final PK id,Integer empresaSistema) {
		entityManager=entityManagerF.createEntityManager();
		Session session = (Session) entityManager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(classe);  
	    criteria.add(Restrictions.eq("id",id));
	    criteria.add(Restrictions.eq("empresaSistema",empresaSistema));
		return (T) criteria.uniqueResult();
	}
	
	@Transactional
	public T getByEmail(Class classe,String email) {
		entityManager=entityManagerF.createEntityManager();
		Session session = (Session) entityManager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(classe);  
	    criteria.add(Restrictions.eq("email",email));
		return (T) criteria.uniqueResult();
	}
	
	@Transactional
	public T getByIdEmpresa(Class classe,final PK id) {
		entityManager=entityManagerF.createEntityManager();
		Session session = (Session) entityManager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(classe);  
	    criteria.add(Restrictions.eq("id",id));
		return (T) criteria.uniqueResult();
	}
	
	public EntityManager getEntityManagerFactory() {
		entityManager=entityManagerF.createEntityManager();
		return entityManager;
	}

	public T update(T entity) {
		entityManager=entityManagerF.createEntityManager();
		Session session = (Session) entityManager.unwrap(Session.class);
		
		Transaction tx = null;
		
	
		try{
			
		tx = session.beginTransaction();
				
		session.merge(entity);
		session.flush();
		
		tx.commit();
			
		}catch (Exception e) {
			if (tx!=null) tx.rollback();
		     throw e;
		}finally {
			session.close();
		 }
		
		return entity;

	}

	public void delete(final T entity) {
		
		try{
		
		entityManager=entityManagerF.createEntityManager();
		Session session = (Session) entityManager.unwrap(Session.class);
		
		Transaction tx = null;
		
		try{
			
		tx = session.beginTransaction();
				
		session.delete(session.contains(entity) ? entity : session.merge(entity));
		session.flush();
		
		tx.commit();
			
		}catch (Exception e) {
			if (tx!=null) tx.rollback();
		     throw e;
		}finally {
			session.close();
		 }
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(Integer empresaSistema) {
		entityManager=entityManagerF.createEntityManager();
		return entityManager.createQuery(("FROM " + getTypeClass().getName()+" where empresaSistema = "+empresaSistema)).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAllSemEmpresa() {
		entityManager=entityManagerF.createEntityManager();
		return entityManager.createQuery(("FROM " + getTypeClass().getName())).getResultList();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	private Class<T> getTypeClass() {
		return entityClass;
	}

	protected Session getSession() {
		entityManager=entityManagerF.createEntityManager();
		return (Session) getEntityManager().getDelegate();
	}

}