package com.sony.engineering.portalcadastro.repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

public abstract class GenericDaoImpl<T> implements GenericDao<T>{

	@PersistenceContext
	protected EntityManager em;

	protected Class<T> classType;
	
	public GenericDaoImpl() {
		
		this.classType = ((Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	
//	public T getOneByAttr(String attribute, String value) {
//
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<T> query = cb.createQuery(classType);
//		Root<T> root = query.from(classType);
//		query.select(root).where(cb.equal(root.get(attribute), value)).distinct(true);
//		try {
//			return getEm().createQuery(query).getSingleResult();
//		} catch (NoResultException nr){
//			return null;
//		}
//	}	
//	
//	public List<T> getListByAttr(String attribute, String value) {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<T> query = cb.createQuery(classType);
//		Root<T> root = query.from(classType);
//		query.select(root).where(cb.equal(root.get(attribute), value)).distinct(true);
//		return getEm().createQuery(query).getResultList();	
//	}

//	@Override
//	public List<T> getAttrList(String attribute){
//		
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<T> query = cb.createQuery(classType);
//		Root<T> equipRoot = query.from(classType);
//		query.select(equipRoot.get(attribute)).distinct(true);
//		return (List<T>)getEm().createQuery(query).getResultList();		
//	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	


}
