package com.sony.engineering.portalcadastro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.repository.GenericDao;

@Service
public abstract class GenericServiceImpl<T> implements GenericService<T> {

	@Autowired
	public GenericDao<T> dao;
	
	public GenericServiceImpl(GenericDao<T> dao) {
		this.dao = dao;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(T t) {
		
		dao.save(t);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer id) {

		dao.delete(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(T t) {

		dao.edit(t);
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

}
