package com.sony.engineering.portalcadastro.repository;

import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public abstract class GenericDaoImpl<T> implements GenericDao<T>{

	@PersistenceContext
	protected EntityManager em;

	protected Class<T> classType;
	
	public GenericDaoImpl() {
		
		this.classType = ((Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	


}
