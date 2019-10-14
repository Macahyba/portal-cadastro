package com.sony.engineering.portalcadastro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.GenericDaoImpl;

@Service
public abstract class GenericServiceImpl<T> implements GenericService<T> {

	@Autowired
	private GenericDaoImpl<T> dao;
	
	public GenericServiceImpl(GenericDao<T> dao) {
		this.dao = (GenericDaoImpl<T>)dao;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T save(T t) {
		dao.save(t);
		return t;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer id) {

		dao.delete(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T edit(T t) {

		dao.edit(t);
		return t;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public T getOne(Integer id) {

		return dao.getOne(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<T> getAll() {

		return dao.getAll();
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<T> getListByAttr(String attribute, String value) {
		
		return dao.getListByAttr(attribute, value);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<T> getAttrList(String attribute) {
		
		return dao.getAttrList(attribute);
	}
	
}
