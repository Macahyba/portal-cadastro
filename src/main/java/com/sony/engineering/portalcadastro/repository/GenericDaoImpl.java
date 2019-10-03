package com.sony.engineering.portalcadastro.repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.stereotype.Repository;

@Repository
public abstract class GenericDaoImpl<T> implements GenericDao<T>{

	@PersistenceContext
	protected EntityManager em;

	private Class<T> classType;
	
	public GenericDaoImpl() {
		
		this.classType = ((Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}
	
	@Override
	public void save(T t) {

		em.persist(t);
	}

	@Override
	public void delete(Integer id) {
		
		em.remove(getOne(id));
	}

	@Override
	public void edit(T t) {

		em.merge(t);
	}

	@Override
	public T getOne(Integer id) {

		return em.find(classType, id);
	}

	@Override
	public List<T> getAll() {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(classType);
		query.from(classType);
		return em.createQuery(query).getResultList();
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	


}
